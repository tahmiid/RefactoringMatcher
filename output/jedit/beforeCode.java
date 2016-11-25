{
		//{{{ Debug code
		if(Debug.SCROLL_DEBUG)
		{
			Log.log(Log.DEBUG,this,"changed() before: "
				+ physicalLine + ':' + scrollLine
				+ ':' + skew);
		} //}}}

		ensurePhysicalLineIsVisible();

		int screenLines = displayManager
			.getScreenLineCount(physicalLine);
		if(skew >= screenLines)
			skew = screenLines - 1;

		//{{{ Debug code
		if(Debug.SCROLL_VERIFY)
		{
			System.err.println("SCROLL_VERIFY");
			int verifyScrollLine = 0;

			for(int i = 0; i < displayManager.getBuffer()
				.getLineCount(); i++)
			{
				if(!displayManager.isLineVisible(i))
					continue;

				if(i >= physicalLine)
					break;

				verifyScrollLine += displayManager
					.getScreenLineCount(i);
			}

			if(verifyScrollLine != scrollLine)
			{
				Exception ex = new Exception(scrollLine + ":" + verifyScrollLine);
				Log.log(Log.ERROR,this,ex);
			}
		}

		if(Debug.SCROLL_DEBUG)
		{
			Log.log(Log.DEBUG,this,"changed() after: "
				+ physicalLine + ':' + scrollLine
				+ ':' + skew);
		} //}}}
	}
{
		editPane.setBuffer(buffer, focus);
		int check = jEdit.getIntegerProperty("checkFileStatus");
		if(!disableFileStatusCheck && (check == GeneralOptionPane.checkFileStatus_all ||
						  check == GeneralOptionPane.checkFileStatus_operations ||
						  check == GeneralOptionPane.checkFileStatus_focusBuffer))
			jEdit.checkBufferStatus(this, true);
	}
{
		OutputStream out = null;

		try
		{
			String[] args = { vfs.getFileName(path) };
			setStatus(jEdit.getProperty("vfs.status.autosave",args));

			// the entire save operation can be aborted...
			setAbortable(true);

			try
			{
				//buffer.readLock();

				if(!buffer.isDirty())
				{
					// buffer has been saved while we
					// were waiting.
					return;
				}

				out = vfs._createOutputStream(session,path,view);
				if(out == null)
					return;

				write(buffer,out);
			}
			catch (FileNotFoundException e)
			{
				Log.log(Log.WARNING,this,"Unable to save " + e.getMessage());
			}
			catch(Exception e)
			{
				Log.log(Log.ERROR,this,e);
				String[] pp = { e.toString() };
				VFSManager.error(view,path,"ioerror.write-error",pp);

				// Incomplete autosave file should not exist.
				if(out != null)
				{
					try
					{
						out.close();
						out = null;
						vfs._delete(session,path,view);
					}
					catch(IOException ioe)
					{
						Log.log(Log.ERROR,this,ioe);
					}
				}
			}
			//finally
			//{
				//buffer.readUnlock();
			//}
		}
		catch(WorkThread.Abort a)
		{
		}
		finally
		{
			IOUtilities.closeQuietly(out);
		}
	}
{
		super(new BorderLayout());

		listenerList = new EventListenerList();

		this.mode = mode;
		this.multipleSelection = multipleSelection;
		this.view = view;


		currentEncoding = null;
		autoDetectEncoding = jEdit.getBooleanProperty(
			"buffer.encodingAutodetect");

		ActionHandler actionHandler = new ActionHandler();

		topBox = new Box(BoxLayout.Y_AXIS);
		horizontalLayout = mode != BROWSER
			|| DockableWindowManager.TOP.equals(position)
			|| DockableWindowManager.BOTTOM.equals(position);

		toolbarBox = new Box(horizontalLayout
			? BoxLayout.X_AXIS
			: BoxLayout.Y_AXIS);

		topBox.add(toolbarBox);

		GridBagLayout layout = new GridBagLayout();
		pathAndFilterPanel = new JPanel(layout);
		if(isHorizontalLayout())
			pathAndFilterPanel.setBorder(new EmptyBorder(12,12,12,12));

		GridBagConstraints cons = new GridBagConstraints();
		cons.gridwidth = cons.gridheight = 1;
		cons.gridx = cons.gridy = 0;
		cons.fill = GridBagConstraints.BOTH;
		cons.anchor = GridBagConstraints.EAST;
		JLabel label = new JLabel(jEdit.getProperty("vfs.browser.path"),
			SwingConstants.RIGHT);
		label.setBorder(new EmptyBorder(0,0,0,12));
		layout.setConstraints(label,cons);
		pathAndFilterPanel.add(label);

		pathField = new HistoryTextField("vfs.browser.path");
		pathField.setName("path");
		pathField.setInstantPopups(true);
		pathField.setEnterAddsToHistory(false);
		pathField.setSelectAllOnFocus(true);


		// because its preferred size can be quite wide, we
		// don't want it to make the browser way too big,
		// so set the preferred width to 0.
		Dimension prefSize = pathField.getPreferredSize();
		prefSize.width = 0;
		pathField.setPreferredSize(prefSize);
		pathField.addActionListener(actionHandler);
		cons.gridx = 1;
		cons.weightx = 1.0;
		cons.gridwidth = GridBagConstraints.REMAINDER;

		layout.setConstraints(pathField,cons);
		pathAndFilterPanel.add(pathField);

		filterCheckbox = new JCheckBox(jEdit.getProperty("vfs.browser.filter"));
		filterCheckbox.setMargin(new Insets(0,0,0,0));
//		filterCheckbox.setRequestFocusEnabled(false);
		filterCheckbox.setBorder(new EmptyBorder(0,0,0,12));
		filterCheckbox.setSelected(jEdit.getBooleanProperty(
			"vfs.browser.filter-enabled"));

		filterCheckbox.addActionListener(actionHandler);
		filterCheckbox.setName("filter-checkbox");
		if(mode != CHOOSE_DIRECTORY_DIALOG)
		{
			cons.gridwidth = 1;
			cons.gridx = 0;
			cons.weightx = 0.0;
			cons.gridy = 1;
			layout.setConstraints(filterCheckbox,cons);
			pathAndFilterPanel.add(filterCheckbox);
		}

		filterField = new JComboBox();
		filterEditor = new HistoryComboBoxEditor("vfs.browser.filter");
		filterEditor.setToolTipText(jEdit.getProperty("glob.tooltip"));
		filterEditor.setInstantPopups(true);
		filterEditor.setSelectAllOnFocus(true);
		filterEditor.addActionListener(actionHandler);
		filterField.setName("filter-field");
		if (mode == BROWSER)
		{
			DockableWindowManager dwm = view.getDockableWindowManager();
			KeyListener keyListener = dwm.closeListener(NAME);
			filterCheckbox.addKeyListener(keyListener);
			addKeyListener(keyListener);
			filterEditor.addKeyListener(keyListener);
			pathField.addKeyListener(keyListener);
			// save the location on close of dockable.
			pathField.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyReleased(KeyEvent e)
				{
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					{
						pathField.setText(VFSBrowser.this.path);
					}
				}
			});
		}

		String filter;
		if(mode == BROWSER || !jEdit.getBooleanProperty(
			"vfs.browser.currentBufferFilter"))
		{
			filter = jEdit.getProperty("vfs.browser.last-filter");
			if(filter == null)
				filter = jEdit.getProperty("vfs.browser.default-filter");
		}
		else
		{
			String ext = MiscUtilities.getFileExtension(
				view.getBuffer().getName());
			if(ext.length() == 0)
				filter = jEdit.getProperty("vfs.browser.default-filter");
			else
				filter = '*' + ext;
		}

		// filterField.getEditor().setItem(new GlobVFSFileFilter(filter));
		// filterField.addItem(filterField.getEditor().getItem());
		filterEditor.setItem(new GlobVFSFileFilter(filter));
		filterField.addItem(filterEditor.getItem());
		filterField.addItemListener(actionHandler);
		filterField.setRenderer(new VFSFileFilterRenderer());

		// loads the registered VFSFileFilter services.
		String[] _filters = ServiceManager.getServiceNames(VFSFileFilter.SERVICE_NAME);
		for (int i = 0; i < _filters.length; i++)
		{
			VFSFileFilter _filter = (VFSFileFilter)
				ServiceManager.getService(VFSFileFilter.SERVICE_NAME, _filters[i]);
			filterField.addItem(_filter);
		}

		if(mode != CHOOSE_DIRECTORY_DIALOG)
		{
			cons.gridwidth = GridBagConstraints.REMAINDER;
			cons.fill = GridBagConstraints.HORIZONTAL;
			cons.gridx = 1;
			cons.weightx = 1.0;
			if (filterField.getItemCount() > 1)
			{
				filterField.setEditor(filterEditor);
				filterField.setEditable(true);
				layout.setConstraints(filterField,cons);
				pathAndFilterPanel.add(filterField);
			}
			else
			{
				layout.setConstraints(filterEditor,cons);
				pathAndFilterPanel.add(filterEditor);
			}
		}

		topBox.add(pathAndFilterPanel);
		add(BorderLayout.NORTH,topBox);

		add(BorderLayout.CENTER,browserView = new BrowserView(this));
		if(isHorizontalLayout())
			browserView.setBorder(new EmptyBorder(0,12,0,12));
		defaultFocusComponent = browserView.getTable();
		propertiesChanged();

		updateFilterEnabled();

		setFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
		// see VFSBrowser.browseDirectory()
		if(path == null)
			path = jEdit.getProperty("vfs.browser.path.tmp");

		if(path == null || path.isEmpty())
		{
			String userHome = System.getProperty("user.home");
			String defaultPath = jEdit.getProperty("vfs.browser.defaultPath");
			if("home".equals(defaultPath))
				path = userHome;
			else if("working".equals(defaultPath))
				path = System.getProperty("user.dir");
			else if("buffer".equals(defaultPath))
			{
				Buffer buffer = view.getBuffer();
				boolean browseable = (buffer.getVFS().getCapabilities() & VFS.BROWSE_CAP) != 0;
				if (browseable)
					path = buffer.getDirectory();
			}
			else if("last".equals(defaultPath))
			{
				HistoryModel pathModel = HistoryModel.getModel("vfs.browser.path");
				if(pathModel.getSize() == 0)
					path = "~";
				else
					path = pathModel.getItem(0);
			}
			else if("favorites".equals(defaultPath))
				path = "favorites:";

			if (path == null || path.isEmpty())
			{
				// unknown value??!!!
				path = userHome;
			}
		}

		final String _path = path;

		ThreadUtilities.runInDispatchThread(new Runnable()
		{
			@Override
			public void run()
			{
				setDirectory(_path);
			}
		});
	}
{
		if ((pluginList == null) || (pluginList.plugins == null)) return;
		// for each plugin that is installed
		for (PluginJAR jar: jEdit.getPluginJARs()) 
		{
			EditPlugin eplugin = jar.getPlugin();
			if(eplugin == null) continue;
			String installedVersion = jEdit.getProperty("plugin." + eplugin.getClassName() + ".version");					 
			// find corresponding entry in pluginList
			for (Plugin plugin: pluginList.plugins) 
			{
				if (MiscUtilities.pathsEqual(plugin.jar, MiscUtilities.getFileName(jar.getPath()))) 
				{
					// Find the branch we are using in that list
					for (Branch branch: plugin.branches) 
						if (branch.version.equals(installedVersion)) 
						{
							for (Dependency dep: branch.deps) 
								if (dep.what.equals("jedit") && (dep.to != null)) 
									if (StandardUtilities.compareStrings(jEdit.getBuild(), dep.to, false) > 0) 
									{ 
										Log.log(Log.ERROR, this, "Plugin: " + plugin.name + " " + installedVersion +
											" is not supported on this version of jEdit! Disabling plugin.");
										jEdit.removePluginJAR(jar,false);
										// TODO: properly mark it red or completely remove it?
										String jarName = MiscUtilities.getFileName(jar.getPath());
										jEdit.setBooleanProperty("plugin-blacklist."+ jarName,true);
										jEdit.setBooleanProperty("plugin." + jarName + ".disabled", true);
										jEdit.propertiesChanged();
									}
						}
				}
			}		
		}
	}
{
		int len = str.length();
		moveGapStart(start);
		if(gapEnd - gapStart < len)
		{
			ensureCapacity(length + len + 1024);
			moveGapEnd(start + len + 1024);
		}

		str.getChars(0,len,text,start);
		gapStart += len;
		length += len;
	}
{
		// compare existing entries with this
		int length = (wrap ? ring.length : count);
		for(int i = 0; i < length; i++)
		{
			if(ring[i].str.equals(rem.str))
			{
				// we don't want duplicate entries
				// in the kill ring
				return;
			}
		}

		// no duplicates, check for all-whitespace string
		boolean allWhitespace = true;
		for(int i = 0; i < rem.str.length(); i++)
		{
			if(!Character.isWhitespace(rem.str.charAt(i)))
			{
				allWhitespace = false;
				break;
			}
		}

		if(allWhitespace)
			return;

		ring[count] = rem;
		if(++count >= ring.length)
		{
			wrap = true;
			count = 0;
		}
	}
{
		if ("off".equals(keysMode))
			return false;

		if (electricKeys == null)
		{
			String prop = (String) getProperty("electricKeys");
			electricKeys = ( prop == null ? "" : prop );

			String[] props = {
				"indentOpenBrackets",
				"indentCloseBrackets"
			};

			StringBuilder buf = new StringBuilder();
			for(int i = 0; i < props.length; i++)
			{
				prop = (String) getProperty(props[i]);
				if (prop != null)
					buf.append(prop);
			}
			electricBrackets = buf.toString();
		}

		boolean isElectric1 = (electricBrackets.indexOf(ch) >= 0);
		boolean isElectric2 = (!"brackets only".equals(keysMode)
			&& (electricKeys.indexOf(ch) >= 0 ));
		return isElectric1 || isElectric2;
	}
{
		if (electricKeys == null)
		{
			String[] props = {
				"indentOpenBrackets",
				"indentCloseBrackets",
				"electricKeys"
			};

			StringBuilder buf = new StringBuilder();
			for(int i = 0; i < props.length; i++)
			{
				String prop = (String) getProperty(props[i]);
				if (prop != null)
					buf.append(prop);
			}

			electricKeys = buf.toString();
		}

		return (electricKeys.indexOf(ch) >= 0);
	}
{
		if(backupPrefix == null)
			backupPrefix = "";
		if(backupSuffix == null)
			backupSuffix = "";

		String name = file.getName();

		// If backups is 1, create ~ file
		if(backups == 1)
		{
			File backupFile = new File(backupDirectory,
				backupPrefix + name + backupSuffix);
			long modTime = backupFile.lastModified();
			/* if backup file was created less than
			 * 'backupTimeDistance' ago, we do not
			 * create the backup */
			if(System.currentTimeMillis() - modTime
			   >= backupTimeDistance)
			{
				Log.log(Log.DEBUG,MiscUtilities.class,
					"Saving backup of file \"" +
					file.getAbsolutePath() + "\" to \"" +
					backupFile.getAbsolutePath() + '"');
				backupFile.delete();
				if (!file.renameTo(backupFile))
					IOUtilities.moveFile(file, backupFile);
			}
		}
		// If backups > 1, move old ~n~ files, create ~1~ file
		else
		{
			/* delete a backup created using above method */
			new File(backupDirectory,
				backupPrefix + name + backupSuffix
				+ backups + backupSuffix).delete();

			File firstBackup = new File(backupDirectory,
				backupPrefix + name + backupSuffix
				+ '1' + backupSuffix);
			long modTime = firstBackup.lastModified();
			/* if backup file was created less than
			 * 'backupTimeDistance' ago, we do not
			 * create the backup */
			if(System.currentTimeMillis() - modTime
			   >= backupTimeDistance)
			{
				for(int i = backups - 1; i > 0; i--)
				{
					File backup = new File(backupDirectory,
						backupPrefix + name
						+ backupSuffix + i
						+ backupSuffix);

					backup.renameTo(new File(backupDirectory,
						backupPrefix + name
						+ backupSuffix + (i + 1)
						+ backupSuffix));
				}

				File backupFile = new File(backupDirectory,
					backupPrefix + name + backupSuffix
					+ '1' + backupSuffix);
				Log.log(Log.DEBUG,MiscUtilities.class,
					"Saving backup of file \"" +
					file.getAbsolutePath() + "\" to \"" +
					backupFile.getAbsolutePath() + '"');
				if (!file.renameTo(backupFile))
					IOUtilities.moveFile(file, backupFile);
			}
		}
	}
{
		if(backupPrefix == null)
			backupPrefix = "";
		if(backupSuffix == null)
			backupSuffix = "";

		String name = file.getName();

		// If backups is 1, create ~ file
		if(backups == 1)
		{
			File backupFile = new File(backupDirectory,
				backupPrefix + name + backupSuffix);
			long modTime = backupFile.lastModified();
			/* if backup file was created less than
			 * 'backupTimeDistance' ago, we do not
			 * create the backup */
			if(System.currentTimeMillis() - modTime
			   >= backupTimeDistance)
			{
				Log.log(Log.DEBUG,MiscUtilities.class,
					"Saving backup of file \"" +
					file.getAbsolutePath() + "\" to \"" +
					backupFile.getAbsolutePath() + '"');
				backupFile.delete();
				if (!file.renameTo(backupFile))
					IOUtilities.moveFile(file, backupFile);
			}
		}
		// If backups > 1, move old ~n~ files, create ~1~ file
		else
		{
			/* delete a backup created using above method */
			new File(backupDirectory,
				backupPrefix + name + backupSuffix
				+ backups + backupSuffix).delete();

			File firstBackup = new File(backupDirectory,
				backupPrefix + name + backupSuffix
				+ '1' + backupSuffix);
			long modTime = firstBackup.lastModified();
			/* if backup file was created less than
			 * 'backupTimeDistance' ago, we do not
			 * create the backup */
			if(System.currentTimeMillis() - modTime
			   >= backupTimeDistance)
			{
				for(int i = backups - 1; i > 0; i--)
				{
					File backup = new File(backupDirectory,
						backupPrefix + name
						+ backupSuffix + i
						+ backupSuffix);

					backup.renameTo(new File(backupDirectory,
						backupPrefix + name
						+ backupSuffix + (i + 1)
						+ backupSuffix));
				}

				File backupFile = new File(backupDirectory,
					backupPrefix + name + backupSuffix
					+ '1' + backupSuffix);
				Log.log(Log.DEBUG,MiscUtilities.class,
					"Saving backup of file \"" +
					file.getAbsolutePath() + "\" to \"" +
					backupFile.getAbsolutePath() + '"');
				if (!file.renameTo(backupFile))
					IOUtilities.moveFile(file, backupFile);
			}
		}
	}
{
		final Frame frame = JOptionPane.getFrameForComponent(comp);

		synchronized(errorLock)
		{
			error = true;

			errors.add(new ErrorListDialog.ErrorEntry(
				path,messageProp,args));

			if(errors.size() == 1)
			{
				if (!errorDisplayerActive)
					ThreadUtilities.runInBackground(new ErrorDisplayer(frame));
			}
		}
	}
{
		String language = null;
		if (getBooleanProperty("lang.usedefaultlocale"))
		{
			language = Locale.getDefault().getLanguage();
		}
		else
		{
			language = getProperty("lang.current", "en");
		}
		InputStream langResource = null;
		try
		{
			langResource = jEdit.class.getResourceAsStream("/org/jedit/localization/jedit_" + language + ".props");
			propMgr.loadLocalizationProps(langResource);
		}
		catch (IOException e)
		{
			if (getBooleanProperty("lang.usedefaultlocale"))
			{
				// if it is the default locale, it is not an error
				Log.log(Log.ERROR, jEdit.class, "Unable to load language", e);
			}
		}
		finally
		{
			IOUtilities.closeQuietly(langResource);
		}
	}
{
		// FIXME: Need BiDi support.
		int layoutFlags = Font.LAYOUT_LEFT_TO_RIGHT
			| Font.LAYOUT_NO_START_CONTEXT
			| Font.LAYOUT_NO_LIMIT_CONTEXT;

		GlyphVector gv = font.layoutGlyphVector(frc,
						     text,
						     start,
						     end,
						     layoutFlags);

		// This is necessary to work around a memory leak in Sun Java 6 where
		// the sun.font.GlyphLayout is cached and reused while holding an
		// instance to the char array.
		font.layoutGlyphVector(frc, EMPTY_TEXT, 0, 0, layoutFlags);

		glyphs.add(gv);
		return (float) gv.getLogicalBounds().getWidth();
	}
{
		if(str == null)
			return;

		int len = str.length();

		if(len == 0)
			return;

		if(isReadOnly())
			throw new RuntimeException("buffer read-only");

		try
		{
			writeLock();

			if(offset < 0 || offset > contentMgr.getLength())
				throw new ArrayIndexOutOfBoundsException(offset);

			contentMgr.insert(offset,str);

			integerArray.clear();

			for(int i = 0; i < len; i++)
			{
				if(str.charAt(i) == '\n')
					integerArray.add(i + 1);
			}

			if(!undoInProgress)
			{
				undoMgr.contentInserted(offset,len,str,!dirty);
			}

			contentInserted(offset,len,integerArray);
		}
		finally
		{
			writeUnlock();
		}
	}
{
		Chunk lineHead = (Chunk)firstToken;
		boolean seenNonWhitespace = false;
		float endOfWhitespace = 0.0f;
		float x = 0.0f;
		Chunk end = null;
		float endX = 0.0f;
		for (Chunk chunk = lineHead; chunk != null; chunk = (Chunk)chunk.next)
		{
			initChunk(chunk, x, lineText);
			x += chunk.width;
			if(Character.isWhitespace(lineText.array[
				lineText.offset + chunk.offset]))
			{
				if(seenNonWhitespace)
				{
					end = chunk;
					endX = x;
				}
				else
					endOfWhitespace = x;
			}
			else
			{
				if(x > wrapMargin
					&& end != null
					&& seenNonWhitespace)
				{
					Chunk nextLine = new Chunk(endOfWhitespace,
						end.offset + end.length, end.rules);
					initChunk(nextLine, x, lineText);
					nextLine.next = end.next;
					end.next = null;
					out.add(mergeAdjucentChunks(lineHead,lineText));
					lineHead = nextLine;
					x = x - endX + endOfWhitespace;
					end = null;
					endX = x;
				}
				seenNonWhitespace = true;
			}
		}
		out.add(mergeAdjucentChunks(lineHead,lineText));
	}
{
		initialized = true;
		if(!isAccessible())
		{
			// do nothing
		}
		else if(length == 1 && lineText.array[lineText.offset + offset] == '\t')
		{
			float newX = expander.nextTabStop(x,physicalLineOffset+offset);
			width = newX - x;
		}
		else
		{
			str = new String(lineText.array,lineText.offset + offset,length);

			char[] textArray = lineText.array;
			int textStart = lineText.offset + offset;
			width = layoutGlyphs(fontRenderContext,
					     textArray,
					     textStart,
					     textStart + length);
		}
	}
{
			WidgetSelectionDialog dialog = new WidgetSelectionDialog(StatusBarOptionPane.this);
			String value = dialog.getValue();
			if (value != null && value.length() == 0)
				value = null;
			return value;
		}
{
		float width = 0.0f;
		int max = 0;
		Font dflt = style.getFont();

		glyphs = new LinkedList<GlyphVector>();

		while (max != -1 && start < end)
		{
			max = fontSubstEnabled ? dflt.canDisplayUpTo(text, start, end)
			                       : -1;
			if (max == -1)
			{
				width += addGlyphVector(dflt,
							frc,
							text,
							start,
							end);
			}
			else
			{
				/*
				 * Draw as much as we can and update the
				 * current offset.
				 */
				if (max > start)
				{
					width += addGlyphVector(dflt,
								frc,
								text,
								start,
								max);
					start = max;
				}

				/*
				 * Find a font that can display the next
				 * characters.
				 */
				Font f = null;
				for (Font candidate : getFonts())
				{
					 if (candidate.canDisplay(text[start]))
					 {
						 f = candidate;
						 break;
					 }
				}

				if (f != null)
				{
					f = f.deriveFont(dflt.getStyle(), dflt.getSize());

					/*
					 * Find out how many characters
					 * the current font cannot
					 * display, but the chosen one
					 * can.
					 */
					int last = start;
					while (last < end &&
					       f.canDisplay(text[last]) &&
					       !dflt.canDisplay(text[last]))
						last++;

					width += addGlyphVector(f,
								frc,
								text,
								start,
								last);

					start = last;
				}
				else
				{
					width += addGlyphVector(dflt,
								frc,
								text,
								start,
								start + 1);
					start++;
				}
			}
		}
		return width;
	}
{
		String name = getFileName(path);
		int index = name.indexOf('.');
		if(index == -1)
			return name;
		else
			return name.substring(0,index);
	}
{
		if (progress != null)
			progress.setStatus("Initializing");

		InputStream in = null;
		OutputStream out = null;
		try
		{
			VFSFile sourceVFSFile = sourceVFS._getFile(sourceSession, sourcePath, comp);
			if (sourceVFSFile == null)
				throw new FileNotFoundException("source path " + sourcePath + " doesn't exists");
			if (progress != null)
			{
				progress.setMaximum(sourceVFSFile.getLength());
			}
			VFSFile targetVFSFile = targetVFS._getFile(targetSession, targetPath, comp);
			if (targetVFSFile == null)
			{
				String parentTargetPath = MiscUtilities.getParentOfPath(targetPath);
				VFSFile parentTargetVFSFile = targetVFS._getFile(targetSession, parentTargetPath, comp);
				if (parentTargetVFSFile == null)
					throw new FileNotFoundException("target path " + parentTargetPath +
						" doesn't exists");
				if (parentTargetVFSFile.getType() == VFSFile.DIRECTORY)
				{
					String targetFilename = MiscUtilities.getFileName(targetPath);
					targetPath = MiscUtilities.constructPath(parentTargetPath, targetFilename);
				}
				else
				{
					throw new IOException("The parent of target path is a file");
				}
			}
			else if (targetVFSFile.getType() == VFSFile.DIRECTORY)
			{
				if (targetVFSFile.getPath().equals(sourceVFSFile.getPath()))
					return false;
				targetPath = MiscUtilities.constructPath(targetPath, sourceVFSFile.getName());
			}
			in = new BufferedInputStream(sourceVFS._createInputStream(sourceSession, sourcePath, false, comp));
			out = new BufferedOutputStream(targetVFS._createOutputStream(targetSession, targetPath, comp));
			boolean copyResult = IOUtilities.copyStream(IOBUFSIZE, progress, in, out, canStop);
			VFSManager.sendVFSUpdate(targetVFS, targetPath, true);
			return copyResult;
		}
		finally
		{
			IOUtilities.closeQuietly(in);
			IOUtilities.closeQuietly(out);
		}
	}
{
		VFS sourceVFS = VFSManager.getVFSForPath(sourcePath);
		Object sourceSession = sourceVFS.createVFSSession(sourcePath, comp);
		if (sourceSession == null)
		{
			Log.log(Log.WARNING, VFS.class, "Unable to get a valid session from " + sourceVFS + " for path " + sourcePath);
			return false;
		}
		VFS targetVFS = VFSManager.getVFSForPath(targetPath);
		Object targetSession = targetVFS.createVFSSession(targetPath, comp);
		if (targetSession == null)
		{
			Log.log(Log.WARNING, VFS.class, "Unable to get a valid session from " + targetVFS + " for path " + targetPath);
			return false;
		}
		return copy(progress, sourceVFS, sourceSession, sourcePath, targetVFS, targetSession, targetPath, comp,canStop);
	}
{
		if(evt.isConsumed())
			return null;

		if(Debug.DUMP_KEY_EVENTS)
		{
			Log.log(Log.DEBUG,this,"Key event (preprocessing) : "
					+ toString(evt));
		}

		return KeyEventWorkaround.processKeyEvent(evt);
	}
{
		if(files == null)
			files = _getFiles(view);

		if(files == null || files.length == 0)
			return null;

		if(path == null)
		{
			path = view.getBuffer().getSymlinkPath();
			VFS vfs = VFSManager.getVFSForPath(path);
			boolean ignoreCase = ((vfs.getCapabilities()
				& VFS.CASE_INSENSITIVE_CAP) != 0);

			for(int i = 0; i < files.length; i++)
			{
				if(StandardUtilities.compareStrings(
					files[i],path,ignoreCase) == 0)
				{
					return path;
				}
			}

			return getFirstFile(view);
		}
		else
		{
			// -1 so that the last isn't checked
			VFS vfs = VFSManager.getVFSForPath(path);
			boolean ignoreCase = ((vfs.getCapabilities()
				& VFS.CASE_INSENSITIVE_CAP) != 0);

			for(int i = 0; i < files.length - 1; i++)
			{
				if(StandardUtilities.compareStrings(
					files[i],path,ignoreCase) == 0)
				{
					return files[i+1];
				}
			}

			return null;
		}
	}
{
		if(!textArea.isEditable())
		{
			textArea.getToolkit().beep();
			return;
		}

		Register reg = getRegister(register);

		if(reg == null)
		{
			textArea.getToolkit().beep();
			return;
		}
		Transferable transferable = reg.getTransferable();
		Mode mode = null;
		String selection = null;
		if (transferable.isDataFlavorSupported(JEditDataFlavor.jEditRichTextDataFlavor))
		{
			try
			{
				JEditRichText data = (JEditRichText) transferable.getTransferData(JEditDataFlavor.jEditRichTextDataFlavor);
				mode = data.getMode();
				selection = data.getText();
			}
			catch (UnsupportedFlavorException e)
			{
				Log.log(Log.ERROR, Registers.class, e);
			}
			catch (IOException e)
			{
				Log.log(Log.ERROR, Registers.class, e);
			}
		}
		else if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor))
		{
			selection = getTextFromTransferable(transferable, DataFlavor.stringFlavor);
		}
		if(selection == null)
		{
			textArea.getToolkit().beep();
			return;
		}
		JEditBuffer buffer = textArea.getBuffer();
		applyMode(mode, buffer);
		try
		{
			buffer.beginCompoundEdit();

			/* vertical paste */
			if(vertical && textArea.getSelectionCount() == 0)
			{
				int caret = textArea.getCaretPosition();
				int caretLine = textArea.getCaretLine();
				Selection.Rect rect = new Selection.Rect(
					caretLine,caret,caretLine,caret);
				textArea.setSelectedText(rect,selection);
				caretLine = textArea.getCaretLine();

				if(caretLine != textArea.getLineCount() - 1)
				{

					int startColumn = rect.getStartColumn(
						buffer);
					int offset = buffer
						.getOffsetOfVirtualColumn(
						caretLine + 1,startColumn,null);
					if(offset == -1)
					{
						buffer.insertAtColumn(caretLine + 1,startColumn,"");
						textArea.setCaretPosition(
							buffer.getLineEndOffset(
							caretLine + 1) - 1);
					}
					else
					{
						textArea.setCaretPosition(
							buffer.getLineStartOffset(
							caretLine + 1) + offset);
					}
				}
			}
			else /* Regular paste */
			{
				textArea.replaceSelection(selection);
			}
		}
		finally
		{
			buffer.endCompoundEdit();
		}

		HistoryModel.getModel("clipboard").addItem(selection);
	}
{
		if(keyStroke == null)
			return null;
		int modifiers = 0;
		String key;
		int endOfModifiers = keyStroke.indexOf('+');
		if(endOfModifiers <= 0)	// not found or found at first
		{
			key = keyStroke;
		}
		else
		{
			for(int i = 0; i < endOfModifiers; i++)
			{
				switch(Character.toUpperCase(keyStroke
					.charAt(i)))
				{
				case 'A':
					modifiers |= a;
					break;
				case 'C':
					modifiers |= c;
					break;
				case 'M':
					modifiers |= m;
					break;
				case 'S':
					modifiers |= s;
					break;
				}
			}
			key = keyStroke.substring(endOfModifiers + 1);
		}
		if(key.length() == 1)
		{
			return new Key(modifiersToString(modifiers),0,key.charAt(0));
		}
		else if(key.length() == 0)
		{
			Log.log(Log.ERROR,KeyEventTranslator.class,
				"Invalid key stroke: " + keyStroke);
			return null;
		}
		else if(key.equals("SPACE"))
		{
			return new Key(modifiersToString(modifiers),0,' ');
		}
		else
		{
			int ch;

			try
			{
				ch = KeyEvent.class.getField("VK_".concat(key))
					.getInt(null);
			}
			catch(Exception e)
			{
				Log.log(Log.ERROR,KeyEventTranslator.class,
					"Invalid key stroke: "
					+ keyStroke);
				return null;
			}

			return new Key(modifiersToString(modifiers),ch,'\0');
		}
	}
{
		if (svc == null)
			svc = new VarCompressor();
		return svc.compress(path, crossPlatform);
	
	}
{
		if (svc == null)
			svc = new VarCompressor();
		return svc.compress(path);
	}
{
		if(buffer.getFoldHandler() instanceof IndentFoldHandler)
			foldLevel = (foldLevel - 1) * buffer.getIndentSize() + 1;

		showLineRange(0,buffer.getLineCount() - 1);

		int leastFolded = -1;
		int firstInvisible = 0;

		for(int i = 0; i < buffer.getLineCount(); i++)
		{
			// Keep track of the least fold level up to this point in the file,
			// because we can't hide a line at this level since there will be no "root"
			// line to unfold it from
			if (leastFolded == -1 || buffer.getFoldLevel(i) < leastFolded)
			{
				leastFolded = buffer.getFoldLevel(i);
			}
		
			if (buffer.getFoldLevel(i) < foldLevel ||
			    buffer.getFoldLevel(i) == leastFolded)
			{
				if(firstInvisible != i)
				{
					hideLineRange(firstInvisible,
						i - 1);
				}
				firstInvisible = i + 1;
			}
		}

		if(firstInvisible != buffer.getLineCount())
			hideLineRange(firstInvisible,buffer.getLineCount() - 1);

		notifyScreenLineChanges();
		if(textArea.getDisplayManager() == this)
		{
			textArea.foldStructureChanged();
		}
	}
{
		String lf = lfs[lookAndFeel.getSelectedIndex()].getClassName();
		jEdit.setProperty("lookAndFeel",lf);
		jEdit.setFontProperty("metal.primary.font",primaryFont.getFont());
		jEdit.setFontProperty("metal.secondary.font",secondaryFont.getFont());
		jEdit.setFontProperty("helpviewer.font", helpViewerFont.getFont());
		jEdit.setProperty("history",history.getText());
		jEdit.setProperty("menu.spillover",menuSpillover.getText());
		jEdit.setBooleanProperty("tip.show",showTips.isSelected());
		jEdit.setBooleanProperty("appearance.continuousLayout",continuousLayout.isSelected());
		jEdit.setBooleanProperty("systrayicon", systemTrayIcon.isSelected());
		IconTheme.set(iconThemes.getSelectedItem().toString());

		jEdit.setProperty(View.VIEW_DOCKING_FRAMEWORK_PROPERTY,
			(String) dockingFramework.getSelectedItem());

		/* AntiAlias nv = AntiAlias.appearance();
		 int idx = antiAliasExtras.getSelectedIndex();
		nv.set(idx);
		primaryFont.setAntiAliasEnabled(idx > 0);
		secondaryFont.setAntiAliasEnabled(idx > 0);
		primaryFont.repaint();
		secondaryFont.repaint(); */

		// this is handled a little differently from other jEdit settings
		// as the splash screen flag needs to be known very early in the
		// startup sequence, before the user properties have been loaded
		String settingsDirectory = jEdit.getSettingsDirectory();
		if(settingsDirectory != null)
		{
			File file = new File(settingsDirectory,"nosplash");
			if(showSplash.isSelected())
				file.delete();
			else
			{
				FileOutputStream out = null;
				try
				{
					out = new FileOutputStream(file);
					out.write('\n');
					out.close();
				}
				catch(IOException io)
				{
					Log.log(Log.ERROR,this,io);
				}
				finally
				{
					IOUtilities.closeQuietly(out);
				}
			}
		}
		jEdit.setBooleanProperty("textColors",textColors.isSelected());
		jEdit.setBooleanProperty("decorate.frames",decorateFrames.isSelected());
		jEdit.setBooleanProperty("decorate.dialogs",decorateDialogs.isSelected());
	}
{
		List<KeyBinding[]> allBindings = new ArrayList<KeyBinding[]>();
		Set<String> knownBindings = new HashSet<String>();
		models = new Vector<ShortcutsModel>();
		ActionSet[] actionSets = jEdit.getActionSets();
		for(int i = 0; i < actionSets.length; i++)
		{
			ActionSet actionSet = actionSets[i];
			if(actionSet.getActionCount() != 0)
			{
				String modelLabel = actionSet.getLabel();
				if(modelLabel == null)
				{
					Log.log(Log.ERROR,this,"Empty action set: "
						+ actionSet.getPluginJAR());
				}
				ShortcutsModel model = createModel(modelLabel,
						actionSet.getActionNames());
				models.addElement(model);
				List<KeyBinding[]> bindings = model.getBindings();
				for (KeyBinding[] binding : bindings)
				{
					String name = binding[0].name;
					if (!knownBindings.contains(name))
					{
						knownBindings.add(name);
						allBindings.add(binding);
					}
				}
			}
		}
		if (models.size() > 1)
			models.addElement(new ShortcutsModel("All", allBindings));
		Collections.sort(models,new StandardUtilities.StringCompare<ShortcutsModel>(true));
		ShortcutsModel currentModel = models.elementAt(0);
		filteredModel = new FilteredTableModel<ShortcutsModel>(currentModel)
		{
			@Override
			public String prepareFilter(String filter)
			{
				return filter.toLowerCase();
			}

			@Override
			public boolean passFilter(int row, String filter)
			{
				String name = delegated.getBindingAt(row, 0).label.toLowerCase();
				return name.contains(filter);
			}
		};
	}
{
		Object[] selected = clips.getSelectedValues();
		if(selected == null || selected.length == 0)
		{
			getToolkit().beep();
			return;
		}

		String text = getSelectedClipText();

		/**
		 * For each selected clip, we remove it, then add it back
		 * to the model. This has the effect of moving it to the
		 * top of the list.
		 */
		for(int i = 0; i < selected.length; i++)
		{
			listModel.removeElement(selected[i]);
			listModel.insertElementAt(selected[i],0);
		}

		view.getTextArea().setSelectedText(text);

		dispose();
	}
{
		// the first sub-fold. used by JEditTextArea.expandFold().
		int returnValue = -1;

		int lineCount = buffer.getLineCount();
		int end = lineCount - 1;

		if (line == lineCount - 1)
		{
			return -1;
		}
		while (!isLineVisible(line))
		{
			int prevLine = folds.lookup(folds.search(line)) - 1;
			if (!isLineVisible(prevLine))
			{
				return -1;
			}
			expandFold(prevLine, fully);
			if (!isLineVisible(prevLine + 1))
			{
				return -1;
			}
		}
		if (isLineVisible(line+1) && !fully)
		{
			return -1;
		}

		//{{{ Find fold start and fold end...
		int start;
		int initialFoldLevel = buffer.getFoldLevel(line);
		if (buffer.getFoldLevel(line + 1) > initialFoldLevel)
		{
			// this line is the start of a fold
			start = line;
			if (!isLineVisible(line + 1) && folds.search(line + 1) != folds.count() - 1)
			{
				int index = folds.search(line + 1);
				end = folds.lookup(index + 1) - 1;
			}
			else
			{
				for (int i = line + 1; i < lineCount; i++)
				{
					if (buffer.getFoldLevel(i) <= initialFoldLevel)
					{
						end = i - 1;
						break;
					}
				}
			}
		}
		else
		{
			if (!fully)
			{
				return -1;
			}
			start = line;
			while (start > 0 && buffer.getFoldLevel(start) >= initialFoldLevel)
			{
				start--;
			}
			initialFoldLevel = buffer.getFoldLevel(start);
			for (int i = line + 1; i < lineCount; i++)
			{
				if (buffer.getFoldLevel(i) <= initialFoldLevel)
				{
					end = i - 1;
					break;
				}
			}
		} // }}}

		//{{{ Expand the fold...
		if(fully)
		{
			showLineRange(start,end);
		}
		else
		{
			for (int i = start + 1; i <= end;)
			{
				if (returnValue == -1 && buffer.isFoldStart(i))
				{
					returnValue = i;
				}

				showLineRange(i, i);
				int fold = buffer.getFoldLevel(i);
				i++;
				while (i <= end && buffer.getFoldLevel(i) > fold)
				{
					i++;
				}
			}
		} // }}}

		notifyScreenLineChanges();
		textArea.foldStructureChanged();

		return returnValue;
	}
{
		String nogzName = filename.substring(0,filename.length() -
			(filename.endsWith(".gz") ? 3 : 0));

		List<Mode> acceptable = new ArrayList<Mode>();
		
		// First check overrideModes as these are user supplied modes.
		// User modes have priority.
		for(Mode mode : overrideModes.values())
		{
			if(mode.accept(nogzName,firstLine))
			{
				acceptable.add(mode);
			}
		}
		if (acceptable.size() == 0)
		{
			// no user modes were acceptable, so check standard modes.
			for(Mode mode : modes.values())
			{
				if(mode.accept(nogzName,firstLine))
				{
					acceptable.add(mode);
				}
			}
		}
		if (acceptable.size() == 1) 
		{
			return acceptable.get(0);	
		}
		if (acceptable.size() > 1) 
		{
			Collections.reverse(acceptable);
			
			// the very most acceptable mode is one whose file
			// name doesn't only match the file name as regular
			// expression but which is identical
			for (Mode mode : acceptable)
			{
				if (mode.acceptFilenameIdentical(filename)) 
				{
					return mode;	
				}
			}

			// most acceptable is a mode that matches both the
			// filename and the first line glob
			for (Mode mode : acceptable) 
			{
				if (mode.acceptFilename(filename) && 
					mode.acceptFirstLine(firstLine)) 
				{
					return mode;	
				}
			}
			// next best is filename match
			for (Mode mode : acceptable) 
			{
				if (mode.acceptFilename(filename)) {
					return mode;	
				}
			}
			// all acceptable choices are by first line glob, and
			// they all match, so just return the first one.
			return acceptable.get(0);	
		}
		// no matching mode found for this file
		return null;
	}
{
		VFS vfs = VFSManager.getVFSForPath(from);

		String filename = vfs.getFileName(from);
		String to = newname;
		
		if(to == null || filename.equals(newname))
			return;

		to = MiscUtilities.constructPath(vfs.getParentOfPath(from),to);

		Object session = vfs.createVFSSession(from,this);
		if(session == null)
			return;

		if(!startRequest())
			return;

		Runnable delayedAWTRequest = new DelayedEndRequest();
		Task renameTask = new RenameBrowserTask(this, session, vfs, from, to, delayedAWTRequest);
		ThreadUtilities.runInBackground(renameTask);
	}

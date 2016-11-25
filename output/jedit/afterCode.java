{
		System.err.println("SCROLL_VERIFY");
		int verifyScrollLine = 0;
		int currentPhysicalLine = getPhysicalLine();

		for(int i = 0, n = getDisplayManager().getBuffer().getLineCount(); i < n && i < currentPhysicalLine; i++)
		{
			if(getDisplayManager().isLineVisible(i))
				verifyScrollLine += getDisplayManager().getScreenLineCount(i);
		}

		int scrollLine = getScrollLine();
		if(verifyScrollLine != scrollLine)
		{
			RuntimeException ex = new RuntimeException("ScrollLine is " + scrollLine + " but should be " + verifyScrollLine + " diff = " + (verifyScrollLine - scrollLine));
			Log.log(Log.ERROR,this,ex);
		}
	}
{
		if(editPane.getBuffer() == buffer
			&& editPane.getTextArea().getVisibleLines() > 1)
		{
			if (focus)
				editPane.focusOnTextArea();
			return editPane;
		}

		EditPane[] editPanes = getEditPanes();
		for(int i = 0; i < editPanes.length; i++)
		{
			EditPane ep = editPanes[i];
			if(ep.getBuffer() == buffer
				/* ignore zero-height splits, etc */
				&& ep.getTextArea().getVisibleLines() > 1)
			{
				setEditPane(ep);
				if (focus)
					ep.focusOnTextArea();
				return ep;
			}
		}

		editPane.setBuffer(buffer, focus);
		return editPane;
	}
{
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
{
		HistoryModel pathModel = HistoryModel.getModel("vfs.browser.path");
		if(pathModel.getSize() == 0)
			return null;
		else
			return pathModel.getItem(0);
	}
{
		Log.log(Log.ERROR, this, "Plugin: " + plugin.name + " " + installedVersion +
			" is not supported on this version of jEdit! Disabling plugin.");
		jEdit.removePluginJAR(jar,false);
		String jarName = MiscUtilities.getFileName(jar.getPath());
		// Stop it from getting loaded:
		jEdit.setBooleanProperty("plugin-blacklist."+ jarName,true);
		// show as 'Unsupported' in Manage Panel:
		jEdit.setBooleanProperty("plugin." + jarName + ".disabled", true);
		jEdit.propertiesChanged();		
	}
{
		moveGapStart(start);
		if(gapEnd - gapStart < len)
		{
			int gapSize = len + 1024;
			ensureCapacity(length + gapSize);
			moveGapEnd(start + gapSize);
		}
	}
{
		int length = (wrap ? ring.length : count);
		for(int i = length - 1; i >= 0; i--)
		{
			if(ring[i].equals(str))
			{
				return i;
			}
		}
		return -1;
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
		String name = file.getName();
		File backupFile = getNthBackupFile(name, 1, backups,
				backupPrefix, backupSuffix,
				backupDirectory);

		long modTime = backupFile.lastModified();
		/* if backup file was created less than
		* 'backupTimeDistance' ago, we do not
		* create the backup */
		if(System.currentTimeMillis() - modTime
			< backupTimeDistance)
		{
			Log.log(Log.DEBUG,MiscUtilities.class,
				"Backup not done because of backup.minTime");
			return null;
		}

		File lastBackup = getNthBackupFile(name, backups, backups,
				backupPrefix, backupSuffix,
				backupDirectory);
		lastBackup.delete();

		if(backups > 1)
		{
			for(int i = backups - 1; i > 0; i--)
			{
				File backup1 = getNthBackupFile(name, i,
					backups, backupPrefix,
					backupSuffix, backupDirectory);
				File backup2 = getNthBackupFile(name, i+1,
					backups, backupPrefix,
					backupSuffix, backupDirectory);
				
				backup1.renameTo(backup2);
			}
			
		}
		return backupFile;
	}
{
		Log.log(Log.DEBUG,MiscUtilities.class,
			"Saving backup of file \"" +
			file.getAbsolutePath() + "\" to \"" +
			backupFile.getAbsolutePath() + '"');
		if (!file.renameTo(backupFile))
			IOUtilities.moveFile(file, backupFile);
	}
{
		final Frame frame = JOptionPane.getFrameForComponent(comp);

		synchronized(errorLock)
		{
			error = true;

			errors.add(new ErrorListDialog.ErrorEntry(
				path,messageProp,args,urgency));

			if(errors.size() == 1)
			{
				if (!errorDisplayerActive)
					ThreadUtilities.runInBackground(new ErrorDisplayer(frame));
			}
		}
	}
{
		String language;
		if (getBooleanProperty("lang.usedefaultlocale"))
		{
			language = Locale.getDefault().getLanguage();
		}
		else
		{
			language = getProperty("lang.current", "en");
		}
		return language;
	}
{
		// FIXME: Need BiDi support.
		int flags = Font.LAYOUT_LEFT_TO_RIGHT
			| Font.LAYOUT_NO_START_CONTEXT
			| Font.LAYOUT_NO_LIMIT_CONTEXT;

		GlyphVector result =
			font.layoutGlyphVector(frc, text, start, end, flags);

		// This is necessary to work around a memory leak in Sun Java 6 where
		// the sun.font.GlyphLayout is cached and reused while holding an
		// instance to the char array.
		font.layoutGlyphVector(frc, EMPTY_TEXT, 0, 0, flags);

		return result;
	}
{
		if(seq == null)
			return;

		int len = seq.length();

		if(len == 0)
			return;

		if(isReadOnly())
			throw new RuntimeException("buffer read-only");

		try
		{
			writeLock();

			if(offset < 0 || offset > contentMgr.getLength())
				throw new ArrayIndexOutOfBoundsException(offset);

			contentMgr.insert(offset,seq);

			integerArray.clear();

			for(int i = 0; i < len; i++)
			{
				if(seq.charAt(i) == '\n')
					integerArray.add(i + 1);
			}

			if(!undoInProgress)
			{
				undoMgr.contentInserted(offset,len,
							seq.toString(),!dirty);
			}

			contentInserted(offset,len,integerArray);
		}
		finally
		{
			writeUnlock();
		}
	}
{
		float x = 0.0f;
		for(Chunk chunk = lineHead; chunk != null; chunk = (Chunk)chunk.next)
		{
			if(chunk.isTab(lineText))
			{
				initChunk(chunk, x, lineText);
			}
			x += chunk.width;
		}
		return x;
	}
{
		return length == 1
			&& lineText.array[lineText.offset + offset] == '\t';
	}
{
			Object source = evt.getSource();

			if(source == add)
			{
				WidgetSelectionDialog dialog = new WidgetSelectionDialog(StatusBarOptionPane.this);
				String value = dialog.getValue();
				if (value == null || value.isEmpty())
					return;

				int index = list.getSelectedIndex();
				if(index == -1)
					index = listModel.getSize();
				else
					index++;

				listModel.insertElementAt(value,index);
				list.setSelectedIndex(index);
				list.ensureIndexIsVisible(index);
				updatePreview();
			}
			else if(source == remove)
			{
				int index = list.getSelectedIndex();
				listModel.removeElementAt(index);
				if(listModel.getSize() != 0)
				{
					if(listModel.getSize() == index)
						list.setSelectedIndex(index-1);
					else
						list.setSelectedIndex(index);
				}
				updateButtons();
				updatePreview();
			}
			else if(source == moveUp)
			{
				int index = list.getSelectedIndex();
				Object selected = list.getSelectedValue();
				listModel.removeElementAt(index);
				listModel.insertElementAt(selected,index-1);
				list.setSelectedIndex(index-1);
				list.ensureIndexIsVisible(index-1);
				updatePreview();
			}
			else if(source == moveDown)
			{
				int index = list.getSelectedIndex();
				Object selected = list.getSelectedValue();
				listModel.removeElementAt(index);
				listModel.insertElementAt(selected,index+1);
				list.setSelectedIndex(index+1);
				list.ensureIndexIsVisible(index+1);
				updatePreview();
			}
			else if(source == edit)
			{
				Object selectedValue = list.getSelectedValue();
				if (selectedValue == null)
					return;
				WidgetSelectionDialog dialog = new WidgetSelectionDialog(StatusBarOptionPane.this,
											 String.valueOf(selectedValue));
				String value = dialog.getValue();
				if (value == null || value.isEmpty())
					return;
				int index = list.getSelectedIndex();
				listModel.remove(index);
				listModel.insertElementAt(value, index);
				list.setSelectedIndex(index);
				list.ensureIndexIsVisible(index);
				updatePreview();
			}
		}
{
		Font f = null;
		for (Font candidate : getFonts())
		{
			 if (candidate.canDisplay(i))
			 {
				 f = candidate;
				 break;
			 }
		}
		return f;
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
			if (sendVFSUpdate)
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
			Log.log(Log.WARNING, VFS.class, "Unable to get a valid session from " + sourceVFS +
							" for path " + sourcePath);
			return false;
		}
		VFS targetVFS = VFSManager.getVFSForPath(targetPath);
		Object targetSession = targetVFS.createVFSSession(targetPath, comp);
		if (targetSession == null)
		{
			Log.log(Log.WARNING, VFS.class, "Unable to get a valid session from " + targetVFS +
							" for path " + targetPath);
			return false;
		}
		return copy(progress, sourceVFS, sourceSession, sourcePath, targetVFS, targetSession, targetPath,
			    comp,canStop, sendVFSUpdate);
	}
{
		if(Debug.DUMP_KEY_EVENTS)
		{
			Log.log(Log.DEBUG,this,"Key event                 : "
				+ toString(evt) + " from " + from);
		//	Log.log(Log.DEBUG,this,view+".isFocused()="+view.isFocused()+'.',new Exception());
		}

		if(evt.isConsumed())
			return;

		if(Debug.DUMP_KEY_EVENTS)
		{
			Log.log(Log.DEBUG,this,"Key event (preprocessing) : "
					       + toString(evt));
		}

		evt = KeyEventWorkaround.processKeyEvent(evt);
		if(evt == null)
			return;

		if(Debug.DUMP_KEY_EVENTS)
		{
			Log.log(Log.DEBUG,this,"Key event after workaround: "
				+ toString(evt) + " from " + from);
		}

		boolean focusOnTextArea = false;
		switch(evt.getID())
		{
		case KeyEvent.KEY_TYPED:
			// if the user pressed eg C+e n n in the
			// search bar we want focus to go back there
			// after the prefix is done


			if(keyEventInterceptor != null)
				keyEventInterceptor.keyTyped(evt);
			else if(isPrefixActive() || textArea.hasFocus())
			{
				processKeyEventKeyStrokeHandling(evt,from,"type ",global);
			}


			processKeyEventSub(focusOnTextArea);

			break;
		case KeyEvent.KEY_PRESSED:
			if(keyEventInterceptor != null)
				keyEventInterceptor.keyPressed(evt);
			else if(KeyEventWorkaround.isBindable(evt.getKeyCode()))
			{
				processKeyEventKeyStrokeHandling(evt,from,"press",global);

				processKeyEventSub(focusOnTextArea);

			}
			break;
		case KeyEvent.KEY_RELEASED:
			if(keyEventInterceptor != null)
				keyEventInterceptor.keyReleased(evt);
			break;
		}
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

			if (direction == Direction.NEXT)
			{
				return getFirstFile(view);
			}
			else
			{
				return getLastFile(view);
			}
		}
		else
		{
			// -1 so that the last isn't checked
			VFS vfs = VFSManager.getVFSForPath(path);
			boolean ignoreCase = ((vfs.getCapabilities()
				& VFS.CASE_INSENSITIVE_CAP) != 0);
			
			if (direction == Direction.NEXT &&
				StandardUtilities.compareStrings(files[files.length - 1],
					path, ignoreCase) == 0)
			{
				// Going forward and already at the last file
				return null;
			}
			else if (direction == Direction.PREV &&
				StandardUtilities.compareStrings(files[0], path, ignoreCase) == 0)
			{
				// Going backward and already at the first file
				return null;
			}

			for(int i = 1; i < files.length - 1; i++)
			{
				if(StandardUtilities.compareStrings(
					files[i],path,ignoreCase) == 0)
				{
					if (direction == Direction.NEXT)
						return files[i + 1];
					else
						return files[i - 1];
				}
			}

			return null;
		}
	}
{
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
		int modifiers = 0;
		
		for (char ch : modifierString.toCharArray())
		{
			switch (Character.toUpperCase(ch))
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
		
		return modifiers;
	}
{
		if (svc == null)
			svc = new VarCompressor();
		return svc.compress(path);
	}
{
		if (svc == null)
			svc = new VarCompressor();
		return svc.compress(path, crossPlatform);
	
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
		if(update && textArea.getDisplayManager() == this)
		{
			textArea.foldStructureChanged();
		}
	}
{
		String settingsDirectory = jEdit.getSettingsDirectory();
		if(settingsDirectory != null)
		{
			File file = new File(settingsDirectory, fileName);
			if (!present)
			{
				file.delete();
			}
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
	}
{
		models.clear();
		List<KeyBinding[]> allBindings = new ArrayList<KeyBinding[]>();
		Collection<String> knownBindings = new HashSet<String>();
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
				models.add(model);
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
			models.add(new ShortcutsModel("All", allBindings));
		ShortcutsModel delegated = filteredModel.getDelegated();
		Collections.sort(models,new StandardUtilities.StringCompare<ShortcutsModel>(true));
		if (delegated == null)
		{
			delegated = models.get(0);
		}
		else
		{
			for (ShortcutsModel model : models)
			{
				// Find the model with the same name
				if (model.toString().equals(delegated.toString()))
				{
					delegated = model;
					break;
				}
			}
		}
		filteredModel.setDelegated(delegated);
		filteredModel.fireTableDataChanged();
	}
{
		// Remove the reference to the JList from the history model so that the
		// list doesn't keep getting updated after the dialog is gone
		Object[] nothing = {};
		clips.setListData(nothing);
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
			_expandFold(prevLine, fully);
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

		return returnValue;
	}
{
		if ((filepath != null) && filepath.endsWith(".gz"))
			filepath = filepath.substring(0, filepath.length() - 3);
		if ((filename != null) && filename.endsWith(".gz"))
			filename = filename.substring(0, filename.length() - 3);

		List<Mode> acceptable = new ArrayList<Mode>();

		// First check overrideModes as these are user supplied modes.
		// User modes have priority.
		for(Mode mode : overrideModes.values())
		{
			if(mode.accept(filepath, filename, firstLine))
			{
				acceptable.add(mode);
			}
		}
		if (acceptable.size() == 0)
		{
			// no user modes were acceptable, so check standard modes.
			for(Mode mode : modes.values())
			{
				if(mode.accept(filepath, filename, firstLine))
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
				if (mode.acceptIdentical(filepath, filename))
				{
					return mode;
				}
			}

			// most acceptable is a mode that matches both the
			// filepath and the first line glob
			for (Mode mode : acceptable)
			{
				if (mode.acceptFile(filepath, filename) &&
					mode.acceptFirstLine(firstLine))
				{
					return mode;
				}
			}
			// next best is filepath match
			for (Mode mode : acceptable)
			{
				if (mode.acceptFile(filepath, filename)) {
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
		String filename = vfs.getFileName(from);
		String to = newname;

		if(to == null)
			return;

		if (!(vfs instanceof FavoritesVFS))
		{
			if (filename.equals(newname))
				return;
			to = MiscUtilities.constructPath(vfs.getParentOfPath(from),to);
		}

		Object session = vfs.createVFSSession(from,this);
		if(session == null)
			return;

		if(!startRequest())
			return;

		Runnable delayedAWTRequest = new DelayedEndRequest();
		Task renameTask = new RenameBrowserTask(this, session, vfs, from, to, delayedAWTRequest);
		ThreadUtilities.runInBackground(renameTask);
	}

setUp() throws Exception {
        Logger logger = Logger.getLogger(loggerName);
        mockHandler = new MockHandler();
        removeHandlers(logger);
        logger.addHandler(mockHandler);
    }
fixSubstitutedLoggers() {
        List<SubstituteLogger> loggers = TEMP_FACTORY.getLoggers();

        if (loggers.isEmpty()) {
            return;
        }

        Util.report("The following set of substitute loggers may have been accessed");
        Util.report("during the initialization phase. Logging calls during this");
        Util.report("phase were not honored. However, subsequent logging calls to these");
        Util.report("loggers will work as normally expected.");
        Util.report("See also " + SUBSTITUTE_LOGGER_URL);
        for (SubstituteLogger subLogger : loggers) {
            subLogger.setDelegate(getLogger(subLogger.getName()));
            Util.report(subLogger.getName());
        }

        TEMP_FACTORY.clear();
    }
arrayFormat(final String messagePattern, final Object[] argArray) {

        Throwable throwableCandidate = getThrowableCandidate(argArray);

        if (messagePattern == null) {
            return new FormattingTuple(null, argArray, throwableCandidate);
        }

        if (argArray == null) {
            return new FormattingTuple(messagePattern);
        }

        int i = 0;
        int j;
        // use string builder for better multicore performance
        StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);

        int L;
        for (L = 0; L < argArray.length; L++) {

            j = messagePattern.indexOf(DELIM_STR, i);

            if (j == -1) {
                // no more variables
                if (i == 0) { // this is a simple string
                    return new FormattingTuple(messagePattern, argArray, throwableCandidate);
                } else { // add the tail string which contains no variables and return
                    // the result.
                    sbuf.append(messagePattern, i, messagePattern.length());
                    return new FormattingTuple(sbuf.toString(), argArray, throwableCandidate);
                }
            } else {
                if (isEscapedDelimeter(messagePattern, j)) {
                    if (!isDoubleEscaped(messagePattern, j)) {
                        L--; // DELIM_START was escaped, thus should not be incremented
                        sbuf.append(messagePattern, i, j - 1);
                        sbuf.append(DELIM_START);
                        i = j + 1;
                    } else {
                        // The escape character preceding the delimiter start is
                        // itself escaped: "abc x:\\{}"
                        // we have to consume one backward slash
                        sbuf.append(messagePattern, i, j - 1);
                        deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
                        i = j + 2;
                    }
                } else {
                    // normal case
                    sbuf.append(messagePattern, i, j);
                    deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
                    i = j + 2;
                }
            }
        }
        // append the characters following the last {} pair.
        sbuf.append(messagePattern, i, messagePattern.length());
        if (L < argArray.length - 1) {
            return new FormattingTuple(sbuf.toString(), argArray, throwableCandidate);
        } else {
            return new FormattingTuple(sbuf.toString(), argArray, null);
        }
    }
log(Marker marker, String callerFQCN, int level, String message, Object[] argArray, Throwable t) {
        Level julLevel;
        switch (level) {
        case LocationAwareLogger.TRACE_INT:
            julLevel = Level.FINEST;
            break;
        case LocationAwareLogger.DEBUG_INT:
            julLevel = Level.FINE;
            break;
        case LocationAwareLogger.INFO_INT:
            julLevel = Level.INFO;
            break;
        case LocationAwareLogger.WARN_INT:
            julLevel = Level.WARNING;
            break;
        case LocationAwareLogger.ERROR_INT:
            julLevel = Level.SEVERE;
            break;
        default:
            throw new IllegalStateException("Level number " + level + " is not recognized.");
        }
        // the logger.isLoggable check avoids the unconditional
        // construction of location data for disabled log
        // statements. As of 2008-07-31, callers of this method
        // do not perform this check. See also
        // http://jira.qos.ch/browse/SLF4J-81
        if (logger.isLoggable(julLevel)) {
            log(callerFQCN, julLevel, message, t);
        }
    }
log(Marker marker, String callerFQCN, int level, String msg, Object[] argArray, Throwable t) {
        Level log4jLevel;
        switch (level) {
        case LocationAwareLogger.TRACE_INT:
            log4jLevel = traceCapable ? Level.TRACE : Level.DEBUG;
            break;
        case LocationAwareLogger.DEBUG_INT:
            log4jLevel = Level.DEBUG;
            break;
        case LocationAwareLogger.INFO_INT:
            log4jLevel = Level.INFO;
            break;
        case LocationAwareLogger.WARN_INT:
            log4jLevel = Level.WARN;
            break;
        case LocationAwareLogger.ERROR_INT:
            log4jLevel = Level.ERROR;
            break;
        default:
            throw new IllegalStateException("Level number " + level + " is not recognized.");
        }
        logger.log(callerFQCN, log4jLevel, msg, t);
    }
arrayFormat(final String messagePattern,
      final Object[] argArray, final Throwable t) {

    Throwable throwableCandidate = t;

    if (messagePattern == null) {
      return new FormattingTuple(null, argArray, throwableCandidate);
    }

    if (argArray == null) {
      return new FormattingTuple(messagePattern);
    }

    int i = 0;
    int j;
    // use string builder for better multicore performance 
    StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);

    int L;
    for (L = 0; L < argArray.length; L++) {

      j = messagePattern.indexOf(DELIM_STR, i);

      if (j == -1) {
        // no more variables
        if (i == 0) { // this is a simple string
          return new FormattingTuple(messagePattern, argArray,
              throwableCandidate);
        } else { // add the tail string which contains no variables and return
          // the result.
          sbuf.append(messagePattern.substring(i, messagePattern.length()));
          return new FormattingTuple(sbuf.toString(), argArray,
              throwableCandidate);
        }
      } else {
        if (isEscapedDelimeter(messagePattern, j)) {
          if (!isDoubleEscaped(messagePattern, j)) {
            L--; // DELIM_START was escaped, thus should not be incremented
            sbuf.append(messagePattern.substring(i, j - 1));
            sbuf.append(DELIM_START);
            i = j + 1;
          } else {
            // The escape character preceding the delimiter start is
            // itself escaped: "abc x:\\{}"
            // we have to consume one backward slash
            sbuf.append(messagePattern.substring(i, j - 1));
            deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
            i = j + 2;
          }
        } else {
          // normal case
          sbuf.append(messagePattern.substring(i, j));
          deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
          i = j + 2;
        }
      }
    }
    // append the characters following the last {} pair.
    sbuf.append(messagePattern.substring(i, messagePattern.length()));
    if (L < argArray.length - 1) {
      return new FormattingTuple(sbuf.toString(), argArray, throwableCandidate);
    } else {
      return new FormattingTuple(sbuf.toString(), argArray, null);
    }
  }
loggerNameToTag(String loggerName) {
        // Anonymous logger
        if (loggerName == null) {
            return ANONYMOUS_TAG;
        }

        int length = loggerName.length();
        if (length <= TAG_MAX_LENGTH) {
            return loggerName;
        }

        int lastTokenIndex = 0;
        int lastPeriodIndex;
        StringBuilder tagName = new StringBuilder();
        while ((lastPeriodIndex = loggerName.indexOf('.', lastTokenIndex)) != -1) {
            tagName.append(loggerName.charAt(lastTokenIndex));
            // token of one character appended as is otherwise truncate it to one character
            int tokenLength = lastPeriodIndex - lastTokenIndex;
            if (tokenLength > 1) {
                tagName.append('*');
            }
            tagName.append('.');
            lastTokenIndex = lastPeriodIndex + 1;
        }
        // last token (usually class name) appended as is
        tagName.append(loggerName, lastTokenIndex, length);
        if (tagName.length() <= TAG_MAX_LENGTH) {
            return tagName.toString();
        }

        // Either we had no useful dot location at all or name still too long.
        // Take leading part and append '*' to indicate that it was truncated
        lastPeriodIndex = loggerName.lastIndexOf('.');
        return lastPeriodIndex != -1 && length - (lastPeriodIndex + 1) <= TAG_MAX_LENGTH
                ? loggerName.substring(lastPeriodIndex + 1)
                : '*' + loggerName.substring(length - TAG_MAX_LENGTH + 1);
    }
isTraceEnabled() {
        return Log.isLoggable(name, Log.VERBOSE);
    }
isDebugEnabled() {
        return Log.isLoggable(name, Log.DEBUG);
    }
isInfoEnabled() {
        return Log.isLoggable(name, Log.INFO);
    }
isWarnEnabled() {
        return Log.isLoggable(name, Log.WARN);
    }
isErrorEnabled() {
        return Log.isLoggable(name, Log.ERROR);
    }
log(int priority, String message, Throwable throwable) {
        if (Log.isLoggable(name, priority)) {
            _log(name, priority, message, throwable);
        }
    }
trace(final String format, final Object... argArray) {
        if (Log.isLoggable(name, Log.VERBOSE)) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            Log.v(name, ft.getMessage(), ft.getThrowable());
        }
    }
debug(final String format, final Object... argArray) {
        if (Log.isLoggable(name, Log.DEBUG)) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            Log.d(name, ft.getMessage(), ft.getThrowable());
        }
    }
info(final String format, final Object... argArray) {
        if (Log.isLoggable(name, Log.INFO)) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            Log.i(name, ft.getMessage(), ft.getThrowable());
        }
    }
warn(final String format, final Object... argArray) {
        if (Log.isLoggable(name, Log.WARN)) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            Log.w(name, ft.getMessage(), ft.getThrowable());
        }
    }
error(final String format, final Object... argArray) {
        if (Log.isLoggable(name, Log.ERROR)) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            Log.e(name, ft.getMessage(), ft.getThrowable());
        }
    }
log(int level, String message, Throwable t) {
    if (! isLevelEnabled(level)) {
      return;
    }

    StringBuffer buf = new StringBuffer(32);

    // Append date-time if so configured
    if(showDateTime) {
      Date now = new Date();
      String dateText;
      synchronized(dateFormatter) {
        dateText = dateFormatter.format(now);
      }
      buf.append(dateText);
      buf.append(' ');
    } else {
      buf.append(System.currentTimeMillis() - startTime);
      buf.append(' ');
    }

    // Append current thread name if so configured
    if (showThreadName) {
      buf.append('[');
      buf.append(Thread.currentThread().getName());
      buf.append("] ");
	}

    // Append a readable representation of the log level
    switch(level) {
      case LOG_LEVEL_TRACE: buf.append("TRACE"); break;
      case LOG_LEVEL_DEBUG: buf.append("DEBUG"); break;
      case LOG_LEVEL_INFO:  buf.append("INFO");  break;
      case LOG_LEVEL_WARN:  buf.append("WARN");  break;
      case LOG_LEVEL_ERROR: buf.append("ERROR"); break;
//      case LOG_LEVEL_FATAL: buf.append("[FATAL] "); break;
    }
    buf.append(' ');

    // Append the name of the log instance if so configured
    if(showShortName) {
      if(shortLogName==null) {
        // Cut all but the last component of the name for both styles
        shortLogName = name.substring(name.lastIndexOf(".") + 1);
        shortLogName =
            shortLogName.substring(shortLogName.lastIndexOf("/") + 1);
      }
      buf.append(String.valueOf(shortLogName)).append(" - ");
    } else if(showLogName) {
        buf.append(String.valueOf(name)).append(" - ");
    }

    // Append the message
    buf.append(message);

    System.err.println(buf.toString());
    // Append stack trace if not null
    if (t != null) {
      t.printStackTrace(System.err);
    }
    System.err.flush();
  }
arrayFormat(final String messagePattern,
      final Object[] argArray) {

    Throwable throwableCandidate = getThrowableCandidate(argArray);

    if (messagePattern == null) {
      return new FormattingTuple(null, argArray, throwableCandidate);
    }

    if (argArray == null) {
      return new FormattingTuple(messagePattern);
    }

    int i = 0;
    int j;
    StringBuffer sbuf = new StringBuffer(messagePattern.length() + 50);

    int L;
    for (L = 0; L < argArray.length; L++) {

      j = messagePattern.indexOf(DELIM_STR, i);

      if (j == -1) {
        // no more variables
        if (i == 0) { // this is a simple string
          return new FormattingTuple(messagePattern, argArray,
              throwableCandidate);
        } else { // add the tail string which contains no variables and return
          // the result.
          sbuf.append(messagePattern.substring(i, messagePattern.length()));
          return new FormattingTuple(sbuf.toString(), argArray,
              throwableCandidate);
        }
      } else {
        if (isEscapedDelimeter(messagePattern, j)) {
          if (!isDoubleEscaped(messagePattern, j)) {
            L--; // DELIM_START was escaped, thus should not be incremented
            sbuf.append(messagePattern.substring(i, j - 1));
            sbuf.append(DELIM_START);
            i = j + 1;
          } else {
            // The escape character preceding the delimiter start is
            // itself escaped: "abc x:\\{}"
            // we have to consume one backward slash
            sbuf.append(messagePattern.substring(i, j - 1));
            deeplyAppendParameter(sbuf, argArray[L], new HashMap());
            i = j + 2;
          }
        } else {
          // normal case
          sbuf.append(messagePattern.substring(i, j));
          deeplyAppendParameter(sbuf, argArray[L], new HashMap());
          i = j + 2;
        }
      }
    }
    // append the characters following the last {} pair.
    sbuf.append(messagePattern.substring(i, messagePattern.length()));
    if (L < argArray.length - 1) {
      return new FormattingTuple(sbuf.toString(), argArray, throwableCandidate);
    } else {
      return new FormattingTuple(sbuf.toString(), argArray, null);
    }
  }
arrayFormat(final String messagePattern,
      final Object[] argArray) {
    if (messagePattern == null) {
      return null;
    }
    if (argArray == null) {
      return messagePattern;
    }
    int i = 0;
    int j;
    StringBuffer sbuf = new StringBuffer(messagePattern.length() + 50);

    for (int L = 0; L < argArray.length; L++) {

      j = messagePattern.indexOf(DELIM_STR, i);

      if (j == -1) {
        // no more variables
        if (i == 0) { // this is a simple string
          return messagePattern;
        } else { // add the tail string which contains no variables and return
          // the result.
          sbuf.append(messagePattern.substring(i, messagePattern.length()));
          return sbuf.toString();
        }
      } else {
        if (isEscapedDelimeter(messagePattern, j)) {
          if (!isDoubleEscaped(messagePattern, j)) {
            L--; // DELIM_START was escaped, thus should not be incremented
            sbuf.append(messagePattern.substring(i, j - 1));
            sbuf.append(DELIM_START);
            i = j + 1;
          } else {
            // The escape character preceding the delimiter start is
            // itself escaped: "abc x:\\{}"
            // we have to consume one backward slash
            sbuf.append(messagePattern.substring(i, j - 1));
            deeplyAppendParameter(sbuf, argArray[L], new HashMap());
            i = j + 2;
          }
        } else {
          // normal case
          sbuf.append(messagePattern.substring(i, j));
          deeplyAppendParameter(sbuf, argArray[L], new HashMap());
          i = j + 2;
        }
      }
    }
    // append the characters following the last {} pair.
    sbuf.append(messagePattern.substring(i, messagePattern.length()));
    return sbuf.toString();
  }
bind() {
    try {
      // the next line does the binding
      getSingleton();
      INITIALIZATION_STATE = SUCCESSFUL_INITILIZATION;
      emitSubstituteLoggerWarning();
    } catch (NoClassDefFoundError ncde) {
      INITIALIZATION_STATE = FAILED_INITILIZATION;
      String msg = ncde.getMessage();
      if (msg != null && msg.indexOf("org/slf4j/impl/StaticLoggerBinder") != -1) {
        Util
            .reportFailure("Failed to load class \"org.slf4j.impl.StaticLoggerBinder\".");
        Util.reportFailure("See " + NO_STATICLOGGERBINDER_URL
            + " for further details.");

      }
      throw ncde;
    } catch (Exception e) {
      INITIALIZATION_STATE = FAILED_INITILIZATION;
      // we should never get here
      Util.reportFailure("Failed to instantiate logger ["
          + getSingleton().getLoggerFactoryClassStr() + "]", e);
    }
  }
render(Object o) {
		if (o == null) {
			return String.valueOf(o);
		}
		Class objectClass = o.getClass();
		if (unrenderableClasses.containsKey(objectClass) == false) {
			try {
				if (objectClass.isArray()) {
					Class componentType = objectClass.getComponentType();
					StringBuffer sb = new StringBuffer("[");
					if (componentType.isPrimitive() == true) {
						if (Boolean.TYPE.equals(componentType)) {
							boolean[] ba = (boolean[]) o;
							for (int i = 0; i < ba.length; i++) {
								if (i > 0) {
									sb.append(", ");
								}
								sb.append(ba[i]);
							}
						} else if (Integer.TYPE.equals(componentType)) {
							int[] ia = (int[]) o;
							for (int i = 0; i < ia.length; i++) {
								if (i > 0) {
									sb.append(", ");
								}
								sb.append(ia[i]);
							}

						} else if (Long.TYPE.equals(componentType)) {
							long[] ia = (long[]) o;
							for (int i = 0; i < ia.length; i++) {
								if (i > 0) {
									sb.append(", ");
								}
								sb.append(ia[i]);
							}
						} else if (Double.TYPE.equals(componentType)) {
							double[] ia = (double[]) o;
							for (int i = 0; i < ia.length; i++) {
								if (i > 0) {
									sb.append(", ");
								}
								sb.append(ia[i]);
							}
						} else if (Float.TYPE.equals(componentType)) {
							float[] ia = (float[]) o;
							for (int i = 0; i < ia.length; i++) {
								if (i > 0) {
									sb.append(", ");
								}
								sb.append(ia[i]);
							}
						} else if (Character.TYPE.equals(componentType)) {
							char[] ia = (char[]) o;
							for (int i = 0; i < ia.length; i++) {
								if (i > 0) {
									sb.append(", ");
								}
								sb.append(ia[i]);
							}
						} else if (Short.TYPE.equals(componentType)) {
							short[] ia = (short[]) o;
							for (int i = 0; i < ia.length; i++) {
								if (i > 0) {
									sb.append(", ");
								}
								sb.append(ia[i]);
							}
						} else if (Byte.TYPE.equals(componentType)) {
							byte[] ia = (byte[]) o;
							for (int i = 0; i < ia.length; i++) {
								if (i > 0) {
									sb.append(", ");
								}
								sb.append(ia[i]);
							}
						}
					} else {
						Object[] oa = (Object[]) o;
						for (int i = 0; i < oa.length; i++) {
							if (i > 0) {
								sb.append(", ");
							}
							sb.append(render(oa[i]));
						}
					}
					sb.append("]");
					return sb.toString();
				} else {
					return o.toString();
				}
			} catch (Exception e) {
				Long now = new Long(System.currentTimeMillis());
				unrenderableClasses.put(objectClass, now);
			}
		}
		return o.getClass().getName() + "@" + Integer.toHexString(o.hashCode());
	}
hasChildren() {
    return ((children != null) && (children.size() > 0));
  }
testBug72() {
    Logger logger = LoggerFactory.getLogger(PerfTest.class);
    int len = 2000;
    for (int i = 0; i < len; i++) {
      logger.debug("hello");
    }
    
    long start = System.currentTimeMillis();
    for (int i = 0; i < len; i++) {
      logger.debug("hello");
    }

    long end = System.currentTimeMillis();
    
    long duration = end-start;
    // when the code is guarded by a logger.isLoggable condition, 
    // duration is about 16 *micro*seconds for 1000 iterations
    // when it is not guarded the figure is 90 milliseconds,
    // i.e a ration of 1 to 5000
    assertTrue(duration <= 5);
  }
isEnabledFor(Level l) {
    switch (l.level) {
    case Level.TRACE_INT:
      return slf4jLogger.isTraceEnabled();
    case Level.DEBUG_INT:
      return slf4jLogger.isDebugEnabled();
    case Level.INFO_INT:
      return slf4jLogger.isInfoEnabled();
    case Level.WARN_INT:
      return slf4jLogger.isWarnEnabled();
    case Level.ERROR_INT:
      return slf4jLogger.isErrorEnabled();
    case Priority.FATAL_INT:
      return slf4jLogger.isErrorEnabled();
    }
    return false;
  }
arrayFormat(String messagePattern, Object[] argArray) {
    if (messagePattern == null) {
      return null;
    }
    int i = 0;
    int len = messagePattern.length();
    int j = messagePattern.indexOf(DELIM_START);

    if(argArray == null) {
      return messagePattern;
    }
    
    StringBuffer sbuf = new StringBuffer(messagePattern.length() + 50);

    for (int L = 0; L < argArray.length; L++) {

      j = messagePattern.indexOf(DELIM_START, i);

      if (j == -1 || (j + 1 == len)) {
        // no more variables
        if (i == 0) { // this is a simple string
          return messagePattern;
        } else { // add the tail string which contains no variables and return
          // the result.
          sbuf.append(messagePattern.substring(i, messagePattern.length()));
          return sbuf.toString();
        }
      } else {
        char delimStop = messagePattern.charAt(j + 1);

        if (isEscapedDelimeter(messagePattern, j)) {
          if(!isDoubleEscaped(messagePattern, j)) {
            L--; // DELIM_START was escaped, thus should not be incremented
            sbuf.append(messagePattern.substring(i, j - 1));
            sbuf.append(DELIM_START);
            i = j + 1;
          } else {
            // The escape character preceding the delemiter start is
            // itself escaped: "abc x:\\{}"
            // we have to consume one backward slash
            sbuf.append(messagePattern.substring(i, j-1));
            sbuf.append(argArray[L]);
            i = j + 2;
          }
        } else if ((delimStop != DELIM_STOP)) {
          // invalid DELIM_START/DELIM_STOP pair
          sbuf.append(messagePattern.substring(i, messagePattern.length()));
          return sbuf.toString();
        } else {
          // normal case
          sbuf.append(messagePattern.substring(i, j));
          sbuf.append(argArray[L]);
          i = j + 2;
        }
      }
    }
    // append the characters following the last {} pair.
    sbuf.append(messagePattern.substring(i, messagePattern.length()));
    return sbuf.toString();
  }
print() {
    DurationUnit du = Util.selectDurationUnitForDisplay(globalStopWatch);
    String r = buildString(du, "+", "");
    System.out.println(r);
  }
StopWatch(String name) {
    this.name = name;
    this.startTime = System.nanoTime();
    this.status = Status.STARTED;
  }
install(SLF4JBridgeHandler handler) {
    LogManager.getLogManager().reset();
    LogManager.getLogManager().getLogger("").addHandler(handler);
  }
publish(LogRecord record) {
    /*
     * Silently ignore null records.
     */
    if (record == null) {
      return;
    }
    /*
     * Get our SLF4J logger for publishing the record.
     */
    Logger publisher = getPublisher(record);
    Throwable thrown = record.getThrown(); // can be null!
    String message = record.getMessage(); // can be null!
    if (format && getFormatter() != null) {
      try {
        message = getFormatter().format(record);
      } catch (Exception ex) {
        reportError(null, ex, ErrorManager.FORMAT_FAILURE);
        return;
      }
    }
    if (message == null) {
      return;
    }
    /*
     * TRACE
     */
    if (record.getLevel().intValue() <= Level.FINEST.intValue()) {
      publisher.trace(message, thrown);
      return;
    }
    /*
     * DEBUG
     */
    if (record.getLevel() == Level.FINER) {
      publisher.debug(message, thrown);
      return;
    }
    if (record.getLevel() == Level.FINE) {
      publisher.debug(message, thrown);
      return;
    }
    /*
     * INFO
     */
    if (record.getLevel() == Level.CONFIG) {
      publisher.info(message, thrown);
      return;
    }
    if (record.getLevel() == Level.INFO) {
      publisher.info(message, thrown);
      return;
    }
    /*
     * WARN
     */
    if (record.getLevel() == Level.WARNING) {
      publisher.warn(message);
      return;
    }
    /*
     * ERROR
     */
    if (record.getLevel().intValue() >= Level.SEVERE.intValue()) {
      publisher.error(message, thrown);
      return;
    }
    /*
     * Still here? Fallback and out.
     */
    publishFallback(record, publisher);
  }
convert(File file, byte[] input) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(input);
    Reader reader = new InputStreamReader(bais);
    BufferedReader breader = new BufferedReader(reader);
    FileWriter fileWriter = new FileWriter(file);
    while (true) {
      String line = breader.readLine();
      if (line != null) {
        String newLine = lineConverter.getReplacement(line);
        fileWriter.write(newLine);
        fileWriter.write(lineTerminator);
      } else {
        fileWriter.close();
        break;
      }
    }
  }
showException(){
    converter.printException();
  }
ConverterFrame(Converter parent) {
    this.converter = parent;

    setTitle("SLF4J CONVERTER");
    // setIconImage();

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    setPreferredSize(new Dimension(300, 200));
    setLocation(100, 100);

    principalPan = new JPanel();
    FlowLayout fl = new FlowLayout();
    principalPan.setLayout(fl);

    confirmationPan = new JPanel();
    confirmationPan.setLayout(fl);

    JLabel lab = new JLabel("Conversion Mode");
    principalPan.add(lab);
    String choice[] = { "JCL to SLF4J", "Log4J to SLF4J" };
    combo = new JComboBox(choice);
    principalPan.add(combo);

    butCancel = new JButton("Cancel");
    butCancel.addActionListener(this);
    principalPan.add(butCancel);
    butNext = new JButton("Next");
    butNext.addActionListener(this);
    principalPan.add(butNext);

    setContentPane(principalPan);
    pack();
    setVisible(true);
  }
convert(List<File> lstFiles) {
		Iterator<File> itFile = lstFiles.iterator();
		while (itFile.hasNext()) {
			File currentFile = itFile.next();
			File newFile = new File(currentFile.getAbsolutePath() + "new");
			// logger.info("reading file " + currentFile.getAbsolutePath());
			try {
				boolean isEmpty = false;
				writer.initFileWriter(newFile);
				FileReader freader = new FileReader(currentFile);
				BufferedReader breader = new BufferedReader(freader);
				String line;
				while (!isEmpty) {
					line = breader.readLine();
					if (line != null) {
						// logger.info("reading line " + line);
						matcher.matches(line);
					} else {
						isEmpty = true;
						writer.closeFileWriter();
						copy(newFile, currentFile);
						delete(newFile);
					}
				}
			} catch (IOException exc) {
				logger.error("error reading file " + exc);
			}
		}
	}
format(String messagePattern, Object argument) {
    int j = messagePattern.indexOf(DELIM_START);
    int len = messagePattern.length();
    char escape = 'x';

    // if there are no { characters or { is the last character of the messsage
    // then we just return messagePattern
    if (j == -1 || (j+1 == len)) {
      return messagePattern;
    } else {
      if(j+1 == len) {
      }
      
      char delimStop = messagePattern.charAt(j + 1);
      if (j > 0) {
        escape = messagePattern.charAt(j - 1);
      }
      if ((delimStop != DELIM_STOP) || (escape == '\\')) {
        // invalid DELIM_START/DELIM_STOP pair or espace character is
        // present
        return messagePattern;
      } else {
        StringBuffer sbuf = new StringBuffer(len + 20);
        sbuf.append(messagePattern.substring(0, j));
        sbuf.append(argument);
        sbuf.append(messagePattern.substring(j + 2));
        return sbuf.toString();
      }
    }
  }
format(String messagePattern, Object arg1, Object arg2) {
    int i = 0;
    int len = messagePattern.length();
    int j = messagePattern.indexOf(DELIM_START);

    StringBuffer sbuf = new StringBuffer(messagePattern.length() + 50);

    for (int L = 0; L < 2; L++) {
      j = messagePattern.indexOf(DELIM_START, i);

      if (j == -1 || (j+1 == len)) {
        // no more variables
        if (i == 0) { // this is a simple string
          return messagePattern;
        } else { // add the tail string which contains no variables and return the result.
          sbuf.append(messagePattern.substring(i, messagePattern.length()));
          return sbuf.toString();
        }
      } else {
        char delimStop = messagePattern.charAt(j + 1);
        if ((delimStop != DELIM_STOP)) {
          // invalid DELIM_START/DELIM_STOP pair
          sbuf.append(messagePattern.substring(i, messagePattern.length()));
          return sbuf.toString();
        }
        sbuf.append(messagePattern.substring(i, j));
        sbuf.append((L == 0) ? arg1 : arg2);
        i = j + 2;
      }
    }
    // append the characters following the second {} pair.
    sbuf.append(messagePattern.substring(i, messagePattern.length()));
    return sbuf.toString();
  }
newInstance(String name) throws LogConfigurationException {
    Logger logger = LoggerFactory.getLogger(name);
    return new SLF4JLog(logger);
  }

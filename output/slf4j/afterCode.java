{
        mockHandler = new MockHandler();
        removeHandlers(logger);
        logger.addHandler(mockHandler);
    }
{
        Util.report("The following set of substitute loggers may have been accessed");
        Util.report("during the initialization phase. Logging calls during this");
        Util.report("phase were not honored. However, subsequent logging calls to these");
        Util.report("loggers will work as normally expected.");
        Util.report("See also " + SUBSTITUTE_LOGGER_URL);
    }
{

        if (messagePattern == null) {
            return new FormattingTuple(null, argArray, throwable);
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
                    return new FormattingTuple(messagePattern, argArray, throwable);
                } else { // add the tail string which contains no variables and return
                    // the result.
                    sbuf.append(messagePattern, i, messagePattern.length());
                    return new FormattingTuple(sbuf.toString(), argArray, throwable);
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
        return new FormattingTuple(sbuf.toString(), argArray, throwable);
    }
{
        Level julLevel;
        switch (slf4jLevelInt) {
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
            throw new IllegalStateException("Level number " + slf4jLevelInt + " is not recognized.");
        }
        return julLevel;
    }
{
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
        return log4jLevel;
    }
{
        if (Log.isLoggable(name, priority)) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            _log(name, priority, ft.getMessage(), ft.getThrowable());
        }
    }
{
    Date now = new Date();
    String dateText;
    synchronized (DATE_FORMATTER) {
      dateText = DATE_FORMATTER.format(now);
    }
    return dateText;
  }
{
    if (argArray == null || argArray.length == 0) {
      return null;
    }
    
    final Object lastEntry = argArray[argArray.length - 1];
    if(lastEntry instanceof Throwable) {
      return (Throwable) lastEntry;
    }
    return null;
  }
{
    INITIALIZATION_STATE = FAILED_INITILIZATION;
    Util.report("Failed to instantiate SLF4J LoggerFactory", t);
  }
{
		Class componentType = objectClass.getComponentType();
		StringBuffer sb = new StringBuffer("[");

		if (componentType.isPrimitive() == false) {
			Object[] oa = (Object[]) o;
			for (int i = 0; i < oa.length; i++) {
				if (i > 0) {
					sb.append(ELEMENT_SEPARATOR);
				}
				sb.append(render(oa[i]));
			}
		} else {
			if (Boolean.TYPE.equals(componentType)) {
				boolean[] ba = (boolean[]) o;
				for (int i = 0; i < ba.length; i++) {
					if (i > 0) {
						sb.append(ELEMENT_SEPARATOR);
					}
					sb.append(ba[i]);
				}
			} else if (Integer.TYPE.equals(componentType)) {
				int[] ia = (int[]) o;
				for (int i = 0; i < ia.length; i++) {
					if (i > 0) {
						sb.append(ELEMENT_SEPARATOR);
					}
					sb.append(ia[i]);
				}

			} else if (Long.TYPE.equals(componentType)) {
				long[] ia = (long[]) o;
				for (int i = 0; i < ia.length; i++) {
					if (i > 0) {
						sb.append(ELEMENT_SEPARATOR);
					}
					sb.append(ia[i]);
				}
			} else if (Double.TYPE.equals(componentType)) {
				double[] ia = (double[]) o;
				for (int i = 0; i < ia.length; i++) {
					if (i > 0) {
						sb.append(ELEMENT_SEPARATOR);
					}
					sb.append(ia[i]);
				}
			} else if (Float.TYPE.equals(componentType)) {
				float[] ia = (float[]) o;
				for (int i = 0; i < ia.length; i++) {
					if (i > 0) {
						sb.append(ELEMENT_SEPARATOR);
					}
					sb.append(ia[i]);
				}
			} else if (Character.TYPE.equals(componentType)) {
				char[] ia = (char[]) o;
				for (int i = 0; i < ia.length; i++) {
					if (i > 0) {
						sb.append(ELEMENT_SEPARATOR);
					}
					sb.append(ia[i]);
				}
			} else if (Short.TYPE.equals(componentType)) {
				short[] ia = (short[]) o;
				for (int i = 0; i < ia.length; i++) {
					if (i > 0) {
						sb.append(ELEMENT_SEPARATOR);
					}
					sb.append(ia[i]);
				}
			} else if (Byte.TYPE.equals(componentType)) {
				byte[] ia = (byte[]) o;
				for (int i = 0; i < ia.length; i++) {
					if (i > 0) {
						sb.append(ELEMENT_SEPARATOR);
					}
					sb.append(ia[i]);
				}
			}
		}
		sb.append("]");
		return sb;
	}
{
    return ((refereceList != null) && (refereceList.size() > 0));
  }
{
    Logger logger = LoggerFactory.getLogger(PerfTest.class);
    long start = System.currentTimeMillis();
    for (int i = 0; i < len; i++) {
      logger.debug("hello");
    }

    long end = System.currentTimeMillis();

    long duration = end - start;
    return duration;
  }
{
    switch (p.level) {
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
{
    if (o != null && o.getClass().isArray()) {
      // check for primitive arrays because they unfortunately 
      // cannot be cast to Object[]
      if (o instanceof boolean[]) {
        sbuf.append(Arrays.toString((boolean[]) o));
      } else if (o instanceof byte[]) {
        sbuf.append(Arrays.toString((byte[]) o));
      } else if (o instanceof char[]) {
        sbuf.append(Arrays.toString((char[]) o));
      } else if (o instanceof short[]) {
        sbuf.append(Arrays.toString((short[]) o));
      } else if (o instanceof int[]) {
        sbuf.append(Arrays.toString((int[]) o));
      } else if (o instanceof long[]) {
        sbuf.append(Arrays.toString((long[]) o));
      } else if (o instanceof float[]) {
        sbuf.append(Arrays.toString((float[]) o));
      } else {
        sbuf.append(Arrays.toString((Object[]) o));
      }
    } else {
      sbuf.append(o);
    }
  }
{
    DurationUnit du = Util.selectDurationUnitForDisplay(globalStopWatch);
    return buildProfilerString(du, TOP_PROFILER_FIRST_PREFIX, TOTAL_ELAPSED, "");
  }
{
    this.name = name;
    startTime = System.nanoTime();
    status = TimeInstrumentStatus.STARTED;
  }
{
    LogManager.getLogManager().reset();
    LogManager.getLogManager().getLogger("").addHandler(new SLF4JBridgeHandler());
  }
{
    int julLevelValue = record.getLevel().intValue();
    if (julLevelValue <= TRACE_LEVEL_THRESHOLD) {
      slf4jLogger.trace(record.getMessage(), record.getThrown());
    } else if (julLevelValue <= DEBUG_LEVEL_THRESHOLD) {
      slf4jLogger.debug(record.getMessage(), record.getThrown());
    } else if (julLevelValue <= INFO_LEVEL_THRESHOLD) {
      slf4jLogger.info(record.getMessage(), record.getThrown());
    } else if (julLevelValue <= WARN_LEVEL_THRESHOLD) {
      slf4jLogger.warn(record.getMessage(), record.getThrown());
    } else {
      slf4jLogger.error(record.getMessage(), record.getThrown());
    }
  }
{
    for (int i = 0; i < replacement.length; i++) {
      writer.write(replacement[i]);
      writer.write(lineTerminator);
    }
  }
{
    int reponse = JOptionPane.showConfirmDialog(null,
        "RUNNING CONVERTER WILL REPLACE JAVA FILES INTO " + folder,
        "CONVERSION CONFIRMATION", JOptionPane.YES_NO_OPTION);
    if (reponse == JOptionPane.YES_OPTION) {
      converter.convertProject(folder);
      converter.printException();
    } else {
      dispose();
    }
  }
{
    setTitle("SLF4J CONVERTER");
    // setIconImage()
    setLocationRelativeTo(null);
    this.setPreferredSize(new Dimension(512,384));
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    buildComponents();
  }
{
		File newFile = new File(file.getAbsolutePath() + "new");
		try {
			boolean isEmpty = false;
			writer.initFileWriter(newFile);
			FileReader freader = new FileReader(file);
			BufferedReader breader = new BufferedReader(freader);
			String line;
			String newLine;
			while (!isEmpty) {
				line = breader.readLine();
				if (line != null) {
					newLine = matcher.replace(line);
					writer.write(newLine);
				} else {
					isEmpty = true;
					writer.closeFileWriter();
					copy(newFile, file);
					delete(newFile);
				}
			}
		} catch (IOException exc) {
			System.out.println("error reading file " + exc);
		}
	}
{
    if(messagePattern == null) {
      return null;
    }
    int i = 0;
    int len = messagePattern.length();
    int j = messagePattern.indexOf(DELIM_START);
    
    char escape = 'x';
    
    StringBuffer sbuf = new StringBuffer(messagePattern.length() + 50);

    for (int L = 0; L < argArray.length; L++) {
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
        if (j > 0) {
          escape = messagePattern.charAt(j - 1);
        }
        
        if(escape == '\\') {
          sbuf.append(messagePattern.substring(i, j));
          sbuf.append(DELIM_START);
          i = j + 1;
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
    // append the characters following the second {} pair.
    sbuf.append(messagePattern.substring(i, messagePattern.length()));
    return sbuf.toString();
  }
{

    Log instance = (Log) loggerMap.get(name);
    if (instance == null) {
      Logger logger = LoggerFactory.getLogger(name);
      instance = new SLF4JLog(logger);
      loggerMap.put(name, instance);
    }
    return (instance);

  }

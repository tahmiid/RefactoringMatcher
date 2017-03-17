{
        if (isEmpty(str) || isEmpty(abbrevMarker)) {
            return str;
        }

        final int abbrevMarkerLength = abbrevMarker.length();
        final int minAbbrevWidth = abbrevMarkerLength + 1;
        final int minAbbrevWidthOffset = abbrevMarkerLength + abbrevMarkerLength + 1;

        if (maxWidth < minAbbrevWidth) {
            throw new IllegalArgumentException(String.format("Minimum abbreviation width is %d", minAbbrevWidth));
        }
        if (str.length() <= maxWidth) {
            return str;
        }
        if (offset > str.length()) {
            offset = str.length();
        }
        if (str.length() - offset < maxWidth - abbrevMarkerLength) {
            offset = str.length() - (maxWidth - abbrevMarkerLength);
        }
        if (offset <= abbrevMarkerLength+1) {
            return str.substring(0, maxWidth - abbrevMarkerLength) + abbrevMarker;
        }
        if (maxWidth < minAbbrevWidthOffset) {
            throw new IllegalArgumentException(String.format("Minimum abbreviation width with offset is %d", minAbbrevWidthOffset));
        }
        if (offset + maxWidth - abbrevMarkerLength < str.length()) {
            return abbrevMarker + abbreviate(str.substring(offset), abbrevMarker, maxWidth - abbrevMarkerLength);
        }
        return abbrevMarker + str.substring(str.length() - (maxWidth - abbrevMarkerLength));
    }
{
        if (isShutdown()) {
            throw new IllegalStateException("TimedSemaphore is shut down!");
        }

        if (task == null) {
            task = startTimer();
        }
    }
{
        if (getLimit() <= NO_LIMIT || acquireCount < getLimit()) {
            acquireCount++;
            return true;
        }
        return false;
    }
{
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        final char[] chars = str.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        boolean isJava6 = StringUtils.startsWith(System.getProperty("java.version"), "1.6");
        // deal with any possible sign up front
        final int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
        final boolean hasLeadingPlusSign = (start == 1 && chars[0] == '+');
        if (sz > start + 1 && chars[start] == '0') { // leading 0
            if (
                 (chars[start + 1] == 'x') || 
                 (chars[start + 1] == 'X') 
            ) { // leading 0x/0X
                int i = start + 2;
                if (i == sz) {
                    return false; // str == "0x"
                }
                // checking hex (it can't be anything else)
                for (; i < chars.length; i++) {
                    if ((chars[i] < '0' || chars[i] > '9')
                        && (chars[i] < 'a' || chars[i] > 'f')
                        && (chars[i] < 'A' || chars[i] > 'F')) {
                        return false;
                    }
                }
                return true;
           } else if (Character.isDigit(chars[start + 1])) {
               // leading 0, but not hex, must be octal
               int i = start + 1;
               for (; i < chars.length; i++) {
                   if (chars[i] < '0' || chars[i] > '7') {
                       return false;
                   }
               }
               return true;               
           }
        }
        sz--; // don't want to loop to the last char, check it afterwords
              // for type qualifiers
        int i = start;
        // loop to the next to last char or to the last char if we need another digit to
        // make a valid number (e.g. chars[0..5] = "1234E")
        while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;

            } else if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent   
                    return false;
                }
                hasDecPoint = true;
            } else if (chars[i] == 'e' || chars[i] == 'E') {
                // we've already taken care of hex.
                if (hasExp) {
                    // two E's
                    return false;
                }
                if (!foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            } else if (chars[i] == '+' || chars[i] == '-') {
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false; // we need a digit after the E
            } else {
                return false;
            }
            i++;
        }
        if (i < chars.length) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                if (isJava6 && hasLeadingPlusSign && !hasDecPoint) {
                    return false;
                }
                // no type qualifier, OK
                return true;
            }
            if (chars[i] == 'e' || chars[i] == 'E') {
                // can't have an E at the last byte
                return false;
            }
            if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return false;
                }
                // single trailing decimal point after non-exponent is ok
                return foundDigit;
            }
            if (!allowSigns
                && (chars[i] == 'd'
                    || chars[i] == 'D'
                    || chars[i] == 'f'
                    || chars[i] == 'F')) {
                return foundDigit;
            }
            if (chars[i] == 'l'
                || chars[i] == 'L') {
                // not allowing L with an exponent or decimal point
                return foundDigit && !hasExp && !hasDecPoint;
            }
            // last character is illegal
            return false;
        }
        // allowSigns is true iff the val ends in 'E'
        // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
        return !allowSigns && foundDigit;
    }
{
        // switch on type of array, to dispatch to the correct handler
        // handles multi dimensional arrays
        // throws a ClassCastException if rhs is not the correct array type
        if (lhs instanceof long[]) {
            append((long[]) lhs, (long[]) rhs);
        } else if (lhs instanceof int[]) {
            append((int[]) lhs, (int[]) rhs);
        } else if (lhs instanceof short[]) {
            append((short[]) lhs, (short[]) rhs);
        } else if (lhs instanceof char[]) {
            append((char[]) lhs, (char[]) rhs);
        } else if (lhs instanceof byte[]) {
            append((byte[]) lhs, (byte[]) rhs);
        } else if (lhs instanceof double[]) {
            append((double[]) lhs, (double[]) rhs);
        } else if (lhs instanceof float[]) {
            append((float[]) lhs, (float[]) rhs);
        } else if (lhs instanceof boolean[]) {
            append((boolean[]) lhs, (boolean[]) rhs);
        } else {
            // not an array of primitives
            // throws a ClassCastException if rhs is not an array
            append((Object[]) lhs, (Object[]) rhs, comparator);
        }
    }
{
        if (str == null) {
            return null;
        }
        if (newLineStr == null) {
            newLineStr = SystemUtils.LINE_SEPARATOR;
        }
        if (wrapLength < 1) {
            wrapLength = 1;
        }
        if (StringUtils.isBlank(wrapOn)) {
            wrapOn = " ";
        }
        Pattern patternToWrapOn = Pattern.compile(wrapOn);
        final int inputLineLength = str.length();
        int offset = 0;
        final StringBuilder wrappedLine = new StringBuilder(inputLineLength + 32);

        while (offset < inputLineLength) {
            int spaceToWrapAt = -1;
            Matcher matcher = patternToWrapOn.matcher(str.substring(offset, Math.min(offset + wrapLength + 1, inputLineLength)));
            if (matcher.find()) {
                if (matcher.start() == 0) {
                    offset += matcher.end();
                    continue;
                }else {
                    spaceToWrapAt = matcher.start();
                }
            }

            // only last line without leading spaces is left
            if(inputLineLength - offset <= wrapLength) {
                break;
            }

            while(matcher.find()){
                spaceToWrapAt = matcher.start() + offset;
            }

            if (spaceToWrapAt >= offset) {
                // normal case
                wrappedLine.append(str.substring(offset, spaceToWrapAt));
                wrappedLine.append(newLineStr);
                offset = spaceToWrapAt + 1;

            } else {
                // really long word or URL
                if (wrapLongWords) {
                    // wrap really long word one line at a time
                    wrappedLine.append(str.substring(offset, wrapLength + offset));
                    wrappedLine.append(newLineStr);
                    offset += wrapLength;
                } else {
                    // do not wrap really long word, just extend beyond limit
                    matcher = patternToWrapOn.matcher(str.substring(offset + wrapLength));
                    if (matcher.find()) {
                        spaceToWrapAt = matcher.start() + offset + wrapLength;
                    }

                    if (spaceToWrapAt >= 0) {
                        wrappedLine.append(str.substring(offset, spaceToWrapAt));
                        wrappedLine.append(newLineStr);
                        offset = spaceToWrapAt + 1;
                    } else {
                        wrappedLine.append(str.substring(offset));
                        offset = inputLineLength;
                    }
                }
            }
        }

        // Whatever is left in line is short enough to just pass through
        wrappedLine.append(str.substring(offset));

        return wrappedLine.toString();
    }
{
        // 'Switch' on type of array, to dispatch to the correct handler
        // This handles multi dimensional arrays
        if (object instanceof long[]) {
            append((long[]) object);
        } else if (object instanceof int[]) {
            append((int[]) object);
        } else if (object instanceof short[]) {
            append((short[]) object);
        } else if (object instanceof char[]) {
            append((char[]) object);
        } else if (object instanceof byte[]) {
            append((byte[]) object);
        } else if (object instanceof double[]) {
            append((double[]) object);
        } else if (object instanceof float[]) {
            append((float[]) object);
        } else if (object instanceof boolean[]) {
            append((boolean[]) object);
        } else {
            // Not an array of primitives
            append((Object[]) object);
        }
    }
{
        parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
        args = ArrayUtils.nullToEmpty(args);
        
        final String messagePrefix;
        Method method = null;
        boolean isOriginallyAccessible = false;
        Object result = null;
        
        try {
            if (forceAccess) {
            	messagePrefix = "No such method: ";
            	method = getMatchingMethod(object.getClass(),
                        methodName, parameterTypes);
            	if (method != null) {
            	    isOriginallyAccessible = method.isAccessible();
            	    if (!isOriginallyAccessible) {
            	        method.setAccessible(true);
            	    }
            	}
            }  else {
            	messagePrefix = "No such accessible method: ";
            	method = getMatchingAccessibleMethod(object.getClass(),
                        methodName, parameterTypes);
            }
            
            if (method == null) {
                throw new NoSuchMethodException(messagePrefix
                        + methodName + "() on object: "
                        + object.getClass().getName());
            }
            args = toVarArgs(method, args);
            
            result = method.invoke(object, args);
        }
        finally {
            if (method != null && forceAccess && method.isAccessible() != isOriginallyAccessible) {
                method.setAccessible(isOriginallyAccessible);
            }
        }
        
        return result;
    }
{
         if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
             return text;
         }
         String searchText = text;
         if (ignoreCase) {
             searchText = text.toLowerCase();
             searchString = searchString.toLowerCase();
         }
         int start = 0;
         int end = searchText.indexOf(searchString, start);
         if (end == INDEX_NOT_FOUND) {
             return text;
         }
         final int replLength = searchString.length();
         int increase = replacement.length() - replLength;
         increase = increase < 0 ? 0 : increase;
         increase *= max < 0 ? 16 : max > 64 ? 64 : max;
         final StringBuilder buf = new StringBuilder(text.length() + increase);
         while (end != INDEX_NOT_FOUND) {
             buf.append(text.substring(start, end)).append(replacement);
             start = end + replLength;
             if (--max == 0) {
                 break;
             }
             end = searchText.indexOf(searchString, start);
         }
         buf.append(text.substring(start));
         return buf.toString();
     }
{
        if (lhs.getClass() != rhs.getClass()) {
            // Here when we compare different dimensions, for example: a boolean[][] to a boolean[]
            this.setEquals(false);
        }
        // 'Switch' on type of array, to dispatch to the correct handler
        // This handles multi dimensional arrays of the same depth
        else if (lhs instanceof long[]) {
            append((long[]) lhs, (long[]) rhs);
        } else if (lhs instanceof int[]) {
            append((int[]) lhs, (int[]) rhs);
        } else if (lhs instanceof short[]) {
            append((short[]) lhs, (short[]) rhs);
        } else if (lhs instanceof char[]) {
            append((char[]) lhs, (char[]) rhs);
        } else if (lhs instanceof byte[]) {
            append((byte[]) lhs, (byte[]) rhs);
        } else if (lhs instanceof double[]) {
            append((double[]) lhs, (double[]) rhs);
        } else if (lhs instanceof float[]) {
            append((float[]) lhs, (float[]) rhs);
        } else if (lhs instanceof boolean[]) {
            append((boolean[]) lhs, (boolean[]) rhs);
        } else {
            // Not an array of primitives
            append((Object[]) lhs, (Object[]) rhs);
        }
    }
{
        return printer.format(millis, buf);
    }
{
        return printer.format(date, buf);
    }
{
        return printer.format(calendar, buf);
    }
{
        Validate.notNull(listener, "Listener object cannot be null.");
        if (allowDuplicate) {
            listeners.add(listener);
        } else if (!allowDuplicate && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
{
        final String toString = ReflectionToStringBuilder.toString(new TestFixture());

        Assert.assertEquals(ArrayUtils.INDEX_NOT_FOUND, toString.indexOf(SECRET_FIELD));
        Assert.assertEquals(ArrayUtils.INDEX_NOT_FOUND, toString.indexOf(SECRET_VALUE));
        Assert.assertTrue(toString.indexOf(NOT_SECRET_FIELD) > ArrayUtils.INDEX_NOT_FOUND);
        Assert.assertTrue(toString.indexOf(NOT_SECRET_VALUE) > ArrayUtils.INDEX_NOT_FOUND);
    }
{
        final Calendar cal = Calendar.getInstance(timeZone);
        cal.set(2003, Calendar.JUNE, 8, 10, 11, 12);
        return cal;
    }
{
        final TimeZone timeZone = TimeZone.getTimeZone("GMT-3");
        assertFormats(expectedValue, pattern, timeZone, createFebruaryTestDate(timeZone));
    }
{
        assertArrayEquals(new Field[0], FieldUtils.getAllFields(Object.class));
        final Field[] fieldsNumber = Number.class.getDeclaredFields();
        assertArrayEquals(fieldsNumber, FieldUtils.getAllFields(Number.class));
        final Field[] fieldsInteger = Integer.class.getDeclaredFields();
        assertArrayEquals(ArrayUtils.addAll(fieldsInteger, fieldsNumber), FieldUtils.getAllFields(Integer.class));
        assertEquals(5, FieldUtils.getAllFields(PublicChild.class).length);
    }
{
        Class<? super PublicChild> parentClass = PublicChild.class.getSuperclass();
		final Field[] fieldsParent =  parentClass.getDeclaredFields();
        assertArrayEquals(fieldsParent, FieldUtils.getAllFields(parentClass));

		final Field[] fieldsPublicChild = PublicChild.class.getDeclaredFields();
        return ArrayUtils.addAll(fieldsPublicChild, fieldsParent);
    }
{
        final Field[] fieldsNumber = Number.class.getDeclaredFields();
        assertArrayEquals(Number.class.getDeclaredFields(), FieldUtils.getAllFields(Number.class));
        final Field[] fieldsInteger = Integer.class.getDeclaredFields();
        return ArrayUtils.addAll(fieldsInteger, fieldsNumber);
    }
{
        final ConcurrentInitializer<Object> initializer = createInitializer();
        final int threadCount = 20;
        final CountDownLatch startLatch = new CountDownLatch(1);
        class GetThread extends Thread {
            Object object;

            @Override
            public void run() {
                try {
                    // wait until all threads are ready for maximum parallelism
                    startLatch.await();
                    // access the initializer
                    object = initializer.get();
                } catch (final InterruptedException iex) {
                    // ignore
                } catch (final ConcurrentException cex) {
                    object = cex;
                }
            }
        }

        final GetThread[] threads = new GetThread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new GetThread();
            threads[i].start();
        }

        // fire all threads and wait until they are ready
        startLatch.countDown();
        for (final Thread t : threads) {
            t.join();
        }

        // check results
        final Object managedObject = initializer.get();
        for (final GetThread t : threads) {
            assertEquals("Wrong object", managedObject, t.object);
        }
    }
{

        final ConcurrentInitializer<Object> initializer = expectExceptions ?
                createExceptionThrowingInitializer() :
                createInitializer();

        final int threadCount = 20;
        final CountDownLatch startLatch = new CountDownLatch(1);
        class GetThread extends Thread {
            Object object;

            @Override
            public void run() {
                try {
                    // wait until all threads are ready for maximum parallelism
                    startLatch.await();
                    // access the initializer
                    object = initializer.get();
                } catch (final InterruptedException iex) {
                    // ignore
                } catch (final ConcurrentException cex) {
                    object = cex;
                }
            }
        }

        final GetThread[] threads = new GetThread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new GetThread();
            threads[i].start();
        }

        // fire all threads and wait until they are ready
        startLatch.countDown();
        for (final Thread t : threads) {
            t.join();
        }

        // check results
        if ( expectExceptions ) {
            for (GetThread t : threads) {
                assertTrue(t.object instanceof Exception);
                Exception exc = (Exception) t.object;
                assertEquals(expectedMessage, exc.getMessage());
                assertSame(expectedCause, exc.getCause());
            }
        } else {
            final Object managedObject = initializer.get();
            for (final GetThread t : threads) {
                assertEquals("Wrong object", managedObject, t.object);
            }
        }
    }
{
		final Date expectedTime = sdf.parse(formattedDate);
        final Date actualTime = fdf.parse(formattedDate);
        assertEquals(locale.toString()+" "+formattedDate +"\n",expectedTime, actualTime);
	}
{
        Validate.isTrue(field != null, "The field must not be null");

        try {
            if (Modifier.isFinal(field.getModifiers())) {
                // Do all JREs implement Field with a private ivar called "modifiers"?
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                final boolean doForceAccess = forceAccess && !modifiersField.isAccessible();
                if (doForceAccess) {
                    modifiersField.setAccessible(true);
                }
                try {
                    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                } finally {
                    if (doForceAccess) {
                        modifiersField.setAccessible(false);
                    }
                }
            }
        } catch (NoSuchFieldException ignored) {
            // The field class contains always a modifiers field
        } catch (IllegalAccessException ignored) {
            // The modifiers field is made accessible
        }
    }
{
        Calendar cal = Calendar.getInstance(zone, locale);
        cal.clear();

        // http://docs.oracle.com/javase/6/docs/technotes/guides/intl/calendar.doc.html
        if (locale.equals(FastDateParser.JAPANESE_IMPERIAL)) {
            if(year < 1868) {
                cal.set(Calendar.ERA, 0);
                cal.set(Calendar.YEAR, 1868-year);
            }
        }
        else {
            if (year < 0) {
                cal.set(Calendar.ERA, GregorianCalendar.BC);
                year= -year;
            }
            cal.set(Calendar.YEAR, year/100 * 100);
        }
        return cal;
    }
{
        final SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        if (format.equals(SHORT_FORMAT)) {
            sdf.set2DigitYearStart( cs );
        }
        final String fmt = sdf.format(in);
        try {
            final Date out = fdp.parse(fmt);
            assertEquals(locale.toString()+" "+in+" "+ format+ " "+tz.getID(), in, out);
        } catch (final ParseException pe) {
            System.out.println(fmt+" "+locale.toString()+" "+year+" "+ format+ " "+tz.getID());
            throw pe;
        }
    }
{
        if (array == null) {
            return;
        }
        int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
        int j = Math.min(array.length, endIndexExclusive) - 1;
        boolean tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
{
        if (array == null) {
            return;
        }
        int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
        int j = Math.min(array.length, endIndexExclusive) - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
{
        if (array == null) {
            return;
        }
        int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
        int j = Math.min(array.length, endIndexExclusive) - 1;
        char tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
{
        if (array == null) {
            return;
        }
        int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
        int j = Math.min(array.length, endIndexExclusive) - 1;
        double tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
{
        if (array == null) {
            return;
        }
        int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
        int j = Math.min(array.length, endIndexExclusive) - 1;
        float tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
{
        if (array == null) {
            return;
        }
        int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
        int j = Math.min(array.length, endIndexExclusive) - 1;
        int tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
{
        if (array == null) {
            return;
        }
        int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
        int j = Math.min(array.length, endIndexExclusive) - 1;
        long tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
{
        if (array == null) {
            return;
        }
        int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
        int j = Math.min(array.length, endIndexExclusive) - 1;
        Object tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
{
        if (array == null) {
            return;
        }
        int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
        int j = Math.min(array.length, endIndexExclusive) - 1;
        short tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
{
        if (cls == null) {
            throw new IllegalArgumentException("The class must not be null");
        }
        List<Field> allFields = new ArrayList<Field>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            for (Field field : declaredFields) {
                allFields.add(field);
            }
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }
{
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (Array.getLength(array) == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
    }
{
        return applyRules(c, new StringBuffer(mMaxLengthEstimate)).toString();
    }
{
    	ConcurrentMap<Locale,Strategy> cache = getCache(field);
    	Strategy strategy= cache.get(field);
        if(strategy==null) {
        	strategy= field==Calendar.ZONE_OFFSET
        			? new TimeZoneStrategy(locale)
        			: new TextStrategy(field, definingCalendar, locale);
            Strategy inCache= cache.putIfAbsent(locale, strategy);
            if(inCache!=null) {
                return inCache;
            }
        }
        return strategy;
    }
{
                
        Calendar cal= Calendar.getInstance(GMT);
        cal.clear();
        cal.set(2003, 1, 10);
        if (eraBC) {
            cal.set(Calendar.ERA, GregorianCalendar.BC);
        }
        for(Locale locale : Locale.getAvailableLocales()) {
            SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
            DateParser fdf = getInstance(format, locale);

            try {
                checkParse(locale, cal, sdf, fdf);
            } catch(ParseException ex) {
                // TODO: why do ja_JP_JP, hi_IN, th_TH, and th_TH_TH fail?
                System.out.println("Locale "+locale+ " failed with "+format+" era "+(eraBC?"BC":"AD")+"\n" + trimMessage(ex.toString()));
            }
        }
    }
{
        final EnumSet<E> results = EnumSet.noneOf(asEnum(enumClass));
        values = ArrayUtils.clone(Validate.notNull(values));
        ArrayUtils.reverse(values);
        for (E constant : enumClass.getEnumConstants()) {
            int block = constant.ordinal() / Long.SIZE;
            if (block < values.length && (values[block] & 1 << (constant.ordinal() % Long.SIZE)) != 0) {
                results.add(constant);
            }
        }
        return results;
    }
{
        MultipartKey key = new MultipartKey(dateStyle, timeStyle, locale);

        String pattern = cDateTimeInstanceCache.get(key);
        if (pattern == null) {
            try {
                DateFormat formatter;
                if (dateStyle == null) {
                    formatter = DateFormat.getTimeInstance(timeStyle, locale);                    
                }
                else if (timeStyle == null) {
                    formatter = DateFormat.getDateInstance(dateStyle, locale);                    
                }
                else {
                    formatter = DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
                }
                pattern = ((SimpleDateFormat)formatter).toPattern();
                String previous = cDateTimeInstanceCache.putIfAbsent(key, pattern);
                if (previous != null) {
                    // even though it doesn't matter if another thread put the pattern
                    // it's still good practice to return the String instance that is
                    // actually in the ConcurrentMap
                    pattern= previous;
                }
            } catch (ClassCastException ex) {
                throw new IllegalArgumentException("No date time pattern for locale: " + locale);
            }
        }
        return pattern;
    }
{
        Validate.notNull(enumClass, "EnumClass must be defined.");

        final E[] constants = enumClass.getEnumConstants();
        Validate.isTrue(constants != null, "%s does not seem to be an Enum type", enumClass);
        Validate.isTrue(constants.length <= Long.SIZE, "Cannot store %s %s values in %s bits", constants.length,
            enumClass.getSimpleName(), Long.SIZE);

        return enumClass;
    }
{
        contextValues.add(new ImmutablePair<String, Object>(label, value));
        return this;
    }
{
        for (final Iterator<Pair<String, Object>> iter = contextValues.iterator(); iter.hasNext();) {
            final Pair<String, Object> p = iter.next();
            if (StringUtils.equals(label, p.getKey())) {
                iter.remove();
            }
        }
        addContextValue(label, value);
        return this;
    }
{
        if (str == null) {
            return null;
        }

        // handle negatives, which means last n characters
        if (start < 0) {
            start = str.length() + start; // remember start is negative
        }

        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return EMPTY;
        }

        return str.subSequence(start, str.length()).toString();
    }
{
        if (str == null) {
            return null;
        }

        // handle negatives
        if (end < 0) {
            end = str.length() + end; // remember end is negative
        }
        if (start < 0) {
            start = str.length() + start; // remember start is negative
        }

        // check length next
        if (end > str.length()) {
            end = str.length();
        }

        // if start is greater than end, return ""
        if (start > end) {
            return EMPTY;
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return str.subSequence(start, end).toString();
    }
{
        if (osName == null || osVersion == null) {
            return false;
        }
        return osName.startsWith(osNamePrefix) && osVersion.startsWith(osVersionPrefix);
    }
{
        if (javaVersions == null || javaVersions.length == 0) {
            return 0f;
        }
        if (javaVersions.length == 1) {
            return javaVersions[0];
        }
        StringBuilder builder = new StringBuilder();
        builder.append(javaVersions[0]);
        builder.append('.');
        for (int i = 1; i < javaVersions.length; i++) {
            builder.append(javaVersions[i]);
        }
        try {
            return Float.parseFloat(builder.toString());
        } catch (Exception ex) {
            return 0f;
        }
    }
{
        if (javaVersions == null) {
            return 0;
        }
        int intVersion = 0;
        int len = javaVersions.length;
        if (len >= 1) {
            intVersion = javaVersions[0] * 100;
        }
        if (len >= 2) {
            intVersion += javaVersions[1] * 10;
        }
        if (len >= 3) {
            intVersion += javaVersions[2];
        }
        return intVersion;
    }
{
        String linebreak = SystemUtils.LINE_SEPARATOR;
        StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
        List<String> list = new ArrayList<String>();
        while (frames.hasMoreTokens()) {
            list.add(frames.nextToken());
        }
        return list.toArray(new String[list.size()]);
    }
{
        if (str == null || parsePatterns == null) {
            throw new IllegalArgumentException("Date and Patterns must not be null");
        }
        
        SimpleDateFormat parser = null;
        ParsePosition pos = new ParsePosition(0);
        for (int i = 0; i < parsePatterns.length; i++) {

            String pattern = parsePatterns[i];

            // LANG-530 - need to make sure 'ZZ' output doesn't get passed to SimpleDateFormat
            if (parsePatterns[i].endsWith("ZZ")) {
                pattern = pattern.substring(0, pattern.length() - 1);
            }
            
            if (i == 0) {
                parser = new SimpleDateFormat(pattern);
                parser.setLenient(lenient);
            } else {
                parser.applyPattern(pattern); // cannot be null if i != 0
            }
            pos.setIndex(0);

            String str2 = str;
            // LANG-530 - need to make sure 'ZZ' output doesn't hit SimpleDateFormat as it will ParseException
            if (parsePatterns[i].endsWith("ZZ")) {
                str2 = str.replaceAll("([-+][0-9][0-9]):([0-9][0-9])$", "$1$2"); 
            }

            Date date = parser.parse(str2, pos);
            if (date != null && pos.getIndex() == str2.length()) {
                return date;
            }
        }
        throw new ParseException("Unable to parse the date: " + str, -1);
    }
{
        if (str == null || searchStr == null || ordinal <= 0) {
            return INDEX_NOT_FOUND;
        }
        if (searchStr.length() == 0) {
            return lastIndex ? str.length() : 0;
        }
        int found = 0;
        int index = lastIndex ? str.length() : INDEX_NOT_FOUND;
        do {
            if(lastIndex) {
                index = str.lastIndexOf(searchStr, index - 1);
            } else {
                index = str.indexOf(searchStr, index + 1);
            }
            if (index < 0) {
                return index;
            }
            found++;
        } while (found < ordinal);
        return index;
    }
{
    	String str = isEmpty() ? defaultIfEmpty : standard;
        if (str != null) {
            append(str);
        }
        return this;
    }
{
        if (size() > 0) {
            append(standard);
        }
        else {
        	append(defaultIfEmpty);
        }
        return this;
    }
{
        return this.escapingPlus;
    }
{
        while (cls != null) {
            Class<?>[] interfaces = cls.getInterfaces();

            for (Class<?> i : interfaces) {
                if (interfacesFound.add(i)) {
                    getAllInterfaces(i, interfacesFound);
                }
            }

            cls = cls.getSuperclass();
         }
     }
{
        if(cAvailableLocaleSet == null) {
            cAvailableLocaleSet = Collections.unmodifiableSet( new HashSet<Locale>(availableLocaleList()) );
        }
    }
{
        if (this.runningState == STATE_STOPPED || this.runningState == STATE_SUSPENDED) {
            return this.stopTime - this.startTime;
        } else if (this.runningState == STATE_UNSTARTED) {
            return 0;
        } else if (this.runningState == STATE_RUNNING) {
            return System.nanoTime() - this.startTime;
        }
        throw new RuntimeException("Illegal running state has occured. ");
    }
{
        if (this.splitState != STATE_SPLIT) {
            throw new IllegalStateException("Stopwatch must be split to get the split time. ");
        }
        return this.stopTime - this.startTime;
    }
{
        MessageFormat result = new MessageFormat(pattern);
        if (locale != null) {
            result.setLocale(locale);
            result.applyPattern(pattern);
        }
        return result;
    }
{
        if (ArrayUtils.isSameLength(classArray, toClassArray) == false) {
            return false;
        }
        if (classArray == null) {
            classArray = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        if (toClassArray == null) {
            toClassArray = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        for (int i = 0; i < classArray.length; i++) {
            if (isAssignable(classArray[i], toClassArray[i], autoboxing) == false) {
                return false;
            }
        }
        return true;
    }
{
        if (toClass == null) {
            return false;
        }
        // have to check for null, as isAssignableFrom doesn't
        if (cls == null) {
            return !(toClass.isPrimitive());
        }
        //autoboxing:
        if (autoboxing) {
            if (cls.isPrimitive() && !toClass.isPrimitive()) {
                cls = primitiveToWrapper(cls);
                if (cls == null) {
                    return false;
                }
            }
            if (toClass.isPrimitive() && !cls.isPrimitive()) {
                cls = wrapperToPrimitive(cls);
                if (cls == null) {
                    return false;
                }
            }
        }
        if (cls.equals(toClass)) {
            return true;
        }
        if (cls.isPrimitive()) {
            if (toClass.isPrimitive() == false) {
                return false;
            }
            if (Integer.TYPE.equals(cls)) {
                return Long.TYPE.equals(toClass)
                    || Float.TYPE.equals(toClass)
                    || Double.TYPE.equals(toClass);
            }
            if (Long.TYPE.equals(cls)) {
                return Float.TYPE.equals(toClass)
                    || Double.TYPE.equals(toClass);
            }
            if (Boolean.TYPE.equals(cls)) {
                return false;
            }
            if (Double.TYPE.equals(cls)) {
                return false;
            }
            if (Float.TYPE.equals(cls)) {
                return Double.TYPE.equals(toClass);
            }
            if (Character.TYPE.equals(cls)) {
                return Integer.TYPE.equals(toClass)
                    || Long.TYPE.equals(toClass)
                    || Float.TYPE.equals(toClass)
                    || Double.TYPE.equals(toClass);
            }
            if (Short.TYPE.equals(cls)) {
                return Integer.TYPE.equals(toClass)
                    || Long.TYPE.equals(toClass)
                    || Float.TYPE.equals(toClass)
                    || Double.TYPE.equals(toClass);
            }
            if (Byte.TYPE.equals(cls)) {
                return Short.TYPE.equals(toClass)
                    || Integer.TYPE.equals(toClass)
                    || Long.TYPE.equals(toClass)
                    || Float.TYPE.equals(toClass)
                    || Double.TYPE.equals(toClass);
            }
            // should never get here
            return false;
        }
        return toClass.isAssignableFrom(cls);
    }
{
        if (SystemUtils.isJavaVersionAtLeast(1.4f)) {
            assertEquals(message, expected, actual);
        }
    }
{
        if (str == null) {
            return null;
        }

        int len = str.length() ;

        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        if ( ( separator == null ) || ( "".equals( separator ) ) ) {
            // Split on whitespace.
            return splitWorker( str, null, max, preserveAllTokens ) ;
        }


        int separatorLength = separator.length() ;

        ArrayList substrings = new ArrayList() ;
        int numberOfSubstrings = 0 ;
        int beg = 0 ;
        int end = 0 ;
        while ( end < len ) {
            end = str.indexOf( separator, beg ) ;

            if ( end > -1 ) {
                if ( end > beg ) {
                    numberOfSubstrings += 1 ;

                    if ( numberOfSubstrings == max ) {
                        end = len ;
                        substrings.add( str.substring( beg ) ) ;
                    } else {
                        // The following is OK, because String.substring( beg, end ) excludes
                        // the character at the position 'end'.
                        substrings.add( str.substring( beg, end ) ) ;

                        // Set the starting point for the next search.
                        // The following is equivalent to beg = end + (separatorLength - 1) + 1,
                        // which is the right calculation:
                        beg = end + separatorLength ;
                    }
                } else {
                    // We found a consecutive occurrence of the separator, so skip it.
                    if( preserveAllTokens ) {
                        numberOfSubstrings += 1 ;
                        if ( numberOfSubstrings == max ) {
                            end = len ;
                            substrings.add( str.substring( beg ) ) ;
                        } else {
                            substrings.add( "" );
                        }
                    }
                    beg = end + separatorLength ;
                }
            } else {
                // String.substring( beg ) goes from 'beg' to the end of the String.
                substrings.add( str.substring( beg ) ) ;
                end = len ;
            }
        }

        return (String[]) substrings.toArray( new String[substrings.size()] ) ;
    }
{
        StringBuffer pattern = new StringBuffer();
        StringBuffer expected = new StringBuffer();
        StringBuffer decodePattern = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
            pattern.append(i).append(": {").append(i);
            if (formatName != null) {
                pattern.append(',').append(formatName);
            }
            pattern.append("}; ");
            expected.append(i).append(": ");
            if (format != null) {
                format.format(args[i], expected, new FieldPosition(0));
            } else {
                expected.append(String.valueOf(args[i]));
            }
            expected.append("; ");
            decodePattern.append(i).append(": {").append(i);
            if (decodeFormatName != null || formatName != null) {
                decodePattern.append(',').append(
                        decodeFormatName == null ? formatName
                                : decodeFormatName);
            }
            decodePattern.append("}; ");
        }
        doAssertions(expected.toString(), pattern.toString(), args,
                decodePattern.toString());
    }
{
        writer.write(string, 0, firstAmp);
        int len = string.length();
        for (int i = firstAmp; i < len; i++) {
            char c = string.charAt(i);
            if (c == '&') {
                int nextIdx = i + 1;
                int semiColonIdx = string.indexOf(';', nextIdx);
                if (semiColonIdx == -1) {
                    writer.write(c);
                    continue;
                }
                int amphersandIdx = string.indexOf('&', i + 1);
                if (amphersandIdx != -1 && amphersandIdx < semiColonIdx) {
                    // Then the text looks like &...&...;
                    writer.write(c);
                    continue;
                }
                String entityContent = string.substring(nextIdx, semiColonIdx);
                int entityValue = -1;
                int entityContentLen = entityContent.length();
                if (entityContentLen > 0) {
                    if (entityContent.charAt(0) == '#') { // escaped value content is an integer (decimal or
                        // hexidecimal)
                        if (entityContentLen > 1) {
                            char isHexChar = entityContent.charAt(1);
                            try {
                                switch (isHexChar) {
                                    case 'X' :
                                    case 'x' : {
                                        entityValue = Integer.parseInt(entityContent.substring(2), 16);
                                        break;
                                    }
                                    default : {
                                        entityValue = Integer.parseInt(entityContent.substring(1), 10);
                                    }
                                }
                                if (entityValue > 0xFFFF) {
                                    entityValue = -1;
                                }
                            } catch (NumberFormatException e) {
                                entityValue = -1;
                            }
                        }
                    } else { // escaped value content is an entity name
                        entityValue = this.entityValue(entityContent);
                    }
                }

                if (entityValue == -1) {
                    writer.write('&');
                    writer.write(entityContent);
                    writer.write(';');
                } else {
                    writer.write(entityValue);
                }
                i = semiColonIdx; // move index up to the semi-colon
            } else {
                writer.write(c);
            }
        }
    }
{
        StringWriter stringWriter = createStringWriter(str);
        try {
            this.escape(stringWriter, str);
        } catch (IOException e) {
            // This should never happen because ALL the StringWriter methods called by #escape(Writer, String) do not
            // throw IOExceptions.
            throw new UnhandledException(e);
        }
        return stringWriter.toString();
    }
{
        StringWriter stringWriter = createStringWriter(str);
        try {
            this.unescape(stringWriter, str);
        } catch (IOException e) {
            // This should never happen because ALL the StringWriter methods called by #escape(Writer, String) do not
            // throw IOExceptions.
            throw new UnhandledException(e);
        }
        return stringWriter.toString();
    }
{
       ObjectUtils.appendIdentityToString(buffer, value);
    }
{
        SimpleReflectionTestFixture simple = new SimpleReflectionTestFixture();
        simple.o = simple;
        assertTrue(ToStringStyle.getRegistry().isEmpty());
        assertEquals(this.toBaseString(simple) + "[o=" + this.toBaseString(simple) + "]", simple.toString());
        this.validateEmptyToStringStyleRegistry();
    }
{
        SelfInstanceVarReflectionTestFixture test = new SelfInstanceVarReflectionTestFixture();
        assertTrue(ToStringStyle.getRegistry().isEmpty());
        assertEquals(this.toBaseString(test) + "[typeIsSelf=" + this.toBaseString(test) + "]", test.toString());
        this.validateEmptyToStringStyleRegistry();
    }
{
        SelfInstanceTwoVarsReflectionTestFixture test = new SelfInstanceTwoVarsReflectionTestFixture();
        assertTrue(ToStringStyle.getRegistry().isEmpty());
        assertEquals(this.toBaseString(test) + "[typeIsSelf=" + this.toBaseString(test) + ",otherType=" + test.getOtherType().toString() + "]", test.toString());
        this.validateEmptyToStringStyleRegistry();
    }
{
        Calendar cal1 = Calendar.getInstance();
        cal1.set(start[0], start[1], start[2], start[3], start[4], start[5]);
        cal1.set(Calendar.MILLISECOND, 0);
        Calendar cal2 = Calendar.getInstance();
        cal2.set(end[0], end[1], end[2], end[3], end[4], end[5]);
        cal2.set(Calendar.MILLISECOND, 0);
        long milli1 = cal1.getTime().getTime();
        long milli2 = cal2.getTime().getTime();
        String result = DurationFormatUtils.formatPeriod(milli1, milli2, format);
        if (message == null) {
            assertEquals(expected, result);
        } else {
            assertEquals(message, expected, result);
        }
    }
{
        Calendar cal1 = Calendar.getInstance();
        cal1.set(start[0], start[1], start[2], start[3], start[4], start[5]);
        cal1.set(Calendar.MILLISECOND, 0);
        Calendar cal2 = Calendar.getInstance();
        cal2.set(end[0], end[1], end[2], end[3], end[4], end[5]);
        cal2.set(Calendar.MILLISECOND, 0);
        String result = DurationFormatUtils.formatPeriod(cal1.getTime().getTime(), cal2.getTime().getTime(), format);
        assertEquals(expected, result);
    }
{
        if (array == null) {
            return null;
        }
        int bufSize = (endIndex - startIndex);
        if (bufSize <= 0) {
            return EMPTY;
        }

        bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length()) + 1);
        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }
{
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }

        // endIndex - startIndex > 0:   Len = NofStrings *(len(firstString) + len(separator))
        //           (Assuming that all Strings are roughly equally long)
        int bufSize = (endIndex - startIndex);
        if (bufSize <= 0) {
            return EMPTY;
        }

        bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length())
                        + separator.length());

        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }
{
        try {
            Method mth = other.getClass().getMethod("getName", null);
            String name = (String) mth.invoke(other, null);
            return name;
        } catch (NoSuchMethodException e) {
            // ignore - should never happen
        } catch (IllegalAccessException e) {
            // ignore - should never happen
        } catch (InvocationTargetException e) {
            // ignore - should never happen
        }
        throw new IllegalStateException("This should not happen");
    }
{
        try {
            Method mth = other.getClass().getMethod("getName", null);
            String name = (String) mth.invoke(other, null);
            return name;
        } catch (NoSuchMethodException e) {
            // ignore - should never happen
        } catch (IllegalAccessException e) {
            // ignore - should never happen
        } catch (InvocationTargetException e) {
            // ignore - should never happen
        }
        throw new IllegalStateException("This should not happen");
    }
{
        if (lhs == rhs) {
            return true;
        }
        if (lhs == null || rhs == null) {
            return false;
        }
        // Find the leaf class since there may be transients in the leaf 
        // class or in classes between the leaf and root.
        // If we are not testing transients or a subclass has no ivars, 
        // then a subclass can test equals to a superclass.
        Class lhsClass = lhs.getClass();
        Class rhsClass = rhs.getClass();
        Class testClass;
        if (lhsClass.isInstance(rhs)) {
            testClass = lhsClass;
            if (!rhsClass.isInstance(lhs)) {
                // rhsClass is a subclass of lhsClass
                testClass = rhsClass;
            }
        } else if (rhsClass.isInstance(lhs)) {
            testClass = rhsClass;
            if (!lhsClass.isInstance(rhs)) {
                // lhsClass is a subclass of rhsClass
                testClass = lhsClass;
            }
        } else {
            // The two classes are not related.
            return false;
        }
        EqualsBuilder equalsBuilder = new EqualsBuilder();
        try {
            reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
            while (testClass.getSuperclass() != null && testClass != reflectUpToClass) {
                testClass = testClass.getSuperclass();
                reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
            }
        } catch (IllegalArgumentException e) {
            // In this case, we tried to test a subclass vs. a superclass and
            // the subclass has ivars or the ivars are transient and 
            // we are testing transients.
            // If a subclass has ivars that we are trying to test them, we get an
            // exception and we know that the objects are not equal.
            return false;
        }
        return equalsBuilder.isEquals();
    }
{
        List list = new ArrayList();
        while (throwable != null && list.contains(throwable) == false) {
            list.add(throwable);
            throwable = ExceptionUtils.getCause(throwable);
        }
        return list;
    }
{
            this.cause = cause;
        }
{
        try {
            coll.add("Unmodifiable");
            fail();
        } catch (UnsupportedOperationException ex) {}
    }
{
        StrTokenizer cloned = (StrTokenizer) super.clone();
        if (cloned.chars != null) {
            cloned.chars = (char[]) cloned.chars.clone();
        }
        cloned.reset();
        return cloned;
    }
{
        assertEquals(expected, entities.escape(entity));
        StringWriter writer = new StringWriter();
        entities.escape(writer, entity);
        assertEquals(expected, writer.toString());
    }
{
        assertEquals(expected, entities.unescape(entity));
        StringWriter writer = new StringWriter();
        entities.unescape(writer, entity);
        assertEquals(expected, writer.toString());
    }
{
        // Loop until we've found the end of the quoted
        // string or the end of the input
        workArea.clear();
        int pos = start;
        boolean quoting = (quoteLen > 0);
        int trimStart = 0;
        
        while (pos < len) {
            // quoting mode can occur several times throughout a string
            // we must switch between quoting and non-quoting until we
            // encounter a non-quoted delimiter, or end of string
            if (quoting) {
                // In quoting mode
                
                // If we've found a quote character, see if it's
                // followed by a second quote.  If so, then we need
                // to actually put the quote character into the token
                // rather than end the token.
                if (isQuote(chars, pos, len, quoteStart, quoteLen)) {
                    if (isQuote(chars, pos + quoteLen, len, quoteStart, quoteLen)) {
                        // matched pair of quotes, thus an escaped quote
                        workArea.append(chars, pos, quoteLen);
                        pos += (quoteLen * 2);
                        trimStart = workArea.size();
                        continue;
                    }
                    
                    // end of quoting
                    quoting = false;
                    pos += quoteLen;
                    continue;
                }
                
                // copy regular character from inside quotes
                workArea.append(chars[pos++]);
                trimStart = workArea.size();
                
            } else {
                // Not in quoting mode
                
                // check for delimiter, and thus end of token
                int delimLen = delim.isMatch(chars, pos, start, len);
                if (delimLen > 0) {
                    // return condition when end of token found
                    addToken(tokens, workArea.substring(0, trimStart));
                    return pos + delimLen;
                }
                
                // check for quote, and thus back into quoting mode
                if (quoteLen > 0) {
                    if (isQuote(chars, pos, len, quoteStart, quoteLen)) {
                        quoting = true;
                        pos += quoteLen;
                        continue;
                    }
                }
                
                // check for ignored (outside quotes), and ignore
                int ignoredLen = ignored.isMatch(chars, pos, start, len);
                if (ignoredLen > 0) {
                    pos += ignoredLen;
                    continue;
                }
                
                // check for trimmed character
                // don't yet know if its at the end, so copy to workArea
                // use trimStart to keep track of trim at the end
                int trimmedLen = trimmer.isMatch(chars, pos, start, len);
                if (trimmedLen > 0) {
                    workArea.append(chars, pos, trimmedLen);
                    pos += trimmedLen;
                    continue;
                }
                
                // copy regular character from outside quotes
                workArea.append(chars[pos++]);
                trimStart = workArea.size();
            }
        }
        
        // return condition when end of string found
        addToken(tokens, workArea.substring(0, trimStart));
        return -1;
    }
{
        System.arraycopy(buffer, endIndex, buffer, startIndex, size - endIndex);
        size -= len;
    }
{
        int newSize = size - removeLen + insertLen;
        if (insertLen != removeLen) {
            ensureCapacity(newSize);
            System.arraycopy(buffer, endIndex, buffer, startIndex + insertLen, size - endIndex);
            size = newSize;
        }
        if (insertLen > 0) {
            insertStr.getChars(0, insertLen, buffer, startIndex);
        }
    }
{
        if (matcher == null || size == 0) {
            return this;
        }
        int replaceLen = (replaceStr == null ? 0 : replaceStr.length());
        char[] buf = buffer;
        for (int i = from; i < to && replaceCount != 0; i++) {
            int removeLen = matcher.isMatch(buf, i, from, to);
            if (removeLen > 0) {
                replaceImpl(i, i + removeLen, removeLen, replaceStr, replaceLen);
                to = to - removeLen + replaceLen;
                i = i + replaceLen - 1;
                if (replaceCount > 0) {
                    replaceCount--;
                }
            }
        }
        return this;
    }
{
        StrBuilder sb = new StrBuilder();
        assertFalse(sb.startsWith("a"));
        assertFalse(sb.startsWith(null));
        assertTrue(sb.startsWith(""));
        sb.append("abc");
        assertTrue(sb.startsWith("a"));
        assertTrue(sb.startsWith("ab"));
        assertTrue(sb.startsWith("abc"));
        assertFalse(sb.startsWith("cba"));
    }
{
        StrBuilder sb = new StrBuilder();
        assertFalse(sb.endsWith("a"));
        assertFalse(sb.endsWith("c"));
        assertTrue(sb.endsWith(""));
        assertFalse(sb.endsWith(null));
        sb.append("abc");
        assertTrue(sb.endsWith("c"));
        assertTrue(sb.endsWith("bc"));
        assertTrue(sb.endsWith("abc"));
        assertFalse(sb.endsWith("cba"));
        assertFalse(sb.endsWith("abcd"));
        assertFalse(sb.endsWith(" abc"));
        assertFalse(sb.endsWith("abc "));
    }
{
        if (data == null) {
            return null;
        }

        Object resultObj = ref;
        int tokenCnt = 0;
        StrBuilder buf = new StrBuilder(length);

        // on the first call initialize priorVariables
        if (priorVariables == null) {
            priorVariables = new ArrayList();
            priorVariables.add(new String(data, offset, length));
        }

        VariableParser parser = createParser(data, offset, length);
        Token tok;
        while ((tok = parser.nextToken(data)) != null) {
            switch (tok.getType()) {
                case Token.TEXT_TOKEN :
                    buf.append(data, tok.getStartIndex(), tok.getLength());
                    break;

                case Token.ESCAPED_VAR_TOKEN :
                    buf.append(getVariablePrefix());
                    tokenCnt++;
                    break;

                case Token.VARIABLE_TOKEN :
                    String variable = tok.getText(data);

                    // if we've got a loop, create a useful exception message and
                    // throw
                    if (priorVariables.contains(variable)) {
                        String initialBase = priorVariables.remove(0).toString();
                        priorVariables.add(variable);
                        StrBuilder priorVariableSb = new StrBuilder();

                        // create a nice trace of interpolated variables like so:
                        // var1->var2->var3
                        for (Iterator it = priorVariables.iterator(); it.hasNext();) {
                            priorVariableSb.append(it.next());
                            if (it.hasNext()) {
                                priorVariableSb.append("->");
                            }
                        }
                        throw new IllegalStateException("Infinite loop in property interpolation of "
                            + initialBase
                            + ": "
                            + priorVariableSb.toString());
                    }
                    // otherwise, add this variable to the interpolation list.
                    priorVariables.add(variable);

                    resultObj = resolveVariable(variable);
                    if (resultObj != null) {
                        resultObj = doReplace(resultObj, priorVariables);
                        buf.append(resultObj);
                    } else {
                        // variable not defined - so put it back in the value
                        buf.append(getVariablePrefix()).append(variable).append(getVariableSuffix());
                    }

                    // pop the interpolated variable off the stack
                    // this maintains priorVariables correctness for
                    // properties with multiple interpolations, e.g.
                    // prop.name=${some.other.prop1}/blahblah/${some.other.prop2}
                    priorVariables.remove(priorVariables.size() - 1);
                    break;
            }
            tokenCnt++;
        }

        if (resultObj != null && tokenCnt == 1) {
            // if there was only one token, return the reference object
            return resultObj;
        }
        return buf.toString();
    }
{
        return (StrTokenizer) CSV_TOKENIZER_PROTOTYPE.clone();
    }
{
        return (StrTokenizer) TSV_TOKENIZER_PROTOTYPE.clone();
    }
{
        return (String[]) list.toArray(new String[list.size()]);
    }
{
        if (array == null) {
            if (index != 0) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Length: 0");
            }
            Object joinedArray = Array.newInstance(clss, 1);
            Array.set(joinedArray, 0, element);
            return joinedArray;
        }
        int length = Array.getLength(array);
        if (index > length || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
        }
        Object result = Array.newInstance(clss, length + 1);
        System.arraycopy(array, 0, result, 0, index);
        Array.set(result, index, element);
        if (index < length) {
            System.arraycopy(array, index, result, index + 1, length - index);
        }
        return result;
    }
{
        if (throwable == null || type == null) {
            return -1;
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        Throwable[] throwables = ExceptionUtils.getThrowables(throwable);
        if (fromIndex >= throwables.length) {
            return -1;
        }
        if (subclass) {
            for (int i = fromIndex; i < throwables.length; i++) {
                if (type.isAssignableFrom(throwables[i].getClass())) {
                    return i;
                }
            }
        } else {
            for (int i = fromIndex; i < throwables.length; i++) {
                if (type.equals(throwables[i].getClass())) {
                    return i;
                }
            }
        }
        return -1;
    }
{

        if(millis > 28 * DateUtils.MILLIS_PER_DAY) {
            Calendar c = Calendar.getInstance(timezone);
            c.set(1970, 0, 1, 0, 0, 0);
            c.set(Calendar.MILLISECOND, 0);
            return format(c.getTime().getTime(), millis, format, padWithZeros, timezone);
        }

        Token[] tokens = lexx(format);

        int years        = 0;
        int months       = 0;
        int days         = 0;
        int hours        = 0;
        int minutes      = 0;
        int seconds      = 0;
        int milliseconds = 0;

        /*  This will never be evaluated
        if(Token.containsTokenWithValue(tokens, y) ) {
            years = (int) (millis / DateUtils.MILLIS_PER_YEAR);
            millis = millis - (years * DateUtils.MILLIS_PER_YEAR);
        }
        if(Token.containsTokenWithValue(tokens, M) ) {
            months = (int) (millis / DateUtils.MILLIS_PER_MONTH);
            millis = millis - (months * DateUtils.MILLIS_PER_MONTH);
            // as MONTH * 12 != YEAR, this fixes issues
            if(months == 12) {
                years++;
                months = 0;
            }
        }
        */
        if(Token.containsTokenWithValue(tokens, d) ) {
            days = (int) (millis / DateUtils.MILLIS_PER_DAY);
            millis = millis - (days * DateUtils.MILLIS_PER_DAY);
        }
        if(Token.containsTokenWithValue(tokens, H) ) {
            hours = (int) (millis / DateUtils.MILLIS_PER_HOUR);
            millis = millis - (hours * DateUtils.MILLIS_PER_HOUR);
        }
        if(Token.containsTokenWithValue(tokens, m) ) {
            minutes = (int) (millis / DateUtils.MILLIS_PER_MINUTE);
            millis = millis - (minutes * DateUtils.MILLIS_PER_MINUTE);
        }
        if(Token.containsTokenWithValue(tokens, s) ) {
            seconds = (int) (millis / DateUtils.MILLIS_PER_SECOND);
            millis = millis - (seconds * DateUtils.MILLIS_PER_SECOND);
        }
        if(Token.containsTokenWithValue(tokens, S) ) {
            milliseconds = (int) millis;
        }

        return formatDuration(tokens, years, months, days, hours, minutes, seconds, milliseconds, padWithZeros);
    }
{
        StringBuffer buffer = new StringBuffer();
        Token[] tokens = lexx(format);
        int sz = tokens.length;

        int years        = 0;
        int months       = 0;
        int days         = 0;
        int hours        = 0;
        int minutes      = 0;
        int seconds      = 0;
        int milliseconds = 0;

        if(Token.containsTokenWithValue(tokens, y) ) {
            years = (int) (millis / DateUtils.MILLIS_PER_YEAR);
            millis = millis - (years * DateUtils.MILLIS_PER_YEAR);
        }
        if(Token.containsTokenWithValue(tokens, M) ) {
            months = (int) (millis / DateUtils.MILLIS_PER_MONTH);
            millis = millis - (months * DateUtils.MILLIS_PER_MONTH);
            // as MONTH * 12 != YEAR, this fixes issues
            if(months == 12) {
                years++;
                months = 0;
            }
        }
        if(Token.containsTokenWithValue(tokens, d) ) {
            days = (int) (millis / DateUtils.MILLIS_PER_DAY);
            millis = millis - (days * DateUtils.MILLIS_PER_DAY);
        }
        if(Token.containsTokenWithValue(tokens, H) ) {
            hours = (int) (millis / DateUtils.MILLIS_PER_HOUR);
            millis = millis - (hours * DateUtils.MILLIS_PER_HOUR);
        }
        if(Token.containsTokenWithValue(tokens, m) ) {
            minutes = (int) (millis / DateUtils.MILLIS_PER_MINUTE);
            millis = millis - (minutes * DateUtils.MILLIS_PER_MINUTE);
        }
        if(Token.containsTokenWithValue(tokens, s) ) {
            seconds = (int) (millis / DateUtils.MILLIS_PER_SECOND);
            millis = millis - (seconds * DateUtils.MILLIS_PER_SECOND);
        }
        if(Token.containsTokenWithValue(tokens, S) ) {
            milliseconds = (int) millis;
        }


        for(int i=0; i<sz; i++) {
            Token token = tokens[i];
            Object value = token.getValue();
            int count = token.getCount();
            if(value instanceof StringBuffer) {
                buffer.append(value.toString());
            } else {
                if(value == y) {
                    buffer.append( padWithZeros ? StringUtils.leftPad(""+years, count, "0") : ""+years ); 
                } else
                if(value == M) {
                    buffer.append( padWithZeros ? StringUtils.leftPad(""+months, count, "0") : ""+months ); 
                } else
                if(value == d) {
                    buffer.append( padWithZeros ? StringUtils.leftPad(""+days, count, "0") : ""+days ); 
                } else
                if(value == H) {
                    buffer.append( padWithZeros ? StringUtils.leftPad(""+hours, count, "0") : ""+hours ); 
                } else
                if(value == m) {
                    buffer.append( padWithZeros ? StringUtils.leftPad(""+minutes, count, "0") : ""+minutes ); 
                } else
                if(value == s) {
                    buffer.append( padWithZeros ? StringUtils.leftPad(""+seconds, count, "0") : ""+seconds ); 
                } else
                if(value == S) {
                    buffer.append( padWithZeros ? StringUtils.leftPad(""+milliseconds, count, "0") : ""+milliseconds ); 
                }
            }
        }
        
        return buffer.toString();
    }
{
        StringBuffer buffer = new StringBuffer();
        Token[] tokens = lexx(format);
        int sz = tokens.length;

        int years        = 0;
        int months       = 0;
        int days         = 0;
        int hours        = 0;
        int minutes      = 0;
        int seconds      = 0;
        int milliseconds = 0;

        if(Token.containsTokenWithValue(tokens, y) ) {
            years = (int) (millis / DateUtils.MILLIS_PER_YEAR);
            millis = millis - (years * DateUtils.MILLIS_PER_YEAR);
        }
        if(Token.containsTokenWithValue(tokens, M) ) {
            months = (int) (millis / DateUtils.MILLIS_PER_MONTH);
            millis = millis - (months * DateUtils.MILLIS_PER_MONTH);
        }
        if(Token.containsTokenWithValue(tokens, d) ) {
            days = (int) (millis / DateUtils.MILLIS_PER_DAY);
            millis = millis - (days * DateUtils.MILLIS_PER_DAY);
        }
        if(Token.containsTokenWithValue(tokens, H) ) {
            hours = (int) (millis / DateUtils.MILLIS_PER_HOUR);
            millis = millis - (hours * DateUtils.MILLIS_PER_HOUR);
        }
        if(Token.containsTokenWithValue(tokens, m) ) {
            minutes = (int) (millis / DateUtils.MILLIS_PER_MINUTE);
            millis = millis - (minutes * DateUtils.MILLIS_PER_MINUTE);
        }
        if(Token.containsTokenWithValue(tokens, s) ) {
            seconds = (int) (millis / DateUtils.MILLIS_PER_SECOND);
            millis = millis - (seconds * DateUtils.MILLIS_PER_SECOND);
        }
        if(Token.containsTokenWithValue(tokens, S) ) {
            milliseconds = (int) millis;
        }


        for(int i=0; i<sz; i++) {
            Token token = tokens[i];
            Object value = token.getValue();
            int count = token.getCount();
            if(value instanceof StringBuffer) {
                buffer.append(value.toString());
            } else {
                if(value == y) {
                    buffer.append( StringUtils.leftPad(""+years, count, "0") ); 
                } else
                if(value == M) {
                    buffer.append( StringUtils.leftPad(""+months, count, "0") ); 
                } else
                if(value == d) {
                    buffer.append( StringUtils.leftPad(""+days, count, "0") ); 
                } else
                if(value == H) {
                    buffer.append( StringUtils.leftPad(""+hours, count, "0") ); 
                } else
                if(value == m) {
                    buffer.append( StringUtils.leftPad(""+minutes, count, "0") ); 
                } else
                if(value == s) {
                    buffer.append( StringUtils.leftPad(""+seconds, count, "0") ); 
                } else
                if(value == S) {
                    buffer.append( StringUtils.leftPad(""+milliseconds, count, "0") ); 
                }
            }
        }
        
        return buffer.toString();
    }
{
        this.isEquals = isEquals;
    }
{
        if (fraction == null) {
            throw new IllegalArgumentException("The fraction must not be null");
        }
        // zero is identity for addition.
        if (numerator == 0) {
            return isAdd ? fraction : fraction.negate();
        }
        if (fraction.numerator == 0) {
            return this;
        }     
        // if denominators are randomly distributed, d1 will be 1 about 61%
        // of the time.
        int d1 = greatestCommonDivisor(denominator, fraction.denominator);
        if (d1==1) {
            // result is ( (u*v' +/- u'v) / u'v')
            int uvp = mulAndCheck(numerator, fraction.denominator);
            int upv = mulAndCheck(fraction.numerator, denominator);
            return new Fraction
                (isAdd ? addAndCheck(uvp, upv) : subAndCheck(uvp, upv),
                 mulPosAndCheck(denominator, fraction.denominator));
        }
        // the quantity 't' requires 65 bits of precision; see knuth 4.5.1
        // exercise 7.  we're going to use a BigInteger.
        // t = u(v'/d1) +/- v(u'/d1)
        BigInteger uvp = BigInteger.valueOf(numerator)
            .multiply(BigInteger.valueOf(fraction.denominator/d1));
        BigInteger upv = BigInteger.valueOf(fraction.numerator)
            .multiply(BigInteger.valueOf(denominator/d1));
        BigInteger t = isAdd ? uvp.add(upv) : uvp.subtract(upv);
        // but d2 doesn't need extra precision because
        // d2 = gcd(t,d1) = gcd(t mod d1, d1)
        int tmodd1 = t.mod(BigInteger.valueOf(d1)).intValue();
        int d2 = (tmodd1==0)?d1:greatestCommonDivisor(tmodd1, d1);

        // result is (t/d2) / (u'/d1)(v'/d2)
        BigInteger w = t.divide(BigInteger.valueOf(d2));
        if (w.bitLength() > 31) {
            throw new ArithmeticException
                ("overflow: numerator too large after multiply");
        }
        return new Fraction
            (w.intValue(),
             mulPosAndCheck(denominator/d1, fraction.denominator/d2));
    }
{
        // Performance tuned for 2.0 (JDK1.4)

        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        List list = new ArrayList();
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match || preserveAllTokens) {
                    list.add(str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                start = ++i;
                continue;
            } else {
                lastMatch = false;
            }
            match = true;
            i++;
        }
        if (match || (preserveAllTokens && lastMatch)) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }
{
        // Performance tuned for 2.0 (JDK1.4)
        // Direct code is quicker than StringTokenizer.
        // Also, StringTokenizer uses isSpace() not isWhitespace()

        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        List list = new ArrayList();
        int sizePlus1 = 1;
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null) {
            // Null separator means use whitespace
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                } else {
                    lastMatch = false;
                }
                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // Optimise 1 character case
            char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                } else {
                    lastMatch = false;
                }
                match = true;
                i++;
            }
        } else {
            // standard case
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                } else {
                    lastMatch = false;
                }
                match = true;
                i++;
            }
        }
        if (match || (preserveAllTokens && lastMatch)) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }
{
        this.value = value;
    }
{
        if (str == null || str.length() == 0) {
            return str;
        }
        int strLen = str.length();

        int delimitersLen = 0;
        if(delimiters != null) {
            delimitersLen = delimiters.length;
        }

        StringBuffer buffer = new StringBuffer(strLen);
        boolean uncapitalizeNext = true;
        for (int i = 0; i < strLen; i++) {
            char ch = str.charAt(i);

            boolean isDelimiter = false;
            if(delimiters == null) {
                isDelimiter = Character.isWhitespace(ch);
            } else {
                for(int j=0; j < delimitersLen; j++) {
                    if(ch == delimiters[j]) {
                        isDelimiter = true;
                        break;
                    }
                }
            }

            if (isDelimiter) {
                buffer.append(ch);
                uncapitalizeNext = true;
            } else if (uncapitalizeNext) {
                buffer.append(Character.toLowerCase(ch));
                uncapitalizeNext = false;
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }
{
        if (str == null || str.length() == 0) {
            return str;
        }
        int strLen = str.length();
        StringBuffer buffer = new StringBuffer(strLen);

        int delimitersLen = 0;
        if(delimiters != null) {
            delimitersLen = delimiters.length;
        }

        boolean capitalizeNext = true;
        for (int i = 0; i < strLen; i++) {
            char ch = str.charAt(i);

            boolean isDelimiter = false;
            if(delimiters == null) {
                isDelimiter = Character.isWhitespace(ch);
            } else {
                for(int j=0; j < delimitersLen; j++) {
                    if(ch == delimiters[j]) {
                        isDelimiter = true;
                        break;
                    }
                }
            }

            if (isDelimiter) {
                buffer.append(ch);
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer.append(Character.toTitleCase(ch));
                capitalizeNext = false;
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }
{
        if (str == null || str.length() == 0) {
            return str;
        }
        str = str.toLowerCase();
        return capitalize(str, delimiters);
    }
{
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }
{
        return useShortClassName;
    }
{
        this.useShortClassName = useShortClassName;
    }
{
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuffer(strLen)
            .append(Character.toTitleCase(str.charAt(0)))
            .append(str.substring(1))
            .toString();
    }
{
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuffer(strLen)
            .append(Character.toLowerCase(str.charAt(0)))
            .append(str.substring(1))
            .toString();
    }
{
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        StringBuffer buffer = new StringBuffer(strLen);
        boolean whitespace = true;
        for (int i = 0; i < strLen; i++) {
            char ch = str.charAt(i);
            if (Character.isWhitespace(ch)) {
                buffer.append(ch);
                whitespace = true;
            } else if (whitespace) {
                buffer.append(Character.toTitleCase(ch));
                whitespace = false;
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }
{
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        StringBuffer buffer = new StringBuffer(strLen);
        boolean whitespace = true;
        for (int i = 0; i < strLen; i++) {
            char ch = str.charAt(i);
            if (Character.isWhitespace(ch)) {
                buffer.append(ch);
                whitespace = true;
            } else if (whitespace) {
                buffer.append(Character.toLowerCase(ch));
                whitespace = false;
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }
{
        if (dateStr == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        //Get the symbol names
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.ENGLISH);

        //Prep the string to parse
        String value = dateStr.toLowerCase().trim();

        //Get the current date/time
        Calendar now = Calendar.getInstance();
        if (value.endsWith(" ago")) {
            //If this was a date that was "ago" the current time...
            //Strip out the ' ago' part
            value = value.substring(0, value.length() - 4);

            //Split the value and unit
            int start = value.indexOf(" ");
            if (start < 0) {
                throw new IllegalArgumentException("Could not find space in between value and unit");
            }
            String unit = value.substring(start + 1);
            value = value.substring(0, start);
            //We support "a week", so we need to parse the value as "a"
            int val = 0;
            if (value.equals("a") || value.equals("an")) {
                val = 1;
            } else {
                val = Integer.parseInt(value);
            }

            //Determine the unit
            if (unit.equals("milliseconds") || unit.equals("millisecond")) {
                now.add(Calendar.MILLISECOND, -val);
            } else if (unit.equals("seconds") || unit.equals("second")) {
                now.add(Calendar.SECOND, -val);
            } else if (unit.equals("minutes") || unit.equals("minute")) {
                now.add(Calendar.MINUTE, -val);
            } else if (unit.equals("hours") || unit.equals("hour")) {
                now.add(Calendar.HOUR, -val);
            } else if (unit.equals("days") || unit.equals("day")) {
                now.add(Calendar.DATE, -val);
            } else if (unit.equals("weeks") || unit.equals("week")) {
                now.add(Calendar.DATE, -val * 7);
            } else if (unit.equals("fortnights") || unit.equals("fortnight")) {
                now.add(Calendar.DATE, -val * 14);
            } else if (unit.equals("months") || unit.equals("month")) {
                now.add(Calendar.MONTH, -val);
            } else if (unit.equals("years") || unit.equals("year")) {
                now.add(Calendar.YEAR, -val);
            } else {
                throw new IllegalArgumentException("We do not understand that many units ago");
            }
            return now;
        } else if (value.startsWith("last ")) {
            //If this was the last time a certain field was met
            //Strip out the 'last ' part
            value = value.substring(5);
            //Get the current date/time
            String[] strings = symbols.getWeekdays();
            for (int i = 0; i < strings.length; i++) {
                if (value.equalsIgnoreCase(strings[i])) {
                    //How many days after Sunday
                    int daysAgo = now.get(Calendar.DAY_OF_WEEK) - i;
                    if (daysAgo <= 0) {
                        daysAgo += 7;
                    }
                    now.add(Calendar.DATE, -daysAgo);
                    return now;
                }
            }
            strings = symbols.getMonths();
            for (int i = 0; i < strings.length; i++) {
                if (value.equalsIgnoreCase(strings[i])) {
                    //How many days after January
                    int monthsAgo = now.get(Calendar.MONTH) - i;
                    if (monthsAgo <= 0) {
                        monthsAgo += 12;
                    }
                    now.add(Calendar.MONTH, -monthsAgo);
                    return now;
                }
            }
            if (value.equals("week")) {
                now.add(Calendar.DATE, -7);
                return now;
            }
            throw new IllegalArgumentException("We do not understand that last units");
        } else if (value.equals("yesterday")) {
            now.add(Calendar.DATE, -1);
            return now;
        } else if (value.equals("tomorrow")) {
            now.add(Calendar.DATE, 1);
            return now;
        }
        //Try to parse the date a number of different ways
        for (int i = 0; i < dateFormats.length; i++) {
            try {
                Date datetime = dateFormats[i].parse(dateStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(datetime);
                return cal;
            } catch (ParseException pe) {
                //we ignore this and just keep trying
            }
        }

        throw new IllegalArgumentException("Unable to parse '" + dateStr + "'.");
    }
{
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("The Enum name must not be empty or null");
        }
        Entry entry = (Entry) cEnumClasses.get(enumClass);
        if (entry == null) {
            entry = createEntry(enumClass);
            cEnumClasses.put(enumClass, entry);
        }
        if (entry.map.containsKey(name)) {
            throw new IllegalArgumentException("The Enum name must be unique, '" + name + "' has already been added");
        }
        entry.map.put(name, this);
        entry.list.add(this);
    }
{
        if (str == null || open == null || close == null) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != -1) {
            int end = str.indexOf(close, start + open.length());
            if (end != -1) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }
{
        if (set == null) {
            return null;
        }
        return new CharSet(new String[] {set}); 
    }
{
        try {
            Float value = NumberUtils.createFloat(str);
            fail("createFloat(blank) failed: " + value);
        } catch (NumberFormatException ex) {
            // empty
        }
    }
{
        try {
            Double value = NumberUtils.createDouble(str);
            fail("createDouble(blank) failed: " + value);
        } catch (NumberFormatException ex) {
            // empty
        }
    }
{
        try {
            Integer value = NumberUtils.createInteger(str);
            fail("createInteger(blank) failed: " + value);
        } catch (NumberFormatException ex) {
            // empty
        }
    }
{
        try {
            Long value = NumberUtils.createLong(str);
            fail("createLong(blank) failed: " + value);
        } catch (NumberFormatException ex) {
            // empty
        }
    }
{
        try {
            BigInteger value = NumberUtils.createBigInteger(str);
            fail("createBigInteger(blank) failed: " + value);
        } catch (NumberFormatException ex) {
            // empty
        }
    }
{
        try {
            BigDecimal value = NumberUtils.createBigDecimal(str);
            fail("createBigDecimal(blank) failed: " + value);
        } catch (NumberFormatException ex) {
            // empty
        }
    }
{
        if (str == null || size <= 0) {
            return str;
        }
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        str = leftPad(str, strLen + pads / 2, padChar);
        str = rightPad(str, size, padChar);
        return str;
    }
{
        try {
            final String str = "a" + separator + "b" + separator + separator + noMatch + "c";
            String[] res;
            // (str, sepStr)
            res = StringUtils.split(str, sepStr);
            assertEquals(3, res.length);
            assertEquals("a", res[0]);
            assertEquals("b", res[1]);
            assertEquals(noMatch + "c", res[2]);
            
            final String str2 = separator + "a" + separator;
            res = StringUtils.split(str2, sepStr);
            assertEquals(1, res.length);
            assertEquals("a", res[0]);

            res = StringUtils.split(str, sepStr, -1);
            assertEquals(3, res.length);
            assertEquals("a", res[0]);
            assertEquals("b", res[1]);
            assertEquals(noMatch + "c", res[2]);
            
            res = StringUtils.split(str, sepStr, 0);
            assertEquals(3, res.length);
            assertEquals("a", res[0]);
            assertEquals("b", res[1]);
            assertEquals(noMatch + "c", res[2]);
            
            res = StringUtils.split(str, sepStr, 1);
            assertEquals(1, res.length);
            assertEquals(str, res[0]);
            
            res = StringUtils.split(str, sepStr, 2);
            assertEquals(2, res.length);
            assertEquals("a", res[0]);
            assertEquals(str.substring(2), res[1]);
            
        } catch (AssertionFailedError ex) {
            System.out.println("Failed on separator hex(" + Integer.toHexString(separator) +
                 "), noMatch hex(" + Integer.toHexString(noMatch) + "), sepStr(" + sepStr + ")");
            throw ex;
        }
    }
{
        if (array == null) {
            return -1;
        }
        if (startIndex < 0) {
            return -1;
        } else if (startIndex >= array.length) {
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return -1;
    }
{
        //todo: add a version that takes a Writer
        //todo: rewrite underlying method to use a Writer instead of a StringBuffer
        return Entities.HTML40.escape(str);
    }
{
        return Entities.XML.escape(str);
    }
{
        return Entities.HTML40.unescape(str);
    }
{
        return Entities.XML.unescape(str);
    }
{
        String entityValue = Entities.html40[i % Entities.html40.length][1];
        char ch = (char) Integer.parseInt(entityValue);
        return ch;
    }
{
        map.add("foo", 1);
        assertEquals(1, map.value("foo"));
        assertEquals("foo", map.name(1));
        map.add("bar", 2);
        map.add("baz", 3);
        assertEquals(3, map.value("baz"));
        assertEquals("baz", map.name(3));
    }
{
        StringBuffer buf = new StringBuffer(str.length() * 2);
        int i;
        for (i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            String entity = entities.entityName(ch);
            if (entity == null) {
                if (((int) ch) > 0x7F) {
                    int intValue = ((int) ch);
                    buf.append("&#" + intValue + ";");
                } else {
                    buf.append(ch);
                }
            } else {
                buf.append("&" + entity + ";");
            }
        }
        return buf.toString();
    }
{
        StringBuffer buf = new StringBuffer(str.length());
        int i;
        for (i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            if (ch == '&') {
                int semi = str.indexOf(';', i + 1);
                if (semi == -1) {
                    buf.append(ch);
                    continue;
                }
                String entity = str.substring(i + 1, semi);
                Integer iso;
                if (entity.charAt(0) == '#') {
                    iso = new Integer(entity.substring(1));
                } else {
                    iso = entities.entityValue(entity);
                }
                if (iso == null) {
                    buf.append("&" + entity + ";");
                } else {
                    buf.append((char) (iso.intValue()));
                }
                i = semi;
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }
{
        String expected = unescaped;
        String actual = StringEscapeUtils.unescapeJava(original);

        assertEquals("unescape(String) failed" +
                (message == null ? "" : (": " + message)) +
                // we escape this so we can see it in the error message
                ": expected '" + StringUtils.escape(expected) +
                "' actual '" + StringUtils.escape(actual) + "'",
                expected, actual);

        StringPrintWriter writer = new StringPrintWriter();
        StringEscapeUtils.unescapeJava(writer, original);
        assertEquals(unescaped, writer.getString());

    }
{
        CharSet chars = evaluateSet(set);
        StringBuffer buffer = new StringBuffer(str.length());
        char[] chrs = str.toCharArray();
        int sz = chrs.length;
        for(int i=0; i<sz; i++) {
            if(chars.contains(chrs[i]) == expect) {
                buffer.append(chrs[i]);
            }
        }
        return buffer.toString();
    }
{
        String className = cls.getName();
        int index = className.lastIndexOf('$');
        if (index > -1) {
            // is it an anonymous inner class?
            String inner = className.substring(index + 1);
            if (inner.length() > 0 &&
                inner.charAt(0) >= '0' &&
                inner.charAt(0) < '9') {
                return cls.getSuperclass();
            }
        }
        return cls;
    }
{
        if( (start == 0) && (end == 0) ) {
            end = (int)'z';
            start = (int)' ';
            if(!letters && !numbers) {
                start = 0;
                end = Integer.MAX_VALUE;
            }
        }

        StringBuffer buffer = new StringBuffer();
        int gap = end - start;

        while(count-- != 0) {
            char ch;
            if(set == null) {
                ch = (char)(random.nextInt(gap) + start);
            } else {
                ch = set[random.nextInt(gap) + start];
            }
            if( (letters && numbers && Character.isLetterOrDigit(ch)) ||
                (letters && Character.isLetter(ch)) ||
                (numbers && Character.isDigit(ch)) ||
                (!letters && !numbers)
              ) 
            {
                buffer.append( ch );
            } else {
                count++;
            }
        }
        return buffer.toString();
    }
{
        if (lhs == rhs) {
            return 0;
        }
        if (lhs == null || rhs == null) {
            throw new NullPointerException();
        }
        Class c1 = lhs.getClass();
        if (!c1.isInstance(rhs)) {
            throw new ClassCastException();
        }
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        reflectionAppend(lhs, rhs, c1, compareToBuilder, testTransients);
        while (c1.getSuperclass() != null && c1 != reflectUpToClass) {
            c1 = c1.getSuperclass();
            reflectionAppend(lhs, rhs, c1, compareToBuilder, testTransients);
        }
        return compareToBuilder.toComparison();
    }
{
        if (lhs == rhs) {
            return true;
        }
        if (lhs == null || rhs == null) {
            return false;
        }
        // Find the leaf class since there may be transients in the leaf 
        // class or in classes between the leaf and root.
        // If we are not testing transients or a subclass has no ivars, 
        // then a subclass can test equals to a superclass.
        Class lhsClass = lhs.getClass();
        Class rhsClass = rhs.getClass();
        Class testClass;
        if (lhsClass.isInstance(rhs)) {
            testClass = lhsClass;
            if (!rhsClass.isInstance(lhs)) {
                // rhsClass is a subclass of lhsClass
                testClass = rhsClass;
            }
        } else if (rhsClass.isInstance(lhs)) {
            testClass = rhsClass;
            if (!lhsClass.isInstance(rhs)) {
                // lhsClass is a subclass of rhsClass
                testClass = lhsClass;
            }
        } else {
            // The two classes are not related.
            return false;
        }
        EqualsBuilder equalsBuilder = new EqualsBuilder();
        try {
            reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients);
            while (testClass.getSuperclass() != null && testClass != reflectUpToClass) {
                testClass = testClass.getSuperclass();
                reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients);
            }
        } catch (IllegalArgumentException e) {
            // In this case, we tried to test a subclass vs. a superclass and
            // the subclass has ivars or the ivars are transient and 
            // we are testing transients.
            // If a subclass has ivars that we are trying to test them, we get an
            // exception and we know that the objects are not equal.
            return false;
        }
        return equalsBuilder.isEquals();
    }
{

        if (object == null) {
            throw new IllegalArgumentException("The object to build a hash code for must not be null");
        }
        HashCodeBuilder builder = new HashCodeBuilder(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
        Class clazz = object.getClass();
        reflectionAppend(object, clazz, builder, testTransients);
        while (clazz.getSuperclass() != null && clazz != reflectUpToClass) {
            clazz = clazz.getSuperclass();
            reflectionAppend(object, clazz, builder, testTransients);
        }
        return builder.toHashCode();
    }
{
        if (enumClass == null) {
            throw new IllegalArgumentException("The Enum Class must not be null");
        }
        if (Enum.class.isAssignableFrom(enumClass) == false) {
            throw new IllegalArgumentException("The Class must be a subclass of Enum");
        }
        Entry entry = (Entry) cEnumClasses.get(enumClass.getName());
        return entry;
    }
{
        if (object == null) {
            throw new IllegalArgumentException("The object must not be null");
        }
        if (style == null) {
            style = getDefaultStyle();
        }
        ToStringBuilder builder = new ToStringBuilder(object, style);
        Class clazz = object.getClass();
        reflectionAppend(object, clazz, builder, outputTransients);
        while (clazz.getSuperclass() != null && clazz != reflectUpToClass) {
            clazz = clazz.getSuperclass();
            reflectionAppend(object, clazz, builder, outputTransients);
        }
        return builder.toString();
    }
{

        // Make sure we have a method to check
        if (method == null) {
            return (null);
        }

        // If the requested method is not public we cannot call it
        if (!Modifier.isPublic(method.getModifiers())) {
            log("Method is not public");
            return (null);
        }

        // If the declaring class is public, we are done
        Class clazz = method.getDeclaringClass();
        if (Modifier.isPublic(clazz.getModifiers())) {
            log("Class is public");
            return (method);
        }
        
        if (debug) {
            log("Method is in non-public class " + clazz);
        }

        // Check the implemented interfaces and subinterfaces
        String methodName = method.getName();
        Class[] parameterTypes = method.getParameterTypes();
        method =
                getAccessibleMethodFromInterfaceNest(clazz,
                        method.getName(),
                        method.getParameterTypes());

        return (method);

    }
{
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        Class lhsClass = lhs.getClass();
        if (lhsClass.isArray()) {
            // 'Switch' on type of array, to dispatch to the correct handler
            // This handles multi dimensional arrays
            // this could throw a ClassCastException is rhs is not the correct array type
            if (lhs instanceof long[]) {
                append((long[]) lhs, (long[]) rhs);
            } else if (lhs instanceof int[]) {
                append((int[]) lhs, (int[]) rhs);
            } else if (lhs instanceof short[]) {
                append((short[]) lhs, (short[]) rhs);
            } else if (lhs instanceof char[]) {
                append((char[]) lhs, (char[]) rhs);
            } else if (lhs instanceof byte[]) {
                append((byte[]) lhs, (byte[]) rhs);
            } else if (lhs instanceof double[]) {
                append((double[]) lhs, (double[]) rhs);
            } else if (lhs instanceof float[]) {
                append((float[]) lhs, (float[]) rhs);
            } else if (lhs instanceof boolean[]) {
                append((boolean[]) lhs, (boolean[]) rhs);
            } else {
                // Not an array of primitives
                // this could throw a ClassCastException is rhs is not an array
                append((Object[]) lhs, (Object[]) rhs, comparator);
            }
        } else {
            // the simple case, not an array, just test the element
            if (comparator == null) {
                comparison = ((Comparable) lhs).compareTo(rhs);
            } else {
                comparison = comparator.compare(lhs, rhs);
            }
        }
        return this;
    }
{
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = (lhs.length < rhs.length) ? -1 : +1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i], comparator);
        }
        return this;
    }
{
        Throwable cause = getCauseUsingWellKnownTypes(t);
        if (cause == null)
        {
            for (int i = 0; i < methodNames.length; i++)
            {
                cause = getCauseUsingMethodName(t, methodNames[i]);
                if (cause != null)
                {
                    break;
                }
            }

            if (cause == null)
            {
                cause = getCauseUsingFieldName(t, "detail");
            }
        }
        return cause;
    }
{
        Method method = null;
        try
        {
            method = t.getClass().getMethod(methodName, null);
        }
        catch (NoSuchMethodException ignored)
        {
        }
        catch (SecurityException ignored)
        {
        }

        if (method != null &&
            Throwable.class.isAssignableFrom(method.getReturnType()))
        {
            try
            {
                return (Throwable) method.invoke(t, CAUSE_METHOD_PARAMS);
            }
            catch (IllegalAccessException ignored)
            {
            }
            catch (IllegalArgumentException ignored)
            {
            }
            catch (InvocationTargetException ignored)
            {
            }
        }
        return null;
    }

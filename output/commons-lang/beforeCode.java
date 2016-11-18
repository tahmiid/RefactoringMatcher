abbreviate(final String str, int offset, final int maxWidth) {
        if (str == null) {
            return null;
        }
        if (maxWidth < 4) {
            throw new IllegalArgumentException("Minimum abbreviation width is 4");
        }
        if (str.length() <= maxWidth) {
            return str;
        }
        if (offset > str.length()) {
            offset = str.length();
        }
        if (str.length() - offset < maxWidth - 3) {
            offset = str.length() - (maxWidth - 3);
        }
        final String abrevMarker = "...";
        if (offset <= 4) {
            return str.substring(0, maxWidth - 3) + abrevMarker;
        }
        if (maxWidth < 7) {
            throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
        }
        if (offset + maxWidth - 3 < str.length()) {
            return abrevMarker + abbreviate(str.substring(offset), maxWidth - 3);
        }
        return abrevMarker + str.substring(str.length() - (maxWidth - 3));
    }
acquire() throws InterruptedException {
        if (isShutdown()) {
            throw new IllegalStateException("TimedSemaphore is shut down!");
        }

        if (task == null) {
            task = startTimer();
        }

        boolean canPass = false;
        do {
            canPass = getLimit() <= NO_LIMIT || acquireCount < getLimit();
            if (!canPass) {
                wait();
            } else {
                acquireCount++;
            }
        } while (!canPass);
    }
acquire() throws InterruptedException {
        if (isShutdown()) {
            throw new IllegalStateException("TimedSemaphore is shut down!");
        }

        if (task == null) {
            task = startTimer();
        }

        boolean canPass = false;
        do {
            canPass = getLimit() <= NO_LIMIT || acquireCount < getLimit();
            if (!canPass) {
                wait();
            } else {
                acquireCount++;
            }
        } while (!canPass);
    }
isNumber(final String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        final char[] chars = str.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        // deal with any possible sign up front
        final int start = (chars[0] == '-') ? 1 : 0;
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
append(final Object lhs, final Object rhs, final Comparator<?> comparator) {
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
        if (lhs.getClass().isArray()) {
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
        } else {
            // the simple case, not an array, just test the element
            if (comparator == null) {
                @SuppressWarnings("unchecked") // assume this can be done; if not throw CCE as per Javadoc
                final Comparable<Object> comparable = (Comparable<Object>) lhs;
                comparison = comparable.compareTo(rhs);
            } else {
                @SuppressWarnings("unchecked") // assume this can be done; if not throw CCE as per Javadoc
                final Comparator<Object> comparator2 = (Comparator<Object>) comparator;
                comparison = comparator2.compare(lhs, rhs);
            }
        }
        return this;
    }
wrap(final String str, int wrapLength, String newLineStr, final boolean wrapLongWords) {
        if (str == null) {
            return null;
        }
        if (newLineStr == null) {
            newLineStr = SystemUtils.LINE_SEPARATOR;
        }
        if (wrapLength < 1) {
            wrapLength = 1;
        }
        final int inputLineLength = str.length();
        int offset = 0;
        final StringBuilder wrappedLine = new StringBuilder(inputLineLength + 32);
        
        while (offset < inputLineLength) {
            if (str.charAt(offset) == ' ') {
                offset++;
                continue;
            }
            // only last line without leading spaces is left
            if(inputLineLength - offset <= wrapLength) {
                break;
            }
            int spaceToWrapAt = str.lastIndexOf(' ', wrapLength + offset);

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
                    spaceToWrapAt = str.indexOf(' ', wrapLength + offset);
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
append(final Object object) {
        if (object == null) {
            iTotal = iTotal * iConstant;

        } else {
            if(object.getClass().isArray()) {
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
            } else {
                iTotal = iTotal * iConstant + object.hashCode();
            }
        }
        return this;
    }
invokeMethod(final Object object, final String methodName,
            Object[] args, Class<?>[] parameterTypes)
            throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
        args = ArrayUtils.nullToEmpty(args);
        final Method method = getMatchingAccessibleMethod(object.getClass(),
                methodName, parameterTypes);
        if (method == null) {
            throw new NoSuchMethodException("No such accessible method: "
                    + methodName + "() on object: "
                    + object.getClass().getName());
        }
        args = toVarArgs(method, args);
        return method.invoke(object, args);
    }
replace(final String text, final String searchString, final String replacement, int max) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
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
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }
append(final Object lhs, final Object rhs) {
        if (!isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            this.setEquals(false);
            return this;
        }
        final Class<?> lhsClass = lhs.getClass();
        if (!lhsClass.isArray()) {
            // The simple case, not an array, just test the element
            isEquals = lhs.equals(rhs);
        } else if (lhs.getClass() != rhs.getClass()) {
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
        return this;
    }
format(final Calendar calendar) {
        return format(calendar, new StringBuffer(mMaxLengthEstimate)).toString();
    }
format(final Calendar calendar) {
        return format(calendar, new StringBuffer(mMaxLengthEstimate)).toString();
    }
format(final Calendar calendar) {
        return format(calendar, new StringBuffer(mMaxLengthEstimate)).toString();
    }
addListener(final L listener) {
        Validate.notNull(listener, "Listener object cannot be null.");
        listeners.add(listener);
    }
validateSecretFieldAbsent(final String toString) {
        Assert.assertEquals(ArrayUtils.INDEX_NOT_FOUND, toString.indexOf(SECRET_FIELD));
        Assert.assertEquals(ArrayUtils.INDEX_NOT_FOUND, toString.indexOf(SECRET_VALUE));
        this.validateNonSecretField(toString);
    }
testSMTP(){
        final TimeZone timeZone = TimeZone.getTimeZone("GMT-3");
        final Calendar cal = Calendar.getInstance(timeZone);
        cal.set(2003, Calendar.JUNE, 8, 10, 11, 12);
        String text = DateFormatUtils.format(cal.getTime(), 
                        DateFormatUtils.SMTP_DATETIME_FORMAT.getPattern(), timeZone,
                        DateFormatUtils.SMTP_DATETIME_FORMAT.getLocale());
        assertEquals("Sun, 08 Jun 2003 10:11:12 -0300", text);
        text = DateFormatUtils.format(cal.getTime().getTime(), 
                        DateFormatUtils.SMTP_DATETIME_FORMAT.getPattern(), timeZone,
                        DateFormatUtils.SMTP_DATETIME_FORMAT.getLocale());
        assertEquals("Sun, 08 Jun 2003 10:11:12 -0300", text);
        text = DateFormatUtils.SMTP_DATETIME_FORMAT.format(cal);
        assertEquals("Sun, 08 Jun 2003 10:11:12 -0300", text);
        
        // format UTC
        text = DateFormatUtils.formatUTC(cal.getTime().getTime(), 
                        DateFormatUtils.SMTP_DATETIME_FORMAT.getPattern(),
                        DateFormatUtils.SMTP_DATETIME_FORMAT.getLocale());
        assertEquals("Sun, 08 Jun 2003 13:11:12 +0000", text);
    }
testDateTimeISO() throws Exception {
        final TimeZone timeZone = TimeZone.getTimeZone("GMT-3");
        final Calendar cal = Calendar.getInstance(timeZone);
        cal.set(2002, Calendar.FEBRUARY, 23, 9, 11, 12);
        String text = DateFormatUtils.format(cal.getTime(), 
                        DateFormatUtils.ISO_DATETIME_FORMAT.getPattern(), timeZone);
        assertEquals("2002-02-23T09:11:12", text);
        text = DateFormatUtils.format(cal.getTime().getTime(), 
                      DateFormatUtils.ISO_DATETIME_FORMAT.getPattern(), timeZone);
        assertEquals("2002-02-23T09:11:12", text);
        text = DateFormatUtils.ISO_DATETIME_FORMAT.format(cal);
        assertEquals("2002-02-23T09:11:12", text);
        
        text = DateFormatUtils.format(cal.getTime(), 
                      DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern(), timeZone);
        assertEquals("2002-02-23T09:11:12-03:00", text);
        text = DateFormatUtils.format(cal.getTime().getTime(), 
                      DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern(), timeZone);
        assertEquals("2002-02-23T09:11:12-03:00", text);
        text = DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(cal);
        assertEquals("2002-02-23T09:11:12-03:00", text);
        
        Calendar utcCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        utcCal.set(2002, Calendar.FEBRUARY, 23, 9, 11, 12);
        utcCal.set(Calendar.MILLISECOND, 0);
        text = DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(utcCal);
        assertEquals("2002-02-23T09:11:12Z", text);
        Date date = DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.parse(text);
        assertEquals(utcCal.getTime(), date);
    }
testDateISO(){
        final TimeZone timeZone = TimeZone.getTimeZone("GMT-3");
        final Calendar cal = Calendar.getInstance(timeZone);
        cal.set(2002, Calendar.FEBRUARY, 23, 10, 11, 12);
        String text = DateFormatUtils.format(cal.getTime(), 
                        DateFormatUtils.ISO_DATE_FORMAT.getPattern(), timeZone);
        assertEquals("2002-02-23", text);
        text = DateFormatUtils.format(cal.getTime().getTime(), 
                        DateFormatUtils.ISO_DATE_FORMAT.getPattern(), timeZone);
        assertEquals("2002-02-23", text);
        text = DateFormatUtils.ISO_DATE_FORMAT.format(cal);
        assertEquals("2002-02-23", text);
        
        text = DateFormatUtils.format(cal.getTime(), 
                      DateFormatUtils.ISO_DATE_TIME_ZONE_FORMAT.getPattern(), timeZone);
        assertEquals("2002-02-23-03:00", text);
        text = DateFormatUtils.format(cal.getTime().getTime(), 
                      DateFormatUtils.ISO_DATE_TIME_ZONE_FORMAT.getPattern(), timeZone);
        assertEquals("2002-02-23-03:00", text);
        text = DateFormatUtils.ISO_DATE_TIME_ZONE_FORMAT.format(cal);
        assertEquals("2002-02-23-03:00", text);
    }
testTimeISO(){
        final TimeZone timeZone = TimeZone.getTimeZone("GMT-3");
        final Calendar cal = Calendar.getInstance(timeZone);
        cal.set(2002, Calendar.FEBRUARY, 23, 10, 11, 12);
        String text = DateFormatUtils.format(cal.getTime(), 
                        DateFormatUtils.ISO_TIME_FORMAT.getPattern(), timeZone);
        assertEquals("T10:11:12", text);
        text = DateFormatUtils.format(cal.getTime().getTime(), 
                        DateFormatUtils.ISO_TIME_FORMAT.getPattern(), timeZone);
        assertEquals("T10:11:12", text);
        text = DateFormatUtils.ISO_TIME_FORMAT.format(cal);
        assertEquals("T10:11:12", text);
        
        text = DateFormatUtils.format(cal.getTime(), 
                      DateFormatUtils.ISO_TIME_TIME_ZONE_FORMAT.getPattern(), timeZone);
        assertEquals("T10:11:12-03:00", text);
        text = DateFormatUtils.format(cal.getTime().getTime(), 
                      DateFormatUtils.ISO_TIME_TIME_ZONE_FORMAT.getPattern(), timeZone);
        assertEquals("T10:11:12-03:00", text);
        text = DateFormatUtils.ISO_TIME_TIME_ZONE_FORMAT.format(cal);
        assertEquals("T10:11:12-03:00", text);
    }
testTimeNoTISO(){
        final TimeZone timeZone = TimeZone.getTimeZone("GMT-3");
        final Calendar cal = Calendar.getInstance(timeZone);
        cal.set(2002, Calendar.FEBRUARY, 23, 10, 11, 12);
        String text = DateFormatUtils.format(cal.getTime(), 
                        DateFormatUtils.ISO_TIME_NO_T_FORMAT.getPattern(), timeZone);
        assertEquals("10:11:12", text);
        text = DateFormatUtils.format(cal.getTime().getTime(), 
                        DateFormatUtils.ISO_TIME_NO_T_FORMAT.getPattern(), timeZone);
        assertEquals("10:11:12", text);
        text = DateFormatUtils.ISO_TIME_NO_T_FORMAT.format(cal);
        assertEquals("10:11:12", text);
        
        text = DateFormatUtils.format(cal.getTime(), 
                      DateFormatUtils.ISO_TIME_NO_T_TIME_ZONE_FORMAT.getPattern(), timeZone);
        assertEquals("10:11:12-03:00", text);
        text = DateFormatUtils.format(cal.getTime().getTime(), 
                      DateFormatUtils.ISO_TIME_NO_T_TIME_ZONE_FORMAT.getPattern(), timeZone);
        assertEquals("10:11:12-03:00", text);
        text = DateFormatUtils.ISO_TIME_NO_T_TIME_ZONE_FORMAT.format(cal);
        assertEquals("10:11:12-03:00", text);
    }
allPublicChildFields() {
        Class<? super PublicChild> parentClass = PublicChild.class.getSuperclass();
		final Field[] fieldsParent =  parentClass.getDeclaredFields();
        assertArrayEquals(fieldsParent, FieldUtils.getAllFields(parentClass));

		final Field[] fieldsPublicChild = PublicChild.class.getDeclaredFields();
        return ArrayUtils.addAll(fieldsPublicChild, fieldsParent);
    }
allIntegerFields() {
        final Field[] fieldsNumber = Number.class.getDeclaredFields();
        assertArrayEquals(Number.class.getDeclaredFields(), FieldUtils.getAllFields(Number.class));
        final Field[] fieldsInteger = Integer.class.getDeclaredFields();
        return ArrayUtils.addAll(fieldsInteger, fieldsNumber);
    }
testGetAllFields() {
        assertArrayEquals(new Field[0], FieldUtils.getAllFields(Object.class));
        final Field[] fieldsNumber = Number.class.getDeclaredFields();
        assertArrayEquals(fieldsNumber, FieldUtils.getAllFields(Number.class));
        final Field[] fieldsInteger = Integer.class.getDeclaredFields();
        assertArrayEquals(ArrayUtils.addAll(fieldsInteger, fieldsNumber), FieldUtils.getAllFields(Integer.class));
        assertEquals(5, FieldUtils.getAllFields(PublicChild.class).length);
    }
testGetAllFields() {
        assertArrayEquals(new Field[0], FieldUtils.getAllFields(Object.class));
        final Field[] fieldsNumber = Number.class.getDeclaredFields();
        assertArrayEquals(fieldsNumber, FieldUtils.getAllFields(Number.class));
        final Field[] fieldsInteger = Integer.class.getDeclaredFields();
        assertArrayEquals(ArrayUtils.addAll(fieldsInteger, fieldsNumber), FieldUtils.getAllFields(Integer.class));
        assertEquals(5, FieldUtils.getAllFields(PublicChild.class).length);
    }
testGetConcurrentOptionallyWithException(boolean expectExceptions, String expectedMessage,
                                                            Exception expectedCause)
            throws ConcurrentException, InterruptedException {

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
testGetConcurrent() throws ConcurrentException,
            InterruptedException {
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
checkParse(final Locale locale, final Calendar cal, final SimpleDateFormat sdf, final DateParser fdf) throws ParseException {
        final String formattedDate= sdf.format(cal.getTime());
        final Date expectedTime = sdf.parse(formattedDate);
        final Date actualTime = fdf.parse(formattedDate);
        assertEquals(locale.toString()+" "+formattedDate
                +"\n",expectedTime, actualTime);
    }
removeFinalModifier(Field field) {
        Validate.isTrue(field != null, "The field must not be null");

        try {
            if (Modifier.isFinal(field.getModifiers())) {
                // Do all JREs implement Field with a private ivar called "modifiers"?
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                try {
                    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                } finally {
                    modifiersField.setAccessible(false);
                }
            }
        } catch (NoSuchFieldException ignored) {
            // The field class contains always a modifiers field
        } catch (IllegalAccessException ignored) {
             // The modifiers field is made accessible
        }
    }
testParses() throws Exception {
        for(final Locale locale : Locale.getAvailableLocales()) {
            for(final TimeZone tz : new TimeZone[]{NEW_YORK, GMT}) {
                final Calendar cal = Calendar.getInstance(tz);
                for(final int year : new int[]{2003, 1940, 1868, 1867, 0, -1940}) {
                    // http://docs.oracle.com/javase/6/docs/technotes/guides/intl/calendar.doc.html
                    if (year < 1868 && locale.equals(FastDateParser.JAPANESE_IMPERIAL)) {
                        continue; // Japanese imperial calendar does not support eras before 1868
                    }
                    cal.clear();
                    if (year < 0) {
                        cal.set(-year, 1, 10);
                        cal.set(Calendar.ERA, GregorianCalendar.BC);
                    } else {
                        cal.set(year, 1, 10);
                    }
                    final Date in = cal.getTime();
                    for(final String format : new String[]{LONG_FORMAT, SHORT_FORMAT}) {
                        final SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
                        if (format.equals(SHORT_FORMAT)) {
                            if (year < 1930) {
                                sdf.set2DigitYearStart(cal.getTime());
                            }
                        }
                        final String fmt = sdf.format(in);
                        try {
                            final Date out = sdf.parse(fmt);

                            assertEquals(locale.toString()+" "+year+" "+ format+ " "+tz.getID(), in, out);
                        } catch (final ParseException pe) {
                            System.out.println(fmt+" "+locale.toString()+" "+year+" "+ format+ " "+tz.getID());
                            throw pe;
                        }
                    }
                }
            }
        }
    }
testParses() throws Exception {
        for(final Locale locale : Locale.getAvailableLocales()) {
            for(final TimeZone tz : new TimeZone[]{NEW_YORK, GMT}) {
                final Calendar cal = Calendar.getInstance(tz);
                for(final int year : new int[]{2003, 1940, 1868, 1867, 0, -1940}) {
                    // http://docs.oracle.com/javase/6/docs/technotes/guides/intl/calendar.doc.html
                    if (year < 1868 && locale.equals(FastDateParser.JAPANESE_IMPERIAL)) {
                        continue; // Japanese imperial calendar does not support eras before 1868
                    }
                    cal.clear();
                    if (year < 0) {
                        cal.set(-year, 1, 10);
                        cal.set(Calendar.ERA, GregorianCalendar.BC);
                    } else {
                        cal.set(year, 1, 10);
                    }
                    final Date in = cal.getTime();
                    for(final String format : new String[]{LONG_FORMAT, SHORT_FORMAT}) {
                        final SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
                        if (format.equals(SHORT_FORMAT)) {
                            if (year < 1930) {
                                sdf.set2DigitYearStart(cal.getTime());
                            }
                        }
                        final String fmt = sdf.format(in);
                        try {
                            final Date out = sdf.parse(fmt);

                            assertEquals(locale.toString()+" "+year+" "+ format+ " "+tz.getID(), in, out);
                        } catch (final ParseException pe) {
                            System.out.println(fmt+" "+locale.toString()+" "+year+" "+ format+ " "+tz.getID());
                            throw pe;
                        }
                    }
                }
            }
        }
    }
reverse(final Object[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        Object tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
reverse(final long[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        long tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
reverse(final int[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        int tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
reverse(final short[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        short tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
reverse(final char[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        char tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
reverse(final byte[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
reverse(final double[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        double tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
reverse(final float[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        float tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
reverse(final boolean[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        boolean tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
reverse(final byte[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
reverse(final char[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        char tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
reverse(final double[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        double tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
reverse(final float[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        float tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
reverse(final int[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        int tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
reverse(final long[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        long tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
reverse(final Object[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        Object tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
reverse(final short[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        short tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
getAllFields(Class<?> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("The class must not be null");
        }
        List<Field[]> fieldArrayList = new ArrayList<Field[]>();
        int fieldCount = 0;
        Class<?> queryClass = cls;
        while (queryClass != null) {
            final Field[] declaredFields = queryClass.getDeclaredFields();
            fieldCount += declaredFields.length;
            fieldArrayList.add(declaredFields);
            queryClass = queryClass.getSuperclass();
        }
        Field fields[] = new Field[fieldCount];
        int fieldIndex = 0;
        for (Field[] fieldArray : fieldArrayList) {
            for (Field field : fieldArray) {
                fields[fieldIndex++] = field;
            }
        }
        return fields;
    }
min(long[] array) {
        // Validates input
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
    
        // Finds and returns min
        long min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
    
        return min;
    }
min(int[] array) {
        // Validates input
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
    
        // Finds and returns min
        int min = array[0];
        for (int j = 1; j < array.length; j++) {
            if (array[j] < min) {
                min = array[j];
            }
        }
    
        return min;
    }
min(short[] array) {
        // Validates input
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
    
        // Finds and returns min
        short min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
    
        return min;
    }
min(byte[] array) {
        // Validates input
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
    
        // Finds and returns min
        byte min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
    
        return min;
    }
min(double[] array) {
        // Validates input
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
    
        // Finds and returns min
        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (Double.isNaN(array[i])) {
                return Double.NaN;
            }
            if (array[i] < min) {
                min = array[i];
            }
        }
    
        return min;
    }
min(float[] array) {
        // Validates input
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
    
        // Finds and returns min
        float min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (Float.isNaN(array[i])) {
                return Float.NaN;
            }
            if (array[i] < min) {
                min = array[i];
            }
        }
    
        return min;
    }
max(long[] array) {
        // Validates input
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }

        // Finds and returns max
        long max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (array[j] > max) {
                max = array[j];
            }
        }

        return max;
    }
max(int[] array) {
        // Validates input
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
    
        // Finds and returns max
        int max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (array[j] > max) {
                max = array[j];
            }
        }
    
        return max;
    }
max(short[] array) {
        // Validates input
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
    
        // Finds and returns max
        short max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
    
        return max;
    }
max(byte[] array) {
        // Validates input
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
    
        // Finds and returns max
        byte max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
    
        return max;
    }
max(double[] array) {
        // Validates input
        if (array== null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
    
        // Finds and returns max
        double max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (Double.isNaN(array[j])) {
                return Double.NaN;
            }
            if (array[j] > max) {
                max = array[j];
            }
        }
    
        return max;
    }
max(float[] array) {
        // Validates input
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }

        // Finds and returns max
        float max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (Float.isNaN(array[j])) {
                return Float.NaN;
            }
            if (array[j] > max) {
                max = array[j];
            }
        }

        return max;
    }
format(long millis) {
        Calendar c = new GregorianCalendar(mTimeZone, mLocale);  // hard code GregorianCalendar
        c.setTimeInMillis(millis);
        return applyRules(c, new StringBuffer(mMaxLengthEstimate)).toString();
    }
format(Date date) {
        Calendar c = new GregorianCalendar(mTimeZone, mLocale);  // hard code GregorianCalendar
        c.setTime(date);
        return applyRules(c, new StringBuffer(mMaxLengthEstimate)).toString();
    }
getStrategy(String formatField) {
        switch(formatField.charAt(0)) {
        case '\'':
            if(formatField.length()>2) {
                formatField= formatField.substring(1, formatField.length()-1);
            }
            //$FALL-THROUGH$
        default:
            return new CopyQuotedStrategy(formatField);
        case 'D':
            return DAY_OF_YEAR_STRATEGY;
        case 'E':
            return DAY_OF_WEEK_STRATEGY;
        case 'F':
            return DAY_OF_WEEK_IN_MONTH_STRATEGY;
        case 'G':
            return ERA_STRATEGY;
        case 'H':
            return MODULO_HOUR_OF_DAY_STRATEGY;
        case 'K':
            return HOUR_STRATEGY;
        case 'M':
            return formatField.length()>=3 ?TEXT_MONTH_STRATEGY :NUMBER_MONTH_STRATEGY;
        case 'S':
            return MILLISECOND_STRATEGY;
        case 'W':
            return WEEK_OF_MONTH_STRATEGY;
        case 'Z':
            break;
        case 'a':
            return AM_PM_STRATEGY;
        case 'd':
            return DAY_OF_MONTH_STRATEGY;
        case 'h':
            return MODULO_HOUR_STRATEGY;
        case 'k':
            return HOUR_OF_DAY_STRATEGY;
        case 'm':
            return MINUTE_STRATEGY;
        case 's':
            return SECOND_STRATEGY;
        case 'w':
            return WEEK_OF_YEAR_STRATEGY;
        case 'y':
            return formatField.length()>2 ?LITERAL_YEAR_STRATEGY :ABBREVIATED_YEAR_STRATEGY;
        case 'z':
            break;
        }
        TimeZoneStrategy tzs= tzsCache.get(locale);
        if(tzs==null) {
            tzs= new TimeZoneStrategy(locale);
            TimeZoneStrategy inCache= tzsCache.putIfAbsent(locale, tzs);
            if(inCache!=null) {
                return inCache;
            }
        }
        return tzs;
    }
testLocales_Long_AD() throws Exception {
                
        for(Locale locale : Locale.getAvailableLocales()) {
            Calendar cal= Calendar.getInstance(NEW_YORK, Locale.US);
            cal.clear();
            cal.set(2003, 1, 10);

            try {
                String longFormat= "GGGG/yyyy/MMMM/dddd/aaaa/EEEE/ZZZZ";
                SimpleDateFormat sdf = new SimpleDateFormat(longFormat, locale);
                DateParser fdf = getInstance(longFormat, locale);
                checkParse(cal, sdf, fdf);
            }
            catch(ParseException ex) {
                // TODO: why do ja_JP_JP, hi_IN, th_TH, and th_TH_TH fail?
                System.out.println("Long AD Locale "+locale+ " failed\n" + ex.toString());
            }
        }
    }
testLocales_Long_BC() throws Exception {
                
        for(Locale locale : Locale.getAvailableLocales()) {
            Calendar cal= Calendar.getInstance(NEW_YORK, Locale.US);
            cal.clear();
            cal.set(2003, 1, 10);
            cal.set(Calendar.ERA, GregorianCalendar.BC);

            try {
                String longFormat= "GGGG/yyyy/MMMM/dddd/aaaa/EEEE/ZZZZ";
                SimpleDateFormat sdf = new SimpleDateFormat(longFormat, locale);
                DateParser fdf = getInstance(longFormat, locale);               
                checkParse(cal, sdf, fdf);
            }
            catch(ParseException ex) {
                // TODO: why do ja_JP_JP, hi_IN, th_TH, and th_TH_TH fail?
                System.out.println("Long BC Locale "+locale+ " failed\n" + ex.toString());
            }
        }
    }
testLocales_Short_BC() throws Exception {
                
        for(Locale locale : Locale.getAvailableLocales()) {
            Calendar cal= Calendar.getInstance(NEW_YORK, Locale.US);
            cal.clear();
            cal.set(2003, 1, 10);
            cal.set(Calendar.ERA, GregorianCalendar.BC);                        

            try {
                String shortFormat= "G/y/M/d/a/E/Z";
                SimpleDateFormat sdf = new SimpleDateFormat(shortFormat, locale);
                DateParser fdf = getInstance(shortFormat, locale);
                checkParse(cal, sdf, fdf);
            }
            catch(ParseException ex) {
                // TODO: why do ja_JP_JP, hi_IN, th_TH, and th_TH_TH fail?
                System.out.println("Short BC Locale "+locale+ " failed\n" + ex.toString());
            }
        }
    }
testLocales_Short_AD() throws Exception {
                
        for(Locale locale : Locale.getAvailableLocales()) {
            Calendar cal= Calendar.getInstance(NEW_YORK, Locale.US);
            cal.clear();
            cal.set(2003, 1, 10);
            cal.set(Calendar.ERA, GregorianCalendar.AD);

            try {
                String shortFormat= "G/y/M/d/a/E/Z";
                SimpleDateFormat sdf = new SimpleDateFormat(shortFormat, locale);
                DateParser fdf = getInstance(shortFormat, locale);              
                checkParse(cal, sdf, fdf);
            }
            catch(ParseException ex) {
                // TODO: why do ja_JP_JP, hi_IN, th_TH, and th_TH_TH fail?
                System.out.println("Short_AD Locale "+locale+ " failed\n" + ex.toString());
            }
        }
    }
processBitVector(Class<E> enumClass, long value) {
        final E[] constants = checkBitVectorable(enumClass).getEnumConstants();
        final EnumSet<E> results = EnumSet.noneOf(enumClass);
        for (E constant : constants) {
            if ((value & 1 << constant.ordinal()) != 0) {
                results.add(constant);
            }
        }
        return results;
    }
getDateTimeInstance(Integer dateStyle, Integer timeStyle, TimeZone timeZone, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
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
        
        return getInstance(pattern, timeZone, locale);
    }
processBitVector(Class<E> enumClass, long value) {
        if (enumClass == null) {
            throw new IllegalArgumentException("EnumClass must be defined.");
        }
        final E[] constants = enumClass.getEnumConstants();
        if (constants != null && constants.length > 64) {
            throw new IllegalArgumentException("EnumClass is too big to be stored in a 64-bit value.");
        }
        final EnumSet results = EnumSet.noneOf(enumClass);
        if (constants != null && constants.length > 0) {
            for (E constant : constants) {
                if ((value & (1 << constant.ordinal())) != 0) {
                    results.add(constant);
                }
            }
        }
        return results;
    }
generateBitVector(Class<E> enumClass, EnumSet<E> set) {
        if (enumClass == null) {
            throw new IllegalArgumentException("EnumClass must be defined.");
        }
        final E[] constants = enumClass.getEnumConstants();
        if (constants != null && constants.length > 64) {
            throw new IllegalArgumentException("EnumClass is too big to be stored in a 64-bit value.");
        }
        long total = 0;
        if (set != null) {
            if (constants != null && constants.length > 0) {
                for (E constant : constants) {
                    if (set.contains(constant)) {
                        total += Math.pow(2, constant.ordinal());
                    }
                }
            }
        }
        return total;
    }
addContextValue(Pair<String, Object> pair) {
        if (pair == null) {
            throw new NullPointerException();
        }
        contextValues.add(pair);
        return this;
    }
setContextValue(Pair<String, Object> pair) {
        final String label = pair.getKey(); // implicit NPE
        for (final Iterator<Pair<String, Object>> iter = contextValues.iterator(); iter.hasNext();) {
            final Pair<String, Object> p = iter.next();
            if (StringUtils.equals(label, p.getKey())) {
                iter.remove();
            }
        }
        return addContextValue(pair);
    }
sequenceToString(CharSequence cs) {
        if (cs instanceof String) {
            return ((String) cs);
        } else {
            return cs.toString();
        }
    }
sequenceToString(CharSequence cs) {
        if (cs instanceof String) {
            return ((String) cs);
        } else {
            return cs.toString();
        }
    }
getOSMatches(String osNamePrefix, String osVersionPrefix) {
        if (OS_NAME == null || OS_VERSION == null) {
            return false;
        }
        return OS_NAME.startsWith(osNamePrefix) && OS_VERSION.startsWith(osVersionPrefix);
    }
getJavaVersionAsFloat() {
        if (JAVA_VERSION_TRIMMED == null) {
            return 0f;
        }
        String str = JAVA_VERSION_TRIMMED.substring(0, 3);
        if (JAVA_VERSION_TRIMMED.length() >= 5) {
            str = str + JAVA_VERSION_TRIMMED.substring(4, 5);
        }
        try {
            return Float.parseFloat(str);
        } catch (Exception ex) {
            return 0;
        }
    }
getJavaVersionAsInt() {
        if (JAVA_VERSION_TRIMMED == null) {
            return 0;
        }
        String str = JAVA_VERSION_TRIMMED.substring(0, 1);
        str = str + JAVA_VERSION_TRIMMED.substring(2, 3);
        if (JAVA_VERSION_TRIMMED.length() >= 5) {
            str = str + JAVA_VERSION_TRIMMED.substring(4, 5);
        } else {
            str = str + "0";
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception ex) {
            return 0;
        }
    }
toArray(List<String> list) {
        return list.toArray(new String[list.size()]);
    }
parseDate(String str, String[] parsePatterns) throws ParseException {
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
ordinalIndexOf(String str, String searchStr, int ordinal) {
        if (str == null || searchStr == null || ordinal <= 0) {
            return INDEX_NOT_FOUND;
        }
        if (searchStr.length() == 0) {
            return 0;
        }
        int found = 0;
        int index = INDEX_NOT_FOUND;
        do {
            index = str.indexOf(searchStr, index + 1);
            if (index < 0) {
                return index;
            }
            found++;
        } while (found < ordinal);
        return index;
    }
appendSeparator(String separator) {
        if (separator != null && size() > 0) {
            append(separator);
        }
        return this;
    }
appendSeparator(String separator) {
        if (separator != null && size() > 0) {
            append(separator);
        }
        return this;
    }
translate(CharSequence input, int index, Writer out) throws IOException {
        if(input.charAt(index) == '\\') {
            if( (index + 1 < input.length()) && input.charAt(index + 1) == 'u') {
                // consume optional additional 'u' chars
                int i=2;
                while( (index + i < input.length()) && input.charAt(index + i) == 'u') {
                    i++;
                }

                if( (index + i + 4 <= input.length()) ) {
                    // Get 4 hex digits
                    CharSequence unicode = input.subSequence(index + i, index + i + 4);

                    try {
                        int value = Integer.parseInt(unicode.toString(), 16);
                        out.write((char) value);
                    } catch (NumberFormatException nfe) {
                        throw new RuntimeException("Unable to parse unicode value: " + unicode, nfe);
                    }
                    return i + 4;
                } else {
                    throw new IllegalArgumentException("Less than 4 hex digits in unicode value: '" + 
                                                       input.subSequence(index, input.length()) +
                                                       "' due to end of CharSequence");
                }
            }
        }
        return 0;
    }
getAllInterfaces(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        List<Class<?>> list = new ArrayList<Class<?>>();
        while (cls != null) {
            Class<?>[] interfaces = cls.getInterfaces();
            for (Class<?> intface : interfaces) {
                if (list.contains(intface) == false) {
                    list.add(intface);
                }
                List<Class<?>> superInterfaces = getAllInterfaces(intface);
                for (Class<?> superInterface : superInterfaces) {
                    if (list.contains(superInterface) == false) {
                        list.add(superInterface);
                    }
                }
            }
            cls = cls.getSuperclass();
        }
        return list;
    }
availableLocaleSet() {
        Set<Locale> set = cAvailableLocaleSet;
        if (set == null) {
            set = new HashSet<Locale>(availableLocaleList());
            set = Collections.unmodifiableSet(set);
            cAvailableLocaleSet = set;
        }
        return set;
    }
getTime() {
        if (this.runningState == STATE_STOPPED || this.runningState == STATE_SUSPENDED) {
            return this.stopTime - this.startTime;
        } else if (this.runningState == STATE_UNSTARTED) {
            return 0;
        } else if (this.runningState == STATE_RUNNING) {
            return System.currentTimeMillis() - this.startTime;
        }
        throw new RuntimeException("Illegal running state has occured. ");
    }
getSplitTime() {
        if (this.splitState != STATE_SPLIT) {
            throw new IllegalStateException("Stopwatch must be split to get the split time. ");
        }
        return this.stopTime - this.startTime;
    }
checkBuiltInFormat(String pattern, Object[] args, Locale locale) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Pattern=[");
        buffer.append(pattern);
        buffer.append("], locale=[");
        buffer.append(locale);
        buffer.append("]");
        MessageFormat mf = new MessageFormat(pattern);
        if (locale != null) {
            mf.setLocale(locale);
            mf.applyPattern(pattern);
        }
        // System.out.println(buffer + ", result=[" + mf.format(args) +"]");
        ExtendedMessageFormat emf = null;
        if (locale == null) {
            emf = new ExtendedMessageFormat(pattern);
        } else {
            emf = new ExtendedMessageFormat(pattern, locale);
        }
        assertEquals("format "    + buffer.toString(), mf.format(args), emf.format(args));
        assertPatternsEqual("toPattern " + buffer.toString(), mf.toPattern(),  emf.toPattern());
    }
isAssignable(Class[] classArray, Class[] toClassArray) {
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
            if (isAssignable(classArray[i], toClassArray[i]) == false) {
                return false;
            }
        }
        return true;
    }
isAssignable(Class cls, Class toClass) {
        if (toClass == null) {
            return false;
        }
        // have to check for null, as isAssignableFrom doesn't
        if (cls == null) {
            return !(toClass.isPrimitive());
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
isAssignable(Class cls, Class toClass) {
        if (toClass == null) {
            return false;
        }
        // have to check for null, as isAssignableFrom doesn't
        if (cls == null) {
            return !(toClass.isPrimitive());
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
testExtendedFormats() {
        String pattern = "Lower: {0,lower} Upper: {1,upper}";
        ExtendedMessageFormat emf = new ExtendedMessageFormat(pattern, registry);
        assertEquals("TOPATTERN", pattern, emf.toPattern());
        assertEquals("Lower: foo Upper: BAR", emf.format(new Object[] {"foo", "bar"}));
        assertEquals("Lower: foo Upper: BAR", emf.format(new Object[] {"Foo", "Bar"}));
        assertEquals("Lower: foo Upper: BAR", emf.format(new Object[] {"FOO", "BAR"}));
        assertEquals("Lower: foo Upper: BAR", emf.format(new Object[] {"FOO", "bar"}));
        assertEquals("Lower: foo Upper: BAR", emf.format(new Object[] {"foo", "BAR"}));
    }
checkBuiltInFormat(String pattern, Object[] args, Locale locale) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Pattern=[");
        buffer.append(pattern);
        buffer.append("], locale=[");
        buffer.append(locale);
        buffer.append("]");
        MessageFormat mf = new MessageFormat(pattern);
        if (locale != null) {
            mf.setLocale(locale);
            mf.applyPattern(pattern);
        }
        // System.out.println(buffer + ", result=[" + mf.format(args) +"]");
        ExtendedMessageFormat emf = null;
        if (locale == null) {
            emf = new ExtendedMessageFormat(pattern);
        } else {
            emf = new ExtendedMessageFormat(pattern, locale);
        }
        assertEquals("format "    + buffer.toString(), mf.format(args), emf.format(args));
        assertEquals("toPattern " + buffer.toString(), mf.toPattern(),  emf.toPattern());
    }
splitByWholeSeparator( String str, String separator, int max ) {
        if (str == null) {
            return null;
        }

        int len = str.length() ;

        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        if ( ( separator == null ) || ( "".equals( separator ) ) ) {
            // Split on whitespace.
            return split( str, null, max ) ;
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
testFullTime() {
        DateFormat df = DateFormat.getTimeInstance(DateFormat.FULL);
        StringBuffer expected = new StringBuffer();
        for (int i = 0; i < DATES.length; i++) {
            if (i > 0) {
                expected.append("; ");
            }
            expected.append("Time ").append(i).append(": ").append(
                    df.format(DATES[i]));
        }
        doAssertions(
                expected.toString(),
                "Time 0: {0,time,full}; Time 1: {1,time,full}; Time 2: {2,time,full}",
                DATES,
                "Time 0: {0,time,long}; Time 1: {1,time,long}; Time 2: {2,time,long}");
    }
unescape(Writer writer, String string) throws IOException {
        int firstAmp = string.indexOf('&');
        if (firstAmp < 0) {
            writer.write(string);
            return;
        }

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
escape(StringWriter writer, String str) {
        try {
            this.escape((Writer) writer, str);
        } catch (IOException e) {
            // This should never happen because ALL the StringWriter methods called by #escape(Writer, String) do not
            // throw IOExceptions.
            throw new UnhandledException(e);
        }
    }
unescape(StringWriter writer, String string) {
        try {
            this.unescape((Writer) writer, string);
        } catch (IOException e) {
            // This should never happen because ALL the StringWriter methods called by #escape(Writer, String) do not
            // throw IOExceptions.
            throw new UnhandledException(e);
        }
    }
appendInternal(StringBuffer buffer, String fieldName, Object value, boolean detail) {
        if (ReflectionToStringBuilder.isRegistered(value)
            && !(value instanceof Number || value instanceof Boolean || value instanceof Character)) {
            ObjectUtils.appendIdentityToString(buffer, value);

        } else if (value instanceof Collection) {
            if (detail) {
                appendDetail(buffer, fieldName, (Collection) value);
            } else {
                appendSummarySize(buffer, fieldName, ((Collection) value).size());
            }

        } else if (value instanceof Map) {
            if (detail) {
                appendDetail(buffer, fieldName, (Map) value);
            } else {
                appendSummarySize(buffer, fieldName, ((Map) value).size());
            }

        } else if (value instanceof long[]) {
            if (detail) {
                appendDetail(buffer, fieldName, (long[]) value);
            } else {
                appendSummary(buffer, fieldName, (long[]) value);
            }

        } else if (value instanceof int[]) {
            if (detail) {
                appendDetail(buffer, fieldName, (int[]) value);
            } else {
                appendSummary(buffer, fieldName, (int[]) value);
            }

        } else if (value instanceof short[]) {
            if (detail) {
                appendDetail(buffer, fieldName, (short[]) value);
            } else {
                appendSummary(buffer, fieldName, (short[]) value);
            }

        } else if (value instanceof byte[]) {
            if (detail) {
                appendDetail(buffer, fieldName, (byte[]) value);
            } else {
                appendSummary(buffer, fieldName, (byte[]) value);
            }

        } else if (value instanceof char[]) {
            if (detail) {
                appendDetail(buffer, fieldName, (char[]) value);
            } else {
                appendSummary(buffer, fieldName, (char[]) value);
            }

        } else if (value instanceof double[]) {
            if (detail) {
                appendDetail(buffer, fieldName, (double[]) value);
            } else {
                appendSummary(buffer, fieldName, (double[]) value);
            }

        } else if (value instanceof float[]) {
            if (detail) {
                appendDetail(buffer, fieldName, (float[]) value);
            } else {
                appendSummary(buffer, fieldName, (float[]) value);
            }

        } else if (value instanceof boolean[]) {
            if (detail) {
                appendDetail(buffer, fieldName, (boolean[]) value);
            } else {
                appendSummary(buffer, fieldName, (boolean[]) value);
            }

        } else if (value.getClass().isArray()) {
            if (detail) {
                appendDetail(buffer, fieldName, (Object[]) value);
            } else {
                appendSummary(buffer, fieldName, (Object[]) value);
            }

        } else {
            if (detail) {
                appendDetail(buffer, fieldName, value);
            } else {
                appendSummary(buffer, fieldName, value);
            }
        }
    }
validateEmptyReflectionRegistry() {
        assertTrue(ReflectionToStringBuilder.getRegistry().isEmpty());        
    }
validateEmptyReflectionRegistry() {
        assertTrue(ReflectionToStringBuilder.getRegistry().isEmpty());        
    }
validateEmptyReflectionRegistry() {
        assertTrue(ReflectionToStringBuilder.getRegistry().isEmpty());        
    }
assertEqualDuration(String expected, int[] start, int[] end, String format) {
        Calendar cal1 = Calendar.getInstance();
        cal1.set(start[0], start[1], start[2], start[3], start[4], start[5]);
        cal1.set(Calendar.MILLISECOND, 0);
        Calendar cal2 = Calendar.getInstance();
        cal2.set(end[0], end[1], end[2], end[3], end[4], end[5]);
        cal2.set(Calendar.MILLISECOND, 0);
        long milli1 = cal1.getTime().getTime();
        long milli2 = cal2.getTime().getTime();
        String result = DurationFormatUtils.formatPeriod(milli1, milli2, format);
        assertEquals(expected, result);
    }
testBugzilla38401() {
        Calendar cal1 = Calendar.getInstance();
        cal1.set(2006, 0, 26, 18, 47, 34);
        cal1.set(Calendar.MILLISECOND, 0);
        Calendar cal2 = Calendar.getInstance();
        cal2.set(2006, 1, 26, 10, 47, 34);
        cal2.set(Calendar.MILLISECOND, 0);

        assertEquals( "0000/00/30 16:00:00 000", DurationFormatUtils.formatPeriod(cal1.getTime().getTime(), cal2.getTime().getTime(), "yyyy/MM/dd HH:mm:ss SSS") );
    }
testJiraLang281() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        cal.set(Calendar.YEAR, 2005);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.MONTH, Calendar.OCTOBER);
        cal2.set(Calendar.DAY_OF_MONTH, 6);
        cal2.set(Calendar.YEAR, 2006);
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        String result = DurationFormatUtils.formatPeriod(cal.getTime().getTime(), cal2.getTime().getTime(), "MM");
        assertEquals("09", result);
    }
join(Object[] array, char separator) {
        if (array == null) {
            return null;
        }
        int arraySize = array.length;
        int bufSize = (arraySize == 0 ? 0 : ((array[0] == null ? 16 : array[0].toString().length()) + 1) * arraySize);
        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }
join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }
        int arraySize = array.length;

        // ArraySize ==  0: Len = 0
        // ArraySize > 0:   Len = NofStrings *(len(firstString) + len(separator))
        //           (Assuming that all Strings are roughly equally long)
        int bufSize =
            ((arraySize == 0)
                ? 0
                : arraySize
                    * ((array[0] == null ? 16 : array[0].toString().length())
                        + separator.length()));

        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }
join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }
        int arraySize = array.length;

        // ArraySize ==  0: Len = 0
        // ArraySize > 0:   Len = NofStrings *(len(firstString) + len(separator))
        //           (Assuming that all Strings are roughly equally long)
        int bufSize =
            ((arraySize == 0)
                ? 0
                : arraySize
                    * ((array[0] == null ? 16 : array[0].toString().length())
                        + separator.length()));

        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }
equals(Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other.getClass() == this.getClass()) {
            // Ok to do a class cast to Enum here since the test above
            // guarantee both
            // classes are in the same class loader.
            return iName.equals(((Enum) other).iName);
        } else {
            // This and other are in different class loaders, we must use reflection.
            try {
                Method mth = other.getClass().getMethod("getName", null);
                String name = (String) mth.invoke(other, null);
                return iName.equals(name);
            } catch (NoSuchMethodException e) {
                // ignore - should never happen
            } catch (IllegalAccessException e) {
                // ignore - should never happen
            } catch (InvocationTargetException e) {
                // ignore - should never happen
            }
            return false;
        }
    }
equals(Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other.getClass() == this.getClass()) {
            // Ok to do a class cast to Enum here since the test above
            // guarantee both
            // classes are in the same class loader.
            return iName.equals(((Enum) other).iName);
        } else {
            // This and other are in different class loaders, we must check indirectly
            if (other.getClass().getName().equals(this.getClass().getName()) == false) {
                return false;
            }
            try {
                Method mth = other.getClass().getMethod("getName", null);
                String name = (String) mth.invoke(other, null);
                return iName.equals(name);
            } catch (NoSuchMethodException e) {
                // ignore - should never happen
            } catch (IllegalAccessException e) {
                // ignore - should never happen
            } catch (InvocationTargetException e) {
                // ignore - should never happen
            }
            return false;
        }
    }
reflectionEquals(Object lhs, Object rhs, boolean testTransients, Class reflectUpToClass) {
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
getRootCause(Throwable throwable) {
        Throwable cause = getCause(throwable);
        if (cause != null) {
            throwable = cause;
            while ((throwable = getCause(throwable)) != null) {
                cause = throwable;
            }
        }
        return cause;
    }
getThrowableCount(Throwable throwable) {
        int count = 0;
        while (throwable != null) {
            count++;
            throwable = ExceptionUtils.getCause(throwable);
        }
        return count;
    }
getThrowables(Throwable throwable) {
        List list = new ArrayList();
        while (throwable != null) {
            list.add(throwable);
            throwable = ExceptionUtils.getCause(throwable);
        }
        return (Throwable[]) list.toArray(new Throwable[list.size()]);
    }
ExceptionWithCause(Throwable cause) {
            this.cause = cause;
        }
assertLocaleLookupList(Locale locale, Locale defaultLocale, Locale[] expected) {
        List localeList = defaultLocale == null ?
                LocaleUtils.localeLookupList(locale) :
                LocaleUtils.localeLookupList(locale, defaultLocale);
        
        assertEquals(expected.length, localeList.size());
        assertEquals(Arrays.asList(expected), localeList);
        try {
            localeList.add("Unmodifiable");
            fail();
        } catch (UnsupportedOperationException ex) {}
    }
assertLanguageByCountry(String country, String[] languages) {
        List list = LocaleUtils.languagesByCountry(country);
        List list2 = LocaleUtils.languagesByCountry(country);
        assertNotNull(list);
        assertSame(list, list2);
        assertEquals(languages.length, list.size());
        //search through langauges
        for (int i = 0; i < languages.length; i++) {
            Iterator iterator = list.iterator();
            boolean found = false;
            // see if it was returned by the set
            while (iterator.hasNext()) {
                Locale locale = (Locale) iterator.next();
                // should have an en empty variant
                assertTrue(locale.getVariant() == null
                        || locale.getVariant().length() == 0);
                assertEquals(country, locale.getCountry());
                if (languages[i].equals(locale.getLanguage())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                fail("Cound not find language: " + languages[i]
                        + " for country: " + country);
            }
        }
        try {
            list.add("Unmodifiable");
            fail();
        } catch (UnsupportedOperationException ex) {}
    }
assertCountriesByLanguage(String language, String[] countries) {
        List list = LocaleUtils.countriesByLanguage(language);
        List list2 = LocaleUtils.countriesByLanguage(language);
        assertNotNull(list);
        assertSame(list, list2);
        assertEquals(countries.length, list.size());
        //search through langauges
        for (int i = 0; i < countries.length; i++) {
            Iterator iterator = list.iterator();
            boolean found = false;
            // see if it was returned by the set
            while (iterator.hasNext()) {
                Locale locale = (Locale) iterator.next();
                // should have an en empty variant
                assertTrue(locale.getVariant() == null
                        || locale.getVariant().length() == 0);
                assertEquals(language, locale.getLanguage());
                if (countries[i].equals(locale.getCountry())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                fail("Cound not find language: " + countries[i]
                        + " for country: " + language);
            }
        }
        try {
            list.add("Unmodifiable");
            fail();
        } catch (UnsupportedOperationException ex) {}
    }
clone() {
        try {
            StrTokenizer cloned = (StrTokenizer) super.clone();
            if (cloned.chars != null) {
                cloned.chars = (char[]) cloned.chars.clone();
            }
            cloned.reset();
            return cloned;

        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }
testEscapeNamedEntity() throws Exception
    {
        assertEquals("&foo;", entities.escape("\u00A1"));
        assertEquals("x&foo;", entities.escape("x\u00A1"));
        assertEquals("&foo;x", entities.escape("\u00A1x"));
        assertEquals("x&foo;x", entities.escape("x\u00A1x"));
        assertEquals("&foo;&bar;", entities.escape("\u00A1\u00A2"));
    }
testUnescapeUnknownEntity() throws Exception
    {
        assertEquals("&zzzz;", entities.unescape("&zzzz;"));
    }
readNextToken(int start, char cbuf[], StringBuffer token) {
        token.setLength(0);
        int len = chars.length;

        // Skip all leading whitespace, unless it is the
        // field delimiter or the quote character
        int ignoreLen = 0;
        int delimLen = 0;
        int quoteLen = 0;
        while (start < len &&
                (ignoreLen = ignored.isMatch(chars, start, 0, len)) >= 1 &&
                (delimLen = delim.isMatch(chars, start, 0, len)) < 1 &&
                (quoteLen = quote.isMatch(chars, start, 0, len)) < 1) {
            start += ignoreLen;
        }

        if (start >= len) {
            return start;
        } else {
            // lengths not setup
            if ((delimLen = delim.isMatch(chars, start, 0, len)) >= 1) {
                start += delimLen;
            } else if ((quoteLen = quote.isMatch(chars, start, 0, len)) >= 1) {
                start = readQuoted(start + quoteLen, cbuf, token);
            } else {
                start = readUnquoted(start, token);
            }
        }
//
//            // lengths not setup
//            if ((delimLen = delim.isMatch(chars, start)) >= 1) {
//                start += delimLen;
//            } else if ((quoteLen = quote.isMatch(chars, start)) >= 1) {
//                start = readQuoted(start + quoteLen, cbuf, token);
//            } else {
//                start = readUnquoted(start, token);
//            }
//        } else {
//            if (delimLen > 0) {
//                start += delimLen;
//            } else if (quoteLen >= 1) {
//                start = readQuoted(start + quoteLen, cbuf, token);
//            } else {
//                start = readUnquoted(start, token);
//            }
//        }

        return start;
    }
delete(int startIndex, int endIndex) {
        endIndex = validateRange(startIndex, endIndex);
        int len = endIndex - startIndex;
        if (len > 0) {
            System.arraycopy(buffer, endIndex, buffer, startIndex, size - endIndex);
            size -= len;
        }
        return this;
    }
delete(char ch) {
        for (int i = 0; i < size; i++) {
            if (buffer[i] == ch) {
                int start = i;
                while (++i < size) {
                    if (buffer[i] != ch) {
                        break;
                    }
                }
                System.arraycopy(buffer, i, buffer, start, size - i);
                size -= (i - start);
            }
        }
        return this;
    }
replace(int startIndex, int endIndex, String str) {
        endIndex = validateRange(startIndex, endIndex);
        int insertLen = str.length();
        int removeLen = endIndex - startIndex;
        int newSize = size - removeLen + insertLen;
        if (insertLen > removeLen) {
            ensureCapacity(newSize);
        }
        if (insertLen != removeLen) {
            System.arraycopy(buffer, endIndex, buffer, startIndex + insertLen, size - endIndex);
            size = newSize;
        }
        str.getChars(0, insertLen, buffer, startIndex);
        return this;
    }
replace(int startIndex, int endIndex, StrBuilder builder) {
        endIndex = validateRange(startIndex, endIndex);
        int insertLen = builder.length();
        int removeLen = endIndex - startIndex;
        if (insertLen > removeLen) {
            ensureCapacity(size - removeLen + insertLen);
        }
        if (insertLen != removeLen) {
            //shift the current characters to the right
            System.arraycopy(buffer, endIndex, buffer, startIndex + insertLen, size - endIndex);
            //adjust the size accordingly
            size += (insertLen - removeLen);
        }
        builder.getChars(0, insertLen, buffer, startIndex);
        return this;
    }
replace(int startIndex, int endIndex, StrBuilder builder) {
        endIndex = validateRange(startIndex, endIndex);
        int insertLen = builder.length();
        int removeLen = endIndex - startIndex;
        if (insertLen > removeLen) {
            ensureCapacity(size - removeLen + insertLen);
        }
        if (insertLen != removeLen) {
            //shift the current characters to the right
            System.arraycopy(buffer, endIndex, buffer, startIndex + insertLen, size - endIndex);
            //adjust the size accordingly
            size += (insertLen - removeLen);
        }
        builder.getChars(0, insertLen, buffer, startIndex);
        return this;
    }
testStartsWith(StrBuilder sb ) {
        assertFalse(sb.startsWith("a"));
        assertFalse(sb.startsWith(null));
        assertTrue(sb.startsWith(""));
        sb.append("abc");
        assertTrue(sb.startsWith("a"));
        assertTrue(sb.startsWith("ab"));
        assertTrue(sb.startsWith("abc"));
        assertFalse(sb.startsWith("cba"));
    }
testEndsWith(StrBuilder sb) {
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
doReplace(Object obj, List priorVariables) {
        if (obj == null) {
            return null;
        }

        String base = obj.toString();
        if (base.indexOf(getVariablePrefix()) < 0) {
            return obj;
        }

        // on the first call initialize priorVariables
        // and add base as the first element
        if (priorVariables == null) {
            priorVariables = new ArrayList();
            priorVariables.add(base);
        }

        int begin = -1;
        int end = -1;
        int prec = 0 - getVariableSuffix().length();
        String variable = null;
        StringBuffer result = new StringBuffer();
        Object objResult = null;
        int objLen = 0;

        while (((begin = base.indexOf(
                getVariablePrefix(), 
                prec + getVariableSuffix().length())) > -1)
            && ((end = findEndToken(base, begin)) > -1)) {
            int escBegin = escaped(base, begin);
            if (escBegin >= 0) {
                result.append(base.substring(prec + getVariableSuffix().length(), escBegin));
                unescape(result, base, escBegin, end + getVariableSuffix().length(), priorVariables);
            }

            else {
                result.append(base.substring(prec + getVariableSuffix().length(), begin));
                variable = base.substring(begin + getVariablePrefix().length(), end);

                // if we've got a loop, create a useful exception message and
                // throw
                if (priorVariables.contains(variable)) {
                    String initialBase = priorVariables.remove(0).toString();
                    priorVariables.add(variable);
                    StringBuffer priorVariableSb = new StringBuffer();

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

                objResult = resolveVariable(variable);
                if (objResult != null) {
                    objResult = doReplace(objResult, priorVariables);
                    result.append(objResult);
                    objLen = objResult.toString().length();
                } else {
                    // variable not defined - so put it back in the value
                    result.append(getVariablePrefix()).append(variable).append(getVariableSuffix());
                }

                // pop the interpolated variable off the stack
                // this maintains priorVariables correctness for
                // properties with multiple interpolations, e.g.
                // prop.name=${some.other.prop1}/blahblah/${some.other.prop2}
                priorVariables.remove(priorVariables.size() - 1);
            }

            prec = end;
        }

        result.append(base.substring(prec + getVariableSuffix().length(), base.length()));
        return (objResult != null && objLen > 0 && objLen == result.length()) ? objResult : result.toString();
    }
getCSVInstance() {
        return (StrTokenizer)CSV_TOKENIZER_PROTOTYPE.clone();
    }
getTSVInstance() {
        return (StrTokenizer)TSV_TOKENIZER_PROTOTYPE.clone();
    }
getStackFrames(String stackTrace) {
        String linebreak = SystemUtils.LINE_SEPARATOR;
        StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
        List list = new LinkedList();
        while (frames.hasMoreTokens()) {
            list.add(frames.nextToken());
        }
        return (String[]) list.toArray(new String[list.size()]);
    }
add(Object[] array, int index, Object element) {
        if (array == null) {
            if (index != 0) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Length: 0");
            }
            Object joinedArray = Array.newInstance(element != null ? element.getClass() : Object.class, 1);
            Array.set(joinedArray, 0, element);
            return (Object[]) joinedArray;
        }
        int length = array.length;
        if (index > length || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
        }
        Object result = Array.newInstance(array.getClass().getComponentType(), length + 1);
        System.arraycopy(array, 0, result, 0, index);
        Array.set(result, index, element);
        if (index < length) {
            System.arraycopy(array, index, result, index + 1, length - index);
        }
        return (Object[]) result;
    }
indexOfThrowable(Throwable throwable, Class type, int fromIndex) {
        if (throwable == null) {
            return -1;
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        Throwable[] throwables = ExceptionUtils.getThrowables(throwable);
        if (fromIndex >= throwables.length) {
            return -1;
        }
        for (int i = fromIndex; i < throwables.length; i++) {
// TODO: decide on whether to include this
//            if (type.isAssignableFrom(throwables[i].getClass())) {
            if (throwables[i].getClass().equals(type)) {
                return i;
            }
        }
        return -1;
    }
format(long millis, String format, boolean padWithZeros) {
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
format(long millis, String format) {
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
formatISO(long millis) {
        int hours, minutes, seconds, milliseconds;
        hours = (int) (millis / DateUtils.MILLIS_PER_HOUR);
        millis = millis - (hours * DateUtils.MILLIS_PER_HOUR);
        minutes = (int) (millis / DateUtils.MILLIS_PER_MINUTE);
        millis = millis - (minutes * DateUtils.MILLIS_PER_MINUTE);
        seconds = (int) (millis / DateUtils.MILLIS_PER_SECOND);
        millis = millis - (seconds * DateUtils.MILLIS_PER_SECOND);
        milliseconds = (int) millis;

        StringBuffer buf = new StringBuffer(32);
        buf.append(hours);
        buf.append(':');
        buf.append((char) (minutes / 10 + '0'));
        buf.append((char) (minutes % 10 + '0'));
        buf.append(':');
        buf.append((char) (seconds / 10 + '0'));
        buf.append((char) (seconds % 10 + '0'));
        buf.append('.');
        if (milliseconds < 10) {
            buf.append('0').append('0');
        } else if (milliseconds < 100) {
            buf.append('0');
        }
        buf.append(milliseconds);
        return buf.toString();
    }
append(Object lhs, Object rhs) {
        if (isEquals == false) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            isEquals = false;
            return this;
        }
        Class lhsClass = lhs.getClass();
        if (!lhsClass.isArray()) {
            // The simple case, not an array, just test the element
            isEquals = lhs.equals(rhs);
        } else if (lhs.getClass() != rhs.getClass()) {
            // Here when we compare different dimensions, for example: a boolean[][] to a boolean[] 
            isEquals = false;
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
        return this;
    }
append(Object[] lhs, Object[] rhs) {
        if (isEquals == false) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            isEquals = false;
            return this;
        }
        if (lhs.length != rhs.length) {
            isEquals = false;
            return this;
        }
        for (int i = 0; i < lhs.length && isEquals; ++i) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }
append(long[] lhs, long[] rhs) {
        if (isEquals == false) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            isEquals = false;
            return this;
        }
        if (lhs.length != rhs.length) {
            isEquals = false;
            return this;
        }
        for (int i = 0; i < lhs.length && isEquals; ++i) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }
append(int[] lhs, int[] rhs) {
        if (isEquals == false) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            isEquals = false;
            return this;
        }
        if (lhs.length != rhs.length) {
            isEquals = false;
            return this;
        }
        for (int i = 0; i < lhs.length && isEquals; ++i) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }
append(short[] lhs, short[] rhs) {
        if (isEquals == false) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            isEquals = false;
            return this;
        }
        if (lhs.length != rhs.length) {
            isEquals = false;
            return this;
        }
        for (int i = 0; i < lhs.length && isEquals; ++i) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }
append(char[] lhs, char[] rhs) {
        if (isEquals == false) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            isEquals = false;
            return this;
        }
        if (lhs.length != rhs.length) {
            isEquals = false;
            return this;
        }
        for (int i = 0; i < lhs.length && isEquals; ++i) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }
append(byte[] lhs, byte[] rhs) {
        if (isEquals == false) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            isEquals = false;
            return this;
        }
        if (lhs.length != rhs.length) {
            isEquals = false;
            return this;
        }
        for (int i = 0; i < lhs.length && isEquals; ++i) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }
append(double[] lhs, double[] rhs) {
        if (isEquals == false) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            isEquals = false;
            return this;
        }
        if (lhs.length != rhs.length) {
            isEquals = false;
            return this;
        }
        for (int i = 0; i < lhs.length && isEquals; ++i) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }
append(float[] lhs, float[] rhs) {
        if (isEquals == false) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            isEquals = false;
            return this;
        }
        if (lhs.length != rhs.length) {
            isEquals = false;
            return this;
        }
        for (int i = 0; i < lhs.length && isEquals; ++i) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }
append(boolean[] lhs, boolean[] rhs) {
        if (isEquals == false) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            isEquals = false;
            return this;
        }
        if (lhs.length != rhs.length) {
            isEquals = false;
            return this;
        }
        for (int i = 0; i < lhs.length && isEquals; ++i) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }
add(Fraction fraction) {
        if (fraction == null) {
            throw new IllegalArgumentException("The fraction must not be null");
        }
        if (numerator == 0) {
            return fraction;
        }
        if (fraction.numerator == 0) {
            return this;
        }     
        // Compute lcd explicitly to limit overflow
        int gcd = greatestCommonDivisor(Math.abs(fraction.denominator), Math.abs(denominator));
        int thisResidue = denominator/gcd;
        int thatResidue = fraction.denominator/gcd;
        double denominatorValue = Math.abs((double) gcd * thisResidue * thatResidue);
        double numeratorValue = (double) numerator * thatResidue + fraction.numerator * thisResidue;
        if (Math.abs(numeratorValue) > Integer.MAX_VALUE || 
            Math.abs(denominatorValue) > Integer.MAX_VALUE) {
                throw new ArithmeticException("Integer overflow");
        }
        return Fraction.getReducedFraction((int) numeratorValue, (int) denominatorValue);
    }
subtract(Fraction fraction) {
        if (fraction == null) {
            throw new IllegalArgumentException("The fraction must not be null");
        }
        return add(fraction.negate());
    }
split(String str, char separatorChar) {
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
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match) {
                    list.add(str.substring(start, i));
                    match = false;
                }
                start = ++i;
                continue;
            }
            match = true;
            i++;
        }
        if (match) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }
split(String str, String separatorChars, int max) {
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
        if (separatorChars == null) {
            // Null separator means use whitespace
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = len;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // Optimise 1 character case
            char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = len;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                match = true;
                i++;
            }
        } else {
            // standard case
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = len;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                match = true;
                i++;
            }
        }
        if (match) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }
setValue(byte value) {
        this.value = value;
    }
setValue(double value) {
        this.value = value;
    }
setValue(float value) {
        this.value = value;
    }
setValue(int value) {
        this.value = value;
    }
setValue(long value) {
        this.value = value;
    }
setValue(short value) {
        this.value = value;
    }
uncapitalize(String str) {
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
capitalize(String str) {
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
capitalizeFully(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        str = str.toLowerCase();
        return capitalize(str);
    }
stringToInt(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }
isShortClassName() {
        return super.isShortClassName();
    }
setShortClassName(boolean shortClassName) {
        super.setShortClassName(shortClassName);
    }
capitalise(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuffer(strLen)
            .append(Character.toTitleCase(str.charAt(0)))
            .append(str.substring(1))
            .toString();
    }
uncapitalise(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuffer(strLen)
            .append(Character.toLowerCase(str.charAt(0)))
            .append(str.substring(1))
            .toString();
    }
capitaliseAllWords(String str) {
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
uncapitaliseAllWords(String str) {
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
parseCVS(String dateStr, Locale locale) {
        if (dateStr == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        //Get the symbol names
        DateFormatSymbols symbols = new DateFormatSymbols(locale);

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
Enum(String name) {
        super();

        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("The Enum name must not be empty or null");
        }
        iName = name;
        Class enumClass = Enum.getEnumClass(getClass());
        Entry entry = (Entry) cEnumClasses.get(enumClass);
        if (entry == null) {
            entry = createEntry(getClass());
            cEnumClasses.put(enumClass, entry);
        }
        if (entry.map.containsKey(name)) {
            throw new IllegalArgumentException("The Enum name must be unique, '" + name + "' has already been added");
        }
        entry.map.put(name, this);
        entry.list.add(this);
        
        iHashCode = 7 + enumClass.hashCode() + 3 * name.hashCode();
        // cannot create toString here as subclasses may want to include other data
    }
getNestedString(String str, String open, String close) {
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
squeeze(String str, String[] set) {
        if (str == null) {
            return null;
        }
        CharSet chars = evaluateSet(set);
        StringBuffer buffer = new StringBuffer(str.length());
        char[] chrs = str.toCharArray();
        int sz = chrs.length;
        char lastChar = ' ';
        char ch = ' ';
        for (int i = 0; i < sz; i++) {
            ch = chrs[i];
            if (chars.contains(ch)) {
                if ((ch == lastChar) && (i != 0)) {
                    continue;
                }
            }
            buffer.append(ch);
            lastChar = ch;
        }
        return buffer.toString();
    }
testCreateFloat() {
        assertEquals("createFloat(String) failed", new Float("1234.5"), NumberUtils.createFloat("1234.5"));
        assertEquals("createFloat(null) failed", null, NumberUtils.createFloat(null));
        try {
            Float f = NumberUtils.createFloat("");
            fail("createFloat(empty) failed");
        } catch (NumberFormatException ex) {
            // empty
        }
    }
testCreateDouble() {
        assertEquals("createDouble(String) failed", new Double("1234.5"), NumberUtils.createDouble("1234.5"));
        assertEquals("createDouble(null) failed", null, NumberUtils.createDouble(null));
        try {
            Double d = NumberUtils.createDouble("");
            fail("createDouble(empty) failed");
        } catch (NumberFormatException ex) {
            // empty
        }
    }
testCreateInteger() {
        assertEquals("createInteger(String) failed", new Integer("12345"), NumberUtils.createInteger("12345"));
        assertEquals("createInteger(null) failed", null, NumberUtils.createInteger(null));
        try {
            Integer i = NumberUtils.createInteger("");
            fail("createInteger(empty) failed");
        } catch (NumberFormatException ex) {
            // empty
        }
    }
testCreateLong() {
        assertEquals("createLong(String) failed", new Long("12345"), NumberUtils.createLong("12345"));
        assertEquals("createLong(null) failed", null, NumberUtils.createLong(null));
        try {
            Long l = NumberUtils.createLong("");
            fail("createLong(empty) failed");
        } catch (NumberFormatException ex) {
            // empty
        }
    }
testCreateBigInteger() {
        assertEquals("createBigInteger(String) failed", new BigInteger("12345"), NumberUtils.createBigInteger("12345"));
        assertEquals("createBigInteger(null) failed", null, NumberUtils.createBigInteger(null));
        try {
            BigInteger i = NumberUtils.createBigInteger("");
            fail("createBigInteger(empty) failed");
        } catch (NumberFormatException ex) {
            // empty
        }
    }
testCreateBigDecimal() {
        assertEquals("createBigDecimal(String) failed", new BigDecimal("1234.5"), NumberUtils.createBigDecimal("1234.5"));
        assertEquals("createBigDecimal(null) failed", null, NumberUtils.createBigDecimal(null));
        try {
            BigDecimal d = NumberUtils.createBigDecimal("");
            fail("createBigDecimal(empty) failed");
        } catch (NumberFormatException ex) {
            // empty
        }
    }
center(String str, int size) {
        if (str == null || size <= 0) {
            return str;
        }
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        str = leftPad(str, strLen + pads / 2, ' ');
        str = rightPad(str, size, ' ');
        return str;
    }
testSplit() {
        assertEquals(null, StringUtils.split(null));
        assertEquals(null, StringUtils.split(null, '.'));
        assertEquals(null, StringUtils.split(null, "."));
        assertEquals(null, StringUtils.split(null, ".", 3));
        
        String[] res = StringUtils.split("a..b.c", '.');
        assertEquals(4, res.length);
        assertEquals("a", res[0]);
        assertEquals("", res[1]);
        assertEquals("b", res[2]);
        assertEquals("c", res[3]);

        String[] result = StringUtils.split(TEXT_LIST, SEPARATOR, 2);
        String[] expected = { "foo", "bar,baz" };
        assertEquals("split(Object[], String, int) yielded unexpected length",
                     expected.length, result.length);
        for (int i = 0; i < result.length; i++)
        {
            assertEquals("split(Object[], String, int) failed", expected[i],
                         result[i]);
        }

        result = StringUtils.split(TEXT_LIST, SEPARATOR, 0);
        expected = ARRAY_LIST;
        assertEquals("split(Object[], String, int) yielded unexpected length",
                     expected.length, result.length);
        for (int i = 0; i < result.length; i++)
        {
            assertEquals("split(Object[], String, int) failed", expected[i],
                         result[i]);
        }

        result = StringUtils.split(TEXT_LIST, SEPARATOR, -1);
        expected = ARRAY_LIST;
        assertEquals("split(Object[], String, int) yielded unexpected length",
                     expected.length, result.length);
        for (int i = 0; i < result.length; i++)
        {
            assertEquals("split(Object[], String, int) failed", expected[i],
                         result[i]);
        }

        result = StringUtils.split("one two three four five six", null, 3);
        assertEquals("split(Object[], null, int)[0] failed", "one", result[0]);
        assertEquals("split(Object[], null, int)[1] failed", "two", result[1]);
        assertEquals("split(Object[], null, int)[2] failed", "three four five six", result[2]);
    }
lastIndexOf(final Object[] array, final Object objectToFind) {
        if (array == null) {
            return -1;
        }
        return lastIndexOf(array, objectToFind, array.length - 1);
    }
escapeEntities(String str, Entities entities) {
        return entities.escape(str);
    }
escapeEntities(String str, Entities entities) {
        return entities.escape(str);
    }
unescapeEntities(String str, Entities entities) {
        return entities.unescape(str);
    }
unescapeEntities(String str, Entities entities) {
        return entities.unescape(str);
    }
setUp() {
        if (stringWithUnicode == null) {
            StringBuffer buf = new StringBuffer(STRING_LENGTH);
            for (int i = 0; i < STRING_LENGTH/5; ++i) {
                buf.append("xxxx");
                String entityValue = Entities.html40[i % Entities.html40.length][1];
                char ch = (char) Integer.parseInt(entityValue);
                buf.append(ch);
            }
            stringWithUnicode = buf.toString();
            stringWithEntities = Entities.HTML40.unescape(stringWithUnicode);
        }

    }
testArrayIntMap() throws Exception
    {
        Entities.ArrayIntMap map = new Entities.ArrayIntMap();
        map.growBy = 2;
        map.add("foo", 1);
        assertEquals(1, map.value("foo"));
        assertEquals("foo", map.name(1));
        map.add("bar", 2);
        map.add("baz", 3);
        assertEquals(3, map.value("baz"));
        assertEquals("baz", map.name(3));
    }
escapeHtml(String str) {
        StringBuffer buf = new StringBuffer(str.length() * 2);
        int i;
        for (i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            String entity = entityName(ch);
            if (entity == null) {
                if (((int) ch) > 128) {   // should this be 127 or 128?
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
unescapeHtml(String str) {
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
                    iso = entityValue(entity);
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
assertUnescapeJava(String unescaped, String original) throws IOException {
        assertEquals("unescape(String) failed",
                unescaped, StringUtils.unescape(original));

        StringPrintWriter writer = new StringPrintWriter();
        StringEscapeUtils.unescapeJava(writer, original);
        assertEquals(unescaped, writer.getString());

    }
delete(String str, String[] set) {
        CharSet chars = evaluateSet(set);
        StringBuffer buffer = new StringBuffer(str.length());
        char[] chrs = str.toCharArray();
        int sz = chrs.length;
        for(int i=0; i<sz; i++) {
            if(!chars.contains(chrs[i])) {
                buffer.append(chrs[i]);
            }
        }
        return buffer.toString();
    }
toString() {
        String shortName = Enum.getEnumClassName(getClass());
        int pos = shortName.lastIndexOf('.');
        if (pos != -1) {
            shortName = shortName.substring(pos + 1);
        }
        shortName = shortName.replace('$', '.');
        return shortName + "[" + getName() + "]";
    }
random(int count, int start, int end, boolean letters, boolean numbers, char[] set) {
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
                ch = (char)(RANDOM.nextInt(gap) + start);
            } else {
                ch = set[RANDOM.nextInt(gap) + start];
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
reflectionCompare(Object lhs, Object rhs, boolean testTransients) {
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
        Field[] fields = c1.getDeclaredFields();
        Field.setAccessible(fields, true);
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        for (int i = 0; i < fields.length && compareToBuilder.comparison == 0; ++i) {
            Field f = fields[i];
            if (testTransients || !Modifier.isTransient(f.getModifiers())) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    try {
                        compareToBuilder.append(f.get(lhs), f.get(rhs));
                    } catch (IllegalAccessException ex) {
                        //this can't happen. Would get a Security exception instead
                        //throw a runtime exception in case the impossible happens.
                        throw new InternalError("Unexpected IllegalAccessException");
                    }
                }
            }
        }
        return compareToBuilder.toComparison();
    }
reflectionEquals(Object lhs, Object rhs,
            boolean testTransients) {
        if (lhs == rhs) {
            return true;
        }
        if (lhs == null || rhs == null) {
            return false;
        }
        Class c1 = lhs.getClass();
        if (!c1.isInstance(rhs)) {
            return false;
        }
        Field[] fields = c1.getDeclaredFields();
        Field.setAccessible(fields, true);
        EqualsBuilder equalsBuilder = new EqualsBuilder();
        for (int i = 0; i < fields.length && equalsBuilder.isEquals; ++i) {
            Field f = fields[i];
            if (testTransients || !Modifier.isTransient(f.getModifiers())) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    try {
                        equalsBuilder.append(f.get(lhs), f.get(rhs));
                    } catch (IllegalAccessException e) {
                        //this can't happen. Would get a Security exception instead
                        //throw a runtime exception in case the impossible happens.
                        throw new InternalError("Unexpected IllegalAccessException");
                    }
                }
            }
        }
        return equalsBuilder.isEquals();
    }
reflectionHashCode(
            int initialNonZeroOddNumber, int multiplierNonZeroOddNumber,
            Object object, boolean testTransients) {

        if (object == null) {
            throw new IllegalArgumentException("The object to build a hash code for must not be null");
        }
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
        Field[] fields = object.getClass().getDeclaredFields();
        Field.setAccessible(fields, true);
        for (int i = 0; i < fields.length; ++i) {
            Field f = fields[i];
            if (testTransients || !Modifier.isTransient(f.getModifiers())) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    try {
                        hashCodeBuilder.append(f.get(object));
                    } catch (IllegalAccessException e) {
                        //this can't happen. Would get a Security exception instead
                        //throw a runtime exception in case the impossible happens.
                        throw new InternalError("Unexpected IllegalAccessException");
                    }
                }
            }
        }
        return hashCodeBuilder.toHashCode();
    }
getEnum(Class enumClass, String name) {
        if (enumClass == null) {
            throw new IllegalArgumentException("The Enum Class must not be null");
        }
        Entry entry = (Entry) cEnumClasses.get(enumClass.getName());
        if (entry == null) {
            return null;
        }
        return (Enum) entry.map.get(name);
    }
getEnumMap(Class enumClass) {
        if (enumClass == null) {
            throw new IllegalArgumentException("The Enum Class must not be null");
        }
        if (Enum.class.isAssignableFrom(enumClass) == false) {
            throw new IllegalArgumentException("The Class must be a subclass of Enum");
        }
        Entry entry = (Entry) cEnumClasses.get(enumClass.getName());
        if (entry == null) {
            return EMPTY_MAP;
        }
        return Collections.unmodifiableMap(entry.map);
    }
getEnumList(Class enumClass) {
        if (enumClass == null) {
            throw new IllegalArgumentException("The Enum Class must not be null");
        }
        if (Enum.class.isAssignableFrom(enumClass) == false) {
            throw new IllegalArgumentException("The Class must be a subclass of Enum");
        }
        Entry entry = (Entry) cEnumClasses.get(enumClass.getName());
        if (entry == null) {
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableList(entry.list);
    }
reflectionToString(Object object, ToStringStyle style, 
            boolean outputTransients) {
        if (object == null) {
            throw new IllegalArgumentException("The object must not be null");
        }
        if (style == null) {
            style = getDefaultStyle();
        }
        Field[] fields = object.getClass().getDeclaredFields();
        Field.setAccessible(fields, true);
        ToStringBuilder builder = new ToStringBuilder(object, style);
        for (int i = 0; i < fields.length; ++i) {
            Field f = fields[i];
            if (outputTransients || !Modifier.isTransient(f.getModifiers())) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    try {
                        builder.append(f.getName(), f.get(object));
                        
                    } catch (IllegalAccessException ex) {
                        //this can't happen. Would get a Security exception instead
                        //throw a runtime exception in case the impossible happens.
                        throw new InternalError("Unexpected IllegalAccessException");
                    }
                }
            }
        }
        return builder.toString();
    }
getAccessibleMethod(Method method) {

        // Make sure we have a method to check
        if (method == null) {
            return (null);
        }

        // If the requested method is not public we cannot call it
        if (!Modifier.isPublic(method.getModifiers())) {
            return (null);
        }

        // If the declaring class is public, we are done
        Class clazz = method.getDeclaringClass();
        if (Modifier.isPublic(clazz.getModifiers())) {
            return (method);
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
append(Object lhs, Object rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            throw new NullPointerException();
        }
        Class lhsClass = lhs.getClass();
        if (!lhsClass.isArray()) {
            //the simple case, not an array, just test the element 
            comparison = ((Comparable) lhs).compareTo(rhs);
        } else {
            //'Switch' on type of array, to dispatch to the correct handler
            // This handles multi dimensional arrays
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
                append((Object[]) lhs, (Object[]) rhs);
            }
        }
        return this;
    }
append(Object[] lhs, Object[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            throw new NullPointerException();
        }

        int length = (lhs.length < rhs.length) ? lhs.length : rhs.length;
        for (int i = 0; i < length && comparison == 0; ++i) {
            Class lhsClass = lhs[i].getClass();
            if (!lhsClass.isInstance(rhs[i])) {
                throw new ClassCastException();
            }
            append(lhs[i], rhs[i]);
        }
        if (comparison == 0 && lhs.length != rhs.length) {
            comparison = (lhs.length < rhs.length) ? -1 : +1;
        }
        return this;
    }
append(Object[] lhs, Object[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            throw new NullPointerException();
        }

        int length = (lhs.length < rhs.length) ? lhs.length : rhs.length;
        for (int i = 0; i < length && comparison == 0; ++i) {
            Class lhsClass = lhs[i].getClass();
            if (!lhsClass.isInstance(rhs[i])) {
                throw new ClassCastException();
            }
            append(lhs[i], rhs[i]);
        }
        if (comparison == 0 && lhs.length != rhs.length) {
            comparison = (lhs.length < rhs.length) ? -1 : +1;
        }
        return this;
    }
getCause(Throwable t)
    {
        Throwable cause = getCauseUsingWellKnownTypes(t);
        if (cause == null)
        {
            cause = getCauseUsingMethodName(CAUSE_METHOD_NAME, t);
        }
        return cause;
    }
getCause(Throwable t)
    {
        Throwable cause = null;

        if (t instanceof NestableException)
        {
            cause = ((NestableException) t).getCause();
        }
        else if (t instanceof NestableRuntimeException)
        {
            cause = ((NestableRuntimeException) t).getCause();
        }
        else if (t instanceof SQLException)
        {
            cause = ((SQLException) t).getNextException();
        }
        else
        {
            Method getCause = null;
            Class c = t.getClass();
            try
            {
                getCause = c.getMethod(CAUSE_METHOD_NAME, null);
            }
            catch (NoSuchMethodException ignored)
            {
            }
            catch (SecurityException ignored)
            {
            }

            if (getCause != null &&
                Throwable.class.isAssignableFrom(getCause.getReturnType()))
            {
                try
                {
                    cause = (Throwable) getCause.invoke(t, CAUSE_METHOD_PARAMS);
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
        }

        return cause;
    }

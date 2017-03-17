{
		Assert.notNull(name, "'name' must not be null");
		StringBuilder builder = new StringBuilder("form-data; name=\"");
		builder.append(name).append('\"');
		if (filename != null) {
			if(charset == null || StandardCharsets.US_ASCII.equals(charset)) {
				builder.append("; filename=\"");
				builder.append(filename).append('\"');
			}
			else {
				builder.append("; filename*=");
				builder.append(encodeHeaderFieldParam(filename, charset));
			}
		}
		set(CONTENT_DISPOSITION, builder.toString());
	}
{
		this.initializer = initializer;
		this.simpleValueDataBinder = new WebExchangeDataBinder(null);
		if (initializer != null) {
			initializer.initBinder(this.simpleValueDataBinder);
		}
	}
{
		Assert.notNull(filters, "filters cannot be null");

		for (Filter f : filters) {
			Assert.notNull(f, "filters cannot contain null values");
			this.filters.add(f);
		}
		return (T) this;
	}
{
		this.dispatchOptions = dispatchOptions;
		return (T) this;
	}
{
		Assert.notNull(exchange, "'exchange' is required.");
		this.exchange = exchange;
		this.body = new RequestBodyPublisher(exchange, dataBufferFactory);
		this.body.registerListener();
	}
{
		Assert.notNull(method, "Method must not be null");
		return method.getDeclaringClass().getName() + "." + method.getName();
	}
{
		SimpleTypeConverter converter = new SimpleTypeConverter();
		if (initializer instanceof ConfigurableWebBindingInitializer) {
			converter.setConversionService(
					((ConfigurableWebBindingInitializer) initializer).getConversionService());
		}
		else if (initializer != null) {
			WebDataBinder dataBinder = new WebDataBinder(null);
			initializer.initBinder(dataBinder);
			converter.setConversionService(dataBinder.getConversionService());
		}
		return converter;
	}
{
		ServerHttpRequest request = new MockServerHttpRequest(HttpMethod.GET, "/bar/test.html");
		ServerWebExchange exchange = new DefaultServerWebExchange(request,
				new MockServerHttpResponse(), new MockWebSessionManager());
		assertNull(this.configSource.getCorsConfiguration(exchange));
	}
{
		if (this.bootstrap == null) {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(this.eventLoopGroup).channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel channel) throws Exception {
							configureChannel(channel.config());
							ChannelPipeline pipeline = channel.pipeline();
							if (sslContext != null) {
								pipeline.addLast(sslContext.newHandler(channel.alloc()));
							}
							pipeline.addLast(new HttpClientCodec());
							pipeline.addLast(new HttpObjectAggregator(maxResponseSize));
							if (readTimeout > 0) {
								pipeline.addLast(new ReadTimeoutHandler(readTimeout,
										TimeUnit.MILLISECONDS));
							}
						}
					});
			this.bootstrap = bootstrap;
		}
		return this.bootstrap;
	}
{
		String path = exchange.getRequest().getURI().getRawPath();
		return (this.shouldUrlDecode() ? decode(exchange, path) : path);
	}
{
		Resource resource = location.createRelative(resourcePath);
		if (resource.exists() && resource.isReadable()) {
			if (checkResource(resource, location)) {
				return resource;
			}
			else if (logger.isTraceEnabled()) {
				logger.trace("Resource path=\"" + resourcePath + "\" was successfully resolved " +
						"but resource=\"" +	resource.getURL() + "\" is neither under the " +
						"current location=\"" + location.getURL() + "\" nor under any of the " +
						"allowed locations=" + Arrays.asList(getAllowedLocations()));
			}
		}
		return null;
	}
{
		Resource location = new ClassPathResource("test/", GzipResourceResolverTests.class);
		Resource jsFile = new FileSystemResource(location.createRelative("/js/foo.js").getFile());
		Resource gzJsFile = jsFile.createRelative("foo.js.gz");
		Resource fingerPrintedFile = new FileSystemResource(location.createRelative("foo-e36d2e05253c6c7085a91522ce43a0b4.css").getFile());
		Resource gzFingerPrintedFile = fingerPrintedFile.createRelative("foo-e36d2e05253c6c7085a91522ce43a0b4.css.gz");

		if (gzJsFile.getFile().createNewFile()) {
			GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(gzJsFile.getFile()));
			FileCopyUtils.copy(jsFile.getInputStream(), out);
		}

		if (gzFingerPrintedFile.getFile().createNewFile()) {
			GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(gzFingerPrintedFile.getFile()));
			FileCopyUtils.copy(fingerPrintedFile.getInputStream(), out);
		}

		assertTrue(gzJsFile.exists());
		assertTrue(gzFingerPrintedFile.exists());
	}
{
		Set<BeanDefinition> candidates = new LinkedHashSet<>();
		try {
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
					resolveBasePackage(basePackage) + "/" + this.resourcePattern;
			Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
			boolean traceEnabled = logger.isTraceEnabled();
			boolean debugEnabled = logger.isDebugEnabled();
			for (Resource resource : resources) {
				if (traceEnabled) {
					logger.trace("Scanning " + resource);
				}
				if (resource.isReadable()) {
					try {
						MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
						if (isCandidateComponent(metadataReader)) {
							ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
							sbd.setResource(resource);
							sbd.setSource(resource);
							if (isCandidateComponent(sbd)) {
								if (debugEnabled) {
									logger.debug("Identified candidate component class: " + resource);
								}
								candidates.add(sbd);
							}
							else {
								if (debugEnabled) {
									logger.debug("Ignored because not a concrete top-level class: " + resource);
								}
							}
						}
						else {
							if (traceEnabled) {
								logger.trace("Ignored because not matching any filter: " + resource);
							}
						}
					}
					catch (Throwable ex) {
						throw new BeanDefinitionStoreException(
								"Failed to read candidate component class: " + resource, ex);
					}
				}
				else {
					if (traceEnabled) {
						logger.trace("Ignored because not readable: " + resource);
					}
				}
			}
		}
		catch (IOException ex) {
			throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
		}
		return candidates;
	}
{
		Set<BeanDefinition> candidates = new LinkedHashSet<>();
		try {
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
					resolveBasePackage(basePackage) + "/" + this.resourcePattern;
			Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
			boolean traceEnabled = logger.isTraceEnabled();
			boolean debugEnabled = logger.isDebugEnabled();
			for (Resource resource : resources) {
				if (traceEnabled) {
					logger.trace("Scanning " + resource);
				}
				if (resource.isReadable()) {
					try {
						MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
						if (isCandidateComponent(metadataReader)) {
							ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
							sbd.setResource(resource);
							sbd.setSource(resource);
							if (isCandidateComponent(sbd)) {
								if (debugEnabled) {
									logger.debug("Identified candidate component class: " + resource);
								}
								candidates.add(sbd);
							}
							else {
								if (debugEnabled) {
									logger.debug("Ignored because not a concrete top-level class: " + resource);
								}
							}
						}
						else {
							if (traceEnabled) {
								logger.trace("Ignored because not matching any filter: " + resource);
							}
						}
					}
					catch (Throwable ex) {
						throw new BeanDefinitionStoreException(
								"Failed to read candidate component class: " + resource, ex);
					}
				}
				else {
					if (traceEnabled) {
						logger.trace("Ignored because not readable: " + resource);
					}
				}
			}
		}
		catch (IOException ex) {
			throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
		}
		return candidates;
	}
{
		SpringPersistenceUnitInfo scannedUnit = new SpringPersistenceUnitInfo();
		scannedUnit.setPersistenceUnitName(this.defaultPersistenceUnitName);
		scannedUnit.setExcludeUnlistedClasses(true);

		if (this.packagesToScan != null) {
			for (String pkg : this.packagesToScan) {
				try {
					String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
							ClassUtils.convertClassNameToResourcePath(pkg) + CLASS_RESOURCE_PATTERN;
					Resource[] resources = this.resourcePatternResolver.getResources(pattern);
					MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
					for (Resource resource : resources) {
						if (resource.isReadable()) {
							MetadataReader reader = readerFactory.getMetadataReader(resource);
							String className = reader.getClassMetadata().getClassName();
							if (matchesFilter(reader, readerFactory)) {
								scannedUnit.addManagedClassName(className);
								if (scannedUnit.getPersistenceUnitRootUrl() == null) {
									URL url = resource.getURL();
									if (ResourceUtils.isJarURL(url)) {
										scannedUnit.setPersistenceUnitRootUrl(ResourceUtils.extractJarFileURL(url));
									}
								}
							}
							else if (className.endsWith(PACKAGE_INFO_SUFFIX)) {
								scannedUnit.addManagedPackage(
										className.substring(0, className.length() - PACKAGE_INFO_SUFFIX.length()));
							}
						}
					}
				}
				catch (IOException ex) {
					throw new PersistenceException("Failed to scan classpath for unlisted entity classes", ex);
				}
			}
		}

		if (this.mappingResources != null) {
			for (String mappingFileName : this.mappingResources) {
				scannedUnit.addMappingFileName(mappingFileName);
			}
		}
		else {
			Resource ormXml = getOrmXmlForDefaultPersistenceUnit();
			if (ormXml != null) {
				scannedUnit.addMappingFileName(DEFAULT_ORM_XML_RESOURCE);
				if (scannedUnit.getPersistenceUnitRootUrl() == null) {
					try {
						scannedUnit.setPersistenceUnitRootUrl(
								PersistenceUnitReader.determinePersistenceUnitRootUrl(ormXml));
					}
					catch (IOException ex) {
						logger.debug("Failed to determine persistence unit root URL from orm.xml location", ex);
					}
				}
			}
		}

		return scannedUnit;
	}
{
		Assert.notNull(name, "'name' must not be null");
		StringBuilder builder = new StringBuilder("form-data; name=\"");
		builder.append(name).append('\"');
		if (filename != null) {
			builder.append("; filename=\"");
			builder.append(filename).append('\"');
		}
		set(CONTENT_DISPOSITION, builder.toString());
	}
{

		String[] candidateNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(
				this, requiredType, true, descriptor.isEager());
		Map<String, Object> result = new LinkedHashMap<>(candidateNames.length);
		for (Class<?> autowiringType : this.resolvableDependencies.keySet()) {
			if (autowiringType.isAssignableFrom(requiredType)) {
				Object autowiringValue = this.resolvableDependencies.get(autowiringType);
				autowiringValue = AutowireUtils.resolveAutowiringValue(autowiringValue, requiredType);
				if (requiredType.isInstance(autowiringValue)) {
					result.put(ObjectUtils.identityToString(autowiringValue), autowiringValue);
					break;
				}
			}
		}
		for (String candidateName : candidateNames) {
			if (!isSelfReference(beanName, candidateName) && isAutowireCandidate(candidateName, descriptor)) {
				result.put(candidateName, descriptor.resolveCandidate(candidateName, requiredType, this));
			}
		}
		if (result.isEmpty() && !indicatesMultipleBeans(requiredType)) {
			// Consider fallback matches if the first pass failed to find anything...
			DependencyDescriptor fallbackDescriptor = descriptor.forFallbackMatch();
			for (String candidateName : candidateNames) {
				if (!isSelfReference(beanName, candidateName) && isAutowireCandidate(candidateName, fallbackDescriptor)) {
					result.put(candidateName, descriptor.resolveCandidate(candidateName, requiredType, this));
				}
			}
			if (result.isEmpty()) {
				// Consider self references before as a final pass
				for (String candidateName : candidateNames) {
					if (isSelfReference(beanName, candidateName) && isAutowireCandidate(candidateName, fallbackDescriptor)) {
						result.put(candidateName, descriptor.resolveCandidate(candidateName, requiredType, this));
					}
				}
			}
		}
		return result;
	}
{
		Assert.notNull(requiredType, "Required type must not be null");
		String[] beanNames = getBeanNamesForType(requiredType);
		if (beanNames.length > 1) {
			ArrayList<String> autowireCandidates = new ArrayList<>();
			for (String beanName : beanNames) {
				if (!containsBeanDefinition(beanName) || getBeanDefinition(beanName).isAutowireCandidate()) {
					autowireCandidates.add(beanName);
				}
			}
			if (autowireCandidates.size() > 0) {
				beanNames = autowireCandidates.toArray(new String[autowireCandidates.size()]);
			}
		}
		if (beanNames.length == 1) {
			return getBean(beanNames[0], requiredType, args);
		}
		else if (beanNames.length > 1) {
			Map<String, Object> candidates = new HashMap<>();
			for (String beanName : beanNames) {
				candidates.put(beanName, getBean(beanName, requiredType, args));
			}
			String primaryCandidate = determinePrimaryCandidate(candidates, requiredType);
			if (primaryCandidate != null) {
				return getBean(primaryCandidate, requiredType, args);
			}
			String priorityCandidate = determineHighestPriorityCandidate(candidates, requiredType);
			if (priorityCandidate != null) {
				return getBean(priorityCandidate, requiredType, args);
			}
			throw new NoUniqueBeanDefinitionException(requiredType, candidates.keySet());
		}
		else if (getParentBeanFactory() != null) {
			return getParentBeanFactory().getBean(requiredType, args);
		}
		else {
			throw new NoSuchBeanDefinitionException(requiredType);
		}
	}
{
			if (this.outputStream.isReady()) {
				if (logger.isTraceEnabled()) {
					logger.trace("flush");
				}
				this.outputStream.flush();
				this.flushOnNext = false;
				return;
			}
			this.flushOnNext = true;

		}
{

		if (source == null) {
			return null;
		}
		Assert.hasLength(encoding, "Encoding must not be empty");
		byte[] bytes = encodeBytes(source.getBytes(encoding), type);
		return new String(bytes, "US-ASCII");
	}
{
		Assert.notNull(annotationType, "'annotationType' must not be null");
		if (classLoader != null) {
			try {
				this.annotationType = (Class<? extends Annotation>) classLoader.loadClass(annotationType);
			}
			catch (ClassNotFoundException ex) {
				// Annotation Class not resolvable
			}
		}
		this.displayName = annotationType;
	}
{

		AnnotationAttributes attributes =
				retrieveAnnotationAttributes(annotatedElement, annotation, classValuesAsString, nestedAnnotationsAsMap);
		postProcessAnnotationAttributes(annotatedElement, attributes, classValuesAsString, nestedAnnotationsAsMap);
		return attributes;
	}
{
		if (annotation == null) {
			return null;
		}
		if (annotation instanceof SynthesizedAnnotation) {
			return annotation;
		}

		Class<? extends Annotation> annotationType = annotation.annotationType();
		if (!isSynthesizable(annotationType)) {
			return annotation;
		}

		DefaultAnnotationAttributeExtractor attributeExtractor =
				new DefaultAnnotationAttributeExtractor(annotation, annotatedElement);
		InvocationHandler handler = new SynthesizedAnnotationInvocationHandler(attributeExtractor);

		// Can always expose Spring's SynthesizedAnnotation marker since we explicitly check for a
		// synthesizable annotation before (which needs to declare @AliasFor from the same package)
		Class<?>[] exposedInterfaces = new Class<?>[] {annotationType, SynthesizedAnnotation.class};
		return (A) Proxy.newProxyInstance(annotation.getClass().getClassLoader(), exposedInterfaces, handler);
	}
{
		Field ruleField = null;

		for (Field field : testClass.getFields()) {
			int modifiers = field.getModifiers();
			if (!Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers) &&
					SpringMethodRule.class.isAssignableFrom(field.getType())) {
				ruleField = field;
				break;
			}
		}

		if (ruleField == null) {
			throw new IllegalStateException(String.format(
					"Failed to find 'public SpringMethodRule' field in test class [%s]. " +
					"Consult the javadoc for SpringClassRule for details.", testClass.getName()));
		}

		if (!ruleField.isAnnotationPresent(Rule.class)) {
			throw new IllegalStateException(String.format(
					"SpringMethodRule field [%s] must be annotated with JUnit's @Rule annotation. " +
					"Consult the javadoc for SpringClassRule for details.", ruleField));
		}
	}
{
		notNull(type, "Type to check against must not be null");
		if (!type.isInstance(obj)) {
			throw new IllegalArgumentException(
					(StringUtils.hasLength(message) ? message + " " : "") +
					"Object of class [" + (obj != null ? obj.getClass().getName() : "null") +
					"] must be an instance of " + type);
		}
	}
{
		notNull(superType, "Type to check against must not be null");
		if (subType == null || !superType.isAssignableFrom(subType)) {
			throw new IllegalArgumentException((StringUtils.hasLength(message) ? message + " " : "") +
					subType + " is not assignable to " + superType);
		}
	}
{
		callBack.runTestMethod(testResult);

		Throwable testResultException = testResult.getThrowable();
		if (testResultException instanceof InvocationTargetException) {
			testResultException = ((InvocationTargetException) testResultException).getCause();
		}
		this.testException = testResultException;
	}
{
		Assert.notNull(testInstance, "Test instance must not be null");
		if (logger.isTraceEnabled()) {
			logger.trace("beforeTestMethod(): instance [" + testInstance + "], method [" + testMethod + "]");
		}
		getTestContext().updateState(testInstance, testMethod, null);

		for (TestExecutionListener testExecutionListener : getTestExecutionListeners()) {
			try {
				testExecutionListener.beforeTestMethod(getTestContext());
			}
			catch (Throwable ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Caught exception while allowing TestExecutionListener [" + testExecutionListener +
							"] to process 'before' execution of test method [" + testMethod + "] for test instance [" +
							testInstance + "]", ex);
				}
				ReflectionUtils.rethrowException(ex);
			}
		}
	}
{
		Assert.notNull(testInstance, "Test instance must not be null");
		if (logger.isTraceEnabled()) {
			logger.trace("afterTestMethod(): instance [" + testInstance + "], method [" + testMethod +
					"], exception [" + exception + "]");
		}
		getTestContext().updateState(testInstance, testMethod, exception);

		Throwable afterTestMethodException = null;
		// Traverse the TestExecutionListeners in reverse order to ensure proper
		// "wrapper"-style execution of listeners.
		for (TestExecutionListener testExecutionListener : getReversedTestExecutionListeners()) {
			try {
				testExecutionListener.afterTestMethod(getTestContext());
			}
			catch (Throwable ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Caught exception while allowing TestExecutionListener [" + testExecutionListener +
							"] to process 'after' execution for test: method [" + testMethod + "], instance [" +
							testInstance + "], exception [" + exception + "]", ex);
				}
				if (afterTestMethodException == null) {
					afterTestMethodException = ex;
				}
			}
		}
		if (afterTestMethodException != null) {
			ReflectionUtils.rethrowException(afterTestMethodException);
		}
	}
{
		Assert.notNull(testInstance, "Test instance must not be null");
		if (logger.isTraceEnabled()) {
			logger.trace("beforeTestMethod(): instance [" + testInstance + "], method [" + testMethod + "]");
		}
		getTestContext().updateState(testInstance, testMethod, null);

		for (TestExecutionListener testExecutionListener : getTestExecutionListeners()) {
			try {
				testExecutionListener.beforeTestMethod(getTestContext());
			}
			catch (Throwable ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Caught exception while allowing TestExecutionListener [" + testExecutionListener +
							"] to process 'before' execution of test method [" + testMethod + "] for test instance [" +
							testInstance + "]", ex);
				}
				ReflectionUtils.rethrowException(ex);
			}
		}
	}
{
		URI url = new URI(requestUrl);
		RequestEntity<List<Person>> request = RequestEntity.post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Arrays.asList(new Person("Robert"), new Person("Marie")));
		ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, this.wac.getBean(TestRestController.class).persons.size());
	}
{
		URI url = new URI(requestUrl);
		RequestEntity<List<Person>> request = RequestEntity.post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Arrays.asList(new Person("Robert"), new Person("Marie")));
		ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, this.wac.getBean(TestRestController.class).persons.size());
	}
{
		URI url = new URI(requestUrl);
		RequestEntity<List<Person>> request = RequestEntity.post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Arrays.asList(new Person("Robert"), new Person("Marie")));
		ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, this.wac.getBean(TestRestController.class).persons.size());
	}
{
		URI url = new URI(requestUrl);
		People people = new People();
		people.getPerson().add(new Person("Robert"));
		people.getPerson().add(new Person("Marie"));
		RequestEntity<People> request =
				RequestEntity.post(url).contentType(MediaType.APPLICATION_XML)
						.body(people);
		ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, this.wac.getBean(TestRestController.class).persons.size());
	}
{
		URI url = new URI(requestUrl);
		People people = new People();
		people.getPerson().add(new Person("Robert"));
		people.getPerson().add(new Person("Marie"));
		RequestEntity<People> request =
				RequestEntity.post(url).contentType(MediaType.APPLICATION_XML)
						.body(people);
		ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, this.wac.getBean(TestRestController.class).persons.size());
	}
{
		URI url = new URI(requestUrl);
		People people = new People();
		people.getPerson().add(new Person("Robert"));
		people.getPerson().add(new Person("Marie"));
		RequestEntity<People> request =
				RequestEntity.post(url).contentType(MediaType.APPLICATION_XML)
						.body(people);
		ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, this.wac.getBean(TestRestController.class).persons.size());
	}
{
		if (methodOrConstructor instanceof Method) {
			return new MethodParameter((Method) methodOrConstructor, parameterIndex);
		}
		else if (methodOrConstructor instanceof Constructor) {
			return new MethodParameter((Constructor<?>) methodOrConstructor, parameterIndex);
		}
		else {
			throw new IllegalArgumentException(
					"Given object [" + methodOrConstructor + "] is neither a Method nor a Constructor");
		}
	}
{
		Assert.state(this.targetConnectionFactory != null, "'targetConnectionFactory' is required");
		if (StringUtils.hasLength(username)) {
			return this.targetConnectionFactory.createConnection(username, password);
		}
		else {
			return this.targetConnectionFactory.createConnection();
		}
	}
{
		return this.payloadConverter.toMessage(payload, session);
	}
{
		return null;
	}
{
		return null;
	}
{
		return this.methodParam;
	}
{
		if (servletPartClass != null) {
			// Servlet 3.0 available ..
			return new StandardMultipartHttpServletRequest(request);
		}
		throw new MultipartException("Expected MultipartHttpServletRequest: is a MultipartResolver configured?");
	}
{
		if (this.propertySources != null) {
			for (PropertySource<?> propertySource : this.propertySources) {
				if (logger.isTraceEnabled()) {
					logger.trace(String.format("Searching for key '%s' in [%s]", key, propertySource.getName()));
				}
				Object value = propertySource.getProperty(key);
				if (value != null) {
					if (resolveNestedPlaceholders && value instanceof String) {
						value = resolveNestedPlaceholders((String) value);
					}
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Found key '%s' in [%s] with type [%s] and value '%s'",
								key, propertySource.getName(), value.getClass().getSimpleName(), value));
					}
					return this.conversionService.convert(value, targetValueType);
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Could not find key '%s' in any property source", key));
		}
		return null;
	}
{
		request.setPathInfo(null);
	}
{
		Class<? extends Exception> exceptionType = exception.getClass();
		Method method = this.exceptionLookupCache.get(exceptionType);
		if (method == null) {
			method = getMappedMethod(exceptionType);
			this.exceptionLookupCache.put(exceptionType, method != null ? method : NO_METHOD_FOUND);
		}
		return method != NO_METHOD_FOUND ? method : null;
	}
{
		Flux<DataBuffer> inputFlux = Flux.from(inputStream);
		if (this.splitOnNewline) {
			inputFlux = inputFlux.flatMap(StringDecoder::splitOnNewline);
		}
		Charset charset = getCharset(mimeType);
		return inputFlux.map(dataBuffer -> {
			CharBuffer charBuffer = charset.decode(dataBuffer.asByteBuffer());
			DataBufferUtils.release(dataBuffer);
			return charBuffer.toString();
		});
	}
{

		Assert.notNull(inputStream, "'inputStream' must not be null");
		Assert.notNull(elementType, "'elementType' must not be null");
		TypeFactory typeFactory = this.mapper.getTypeFactory();
		JavaType javaType = typeFactory.constructType(elementType.getType());
		ObjectReader reader = this.mapper.readerFor(javaType);

		return this.fluxPreProcessor.decode(inputStream, elementType, mimeType, hints)
				.map(dataBuffer -> {
					try {
						Object value = reader.readValue(dataBuffer.asInputStream());
						DataBufferUtils.release(dataBuffer);
						return value;
					}
					catch (IOException e) {
						return Flux.error(new CodecException("Error while reading the data", e));
					}
		});
	}
{
		if (this.decoder == null) {
			return Flux.error(new IllegalStateException("No decoder set"));
		}
		MediaType contentType = inputMessage.getHeaders().getContentType();
		if (contentType == null) {
			contentType = MediaType.APPLICATION_OCTET_STREAM;
		}
		return this.decoder.decode(inputMessage.getBody(), type, contentType);
	}
{
		containerFactory.setMessageConverter(new UpperCaseMessageConverter());

		MethodJmsListenerEndpoint endpoint = createDefaultMethodJmsEndpoint(
				listener.getClass(), "handleIt", String.class, String.class);
		Message message = new StubTextMessage("foo-bar");
		message.setStringProperty("my-header", "my-value");

		invokeListener(endpoint, message);
		assertListenerMethodInvocation("handleIt");
	}
{
		Object handler = result.getHandler();
		if (handler instanceof HandlerMethod) {
			MethodParameter returnType = ((HandlerMethod) handler).getReturnType();
			Class<?> containingClass = returnType.getContainingClass();
			return (AnnotationUtils.findAnnotation(containingClass, ResponseBody.class) != null ||
					returnType.getMethodAnnotation(ResponseBody.class) != null);
		}
		return false;
	}
{

		ConversionService conversionService = new DefaultConversionService();
		ViewResolutionResultHandler handler = new ViewResolutionResultHandler(resolvers, conversionService);
		handler.setDefaultViews(defaultViews);

		Method method = TestController.class.getMethod(methodName);
		HandlerMethod handlerMethod = new HandlerMethod(new TestController(), method);
		ResolvableType type = ResolvableType.forMethodReturnType(method);
		HandlerResult handlerResult = new HandlerResult(handlerMethod, value, type, this.model);

		this.request.setUri(new URI(path));
		WebSessionManager sessionManager = new DefaultWebSessionManager();
		ServerWebExchange exchange = new DefaultServerWebExchange(this.request, this.response, sessionManager);

		Mono<Void> mono = handler.handleResult(exchange, handlerResult);

		return TestSubscriber.subscribe(mono).await(Duration.ofSeconds(1));
	}
{
		ResponseBodyResultHandler handler = createHandler(new StringEncoder());
		TestController controller = new TestController();

		HandlerMethod hm = new HandlerMethod(controller, TestController.class.getMethod("notAnnotated"));
		ResolvableType type = ResolvableType.forMethodParameter(hm.getReturnType());
		assertFalse(handler.supports(new HandlerResult(hm, null, type, new ExtendedModelMap())));

		hm = new HandlerMethod(controller, TestController.class.getMethod("publisherString"));
		type = ResolvableType.forMethodParameter(hm.getReturnType());
		assertTrue(handler.supports(new HandlerResult(hm, null, type, new ExtendedModelMap())));

		hm = new HandlerMethod(controller, TestController.class.getMethod("publisherVoid"));
		type = ResolvableType.forMethodParameter(hm.getReturnType());
		assertTrue(handler.supports(new HandlerResult(hm, null, type, new ExtendedModelMap())));
	}
{
		// Rely on default serialization, just initialize state after deserialization.
		ois.defaultReadObject();

		// Initialize transient fields.
		this.adviceMonitor = new Object();
	}
{
		if (o1 == o2) {
			return true;
		}
		if (o1 == null || o2 == null) {
			return false;
		}
		if (o1.equals(o2)) {
			return true;
		}
		if (o1.getClass().isArray() && o2.getClass().isArray()) {
			if (o1 instanceof Object[] && o2 instanceof Object[]) {
				return Arrays.equals((Object[]) o1, (Object[]) o2);
			}
			if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
				return Arrays.equals((boolean[]) o1, (boolean[]) o2);
			}
			if (o1 instanceof byte[] && o2 instanceof byte[]) {
				return Arrays.equals((byte[]) o1, (byte[]) o2);
			}
			if (o1 instanceof char[] && o2 instanceof char[]) {
				return Arrays.equals((char[]) o1, (char[]) o2);
			}
			if (o1 instanceof double[] && o2 instanceof double[]) {
				return Arrays.equals((double[]) o1, (double[]) o2);
			}
			if (o1 instanceof float[] && o2 instanceof float[]) {
				return Arrays.equals((float[]) o1, (float[]) o2);
			}
			if (o1 instanceof int[] && o2 instanceof int[]) {
				return Arrays.equals((int[]) o1, (int[]) o2);
			}
			if (o1 instanceof long[] && o2 instanceof long[]) {
				return Arrays.equals((long[]) o1, (long[]) o2);
			}
			if (o1 instanceof short[] && o2 instanceof short[]) {
				return Arrays.equals((short[]) o1, (short[]) o2);
			}
		}
		return false;
	}
{

		ResolvableType type = ResolvableType.forMethodParameter(parameter);
		ResolvableType elementType = type.hasGenerics() ? type.getGeneric(0) : type;

		MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
		if (mediaType == null) {
			mediaType = MediaType.APPLICATION_OCTET_STREAM;
		}

		Flux<?> elementFlux = exchange.getRequest().getBody();

		HttpMessageConverter<?> converter = getMessageConverter(elementType, mediaType);
		if (converter != null) {
			elementFlux = converter.read(elementType, exchange.getRequest());
		}

		if (type.getRawClass() == Flux.class) {
			return Mono.just(elementFlux);
		}
		else if (type.getRawClass() == Mono.class) {
			return Mono.just(Mono.from(elementFlux));
		}
		else if (this.conversionService.canConvert(Publisher.class, type.getRawClass())) {
			Object target = this.conversionService.convert(elementFlux, type.getRawClass());
			return Mono.just(target);
		}

		// TODO Currently manage only "Foo" parameter, not "List<Foo>" parameters, Stéphane is going to add toIterable/toIterator to Flux to support that use case
		return elementFlux.next().map(o -> o);
	}
{
		if (ObjectUtils.isEmpty(this.argumentResolvers)) {

//			List<HttpMessageConverter<?>> converters = Arrays.asList(
//					new CodecHttpMessageConverter<ByteBuffer>(new ByteBufferEncoder(), new ByteBufferDecoder()),
//					new CodecHttpMessageConverter<String>(new StringEncoder(), new StringDecoder()),
//					new CodecHttpMessageConverter<Object>(new Jaxb2Encoder(), new Jaxb2Decoder()),
//					new CodecHttpMessageConverter<Object>(new JacksonJsonEncoder(),
//							new JacksonJsonDecoder(new JsonObjectDecoder())));

			// Annotation-based argument resolution
			ConversionService cs = getConversionService();
			this.argumentResolvers.add(new RequestParamMethodArgumentResolver(cs, getBeanFactory(), false));
			this.argumentResolvers.add(new RequestParamMapMethodArgumentResolver());
			this.argumentResolvers.add(new PathVariableMethodArgumentResolver(cs, getBeanFactory()));
			this.argumentResolvers.add(new PathVariableMapMethodArgumentResolver());
			this.argumentResolvers.add(new RequestBodyArgumentResolver(getMessageConverters(), cs));
			this.argumentResolvers.add(new RequestHeaderMethodArgumentResolver(cs, getBeanFactory()));
			this.argumentResolvers.add(new RequestHeaderMapMethodArgumentResolver());
			this.argumentResolvers.add(new CookieValueMethodArgumentResolver(cs, getBeanFactory()));
			this.argumentResolvers.add(new ExpressionValueMethodArgumentResolver(cs, getBeanFactory()));
			this.argumentResolvers.add(new SessionAttributeMethodArgumentResolver(cs, getBeanFactory()));
			this.argumentResolvers.add(new RequestAttributeMethodArgumentResolver(cs , getBeanFactory()));

			// Type-based argument resolution
			this.argumentResolvers.add(new ModelArgumentResolver());

			// Catch-all
			this.argumentResolvers.add(new RequestParamMethodArgumentResolver(cs, getBeanFactory(), true));
		}
	}
{

		List<ViewResolver> resolverList = Arrays.asList(resolvers);
		ConversionService conversionService = new DefaultConversionService();
		HandlerResultHandler handler = new ViewResolutionResultHandler(resolverList, conversionService);
		Method method = TestController.class.getMethod(methodName);
		HandlerMethod handlerMethod = new HandlerMethod(new TestController(), method);
		ResolvableType type = ResolvableType.forMethodReturnType(method);
		HandlerResult handlerResult = new HandlerResult(handlerMethod, value, type, this.model);

		ServerHttpRequest request = new MockServerHttpRequest(HttpMethod.GET, new URI(path));
		this.response = new MockServerHttpResponse();
		WebSessionManager sessionManager = new DefaultWebSessionManager();
		ServerWebExchange exchange = new DefaultServerWebExchange(request, this.response, sessionManager);

		Mono<Void> mono = handler.handleResult(exchange, handlerResult);

		TestSubscriber<Void> subscriber = new TestSubscriber<>();
		return subscriber.bindTo(mono).await(Duration.ofSeconds(1));
	}
{
		Date expires = null;
		if (cookie.getMaxAge() > -1) {
			expires = new Date(System.currentTimeMillis() + cookie.getMaxAge() * 1000);
		}
		BasicClientCookie result = new BasicClientCookie(cookie.getName(), cookie.getValue());
		result.setDomain(cookie.getDomain());
		result.setComment(cookie.getComment());
		result.setExpiryDate(expires);
		result.setPath(cookie.getPath());
		result.setSecure(cookie.getSecure());
		if(cookie.isHttpOnly()) {
			result.setAttribute("httponly", "true");
		}
		return new com.gargoylesoftware.htmlunit.util.Cookie(result).toString();
	}
{
		return client.getWebConnection().getResponse(new WebRequest(new URL(url)));
	}
{
		if (allowNullValues != this.allowNullValues) {
			this.allowNullValues = allowNullValues;
			// Need to recreate all Cache instances with the new null-value configuration...
			for (Map.Entry<String, Cache> entry : this.cacheMap.entrySet()) {
				entry.setValue(createConcurrentMapCache(entry.getKey()));
			}
		}
	}
{
		// Recursively process any member (nested) classes first
		processMemberClasses(configClass, sourceClass);

		// Process any @PropertySource annotations
		for (AnnotationAttributes propertySource : AnnotationConfigUtils.attributesForRepeatable(
				sourceClass.getMetadata(), PropertySources.class, org.springframework.context.annotation.PropertySource.class)) {
			if (this.environment instanceof ConfigurableEnvironment) {
				processPropertySource(propertySource);
			}
			else {
				logger.warn("Ignoring @PropertySource annotation on [" + sourceClass.getMetadata().getClassName() +
						"]. Reason: Environment must implement ConfigurableEnvironment");
			}
		}

		// Process any @ComponentScan annotations
		Set<AnnotationAttributes> componentScans = AnnotationConfigUtils.attributesForRepeatable(
				sourceClass.getMetadata(), ComponentScans.class, ComponentScan.class);
		if (!componentScans.isEmpty() && !this.conditionEvaluator.shouldSkip(sourceClass.getMetadata(), ConfigurationPhase.REGISTER_BEAN)) {
			for (AnnotationAttributes componentScan : componentScans) {
				// The config class is annotated with @ComponentScan -> perform the scan immediately
				Set<BeanDefinitionHolder> scannedBeanDefinitions =
						this.componentScanParser.parse(componentScan, sourceClass.getMetadata().getClassName());
				// Check the set of scanned definitions for any further config classes and parse recursively if necessary
				for (BeanDefinitionHolder holder : scannedBeanDefinitions) {
					if (ConfigurationClassUtils.checkConfigurationClassCandidate(holder.getBeanDefinition(), this.metadataReaderFactory)) {
						parse(holder.getBeanDefinition().getBeanClassName(), holder.getBeanName());
					}
				}
			}
		}

		// Process any @Import annotations
		processImports(configClass, sourceClass, getImports(sourceClass), true);

		// Process any @ImportResource annotations
		if (sourceClass.getMetadata().isAnnotated(ImportResource.class.getName())) {
			AnnotationAttributes importResource = AnnotationConfigUtils.attributesFor(sourceClass.getMetadata(), ImportResource.class);
			String[] resources = importResource.getAliasedStringArray("locations", ImportResource.class, sourceClass);
			Class<? extends BeanDefinitionReader> readerClass = importResource.getClass("reader");
			for (String resource : resources) {
				String resolvedResource = this.environment.resolveRequiredPlaceholders(resource);
				configClass.addImportedResource(resolvedResource, readerClass);
			}
		}

		// Process individual @Bean methods
		Set<MethodMetadata> beanMethods = sourceClass.getMetadata().getAnnotatedMethods(Bean.class.getName());
		for (MethodMetadata methodMetadata : beanMethods) {
			configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
		}

		// Process default methods on interfaces
		for (SourceClass ifc : sourceClass.getInterfaces()) {
			beanMethods = ifc.getMetadata().getAnnotatedMethods(Bean.class.getName());
			for (MethodMetadata methodMetadata : beanMethods) {
				if (!methodMetadata.isAbstract()) {
					// A default method or other concrete method on a Java 8+ interface...
					configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
				}
			}
		}

		// Process superclass, if any
		if (sourceClass.getMetadata().hasSuperClass()) {
			String superclass = sourceClass.getMetadata().getSuperClassName();
			if (!superclass.startsWith("java") && !this.knownSuperclasses.containsKey(superclass)) {
				this.knownSuperclasses.put(superclass, configClass);
				// Superclass found, return its annotation metadata and recurse
				return sourceClass.getSuperClass();
			}
		}

		// No superclass -> processing is complete
		return null;
	}
{
		ServerHttpRequest request = new MockServerHttpRequest(HttpMethod.GET, new URI("/path"));
		this.response = new MockServerHttpResponse();
		WebSessionManager sessionManager = new DefaultWebSessionManager();
		this.exchange = new DefaultServerWebExchange(request, this.response, sessionManager);
		this.model = new ExtendedModelMap().addAttribute("id", "123");
		this.conversionService = new DefaultConversionService();
		this.conversionService.addConverter(new ReactiveStreamsToRxJava1Converter());
	}
{
		long now = System.currentTimeMillis();

		if (this.taskScheduler == null) {
			this.localExecutor = Executors.newSingleThreadScheduledExecutor();
			this.taskScheduler = new ConcurrentTaskScheduler(this.localExecutor);
		}
		if (this.triggerTasks != null) {
			for (TriggerTask task : this.triggerTasks) {
				this.scheduledFutures.add(this.taskScheduler.schedule(
						task.getRunnable(), task.getTrigger()));
			}
		}
		if (this.cronTasks != null) {
			for (CronTask task : this.cronTasks) {
				this.scheduledFutures.add(this.taskScheduler.schedule(
						task.getRunnable(), task.getTrigger()));
			}
		}
		if (this.fixedRateTasks != null) {
			for (IntervalTask task : this.fixedRateTasks) {
				if (task.getInitialDelay() > 0) {
					Date startTime = new Date(now + task.getInitialDelay());
					this.scheduledFutures.add(this.taskScheduler.scheduleAtFixedRate(
							task.getRunnable(), startTime, task.getInterval()));
				}
				else {
					this.scheduledFutures.add(this.taskScheduler.scheduleAtFixedRate(
							task.getRunnable(), task.getInterval()));
				}
			}
		}
		if (this.fixedDelayTasks != null) {
			for (IntervalTask task : this.fixedDelayTasks) {
				if (task.getInitialDelay() > 0) {
					Date startTime = new Date(now + task.getInitialDelay());
					this.scheduledFutures.add(this.taskScheduler.scheduleWithFixedDelay(
							task.getRunnable(), startTime, task.getInterval()));
				}
				else {
					this.scheduledFutures.add(this.taskScheduler.scheduleWithFixedDelay(
							task.getRunnable(), task.getInterval()));
				}
			}
		}
	}
{
		long now = System.currentTimeMillis();

		if (this.taskScheduler == null) {
			this.localExecutor = Executors.newSingleThreadScheduledExecutor();
			this.taskScheduler = new ConcurrentTaskScheduler(this.localExecutor);
		}
		if (this.triggerTasks != null) {
			for (TriggerTask task : this.triggerTasks) {
				this.scheduledFutures.add(this.taskScheduler.schedule(
						task.getRunnable(), task.getTrigger()));
			}
		}
		if (this.cronTasks != null) {
			for (CronTask task : this.cronTasks) {
				this.scheduledFutures.add(this.taskScheduler.schedule(
						task.getRunnable(), task.getTrigger()));
			}
		}
		if (this.fixedRateTasks != null) {
			for (IntervalTask task : this.fixedRateTasks) {
				if (task.getInitialDelay() > 0) {
					Date startTime = new Date(now + task.getInitialDelay());
					this.scheduledFutures.add(this.taskScheduler.scheduleAtFixedRate(
							task.getRunnable(), startTime, task.getInterval()));
				}
				else {
					this.scheduledFutures.add(this.taskScheduler.scheduleAtFixedRate(
							task.getRunnable(), task.getInterval()));
				}
			}
		}
		if (this.fixedDelayTasks != null) {
			for (IntervalTask task : this.fixedDelayTasks) {
				if (task.getInitialDelay() > 0) {
					Date startTime = new Date(now + task.getInitialDelay());
					this.scheduledFutures.add(this.taskScheduler.scheduleWithFixedDelay(
							task.getRunnable(), startTime, task.getInterval()));
				}
				else {
					this.scheduledFutures.add(this.taskScheduler.scheduleWithFixedDelay(
							task.getRunnable(), task.getInterval()));
				}
			}
		}
	}
{
		if (this.materialized == null) {
			synchronized (this) {
				if (this.materialized == null) {
					this.materialized = this.maaif.getAspectInstance();
				}
			}
		}
		return this.materialized;
	}
{
		if (this.materialized == null) {
			synchronized (this) {
				if (this.materialized == null) {
					this.materialized = this.maaif.getAspectInstance();
				}
			}
		}
		return this.materialized;
	}
{
		if (this.materialized == null) {
			synchronized (this) {
				if (this.materialized == null) {
					this.materialized = this.maaif.getAspectInstance();
				}
			}
		}
		return this.materialized;
	}
{
		if (this.materialized == null) {
			synchronized (this) {
				if (this.materialized == null) {
					this.materialized = this.maaif.getAspectInstance();
				}
			}
		}
		return this.materialized;
	}
{
		return this.baseUrl;
	}
{
		WebHandler webHandler = this.targetHandler;
		if (!this.exceptionHandlers.isEmpty()) {
			WebExceptionHandler[] array = new WebExceptionHandler[this.exceptionHandlers.size()];
			webHandler = new ExceptionHandlingWebHandler(webHandler,  this.exceptionHandlers.toArray(array));
		}
		if (!this.filters.isEmpty()) {
			WebFilter[] array = new WebFilter[this.filters.size()];
			webHandler = new FilteringWebHandler(webHandler, this.filters.toArray(array));
		}
		return webHandler;
	}
{

		Optional<Object> value = result.getReturnValue();
		if (!value.isPresent()) {
			return Mono.empty();
		}

		Publisher<?> publisher;
		ResolvableType elementType;
		ResolvableType returnType = result.getReturnValueType();
		if (this.conversionService.canConvert(returnType.getRawClass(), Publisher.class)) {
			publisher = this.conversionService.convert(value.get(), Publisher.class);
			elementType = returnType.getGeneric(0);
			if (Void.class.equals(elementType.getRawClass())) {
				return Mono.from((Publisher<Void>)publisher);
			}
		}
		else {
			publisher = Mono.just(value.get());
			elementType = returnType;
		}

		List<MediaType> requestedMediaTypes = getAcceptableMediaTypes(exchange.getRequest());
		List<MediaType> producibleMediaTypes = getProducibleMediaTypes(elementType);

		if (producibleMediaTypes.isEmpty()) {
			producibleMediaTypes.add(MediaType.ALL);
		}

		Set<MediaType> compatibleMediaTypes = new LinkedHashSet<>();
		for (MediaType requestedType : requestedMediaTypes) {
			for (MediaType producibleType : producibleMediaTypes) {
				if (requestedType.isCompatibleWith(producibleType)) {
					compatibleMediaTypes.add(getMostSpecificMediaType(requestedType, producibleType));
				}
			}
		}
		if (compatibleMediaTypes.isEmpty()) {
			return Mono.error(new NotAcceptableStatusException(producibleMediaTypes));
		}

		List<MediaType> mediaTypes = new ArrayList<>(compatibleMediaTypes);
		MediaType.sortBySpecificityAndQuality(mediaTypes);

		MediaType selectedMediaType = null;
		for (MediaType mediaType : mediaTypes) {
			if (mediaType.isConcrete()) {
				selectedMediaType = mediaType;
				break;
			}
			else if (mediaType.equals(MediaType.ALL) || mediaType.equals(MEDIA_TYPE_APPLICATION)) {
				selectedMediaType = MediaType.APPLICATION_OCTET_STREAM;
				break;
			}
		}

		if (selectedMediaType != null) {
			Encoder<?> encoder = resolveEncoder(elementType, selectedMediaType);
			if (encoder != null) {
				ServerHttpResponse response = exchange.getResponse();
				response.getHeaders().setContentType(selectedMediaType);
				DataBufferAllocator allocator = response.allocator();
				return response.setBody(
						encoder.encode((Publisher) publisher, allocator, elementType,
								selectedMediaType));
			}
		}

		return Mono.error(new NotAcceptableStatusException(this.allMediaTypes));
	}
{
		for (Entry<String, List<String>> entry : uriComponents.getQueryParams().entrySet()) {
			String name = entry.getKey();
			for (String value : entry.getValue()) {
				try {
					value = (value != null ? URLDecoder.decode(value, "UTF-8") : "");
					request.addParameter(name, value);
				}
				catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}
		}
		for (NameValuePair param : this.webRequest.getRequestParameters()) {
			request.addParameter(param.getName(), param.getValue());
		}
	}
{
		String charSet = getParameter(PARAM_CHARSET);
		return (charSet != null ? Charset.forName(unquote(charSet)) : null);
	}
{
		if (targetClass == null) {
			return false;
		}
		JavaType javaType = this.objectMapper.constructType(targetClass);
		if (!logger.isWarnEnabled()) {
			return (this.objectMapper.canDeserialize(javaType) && supportsMimeType(message.getHeaders()));
		}
		AtomicReference<Throwable> causeRef = new AtomicReference<Throwable>();
		if (this.objectMapper.canDeserialize(javaType, causeRef) && supportsMimeType(message.getHeaders())) {
			return true;
		}
		Throwable cause = causeRef.get();
		if (cause != null) {
			String msg = "Failed to evaluate deserialization for type " + javaType;
			if (logger.isDebugEnabled()) {
				logger.warn(msg, cause);
			}
			else {
				logger.warn(msg + ": " + cause);
			}
		}
		return false;
	}
{
		JavaType javaType = getJavaType(type, contextClass);
		if (!canRead(mediaType)) {
			return false;
		}
		if (!logger.isWarnEnabled()) {
			return this.objectMapper.canDeserialize(javaType);
		}
		AtomicReference<Throwable> causeRef = new AtomicReference<Throwable>();
		if (this.objectMapper.canDeserialize(javaType, causeRef)) {
			return true;
		}
		Throwable cause = causeRef.get();
		if (cause != null) {
			String msg = "Failed to evaluate deserialization for type " + javaType;
			if (logger.isDebugEnabled()) {
				logger.warn(msg, cause);
			}
			else {
				logger.warn(msg + ": " + cause);
			}
		}
		return false;
	}
{
		try {
			return getFirstDate(EXPIRES);
		}
		catch (IllegalArgumentException ex) {
			return -1;
		}
	}
{
		Assert.notNull(context, "context must not be null");
		Assert.notNull(locations, "locations must not be null");
		try {
			ConfigurableEnvironment environment = context.getEnvironment();
			for (String location : locations) {
				String resolvedLocation = environment.resolveRequiredPlaceholders(location);
				Resource resource = context.getResource(resolvedLocation);
				environment.getPropertySources().addFirst(new ResourcePropertySource(resource));
			}
		}
		catch (IOException e) {
			throw new IllegalStateException("Failed to add PropertySource to Environment", e);
		}
	}
{

		ResolvableType resolvableType = ResolvableType.forClass(sourceClass);
		//noinspection unchecked
		return webResponse -> (Mono<T>) webResponse.getClientResponse()
				.flatMap(resp -> decodeResponseBody(resp, resolvableType, webResponse.getMessageDecoders()))
				.next();
	}
{

		ResolvableType resolvableType = ResolvableType.forClass(sourceClass);
		return webResponse -> webResponse.getClientResponse()
				.flatMap(resp -> decodeResponseBody(resp, resolvableType, webResponse.getMessageDecoders()));
	}
{

		ResolvableType bodyType = ResolvableType.forClass(bodyClass);
		return webResponse -> webResponse.getClientResponse()
				.then(response -> {
					List<Decoder<?>> decoders = webResponse.getMessageDecoders();
					return Mono.when(
							decodeResponseBody(response, bodyType, decoders).next().defaultIfEmpty(EMPTY_BODY),
							Mono.just(response.getHeaders()),
							Mono.just(response.getStatusCode()));
				})
				.map(tuple -> {
					Object body = (tuple.getT1() != EMPTY_BODY ? tuple.getT1() : null);
					//noinspection unchecked
					return new ResponseEntity<>((T) body, tuple.getT2(), tuple.getT3());
				});
	}
{
		ResolvableType resolvableType = ResolvableType.forClass(sourceClass);
		return webResponse -> webResponse.getClientResponse()
				.map(response -> new ResponseEntity<>(
						decodeResponseBody(response, resolvableType, webResponse.getMessageDecoders()),
						response.getHeaders(), response.getStatusCode()));
	}
{

		Class<?> outputClass = type.getRawClass();
		try {
			Source source = processSource(
					new StreamSource(DataBufferUtils.toInputStream(inputStream)));
			Unmarshaller unmarshaller = createUnmarshaller(outputClass);
			if (outputClass.isAnnotationPresent(XmlRootElement.class)) {
				return Flux.just(unmarshaller.unmarshal(source));
			}
			else {
				JAXBElement<?> jaxbElement = unmarshaller.unmarshal(source, outputClass);
				return Flux.just(jaxbElement.getValue());
			}
		}
		catch (UnmarshalException ex) {
			return Flux.error(
			  new CodecException("Could not unmarshal to [" + outputClass + "]: " + ex.getMessage(), ex));
		}
		catch (JAXBException ex) {
			return Flux.error(new CodecException("Could not instantiate JAXBContext: " +
					ex.getMessage(), ex));
		}
	}
{

		if (initialRequest) {
			writeFrame(SockJsFrame.openFrame());
			resetRequest();
		}
		else if (!getMessageCache().isEmpty()) {
			flushCache();
		}
		else {
			scheduleHeartbeat();
		}
	}
{
		List<Object> arguments = new LinkedList<Object>();
		String[] codes = new String[] {objectName + Errors.NESTED_PATH_SEPARATOR + field, field};
		arguments.add(new DefaultMessageSourceResolvable(codes, field));
		// Using a TreeMap for alphabetical ordering of attribute names
		Map<String, Object> attributesToExpose = new TreeMap<String, Object>();
		for (Map.Entry<String, Object> entry : descriptor.getAttributes().entrySet()) {
			String attributeName = entry.getKey();
			Object attributeValue = entry.getValue();
			if (!internalAnnotationAttributes.contains(attributeName)) {
				if (attributeValue instanceof String) {
					attributeValue = new ResolvableAttribute(attributeValue.toString());
				}
				attributesToExpose.put(attributeName, attributeValue);
			}
		}
		arguments.addAll(attributesToExpose.values());
		return arguments.toArray(new Object[arguments.size()]);
	}
{
		Assert.notNull(element, "AnnotatedElement must not be null");
		Assert.notNull(annotationType, "annotationType must not be null");
		final Set<String> types = new LinkedHashSet<String>();

		try {
			Annotation annotation = element.getAnnotation(annotationType);
			if (annotation != null) {
				searchWithGetSemantics(annotation.annotationType(), annotationType, null, null,
					new SimpleAnnotationProcessor<Object>() {
					@Override
					public Object process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth) {
						types.add(annotation.annotationType().getName());
						return CONTINUE;
					}
				}, new HashSet<AnnotatedElement>(), 1);
			}
		}
		catch (Throwable ex) {
			AnnotationUtils.rethrowAnnotationConfigurationException(ex);
			throw new IllegalStateException("Failed to introspect annotations on " + element, ex);
		}

		return (!types.isEmpty() ? types : null);
	}
{

		try {
			return searchWithGetSemantics(
					element, annotationType, annotationName, processor, new HashSet<AnnotatedElement>(), 0);
		}
		catch (Throwable ex) {
			AnnotationUtils.rethrowAnnotationConfigurationException(ex);
			throw new IllegalStateException("Failed to introspect annotations on " + element, ex);
		}
	}
{
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(startsWith("annotationType must be a repeatable annotation"));
		exception.expectMessage(containsString("failed to resolve container type for"));
		exception.expectMessage(containsString(NonRepeatable.class.getName()));
		findMergedRepeatableAnnotations(getClass(), NonRepeatable.class);
	}
{
		exception.expect(AnnotationConfigurationException.class);
		exception.expectMessage(startsWith("Invalid declaration of container type"));
		exception.expectMessage(containsString(ContainerMissingValueAttribute.class.getName()));
		exception.expectMessage(containsString("for repeatable annotation"));
		exception.expectMessage(containsString(InvalidRepeatable.class.getName()));
		exception.expectCause(isA(NoSuchMethodException.class));
		findMergedRepeatableAnnotations(getClass(), InvalidRepeatable.class, ContainerMissingValueAttribute.class);
	}
{
		exception.expect(AnnotationConfigurationException.class);
		exception.expectMessage(startsWith("Container type"));
		exception.expectMessage(containsString(ContainerWithNonArrayValueAttribute.class.getName()));
		exception.expectMessage(containsString("must declare a 'value' attribute for an array of type"));
		exception.expectMessage(containsString(InvalidRepeatable.class.getName()));
		findMergedRepeatableAnnotations(getClass(), InvalidRepeatable.class, ContainerWithNonArrayValueAttribute.class);
	}
{
		exception.expect(AnnotationConfigurationException.class);
		exception.expectMessage(startsWith("Container type"));
		exception.expectMessage(containsString(ContainerWithArrayValueAttributeButWrongComponentType.class.getName()));
		exception.expectMessage(containsString("must declare a 'value' attribute for an array of type"));
		exception.expectMessage(containsString(InvalidRepeatable.class.getName()));
		findMergedRepeatableAnnotations(getClass(), InvalidRepeatable.class,
			ContainerWithArrayValueAttributeButWrongComponentType.class);
	}
{
		URI uri = request.getURI();
		UriComponentsBuilder builder = UriComponentsBuilder.fromUri(uri);

		String scheme = uri.getScheme();
		String host = uri.getHost();
		int port = uri.getPort();

		String forwardedHeader = request.getHeaders().getFirst("Forwarded");
		if (StringUtils.hasText(forwardedHeader)) {
			String forwardedToUse = StringUtils.commaDelimitedListToStringArray(forwardedHeader)[0];
			Matcher m = FORWARDED_HOST_PATTERN.matcher(forwardedToUse);
			if (m.find()) {
				host = m.group(1).trim();
			}
			m = FORWARDED_PROTO_PATTERN.matcher(forwardedToUse);
			if (m.find()) {
				scheme = m.group(1).trim();
			}
		}
		else {
			String hostHeader = request.getHeaders().getFirst("X-Forwarded-Host");
			if (StringUtils.hasText(hostHeader)) {
				String[] hosts = StringUtils.commaDelimitedListToStringArray(hostHeader);
				String hostToUse = hosts[0];
				if (hostToUse.contains(":")) {
					String[] hostAndPort = StringUtils.split(hostToUse, ":");
					host = hostAndPort[0];
					port = Integer.parseInt(hostAndPort[1]);
				}
				else {
					host = hostToUse;
					port = -1;
				}
			}

			String portHeader = request.getHeaders().getFirst("X-Forwarded-Port");
			if (StringUtils.hasText(portHeader)) {
				String[] ports = StringUtils.commaDelimitedListToStringArray(portHeader);
				port = Integer.parseInt(ports[0]);
			}

			String protocolHeader = request.getHeaders().getFirst("X-Forwarded-Proto");
			if (StringUtils.hasText(protocolHeader)) {
				String[] protocols = StringUtils.commaDelimitedListToStringArray(protocolHeader);
				scheme = protocols[0];
			}
		}

		builder.scheme(scheme);
		builder.host(host);
		builder.port(null);
		if (scheme.equals("http") && port != 80 || scheme.equals("https") && port != 443) {
			builder.port(port);
		}
		return builder;
	}
{
		return new UriComponentsBuilder(this);
	}
{

		Assert.notNull(element, "AnnotatedElement must not be null");
		Assert.notNull(annotationType, "annotationType must not be null");

		MergedAnnotationAttributesProcessor processor =
				new MergedAnnotationAttributesProcessor(annotationType, null, false, false, true);
		searchWithFindSemantics(element, annotationType, annotationType.getName(), processor);

		Set<A> annotations = new LinkedHashSet<A>();
		for (AnnotationAttributes attributes : processor.getAggregatedResults()) {
			AnnotationUtils.postProcessAnnotationAttributes(element, attributes, false, false);
			annotations.add(AnnotationUtils.synthesizeAnnotation(attributes, annotationType, element));
		}
		return annotations;
	}
{

		Assert.notNull(element, "AnnotatedElement must not be null");
		Assert.notNull(annotationType, "annotationType must not be null");

		if (containerType == null) {
			containerType = AnnotationUtils.resolveContainerAnnotationType(annotationType);
			if (containerType == null) {
				throw new IllegalArgumentException(
					"annotationType must be a repeatable annotation: failed to resolve container type for "
							+ annotationType.getName());
			}
		}
		else {
			validateRepeatableContainerType(annotationType, containerType);
		}

		MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(annotationType, null,
			false, false, true);

		searchWithFindSemantics(element, annotationType, annotationType.getName(), containerType, processor);

		Set<A> annotations = new LinkedHashSet<A>();
		for (AnnotationAttributes attributes : processor.getAggregatedResults()) {
			AnnotationUtils.postProcessAnnotationAttributes(element, attributes, false, false);
			annotations.add(AnnotationUtils.synthesizeAnnotation(attributes, annotationType, element));
		}
		return annotations;
	}
{

		try {
			return searchWithFindSemantics(
					element, annotationType, annotationName, processor, new HashSet<AnnotatedElement>(), 0);
		}
		catch (Throwable ex) {
			AnnotationUtils.rethrowAnnotationConfigurationException(ex);
			throw new IllegalStateException("Failed to introspect annotations on " + element, ex);
		}
	}
{
		TransactionalEventListener annotation =
				AnnotatedElementUtils.findMergedAnnotation(method, TransactionalEventListener.class);
		if (annotation == null) {
			throw new IllegalStateException("No TransactionalEventListener annotation found on '" + method + "'");
		}
		return annotation;
	}
{

		InputStream in = resource.getInputStream();
		try {
			StreamUtils.copy(in, outputMessage.getBody());
		}
		finally {
			try {
				in.close();
			}
			catch (IOException ex) {
			}
		}
		outputMessage.getBody().flush();
	}
{

		long length = resource.contentLength();

		List<HttpRange> ranges;
		try {
			HttpHeaders headers = new ServletServerHttpRequest(request).getHeaders();
			ranges = headers.getRange();
		}
		catch (IllegalArgumentException ex) {
			response.addHeader("Content-Range", "bytes */" + length);
			response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            return;
		}

		response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

		if (ranges.size() == 1) {
			HttpRange range = ranges.get(0);

			long start = range.getRangeStart(length);
			long end = range.getRangeEnd(length);
			long rangeLength = end - start + 1;

			setHeaders(response, resource, contentType);
			response.addHeader("Content-Range", "bytes " + start + "-" + end + "/" + length);
            response.setContentLength((int) rangeLength);

			InputStream in = resource.getInputStream();
			try {
				copyRange(in, response.getOutputStream(), start, end);
			}
			finally {
				try {
					in.close();
				}
				catch (IOException ex) {
					// ignore
				}
			}
		}
		else {
			String boundaryString = MimeTypeUtils.generateMultipartBoundaryString();
			response.setContentType("multipart/byteranges; boundary=" + boundaryString);

			ServletOutputStream out = response.getOutputStream();

			for (HttpRange range : ranges) {
				long start = range.getRangeStart(length);
				long end = range.getRangeEnd(length);

				InputStream in = resource.getInputStream();

                // Writing MIME header.
                out.println();
                out.println("--" + boundaryString);
                if (contentType != null) {
	                out.println("Content-Type: " + contentType);
                }
                out.println("Content-Range: bytes " + start + "-" + end + "/" + length);
                out.println();

                // Printing content
                copyRange(in, out, start, end);
			}
			out.println();
            out.print("--" + boundaryString + "--");
		}
	}
{
		String body = "Foo";
		ResponseEntity<String> returnValue = new ResponseEntity<String>(body, HttpStatus.OK);

		MediaType accepted = MediaType.TEXT_PLAIN;
		servletRequest.addHeader("Accept", accepted.toString());

		given(messageConverter.canWrite(String.class, null)).willReturn(true);
		given(messageConverter.getSupportedMediaTypes()).willReturn(Collections.singletonList(MediaType.TEXT_PLAIN));
		given(messageConverter.canWrite(String.class, accepted)).willReturn(true);

		processor.handleReturnValue(returnValue, returnTypeResponseEntity, mavContainer, webRequest);

		assertTrue(mavContainer.isRequestHandled());
		verify(messageConverter).write(eq(body), eq(accepted), isA(HttpOutputMessage.class));
	}
{

			Object value = AnnotationUtils.getValue(annotation, sourceAttributeName);
			Object adaptedValue = AnnotationUtils.adaptValue(element, value, this.classValuesAsString,
				this.nestedAnnotationsAsMap);

			for (String targetAttributeName : targetAttributeNames) {
				attributes.put(targetAttributeName, adaptedValue);
			}
		}
{
		Class<?>[] specifiedInterfaces = advised.getProxiedInterfaces();
		if (specifiedInterfaces.length == 0) {
			// No user-specified interfaces: check whether target class is an interface.
			Class<?> targetClass = advised.getTargetClass();
			if (targetClass != null) {
				if (targetClass.isInterface()) {
					advised.setInterfaces(targetClass);
				}
				else if (Proxy.isProxyClass(targetClass)) {
					advised.setInterfaces(targetClass.getInterfaces());
				}
				specifiedInterfaces = advised.getProxiedInterfaces();
			}
		}
		boolean addSpringProxy = !advised.isInterfaceProxied(SpringProxy.class);
		boolean addAdvised = !advised.isOpaque() && !advised.isInterfaceProxied(Advised.class);
		int nonUserIfcCount = 0;
		if (addSpringProxy) {
			nonUserIfcCount++;
		}
		if (addAdvised) {
			nonUserIfcCount++;
		}
		Class<?>[] proxiedInterfaces = new Class<?>[specifiedInterfaces.length + nonUserIfcCount];
		System.arraycopy(specifiedInterfaces, 0, proxiedInterfaces, 0, specifiedInterfaces.length);
		if (addSpringProxy) {
			proxiedInterfaces[specifiedInterfaces.length] = SpringProxy.class;
		}
		if (addAdvised) {
			proxiedInterfaces[proxiedInterfaces.length - 1] = Advised.class;
		}
		return proxiedInterfaces;
	}
{

		Assert.notNull(number, "Number must not be null");
		Assert.notNull(targetClass, "Target class must not be null");

		if (targetClass.isInstance(number)) {
			return (T) number;
		}
		else if (Byte.class == targetClass) {
			long value = number.longValue();
			if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
				raiseOverflowException(number, targetClass);
			}
			return (T) Byte.valueOf(number.byteValue());
		}
		else if (Short.class == targetClass) {
			long value = number.longValue();
			if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
				raiseOverflowException(number, targetClass);
			}
			return (T) Short.valueOf(number.shortValue());
		}
		else if (Integer.class == targetClass) {
			long value = number.longValue();
			if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
				raiseOverflowException(number, targetClass);
			}
			return (T) Integer.valueOf(number.intValue());
		}
		else if (Long.class == targetClass) {
			BigInteger bigInt = null;
			if (number instanceof BigInteger) {
				bigInt = (BigInteger) number;
			}
			else if (number instanceof BigDecimal) {
				bigInt = ((BigDecimal) number).toBigInteger();
			}
			// Effectively analogous to JDK 8's BigInteger.longValueExact()
			if (bigInt != null && (bigInt.compareTo(LONG_MIN) < 0 || bigInt.compareTo(LONG_MAX) > 0)) {
				raiseOverflowException(number, targetClass);
			}
			return (T) Long.valueOf(number.longValue());
		}
		else if (BigInteger.class == targetClass) {
			if (number instanceof BigDecimal) {
				// do not lose precision - use BigDecimal's own conversion
				return (T) ((BigDecimal) number).toBigInteger();
			}
			else {
				// original value is not a Big* number - use standard long conversion
				return (T) BigInteger.valueOf(number.longValue());
			}
		}
		else if (Float.class == targetClass) {
			return (T) Float.valueOf(number.floatValue());
		}
		else if (Double.class == targetClass) {
			return (T) Double.valueOf(number.doubleValue());
		}
		else if (BigDecimal.class == targetClass) {
			// always use BigDecimal(String) here to avoid unpredictability of BigDecimal(double)
			// (see BigDecimal javadoc for details)
			return (T) new BigDecimal(number.toString());
		}
		else {
			throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" +
					number.getClass().getName() + "] to unsupported target class [" + targetClass.getName() + "]");
		}
	}
{
		Assert.notNull(defaultConnection, "Default WebConnection must not be null");
		MockMvcWebConnection mockMvcWebConnection = new MockMvcWebConnection(this.mockMvc, this.contextPath);

		if (this.alwaysUseMockMvc) {
			return mockMvcWebConnection;
		}

		List<DelegatingWebConnection.DelegateWebConnection> delegates = new ArrayList<DelegatingWebConnection.DelegateWebConnection>(
			this.mockMvcRequestMatchers.size());
		for (WebRequestMatcher matcher : this.mockMvcRequestMatchers) {
			delegates.add(new DelegatingWebConnection.DelegateWebConnection(matcher, mockMvcWebConnection));
		}

		return new DelegatingWebConnection(defaultConnection, delegates);
	}
{
		setField(person, "id", new Long(99), long.class);
		setField(person, "name", "Tom");
		setField(person, "age", new Integer(42));
		setField(person, "eyeColor", "blue", String.class);
		setField(person, "likesPets", Boolean.TRUE);
		setField(person, "favoriteNumber", PI, Number.class);

		assertEquals("ID (private field in a superclass)", 99, person.getId());
		assertEquals("name (protected field)", "Tom", person.getName());
		assertEquals("age (private field)", 42, person.getAge());
		assertEquals("eye color (package private field)", "blue", person.getEyeColor());
		assertEquals("'likes pets' flag (package private boolean field)", true, person.likesPets());
		assertEquals("'favorite number' (package field)", PI, person.getFavoriteNumber());

		assertEquals(new Long(99), getField(person, "id"));
		assertEquals("Tom", getField(person, "name"));
		assertEquals(new Integer(42), getField(person, "age"));
		assertEquals("blue", getField(person, "eyeColor"));
		assertEquals(Boolean.TRUE, getField(person, "likesPets"));
		assertEquals(PI, getField(person, "favoriteNumber"));
	}
{
		System.setProperty("domain", "bean");
		AnnotationConfigApplicationContext ctx =
				new AnnotationConfigApplicationContext(LazyAssemblingConfiguration.class);
		try {
			MBeanServer server = (MBeanServer) ctx.getBean("server");

			ObjectName oname = ObjectNameManager.getInstance("bean:name=testBean4");
			assertNotNull(server.getObjectInstance(oname));
			String name = (String) server.getAttribute(oname, "Name");
			assertEquals("Invalid name returned", "TEST", name);

			oname = ObjectNameManager.getInstance("bean:name=testBean5");
			assertNotNull(server.getObjectInstance(oname));
			name = (String) server.getAttribute(oname, "Name");
			assertEquals("Invalid name returned", "FACTORY", name);

			oname = ObjectNameManager.getInstance("spring:mbean=true");
			assertNotNull(server.getObjectInstance(oname));
			name = (String) server.getAttribute(oname, "Name");
			assertEquals("Invalid name returned", "Rob Harrop", name);

			oname = ObjectNameManager.getInstance("spring:mbean=another");
			assertNotNull(server.getObjectInstance(oname));
			name = (String) server.getAttribute(oname, "Name");
			assertEquals("Invalid name returned", "Juergen Hoeller", name);
		}
		finally {
			System.clearProperty("domain");
			ctx.close();
		}
	}
{
		AsyncTaskExecutor executor = this.executors.get(method);
		if (executor == null) {
			Executor executorToUse = this.defaultExecutor;
			String qualifier = getExecutorQualifier(method);
			if (StringUtils.hasLength(qualifier)) {
				if (this.beanFactory == null) {
					throw new IllegalStateException("BeanFactory must be set on " + getClass().getSimpleName() +
							" to access qualified executor '" + qualifier + "'");
				}
				executorToUse = BeanFactoryAnnotationUtils.qualifiedBeanOfType(
						this.beanFactory, Executor.class, qualifier);
			}
			else if (executorToUse == null) {
				return null;
			}
			executor = (executorToUse instanceof AsyncListenableTaskExecutor ?
					(AsyncListenableTaskExecutor) executorToUse : new TaskExecutorAdapter(executorToUse));
			this.executors.put(method, executor);
		}
		return executor;
	}
{
		Class<?> testClass = getBootstrapContext().getTestClass();
		CacheAwareContextLoaderDelegate cacheAwareContextLoaderDelegate = getCacheAwareContextLoaderDelegate();

		if (MetaAnnotationUtils.findAnnotationDescriptorForTypes(testClass, ContextConfiguration.class,
			ContextHierarchy.class) == null) {
			if (logger.isInfoEnabled()) {
				logger.info(String.format(
					"Neither @ContextConfiguration nor @ContextHierarchy found for test class [%s]",
					testClass.getName()));
			}
			return new MergedContextConfiguration(testClass, null, null, null, null);
		}

		if (AnnotationUtils.findAnnotation(testClass, ContextHierarchy.class) != null) {
			Map<String, List<ContextConfigurationAttributes>> hierarchyMap = ContextLoaderUtils.buildContextHierarchyMap(testClass);
			MergedContextConfiguration parentConfig = null;
			MergedContextConfiguration mergedConfig = null;

			for (List<ContextConfigurationAttributes> list : hierarchyMap.values()) {
				List<ContextConfigurationAttributes> reversedList = new ArrayList<ContextConfigurationAttributes>(list);
				Collections.reverse(reversedList);

				// Don't use the supplied testClass; instead ensure that we are
				// building the MCC for the actual test class that declared the
				// configuration for the current level in the context hierarchy.
				Assert.notEmpty(reversedList, "ContextConfigurationAttributes list must not be empty");
				Class<?> declaringClass = reversedList.get(0).getDeclaringClass();

				mergedConfig = buildMergedContextConfiguration(declaringClass, reversedList, parentConfig,
					cacheAwareContextLoaderDelegate);
				parentConfig = mergedConfig;
			}

			// Return the last level in the context hierarchy
			return mergedConfig;
		}
		else {
			return buildMergedContextConfiguration(testClass,
				ContextLoaderUtils.resolveContextConfigurationAttributes(testClass), null,
				cacheAwareContextLoaderDelegate);
		}
	}
{
		Class<?> testClass = bootstrapContext.getTestClass();

		Class<? extends TestContextBootstrapper> clazz = null;
		try {

			MultiValueMap<String, Object> attributesMultiMap = AnnotatedElementUtils.getAllAnnotationAttributes(
				testClass, BootstrapWith.class.getName());
			List<Object> values = (attributesMultiMap == null ? null : attributesMultiMap.get(AnnotationUtils.VALUE));

			if (values != null) {
				if (values.size() != 1) {
					String msg = String.format(
						"Configuration error: found multiple declarations of @BootstrapWith on test class [%s] with values %s",
						testClass.getName(), values);
					throw new IllegalStateException(msg);
				}
				clazz = (Class<? extends TestContextBootstrapper>) values.get(0);
			}
			else {
				clazz = (Class<? extends TestContextBootstrapper>) ClassUtils.forName(
					DEFAULT_TEST_CONTEXT_BOOTSTRAPPER_CLASS_NAME, BootstrapUtils.class.getClassLoader());
			}

			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Instantiating TestContextBootstrapper for test class [%s] from class [%s]",
					testClass.getName(), clazz.getName()));
			}

			TestContextBootstrapper testContextBootstrapper = instantiateClass(clazz, TestContextBootstrapper.class);
			testContextBootstrapper.setBootstrapContext(bootstrapContext);

			return testContextBootstrapper;
		}
		catch (Throwable t) {
			if (t instanceof IllegalStateException) {
				throw (IllegalStateException) t;
			}

			throw new IllegalStateException("Could not load TestContextBootstrapper [" + clazz
					+ "]. Specify @BootstrapWith's 'value' attribute "
					+ "or make the default bootstrapper class available.", t);
		}
	}
{
		String[] expected = asArray("com.example.app.test");
		Class<?> element = ComponentScanWithBasePackagesAndValueAliasClass.class;
		AnnotationAttributes attributes = findMergedAnnotationAttributes(element, ComponentScan.class);

		assertNotNull("Should find @ComponentScan on " + element, attributes);
		assertArrayEquals("value: ", expected, attributes.getStringArray("value"));
		assertArrayEquals("basePackages: ", expected, attributes.getStringArray("basePackages"));
	}
{

		// For very general mappings (e.g. "/") we need to check 404 first
		Resource resource = getResource(request);
		if (resource == null) {
			logger.trace("No matching resource found - returning 404");
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		if (HttpMethod.OPTIONS.matches(request.getMethod())) {
			response.setHeader("Allow", getAllowHeader());
			return;
		}

		// Supported methods and required session
		checkRequest(request);

		// Header phase
		if (new ServletWebRequest(request, response).checkNotModified(resource.lastModified())) {
			logger.trace("Resource not modified - returning 304");
			return;
		}

		// Apply cache settings, if any
		prepareResponse(response);

		// Check the resource's media type
		MediaType mediaType = getMediaType(resource);
		if (mediaType != null) {
			if (logger.isTraceEnabled()) {
				logger.trace("Determined media type '" + mediaType + "' for " + resource);
			}
		}
		else {
			if (logger.isTraceEnabled()) {
				logger.trace("No media type found for " + resource + " - not sending a content-type header");
			}
		}

		// Content phase
		if (METHOD_HEAD.equals(request.getMethod())) {
			setHeaders(response, resource, mediaType);
			logger.trace("HEAD request - skipping content");
			return;
		}

		if (request.getHeader(HttpHeaders.RANGE) == null) {
			setHeaders(response, resource, mediaType);
			writeContent(response, resource);
		}
		else {
			writePartialContent(request, response, resource, mediaType);
		}
	}
{
			super(request);

			HttpRequest httpRequest = new ServletServerHttpRequest(request);
			UriComponents uriComponents = UriComponentsBuilder.fromHttpRequest(httpRequest).build();
			int port = uriComponents.getPort();

			this.scheme = uriComponents.getScheme();
			this.secure = "https".equals(scheme);
			this.host = uriComponents.getHost();
			this.port = (port == -1 ? (this.secure ? 443 : 80) : port);
			this.portInUrl = (port == -1 ? "" : ":" + port);

			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String name = headerNames.nextElement();
				this.headers.put(name, Collections.list(request.getHeaders(name)));
			}
			for (String name : FORWARDED_HEADER_NAMES) {
				this.headers.remove(name);
			}
		}
{
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
			inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
			inputFactory.setXMLResolver(NO_OP_XML_RESOLVER);
			return new XmlMapper(inputFactory);
		}
{
		long ifModifiedSince = -1;
		try {
			ifModifiedSince = getRequest().getDateHeader(HEADER_IF_MODIFIED_SINCE);
		}
		catch (IllegalArgumentException ex) {
			String headerValue = getRequest().getHeader(HEADER_IF_MODIFIED_SINCE);
			// Possibly an IE 10 style value: "Wed, 09 Apr 2014 09:57:42 GMT; length=13774"
			int separatorIndex = headerValue.indexOf(';');
			if (separatorIndex != -1) {
				String datePart = headerValue.substring(0, separatorIndex);
				try {
					ifModifiedSince = Date.parse(datePart);
				}
				catch (IllegalArgumentException ex2) {
					// Giving up
				}
			}
		}
		return (ifModifiedSince >= (lastModifiedTimestamp / 1000 * 1000));
	}
{
		Class<?> clazz = ComposedAnnotationController.class;
		Method method = clazz.getMethod("handleInput");
		RequestMappingInfo info = this.handlerMapping.getMappingForMethod(method, clazz);

		assertNotNull(info);
		assertEquals(Collections.singleton("/input"), info.getPatternsCondition().getPatterns());
	}
{
		return this.remainingExpectations;
	}
{
		Assert.notNull(restGateway, "'gatewaySupport' must not be null");
		return createServer(restGateway.getRestTemplate());
	}
{
			Assert.notNull(uri, "'uri' must not be null");
			Assert.notNull(httpMethod, "'httpMethod' must not be null");

			if (this.requestIterator == null) {
				this.requestIterator = MockRestServiceServer.this.expectedRequests.iterator();
			}
			if (!this.requestIterator.hasNext()) {
				throw new AssertionError("No further requests expected: HTTP " + httpMethod + " " + uri);
			}

			RequestMatcherClientHttpRequest request = this.requestIterator.next();
			request.setURI(uri);
			request.setMethod(httpMethod);

			MockRestServiceServer.this.actualRequests.add(request);
			return request;
		}
{
		RequestMethod requestMethod = getRequestMethod(request);
		if (this.methods.isEmpty()) {
			return (RequestMethod.OPTIONS.equals(requestMethod) ? null : this);
		}
		if (requestMethod != null) {
			for (RequestMethod method : this.methods) {
				if (method.equals(requestMethod)) {
					return new RequestMethodsRequestCondition(method);
				}
			}
			if (RequestMethod.HEAD.equals(requestMethod) && getMethods().contains(RequestMethod.GET)) {
				return HEAD_CONDITION;
			}
		}
		return null;
	}
{
		return new OkHttpClientHttpRequest(this.client, uri, httpMethod);
	}
{

		class TestRedirectView extends RedirectView {

			public boolean queryPropertiesCalled = false;

			/**
			 * Test whether this callback method is called with correct args
			 */
			@Override
			protected Map<String, Object> queryProperties(Map<String, Object> model) {
				// They may not be the same model instance, but they're still equal
				assertTrue("Map and model must be equal.", map.equals(model));
				this.queryPropertiesCalled = true;
				return super.queryProperties(model);
			}
		}

		TestRedirectView rv = new TestRedirectView();
		rv.setUrl(url);
		rv.setContextRelative(contextRelative);
		rv.setExposeModelAttributes(exposeModelAttributes);

		HttpServletRequest request = mock(HttpServletRequest.class, "request");
		if (exposeModelAttributes) {
			given(request.getCharacterEncoding()).willReturn(WebUtils.DEFAULT_CHARACTER_ENCODING);
		}
		if (contextRelative) {
			expectedUrlForEncoding = "/context" + expectedUrlForEncoding;
			given(request.getContextPath()).willReturn("/context");
		}

		given(request.getAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE)).willReturn(new FlashMap());

		FlashMapManager flashMapManager = new SessionFlashMapManager();
		given(request.getAttribute(DispatcherServlet.FLASH_MAP_MANAGER_ATTRIBUTE)).willReturn(flashMapManager);

		HttpServletResponse response = mock(HttpServletResponse.class, "response");
		given(response.encodeRedirectURL(expectedUrlForEncoding)).willReturn(expectedUrlForEncoding);
		response.sendRedirect(expectedUrlForEncoding);

		rv.render(map, request, response);
		if (exposeModelAttributes) {
			assertTrue("queryProperties() should have been called.", rv.queryPropertiesCalled);
		}
	}
{

		class TestRedirectView extends RedirectView {

			public boolean queryPropertiesCalled = false;

			/**
			 * Test whether this callback method is called with correct args
			 */
			@Override
			protected Map<String, Object> queryProperties(Map<String, Object> model) {
				// They may not be the same model instance, but they're still equal
				assertTrue("Map and model must be equal.", map.equals(model));
				this.queryPropertiesCalled = true;
				return super.queryProperties(model);
			}
		}

		TestRedirectView rv = new TestRedirectView();
		rv.setUrl(url);
		rv.setContextRelative(contextRelative);
		rv.setExposeModelAttributes(exposeModelAttributes);

		HttpServletRequest request = mock(HttpServletRequest.class, "request");
		if (exposeModelAttributes) {
			given(request.getCharacterEncoding()).willReturn(WebUtils.DEFAULT_CHARACTER_ENCODING);
		}
		if (contextRelative) {
			expectedUrlForEncoding = "/context" + expectedUrlForEncoding;
			given(request.getContextPath()).willReturn("/context");
		}

		given(request.getAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE)).willReturn(new FlashMap());

		FlashMapManager flashMapManager = new SessionFlashMapManager();
		given(request.getAttribute(DispatcherServlet.FLASH_MAP_MANAGER_ATTRIBUTE)).willReturn(flashMapManager);

		HttpServletResponse response = mock(HttpServletResponse.class, "response");
		given(response.encodeRedirectURL(expectedUrlForEncoding)).willReturn(expectedUrlForEncoding);
		response.sendRedirect(expectedUrlForEncoding);

		rv.render(map, request, response);
		if (exposeModelAttributes) {
			assertTrue("queryProperties() should have been called.", rv.queryPropertiesCalled);
		}
	}
{
		Assert.notNull(buffer, "'buffer' must not be null");

		byte[] bytes1 = new byte[buffer.readableByteCount()];
		buffer.read(bytes1);

		Byte[] bytes2 = new Byte[bytes1.length];
		for (int i = 0; i < bytes1.length; i++) {
			bytes2[i] = bytes1[i];
		}
		return Flux.fromArray(bytes2);
	}
{

		HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(servletRequest);

		Object arg = readWithMessageConverters(inputMessage, methodParam, paramType);
		if (arg == null) {
			if (methodParam.getParameterAnnotation(RequestBody.class).required()) {
				throw new HttpMessageNotReadableException("Required request body is missing: " +
						methodParam.getMethod().toGenericString());
			}
		}
		return arg;
	}
{
		this.headersWritten = true;
		return body;
	}
{
		loadBeanDefinitions("mvc-config-argument-resolvers.xml");
		RequestMappingHandlerAdapter adapter = appContext.getBean(RequestMappingHandlerAdapter.class);
		assertNotNull(adapter);
		Object value = new DirectFieldAccessor(adapter).getPropertyValue("customArgumentResolvers");
		assertNotNull(value);
		assertTrue(value instanceof List);
		List<HandlerMethodArgumentResolver> resolvers = (List<HandlerMethodArgumentResolver>) value;
		assertEquals(3, resolvers.size());
		assertTrue(resolvers.get(0) instanceof ServletWebArgumentResolverAdapter);
		assertTrue(resolvers.get(1) instanceof TestHandlerMethodArgumentResolver);
		assertTrue(resolvers.get(2) instanceof TestHandlerMethodArgumentResolver);
		assertNotSame(resolvers.get(1), resolvers.get(2));
	}
{
		loadBeanDefinitions("mvc-config-return-value-handlers.xml");
		RequestMappingHandlerAdapter adapter = appContext.getBean(RequestMappingHandlerAdapter.class);
		assertNotNull(adapter);
		Object value = new DirectFieldAccessor(adapter).getPropertyValue("customReturnValueHandlers");
		assertNotNull(value);
		assertTrue(value instanceof List);
		List<HandlerMethodReturnValueHandler> handlers = (List<HandlerMethodReturnValueHandler>) value;
		assertEquals(2, handlers.size());
		assertEquals(TestHandlerMethodReturnValueHandler.class, handlers.get(0).getClass());
		assertEquals(TestHandlerMethodReturnValueHandler.class, handlers.get(1).getClass());
		assertNotSame(handlers.get(0), handlers.get(1));
	}
{

		Class<?> type = descriptor.getDependencyType();
		Object value = getAutowireCandidateResolver().getSuggestedValue(descriptor);
		if (value != null) {
			if (value instanceof String) {
				String strVal = resolveEmbeddedValue((String) value);
				BeanDefinition bd = (beanName != null && containsBean(beanName) ? getMergedBeanDefinition(beanName) : null);
				value = evaluateBeanDefinitionString(strVal, bd);
			}
			TypeConverter converter = (typeConverter != null ? typeConverter : getTypeConverter());
			return (descriptor.getField() != null ?
					converter.convertIfNecessary(value, type, descriptor.getField()) :
					converter.convertIfNecessary(value, type, descriptor.getMethodParameter()));
		}

		if (type.isArray()) {
			Class<?> componentType = type.getComponentType();
			DependencyDescriptor targetDesc = new DependencyDescriptor(descriptor);
			targetDesc.increaseNestingLevel();
			Map<String, Object> matchingBeans = findAutowireCandidates(beanName, componentType, targetDesc);
			if (matchingBeans.isEmpty()) {
				if (descriptor.isRequired()) {
					raiseNoSuchBeanDefinitionException(componentType, "array of " + componentType.getName(), descriptor);
				}
				return null;
			}
			if (autowiredBeanNames != null) {
				autowiredBeanNames.addAll(matchingBeans.keySet());
			}
			TypeConverter converter = (typeConverter != null ? typeConverter : getTypeConverter());
			Object result = converter.convertIfNecessary(matchingBeans.values(), type);
			if (getDependencyComparator() != null && result instanceof Object[]) {
				Arrays.sort((Object[]) result, adaptDependencyComparator(matchingBeans));
			}
			return result;
		}
		else if (Collection.class.isAssignableFrom(type) && type.isInterface()) {
			Class<?> elementType = descriptor.getCollectionType();
			if (elementType == null) {
				if (descriptor.isRequired()) {
					throw new FatalBeanException("No element type declared for collection [" + type.getName() + "]");
				}
				return null;
			}
			DependencyDescriptor targetDesc = new DependencyDescriptor(descriptor);
			targetDesc.increaseNestingLevel();
			Map<String, Object> matchingBeans = findAutowireCandidates(beanName, elementType, targetDesc);
			if (matchingBeans.isEmpty()) {
				if (descriptor.isRequired()) {
					raiseNoSuchBeanDefinitionException(elementType, "collection of " + elementType.getName(), descriptor);
				}
				return null;
			}
			if (autowiredBeanNames != null) {
				autowiredBeanNames.addAll(matchingBeans.keySet());
			}
			TypeConverter converter = (typeConverter != null ? typeConverter : getTypeConverter());
			Object result = converter.convertIfNecessary(matchingBeans.values(), type);
			if (getDependencyComparator() != null && result instanceof List) {
				Collections.sort((List<?>) result, adaptDependencyComparator(matchingBeans));
			}
			return result;
		}
		else if (Map.class.isAssignableFrom(type) && type.isInterface()) {
			Class<?> keyType = descriptor.getMapKeyType();
			if (String.class != keyType) {
				if (descriptor.isRequired()) {
					throw new FatalBeanException("Key type [" + keyType + "] of map [" + type.getName() +
							"] must be [java.lang.String]");
				}
				return null;
			}
			Class<?> valueType = descriptor.getMapValueType();
			if (valueType == null) {
				if (descriptor.isRequired()) {
					throw new FatalBeanException("No value type declared for map [" + type.getName() + "]");
				}
				return null;
			}
			DependencyDescriptor targetDesc = new DependencyDescriptor(descriptor);
			targetDesc.increaseNestingLevel();
			Map<String, Object> matchingBeans = findAutowireCandidates(beanName, valueType, targetDesc);
			if (matchingBeans.isEmpty()) {
				if (descriptor.isRequired()) {
					raiseNoSuchBeanDefinitionException(valueType, "map with value type " + valueType.getName(), descriptor);
				}
				return null;
			}
			if (autowiredBeanNames != null) {
				autowiredBeanNames.addAll(matchingBeans.keySet());
			}
			return matchingBeans;
		}
		else {
			Map<String, Object> matchingBeans = findAutowireCandidates(beanName, type, descriptor);
			if (matchingBeans.isEmpty()) {
				if (descriptor.isRequired()) {
					raiseNoSuchBeanDefinitionException(type, "", descriptor);
				}
				return null;
			}
			if (matchingBeans.size() > 1) {
				String primaryBeanName = determineAutowireCandidate(matchingBeans, descriptor);
				if (primaryBeanName == null) {
					throw new NoUniqueBeanDefinitionException(type, matchingBeans.keySet());
				}
				if (autowiredBeanNames != null) {
					autowiredBeanNames.add(primaryBeanName);
				}
				return matchingBeans.get(primaryBeanName);
			}
			// We have exactly one match.
			Map.Entry<String, Object> entry = matchingBeans.entrySet().iterator().next();
			if (autowiredBeanNames != null) {
				autowiredBeanNames.add(entry.getKey());
			}
			return entry.getValue();
		}
	}
{
		MockHttpServletRequest request = createServletRequest(servletContext);

		String requestUri = this.url.getRawPath();
		request.setRequestURI(requestUri);
		updatePathRequestProperties(request, requestUri);

		if (this.url.getScheme() != null) {
			request.setScheme(this.url.getScheme());
		}
		if (this.url.getHost() != null) {
			request.setServerName(this.url.getHost());
		}
		if (this.url.getPort() != -1) {
			request.setServerPort(this.url.getPort());
		}

		request.setMethod(this.method);

		for (String name : this.headers.keySet()) {
			for (Object value : this.headers.get(name)) {
				request.addHeader(name, value);
			}
		}

		try {
			if (this.url.getRawQuery() != null) {
				request.setQueryString(this.url.getRawQuery());
			}

			MultiValueMap<String, String> queryParams =
					UriComponentsBuilder.fromUri(this.url).build().getQueryParams();

			for (Entry<String, List<String>> entry : queryParams.entrySet()) {
				for (String value : entry.getValue()) {
					value = (value != null) ? UriUtils.decode(value, "UTF-8") : null;
					request.addParameter(UriUtils.decode(entry.getKey(), "UTF-8"), value);
				}
			}
		}
		catch (UnsupportedEncodingException ex) {
			// shouldn't happen
		}

		for (String name : this.parameters.keySet()) {
			for (String value : this.parameters.get(name)) {
				request.addParameter(name, value);
			}
		}

		request.setContentType(this.contentType);
		request.setContent(this.content);
		request.setCharacterEncoding(this.characterEncoding);

		if (!ObjectUtils.isEmpty(this.cookies)) {
			request.setCookies(this.cookies.toArray(new Cookie[this.cookies.size()]));
		}

		if (this.locale != null) {
			request.addPreferredLocale(this.locale);
		}

		if (this.secure != null) {
			request.setSecure(this.secure);
		}

		request.setUserPrincipal(this.principal);

		for (String name : this.attributes.keySet()) {
			request.setAttribute(name, this.attributes.get(name));
		}

		// Set session before session and flash attributes
		if (this.session != null) {
			request.setSession(this.session);
		}
		for (String name : this.sessionAttributes.keySet()) {
			request.getSession().setAttribute(name, this.sessionAttributes.get(name));
		}

		FlashMap flashMap = new FlashMap();
		flashMap.putAll(this.flashAttributes);

		FlashMapManager flashMapManager = getFlashMapManager(request);
		flashMapManager.saveOutputFlashMap(flashMap, request, new MockHttpServletResponse());

		request.setAsyncSupported(true);

		return request;
	}
{
		Method specificMethod = getMostSpecificMethod();
		SendTo ann = AnnotationUtils.getAnnotation(specificMethod, SendTo.class);
		if (ann != null) {
			Object[] destinations = ann.value();
			if (destinations.length != 1) {
				throw new IllegalStateException("Invalid @" + SendTo.class.getSimpleName() + " annotation on '" +
						specificMethod + "' one destination must be set (got " + Arrays.toString(destinations) + ")");
			}
			return resolve((String) destinations[0]);
		}
		return null;
	}
{
		if (returnValue == null) {
			return;
		}

		MessageHeaders headers = message.getHeaders();
		String sessionId = SimpMessageHeaderAccessor.getSessionId(headers);
		PlaceholderResolver varResolver = initVarResolver(headers);
		SendToUser sendToUser = returnType.getMethodAnnotation(SendToUser.class);

		if (sendToUser != null) {
			boolean broadcast = sendToUser.broadcast();
			String user = getUserName(message, headers);
			if (user == null) {
				if (sessionId == null) {
					throw new MissingSessionUserException(message);
				}
				user = sessionId;
				broadcast = false;
			}
			String[] destinations = getTargetDestinations(sendToUser, message, this.defaultUserDestinationPrefix);
			for (String destination : destinations) {
				destination = this.placeholderHelper.replacePlaceholders(destination, varResolver);
				if (broadcast) {
					this.messagingTemplate.convertAndSendToUser(
							user, destination, returnValue, createHeaders(null, returnType));
				}
				else {
					this.messagingTemplate.convertAndSendToUser(
							user, destination, returnValue, createHeaders(sessionId, returnType));
				}
			}
		}
		else {
			SendTo sendTo = returnType.getMethodAnnotation(SendTo.class);
			String[] destinations = getTargetDestinations(sendTo, message, this.defaultDestinationPrefix);
			for (String destination : destinations) {
				destination = this.placeholderHelper.replacePlaceholders(destination, varResolver);
				this.messagingTemplate.convertAndSend(destination, returnValue, createHeaders(sessionId, returnType));
			}
		}
	}
{
		given(this.messageChannel.send(any(Message.class))).willReturn(true);

		Message<?> inputMessage = createInputMessage("sess1", "sub1", "/app", "/dest", null);
		this.handler.handleReturnValue(PAYLOAD, this.noAnnotationsReturnType, inputMessage);

		verify(this.messageChannel, times(1)).send(this.messageCaptor.capture());

		SimpMessageHeaderAccessor accessor = getCapturedAccessor(0);
		assertEquals("sess1", accessor.getSessionId());
		assertEquals("/topic/dest", accessor.getDestination());
		assertEquals(MIME_TYPE, accessor.getContentType());
		assertNull("Subscription id should not be copied", accessor.getSubscriptionId());
		assertEquals(this.noAnnotationsReturnType, accessor.getHeader(SimpMessagingTemplate.CONVERSION_HINT_HEADER));
	}
{

		StringWriter writer = new StringWriter();
		objectMapper.writeValue(writer, object);
		return session.createTextMessage(writer.toString());
	}
{

		ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
		OutputStreamWriter writer = new OutputStreamWriter(bos, this.encoding);
		objectMapper.writeValue(writer, object);

		BytesMessage message = session.createBytesMessage();
		message.writeBytes(bos.toByteArray());
		if (this.encodingPropertyName != null) {
			message.setStringProperty(this.encodingPropertyName, this.encoding);
		}
		return message;
	}
{

		throw new IllegalArgumentException("Unsupported message type [" + targetType +
				"]. MappingJackson2MessageConverter by default only supports TextMessages and BytesMessages.");
	}
{
		Assert.notNull(callback, "'callback' must not be null");
		synchronized (this.mutex) {
			switch (this.state) {
				case NEW:
					this.successCallbacks.add(callback);
					this.failureCallbacks.add(callback);
					break;
				case SUCCESS:
					callback.onSuccess((T) this.result);
					break;
				case FAILURE:
					callback.onFailure((Throwable) this.result);
					break;
			}
		}
	}
{
		Assert.notNull(callback, "'callback' must not be null");
		synchronized (this.mutex) {
			switch (this.state) {
				case NEW:
					this.successCallbacks.add(callback);
					this.failureCallbacks.add(callback);
					break;
				case SUCCESS:
					callback.onSuccess((T) this.result);
					break;
				case FAILURE:
					callback.onFailure((Throwable) this.result);
					break;
			}
		}
	}
{

		Mono<Object[]> argsPublisher = NO_ARGS;
		try {
			if (!ObjectUtils.isEmpty(getMethodParameters())) {
				List<Mono<Object>> publishers = resolveArguments(request, providedArgs);
				argsPublisher = Flux.zip(publishers, this::initArgs).next();
			}
		}
		catch (Throwable ex) {
			return Mono.error(ex);
		}

		return Flux.from(argsPublisher).concatMap(args -> {
			try {
				Object value = doInvoke(args);
				ResolvableType type =  ResolvableType.forMethodParameter(getReturnType());
				HandlerResult handlerResult = new HandlerResult(this, value, type);
				return Mono.just(handlerResult);
			}
			catch (InvocationTargetException ex) {
				return Mono.error(ex.getTargetException());
			}
			catch (Throwable ex) {
				String s = getInvocationErrorMessage(args);
				return Mono.error(new IllegalStateException(s));
			}
		}).next();
	}
{
		if (this.headers == null) {
			this.headers = new HttpHeaders();
			for (String name : this.request.getHeaderNames()) {
				for (String value : this.request.getAllHeaderValues(name)) {
					this.headers.add(name, value);
				}
			}
		}
		return this.headers;
	}
{
		if (this.headers == null) {
			this.headers = new HttpHeaders();
			for (HeaderValues headerValues : this.exchange.getRequestHeaders()) {
				for (String value : headerValues) {
					this.headers.add(headerValues.getHeaderName().toString(), value);
				}
			}
		}
		return this.headers;
	}
{
		ListenableFuture<ResponseEntity<String>> futureEntity =
				template.getForEntity(baseUrl + "/{method}", String.class, "get");
		futureEntity.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
			@Override
			public void onSuccess(ResponseEntity<String> entity) {
				assertEquals("Invalid content", helloWorld, entity.getBody());
				assertFalse("No headers", entity.getHeaders().isEmpty());
				assertEquals("Invalid content-type", textContentType, entity.getHeaders().getContentType());
				assertEquals("Invalid status code", HttpStatus.OK, entity.getStatusCode());
			}
			@Override
			public void onFailure(Throwable ex) {
				fail(ex.getMessage());
			}
		});
		// wait till done
		while (!futureEntity.isDone()) {
		}
	}
{

		String path = servletRequest.getRequestURI();  // shouldn't matter
		ServerEndpointRegistration endpointRegistration = new ServerEndpointRegistration(path, endpoint);
		endpointRegistration.setSubprotocols(Arrays.asList(selectedProtocol));
		endpointRegistration.setExtensions(selectedExtensions);

		EncodingFactory encodingFactory = new EncodingFactory(
				Collections.<Class<?>, List<InstanceFactory<? extends Encoder>>>emptyMap(),
				Collections.<Class<?>, List<InstanceFactory<? extends Decoder>>>emptyMap(),
				Collections.<Class<?>, List<InstanceFactory<? extends Encoder>>>emptyMap(),
				Collections.<Class<?>, List<InstanceFactory<? extends Decoder>>>emptyMap());
		try {
			return (endpointConstructorWithEndpointFactory ?
					endpointConstructor.newInstance(endpointRegistration,
							new EndpointInstanceFactory(endpoint), null, encodingFactory, null) :
					endpointConstructor.newInstance(endpointRegistration,
							new EndpointInstanceFactory(endpoint), null, encodingFactory));
		}
		catch (Exception ex) {
			throw new HandshakeFailureException("Failed to instantiate ConfiguredServerEndpoint", ex);
		}
	}
{
		Assert.notNull(name, "Property name must not be null");
		if (containsKey(name)) {
			return name;
		}

		String usName = name.replace('.', '_');
		if (!name.equals(usName) && containsKey(usName)) {
			return usName;
		}

		String ucName = name.toUpperCase();
		if (!name.equals(ucName)) {
			if (containsKey(ucName)) {
				return ucName;
			}
			else {
				String usUcName = ucName.replace('.', '_');
				if (!ucName.equals(usUcName) && containsKey(usUcName)) {
					return usUcName;
				}
			}
		}

		return name;
	}
{
		AnnotatedClass target = new AnnotatedClass();
		Method method = ReflectionUtils.findMethod(AnnotatedClass.class, "multipleCaching", Object.class,
				Object.class);
		Object[] args = new Object[] { new Object(), new Object() };
		Collection<ConcurrentMapCache> caches = Collections.singleton(new ConcurrentMapCache("test"));
		EvaluationContext context = eval.createEvaluationContext(caches, method, args, target, target.getClass(), result);
		return context;
	}
{
		if (defaultNamespaceUri.equals(namespaceUri)) {
			return Collections.singletonList(XMLConstants.DEFAULT_NS_PREFIX);
		}
		else if (XMLConstants.XML_NS_URI.equals(namespaceUri)) {
			return Collections.singletonList(XMLConstants.XML_NS_PREFIX);
		}
		else if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceUri)) {
			return Collections.singletonList(XMLConstants.XMLNS_ATTRIBUTE);
		}
		else {
			List<String> list = namespaceUriToPrefixes.get(namespaceUri);
			if (list == null) {
				list = new ArrayList<String>();
				namespaceUriToPrefixes.put(namespaceUri, list);
			}
			return list;
		}
	}
{
		if (this.jpaVendorAdapter != null) {
			if (this.persistenceProvider == null) {
				this.persistenceProvider = this.jpaVendorAdapter.getPersistenceProvider();
			}
			Map<String, ?> vendorPropertyMap = this.jpaVendorAdapter.getJpaPropertyMap();
			if (vendorPropertyMap != null) {
				for (Map.Entry<String, ?> entry : vendorPropertyMap.entrySet()) {
					if (!this.jpaPropertyMap.containsKey(entry.getKey())) {
						this.jpaPropertyMap.put(entry.getKey(), entry.getValue());
					}
				}
			}
			if (this.entityManagerFactoryInterface == null) {
				this.entityManagerFactoryInterface = this.jpaVendorAdapter.getEntityManagerFactoryInterface();
				if (!ClassUtils.isVisible(this.entityManagerFactoryInterface, this.beanClassLoader)) {
					this.entityManagerFactoryInterface = EntityManagerFactory.class;
				}
			}
			if (this.entityManagerInterface == null) {
				this.entityManagerInterface = this.jpaVendorAdapter.getEntityManagerInterface();
				if (!ClassUtils.isVisible(this.entityManagerInterface, this.beanClassLoader)) {
					this.entityManagerInterface = EntityManager.class;
				}
			}
			if (this.jpaDialect == null) {
				this.jpaDialect = this.jpaVendorAdapter.getJpaDialect();
			}
		}

		this.nativeEntityManagerFactory = createNativeEntityManagerFactory();
		if (this.nativeEntityManagerFactory == null) {
			throw new IllegalStateException(
					"JPA PersistenceProvider returned null EntityManagerFactory - check your JPA provider setup!");
		}
		if (this.jpaVendorAdapter != null) {
			this.jpaVendorAdapter.postProcessEntityManagerFactory(this.nativeEntityManagerFactory);
		}

		// Wrap the EntityManagerFactory in a factory implementing all its interfaces.
		// This allows interception of createEntityManager methods to return an
		// application-managed EntityManager proxy that automatically joins
		// existing transactions.
		this.entityManagerFactory = createEntityManagerFactoryProxy(this.nativeEntityManagerFactory);
	}
{
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
		dateFormat.setTimeZone(GMT);
		setHeaderValue(name, dateFormat.format(new Date(value)));
	}
{
		String acceptHeader = request.getHeaders().getFirst(HttpHeaders.ACCEPT);
		List<MediaType> mediaTypes = MediaType.parseMediaTypes(acceptHeader);
		MediaType.sortBySpecificityAndQuality(mediaTypes);
		return ( mediaTypes.size() > 0 ? mediaTypes.get(0) : MediaType.TEXT_PLAIN);
	}
{
		if (bean instanceof AopInfrastructureBean) {
			// Ignore AOP infrastructure such as scoped proxies.
			return bean;
		}

		if (bean instanceof Advised) {
			Advised advised = (Advised) bean;
			if (!advised.isFrozen() && isEligible(AopUtils.getTargetClass(bean))) {
				// Add our local Advisor to the existing proxy's Advisor chain...
				if (this.beforeExistingAdvisors) {
					advised.addAdvisor(0, this.advisor);
				}
				else {
					advised.addAdvisor(this.advisor);
				}
				return bean;
			}
		}

		if (isEligible(bean, beanName)) {
			ProxyFactory proxyFactory = new ProxyFactory();
			proxyFactory.copyFrom(this);
			proxyFactory.setTarget(bean);
			if (!proxyFactory.isProxyTargetClass()) {
				evaluateProxyInterfaces(bean.getClass(), proxyFactory);
			}
			proxyFactory.addAdvisor(this.advisor);
			return proxyFactory.getProxy(getProxyClassLoader());
		}

		// No async proxy needed.
		return bean;
	}
{
		List<Class<? extends Throwable>> result = new ArrayList<Class<? extends Throwable>>();
		MessageExceptionHandler annot = AnnotationUtils.findAnnotation(method, MessageExceptionHandler.class);
		result.addAll(Arrays.asList(annot.value()));
		if (result.isEmpty()) {
			result.addAll(getExceptionsFromMethodSignature(method));
		}
		return result;
	}
{

		String key = getMediaTypeKey(webRequest);
		if (StringUtils.hasText(key)) {
			MediaType mediaType = lookupMediaType(key);
			if (mediaType != null) {
				handleMatch(key, mediaType);
				return Collections.singletonList(mediaType);
			}
			mediaType = handleNoMatch(webRequest, key);
			if (mediaType != null) {
				addMapping(key, mediaType);
				return Collections.singletonList(mediaType);
			}
		}
		return Collections.emptyList();
	}
{
		if (!StringUtils.hasText(extension)) {
			return true;
		}
		extension = extension.toLowerCase(Locale.ENGLISH);
		if (this.safeExtensions.contains(extension)) {
			return true;
		}
		String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		if (pattern != null && pattern.endsWith("." + extension)) {
			return true;
		}
		if (extension.equals("html")) {
			String name = HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE;
			Set<MediaType> mediaTypes = (Set<MediaType>) request.getAttribute(name);
			if (!CollectionUtils.isEmpty(mediaTypes) && mediaTypes.contains(MediaType.TEXT_HTML)) {
				return true;
			}
		}
		return false;
	}
{
		long currentTime = new Date().getTime();
		long oneMinuteAgo  = currentTime - (1000 * 60);
		servletRequest.addHeader(HttpHeaders.IF_MODIFIED_SINCE, dateFormat.format(currentTime));
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setDate(HttpHeaders.LAST_MODIFIED, oneMinuteAgo);
		ResponseEntity<String> returnValue = new ResponseEntity<String>("body", responseHeaders, HttpStatus.OK);

		given(messageConverter.canWrite(String.class, null)).willReturn(true);
		given(messageConverter.getSupportedMediaTypes()).willReturn(Collections.singletonList(MediaType.TEXT_PLAIN));
		given(messageConverter.canWrite(String.class, MediaType.TEXT_PLAIN)).willReturn(true);

		processor.handleReturnValue(returnValue, returnTypeResponseEntity, mavContainer, webRequest);

		assertTrue(mavContainer.isRequestHandled());
		assertEquals(HttpStatus.NOT_MODIFIED.value(), servletResponse.getStatus());
		assertEquals(1, servletResponse.getHeaderValues(HttpHeaders.LAST_MODIFIED).size());
		assertEquals(dateFormat.format(oneMinuteAgo), servletResponse.getHeader(HttpHeaders.LAST_MODIFIED));
		assertEquals(0, servletResponse.getContentAsByteArray().length);
	}
{
		String wildcardValue = "*";
		String etagValue = "\"some-etag\"";
		servletRequest.setMethod("POST");
		servletRequest.addHeader(HttpHeaders.IF_NONE_MATCH, wildcardValue);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set(HttpHeaders.ETAG, etagValue);
		ResponseEntity<String> returnValue = new ResponseEntity<String>("body", responseHeaders, HttpStatus.OK);

		given(messageConverter.canWrite(String.class, null)).willReturn(true);
		given(messageConverter.getSupportedMediaTypes()).willReturn(Collections.singletonList(MediaType.TEXT_PLAIN));
		given(messageConverter.canWrite(String.class, MediaType.TEXT_PLAIN)).willReturn(true);


		processor.handleReturnValue(returnValue, returnTypeResponseEntity, mavContainer, webRequest);

		assertTrue(mavContainer.isRequestHandled());
		assertEquals(HttpStatus.OK.value(), servletResponse.getStatus());
		assertEquals(1, servletResponse.getHeaderValues(HttpHeaders.ETAG).size());
		assertEquals(etagValue, servletResponse.getHeader(HttpHeaders.ETAG));
		ArgumentCaptor<HttpOutputMessage> outputMessage = ArgumentCaptor.forClass(HttpOutputMessage.class);
		verify(messageConverter).write(eq("body"), eq(MediaType.TEXT_PLAIN),  outputMessage.capture());
	}
{
		if (this.variableResolver == null) {
			return (other == null);
		}
		if (other == null) {
			return false;
		}
		return ObjectUtils.nullSafeEquals(this.variableResolver.getSource(), other.getSource());
	}
{
		int hashCode = ObjectUtils.nullSafeHashCode(this.type);
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode(getSource());
		hashCode = 31 * hashCode + variableResolverSourceHashCode();
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode(this.componentType);
		return hashCode;
	}
{

		HttpServletRequest servletRequest = getHttpServletRequest(request);
		HttpServletResponse servletResponse = getHttpServletResponse(response);

		TyrusServerContainer serverContainer = (TyrusServerContainer) getContainer(servletRequest);
		TyrusWebSocketEngine engine = (TyrusWebSocketEngine) serverContainer.getWebSocketEngine();
		Object tyrusEndpoint = null;

		try {
			// Shouldn't matter for processing but must be unique
			String path = "/" + random.nextLong();
			tyrusEndpoint = createTyrusEndpoint(endpoint, path, selectedProtocol, extensions, serverContainer, engine);
			getEndpointHelper().register(engine, tyrusEndpoint);

			HttpHeaders headers = request.getHeaders();
			RequestContext requestContext = createRequestContext(servletRequest, path, headers);
			TyrusUpgradeResponse upgradeResponse = new TyrusUpgradeResponse();
			UpgradeInfo upgradeInfo = engine.upgrade(requestContext, upgradeResponse);

			switch (upgradeInfo.getStatus()) {
				case SUCCESS:
					if (logger.isTraceEnabled()) {
						logger.trace("Successful upgrade: " + upgradeResponse.getHeaders());
					}
					handleSuccess(servletRequest, servletResponse, upgradeInfo, upgradeResponse);
					break;
				case HANDSHAKE_FAILED:
					// Should never happen
					throw new HandshakeFailureException("Unexpected handshake failure: " + request.getURI());
				case NOT_APPLICABLE:
					// Should never happen
					throw new HandshakeFailureException("Unexpected handshake mapping failure: " + request.getURI());
			}
		}
		catch (Exception ex) {
			throw new HandshakeFailureException("Error during handshake: " + request.getURI(), ex);
		}
		finally {
			if (tyrusEndpoint != null) {
				getEndpointHelper().unregister(engine, tyrusEndpoint);
			}
		}
	}
{
		JSONAssert.assertEquals(expected, actual, false);
	}
{
		JSONAssert.assertNotEquals(expected, actual, false);
	}
{
			Buffer buffer = new Buffer();
			buffer.append(prev);
			if (count > 1) {
				buffer.append("]");
			}
			buffer.flip();
			subscriber.onNext(buffer.byteBuffer());
			subscriber.onComplete();
		}
{
		JsonpPollingTransportHandler transportHandler = new JsonpPollingTransportHandler();
		transportHandler.initialize(this.sockJsConfig);
		PollingSockJsSession session = transportHandler.createSession("1", this.webSocketHandler, null);

		transportHandler.handleRequest(this.request, this.response, this.webSocketHandler, session);

		assertEquals(500, this.servletResponse.getStatus());
		assertEquals("\"callback\" parameter required", this.servletResponse.getContentAsString());

		resetRequestAndResponse();
		setRequest("POST", "/");
		this.servletRequest.setQueryString("c=callback");
		this.servletRequest.addParameter("c", "callback");
		transportHandler.handleRequest(this.request, this.response, this.webSocketHandler, session);

		assertEquals("application/javascript;charset=UTF-8", this.response.getHeaders().getContentType().toString());
		assertFalse("Polling request should complete after open frame", this.servletRequest.isAsyncStarted());
		verify(this.webSocketHandler).afterConnectionEstablished(session);
	}
{
		Assert.state(getApplicationContext() != null,
				"injectDependencies() called without first configuring an ApplicationContext");
		if (isPopulateProtectedVariables()) {
			if (this.managedVariableNames == null) {
				initManagedVariableNames();
			}
			populateProtectedVariables();
		}
		getApplicationContext().getBeanFactory().autowireBeanProperties(this, getAutowireMode(), isDependencyCheck());
	}
{

		RestTemplate restTemplate = new RestTemplate();

		URI url = new URI("http://localhost:" + port + "/person");
		RequestEntity<Void> request = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
		ResponseEntity<Person> response = restTemplate.exchange(request, Person.class);

		assertEquals(new Person("Robert"), response.getBody());
	}
{

		RestTemplate restTemplate = new RestTemplate();

		URI url = new URI("http://localhost:" + port + "/list");
		RequestEntity<Void> request = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
		List<Person> results = restTemplate.exchange(request, new ParameterizedTypeReference<List<Person>>(){}).getBody();

		assertEquals(2, results.size());
		assertEquals(new Person("Robert"), results.get(0));
		assertEquals(new Person("Marie"), results.get(1));
	}
{
		Collection<? extends Cache> caches = loadCaches();

		// Preserve the initial order of the cache names
		this.cacheMap.clear();
		this.cacheNames.clear();
		for (Cache cache : caches) {
			addCache(cache);
		}
	}
{

		Object script;

		try {
			if (this.scriptEngine == null) {
				this.scriptEngine = retrieveScriptEngine(scriptSource);
				if (this.scriptEngine == null) {
					throw new IllegalStateException("Could not determine script engine for " + scriptSource);
				}
			}
			script = this.scriptEngine.eval(scriptSource.getScriptAsString());
		}
		catch (Exception ex) {
			throw new ScriptCompilationException(scriptSource, ex);
		}

		if (!ObjectUtils.isEmpty(actualInterfaces)) {
			boolean adaptationRequired = false;
			for (Class<?> requestedIfc : actualInterfaces) {
				if (!requestedIfc.isInstance(script)) {
					adaptationRequired = true;
				}
			}
			if (adaptationRequired) {
				Class<?> adaptedIfc;
				if (actualInterfaces.length == 1) {
					adaptedIfc = actualInterfaces[0];
				}
				else {
					adaptedIfc = ClassUtils.createCompositeInterface(actualInterfaces, this.beanClassLoader);
				}
				if (adaptedIfc != null) {
					if (!(this.scriptEngine instanceof Invocable)) {
						throw new ScriptCompilationException(scriptSource,
								"ScriptEngine must implement Invocable in order to adapt it to an interface: " +
										this.scriptEngine);
					}
					Invocable invocable = (Invocable) this.scriptEngine;
					if (script != null) {
						script = invocable.getInterface(script, adaptedIfc);
					}
					if (script == null) {
						script = invocable.getInterface(adaptedIfc);
						if (script == null) {
							throw new ScriptCompilationException(scriptSource,
									"Could not adapt script to interface [" + adaptedIfc.getName() + "]");
						}
					}
				}
			}
		}

		if (script instanceof Class) {
			Class<?> scriptClass = (Class<?>) script;
			try {
				return scriptClass.newInstance();
			}
			catch (InstantiationException ex) {
				throw new ScriptCompilationException(
						scriptSource, "Could not instantiate script class: " + scriptClass.getName(), ex);
			}
			catch (IllegalAccessException ex) {
				throw new ScriptCompilationException(
						scriptSource, "Could not access script constructor: " + scriptClass.getName(), ex);
			}
		}

		return script;
	}
{

		Object script;

		try {
			if (this.scriptEngine == null) {
				this.scriptEngine = retrieveScriptEngine(scriptSource);
				if (this.scriptEngine == null) {
					throw new IllegalStateException("Could not determine script engine for " + scriptSource);
				}
			}
			script = this.scriptEngine.eval(scriptSource.getScriptAsString());
		}
		catch (Exception ex) {
			throw new ScriptCompilationException(scriptSource, ex);
		}

		if (!ObjectUtils.isEmpty(actualInterfaces)) {
			boolean adaptationRequired = false;
			for (Class<?> requestedIfc : actualInterfaces) {
				if (!requestedIfc.isInstance(script)) {
					adaptationRequired = true;
				}
			}
			if (adaptationRequired) {
				Class<?> adaptedIfc;
				if (actualInterfaces.length == 1) {
					adaptedIfc = actualInterfaces[0];
				}
				else {
					adaptedIfc = ClassUtils.createCompositeInterface(actualInterfaces, this.beanClassLoader);
				}
				if (adaptedIfc != null) {
					if (!(this.scriptEngine instanceof Invocable)) {
						throw new ScriptCompilationException(scriptSource,
								"ScriptEngine must implement Invocable in order to adapt it to an interface: " +
										this.scriptEngine);
					}
					Invocable invocable = (Invocable) this.scriptEngine;
					if (script != null) {
						script = invocable.getInterface(script, adaptedIfc);
					}
					if (script == null) {
						script = invocable.getInterface(adaptedIfc);
						if (script == null) {
							throw new ScriptCompilationException(scriptSource,
									"Could not adapt script to interface [" + adaptedIfc.getName() + "]");
						}
					}
				}
			}
		}

		if (script instanceof Class) {
			Class<?> scriptClass = (Class<?>) script;
			try {
				return scriptClass.newInstance();
			}
			catch (InstantiationException ex) {
				throw new ScriptCompilationException(
						scriptSource, "Could not instantiate script class: " + scriptClass.getName(), ex);
			}
			catch (IllegalAccessException ex) {
				throw new ScriptCompilationException(
						scriptSource, "Could not access script constructor: " + scriptClass.getName(), ex);
			}
		}

		return script;
	}
{
		AnnotationCacheKey cacheKey = new AnnotationCacheKey(clazz, annotationType);
		A result = (A) findAnnotationCache.get(cacheKey);
		if (result == null) {
			result = findAnnotation(clazz, annotationType, new HashSet<Annotation>());
			if (result != null) {
				findAnnotationCache.put(cacheKey, result);
			}
		}
		return synthesizeAnnotation(result, clazz);
	}
{
		if (contentDisposition == null) {
			return null;
		}
		// TODO: can only handle the typical case at the moment
		int startIndex = contentDisposition.indexOf(FILENAME_KEY);
		if (startIndex == -1) {
			return null;
		}
		String filename = contentDisposition.substring(startIndex + FILENAME_KEY.length());
		if (filename.startsWith("\"")) {
			int endIndex = filename.indexOf("\"", 1);
			if (endIndex != -1) {
				return filename.substring(1, endIndex);
			}
		}
		else {
			int endIndex = filename.indexOf(";");
			if (endIndex != -1) {
				return filename.substring(0, endIndex);
			}
		}
		return filename;
	}
{
		Assert.state(this.bindingResult == null,
				"DataBinder is already initialized - call initBeanPropertyAccess before other configuration methods");
		this.bindingResult = new BeanPropertyBindingResult(
				getTarget(), getObjectName(), isAutoGrowNestedPaths(), getAutoGrowCollectionLimit());
		if (this.conversionService != null) {
			this.bindingResult.initConversion(this.conversionService);
		}
	}
{
		Assert.state(this.bindingResult == null,
				"DataBinder is already initialized - call initDirectFieldAccess before other configuration methods");
		this.bindingResult = new DirectFieldBindingResult(getTarget(), getObjectName(), isAutoGrowNestedPaths());
		if (this.conversionService != null) {
			this.bindingResult.initConversion(this.conversionService);
		}
	}
{
		new JsonPathRequestMatchers("$.bar").isArray().match(this.request);
	}
{
		ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(EnableCachingConfig.class);
		jCacheManager = context.getBean("jCacheManager", CacheManager.class);
		return context;
	}
{
		Object value = evaluateJsonPath(content);
		String reason = "No value for JSON path " + this.expression;
		assertTrue(reason, value != null);
		if (List.class.isInstance(value)) {
			assertTrue(reason, !((List<?>) value).isEmpty());
		}
	}
{

		ServletInvocableHandlerMethod requestMethod;
		requestMethod = new ServletInvocableHandlerMethod(handlerMethod);
		requestMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
		requestMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
		requestMethod.setDataBinderFactory(binderFactory);
		requestMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
		return requestMethod;
	}
{
		if (!canConvertFrom(message, targetClass)) {
			return null;
		}
		return convertFromInternal(message, targetClass);
	}
{
		if (!canConvertTo(payload, headers)) {
			return null;
		}

		payload = convertToInternal(payload, headers);
		MimeType mimeType = getDefaultContentType(payload);

		if (headers != null) {
			MessageHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(headers, MessageHeaderAccessor.class);
			if (accessor != null && accessor.isMutable()) {
				accessor.setHeaderIfAbsent(MessageHeaders.CONTENT_TYPE, mimeType);
				return MessageBuilder.createMessage(payload, accessor.getMessageHeaders());
			}
		}

		MessageBuilder<?> builder = MessageBuilder.withPayload(payload);
		if (headers != null) {
			builder.copyHeaders(headers);
		}
		builder.setHeaderIfAbsent(MessageHeaders.CONTENT_TYPE, mimeType);
		return builder.build();
	}
{
		MethodParameter returnType = (headers != null ?
				(MethodParameter) headers.get(METHOD_PARAMETER_HINT_HEADER) : null);
		if (returnType == null) {
			return null;
		}
		JsonView annotation = returnType.getMethodAnnotation(JsonView.class);
		if (annotation == null) {
			return null;
		}
		Class<?>[] classes = annotation.value();
		if (classes.length != 1) {
			throw new IllegalArgumentException(
					"@JsonView only supported for handler methods with exactly 1 class argument: " + returnType);
		}
		return classes[0];
	}
{
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		assertThat(requestAttributes, instanceOf(ServletRequestAttributes.class));
		assertRequestAttributes(((ServletRequestAttributes) requestAttributes).getRequest());
	}
{
		assertThat(request.getAttribute(FROM_TCF_MOCK), is(FROM_TCF_MOCK));
		assertThat(request.getAttribute(FROM_MVC_TEST_DEFAULT), is(FROM_MVC_TEST_DEFAULT));
		assertThat(request.getAttribute(FROM_MVC_TEST_MOCK), is(FROM_MVC_TEST_MOCK));
		assertThat(request.getAttribute(FROM_REQUEST_FILTER), is(FROM_REQUEST_FILTER));
		assertThat(request.getAttribute(FROM_REQUEST_ATTRIBUTES_FILTER), is(FROM_REQUEST_ATTRIBUTES_FILTER));
	}
{
		Assert.notNull(request, "Request must not be null");
		Assert.notNull(allowedOrigins, "Allowed origins must not be null");

		String origin = request.getHeaders().getOrigin();
		if (origin == null || allowedOrigins.contains("*")) {
			return true;
		}
		else if (CollectionUtils.isEmpty(allowedOrigins)) {
			UriComponents actualUrl = UriComponentsBuilder.fromHttpRequest(request).build();
			UriComponents originUrl = UriComponentsBuilder.fromOriginHeader(origin).build();
			return (actualUrl.getHost().equals(originUrl.getHost()) && getPort(actualUrl) == getPort(originUrl));
		}
		else {
			return allowedOrigins.contains(origin);
		}
	}
{
		if (!ApplicationEvent.class.isAssignableFrom(this.declaredEventType.getRawClass())
				&& event instanceof PayloadApplicationEvent) {
			PayloadApplicationEvent<?> payloadEvent = (PayloadApplicationEvent<?>) event;
			ResolvableType payloadType =  payloadEvent.getResolvableType()
					.as(PayloadApplicationEvent.class).getGeneric(0);
			if (this.declaredEventType.isAssignableFrom(payloadType)) {
				return new Object[] {payloadEvent.getPayload()};
			}
		}
		else {
			return new Object[] {event};
		}
		return null;
	}
{

		// Check whether we should support the request method.
		String method = request.getMethod();
		if (this.supportedMethods != null && !this.supportedMethods.contains(method)) {
			throw new HttpRequestMethodNotSupportedException(
					method, StringUtils.toStringArray(this.supportedMethods));
		}

		// Check whether a session is required.
		if (this.requireSession) {
			if (request.getSession(false) == null) {
				throw new HttpSessionRequiredException("Pre-existing session required but none found");
			}
		}

		if (this.usePreviousHttpCachingBehavior) {
			addHttp10CacheHeaders(response);
		}
		else if (cacheControl != null) {
			String ccValue = cacheControl.getHeaderValue();
			if (ccValue != null) {
				response.setHeader(HEADER_CACHE_CONTROL, ccValue);
			}
		}
	}
{
		TestContextManager testContextManager = new TestContextManager(ExplicitListenersTestCase.class);
		assertEquals("Num registered TELs for ExplicitListenersTestCase.", 3,
			testContextManager.getTestExecutionListeners().size());
	}
{
		if (this.content.size() > 0) {
			HttpServletResponse rawResponse = (HttpServletResponse) getResponse();
			if(! rawResponse.isCommitted()){
				rawResponse.setContentLength(this.content.size());
			}
			this.content.writeTo(rawResponse.getOutputStream());
			this.content.reset();
		}
	}
{

		Class<?> returnValueClass = getReturnValueType(returnValue, returnType);
		HttpServletRequest servletRequest = inputMessage.getServletRequest();
		List<MediaType> requestedMediaTypes = getAcceptableMediaTypes(servletRequest);
		List<MediaType> producibleMediaTypes = getProducibleMediaTypes(servletRequest, returnValueClass);

		Assert.isTrue(returnValue == null || !producibleMediaTypes.isEmpty(),
				"No converter found for return value of type: " + returnValueClass);

		Set<MediaType> compatibleMediaTypes = new LinkedHashSet<MediaType>();
		for (MediaType requestedType : requestedMediaTypes) {
			for (MediaType producibleType : producibleMediaTypes) {
				if (requestedType.isCompatibleWith(producibleType)) {
					compatibleMediaTypes.add(getMostSpecificMediaType(requestedType, producibleType));
				}
			}
		}
		if (compatibleMediaTypes.isEmpty()) {
			if (returnValue != null) {
				throw new HttpMediaTypeNotAcceptableException(producibleMediaTypes);
			}
			return;
		}

		List<MediaType> mediaTypes = new ArrayList<MediaType>(compatibleMediaTypes);
		MediaType.sortBySpecificityAndQuality(mediaTypes);

		MediaType selectedMediaType = null;
		for (MediaType mediaType : mediaTypes) {
			if (mediaType.isConcrete()) {
				selectedMediaType = mediaType;
				break;
			}
			else if (mediaType.equals(MediaType.ALL) || mediaType.equals(MEDIA_TYPE_APPLICATION)) {
				selectedMediaType = MediaType.APPLICATION_OCTET_STREAM;
				break;
			}
		}

		if (selectedMediaType != null) {
			selectedMediaType = selectedMediaType.removeQualityValue();
			for (HttpMessageConverter<?> messageConverter : this.messageConverters) {
				if (messageConverter.canWrite(returnValueClass, selectedMediaType)) {
					returnValue = (T) getAdvice().beforeBodyWrite(returnValue, returnType, selectedMediaType,
							(Class<? extends HttpMessageConverter<?>>) messageConverter.getClass(),
							inputMessage, outputMessage);
					if (returnValue != null) {
						if (messageConverter instanceof GenericHttpMessageConverter) {
							Type type;
							if (HttpEntity.class.isAssignableFrom(returnType.getParameterType())) {
								returnType.increaseNestingLevel();
								type = returnType.getNestedGenericParameterType();
							}
							else {
								type = returnType.getGenericParameterType();
							}
							((GenericHttpMessageConverter<T>) messageConverter).write(returnValue, type, selectedMediaType, outputMessage);
						}
						else {
							((HttpMessageConverter<T>) messageConverter).write(returnValue, selectedMediaType, outputMessage);
						}
						if (logger.isDebugEnabled()) {
							logger.debug("Written [" + returnValue + "] as \"" +
									selectedMediaType + "\" using [" + messageConverter + "]");
						}
					}
					return;
				}
			}
		}

		if (returnValue != null) {
			throw new HttpMediaTypeNotAcceptableException(this.allSupportedMediaTypes);
		}
	}
{
		Set<MediaType> mediaTypes = (Set<MediaType>) request.getAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
		if (!CollectionUtils.isEmpty(mediaTypes)) {
			return new ArrayList<MediaType>(mediaTypes);
		}
		else if (!this.allSupportedMediaTypes.isEmpty()) {
			List<MediaType> result = new ArrayList<MediaType>();
			for (HttpMessageConverter<?> converter : this.messageConverters) {
				if (converter.canWrite(returnValueClass, null)) {
					result.addAll(converter.getSupportedMediaTypes());
				}
			}
			return result;
		}
		else {
			return Collections.singletonList(MediaType.ALL);
		}
	}
{
		List<Resource> list = new ArrayList<Resource>();
		for (String path : paths) {
			list.add(resourceLoader.getResource(path));
		}
		return list.toArray(new Resource[list.size()]);
	}
{

		final HttpHeaders headers = outputMessage.getHeaders();
		if (headers.getContentType() == null) {
			MediaType contentTypeToUse = contentType;
			if (contentType == null || contentType.isWildcardType() || contentType.isWildcardSubtype()) {
				contentTypeToUse = getDefaultContentType(t);
			}
			else if (MediaType.APPLICATION_OCTET_STREAM.equals(contentType)) {
				MediaType type = getDefaultContentType(t);
				contentTypeToUse = (type != null ? type : contentTypeToUse);
			}
			if (contentTypeToUse != null) {
				headers.setContentType(contentTypeToUse);
			}
		}
		if (headers.getContentLength() == -1) {
			Long contentLength = getContentLength(t, headers.getContentType());
			if (contentLength != null) {
				headers.setContentLength(contentLength);
			}
		}

		if (outputMessage instanceof StreamingHttpOutputMessage) {
			StreamingHttpOutputMessage streamingOutputMessage =
					(StreamingHttpOutputMessage) outputMessage;
			streamingOutputMessage.setBody(new StreamingHttpOutputMessage.Body() {
				@Override
				public void writeTo(final OutputStream outputStream) throws IOException {
					writeInternal(t, new HttpOutputMessage() {
						@Override
						public OutputStream getBody() throws IOException {
							return outputStream;
						}
						@Override
						public HttpHeaders getHeaders() {
							return headers;
						}
					});
				}
			});
		}
		else {
			writeInternal(t, outputMessage);
			outputMessage.getBody().flush();
		}
	}
{
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(startsWith("Attributes map"));
		exception.expectMessage(containsString("returned null for required attribute [value]"));
		exception.expectMessage(containsString("defined by annotation type [" + Component.class.getName() + "]"));
		synthesizeAnnotation(new HashMap<String, Object>(), Component.class, null);
	}
{

		try {
			return searchWithFindSemantics(element, annotationName, searchOnInterfaces, searchOnSuperclasses,
				searchOnMethodsInInterfaces, searchOnMethodsInSuperclasses, processor, new HashSet<AnnotatedElement>(), 0);
		}
		catch (Throwable ex) {
			AnnotationUtils.rethrowAnnotationConfigurationException(ex);
			throw new IllegalStateException("Failed to introspect annotations on " + element, ex);
		}
	}
{

		Assert.hasText(attributeName, "attributeName must not be null or empty");
		Assert.notNull(annotationType, "annotationType must not be null");

		String[] attributeValue = getStringArrayWithoutNullCheck(attributeName);
		String aliasName = AnnotationUtils.getAttributeAliasMap(annotationType).get(attributeName);
		String[] aliasValue = getStringArrayWithoutNullCheck(aliasName);
		boolean attributeDeclared = !ObjectUtils.isEmpty(attributeValue);
		boolean aliasDeclared = !ObjectUtils.isEmpty(aliasValue);

		if (!ObjectUtils.nullSafeEquals(attributeValue, aliasValue) && attributeDeclared && aliasDeclared) {
			String elementName = (annotationSource == null ? "unknown element" : annotationSource.toString());
			String msg = String.format("In annotation [%s] declared on [%s], "
					+ "attribute [%s] and its alias [%s] are present with values of [%s] and [%s], "
					+ "but only one is permitted.", this.displayName, elementName, attributeName, aliasName,
				ObjectUtils.nullSafeToString(attributeValue), ObjectUtils.nullSafeToString(aliasValue));
			throw new AnnotationConfigurationException(msg);
		}

		if (!attributeDeclared) {
			attributeValue = aliasValue;
		}

		assertAttributePresence(attributeName, aliasName, attributeValue);

		return attributeValue;
	}
{
		Assert.hasText(attributeName, "attributeName must not be null or empty");
		Object value = get(attributeName);
		if (value == null) {
			throw new IllegalArgumentException(String.format(
				"Attribute '%s' not found in attributes for annotation [%s]", attributeName, this.displayName));
		}
		if (!expectedType.isInstance(value)) {
			if (expectedType.isArray() && expectedType.getComponentType().isInstance(value)) {
				Object array = Array.newInstance(expectedType.getComponentType(), 1);
				Array.set(array, 0, value);
				value = array;
			}
			else {
				throw new IllegalArgumentException(String.format(
					"Attribute '%s' is of type [%s], but [%s] was expected in attributes for annotation [%s]",
					attributeName, value.getClass().getSimpleName(), expectedType.getSimpleName(), this.displayName));
			}
		}
		return (T) value;
	}
{
		Assert.hasText(attributeName, "attributeName must not be null or empty");
		Object value = get(attributeName);
		if (value == null) {
			throw new IllegalArgumentException(String.format(
				"Attribute '%s' not found in attributes for annotation [%s]", attributeName, this.displayName));
		}
		if (!expectedType.isInstance(value)) {
			if (expectedType.isArray() && expectedType.getComponentType().isInstance(value)) {
				Object array = Array.newInstance(expectedType.getComponentType(), 1);
				Array.set(array, 0, value);
				value = array;
			}
			else {
				throw new IllegalArgumentException(String.format(
					"Attribute '%s' is of type [%s], but [%s] was expected in attributes for annotation [%s]",
					attributeName, value.getClass().getSimpleName(), expectedType.getSimpleName(), this.displayName));
			}
		}
		return (T) value;
	}
{
		if (isEqualsMethod(method)) {
			return equals(proxy, args[0]);
		}
		if (isHashCodeMethod(method)) {
			return hashCode(proxy);
		}
		if (isToStringMethod(method)) {
			return toString(proxy);
		}

		String methodName = method.getName();
		Class<?> returnType = method.getReturnType();
		boolean nestedAnnotation = (Annotation[].class.isAssignableFrom(returnType) || Annotation.class.isAssignableFrom(returnType));
		String aliasedAttributeName = aliasMap.get(methodName);
		boolean aliasPresent = (aliasedAttributeName != null);

		makeAccessible(method);

		// No custom processing necessary?
		if (!aliasPresent && !nestedAnnotation) {
			return invokeMethod(method, this.annotation, args);
		}

		Object cachedValue = this.computedValueCache.get(methodName);
		if (cachedValue != null) {
			return cachedValue;
		}

		Object value = invokeMethod(method, this.annotation, args);

		if (aliasPresent) {
			Method aliasedMethod = null;
			try {
				aliasedMethod = this.annotationType.getDeclaredMethod(aliasedAttributeName);
			}
			catch (NoSuchMethodException e) {
				String msg = String.format("In annotation [%s], attribute [%s] is declared as an @AliasFor [%s], "
						+ "but attribute [%s] does not exist.", this.annotationType.getName(), methodName,
					aliasedAttributeName, aliasedAttributeName);
				throw new AnnotationConfigurationException(msg);
			}

			makeAccessible(aliasedMethod);
			Object aliasedValue = invokeMethod(aliasedMethod, this.annotation);
			Object defaultValue = getDefaultValue(this.annotation, methodName);

			if (!nullSafeEquals(value, aliasedValue) && !nullSafeEquals(value, defaultValue)
					&& !nullSafeEquals(aliasedValue, defaultValue)) {
				String elementName = (this.annotatedElement == null ? "unknown element"
						: this.annotatedElement.toString());
				String msg = String.format(
					"In annotation [%s] declared on [%s], attribute [%s] and its alias [%s] are "
							+ "declared with values of [%s] and [%s], but only one declaration is permitted.",
					this.annotationType.getName(), elementName, methodName, aliasedAttributeName,
					nullSafeToString(value), nullSafeToString(aliasedValue));
				throw new AnnotationConfigurationException(msg);
			}

			// If the user didn't declare the annotation with an explicit value, return
			// the value of the alias.
			if (nullSafeEquals(value, defaultValue)) {
				value = aliasedValue;
			}
		}

		// Synthesize nested annotations before returning them.
		if (value instanceof Annotation) {
			value = synthesizeAnnotation((Annotation) value, this.annotatedElement);
		}
		else if (value instanceof Annotation[]) {
			Annotation[] annotations = (Annotation[]) value;
			for (int i = 0; i < annotations.length; i++) {
				annotations[i] = synthesizeAnnotation(annotations[i], this.annotatedElement);
			}
		}

		this.computedValueCache.put(methodName, value);

		return value;
	}
{
			annotation = AnnotationUtils.synthesizeAnnotation(annotation, element);
			Class<? extends Annotation> targetAnnotationType = attributes.annotationType();

			for (Method attributeMethod : AnnotationUtils.getAttributeMethods(annotation.annotationType())) {
				String attributeName = attributeMethod.getName();
				String aliasedAttributeName = AnnotationUtils.getAliasedAttributeName(attributeMethod,
					targetAnnotationType);

				// Explicit annotation attribute override declared via @AliasFor
				if (StringUtils.hasText(aliasedAttributeName)) {
					if (attributes.containsKey(aliasedAttributeName)) {
						Object value = AnnotationUtils.getValue(annotation, attributeName);
						attributes.put(aliasedAttributeName, AnnotationUtils.adaptValue(element, value,
							this.classValuesAsString, this.nestedAnnotationsAsMap));
					}
				}
				// Implicit annotation attribute override based on convention
				else if (!AnnotationUtils.VALUE.equals(attributeName) && attributes.containsKey(attributeName)) {
					Object value = AnnotationUtils.getValue(annotation, attributeName);
					Object adaptedValue = AnnotationUtils.adaptValue(element, value, this.classValuesAsString,
						this.nestedAnnotationsAsMap);
					attributes.put(attributeName, adaptedValue);

					// If an aliased attribute defined by @AliasFor semantics does not
					// already have an explicit value, ensure that the aliased attribute
					// is also present in the map with a value identical to its mirror
					// alias.
					Method attributeMethodInTarget = ReflectionUtils.findMethod(targetAnnotationType, attributeName);
					if (attributeMethodInTarget != null) {
						String aliasedAttributeNameInTarget = AnnotationUtils.getAliasedAttributeName(
							attributeMethodInTarget, null);
						if (aliasedAttributeNameInTarget != null) {
							attributes.putIfAbsent(aliasedAttributeNameInTarget, adaptedValue);
						}
					}
				}
			}
		}
{
		try {
			A ann = annotatedElement.getAnnotation(annotationType);
			if (ann == null) {
				for (Annotation metaAnn : annotatedElement.getAnnotations()) {
					ann = metaAnn.annotationType().getAnnotation(annotationType);
					if (ann != null) {
						break;
					}
				}
			}
			return ann;
		}
		catch (Exception ex) {
			// Assuming nested Class values not resolvable within annotation attributes...
			logIntrospectionFailure(annotatedElement, ex);
			return null;
		}
	}
{

		String path = chain.resolveUrlPath(resourceUrlPath, locations);
		if (path == null) {
			try {
				int startOffset = resourceUrlPath.startsWith("/") ? 1 : 0;
				int endOffset = resourceUrlPath.indexOf("/", 1);
				if (endOffset != -1) {
					String webjar = resourceUrlPath.substring(startOffset, endOffset);
					String partialPath = resourceUrlPath.substring(endOffset);
					String webJarPath = webJarAssetLocator.getFullPath(webjar, partialPath);
					return chain.resolveUrlPath(webJarPath.substring(WEBJARS_LOCATION_LENGTH), locations);
				}
			}
			catch (MultipleMatchesException ex) {
				logger.warn("WebJar version conflict for \"" + resourceUrlPath + "\"", ex);
			}
			catch (IllegalArgumentException ex) {
				if (logger.isTraceEnabled()) {
					logger.trace("No WebJar resource found for \"" + resourceUrlPath + "\"");
				}
			}
		}
		return path;
	}
{
		baseUrl = getBaseUrlToUse(baseUrl);
		String typePath = getTypeRequestMapping(method.getDeclaringClass());
		String methodPath = getMethodRequestMapping(method);
		String path = pathMatcher.combine(typePath, methodPath);
		baseUrl.path(path);
		UriComponents uriComponents = applyContributors(baseUrl, method, args);
		return UriComponentsBuilder.newInstance().uriComponents(uriComponents);
	}
{
		if (!(message.getPayload() instanceof byte[])) {
			logger.error("Expected byte[] payload. Ignoring " + message + ".");
			return;
		}
		StompHeaderAccessor stompAccessor = getStompHeaderAccessor(message);
		StompCommand command = stompAccessor.getCommand();
		if (StompCommand.MESSAGE.equals(command)) {
			if (stompAccessor.getSubscriptionId() == null) {
				logger.warn("No STOMP \"subscription\" header in " + message);
			}
			String origDestination = stompAccessor.getFirstNativeHeader(SimpMessageHeaderAccessor.ORIGINAL_DESTINATION);
			if (origDestination != null) {
				stompAccessor = toMutableAccessor(stompAccessor, message);
				stompAccessor.removeNativeHeader(SimpMessageHeaderAccessor.ORIGINAL_DESTINATION);
				stompAccessor.setDestination(origDestination);
			}
		}
		else if (StompCommand.CONNECTED.equals(command)) {
			this.stats.incrementConnectedCount();
			stompAccessor = afterStompSessionConnected(message, stompAccessor, session);
			if (this.eventPublisher != null && StompCommand.CONNECTED.equals(command)) {
				try {
					SimpAttributes simpAttributes = new SimpAttributes(session.getId(), session.getAttributes());
					SimpAttributesContextHolder.setAttributes(simpAttributes);
					Principal user = session.getPrincipal();
					publishEvent(new SessionConnectedEvent(this, (Message<byte[]>) message, user));
				}
				finally {
					SimpAttributesContextHolder.resetAttributes();
				}
			}
		}
		try {
			byte[] payload = (byte[]) message.getPayload();
			byte[] bytes = this.stompEncoder.encode(stompAccessor.getMessageHeaders(), payload);

			boolean useBinary = (payload.length > 0 && !(session instanceof SockJsSession) &&
					MimeTypeUtils.APPLICATION_OCTET_STREAM.isCompatibleWith(stompAccessor.getContentType()));

			if (useBinary) {
				session.sendMessage(new BinaryMessage(bytes));
			}
			else {
				session.sendMessage(new TextMessage(bytes));
			}
		}
		catch (SessionLimitExceededException ex) {
			// Bad session, just get out
			throw ex;
		}
		catch (Throwable ex) {
			// Could be part of normal workflow (e.g. browser tab closed)
			logger.debug("Failed to send WebSocket message to client in session " + session.getId() + ".", ex);
			command = StompCommand.ERROR;
		}
		finally {
			if (StompCommand.ERROR.equals(command)) {
				try {
					session.close(CloseStatus.PROTOCOL_ERROR);
				}
				catch (IOException ex) {
					// Ignore
				}
			}
		}
	}
{

		List<Message<byte[]>> messages;
		try {
			ByteBuffer byteBuffer;
			if (webSocketMessage instanceof TextMessage) {
				byteBuffer = ByteBuffer.wrap(((TextMessage) webSocketMessage).asBytes());
			}
			else if (webSocketMessage instanceof BinaryMessage) {
				byteBuffer = ((BinaryMessage) webSocketMessage).getPayload();
			}
			else {
				return;
			}

			BufferingStompDecoder decoder = this.decoders.get(session.getId());
			if (decoder == null) {
				throw new IllegalStateException("No decoder for session id '" + session.getId() + "'");
			}

			messages = decoder.decode(byteBuffer);
			if (messages.isEmpty()) {
				if (logger.isTraceEnabled()) {
					logger.trace("Incomplete STOMP frame content received in session " +
							session + ", bufferSize=" + decoder.getBufferSize() +
							", bufferSizeLimit=" + decoder.getBufferSizeLimit() + ".");
				}
				return;
			}
		}
		catch (Throwable ex) {
			if (logger.isErrorEnabled()) {
				logger.error("Failed to parse " + webSocketMessage +
						" in session " + session.getId() + ". Sending STOMP ERROR to client.", ex);
			}
			sendErrorMessage(session, ex);
			return;
		}

		for (Message<byte[]> message : messages) {
			try {
				StompHeaderAccessor headerAccessor =
						MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

				Principal user = session.getPrincipal();

				headerAccessor.setSessionId(session.getId());
				headerAccessor.setSessionAttributes(session.getAttributes());
				headerAccessor.setUser(user);
				headerAccessor.setHeader(SimpMessageHeaderAccessor.HEART_BEAT_HEADER, headerAccessor.getHeartbeat());
				if (!detectImmutableMessageInterceptor(outputChannel)) {
					headerAccessor.setImmutable();
				}

				if (logger.isTraceEnabled()) {
					logger.trace("From client: " + headerAccessor.getShortLogMessage(message.getPayload()));
				}

				if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
					this.stats.incrementConnectCount();
				}
				else if (StompCommand.DISCONNECT.equals(headerAccessor.getCommand())) {
					this.stats.incrementDisconnectCount();
				}

				try {
					SimpAttributesContextHolder.setAttributesFromMessage(message);
					if (this.eventPublisher != null) {
						if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
							publishEvent(new SessionConnectEvent(this, message, user));
						}
						else if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
							publishEvent(new SessionSubscribeEvent(this, message, user));
						}
						else if (StompCommand.UNSUBSCRIBE.equals(headerAccessor.getCommand())) {
							publishEvent(new SessionUnsubscribeEvent(this, message, user));
						}
					}
					outputChannel.send(message);
				}
				finally {
					SimpAttributesContextHolder.resetAttributes();
				}
			}
			catch (Throwable ex) {
				logger.error("Failed to send client message to application via MessageChannel" +
						" in session " + session.getId() + ". Sending STOMP ERROR to client.", ex);
				sendErrorMessage(session, ex);

			}
		}
	}
{
		if (!(message.getPayload() instanceof byte[])) {
			logger.error("Expected byte[] payload. Ignoring " + message + ".");
			return;
		}
		StompHeaderAccessor stompAccessor = getStompHeaderAccessor(message);
		StompCommand command = stompAccessor.getCommand();
		if (StompCommand.MESSAGE.equals(command)) {
			if (stompAccessor.getSubscriptionId() == null) {
				logger.warn("No STOMP \"subscription\" header in " + message);
			}
			String origDestination = stompAccessor.getFirstNativeHeader(SimpMessageHeaderAccessor.ORIGINAL_DESTINATION);
			if (origDestination != null) {
				stompAccessor = toMutableAccessor(stompAccessor, message);
				stompAccessor.removeNativeHeader(SimpMessageHeaderAccessor.ORIGINAL_DESTINATION);
				stompAccessor.setDestination(origDestination);
			}
		}
		else if (StompCommand.CONNECTED.equals(command)) {
			this.stats.incrementConnectedCount();
			stompAccessor = afterStompSessionConnected(message, stompAccessor, session);
			if (this.eventPublisher != null && StompCommand.CONNECTED.equals(command)) {
				try {
					SimpAttributes simpAttributes = new SimpAttributes(session.getId(), session.getAttributes());
					SimpAttributesContextHolder.setAttributes(simpAttributes);
					Principal user = session.getPrincipal();
					publishEvent(new SessionConnectedEvent(this, (Message<byte[]>) message, user));
				}
				finally {
					SimpAttributesContextHolder.resetAttributes();
				}
			}
		}
		try {
			byte[] payload = (byte[]) message.getPayload();
			byte[] bytes = this.stompEncoder.encode(stompAccessor.getMessageHeaders(), payload);

			boolean useBinary = (payload.length > 0 && !(session instanceof SockJsSession) &&
					MimeTypeUtils.APPLICATION_OCTET_STREAM.isCompatibleWith(stompAccessor.getContentType()));

			if (useBinary) {
				session.sendMessage(new BinaryMessage(bytes));
			}
			else {
				session.sendMessage(new TextMessage(bytes));
			}
		}
		catch (SessionLimitExceededException ex) {
			// Bad session, just get out
			throw ex;
		}
		catch (Throwable ex) {
			// Could be part of normal workflow (e.g. browser tab closed)
			logger.debug("Failed to send WebSocket message to client in session " + session.getId() + ".", ex);
			command = StompCommand.ERROR;
		}
		finally {
			if (StompCommand.ERROR.equals(command)) {
				try {
					session.close(CloseStatus.PROTOCOL_ERROR);
				}
				catch (IOException ex) {
					// Ignore
				}
			}
		}
	}
{

		URI expanded = new UriTemplate(url).expand(urlVariables);
		return doExecute(expanded, method, requestCallback, responseExtractor);
	}
{

		Assert.notNull(element, "AnnotatedElement must not be null");
		Assert.hasText(annotationType, "annotationType must not be null or empty");

		if (visited.add(element)) {
			try {
				// Local annotations: declared OR inherited
				Annotation[] annotations = element.getAnnotations();

				// Search in local annotations
				for (Annotation annotation : annotations) {
					if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotation)
							&& (annotation.annotationType().getName().equals(annotationType) || metaDepth > 0)) {
						T result = processor.process(annotation, metaDepth);
						if (result != null) {
							return result;
						}
					}
				}

				// Search in meta annotations on local annotations
				for (Annotation annotation : annotations) {
					if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotation)) {
						T result = searchWithGetSemantics(annotation.annotationType(), annotationType, processor,
							visited, metaDepth + 1);
						if (result != null) {
							processor.postProcess(annotation, result);
							return result;
						}
					}
				}

			}
			catch (Exception ex) {
				AnnotationUtils.logIntrospectionFailure(element, ex);
			}
		}
		return null;
	}
{
		Class<?> fieldType = GenericTypeResolver.resolveTypeArgument(formatter.getClass(), Formatter.class);
		if (fieldType == null) {
			throw new IllegalArgumentException("Unable to extract parameterized field type argument from Formatter [" +
					formatter.getClass().getName() + "]; does the formatter parameterize the <T> generic type?");
		}
		addFormatterForFieldType(fieldType, formatter);
	}
{
		Class<? extends Annotation> annotationType = (Class<? extends Annotation>)
				GenericTypeResolver.resolveTypeArgument(annotationFormatterFactory.getClass(), AnnotationFormatterFactory.class);
		if (annotationType == null) {
			throw new IllegalArgumentException("Unable to extract parameterized Annotation type argument from AnnotationFormatterFactory [" +
					annotationFormatterFactory.getClass().getName() + "]; does the factory parameterize the <A extends Annotation> generic type?");
		}
		if (this.embeddedValueResolver != null && annotationFormatterFactory instanceof EmbeddedValueResolverAware) {
			((EmbeddedValueResolverAware) annotationFormatterFactory).setEmbeddedValueResolver(this.embeddedValueResolver);
		}
		Set<Class<?>> fieldTypes = annotationFormatterFactory.getFieldTypes();
		for (Class<?> fieldType : fieldTypes) {
			addConverter(new AnnotationPrinterConverter(annotationType, annotationFormatterFactory, fieldType));
			addConverter(new AnnotationParserConverter(annotationType, annotationFormatterFactory, fieldType));
		}
	}
{
		Object source = context.extractSource(element);
		CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), source);
		context.pushContainingComponent(compDefinition);

		RootBeanDefinition handlerMappingDef = new RootBeanDefinition(SimpleUrlHandlerMapping.class);

		String orderAttribute = element.getAttribute("order");
		int order = orderAttribute.isEmpty() ? DEFAULT_MAPPING_ORDER : Integer.valueOf(orderAttribute);
		handlerMappingDef.getPropertyValues().add("order", order);

		String pathHelper = element.getAttribute("path-helper");
		if (StringUtils.hasText(pathHelper)) {
			handlerMappingDef.getPropertyValues().add("urlPathHelper", new RuntimeBeanReference(pathHelper));
		}

		ManagedMap<String, Object> urlMap = new ManagedMap<String, Object>();
		urlMap.setSource(source);
		handlerMappingDef.getPropertyValues().add("urlMap", urlMap);

		registerBeanDef(handlerMappingDef, context, source);

		Element channelElem = DomUtils.getChildElementByTagName(element, "client-inbound-channel");
		RuntimeBeanReference inChannel = getMessageChannel("clientInboundChannel", channelElem, context, source);

		channelElem = DomUtils.getChildElementByTagName(element, "client-outbound-channel");
		RuntimeBeanReference outChannel = getMessageChannel("clientOutboundChannel", channelElem, context, source);

		RootBeanDefinition registryBeanDef = new RootBeanDefinition(DefaultUserSessionRegistry.class);
		String registryBeanName = registerBeanDef(registryBeanDef, context, source);
		RuntimeBeanReference sessionRegistry = new RuntimeBeanReference(registryBeanName);

		RuntimeBeanReference subProtoHandler = registerSubProtoHandler(element, inChannel, outChannel,
				sessionRegistry, context, source);

		for (Element endpointElem : DomUtils.getChildElementsByTagName(element, "stomp-endpoint")) {
			RuntimeBeanReference requestHandler = registerRequestHandler(endpointElem, subProtoHandler, context, source);
			String pathAttribute = endpointElem.getAttribute("path");
			Assert.state(StringUtils.hasText(pathAttribute), "Invalid <stomp-endpoint> (no path mapping)");
			List<String> paths = Arrays.asList(StringUtils.tokenizeToStringArray(pathAttribute, ","));
			for (String path : paths) {
				path = path.trim();
				Assert.state(StringUtils.hasText(path), "Invalid <stomp-endpoint> path attribute: " + pathAttribute);
				if (DomUtils.getChildElementByTagName(endpointElem, "sockjs") != null) {
					path = path.endsWith("/") ? path + "**" : path + "/**";
				}
				urlMap.put(path, requestHandler);
			}
		}

		channelElem = DomUtils.getChildElementByTagName(element, "broker-channel");
		RuntimeBeanReference brokerChannel = getMessageChannel("brokerChannel", channelElem, context, source);

		RuntimeBeanReference resolver = registerUserDestResolver(element, sessionRegistry, context, source);
		RuntimeBeanReference userDestHandler = registerUserDestHandler(element, inChannel,
				brokerChannel, resolver, context, source);

		RootBeanDefinition broker = registerMessageBroker(element, userDestHandler, inChannel,
				outChannel, brokerChannel, context, source);

		RuntimeBeanReference converter = registerMessageConverter(element, context, source);
		RuntimeBeanReference template = registerMessagingTemplate(element, brokerChannel, converter, context, source);
		registerAnnotationMethodMessageHandler(element, inChannel, outChannel,converter, template, context, source);

		Map<String, Object> scopeMap = Collections.<String, Object>singletonMap("websocket", new SimpSessionScope());
		RootBeanDefinition scopeConfigurer = new RootBeanDefinition(CustomScopeConfigurer.class);
		scopeConfigurer.getPropertyValues().add("scopes", scopeMap);
		registerBeanDefByName("webSocketScopeConfigurer", scopeConfigurer, context, source);

		registerWebSocketMessageBrokerStats(broker, inChannel, outChannel, context, source);

		context.popAndRegisterContainingComponent();
		return null;
	}
{
		return this.userDestinationBroadcast;
	}
{
		Assert.notNull(annotation, "Annotation must not be null");
		return annotation.annotationType().getName().startsWith("java.lang.annotation");
	}
{
		boolean hasCorsResponseHeaders = false;
		try {
			// Perhaps a CORS Filter has already added this?
			hasCorsResponseHeaders = response.getHeaders().getAccessControlAllowOrigin() != null;
		}
		catch (NullPointerException npe) {
			// See SPR-11919 and https://issues.jboss.org/browse/WFLY-3474
		}
		return hasCorsResponseHeaders;
	}
{

		if (!checkOrigin(request, config.getAllowedOrigins()) ||
				!checkMethod(request, config.getAllowedMethods(), isPreFlight) ||
				!checkHeaders(request, config.getAllowedHeaders(), isPreFlight)) {
			response.setStatusCode(HttpStatus.FORBIDDEN);
			response.getBody().write("Invalid CORS request".getBytes(UTF8_CHARSET));
			return false;
		}
		return true;
	}
{
		Assert.hasLength(encoding, "Encoding must not be empty");
		if (this.encoded) {
			return this;
		}
		String encodedScheme = encodeUriComponent(getScheme(), encoding, Type.SCHEME);
		String encodedUserInfo = encodeUriComponent(this.userInfo, encoding, Type.USER_INFO);
		String encodedHost = encodeUriComponent(this.host, encoding, getHostType());

		PathComponent encodedPath = this.path.encode(encoding);
		MultiValueMap<String, String> encodedQueryParams =
				new LinkedMultiValueMap<String, String>(this.queryParams.size());
		for (Map.Entry<String, List<String>> entry : this.queryParams.entrySet()) {
			String encodedName = encodeUriComponent(entry.getKey(), encoding, Type.QUERY_PARAM);
			List<String> encodedValues = new ArrayList<String>(entry.getValue().size());
			for (String value : entry.getValue()) {
				String encodedValue = encodeUriComponent(value, encoding, Type.QUERY_PARAM);
				encodedValues.add(encodedValue);
			}
			encodedQueryParams.put(encodedName, encodedValues);
		}
		String encodedFragment = encodeUriComponent(this.getFragment(), encoding, Type.FRAGMENT);
		return new HierarchicalUriComponents(encodedScheme, encodedUserInfo, encodedHost, this.port, encodedPath,
				encodedQueryParams, encodedFragment, true, false);
	}
{
		Assert.state(!this.encoded, "Cannot expand an already encoded UriComponents object");
		String expandedScheme = expandUriComponent(getScheme(), uriVariables);
		String expandedUserInfo = expandUriComponent(this.userInfo, uriVariables);
		String expandedHost = expandUriComponent(this.host, uriVariables);
		String expandedPort = expandUriComponent(this.port, uriVariables);
		PathComponent expandedPath = this.path.expand(uriVariables);
		MultiValueMap<String, String> expandedQueryParams =
				new LinkedMultiValueMap<String, String>(this.queryParams.size());
		for (Map.Entry<String, List<String>> entry : this.queryParams.entrySet()) {
			String expandedName = expandUriComponent(entry.getKey(), uriVariables);
			List<String> expandedValues = new ArrayList<String>(entry.getValue().size());
			for (String value : entry.getValue()) {
				String expandedValue = expandUriComponent(value, uriVariables);
				expandedValues.add(expandedValue);
			}
			expandedQueryParams.put(expandedName, expandedValues);
		}
		String expandedFragment = expandUriComponent(this.getFragment(), uriVariables);
		return new HierarchicalUriComponents(expandedScheme, expandedUserInfo, expandedHost, expandedPort, expandedPath,
				expandedQueryParams, expandedFragment, false, false);
	}
{
		Class<NettyTcpClient> type = REACTOR_TCP_CLIENT_TYPE;
		TcpClient<Message<P>, Message<P>> tcpClient = NetStreams.tcpClient(type, this.tcpClientSpecFactory);
		synchronized (this.tcpClients) {
			this.tcpClients.add(tcpClient);
		}
		return tcpClient;
	}
{
		Class<NettyTcpClient> type = REACTOR_TCP_CLIENT_TYPE;
		TcpClient<Message<P>, Message<P>> tcpClient = NetStreams.tcpClient(type, this.tcpClientSpecFactory);
		synchronized (this.tcpClients) {
			this.tcpClients.add(tcpClient);
		}
		return tcpClient;
	}
{
		Class<NettyTcpClient> type = REACTOR_TCP_CLIENT_TYPE;

		TcpClient<Message<P>, Message<P>> tcpClient = NetStreams.tcpClient(type, this.tcpClientSpecFactory);

		Promise<Void> promise = tcpClient.start(composeConnectionHandling(tcpClient, connectionHandler));

		return new PassThroughPromiseToListenableFutureAdapter<Void>(
				promise.onError(new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) {
						connectionHandler.afterConnectFailure(throwable);
					}
				})
		);
	}
{
		RequestMappingInfo info = null;
		RequestMapping methodAnnotation = AnnotationUtils.findAnnotation(method, RequestMapping.class);
		if (methodAnnotation != null) {
			RequestCondition<?> methodCondition = getCustomMethodCondition(method);
			info = createRequestMappingInfo(methodAnnotation, methodCondition);
			RequestMapping typeAnnotation = AnnotationUtils.findAnnotation(handlerType, RequestMapping.class);
			if (typeAnnotation != null) {
				RequestCondition<?> typeCondition = getCustomTypeCondition(handlerType);
				info = createRequestMappingInfo(typeAnnotation, typeCondition).combine(info);
			}
		}
		return info;
	}
{

		//FIXME Should it be exposed in Spring ?
		int ioThreadCount;
		try {
			ioThreadCount = Integer.parseInt(System.getProperty("reactor.tcp.ioThreadCount"));
		} catch (Exception i) {
			ioThreadCount = -1;
		}
		if (ioThreadCount <= 0l) {
			ioThreadCount = Runtime.getRuntime().availableProcessors();
		}

		final NioEventLoopGroup eventLoopGroup =
				new NioEventLoopGroup(ioThreadCount, new NamedDaemonThreadFactory("reactor-tcp-io"));

		this.tcpClientSpec = new Function<Spec.TcpClientSpec<Message<P>, Message<P>>,
				Spec.TcpClientSpec<Message<P>, Message<P>>>() {

			@Override
			public Spec.TcpClientSpec<Message<P>, Message<P>> apply(Spec.TcpClientSpec<Message<P>, Message<P>>
					                                                        messageMessageTcpClientSpec) {
				return messageMessageTcpClientSpec
						.codec(codec)
								//make connect dynamic or use reconnect strategy to LB onto cluster
						.connect(host, port)
						.options(new NettyClientSocketOptions().eventLoopGroup(eventLoopGroup));
			}
		};
	}
{
		RequestMappingInfoHandlerMapping handlerMapping = getRequestMappingInfoHandlerMapping();
		List<HandlerMethod> handlerMethods = handlerMapping.getHandlerMethodsForMappingName(mappingName);
		if (handlerMethods == null) {
			throw new IllegalArgumentException("Mapping mappingName not found: " + mappingName);
		}
		if (handlerMethods.size() != 1) {
			throw new IllegalArgumentException(
					"No unique match for mapping mappingName " + mappingName + ": " + handlerMethods);
		}
		return new MethodArgumentBuilder(handlerMethods.get(0).getMethod());
	}
{

		given(this.messageChannel.send(any(Message.class))).willReturn(true);

		Message<?> inputMessage = createInputMessage("sess1", "sub1", "/app", "/dest", null);
		this.handler.handleReturnValue(PAYLOAD, this.noAnnotationsReturnType, inputMessage);

		verify(this.messageChannel, times(1)).send(this.messageCaptor.capture());

		Message<?> message = this.messageCaptor.getAllValues().get(0);
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(message);
		assertEquals("sess1", headers.getSessionId());
		assertEquals("/topic/dest", headers.getDestination());
		assertEquals(MIME_TYPE, headers.getContentType());
		assertNull("Subscription id should not be copied", headers.getSubscriptionId());
	}
{
		MultiValueMap<String, String> result = this.destinationCache.getSubscriptions(destination);
		if (result != null) {
			return result;
		}
		result = new LinkedMultiValueMap<String, String>();
		for (SessionSubscriptionInfo info : this.subscriptionRegistry.getAllSubscriptions()) {
			for (String destinationPattern : info.getDestinations()) {
				if (this.pathMatcher.match(destinationPattern, destination)) {
					for (String subscriptionId : info.getSubscriptions(destinationPattern)) {
						result.add(info.sessionId, subscriptionId);
					}
				}
			}
		}
		if (!result.isEmpty()) {
			this.destinationCache.addSubscriptions(destination, result);
		}
		return result;
	}
{
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.create(SimpMessageType.SUBSCRIBE);
		headers.setSessionId(sessionId);
		headers.setSubscriptionId(subscriptionId);
		if (destination != null) {
			headers.setDestination(destination);
		}
		return MessageBuilder.withPayload("").copyHeaders(headers.toMap()).build();
	}
{
		CacheAwareContextLoaderDelegate cacheAwareContextLoaderDelegate = new DefaultCacheAwareContextLoaderDelegate();
		BootstrapContext bootstrapContext = new DefaultBootstrapContext(testClass, cacheAwareContextLoaderDelegate);
		TestContextBootstrapper testContextBootstrapper = BootstrapUtils.resolveTestContextBootstrapper(bootstrapContext);
		this.testContext = testContextBootstrapper.buildTestContext();
		registerTestExecutionListeners(testContextBootstrapper.getTestExecutionListeners());
	}
{
		String destination = SimpMessageHeaderAccessor.getDestination(message.getHeaders());
		DestinationInfo info = parseUserDestination(message);
		if (info == null) {
			return null;
		}
		Set<String> resolved = new HashSet<String>();
		for (String sessionId : info.getSessionIds()) {
			String targetDestination = getTargetDestination(
					destination, info.getDestinationWithoutPrefix(), sessionId, info.getUser());
			if (targetDestination != null) {
				resolved.add(targetDestination);
			}
		}
		return new UserDestinationResult(destination, resolved, info.getSubscribeDestination(), info.getUser());
	}
{
		Method specificMethod = getMostSpecificMethod();
		SendTo ann = AnnotationUtils.getAnnotation(specificMethod, SendTo.class);
		if (ann != null) {
			Object[] destinations = ann.value();
			if (destinations.length != 1) {
				throw new IllegalStateException("Invalid @" + SendTo.class.getSimpleName() + " annotation on '"
						+ specificMethod + "' one destination must be set (got " + Arrays.toString(destinations) + ")");
			}
			return (String) destinations[0];
		}
		return null;
	}
{
		Assert.notNull(target, "Target object must not be null");
		Field field = ReflectionUtils.findField(target.getClass(), name, type);

		// SPR-9571: inline Assert.notNull() in order to avoid accidentally invoking
		// toString() on a non-null target.
		if (field == null) {
			throw new IllegalArgumentException(String.format("Could not find field [%s] of type [%s] on target [%s]",
				name, type, target));
		}

		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Setting field [%s] of type [%s] on target [%s] to value [%s]", name, type,
				target,
				value));
		}
		ReflectionUtils.makeAccessible(field);
		ReflectionUtils.setField(field, target, value);
	}
{
		Assert.notNull(target, "Target object must not be null");
		Field field = ReflectionUtils.findField(target.getClass(), name);
		Assert.notNull(field, "Could not find field [" + name + "] on target [" + target + "]");

		if (logger.isDebugEnabled()) {
			logger.debug("Getting field [" + name + "] from target [" + target + "]");
		}
		ReflectionUtils.makeAccessible(field);
		return ReflectionUtils.getField(field, target);
	}
{
		MessageHeaders headers = message.getHeaders();
		SimpMessageType messageType = SimpMessageHeaderAccessor.getMessageType(headers);
		String destination = SimpMessageHeaderAccessor.getDestination(headers);
		String sessionId = SimpMessageHeaderAccessor.getSessionId(headers);

		if (!checkDestinationPrefix(destination)) {
			return;
		}

		SimpMessageHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, SimpMessageHeaderAccessor.class);
		if (accessor == null) {
			throw new IllegalStateException(
					"No header accessor (not using the SimpMessagingTemplate?): " + message);
		}

		if (SimpMessageType.MESSAGE.equals(messageType)) {
			logMessage(message);
			sendMessageToSubscribers(destination, message);
		}
		else if (SimpMessageType.CONNECT.equals(messageType)) {
			logMessage(message);
			SimpMessageHeaderAccessor connectAck = SimpMessageHeaderAccessor.create(SimpMessageType.CONNECT_ACK);
			initHeaders(connectAck);
			connectAck.setSessionId(sessionId);
			connectAck.setUser(SimpMessageHeaderAccessor.getUser(headers));
			connectAck.setHeader(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER, message);
			Message<byte[]> messageOut = MessageBuilder.createMessage(EMPTY_PAYLOAD, connectAck.getMessageHeaders());
			getClientOutboundChannel().send(messageOut);
		}
		else if (SimpMessageType.DISCONNECT.equals(messageType)) {
			logMessage(message);
			this.subscriptionRegistry.unregisterAllSubscriptions(sessionId);
			SimpMessageHeaderAccessor disconnectAck = SimpMessageHeaderAccessor.create(SimpMessageType.DISCONNECT_ACK);
			initHeaders(disconnectAck);
			disconnectAck.setSessionId(sessionId);
			disconnectAck.setUser(SimpMessageHeaderAccessor.getUser(headers));
			Message<byte[]> messageOut = MessageBuilder.createMessage(EMPTY_PAYLOAD, disconnectAck.getMessageHeaders());
			getClientOutboundChannel().send(messageOut);
		}
		else if (SimpMessageType.SUBSCRIBE.equals(messageType)) {
			logMessage(message);
			this.subscriptionRegistry.registerSubscription(message);
		}
		else if (SimpMessageType.UNSUBSCRIBE.equals(messageType)) {
			logMessage(message);
			this.subscriptionRegistry.unregisterSubscription(message);
		}
	}
{
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CORS request");
	}
{
			PathSegmentComponentBuilder psBuilder = getLastBuilder(PathSegmentComponentBuilder.class);
			FullPathComponentBuilder fpBuilder = getLastBuilder(FullPathComponentBuilder.class);
			if (psBuilder != null) {
				path = path.startsWith("/") ? path : "/" + path;
			}
			if (fpBuilder == null) {
				fpBuilder = new FullPathComponentBuilder();
				this.builders.add(fpBuilder);
			}
			fpBuilder.append(path);
		}
{
		HttpServletResponse response = getResponse();
		if (lastModifiedTimestamp >= 0 && !this.notModified &&
				(response == null || !response.containsHeader(HEADER_LAST_MODIFIED))) {
			long ifModifiedSince = -1;
			try {
				ifModifiedSince = getRequest().getDateHeader(HEADER_IF_MODIFIED_SINCE);
			}
			catch (IllegalArgumentException ex) {
				String headerValue = getRequest().getHeader(HEADER_IF_MODIFIED_SINCE);
				// Possibly an IE 10 style value: "Wed, 09 Apr 2014 09:57:42 GMT; length=13774"
				int separatorIndex = headerValue.indexOf(';');
				if (separatorIndex != -1) {
					String datePart = headerValue.substring(0, separatorIndex);
					try {
						ifModifiedSince = Date.parse(datePart);
					}
					catch (IllegalArgumentException ex2) {
						// Giving up
					}
				}
			}
			this.notModified = (ifModifiedSince >= (lastModifiedTimestamp / 1000 * 1000));
			if (response != null) {
				if (this.notModified && supportsNotModifiedStatus()) {
					response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				}
				else {
					response.setDateHeader(HEADER_LAST_MODIFIED, lastModifiedTimestamp);
				}
			}
		}
		return this.notModified;
	}
{
		HttpServletResponse response = getResponse();
		if (StringUtils.hasLength(etag) && !this.notModified &&
				(response == null || !response.containsHeader(HEADER_ETAG))) {
			String ifNoneMatch = getRequest().getHeader(HEADER_IF_NONE_MATCH);
			this.notModified = etag.equals(ifNoneMatch);
			if (response != null) {
				if (this.notModified && supportsNotModifiedStatus()) {
					response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				}
				else {
					response.setHeader(HEADER_ETAG, etag);
				}
			}
		}
		return this.notModified;
	}
{
		Class<?> testClass = testContext.getTestClass();
		Assert.notNull(testClass, "The test class of the supplied TestContext must not be null");
		Method testMethod = testContext.getTestMethod();
		Assert.notNull(testMethod, "The test method of the supplied TestContext must not be null");

		final String annotationType = DirtiesContext.class.getName();
		AnnotationAttributes methodAnnAttrs = AnnotatedElementUtils.getAnnotationAttributes(testMethod, annotationType);
		AnnotationAttributes classAnnAttrs = AnnotatedElementUtils.getAnnotationAttributes(testClass, annotationType);
		boolean methodDirtiesContext = methodAnnAttrs != null;
		boolean classDirtiesContext = classAnnAttrs != null;
		ClassMode classMode = classDirtiesContext ? classAnnAttrs.<ClassMode> getEnum("classMode") : null;

		if (logger.isDebugEnabled()) {
			logger.debug(String.format(
				"After test method: context %s, class dirties context [%s], class mode [%s], method dirties context [%s].",
				testContext, classDirtiesContext, classMode, methodDirtiesContext));
		}

		if (methodDirtiesContext || (classMode == AFTER_EACH_TEST_METHOD)) {
			HierarchyMode hierarchyMode = methodDirtiesContext ? methodAnnAttrs.<HierarchyMode> getEnum("hierarchyMode")
					: classAnnAttrs.<HierarchyMode> getEnum("hierarchyMode");
			dirtyContext(testContext, hierarchyMode);
		}
	}
{
		Class<?> testClass = testContext.getTestClass();
		Assert.notNull(testClass, "The test class of the supplied TestContext must not be null");

		final String annotationType = DirtiesContext.class.getName();
		AnnotationAttributes annAttrs = AnnotatedElementUtils.getAnnotationAttributes(testClass, annotationType);
		boolean dirtiesContext = annAttrs != null;

		if (logger.isDebugEnabled()) {
			logger.debug(String.format("After test class: context %s, dirtiesContext [%s].", testContext,
				dirtiesContext));
		}
		if (dirtiesContext) {
			HierarchyMode hierarchyMode = annAttrs.<HierarchyMode> getEnum("hierarchyMode");
			dirtyContext(testContext, hierarchyMode);
		}
	}
{
			if (StringUtils.hasText(path)) {
				PathSegmentComponentBuilder psBuilder = getLastBuilder(PathSegmentComponentBuilder.class);
				FullPathComponentBuilder fpBuilder = getLastBuilder(FullPathComponentBuilder.class);
				if (psBuilder != null) {
					path = path.startsWith("/") ? path : "/" + path;
				}
				if (fpBuilder == null) {
					fpBuilder = new FullPathComponentBuilder();
					this.builders.add(fpBuilder);
				}
				fpBuilder.append(path);
			}
		}
{
		byte[] boundary = new byte[this.random.nextInt(11) + 30];
		for (int i = 0; i < boundary.length; i++) {
			boundary[i] = BOUNDARY_CHARS[this.random.nextInt(BOUNDARY_CHARS.length)];
		}
		return boundary;
	}
{
		if (logger.isDebugEnabled()) {
			logger.debug("Searching methods to handle " + ex.getClass().getSimpleName());
		}
		Class<?> beanType = handlerMethod.getBeanType();
		AbstractExceptionHandlerMethodResolver resolver = this.exceptionHandlerCache.get(beanType);
		if (resolver == null) {
			resolver = createExceptionHandlerMethodResolverFor(beanType);
			this.exceptionHandlerCache.put(beanType, resolver);
		}
		Method method = resolver.resolveMethod(ex);
		if (method == null) {
			logger.error("Unhandled exception", ex);
			return;
		}
		InvocableHandlerMethod invocable = new InvocableHandlerMethod(handlerMethod.getBean(), method);
		invocable.setMessageMethodArgumentResolvers(this.argumentResolvers);
		if (logger.isDebugEnabled()) {
			logger.debug("Invoking " + invocable.getShortLogMessage());
		}
		try {
			Object returnValue = invocable.invoke(message, ex);
			MethodParameter returnType = invocable.getReturnType();
			if (void.class.equals(returnType.getParameterType())) {
				return;
			}
			this.returnValueHandlers.handleReturnValue(returnValue, returnType, message);
		}
		catch (Throwable t) {
			logger.error("Error while handling exception", t);
			return;
		}
	}
{
		String beanName = transformedBeanName(name);
		Class<?> typeToMatch = (targetType != null ? targetType : Object.class);

		// Check manually registered singletons.
		Object beanInstance = getSingleton(beanName, false);
		if (beanInstance != null) {
			if (beanInstance instanceof FactoryBean) {
				if (!BeanFactoryUtils.isFactoryDereference(name)) {
					Class<?> type = getTypeForFactoryBean((FactoryBean<?>) beanInstance);
					return (type != null && ClassUtils.isAssignable(typeToMatch, type));
				}
				else {
					return ClassUtils.isAssignableValue(typeToMatch, beanInstance);
				}
			}
			else {
				return !BeanFactoryUtils.isFactoryDereference(name) &&
						ClassUtils.isAssignableValue(typeToMatch, beanInstance);
			}
		}
		else if (containsSingleton(beanName) && !containsBeanDefinition(beanName)) {
			// null instance registered
			return false;
		}

		else {
			// No singleton instance found -> check bean definition.
			BeanFactory parentBeanFactory = getParentBeanFactory();
			if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
				// No bean definition found in this factory -> delegate to parent.
				return parentBeanFactory.isTypeMatch(originalBeanName(name), targetType);
			}

			// Retrieve corresponding bean definition.
			RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);

			Class<?>[] typesToMatch = (FactoryBean.class.equals(typeToMatch) ?
					new Class<?>[] {typeToMatch} : new Class<?>[] {FactoryBean.class, typeToMatch});

			// Check decorated bean definition, if any: We assume it'll be easier
			// to determine the decorated bean's type than the proxy's type.
			BeanDefinitionHolder dbd = mbd.getDecoratedDefinition();
			if (dbd != null && !BeanFactoryUtils.isFactoryDereference(name)) {
				RootBeanDefinition tbd = getMergedBeanDefinition(dbd.getBeanName(), dbd.getBeanDefinition(), mbd);
				Class<?> targetClass = predictBeanType(dbd.getBeanName(), tbd, typesToMatch);
				if (targetClass != null && !FactoryBean.class.isAssignableFrom(targetClass)) {
					return typeToMatch.isAssignableFrom(targetClass);
				}
			}

			Class<?> beanType = predictBeanType(beanName, mbd, typesToMatch);
			if (beanType == null) {
				return false;
			}

			// Check bean class whether we're dealing with a FactoryBean.
			if (FactoryBean.class.isAssignableFrom(beanType)) {
				if (!BeanFactoryUtils.isFactoryDereference(name)) {
					// If it's a FactoryBean, we want to look at what it creates, not the factory class.
					beanType = getTypeForFactoryBean(beanName, mbd);
					if (beanType == null) {
						return false;
					}
				}
			}
			else if (BeanFactoryUtils.isFactoryDereference(name)) {
				// Special case: A SmartInstantiationAwareBeanPostProcessor returned a non-FactoryBean
				// type but we nevertheless are being asked to dereference a FactoryBean...
				// Let's check the original bean class and proceed with it if it is a FactoryBean.
				beanType = predictBeanType(beanName, mbd, FactoryBean.class);
				if (beanType == null || !FactoryBean.class.isAssignableFrom(beanType)) {
					return false;
				}
			}

			return typeToMatch.isAssignableFrom(beanType);
		}
	}
{
		boolean isFactoryType = (type != null && FactoryBean.class.isAssignableFrom(type));
		List<String> matches = new ArrayList<String>();
		for (String name : this.beans.keySet()) {
			Object beanInstance = this.beans.get(name);
			if (beanInstance instanceof FactoryBean && !isFactoryType) {
				if (includeFactoryBeans) {
					Class<?> objectType = ((FactoryBean<?>) beanInstance).getObjectType();
					if (objectType != null && (type == null || type.isAssignableFrom(objectType))) {
						matches.add(name);
					}
				}
			}
			else {
				if (type == null || type.isInstance(beanInstance)) {
					matches.add(name);
				}
			}
		}
		return StringUtils.toStringArray(matches);
	}
{
		String mapping = getTypeRequestMapping(controllerType);
		return ServletUriComponentsBuilder.fromCurrentServletMapping().path(mapping);
	}
{
		Assert.isInstanceOf(MethodInvocationInfo.class, invocationInfo);
		MethodInvocationInfo info = (MethodInvocationInfo) invocationInfo;
		return fromMethod(info.getControllerMethod(), info.getArgumentValues());
	}
{
		String typePath = getTypeRequestMapping(method.getDeclaringClass());
		String methodPath = getMethodRequestMapping(method);
		String path = pathMatcher.combine(typePath, methodPath);

		UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentServletMapping().path(path);
		UriComponents uriComponents = applyContributors(builder, method, argumentValues);
		return ServletUriComponentsBuilder.newInstance().uriComponents(uriComponents);
	}
{
		for (ConstraintViolation<Object> violation : violations) {
			String field = violation.getPropertyPath().toString();
			FieldError fieldError = errors.getFieldError(field);
			if (fieldError == null || !fieldError.isBindingFailure()) {
				try {
					ConstraintDescriptor<?> cd = violation.getConstraintDescriptor();
					String errorCode = cd.getAnnotation().annotationType().getSimpleName();
					Object[] errorArgs = getArgumentsForConstraint(errors.getObjectName(), field, cd);
					if (errors instanceof BindingResult) {
						// Can do custom FieldError registration with invalid value from ConstraintViolation,
						// as necessary for Hibernate Validator compatibility (non-indexed set path in field)
						BindingResult bindingResult = (BindingResult) errors;
						String nestedField = bindingResult.getNestedPath() + field;
						if ("".equals(nestedField)) {
							String[] errorCodes = bindingResult.resolveMessageCodes(errorCode);
							bindingResult.addError(new ObjectError(
									errors.getObjectName(), errorCodes, errorArgs, violation.getMessage()));
						}
						else {
							Object invalidValue = violation.getInvalidValue();
							if (!"".equals(field) && (invalidValue == violation.getLeafBean() ||
									(field.contains(".") && !field.contains("[]")))) {
								// Possibly a bean constraint with property path: retrieve the actual property value.
								// However, explicitly avoid this for "address[]" style paths that we can't handle.
								invalidValue = bindingResult.getRawFieldValue(field);
							}
							String[] errorCodes = bindingResult.resolveMessageCodes(errorCode, field);
							bindingResult.addError(new FieldError(
									errors.getObjectName(), nestedField, invalidValue, false,
									errorCodes, errorArgs, violation.getMessage()));
						}
					}
					else {
						// got no BindingResult - can only do standard rejectValue call
						// with automatic extraction of the current field value
						errors.rejectValue(field, errorCode, errorArgs, violation.getMessage());
					}
				}
				catch (NotReadablePropertyException ex) {
					throw new IllegalStateException("JSR-303 validated property '" + field +
							"' does not have a corresponding accessor for Spring data binding - " +
							"check your DataBinder's configuration (bean property versus direct field access)", ex);
				}
			}
		}
	}
{
		ConfigurableApplicationContext context = initContext(
				new RootBeanDefinition(AsyncAnnotationBeanPostProcessor.class));
		ITestBean testBean = context.getBean("target", ITestBean.class);
		final Future<Object> result = testBean.failWithFuture();

		try {
			result.get();
		}
		catch (InterruptedException ex) {
			fail("Should not have failed with InterruptedException: " + ex);
		}
		catch (ExecutionException ex) {
			// expected
			assertEquals("Wrong exception cause", UnsupportedOperationException.class, ex.getCause().getClass());
		}
	}
{

		if (contentType == null || contentType.isWildcardType() || contentType.isWildcardSubtype()) {
			contentType = getDefaultContentType();
		}
		Assert.notNull(contentType,
				"Count not determine Content-Type, set one using the 'defaultContentType' property");
		outputMessage.getHeaders().setContentType(contentType);
		ImageOutputStream imageOutputStream = null;
		ImageWriter imageWriter = null;
		try {
			imageOutputStream = createImageOutputStream(outputMessage.getBody());
			Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByMIMEType(contentType.toString());
			if (imageWriters.hasNext()) {
				imageWriter = imageWriters.next();
				ImageWriteParam iwp = imageWriter.getDefaultWriteParam();
				process(iwp);
				imageWriter.setOutput(imageOutputStream);
				imageWriter.write(null, new IIOImage(image, null, null), iwp);
			}
			else {
				throw new HttpMessageNotWritableException(
						"Could not find javax.imageio.ImageWriter for Content-Type [" + contentType + "]");
			}
		}
		finally {
			if (imageWriter != null) {
				imageWriter.dispose();
			}
			if (imageOutputStream != null) {
				try {
					imageOutputStream.close();
				}
				catch (IOException ex) {
					// ignore
				}
			}
		}
	}
{
		Assert.notNull(endpoint, "Endpoint must not be null");
		Assert.notNull(factory, "Factory must not be null");

		String id = endpoint.getId();
		Assert.notNull(id, "Endpoint id must not be null");
		Assert.state(!this.listenerContainers.containsKey(id),
				"Another endpoint is already registered with id '" + id + "'");

		MessageListenerContainer container = createListenerContainer(endpoint, factory);
		this.listenerContainers.put(id, container);
	}
{
		for (MessageListenerContainer listenerContainer : getListenerContainers()) {
			if (listenerContainer.isAutoStartup()) {
				listenerContainer.start();
			}
		}
	}
{
		Annotation[] annotations = param.getParameterAnnotations();
		for (Annotation ann : annotations) {
			Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
			if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
				Object hints = (validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann));
				Object[] validationHints = (hints instanceof Object[] ? (Object[]) hints : new Object[] {hints});
				binder.validate(validationHints);
				BindingResult bindingResult = binder.getBindingResult();
				if (bindingResult.hasErrors()) {
					if (isBindingErrorFatal(param)) {
						throw new MethodArgumentNotValidException(param, bindingResult);
					}
				}
			}
		}
	}
{
		Annotation[] annotations = parameter.getParameterAnnotations();
		for (Annotation ann : annotations) {
			Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
			if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
				Object hints = (validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann));
				Object[] validationHints = (hints instanceof Object[] ? (Object[]) hints : new Object[] {hints});
				binder.validate(validationHints);
				BindingResult bindingResult = binder.getBindingResult();
				if (bindingResult.hasErrors()) {
					if (isBindingErrorFatal(parameter)) {
						throw new MethodArgumentNotValidException(parameter, bindingResult);
					}
				}
				break;
			}
		}
	}
{
		Object[] args = resolveArguments(event);
		if (shouldHandle(event, args)) {
			Object result = doInvoke(args);
			if (result != null) {
				handleResult(result);
			}
			else {
				logger.trace("No result object given - no result to handle");
			}
		}
	}
{
			if (event instanceof BrokerAvailabilityEvent) {
				this.availabilityEvents.add(((BrokerAvailabilityEvent) event).isBrokerAvailable());
			}
		}
{
			logger.debug("Processing ApplicationEvent " + event);
			if (event instanceof BrokerAvailabilityEvent) {
				this.eventQueue.add((BrokerAvailabilityEvent) event);
			}
		}
{
		for (final ApplicationListener<?> listener : getApplicationListeners(event)) {
			Executor executor = getTaskExecutor();
			if (executor != null) {
				executor.execute(new Runnable() {
					@Override
					public void run() {
						invokeListener(listener, event);
					}
				});
			}
			else {
				invokeListener(listener, event);
			}
		}
	}
{
		Assert.notNull(event, "Event must not be null");
		if (logger.isTraceEnabled()) {
			logger.trace("Publishing event in " + getDisplayName() + ": " + event);
		}
		getApplicationEventMulticaster().multicastEvent(event);
		if (this.parent != null) {
			this.parent.publishEvent(event);
		}
	}
{
		if (this.scheduler != null) {
			this.registrar.setScheduler(this.scheduler);
		}

		if (this.beanFactory instanceof ListableBeanFactory) {
			Map<String, SchedulingConfigurer> configurers =
					((ListableBeanFactory) this.beanFactory).getBeansOfType(SchedulingConfigurer.class);
			for (SchedulingConfigurer configurer : configurers.values()) {
				configurer.configureTasks(this.registrar);
			}
		}

		if (this.registrar.hasTasks() && this.registrar.getScheduler() == null) {
			Assert.state(this.beanFactory != null, "BeanFactory must be set to find scheduler by type");
			try {
				// Search for TaskScheduler bean...
				this.registrar.setScheduler(this.beanFactory.getBean(TaskScheduler.class));
			}
			catch (NoUniqueBeanDefinitionException ex) {
				throw new IllegalStateException("More than one TaskScheduler exists within the context. " +
						"Remove all but one of the beans; or implement the SchedulingConfigurer interface and call " +
						"ScheduledTaskRegistrar#setScheduler explicitly within the configureTasks() callback.", ex);
			}
			catch (NoSuchBeanDefinitionException ex) {
				logger.debug("Could not find default TaskScheduler bean", ex);
				// Search for ScheduledExecutorService bean next...
				try {
					this.registrar.setScheduler(this.beanFactory.getBean(ScheduledExecutorService.class));
				}
				catch (NoUniqueBeanDefinitionException ex2) {
					throw new IllegalStateException("More than one ScheduledExecutorService exists within the context. " +
							"Remove all but one of the beans; or implement the SchedulingConfigurer interface and call " +
							"ScheduledTaskRegistrar#setScheduler explicitly within the configureTasks() callback.", ex);
				}
				catch (NoSuchBeanDefinitionException ex2) {
					logger.debug("Could not find default ScheduledExecutorService bean", ex);
					// Giving up -> falling back to default scheduler within the registrar...
				}
			}
		}

		this.registrar.afterPropertiesSet();
	}
{
		// Do not attempt to lookup tx manager if no tx attributes are set
		if (txAttr == null || this.beanFactory == null) {
			return getTransactionManager();
		}
		String qualifier = (txAttr.getQualifier() != null ?
				txAttr.getQualifier() : this.transactionManagerBeanName);
		if (StringUtils.hasText(qualifier)) {
			PlatformTransactionManager txManager = this.transactionManagerCache.get(qualifier);
			if (txManager == null) {
				txManager = BeanFactoryAnnotationUtils.qualifiedBeanOfType(
						this.beanFactory, PlatformTransactionManager.class, qualifier);
				this.transactionManagerCache.putIfAbsent(qualifier, txManager);
			}
			return txManager;
		}
		else {
			PlatformTransactionManager defaultTransactionManager = getTransactionManager();
			if (defaultTransactionManager == null) {
				defaultTransactionManager = this.beanFactory.getBean(PlatformTransactionManager.class);
				this.transactionManagerCache.putIfAbsent(
						DEFAULT_TRANSACTION_MANAGER_KEY, defaultTransactionManager);
			}
			return defaultTransactionManager;
		}
	}
{
		Registry<ConnectionSocketFactory> schemeRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", SSLConnectionSocketFactory.getSocketFactory())
				.build();

		PoolingHttpClientConnectionManager connectionManager
				= new PoolingHttpClientConnectionManager(schemeRegistry);
		connectionManager.setMaxTotal(DEFAULT_MAX_TOTAL_CONNECTIONS);
		connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS_PER_ROUTE);

		this.httpClient = HttpClientBuilder.create().setConnectionManager(connectionManager).build();
	}
{
		PlatformTransactionManager txManager = this.transactionManagerCache.get(qualifier);
		if (txManager == null) {
			txManager = BeanFactoryAnnotationUtils.qualifiedBeanOfType(
					this.beanFactory, PlatformTransactionManager.class, qualifier);
			this.transactionManagerCache.putIfAbsent(qualifier, txManager);
		}
		return txManager;
	}
{
		if (this.beanFactory != null) {
			String qualifier = txAttr != null ? txAttr.getQualifier() : null;
			if (StringUtils.hasText(qualifier)) {
				PlatformTransactionManager txManager = this.transactionManagerCache.get(qualifier);
				if (txManager == null) {
					txManager = BeanFactoryAnnotationUtils.qualifiedBeanOfType(
							this.beanFactory, PlatformTransactionManager.class, qualifier);
					this.transactionManagerCache.putIfAbsent(qualifier, txManager);
				}
				return txManager;
			}
			else if (StringUtils.hasText(this.transactionManagerBeanName)) {
				PlatformTransactionManager txManager = this.transactionManagerCache.get(this.transactionManagerBeanName);
				if (txManager == null) {
					txManager = this.beanFactory.getBean(
							this.transactionManagerBeanName, PlatformTransactionManager.class);
					this.transactionManagerCache.putIfAbsent(this.transactionManagerBeanName, txManager);
				}
				return txManager;
			} else {
				PlatformTransactionManager defaultTransactionManager = getTransactionManager();
				if (defaultTransactionManager == null) {
					defaultTransactionManager = this.beanFactory.getBean(PlatformTransactionManager.class);
					this.transactionManagerCache.putIfAbsent(
							DEFAULT_TRANSACTION_MANAGER_KEY, defaultTransactionManager);
				}
				return defaultTransactionManager;
			}
		}
		return getTransactionManager();
	}
{
		Class<? extends ApplicationEvent> eventType = event.getClass();
		Object source = event.getSource();
		Class<?> sourceType = (source != null ? source.getClass() : null);
		ListenerCacheKey cacheKey = new ListenerCacheKey(eventType, sourceType);
		ListenerRetriever retriever = this.retrieverCache.get(cacheKey);
		if (retriever != null) {
			return retriever.getApplicationListeners();
		}
		else {
			retriever = new ListenerRetriever(true);
			LinkedList<ApplicationListener<?>> allListeners = new LinkedList<ApplicationListener<?>>();
			Set<ApplicationListener<?>> listeners;
			Set<String> listenerBeans;
			synchronized (this.defaultRetriever) {
				listeners = new LinkedHashSet<ApplicationListener<?>>(this.defaultRetriever.applicationListeners);
				listenerBeans = new LinkedHashSet<String>(this.defaultRetriever.applicationListenerBeans);
			}
			for (ApplicationListener<?> listener : listeners) {
				if (supportsEvent(listener, eventType, sourceType)) {
					retriever.applicationListeners.add(listener);
					allListeners.add(listener);
				}
			}
			if (!listenerBeans.isEmpty()) {
				BeanFactory beanFactory = getBeanFactory();
				for (String listenerBeanName : listenerBeans) {
					try {
						Class<?> listenerType = beanFactory.getType(listenerBeanName);
						if (listenerType == null || supportsEvent(listenerType, event)) {
							ApplicationListener<?> listener =
									beanFactory.getBean(listenerBeanName, ApplicationListener.class);
							if (!allListeners.contains(listener) && supportsEvent(listener, eventType, sourceType)) {
								retriever.applicationListenerBeans.add(listenerBeanName);
								allListeners.add(listener);
							}
						}
					}
					catch (NoSuchBeanDefinitionException ex) {
						// Singleton listener instance (without backing bean definition) disappeared -
						// probably in the middle of the destruction phase
					}
				}
			}
			OrderComparator.sort(allListeners);
			if (this.beanClassLoader == null ||
					(ClassUtils.isCacheSafe(eventType, this.beanClassLoader) &&
							(sourceType == null || ClassUtils.isCacheSafe(sourceType, this.beanClassLoader)))) {
				this.retrieverCache.put(cacheKey, retriever);
			}
			return allListeners;
		}
	}
{
		Assert.isTrue(timeout >= 0, "Timeout must be a non-negative value");
		getHttpClient().getParams().setIntParameter(org.apache.http.params.CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
	}
{
		Assert.isTrue(timeout >= 0, "Timeout must be a non-negative value");
		getHttpClient().getParams().setIntParameter(org.apache.http.params.CoreConnectionPNames.SO_TIMEOUT, timeout);
	}
{
		TransactionInterceptor ti = new TransactionInterceptor();
		ti.setBeanFactory(beanFactory);
		ti.setTransactionAttributeSource(new NameMatchTransactionAttributeSource());
		ti.afterPropertiesSet();
		return ti;
	}
{
		BeanFactory beanFactory = mock(BeanFactory.class);
		TransactionInterceptor ti = createTestTransactionInterceptor(beanFactory);

		PlatformTransactionManager txManager = mock(PlatformTransactionManager.class);
		given(beanFactory.containsBean("fooTransactionManager")).willReturn(true);
		given(beanFactory.getBean("fooTransactionManager", PlatformTransactionManager.class)).willReturn(txManager);

		DefaultTransactionAttribute attribute = new DefaultTransactionAttribute();
		attribute.setQualifier("fooTransactionManager");
		PlatformTransactionManager actual = ti.determineTransactionManager(attribute);
		assertSame(txManager, actual);

		// Call again, should be cached
		PlatformTransactionManager actual2 = ti.determineTransactionManager(attribute);
		assertSame(txManager, actual2);
		verify(beanFactory, times(1)).containsBean("fooTransactionManager");
		verify(beanFactory, times(1)).getBean("fooTransactionManager", PlatformTransactionManager.class);
	}
{
		this.asyncRequestControl.start(-1);
		if (this.messageCache.size() > 0) {
			flushCache();
		}
		else {
			scheduleHeartbeat();
		}
		this.requestInitialized = true;
	}
{
		this.asyncRequestControl.start(-1);
		if (this.messageCache.size() > 0) {
			flushCache();
		}
		else {
			scheduleHeartbeat();
		}
		this.requestInitialized = true;
	}
{
		synchronized (this.responseLock) {
			if (this.messageCache.isEmpty()) {
				logger.trace("Nothing to flush in session=" + this.getId());
				return false;
			}
			if (logger.isTraceEnabled()) {
				logger.trace(this.messageCache.size() + " message(s) to flush in session " + this.getId());
			}
			if (isActive() && this.requestInitialized) {
				if (logger.isTraceEnabled()) {
					logger.trace("Session is active, ready to flush.");
				}
				cancelHeartbeat();
				flushCache();
				return true;
			}
			else {
				if (logger.isTraceEnabled()) {
					logger.trace("Session is not active, not ready to flush.");
				}
				return false;
			}
		}
	}
{
		Assert.notNull(method, "Method must not be null");
		PropertyDescriptor[] pds = getPropertyDescriptors(method.getDeclaringClass());
		for (PropertyDescriptor pd : pds) {
			if (method.equals(pd.getReadMethod()) || method.equals(pd.getWriteMethod())) {
				return pd;
			}
		}
		return null;
	}
{
		String canonicalName = canonicalName(beanName);
		Set<String> dependentBeans = this.dependentBeanMap.get(canonicalName);
		if (dependentBeans == null) {
			return false;
		}
		if (dependentBeans.contains(dependentBeanName)) {
			return true;
		}
		for (String transitiveDependency : dependentBeans) {
			if (isDependent(transitiveDependency, dependentBeanName)) {
				return true;
			}
		}
		return false;
	}
{
		String scheme = request.getScheme();
		String host = request.getServerName();
		int port = request.getServerPort();

		String hostHeader = request.getHeader("X-Forwarded-Host");
		if (StringUtils.hasText(hostHeader)) {
			String[] hosts = StringUtils.commaDelimitedListToStringArray(hostHeader);
			String hostToUse = hosts[0];
			if (hostToUse.contains(":")) {
				String[] hostAndPort = StringUtils.split(hostToUse, ":");
				host  = hostAndPort[0];
				port = Integer.parseInt(hostAndPort[1]);
			}
			else {
				host = hostToUse;
				port = -1;
			}
		}

		String portHeader = request.getHeader("X-Forwarded-Port");
		if (StringUtils.hasText(portHeader)) {
			port = Integer.parseInt(portHeader);
		}

		String protocolHeader = request.getHeader("X-Forwarded-Proto");
		if (StringUtils.hasText(protocolHeader)) {
			scheme = protocolHeader;
		}

		String path = request.getRequestURI();
		path = prependForwardedPrefix(request, path);

		ServletUriComponentsBuilder builder = new ServletUriComponentsBuilder();
		builder.scheme(scheme);
		builder.host(host);
		if (scheme.equals("http") && port != 80 || scheme.equals("https") && port != 443) {
			builder.port(port);
		}
		builder.initPath(path);
		builder.query(request.getQueryString());
		return builder;
	}
{
		String scheme = request.getScheme();
		String host = request.getServerName();
		int port = request.getServerPort();
		String path = request.getRequestURI();

		String hostHeader = request.getHeader("X-Forwarded-Host");
		if (StringUtils.hasText(hostHeader)) {
			String[] hosts = StringUtils.commaDelimitedListToStringArray(hostHeader);
			String hostToUse = hosts[0];
			if (hostToUse.contains(":")) {
				String[] hostAndPort = StringUtils.split(hostToUse, ":");
				host  = hostAndPort[0];
				port = Integer.parseInt(hostAndPort[1]);
			}
			else {
				host = hostToUse;
				port = -1;
			}
		}

		String portHeader = request.getHeader("X-Forwarded-Port");
		if (StringUtils.hasText(portHeader)) {
			port = Integer.parseInt(portHeader);
		}

		String protocolHeader = request.getHeader("X-Forwarded-Proto");
		if (StringUtils.hasText(protocolHeader)) {
			scheme = protocolHeader;
		}

		String prefix = request.getHeader("X-Forwarded-Prefix");
		if (StringUtils.hasText(prefix)) {
			path = prefix + path;
		}

		ServletUriComponentsBuilder builder = new ServletUriComponentsBuilder();
		builder.scheme(scheme);
		builder.host(host);
		if (scheme.equals("http") && port != 80 || scheme.equals("https") && port != 443) {
			builder.port(port);
		}
		builder.initPath(path);
		builder.query(request.getQueryString());
		return builder;
	}
{
		List<MessageConverter> converters = new ArrayList<MessageConverter>();
		boolean registerDefaults = configureMessageConverters(converters);
		if (registerDefaults) {
			converters.add(new StringMessageConverter());
			converters.add(new ByteArrayMessageConverter());
			if (jackson2Present) {
				DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
				resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
				MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
				converter.setContentTypeResolver(resolver);
				converters.add(converter);
			}
		}
		return new CompositeMessageConverter(converters);
	}
{
		Assert.notNull(objectMapper, "ObjectMapper must not be null");

		if (this.dateFormat != null) {
			objectMapper.setDateFormat(this.dateFormat);
		}

		if (this.annotationIntrospector != null) {
			objectMapper.setAnnotationIntrospector(this.annotationIntrospector);
		}

		if (this.serializationInclusion != null) {
			objectMapper.setSerializationInclusion(this.serializationInclusion);
		}

		if (!this.serializers.isEmpty() || !this.deserializers.isEmpty()) {
			SimpleModule module = new SimpleModule();
			addSerializers(module);
			addDeserializers(module);
			objectMapper.registerModule(module);
		}

		if (!this.features.containsKey(MapperFeature.DEFAULT_VIEW_INCLUSION)) {
			configureFeature(objectMapper, MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		}
		if (!this.features.containsKey(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)) {
			configureFeature(objectMapper, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
		for (Object feature : this.features.keySet()) {
			configureFeature(objectMapper, feature, this.features.get(feature));
		}

		if (this.modules != null) {
			// Complete list of modules given
			for (Module module : this.modules) {
				// Using Jackson 2.0+ registerModule method, not Jackson 2.2+ registerModules
				objectMapper.registerModule(module);
			}
		}
		else {
			// Combination of modules by class names specified and class presence in the classpath
			if (this.modulesToInstall != null) {
				for (Class<? extends Module> module : this.modulesToInstall) {
					objectMapper.registerModule(BeanUtils.instantiate(module));
				}
			}
			if (this.findModulesViaServiceLoader) {
				// Jackson 2.2+
				objectMapper.registerModules(ObjectMapper.findModules(this.moduleClassLoader));
			}
			else {
				registerWellKnownModulesIfAvailable(objectMapper);
			}
		}

		if (this.propertyNamingStrategy != null) {
			objectMapper.setPropertyNamingStrategy(this.propertyNamingStrategy);
		}
		for (Class<?> target : this.mixIns.keySet()) {
			objectMapper.addMixInAnnotations(target, this.mixIns.get(target));
		}
	}
{

		MessageHeaders headers = message.getHeaders();
		SimpMessageType messageType = SimpMessageHeaderAccessor.getMessageType(headers);
		String destination = SimpMessageHeaderAccessor.getDestination(headers);
		String sessionId = SimpMessageHeaderAccessor.getSessionId(headers);

		if (!checkDestinationPrefix(destination)) {
			return;
		}

		SimpMessageHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, SimpMessageHeaderAccessor.class);
		if (accessor == null) {
			throw new IllegalStateException(
					"No header accessor (not using the SimpMessagingTemplate?): " + message);
		}

		if (SimpMessageType.MESSAGE.equals(messageType)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Processing " + accessor.getShortLogMessage(message.getPayload()));
			}
			sendMessageToSubscribers(destination, message);
		}
		else if (SimpMessageType.CONNECT.equals(messageType)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Processing " + accessor.getShortLogMessage(EMPTY_PAYLOAD));
			}
			SimpMessageHeaderAccessor connectAck = SimpMessageHeaderAccessor.create(SimpMessageType.CONNECT_ACK);
			initHeaders(connectAck);
			connectAck.setSessionId(sessionId);
			connectAck.setUser(SimpMessageHeaderAccessor.getUser(headers));
			connectAck.setHeader(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER, message);
			Message<byte[]> messageOut = MessageBuilder.createMessage(EMPTY_PAYLOAD, connectAck.getMessageHeaders());
			getClientOutboundChannel().send(messageOut);
		}
		else if (SimpMessageType.DISCONNECT.equals(messageType)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Processing " + accessor.getShortLogMessage(EMPTY_PAYLOAD));
			}
			this.subscriptionRegistry.unregisterAllSubscriptions(sessionId);
			SimpMessageHeaderAccessor disconnectAck = SimpMessageHeaderAccessor.create(SimpMessageType.DISCONNECT_ACK);
			initHeaders(disconnectAck);
			disconnectAck.setSessionId(sessionId);
			disconnectAck.setUser(SimpMessageHeaderAccessor.getUser(headers));
			Message<byte[]> messageOut = MessageBuilder.createMessage(EMPTY_PAYLOAD, disconnectAck.getMessageHeaders());
			getClientOutboundChannel().send(messageOut);
		}
		else if (SimpMessageType.SUBSCRIBE.equals(messageType)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Processing " + accessor.getShortLogMessage(EMPTY_PAYLOAD));
			}
			this.subscriptionRegistry.registerSubscription(message);
		}
		else if (SimpMessageType.UNSUBSCRIBE.equals(messageType)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Processing " + accessor.getShortLogMessage(EMPTY_PAYLOAD));
			}
			this.subscriptionRegistry.unregisterSubscription(message);
		}
	}
{
		if (collectionType.isInterface()) {
			if (List.class.equals(collectionType)) {
				return new ArrayList<E>(initialCapacity);
			}
			else if (SortedSet.class.equals(collectionType) || NavigableSet.class.equals(collectionType)) {
				return new TreeSet<E>();
			}
			else if (Set.class.equals(collectionType) || Collection.class.equals(collectionType)) {
				return new LinkedHashSet<E>(initialCapacity);
			}
			else {
				throw new IllegalArgumentException("Unsupported Collection interface: " + collectionType.getName());
			}
		}
		else {
			if (!Collection.class.isAssignableFrom(collectionType)) {
				throw new IllegalArgumentException("Unsupported Collection type: " + collectionType.getName());
			}
			try {
				return (Collection<E>) collectionType.newInstance();
			}
			catch (Exception ex) {
				throw new IllegalArgumentException("Could not instantiate Collection type: " +
						collectionType.getName(), ex);
			}
		}
	}
{
		if (mapType.isInterface()) {
			if (Map.class.equals(mapType)) {
				return new LinkedHashMap<K, V>(initialCapacity);
			}
			else if (SortedMap.class.equals(mapType) || NavigableMap.class.equals(mapType)) {
				return new TreeMap<K, V>();
			}
			else if (MultiValueMap.class.equals(mapType)) {
				return new LinkedMultiValueMap();
			}
			else {
				throw new IllegalArgumentException("Unsupported Map interface: " + mapType.getName());
			}
		}
		else {
			if (!Map.class.isAssignableFrom(mapType)) {
				throw new IllegalArgumentException("Unsupported Map type: " + mapType.getName());
			}
			try {
				return (Map<K, V>) mapType.newInstance();
			}
			catch (Exception ex) {
				throw new IllegalArgumentException("Could not instantiate Map type: " +
						mapType.getName(), ex);
			}
		}
	}
{
		// Quick check on the concurrent map first, with minimal locking.
		// Fall back to class name as cache key, for backwards compatibility with custom callers.
		String cacheKey = (StringUtils.hasLength(beanName) ? beanName : clazz.getName());
		InjectionMetadata metadata = this.injectionMetadataCache.get(cacheKey);
		if (InjectionMetadata.needsRefresh(metadata, clazz)) {
			synchronized (this.injectionMetadataCache) {
				metadata = this.injectionMetadataCache.get(cacheKey);
				if (InjectionMetadata.needsRefresh(metadata, clazz)) {
					LinkedList<InjectionMetadata.InjectedElement> elements = new LinkedList<InjectionMetadata.InjectedElement>();
					Class<?> targetClass = clazz;

					do {
						LinkedList<InjectionMetadata.InjectedElement> currElements = new LinkedList<InjectionMetadata.InjectedElement>();
						for (Field field : targetClass.getDeclaredFields()) {
							if (webServiceRefClass != null && field.isAnnotationPresent(webServiceRefClass)) {
								if (Modifier.isStatic(field.getModifiers())) {
									throw new IllegalStateException("@WebServiceRef annotation is not supported on static fields");
								}
								currElements.add(new WebServiceRefElement(field, null));
							}
							else if (ejbRefClass != null && field.isAnnotationPresent(ejbRefClass)) {
								if (Modifier.isStatic(field.getModifiers())) {
									throw new IllegalStateException("@EJB annotation is not supported on static fields");
								}
								currElements.add(new EjbRefElement(field, null));
							}
							else if (field.isAnnotationPresent(Resource.class)) {
								if (Modifier.isStatic(field.getModifiers())) {
									throw new IllegalStateException("@Resource annotation is not supported on static fields");
								}
								if (!ignoredResourceTypes.contains(field.getType().getName())) {
									currElements.add(new ResourceElement(field, null));
								}
							}
						}
						for (Method method : targetClass.getDeclaredMethods()) {
							if (!method.isBridge() && method.equals(ClassUtils.getMostSpecificMethod(method, clazz))) {
								if (webServiceRefClass != null && method.isAnnotationPresent(webServiceRefClass)) {
									if (Modifier.isStatic(method.getModifiers())) {
										throw new IllegalStateException("@WebServiceRef annotation is not supported on static methods");
									}
									if (method.getParameterTypes().length != 1) {
										throw new IllegalStateException("@WebServiceRef annotation requires a single-arg method: " + method);
									}
									PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
									currElements.add(new WebServiceRefElement(method, pd));
								}
								else if (ejbRefClass != null && method.isAnnotationPresent(ejbRefClass)) {
									if (Modifier.isStatic(method.getModifiers())) {
										throw new IllegalStateException("@EJB annotation is not supported on static methods");
									}
									if (method.getParameterTypes().length != 1) {
										throw new IllegalStateException("@EJB annotation requires a single-arg method: " + method);
									}
									PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
									currElements.add(new EjbRefElement(method, pd));
								}
								else if (method.isAnnotationPresent(Resource.class)) {
									if (Modifier.isStatic(method.getModifiers())) {
										throw new IllegalStateException("@Resource annotation is not supported on static methods");
									}
									Class<?>[] paramTypes = method.getParameterTypes();
									if (paramTypes.length != 1) {
										throw new IllegalStateException("@Resource annotation requires a single-arg method: " + method);
									}
									if (!ignoredResourceTypes.contains(paramTypes[0].getName())) {
										PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
										currElements.add(new ResourceElement(method, pd));
									}
								}
							}
						}
						elements.addAll(0, currElements);
						targetClass = targetClass.getSuperclass();
					}
					while (targetClass != null && targetClass != Object.class);

					metadata = new InjectionMetadata(clazz, elements);
					this.injectionMetadataCache.put(cacheKey, metadata);
				}
			}
		}
		return metadata;
	}
{
		// Quick check on the concurrent map first, with minimal locking.
		// Fall back to class name as cache key, for backwards compatibility with custom callers.
		String cacheKey = (StringUtils.hasLength(beanName) ? beanName : clazz.getName());
		InjectionMetadata metadata = this.injectionMetadataCache.get(cacheKey);
		if (InjectionMetadata.needsRefresh(metadata, clazz)) {
			synchronized (this.injectionMetadataCache) {
				metadata = this.injectionMetadataCache.get(cacheKey);
				if (InjectionMetadata.needsRefresh(metadata, clazz)) {
					LinkedList<InjectionMetadata.InjectedElement> elements = new LinkedList<InjectionMetadata.InjectedElement>();
					Class<?> targetClass = clazz;

					do {
						LinkedList<InjectionMetadata.InjectedElement> currElements = new LinkedList<InjectionMetadata.InjectedElement>();
						for (Field field : targetClass.getDeclaredFields()) {
							PersistenceContext pc = field.getAnnotation(PersistenceContext.class);
							PersistenceUnit pu = field.getAnnotation(PersistenceUnit.class);
							if (pc != null || pu != null) {
								if (Modifier.isStatic(field.getModifiers())) {
									throw new IllegalStateException("Persistence annotations are not supported on static fields");
								}
								currElements.add(new PersistenceElement(field, null));
							}
						}
						for (Method method : targetClass.getDeclaredMethods()) {
							PersistenceContext pc = method.getAnnotation(PersistenceContext.class);
							PersistenceUnit pu = method.getAnnotation(PersistenceUnit.class);
							if ((pc != null || pu != null) && !method.isBridge() &&
									method.equals(ClassUtils.getMostSpecificMethod(method, clazz))) {
								if (Modifier.isStatic(method.getModifiers())) {
									throw new IllegalStateException("Persistence annotations are not supported on static methods");
								}
								if (method.getParameterTypes().length != 1) {
									throw new IllegalStateException("Persistence annotation requires a single-arg method: " + method);
								}
								PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
								currElements.add(new PersistenceElement(method, pd));
							}
						}
						elements.addAll(0, currElements);
						targetClass = targetClass.getSuperclass();
					}
					while (targetClass != null && targetClass != Object.class);

					metadata = new InjectionMetadata(clazz, elements);
					this.injectionMetadataCache.put(cacheKey, metadata);
				}
			}
		}
		return metadata;
	}
{
		return this.allowBeanDefinitionOverriding;
	}
{
		if (value instanceof String) {
			return this.beanFactory.evaluateBeanDefinitionString((String) value, this.beanDefinition);
		}
		else {
			return value;
		}
	}
{
		synchronized (this.connectionMonitor) {
			if (this.connection == null) {
				initConnection();
			}
			return this.connection;
		}
	}
{
			if (method.getName().equals("equals")) {
				// Only consider equal when proxies are identical.
				return (proxy == args[0]);
			}
			else if (method.getName().equals("hashCode")) {
				// Use hashCode of Connection proxy.
				return System.identityHashCode(proxy);
			}
			else if (method.getName().equals("toString")) {
				return "Shared JMS Connection: " + this.target;
			}
			else if (method.getName().equals("setClientID")) {
				// Handle setClientID method: throw exception if not compatible.
				String currentClientId = this.target.getClientID();
				if (currentClientId != null && currentClientId.equals(args[0])) {
					return null;
				}
				else {
					throw new javax.jms.IllegalStateException(
							"setClientID call not supported on proxy for shared Connection. " +
							"Set the 'clientId' property on the SingleConnectionFactory instead.");
				}
			}
			else if (method.getName().equals("setExceptionListener")) {
				// Handle setExceptionListener method: add to the chain.
				ExceptionListener currentExceptionListener = this.target.getExceptionListener();
				if (currentExceptionListener instanceof InternalChainedExceptionListener && args[0] != null) {
					((InternalChainedExceptionListener) currentExceptionListener).addDelegate((ExceptionListener) args[0]);
					return null;
				}
				else {
					throw new javax.jms.IllegalStateException(
							"setExceptionListener call not supported on proxy for shared Connection. " +
							"Set the 'exceptionListener' property on the SingleConnectionFactory instead. " +
							"Alternatively, activate SingleConnectionFactory's 'reconnectOnException' feature, " +
							"which will allow for registering further ExceptionListeners to the recovery chain.");
				}
			}
			else if (method.getName().equals("start")) {
				// Handle start method: track started state.
				synchronized (connectionMonitor) {
					if (!started) {
						this.target.start();
						started = true;
					}
				}
				return null;
			}
			else if (method.getName().equals("stop")) {
				// Handle stop method: don't pass the call on.
				return null;
			}
			else if (method.getName().equals("close")) {
				// Handle close method: don't pass the call on.
				return null;
			}
			else if (method.getName().equals("createSession") || method.getName().equals("createQueueSession") ||
					method.getName().equals("createTopicSession")) {
				// Default: JMS 2.0 createSession() method
				Integer mode = Session.AUTO_ACKNOWLEDGE;
				if (args != null) {
					if (args.length == 1) {
						// JMS 2.0 createSession(int) method
						mode = (Integer) args[0];
					}
					else if (args.length == 2) {
						// JMS 1.1 createSession(boolean, int) method
						boolean transacted = (Boolean) args[0];
						Integer ackMode = (Integer) args[1];
						mode = (transacted ? Session.SESSION_TRANSACTED : ackMode);
					}
				}
				Session session = getSession(this.target, mode);
				if (session != null) {
					if (!method.getReturnType().isInstance(session)) {
						String msg = "JMS Session does not implement specific domain: " + session;
						try {
							session.close();
						}
						catch (Throwable ex) {
							logger.trace("Failed to close newly obtained JMS Session", ex);
						}
						throw new javax.jms.IllegalStateException(msg);
					}
					return session;
				}
			}
			try {
				Object retVal = method.invoke(this.target, args);
				if (method.getName().equals("getExceptionListener") && retVal instanceof InternalChainedExceptionListener) {
					// Handle getExceptionListener method: hide internal chain.
					InternalChainedExceptionListener listener = (InternalChainedExceptionListener) retVal;
					return listener.getUserListener();
				}
				else {
					return retVal;
				}
			}
			catch (InvocationTargetException ex) {
				throw ex.getTargetException();
			}
		}
{
		HibernateTransactionObject txObject = (HibernateTransactionObject) transaction;

		// Remove the session holder from the thread.
		if (txObject.isNewSessionHolder()) {
			TransactionSynchronizationManager.unbindResource(getSessionFactory());
		}

		// Remove the JDBC connection holder from the thread, if exposed.
		if (getDataSource() != null) {
			TransactionSynchronizationManager.unbindResource(getDataSource());
		}

		Session session = txObject.getSessionHolder().getSession();
		if (this.prepareConnection && session.isConnected() && isSameConnectionForEntireSession(session)) {
			// We're running with connection release mode "on_close": We're able to reset
			// the isolation level and/or read-only flag of the JDBC Connection here.
			// Else, we need to rely on the connection pool to perform proper cleanup.
			try {
				Connection con = ((SessionImplementor) session).connection();
				DataSourceUtils.resetConnectionAfterTransaction(con, txObject.getPreviousIsolationLevel());
			}
			catch (HibernateException ex) {
				logger.debug("Could not access JDBC Connection of Hibernate Session", ex);
			}
		}

		if (txObject.isNewSession()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Closing Hibernate Session [" + session + "] after transaction");
			}
			SessionFactoryUtils.closeSession(session);
		}
		else {
			if (logger.isDebugEnabled()) {
				logger.debug("Not closing pre-bound Hibernate Session [" + session + "] after transaction");
			}
			if (txObject.getSessionHolder().getPreviousFlushMode() != null) {
				session.setFlushMode(txObject.getSessionHolder().getPreviousFlushMode());
			}
			if (!this.hibernateManagedSession) {
				session.disconnect();
			}
		}
		txObject.getSessionHolder().clear();
	}
{
		if (annotationType.isInstance(ann)) {
			return (T) ann;
		}
		try {
			return ann.annotationType().getAnnotation(annotationType);
		}
		catch (Exception ex) {
			// Assuming nested Class values not resolvable within annotation attributes...
			// We're probably hitting a non-present optional arrangement - let's back out.
			if (logger.isInfoEnabled()) {
				logger.info("Failed to introspect annotations on [" + ann.annotationType() + "]: " + ex);
			}
			return null;
		}
	}
{
			String name = ResourceUrlProviderExposingInterceptor.RESOURCE_URL_PROVIDER_ATTR;
			ResourceUrlProvider urlProvider = (ResourceUrlProvider) this.request.getAttribute(name);
			if (urlProvider != null) {
				String translatedUrl = urlProvider.getForRequestUrl(this.request, url);
				if (translatedUrl != null) {
					return super.encodeURL(translatedUrl);
				}
			}
			else {
				logger.debug("Request attribute exposing ResourceUrlProvider not found under name: " + name);
			}
			return super.encodeURL(url);
		}
{
		VersionResourceResolver versionResolver = new VersionResourceResolver();
		versionResolver.setStrategyMap(Collections.singletonMap("/**", new ContentVersionStrategy()));

		List<ResourceResolver> resolvers = new ArrayList<>();
		resolvers.add(versionResolver);
		resolvers.add(new PathResourceResolver());
		this.transformerChain = new DefaultResourceTransformerChain(new DefaultResourceResolverChain(resolvers), null);

		List<Resource> locations = new ArrayList<>();
		locations.add(new ClassPathResource("test/", getClass()));

		ResourceHttpRequestHandler handler = new ResourceHttpRequestHandler();
		handler.setLocations(locations);
		handler.setResourceResolvers(resolvers);

		ResourceUrlProvider urlProvider = new ResourceUrlProvider();
		urlProvider.setHandlerMap(Collections.singletonMap("/resources/**", handler));

		this.transformer = new TestResourceTransformerSupport();
		this.transformer.setResourceUrlProvider(urlProvider);

		this.request = new MockHttpServletRequest();
	}
{

		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			HttpSession session = servletRequest.getServletRequest().getSession(false);
			if (session != null) {
				Enumeration<String> names = session.getAttributeNames();
				while (names.hasMoreElements()) {
					String name = names.nextElement();
					if (CollectionUtils.isEmpty(this.attributeNames) || this.attributeNames.contains(name)) {
						attributes.put(name, session.getAttribute(name));
					}
				}
				if (isCopyHttpSessionId()) {
					attributes.put(HTTP_SESSION_ID_ATTR_NAME, session.getId());
				}
			}
		}
		return true;
	}
{
		ClassPathXmlApplicationContext factory = new ClassPathXmlApplicationContext(
				"ValueInjectionTests.xml", AutowiredConfigurationTests.class);

		System.clearProperty("myProp");

		TestBean testBean = factory.getBean("testBean", TestBean.class);
		assertNull(testBean.getName());

		testBean = factory.getBean("testBean2", TestBean.class);
		assertNull(testBean.getName());

		System.setProperty("myProp", "foo");

		testBean = factory.getBean("testBean", TestBean.class);
		assertThat(testBean.getName(), equalTo("foo"));

		testBean = factory.getBean("testBean2", TestBean.class);
		assertThat(testBean.getName(), equalTo("foo"));

		System.clearProperty("myProp");

		testBean = factory.getBean("testBean", TestBean.class);
		assertNull(testBean.getName());

		testBean = factory.getBean("testBean2", TestBean.class);
		assertNull(testBean.getName());
	}
{
		String username = getUsername();
		String password = getPassword();
		if ("".equals(username)) {  // probably from a placeholder
			username = null;
			if ("".equals(password)) {  // in conjunction with "" username, this means no password to use
				password = null;
			}
		}

		Map<Object, Exception> failedMessages = new LinkedHashMap<Object, Exception>();
		Transport transport;
		try {
			transport = getTransport(getSession());
			transport.connect(getHost(), getPort(), username, password);
		}
		catch (AuthenticationFailedException ex) {
			throw new MailAuthenticationException(ex);
		}
		catch (MessagingException ex) {
			// Effectively, all messages failed...
			for (int i = 0; i < mimeMessages.length; i++) {
				Object original = (originalMessages != null ? originalMessages[i] : mimeMessages[i]);
				failedMessages.put(original, ex);
			}
			throw new MailSendException("Mail server connection failed", ex, failedMessages);
		}

		try {
			for (int i = 0; i < mimeMessages.length; i++) {
				MimeMessage mimeMessage = mimeMessages[i];
				try {
					if (mimeMessage.getSentDate() == null) {
						mimeMessage.setSentDate(new Date());
					}
					String messageId = mimeMessage.getMessageID();
					mimeMessage.saveChanges();
					if (messageId != null) {
						// Preserve explicitly specified message id...
						mimeMessage.setHeader(HEADER_MESSAGE_ID, messageId);
					}
					transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
				}
				catch (MessagingException ex) {
					Object original = (originalMessages != null ? originalMessages[i] : mimeMessage);
					failedMessages.put(original, ex);
				}
			}
		}
		finally {
			try {
				transport.close();
			}
			catch (MessagingException ex) {
				if (!failedMessages.isEmpty()) {
					throw new MailSendException("Failed to close server connection after message failures", ex,
							failedMessages);
				}
				else {
					throw new MailSendException("Failed to close server connection after message sending", ex);
				}
			}
		}

		if (!failedMessages.isEmpty()) {
			throw new MailSendException(failedMessages);
		}
	}
{
		if (this.objectMapper == null) {
			if(this.createXmlMapper) {
				ClassLoader cl = getClassLoader();
				try {
					Class<? extends ObjectMapper> xmlMapper = (Class<? extends ObjectMapper>)
							cl.loadClass("com.fasterxml.jackson.dataformat.xml.XmlMapper");
					this.objectMapper = BeanUtils.instantiate(xmlMapper);
				}
				catch (ClassNotFoundException ex) {
					throw new IllegalStateException("Could not instantiate XmlMapper, it has not been found on the classpath");
				}
			}
			else {
				this.objectMapper = new ObjectMapper();
			}
		}

		if (this.dateFormat != null) {
			this.objectMapper.setDateFormat(this.dateFormat);
		}

		if (this.annotationIntrospector != null) {
			this.objectMapper.setAnnotationIntrospector(this.annotationIntrospector);
		}

		if (this.serializationInclusion != null) {
			this.objectMapper.setSerializationInclusion(this.serializationInclusion);
		}

		if (!this.serializers.isEmpty() || !this.deserializers.isEmpty()) {
			SimpleModule module = new SimpleModule();
			addSerializers(module);
			addDeserializers(module);
			this.objectMapper.registerModule(module);
		}

		if(!features.containsKey(MapperFeature.DEFAULT_VIEW_INCLUSION)) {
			configureFeature(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		}
		if(!features.containsKey(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)) {
			configureFeature(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
		for (Object feature : this.features.keySet()) {
			configureFeature(feature, this.features.get(feature));
		}

		if (this.modules != null) {
			// Complete list of modules given
			for (Module module : this.modules) {
				// Using Jackson 2.0+ registerModule method, not Jackson 2.2+ registerModules
				this.objectMapper.registerModule(module);
			}
		}
		else {
			// Combination of modules by class names specified and class presence in the classpath
			if (this.modulesToInstall != null) {
				for (Class<? extends Module> module : this.modulesToInstall) {
					this.objectMapper.registerModule(BeanUtils.instantiate(module));
				}
			}
			if (this.findModulesViaServiceLoader) {
				// Jackson 2.2+
				this.objectMapper.registerModules(ObjectMapper.findModules(getClassLoader()));
			}
			else {
				registerWellKnownModulesIfAvailable();
			}
		}

		if (this.propertyNamingStrategy != null) {
			this.objectMapper.setPropertyNamingStrategy(this.propertyNamingStrategy);
		}

		return (T)this.objectMapper;
	}
{
		EvaluationContext evaluationContext = state.getEvaluationContext();
		Object value = state.getActiveContextObject().getValue();
		TypeDescriptor targetType = state.getActiveContextObject().getTypeDescriptor();
		Object[] arguments = getArguments(state);
		TypedValue result = getValueInternal(evaluationContext, value, targetType, arguments);
		if (cachedExecutor.get() instanceof ReflectiveMethodExecutor) {
			ReflectiveMethodExecutor executor = (ReflectiveMethodExecutor) cachedExecutor.get();
			Method method = executor.getMethod();
			exitTypeDescriptor = CodeFlow.toDescriptor(method.getReturnType());
		}
		return result;
	}
{
		String path = location;
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		ClassLoader cl = getClassLoader();
		Enumeration<URL> resourceUrls = (cl != null ? cl.getResources(path) : ClassLoader.getSystemResources(path));
		Set<Resource> result = new LinkedHashSet<Resource>(16);
		while (resourceUrls.hasMoreElements()) {
			URL url = resourceUrls.nextElement();
			result.add(convertClassLoaderURL(url));
		}
		if ("".equals(path)) {
			// The above result is likely to be incomplete, i.e. only containing file system references.
			// We need to have pointers to each of the jar files on the classpath as well...
			addAllClassLoaderJarRoots(cl, result);
		}
		postProcessFindAllClassPathResourcesResult(location, result);
		return result.toArray(new Resource[result.size()]);
	}
{
		for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
			String headerName = entry.getKey();
			for (String headerValue : entry.getValue()) {
				this.connection.addRequestProperty(headerName, headerValue);
			}
		}

		if (this.connection.getDoOutput() && this.outputStreaming) {
			this.connection.setFixedLengthStreamingMode(bufferedOutput.length);
		}
		this.connection.connect();
		if (this.connection.getDoOutput()) {
			FileCopyUtils.copy(bufferedOutput, this.connection.getOutputStream());
		}

		return new SimpleClientHttpResponse(this.connection);
	}
{
		if (this.dependencyComparator instanceof OrderProviderComparator) {
			((OrderProviderComparator) this.dependencyComparator)
					.sortArray(items, createFactoryAwareOrderProvider(matchingBeans));
		}
		else {
			Arrays.sort(items, this.dependencyComparator);
		}
	}
{
		boolean p1 = (o1 instanceof PriorityOrdered);
		boolean p2 = (o2 instanceof PriorityOrdered);
		if (p1 && !p2) {
			return -1;
		}
		else if (p2 && !p1) {
			return 1;
		}

		// Direct evaluation instead of Integer.compareTo to avoid unnecessary object creation.
		int i1 = getOrder(o1);
		int i2 = getOrder(o2);
		return (i1 < i2) ? -1 : (i1 > i2) ? 1 : 0;
	}
{
		String beanName = configClass.getBeanName();
		if (StringUtils.hasLength(beanName) && this.registry.containsBeanDefinition(beanName)) {
			this.registry.removeBeanDefinition(beanName);
		}
	}
{
		Class<?> targetType = mbd.getTargetType();
		if (targetType == null) {
			targetType = (mbd.getFactoryMethodName() != null ? getTypeForFactoryMethod(beanName, mbd, typesToMatch) :
					resolveBeanClass(mbd, beanName, typesToMatch));
			if (ObjectUtils.isEmpty(typesToMatch) || getTempClassLoader() == null) {
				mbd.setTargetType(targetType);
			}
		}
		// Apply SmartInstantiationAwareBeanPostProcessors to predict the
		// eventual type after a before-instantiation shortcut.
		if (targetType != null && !mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
			for (BeanPostProcessor bp : getBeanPostProcessors()) {
				if (bp instanceof SmartInstantiationAwareBeanPostProcessor) {
					SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor) bp;
					Class<?> predicted = ibp.predictBeanType(targetType, beanName);
					if (predicted != null && (typesToMatch.length != 1 || !FactoryBean.class.equals(typesToMatch[0]) ||
							FactoryBean.class.isAssignableFrom(predicted))) {
						return predicted;
					}
				}
			}
		}
		return targetType;
	}
{

		FlushMode flushMode = session.getFlushMode();
		FlushMode previousFlushMode = null;
		if (readOnly) {
			// We should suppress flushing for a read-only transaction.
			session.setFlushMode(FlushMode.MANUAL);
			previousFlushMode = flushMode;
		}
		else {
			// We need AUTO or COMMIT for a non-read-only transaction.
			if (flushMode.lessThan(FlushMode.COMMIT)) {
				session.setFlushMode(FlushMode.AUTO);
				previousFlushMode = flushMode;
			}
		}

		boolean resetConnection = (previousIsolationLevel != null || readOnly);
		return new SessionTransactionData(session, previousFlushMode, resetConnection, previousIsolationLevel);
	}
{
		if (StringUtils.hasText(jmsListener.id())) {
			return jmsListener.id();
		}
		else {
			return "org.springframework.jms.JmsListenerEndpointContainer#" + counter.getAndIncrement();
		}
	}
{
		String locationAttr = element.getAttribute("location");
		if (!StringUtils.hasText(locationAttr)) {
			parserContext.getReaderContext().error("The 'location' attribute is required.", parserContext.extractSource(element));
			return null;
		}

		ManagedList<String> locations = new ManagedList<String>();
		locations.addAll(Arrays.asList(StringUtils.commaDelimitedListToStringArray(locationAttr)));

		RootBeanDefinition resourceHandlerDef = new RootBeanDefinition(ResourceHttpRequestHandler.class);
		resourceHandlerDef.setSource(source);
		resourceHandlerDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		resourceHandlerDef.getPropertyValues().add("locations", locations);

		String cacheSeconds = element.getAttribute("cache-period");
		if (StringUtils.hasText(cacheSeconds)) {
			resourceHandlerDef.getPropertyValues().add("cacheSeconds", cacheSeconds);
		}

		ManagedList<? super Object> resourceResolvers = parseResourceResolvers(parserContext, element, source);
		if (!resourceResolvers.isEmpty()) {
			resourceHandlerDef.getPropertyValues().add("resourceResolvers", resourceResolvers);
		}

		ManagedList<? super Object> resourceTransformers = parseResourceTransformers(parserContext, element, source);
		if (!resourceTransformers.isEmpty()) {
			resourceHandlerDef.getPropertyValues().add("resourceTransformers", resourceTransformers);
		}

		String beanName = parserContext.getReaderContext().generateBeanName(resourceHandlerDef);
		parserContext.getRegistry().registerBeanDefinition(beanName, resourceHandlerDef);
		parserContext.registerComponent(new BeanComponentDefinition(resourceHandlerDef, beanName));
		return beanName;
	}
{
		Assert.notNull(cacheBuilder, "CacheBuilder must not be null");
		this.cacheBuilder = cacheBuilder;
	}
{
		String resolvedLocation = this.environment.resolveRequiredPlaceholders(location);
		Resource resource = this.resourceLoader.getResource(resolvedLocation);
		AnnotationPropertySource propertySource = (StringUtils.hasText(name) ?
				new AnnotationPropertySource(name, resource) : new AnnotationPropertySource(resource));
		addPropertySource(propertySource);
	}
{
		Class<?> clazz = getBootstrapContext().getTestClass();
		Class<TestExecutionListeners> annotationType = TestExecutionListeners.class;
		List<Class<? extends TestExecutionListener>> classesList = new ArrayList<Class<? extends TestExecutionListener>>();

		AnnotationDescriptor<TestExecutionListeners> descriptor = MetaAnnotationUtils.findAnnotationDescriptor(clazz,
			annotationType);

		// Use defaults?
		if (descriptor == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("@TestExecutionListeners is not present for class [" + clazz.getName()
						+ "]: using defaults.");
			}
			classesList.addAll(getDefaultTestExecutionListenerClasses());
		}
		else {
			// Traverse the class hierarchy...
			while (descriptor != null) {
				Class<?> declaringClass = descriptor.getDeclaringClass();
				AnnotationAttributes annAttrs = descriptor.getAnnotationAttributes();
				if (logger.isTraceEnabled()) {
					logger.trace(String.format(
						"Retrieved @TestExecutionListeners attributes [%s] for declaring class [%s].", annAttrs,
						declaringClass.getName()));
				}

				Class<? extends TestExecutionListener>[] valueListenerClasses = (Class<? extends TestExecutionListener>[]) annAttrs.getClassArray("value");
				Class<? extends TestExecutionListener>[] listenerClasses = (Class<? extends TestExecutionListener>[]) annAttrs.getClassArray("listeners");
				if (!ObjectUtils.isEmpty(valueListenerClasses) && !ObjectUtils.isEmpty(listenerClasses)) {
					throw new IllegalStateException(String.format(
						"Class [%s] configured with @TestExecutionListeners' "
								+ "'value' [%s] and 'listeners' [%s] attributes. Use one or the other, but not both.",
						declaringClass.getName(), ObjectUtils.nullSafeToString(valueListenerClasses),
						ObjectUtils.nullSafeToString(listenerClasses)));
				}
				else if (!ObjectUtils.isEmpty(valueListenerClasses)) {
					listenerClasses = valueListenerClasses;
				}

				if (listenerClasses != null) {
					classesList.addAll(0, Arrays.<Class<? extends TestExecutionListener>> asList(listenerClasses));
				}
				descriptor = (annAttrs.getBoolean("inheritListeners") ? MetaAnnotationUtils.findAnnotationDescriptor(
					descriptor.getRootDeclaringClass().getSuperclass(), annotationType) : null);
			}
		}

		List<TestExecutionListener> listeners = new ArrayList<TestExecutionListener>(classesList.size());
		for (Class<? extends TestExecutionListener> listenerClass : classesList) {
			NoClassDefFoundError ncdfe = null;
			try {
				listeners.add(BeanUtils.instantiateClass(listenerClass));
			}
			catch (NoClassDefFoundError err) {
				ncdfe = err;
			}
			catch (BeanInstantiationException ex) {
				if (ex.getCause() instanceof NoClassDefFoundError) {
					ncdfe = (NoClassDefFoundError) ex.getCause();
				}
			}
			if (ncdfe != null) {
				if (logger.isInfoEnabled()) {
					logger.info(String.format("Could not instantiate TestExecutionListener [%s]. "
							+ "Specify custom listener classes or make the default listener classes "
							+ "(and their required dependencies) available. Offending class: [%s]",
						listenerClass.getName(), ncdfe.getMessage()));
				}
			}
		}
		return listeners;
	}
{
		context.getEnvironment().setActiveProfiles(mergedConfig.getActiveProfiles());

		Set<Class<? extends ApplicationContextInitializer<? extends ConfigurableApplicationContext>>> initializerClasses = mergedConfig.getContextInitializerClasses();
		if (initializerClasses.isEmpty()) {
			// no ApplicationContextInitializers have been declared -> nothing to do
			return;
		}

		List<ApplicationContextInitializer<ConfigurableApplicationContext>> initializerInstances = new ArrayList<ApplicationContextInitializer<ConfigurableApplicationContext>>();
		Class<?> contextClass = context.getClass();

		for (Class<? extends ApplicationContextInitializer<? extends ConfigurableApplicationContext>> initializerClass : initializerClasses) {
			Class<?> initializerContextClass = GenericTypeResolver.resolveTypeArgument(initializerClass,
				ApplicationContextInitializer.class);
			Assert.isAssignable(initializerContextClass, contextClass, String.format(
				"Could not add context initializer [%s] since its generic parameter [%s] "
						+ "is not assignable from the type of application context used by this "
						+ "context loader [%s]: ", initializerClass.getName(), initializerContextClass.getName(),
				contextClass.getName()));
			initializerInstances.add((ApplicationContextInitializer<ConfigurableApplicationContext>) BeanUtils.instantiateClass(initializerClass));
		}

		AnnotationAwareOrderComparator.sort(initializerInstances);
		for (ApplicationContextInitializer<ConfigurableApplicationContext> initializer : initializerInstances) {
			initializer.initialize(context);
		}
	}
{
		Assert.notNull(requiredType, "Required type must not be null");
		String[] beanNames = getBeanNamesForType(requiredType);
		if (beanNames.length > 1) {
			ArrayList<String> autowireCandidates = new ArrayList<String>();
			for (String beanName : beanNames) {
				if (getBeanDefinition(beanName).isAutowireCandidate()) {
					autowireCandidates.add(beanName);
				}
			}
			if (autowireCandidates.size() > 0) {
				beanNames = autowireCandidates.toArray(new String[autowireCandidates.size()]);
			}
		}
		if (beanNames.length == 1) {
			return getBean(beanNames[0], requiredType);
		}
		else if (beanNames.length > 1) {
			Map<String, Object> candidates = new HashMap<String, Object>();
			for (String beanName : beanNames) {
				candidates.put(beanName, getBean(beanName, requiredType));
			}
			String primaryCandidate = determinePrimaryCandidate(candidates, requiredType);
			if (primaryCandidate != null) {
				return getBean(primaryCandidate, requiredType);
			}
			String priorityCandidate = determineHighestPriorityCandidate(candidates, requiredType);
			if (priorityCandidate != null) {
				return getBean(priorityCandidate, requiredType);
			}
			throw new NoUniqueBeanDefinitionException(requiredType, candidates.keySet());
		}
		else if (getParentBeanFactory() != null) {
			return getParentBeanFactory().getBean(requiredType);
		}
		else {
			throw new NoSuchBeanDefinitionException(requiredType);
		}
	}
{
		if (message == null) {
			return null;
		}
		try {
			return (Message<?>) this.jmsMessageConverter.fromMessage(message);
		}
		catch (JMSException ex) {
			throw new MessageConversionException("Could not convert '" + message + "'", ex);
		}
		catch (JmsException ex) {
			throw new MessageConversionException("Could not convert '" + message + "'", ex);
		}
	}
{

		Session session = getSession(entityManager);
		FlushMode flushMode = session.getFlushMode();
		FlushMode previousFlushMode = null;
		if (readOnly) {
			// We should suppress flushing for a read-only transaction.
			session.setFlushMode(FlushMode.MANUAL);
			previousFlushMode = flushMode;
		}
		else {
			// We need AUTO or COMMIT for a non-read-only transaction.
			if (flushMode.lessThan(FlushMode.COMMIT)) {
				session.setFlushMode(FlushMode.AUTO);
				previousFlushMode = flushMode;
			}
		}
		return new SessionTransactionData(session, previousFlushMode);
	}
{
			try {
				if (connectionMethodToUse == null) {
					// Reflective lookup trying to find SessionImpl's connection() on Hibernate 4.x
					connectionMethodToUse = this.session.getClass().getMethod("connection");
				}
				return (Connection) ReflectionUtils.invokeMethod(connectionMethodToUse, this.session);
			}
			catch (NoSuchMethodException ex) {
				throw new IllegalStateException("Cannot find connection() method on Hibernate Session", ex);
			}
		}
{

		AnnotationAttributes attrs = new AnnotationAttributes();
		Method[] methods = annotation.annotationType().getDeclaredMethods();
		for (Method method : methods) {
			if (method.getParameterTypes().length == 0 && method.getReturnType() != void.class) {
				try {
					Object value = method.invoke(annotation);
					if (classValuesAsString) {
						if (value instanceof Class) {
							value = ((Class<?>) value).getName();
						}
						else if (value instanceof Class[]) {
							Class<?>[] clazzArray = (Class[]) value;
							String[] newValue = new String[clazzArray.length];
							for (int i = 0; i < clazzArray.length; i++) {
								newValue[i] = clazzArray[i].getName();
							}
							value = newValue;
						}
					}
					if (nestedAnnotationsAsMap && value instanceof Annotation) {
						attrs.put(method.getName(),
								getAnnotationAttributes((Annotation) value, classValuesAsString, true));
					}
					else if (nestedAnnotationsAsMap && value instanceof Annotation[]) {
						Annotation[] realAnnotations = (Annotation[]) value;
						AnnotationAttributes[] mappedAnnotations = new AnnotationAttributes[realAnnotations.length];
						for (int i = 0; i < realAnnotations.length; i++) {
							mappedAnnotations[i] = getAnnotationAttributes(realAnnotations[i], classValuesAsString, true);
						}
						attrs.put(method.getName(), mappedAnnotations);
					}
					else {
						attrs.put(method.getName(), value);
					}
				}
				catch (Exception ex) {
					throw new IllegalStateException("Could not obtain annotation attribute values", ex);
				}
			}
		}
		return attrs;
	}
{
			List<Class<?>> hierarchy = new ArrayList<Class<?>>(20);
			Set<Class<?>> visited = new HashSet<Class<?>>(20);
			addToClassHierarchy(0, ClassUtils.resolvePrimitiveIfNecessary(type), false, hierarchy, visited);
			boolean array = type.isArray();
			int i = 0;
			while (i < hierarchy.size()) {
				Class<?> candidate = hierarchy.get(i);
				candidate = (array ? candidate.getComponentType() : ClassUtils.resolvePrimitiveIfNecessary(candidate));
				Class<?> superclass = candidate.getSuperclass();
				if (candidate.getSuperclass() != null && superclass != Object.class) {
					addToClassHierarchy(i + 1, candidate.getSuperclass(), array, hierarchy, visited);
				}
				for (Class<?> implementedInterface : candidate.getInterfaces()) {
					addToClassHierarchy(hierarchy.size(), implementedInterface, array, hierarchy, visited);
				}
				i++;
			}
			addToClassHierarchy(hierarchy.size(), Object.class, array, hierarchy, visited);
			addToClassHierarchy(hierarchy.size(), Object.class, false, hierarchy, visited);
			return hierarchy;
		}
{

		DefaultTransportRequest request = new DefaultTransportRequest(info, headers, transport, type, getMessageCodec());
		request.setUser(getUser());
		if (this.taskScheduler != null) {
			request.setTimeoutValue(serverInfo.getRetransmissionTimeout());
			request.setTimeoutScheduler(this.taskScheduler);
		}
		requests.add(request);
	}
{
		return jsr107Present && jCacheImplPresent;
	}
{
		return jsr107Present && jCacheImplPresent;
	}
{
		return MessageBuilder
				.withPayload("Hello").setHeader("foo", "bar").build();
	}
{

		final TyrusHttpUpgradeHandler upgradeHandler;
		try {
			upgradeHandler = request.upgrade(TyrusHttpUpgradeHandler.class);
		}
		catch (ServletException ex) {
			throw new HandshakeFailureException("Unable to create TyrusHttpUpgradeHandler", ex);
		}

		Connection connection = createConnection(upgradeHandler, response);

		RequestContext requestContext = RequestContext.Builder.create().
				requestURI(URI.create(wsApp.getPath())).requestPath(wsApp.getPath()).
				userPrincipal(request.getUserPrincipal()).
				connection(connection).secure(request.isSecure()).build();

		for (String header : headers.keySet()) {
			requestContext.getHeaders().put(header, headers.get(header));
		}

		boolean upgraded = WebSocketEngine.getEngine().upgrade(connection, requestContext,
				new WebSocketEngine.WebSocketHolderListener() {
					@Override
					public void onWebSocketHolder(WebSocketEngine.WebSocketHolder webSocketHolder) {
						upgradeHandler.setWebSocketHolder(webSocketHolder);
					}
				});

		// GlassFish bug ?? (see same line in TyrusServletFilter.doFilter)
		response.flushBuffer();

		return upgraded;
	}
{
		if (params.length == 0) {
			return SimpleKey.EMPTY;
		}
		if (params.length == 1) {
			Object param = params[0];
			if (param != null && !param.getClass().isArray()) {
				return param;
			}
		}
		return new SimpleKey(params);
	}
{
		String factoryId = element.getAttribute(FACTORY_ID_ATTRIBUTE);
		if (StringUtils.hasText(factoryId)) {
			RootBeanDefinition beanDefinition = createContainerFactory(factoryId, element, propertyValues);
			if (beanDefinition != null) {
				beanDefinition.setSource(parserContext.extractSource(element));
				parserContext.registerBeanComponent(new BeanComponentDefinition(beanDefinition, factoryId));
			}
		}
	}
{
		container.stop();
		if (container instanceof DisposableBean) {
			((DisposableBean) container).destroy();
		}
	}
{
		this.registry.addViewController("/path").setViewName("viewName");
		Map<String, ?> urlMap = getHandlerMapping().getUrlMap();
		ParameterizableViewController controller = (ParameterizableViewController) urlMap.get("/path");
		assertNotNull(controller);
		assertEquals("viewName", controller.getViewName());
	}
{
		callback.onSuccess(this.value);
	}
{
		ListenableFuture<S> listenableAdaptee = (ListenableFuture<S>) getAdaptee();
		listenableAdaptee.addCallback(new ListenableFutureCallback<S>() {
			@Override
			public void onSuccess(S result) {
				try {
					callback.onSuccess(adaptInternal(result));
				}
				catch (ExecutionException ex) {
					Throwable cause = ex.getCause();
					onFailure(cause != null ? cause : ex);
				}
				catch (Throwable t) {
					onFailure(t);
				}
			}

			@Override
			public void onFailure(Throwable t) {
				callback.onFailure(t);
			}
		});
	}
{
		RootBeanDefinition containerDef = new RootBeanDefinition();
		containerDef.setSource(context.getSource());
		containerDef.setBeanClassName("org.springframework.jms.listener.endpoint.JmsMessageEndpointManager");

		containerDef.getPropertyValues().addPropertyValues(context.getContainerValues());


		BeanDefinition activationSpec = getActivationSpecConfigBeanDefinition(context.getContainerValues());
		parseListenerConfiguration(context.getListenerElement(), context.getParserContext(), activationSpec);

		String phase = context.getContainerElement().getAttribute(PHASE_ATTRIBUTE);
		if (StringUtils.hasText(phase)) {
			containerDef.getPropertyValues().add("phase", phase);
		}

		return containerDef;
	}
{
		unregisterNotificationListeners();
		unregisterBeans();
		this.running = false;
	}
{
		txContext.startTransaction();
		++this.transactionsStarted;
		if (logger.isInfoEnabled()) {
			logger.info(String.format(
				"Began transaction (%s) for test context %s; transaction manager [%s]; rollback [%s]",
				this.transactionsStarted, testContext, txContext.transactionManager, isRollback(testContext)));
		}
	}
{
			Assert.notNull(uri, "'uri' must not be null");
			Assert.notNull(httpMethod, "'httpMethod' must not be null");

			if (this.requestIterator == null) {
				this.requestIterator = MockRestServiceServer.this.expectedRequests.iterator();
			}
			if (!this.requestIterator.hasNext()) {
				throw new AssertionError("No further requests expected");
			}

			RequestMatcherClientHttpRequest request = this.requestIterator.next();
			request.setURI(uri);
			request.setMethod(httpMethod);

			MockRestServiceServer.this.actualRequests.add(request);

			return request;
		}
{

		if (!(message.getPayload() instanceof byte[])) {
			logger.error("Ignoring message, expected byte[] content: " + message);
			return;
		}

		MessageHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, MessageHeaderAccessor.class);
		if (accessor == null) {
			logger.error("No header accessor: " + message);
			return;
		}

		StompHeaderAccessor stompAccessor;
		if (accessor instanceof StompHeaderAccessor) {
			stompAccessor = (StompHeaderAccessor) accessor;
		}
		else if (accessor instanceof SimpMessageHeaderAccessor) {
			stompAccessor = StompHeaderAccessor.wrap(message);
			if (SimpMessageType.CONNECT_ACK.equals(stompAccessor.getMessageType())) {
				StompHeaderAccessor connectedHeaders = StompHeaderAccessor.create(StompCommand.CONNECTED);
				connectedHeaders.setVersion(getVersion(stompAccessor));
				connectedHeaders.setHeartbeat(0, 0); // no heart-beat support with simple broker
				stompAccessor = connectedHeaders;
			}
			else if (stompAccessor.getCommand() == null || StompCommand.SEND.equals(stompAccessor.getCommand())) {
				stompAccessor.updateStompCommandAsServerMessage();
			}
		}
		else {
			// Should not happen
			logger.error("Unexpected header accessor type: " + accessor);
			return;
		}

		StompCommand command = stompAccessor.getCommand();
		if (StompCommand.MESSAGE.equals(command)) {
			if (stompAccessor.getSubscriptionId() == null) {
				logger.error("Ignoring message, no subscriptionId header: " + message);
				return;
			}
			String origDestination = stompAccessor.getFirstNativeHeader(SimpMessageHeaderAccessor.ORIGINAL_DESTINATION);
			if (origDestination != null) {
				stompAccessor = toMutableAccessor(stompAccessor, message);
				stompAccessor.removeNativeHeader(SimpMessageHeaderAccessor.ORIGINAL_DESTINATION);
				stompAccessor.setDestination(origDestination);
			}
		}
		else if (StompCommand.CONNECTED.equals(command)) {
			stompAccessor = afterStompSessionConnected(message, stompAccessor, session);
			if (this.eventPublisher != null && StompCommand.CONNECTED.equals(command)) {
				publishEvent(new SessionConnectedEvent(this, (Message<byte[]>) message));
			}
		}

		try {
			byte[] bytes = this.stompEncoder.encode(stompAccessor.getMessageHeaders(), (byte[]) message.getPayload());
			TextMessage textMessage = new TextMessage(bytes);

			session.sendMessage(textMessage);
		}
		catch (SessionLimitExceededException ex) {
			// Bad session, just get out
			throw ex;
		}
		catch (Throwable ex) {
			logger.error("Failed to send WebSocket message to client, sessionId=" + session.getId(), ex);
			command = StompCommand.ERROR;
		}
		finally {
			if (StompCommand.ERROR.equals(command)) {
				try {
					session.close(CloseStatus.PROTOCOL_ERROR);
				}
				catch (IOException ex) {
					// Ignore
				}
			}
		}
	}
{
		Field field = this.fieldMap.get(propertyName);
		if (field == null) {
			throw new NotWritablePropertyException(
					this.target.getClass(), propertyName, "Field '" + propertyName + "' does not exist");
		}
		Object oldValue = null;
		try {
			ReflectionUtils.makeAccessible(field);
			oldValue = field.get(this.target);
			Object convertedValue = this.typeConverterDelegate.convertIfNecessary(
					field.getName(), oldValue, newValue, field.getType(), new TypeDescriptor(field));
			field.set(this.target, convertedValue);
		}
		catch (ConverterNotFoundException ex) {
			PropertyChangeEvent pce = new PropertyChangeEvent(this.target, propertyName, oldValue, newValue);
			throw new ConversionNotSupportedException(pce, field.getType(), ex);
		}
		catch (ConversionException ex) {
			PropertyChangeEvent pce = new PropertyChangeEvent(this.target, propertyName, oldValue, newValue);
			throw new TypeMismatchException(pce, field.getType(), ex);
		}
		catch (IllegalStateException ex) {
			PropertyChangeEvent pce = new PropertyChangeEvent(this.target, propertyName, oldValue, newValue);
			throw new ConversionNotSupportedException(pce, field.getType(), ex);
		}
		catch (IllegalArgumentException ex) {
			PropertyChangeEvent pce = new PropertyChangeEvent(this.target, propertyName, oldValue, newValue);
			throw new TypeMismatchException(pce, field.getType(), ex);
		}
		catch (IllegalAccessException ex) {
			throw new InvalidPropertyException(this.target.getClass(), propertyName, "Field is not accessible", ex);
		}
	}
{
		logger.info("Registering beans for JMX exposure");
		synchronized (this.lifecycleMonitor) {
			try {
				registerBeans();
				registerNotificationListeners();
			} catch (RuntimeException ex) {
				// Unregister beans already registered by this exporter.
				unregisterNotificationListeners();
				unregisterBeans();
				throw ex;
			}
		}
		running = true;
	}
{

		Assert.notNull(request, "Request must not be null");
		Assert.notNull(response, "Response must not be null");
		Assert.notNull(frameFormat, "SockJsFrameFormat must not be null");

		this.response = response;
		this.frameFormat = frameFormat;
		this.asyncRequestControl = request.getAsyncRequestControl(response);
	}
{

		Assert.notNull(request, "Request must not be null");
		Assert.notNull(response, "Response must not be null");
		Assert.notNull(frameFormat, "SockJsFrameFormat must not be null");

		this.response = response;
		this.frameFormat = frameFormat;
		this.asyncRequestControl = request.getAsyncRequestControl(response);
	}
{
		super.resetRequest();
		this.byteCount = 0;
	}
{

		Assert.notNull(webSocketHandler, "webSocketHandler must not be null");
		Assert.notNull(uri, "uri must not be null");

		String scheme = uri.getScheme();
		Assert.isTrue(((scheme != null) && ("ws".equals(scheme) || "wss".equals(scheme))), "Invalid scheme: " + scheme);

		if (logger.isDebugEnabled()) {
			logger.debug("Connecting to " + uri);
		}

		HttpHeaders headersToUse = new HttpHeaders();
		if (headers != null) {
			for (String header : headers.keySet()) {
				if (!specialHeaders.contains(header.toLowerCase())) {
					headersToUse.put(header, headers.get(header));
				}
			}
		}

		List<String> subProtocols = ((headers != null) && (headers.getSecWebSocketProtocol() != null)) ?
				headers.getSecWebSocketProtocol() : Collections.<String>emptyList();

		List<WebSocketExtension> extensions = ((headers != null) && (headers.getSecWebSocketExtensions() != null)) ?
				headers.getSecWebSocketExtensions() : Collections.<WebSocketExtension>emptyList();

		return doHandshakeInternal(webSocketHandler, headersToUse, uri, subProtocols, extensions,
				Collections.<String, Object>emptyMap());
	}
{
		Description description = describeChild(method);
		return new EachTestNotifier(notifier, description);
	}
{

		// The following has been deprecated as late as Jackson 2.2 (April 2013);
		// preserved for the time being, for Jackson 2.0/2.1 compatibility.
		@SuppressWarnings("deprecation")
		JsonGenerator generator = this.objectMapper.getJsonFactory().createJsonGenerator(stream, this.encoding);

		// A workaround for JsonGenerators not applying serialization features
		// https://github.com/FasterXML/jackson-databind/issues/12
		if (this.objectMapper.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
			generator.useDefaultPrettyPrinter();
		}

		if (jsonPrefix != null) {
			generator.writeRaw(jsonPrefix);
		}

		Class<?> serializationView = (Class<?>) model.get(JsonView.class.getName());
		if (serializationView != null) {
			this.objectMapper.writerWithView(serializationView).writeValue(generator, value);
		}
		else {
			this.objectMapper.writeValue(generator, value);
		}
	}
{

		// The following has been deprecated as late as Jackson 2.2 (April 2013);
		// preserved for the time being, for Jackson 2.0/2.1 compatibility.
		@SuppressWarnings("deprecation")
		JsonGenerator generator = this.objectMapper.getJsonFactory().createJsonGenerator(stream, this.encoding);

		// A workaround for JsonGenerators not applying serialization features
		// https://github.com/FasterXML/jackson-databind/issues/12
		if (this.objectMapper.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
			generator.useDefaultPrettyPrinter();
		}

		if (jsonPrefix != null) {
			generator.writeRaw(jsonPrefix);
		}

		Class<?> serializationView = (Class<?>) model.get(JsonView.class.getName());
		if (serializationView != null) {
			this.objectMapper.writerWithView(serializationView).writeValue(generator, value);
		}
		else {
			this.objectMapper.writeValue(generator, value);
		}
	}
{

		Class<?> type = descriptor.getDependencyType();
		Object value = getAutowireCandidateResolver().getSuggestedValue(descriptor);
		if (value != null) {
			if (value instanceof String) {
				String strVal = resolveEmbeddedValue((String) value);
				BeanDefinition bd = (beanName != null && containsBean(beanName) ? getMergedBeanDefinition(beanName) : null);
				value = evaluateBeanDefinitionString(strVal, bd);
			}
			TypeConverter converter = (typeConverter != null ? typeConverter : getTypeConverter());
			return (descriptor.getField() != null ?
					converter.convertIfNecessary(value, type, descriptor.getField()) :
					converter.convertIfNecessary(value, type, descriptor.getMethodParameter()));
		}

		if (type.isArray()) {
			Class<?> componentType = type.getComponentType();
			DependencyDescriptor targetDesc = new DependencyDescriptor(descriptor);
			targetDesc.increaseNestingLevel();
			Map<String, Object> matchingBeans = findAutowireCandidates(beanName, componentType, targetDesc);
			if (matchingBeans.isEmpty()) {
				if (descriptor.isRequired()) {
					raiseNoSuchBeanDefinitionException(componentType, "array of " + componentType.getName(), descriptor);
				}
				return null;
			}
			if (autowiredBeanNames != null) {
				autowiredBeanNames.addAll(matchingBeans.keySet());
			}
			TypeConverter converter = (typeConverter != null ? typeConverter : getTypeConverter());
			Object result = converter.convertIfNecessary(matchingBeans.values(), type);
			if (this.dependencyComparator != null && result instanceof Object[]) {
				Arrays.sort((Object[]) result, this.dependencyComparator);
			}
			return result;
		}
		else if (Collection.class.isAssignableFrom(type) && type.isInterface()) {
			Class<?> elementType = descriptor.getCollectionType();
			if (elementType == null) {
				if (descriptor.isRequired()) {
					throw new FatalBeanException("No element type declared for collection [" + type.getName() + "]");
				}
				return null;
			}
			DependencyDescriptor targetDesc = new DependencyDescriptor(descriptor);
			targetDesc.increaseNestingLevel();
			Map<String, Object> matchingBeans = findAutowireCandidates(beanName, elementType, targetDesc);
			if (matchingBeans.isEmpty()) {
				if (descriptor.isRequired()) {
					raiseNoSuchBeanDefinitionException(elementType, "collection of " + elementType.getName(), descriptor);
				}
				return null;
			}
			if (autowiredBeanNames != null) {
				autowiredBeanNames.addAll(matchingBeans.keySet());
			}
			TypeConverter converter = (typeConverter != null ? typeConverter : getTypeConverter());
			Object result = converter.convertIfNecessary(matchingBeans.values(), type);
			if (this.dependencyComparator != null && result instanceof List) {
				Collections.sort((List<?>) result, this.dependencyComparator);
			}
			return result;
		}
		else if (Map.class.isAssignableFrom(type) && type.isInterface()) {
			Class<?> keyType = descriptor.getMapKeyType();
			if (keyType == null || !String.class.isAssignableFrom(keyType)) {
				if (descriptor.isRequired()) {
					throw new FatalBeanException("Key type [" + keyType + "] of map [" + type.getName() +
							"] must be assignable to [java.lang.String]");
				}
				return null;
			}
			Class<?> valueType = descriptor.getMapValueType();
			if (valueType == null) {
				if (descriptor.isRequired()) {
					throw new FatalBeanException("No value type declared for map [" + type.getName() + "]");
				}
				return null;
			}
			DependencyDescriptor targetDesc = new DependencyDescriptor(descriptor);
			targetDesc.increaseNestingLevel();
			Map<String, Object> matchingBeans = findAutowireCandidates(beanName, valueType, targetDesc);
			if (matchingBeans.isEmpty()) {
				if (descriptor.isRequired()) {
					raiseNoSuchBeanDefinitionException(valueType, "map with value type " + valueType.getName(), descriptor);
				}
				return null;
			}
			if (autowiredBeanNames != null) {
				autowiredBeanNames.addAll(matchingBeans.keySet());
			}
			return matchingBeans;
		}
		else {
			Map<String, Object> matchingBeans = findAutowireCandidates(beanName, type, descriptor);
			if (matchingBeans.isEmpty()) {
				if (descriptor.isRequired()) {
					raiseNoSuchBeanDefinitionException(type, "", descriptor);
				}
				return null;
			}
			if (matchingBeans.size() > 1) {
				String primaryBeanName = determineAutowireCandidate(matchingBeans, descriptor);
				if (primaryBeanName == null) {
					throw new NoUniqueBeanDefinitionException(type, matchingBeans.keySet());
				}
				if (autowiredBeanNames != null) {
					autowiredBeanNames.add(primaryBeanName);
				}
				return matchingBeans.get(primaryBeanName);
			}
			// We have exactly one match.
			Map.Entry<String, Object> entry = matchingBeans.entrySet().iterator().next();
			if (autowiredBeanNames != null) {
				autowiredBeanNames.add(entry.getKey());
			}
			return entry.getValue();
		}
	}
{

		Class<?> type = descriptor.getDependencyType();
		Object value = getAutowireCandidateResolver().getSuggestedValue(descriptor);
		if (value != null) {
			if (value instanceof String) {
				String strVal = resolveEmbeddedValue((String) value);
				BeanDefinition bd = (beanName != null && containsBean(beanName) ? getMergedBeanDefinition(beanName) : null);
				value = evaluateBeanDefinitionString(strVal, bd);
			}
			TypeConverter converter = (typeConverter != null ? typeConverter : getTypeConverter());
			return (descriptor.getField() != null ?
					converter.convertIfNecessary(value, type, descriptor.getField()) :
					converter.convertIfNecessary(value, type, descriptor.getMethodParameter()));
		}

		if (type.isArray()) {
			Class<?> componentType = type.getComponentType();
			DependencyDescriptor targetDesc = new DependencyDescriptor(descriptor);
			targetDesc.increaseNestingLevel();
			Map<String, Object> matchingBeans = findAutowireCandidates(beanName, componentType, targetDesc);
			if (matchingBeans.isEmpty()) {
				if (descriptor.isRequired()) {
					raiseNoSuchBeanDefinitionException(componentType, "array of " + componentType.getName(), descriptor);
				}
				return null;
			}
			if (autowiredBeanNames != null) {
				autowiredBeanNames.addAll(matchingBeans.keySet());
			}
			TypeConverter converter = (typeConverter != null ? typeConverter : getTypeConverter());
			Object result = converter.convertIfNecessary(matchingBeans.values(), type);
			if (this.dependencyComparator != null && result instanceof Object[]) {
				Arrays.sort((Object[]) result, this.dependencyComparator);
			}
			return result;
		}
		else if (Collection.class.isAssignableFrom(type) && type.isInterface()) {
			Class<?> elementType = descriptor.getCollectionType();
			if (elementType == null) {
				if (descriptor.isRequired()) {
					throw new FatalBeanException("No element type declared for collection [" + type.getName() + "]");
				}
				return null;
			}
			DependencyDescriptor targetDesc = new DependencyDescriptor(descriptor);
			targetDesc.increaseNestingLevel();
			Map<String, Object> matchingBeans = findAutowireCandidates(beanName, elementType, targetDesc);
			if (matchingBeans.isEmpty()) {
				if (descriptor.isRequired()) {
					raiseNoSuchBeanDefinitionException(elementType, "collection of " + elementType.getName(), descriptor);
				}
				return null;
			}
			if (autowiredBeanNames != null) {
				autowiredBeanNames.addAll(matchingBeans.keySet());
			}
			TypeConverter converter = (typeConverter != null ? typeConverter : getTypeConverter());
			Object result = converter.convertIfNecessary(matchingBeans.values(), type);
			if (this.dependencyComparator != null && result instanceof List) {
				Collections.sort((List<?>) result, this.dependencyComparator);
			}
			return result;
		}
		else if (Map.class.isAssignableFrom(type) && type.isInterface()) {
			Class<?> keyType = descriptor.getMapKeyType();
			if (keyType == null || !String.class.isAssignableFrom(keyType)) {
				if (descriptor.isRequired()) {
					throw new FatalBeanException("Key type [" + keyType + "] of map [" + type.getName() +
							"] must be assignable to [java.lang.String]");
				}
				return null;
			}
			Class<?> valueType = descriptor.getMapValueType();
			if (valueType == null) {
				if (descriptor.isRequired()) {
					throw new FatalBeanException("No value type declared for map [" + type.getName() + "]");
				}
				return null;
			}
			DependencyDescriptor targetDesc = new DependencyDescriptor(descriptor);
			targetDesc.increaseNestingLevel();
			Map<String, Object> matchingBeans = findAutowireCandidates(beanName, valueType, targetDesc);
			if (matchingBeans.isEmpty()) {
				if (descriptor.isRequired()) {
					raiseNoSuchBeanDefinitionException(valueType, "map with value type " + valueType.getName(), descriptor);
				}
				return null;
			}
			if (autowiredBeanNames != null) {
				autowiredBeanNames.addAll(matchingBeans.keySet());
			}
			return matchingBeans;
		}
		else {
			Map<String, Object> matchingBeans = findAutowireCandidates(beanName, type, descriptor);
			if (matchingBeans.isEmpty()) {
				if (descriptor.isRequired()) {
					raiseNoSuchBeanDefinitionException(type, "", descriptor);
				}
				return null;
			}
			if (matchingBeans.size() > 1) {
				String primaryBeanName = determineAutowireCandidate(matchingBeans, descriptor);
				if (primaryBeanName == null) {
					throw new NoUniqueBeanDefinitionException(type, matchingBeans.keySet());
				}
				if (autowiredBeanNames != null) {
					autowiredBeanNames.add(primaryBeanName);
				}
				return matchingBeans.get(primaryBeanName);
			}
			// We have exactly one match.
			Map.Entry<String, Object> entry = matchingBeans.entrySet().iterator().next();
			if (autowiredBeanNames != null) {
				autowiredBeanNames.add(entry.getKey());
			}
			return entry.getValue();
		}
	}
{

		MessageHeaders messageHeaders = null;
		Map<String, Object> headersToUse = processHeadersToSend(headers);
		if (headersToUse != null) {
			if (headersToUse instanceof MessageHeaders) {
				messageHeaders = (MessageHeaders) headersToUse;
			}
			else {
				messageHeaders = new MessageHeaders(headersToUse);
			}
		}

		Message<?> message = getMessageConverter().toMessage(payload, messageHeaders);
		if (message == null) {
			String payloadType = (payload != null ? payload.getClass().getName() : null);
			Object contentType = (messageHeaders != null ? messageHeaders.get(MessageHeaders.CONTENT_TYPE) : null);
			throw new MessageConversionException("Unable to convert payload with type='" + payloadType +
					"', contentType='" + contentType + "', converter=[" + getMessageConverter() + "]");
		}
		if (postProcessor != null) {
			message = postProcessor.postProcessMessage(message);
		}
		send(destination, message);
	}
{
		// The following has been deprecated as late as Jackson 2.2 (April 2013);
		// preserved for the time being, for Jackson 2.0/2.1 compatibility.
		@SuppressWarnings("deprecation")
		JsonGenerator generator = this.objectMapper.getJsonFactory().createJsonGenerator(stream, this.encoding);

		// A workaround for JsonGenerators not applying serialization features
		// https://github.com/FasterXML/jackson-databind/issues/12
		if (this.objectMapper.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
			generator.useDefaultPrettyPrinter();
		}

		if (jsonPrefix != null) {
			generator.writeRaw(jsonPrefix);
		}
		this.objectMapper.writeValue(generator, value);
	}
{
		try {
			Message<?> message = toMessagingMessage(jmsMessage);
			if (logger.isDebugEnabled()) {
				logger.debug("Processing [" + message + "]");
			}
			Object result = handlerMethod.invoke(message, jmsMessage, session);
			if (result != null) {
				handleResult(result, jmsMessage, session);
			}
			else {
				logger.trace("No result object given - no result to handle");
			}
		}
		catch (MessagingException e) {
			throw new ListenerExecutionFailedException(createMessagingErrorMessage("Listener method could not " +
					"be invoked with the incoming message"), e);
		}
		catch (Exception e) {
			throw new ListenerExecutionFailedException("Listener method '"
					+ handlerMethod.getMethod().toGenericString() + "' threw exception", e);
		}
	}
{

		this.decoders.remove(session.getId());

		Principal principal = session.getPrincipal();
		if ((this.userSessionRegistry != null) && (principal != null)) {
			String userName = resolveNameForUserSessionRegistry(principal);
			this.userSessionRegistry.unregisterSessionId(userName, session.getId());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("WebSocket session ended, sending DISCONNECT message to broker");
		}

		StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);
		if (getHeaderInitializer() != null) {
			getHeaderInitializer().initHeaders(headerAccessor);
		}
		headerAccessor.setSessionId(session.getId());
		Message<?> message = MessageBuilder.createMessage(EMPTY_PAYLOAD, headerAccessor.getMessageHeaders());

		if (this.eventPublisher != null) {
			publishEvent(new SessionDisconnectEvent(this, session.getId(), closeStatus));
		}

		outputChannel.send(message);
	}
{
		DefaultMessageListenerContainer container = this.context.getBean(containerBeanName, DefaultMessageListenerContainer.class);
		BackOff backOff = (BackOff) new DirectFieldAccessor(container).getPropertyValue("backOff");
		assertEquals(FixedBackOff.class, backOff.getClass());
		return ((FixedBackOff)backOff).getInterval();
	}
{
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes == null) {
			logger.debug("No request bound to the current thread: is DispatcherSerlvet used?");
			return null;
		}

		HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
		if (request == null) {
			logger.debug("Request bound to current thread is not an HttpServletRequest");
			return null;
		}

		String attributeName = DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE;
		WebApplicationContext wac = (WebApplicationContext) request.getAttribute(attributeName);
		if (wac == null) {
			logger.debug("No WebApplicationContext found: not in a DispatcherServlet request?");
			return null;
		}

		try {
			return wac.getBean(MVC_URI_COMPONENTS_CONTRIBUTOR_BEAN_NAME, CompositeUriComponentsContributor.class);
		}
		catch (NoSuchBeanDefinitionException ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("No CompositeUriComponentsContributor bean with name '" +
						MVC_URI_COMPONENTS_CONTRIBUTOR_BEAN_NAME + "'");
			}
			return null;
		}
	}
{
		AnnotationAttributes attributes = (AnnotationAttributes) metadata.getAnnotationAttributes(
			TestComponentScan.class.getName(), false);
		String[] basePackages = attributes.getStringArray("basePackages");
		assertThat("length of basePackages[]", basePackages.length, is(1));
		assertThat("basePackages[0]", basePackages[0], is("org.example.componentscan"));
		String[] value = attributes.getStringArray("value");
		assertThat("length of value[]", value.length, is(0));
		Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");
		assertThat("length of basePackageClasses[]", basePackageClasses.length, is(0));
	}
{
		Class<?> targetClass = AopUtils.getTargetClass(bean);
		Boolean eligible = this.eligibleBeans.get(targetClass);
		if (eligible != null) {
			return eligible;
		}
		eligible = AopUtils.canApply(this.advisor, targetClass);
		this.eligibleBeans.put(targetClass, eligible);
		return eligible;
	}
{
		UserDestinationResult result = this.userDestinationResolver.resolveDestination(message);
		if (result == null) {
			return;
		}
		Set<String> destinations = result.getTargetDestinations();
		if (destinations.isEmpty()) {
			return;
		}
		if (SimpMessageType.MESSAGE.equals(SimpMessageHeaderAccessor.getMessageType(message.getHeaders()))) {
			SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(message);
			if (getHeaderInitializer() != null) {
				getHeaderInitializer().initHeaders(headerAccessor);
			}
			headerAccessor.setHeader(SimpMessageHeaderAccessor.ORIGINAL_DESTINATION, result.getSubscribeDestination());
			message = MessageBuilder.createMessage(message.getPayload(), headerAccessor.getMessageHeaders());
		}
		for (String destination : destinations) {
			if (logger.isDebugEnabled()) {
				logger.debug("Sending message to resolved destination=" + destination);
			}
			this.brokerMessagingTemplate.send(destination, message);
		}
	}
{
		MethodJmsListenerEndpoint endpoint = new MethodJmsListenerEndpoint();
		endpoint.setBean(sample);
		endpoint.setMethod(method);
		endpoint.setJmsHandlerMethodFactory(factory);
		return endpoint.createMessageListener(new SimpleMessageListenerContainer());
	}
{
		if (body.length > 0) {
			response.setContentLength(body.length);
			StreamUtils.copy(body, response.getOutputStream());
		}
	}
{
		if (Object.class.getName().equals(superClassName)) {
			return Boolean.FALSE;
		}
		else if (superClassName.startsWith("java.")) {
			try {
				Class<?> clazz = getClass().getClassLoader().loadClass(superClassName);
				return (clazz.getAnnotation(this.annotationType) != null);
			}
			catch (ClassNotFoundException ex) {
				// Class not found - can't determine a match that way.
			}
		}
		return null;
	}
{
		AnnotationAttributes attributes = (AnnotationAttributes) metadata.getAnnotationAttributes(
			TestComponentScan.class.getName(), false);
		String[] basePackages = attributes.getStringArray("basePackages");
		assertThat("length of basePackages[]", basePackages.length, is(1));
		assertThat("basePackages[0]", basePackages[0], is("org.example.componentscan"));
		String[] value = attributes.getStringArray("value");
		assertThat("length of value[]", value.length, is(0));
		Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");
		assertThat("length of basePackageClasses[]", basePackageClasses.length, is(0));
	}
{
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		if (path == null) {
			throw new IllegalStateException("Required request attribute '" +
					HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE + "' is not set");
		}

		if (!StringUtils.hasText(path) || isInvalidPath(path)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Ignoring invalid resource path [" + path + "]");
			}
			return null;
		}

		for (Resource location : this.locations) {
			try {
				if (logger.isDebugEnabled()) {
					logger.debug("Trying relative path [" + path + "] against base location: " + location);
				}
				Resource resource = location.createRelative(path);
				if (resource.exists() && resource.isReadable()) {
					if (logger.isDebugEnabled()) {
						logger.debug("Found matching resource: " + resource);
					}
					return resource;
				}
				else if (logger.isTraceEnabled()) {
					logger.trace("Relative resource doesn't exist or isn't readable: " + resource);
				}
			}
			catch (IOException ex) {
				logger.debug("Failed to create relative resource - trying next resource location", ex);
			}
		}
		return null;
	}
{

		this.clientInboundChannel.subscribe(this);
		this.brokerChannel.subscribe(this);

		if (this.tcpClient == null) {
			this.tcpClient = new StompTcpClientFactory().create(this.relayHost, this.relayPort);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Initializing \"system\" connection");
		}

		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.CONNECT);
		headers.setAcceptVersion("1.1,1.2");
		headers.setLogin(this.systemLogin);
		headers.setPasscode(this.systemPasscode);
		headers.setHeartbeat(this.systemHeartbeatSendInterval, this.systemHeartbeatReceiveInterval);
		headers.setHost(getVirtualHost());

		SystemStompConnectionHandler handler = new SystemStompConnectionHandler(headers);
		this.connectionHandlers.put(handler.getSessionId(), handler);

		this.tcpClient.connect(handler, new FixedIntervalReconnectStrategy(5000));
	}
{
		Assert.notNull(destination, "Destination must not be null");

		SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(message);
		headerAccessor.setDestination(destination);
		headerAccessor.setMessageTypeIfNotSet(SimpMessageType.MESSAGE);
		message = MessageBuilder.createMessage(message.getPayload(), headerAccessor.getMessageHeaders());

		long timeout = this.sendTimeout;
		boolean sent = (timeout >= 0)
				? this.messageChannel.send(message, timeout)
				: this.messageChannel.send(message);

		if (!sent) {
			throw new MessageDeliveryException(message,
					"failed to send message to destination '" + destination + "' within timeout: " + timeout);
		}
	}
{

		if (returnValue == null) {
			return;
		}

		MessageHeaders headers = message.getHeaders();
		String sessionId = SimpMessageHeaderAccessor.getSessionId(headers);
		MessagePostProcessor postProcessor = new SessionHeaderPostProcessor(sessionId);

		SendToUser sendToUser = returnType.getMethodAnnotation(SendToUser.class);
		if (sendToUser != null) {
			Principal principal = SimpMessageHeaderAccessor.getUser(headers);
			if (principal == null) {
				throw new MissingSessionUserException(message);
			}
			String userName = principal.getName();
			if (principal instanceof DestinationUserNameProvider) {
				userName = ((DestinationUserNameProvider) principal).getDestinationUserName();
			}
			String[] destinations = getTargetDestinations(sendToUser, message, this.defaultUserDestinationPrefix);
			for (String destination : destinations) {
				this.messagingTemplate.convertAndSendToUser(userName, destination, returnValue, postProcessor);
			}
			return;
		}
		else {
			SendTo sendTo = returnType.getMethodAnnotation(SendTo.class);
			String[] destinations = getTargetDestinations(sendTo, message, this.defaultDestinationPrefix);
			for (String destination : destinations) {
				this.messagingTemplate.convertAndSend(destination, returnValue, postProcessor);
			}
		}
	}
{
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(128 + message.getPayload().length);
			DataOutputStream output = new DataOutputStream(baos);

			StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);
			if (SimpMessageType.HEARTBEAT == headers.getMessageType()) {
				logger.trace("Encoded heartbeat");
				output.write(message.getPayload());
			}
			else {
				output.write(headers.getCommand().toString().getBytes(UTF8_CHARSET));
				output.write(LF);
				writeHeaders(headers, message, output);
				output.write(LF);
				writeBody(message, output);
				output.write((byte) 0);
			}

			return baos.toByteArray();
		}
		catch (IOException e) {
			throw new StompConversionException("Failed to encode STOMP frame",  e);
		}
	}
{
		if (message != null) {
			@SuppressWarnings("unchecked")
			Map<String, List<String>> headers = (Map<String, List<String>>) message.getHeaders().get(NATIVE_HEADERS);
			if (headers != null) {
				return headers;
			}
		}
		return null;
	}
{
		for (final ApplicationListener listener : getApplicationListeners(event)) {
			Executor executor = getTaskExecutor();
			if (executor != null) {
				executor.execute(new Runnable() {
					@Override
					public void run() {
						listener.onApplicationEvent(event);
					}
				});
			}
			else {
				listener.onApplicationEvent(event);
			}
		}
	}
{
		if (domResult.getNode() == null) {
			try {
				synchronized (this.documentBuilderFactoryMonitor) {
					if (this.documentBuilderFactory == null) {
						this.documentBuilderFactory = createDocumentBuilderFactory();
					}
				}
				DocumentBuilder documentBuilder = createDocumentBuilder(this.documentBuilderFactory);
				domResult.setNode(documentBuilder.newDocument());
			}
			catch (ParserConfigurationException ex) {
				throw new UnmarshallingFailureException(
						"Could not create document placeholder for DOMResult: " + ex.getMessage(), ex);
			}
		}
		marshalDomNode(graph, domResult.getNode());
	}
{
		if (!CollectionUtils.isEmpty(cachingConfigurers)) {
			int nConfigurers = cachingConfigurers.size();
			if (nConfigurers > 1) {
				throw new IllegalStateException(nConfigurers + " implementations of " +
						"CachingConfigurer were found when only 1 was expected. " +
						"Refactor the configuration such that CachingConfigurer is " +
						"implemented only once or not at all.");
			}
			CachingConfigurer cachingConfigurer = cachingConfigurers.iterator().next();
			this.cacheManager = cachingConfigurer.cacheManager();
			this.keyGenerator = cachingConfigurer.keyGenerator();
		}
		else if (!CollectionUtils.isEmpty(cacheManagerBeans)) {
			int nManagers = cacheManagerBeans.size();
			if (nManagers > 1) {
				throw new IllegalStateException(nManagers + " beans of type CacheManager " +
						"were found when only 1 was expected. Remove all but one of the " +
						"CacheManager bean definitions, or implement CachingConfigurer " +
						"to make explicit which CacheManager should be used for " +
						"annotation-driven cache management.");
			}
			CacheManager cacheManager = cacheManagerBeans.iterator().next();
			this.cacheManager = cacheManager;
			// keyGenerator remains null; will fall back to default within CacheInterceptor
		}
		else {
			throw new IllegalStateException("No bean of type CacheManager could be found. " +
					"Register a CacheManager bean or remove the @EnableCaching annotation " +
					"from your configuration.");
		}
	}
{
		Element element = this.cache.get(key);
		return (element != null ? new SimpleValueWrapper(element.getObjectValue()) : null);
	}
{
		Object value = this.cache.getIfPresent(key);
		return (value != null ? new SimpleValueWrapper(fromStoreValue(value)) : null);
	}
{
		Object value = this.store.get(key);
		return (value != null ? new SimpleValueWrapper(fromStoreValue(value)) : null);
	}
{
		StaticApplicationContext context = new StaticApplicationContext();
		BeanDefinition processorDefinition = new RootBeanDefinition(AsyncAnnotationBeanPostProcessor.class);
		BeanDefinition targetDefinition = new RootBeanDefinition(AsyncAnnotationBeanPostProcessorTests.TestBean.class);
		context.registerBeanDefinition("postProcessor", processorDefinition);
		context.registerBeanDefinition("target", targetDefinition);
		context.refresh();
		Object target = context.getBean("target");
		assertTrue(AopUtils.isAopProxy(target));
		context.close();
	}
{

		RootBeanDefinition executorDef = null;
		Element executor = null;

		if (channelElement != null) {
			executor = DomUtils.getChildElementByTagName(channelElement, "executor");
			if (executor != null) {
				executorDef = new RootBeanDefinition(ThreadPoolTaskExecutor.class);
				String attrValue = executor.getAttribute("core-pool-size");
				if (!StringUtils.isEmpty(attrValue)) {
					executorDef.getPropertyValues().add("corePoolSize", attrValue);
				}
				attrValue = executor.getAttribute("max-pool-size");
				if (!StringUtils.isEmpty(attrValue)) {
					executorDef.getPropertyValues().add("maxPoolSize", attrValue);
				}
				attrValue = executor.getAttribute("keep-alive-seconds");
				if (!StringUtils.isEmpty(attrValue)) {
					executorDef.getPropertyValues().add("keepAliveSeconds", attrValue);
				}
				attrValue = executor.getAttribute("queue-capacity");
				if (!StringUtils.isEmpty(attrValue)) {
					executorDef.getPropertyValues().add("queueCapacity", attrValue);
				}
			}
		}
		if ((channelElement == null && !channelName.equals("brokerChannel")) || (channelElement != null && executor == null)) {
			executorDef = new RootBeanDefinition(ThreadPoolTaskExecutor.class);
			executorDef.getPropertyValues().add("corePoolSize", Runtime.getRuntime().availableProcessors() * 2);
			executorDef.getPropertyValues().add("maxPoolSize", Integer.MAX_VALUE);
			executorDef.getPropertyValues().add("queueCapacity", Integer.MAX_VALUE);
		}

		ConstructorArgumentValues values = new ConstructorArgumentValues();
		if (executorDef != null) {
			executorDef.getPropertyValues().add("threadNamePrefix", channelName + "-");
			String executorName = channelName + "Executor";
			registerBeanDefByName(executorName, executorDef, parserCxt, source);
			values.addIndexedArgumentValue(0, new RuntimeBeanReference(executorName));
		}

		RootBeanDefinition channelDef = new RootBeanDefinition(ExecutorSubscribableChannel.class, values, null);

		if (channelElement != null) {
			Element interceptorsElement = DomUtils.getChildElementByTagName(channelElement, "interceptors");
			ManagedList<?> interceptorList = WebSocketNamespaceUtils.parseBeanSubElements(interceptorsElement, parserCxt);
			channelDef.getPropertyValues().add("interceptors", interceptorList);
		}

		registerBeanDefByName(channelName, channelDef, parserCxt, source);
		return new RuntimeBeanReference(channelName);
	}
{
		return this.cacheMap.get(name);
	}
{
		Assert.notNull(requiredType, "Required type must not be null");
		String[] beanNames = getBeanNamesForType(requiredType);
		if (beanNames.length > 1) {
			ArrayList<String> autowireCandidates = new ArrayList<String>();
			for (String beanName : beanNames) {
				if (getBeanDefinition(beanName).isAutowireCandidate()) {
					autowireCandidates.add(beanName);
				}
			}
			if (autowireCandidates.size() > 0) {
				beanNames = autowireCandidates.toArray(new String[autowireCandidates.size()]);
			}
		}
		if (beanNames.length == 1) {
			return getBean(beanNames[0], requiredType);
		}
		else if (beanNames.length > 1) {
			T primaryBean = null;
			for (String beanName : beanNames) {
				T beanInstance = getBean(beanName, requiredType);
				if (isPrimary(beanName, beanInstance)) {
					if (primaryBean != null) {
						throw new NoUniqueBeanDefinitionException(requiredType, beanNames.length,
								"more than one 'primary' bean found of required type: " + Arrays.asList(beanNames));
					}
					primaryBean = beanInstance;
				}
			}
			if (primaryBean != null) {
				return primaryBean;
			}
			throw new NoUniqueBeanDefinitionException(requiredType, beanNames);
		}
		else if (getParentBeanFactory() != null) {
			return getParentBeanFactory().getBean(requiredType);
		}
		else {
			throw new NoSuchBeanDefinitionException(requiredType);
		}
	}
{
		output.write(headers.getCommand().toString().getBytes(UTF8_CHARSET));
		output.write(LF);
	}
{
		try {
			Object jobObject = createJobInstance(bundle);
			return adaptJob(jobObject);
		}
		catch (Exception ex) {
			throw new SchedulerException("Job instantiation failed", ex);
		}
	}
{

		List<Message<byte[]>> messages = null;
		try {
			Assert.isInstanceOf(TextMessage.class,  webSocketMessage);
			TextMessage textMessage = (TextMessage) webSocketMessage;
			ByteBuffer byteBuffer = ByteBuffer.wrap(textMessage.asBytes());

			BufferingStompDecoder decoder = this.decoders.get(session.getId());
			if (decoder == null) {
				throw new IllegalStateException("No decoder for session id '" + session.getId() + "'");
			}

			messages = decoder.decode(byteBuffer);
			if (messages.isEmpty()) {
				logger.debug("Incomplete STOMP frame content received," + "buffered=" +
						decoder.getBufferSize() + ", buffer size limit=" + decoder.getBufferSizeLimit());
				return;
			}
		}
		catch (Throwable ex) {
			logger.error("Failed to parse WebSocket message to STOMP frame(s)", ex);
			sendErrorMessage(session, ex);
			return;
		}

		for (Message<byte[]> message : messages) {
			try {
				StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);
				if (logger.isTraceEnabled()) {
					if (SimpMessageType.HEARTBEAT.equals(headers.getMessageType())) {
						logger.trace("Received heartbeat from client session=" + session.getId());
					}
					else {
						logger.trace("Received message from client session=" + session.getId());
					}
				}

				headers.setSessionId(session.getId());
				headers.setSessionAttributes(session.getAttributes());
				headers.setUser(session.getPrincipal());

				message = MessageBuilder.withPayload(message.getPayload()).setHeaders(headers).build();

				if (SimpMessageType.CONNECT.equals(headers.getMessageType()) && this.eventPublisher != null) {
					this.eventPublisher.publishEvent(new SessionConnectEvent(this, message));
				}

				outputChannel.send(message);
			}
			catch (Throwable ex) {
				logger.error("Terminating STOMP session due to failure to send message", ex);
				sendErrorMessage(session, ex);
			}
		}
	}
{
		Integer contentLength = null;
		if (headers.containsKey("content-length")) {
			String rawContentLength = headers.getFirst("content-length");
			try {
				contentLength = Integer.valueOf(rawContentLength);
			}
			catch (NumberFormatException ex) {
				logger.warn("Ignoring invalid content-length header value: '" + rawContentLength + "'");
			}
		}
		if (contentLength != null && contentLength >= 0) {
			if (buffer.remaining() > contentLength) {
				byte[] payload = new byte[contentLength];
				buffer.get(payload);
				if (buffer.get() != 0) {
					throw new StompConversionException("Frame must be terminated with a null octet");
				}
				return payload;
			}
			else {
				return null;
			}
		}
		else {
			ByteArrayOutputStream payload = new ByteArrayOutputStream(256);
			while (buffer.remaining() > 0) {
				byte b = buffer.get();
				if (b == 0) {
					return payload.toByteArray();
				}
				else {
					payload.write(b);
				}
			}
		}
		return null;
	}
{
		this.sessions.remove(session.getId());
		findProtocolHandler(session).afterSessionEnded(session, closeStatus, this.clientInboundChannel);
	}
{
		Class<?> sourceClass = message.getPayload().getClass();
		Class<?> targetClass = parameter.getParameterType();
		if (ClassUtils.isAssignable(targetClass,sourceClass)) {
			return message.getPayload();
		}
		return this.converter.fromMessage(message, targetClass);
	}
{

		Payload annot = param.getParameterAnnotation(Payload.class);
		if ((annot != null) && StringUtils.hasText(annot.value())) {
			throw new IllegalStateException("@Payload SpEL expressions not supported by this resolver.");
		}

		Object target = getTargetPayload(param, message);
		if (isEmptyPayload(target)) {
			if (annot == null || annot.required()) {
				String paramName = param.getParameterName();
				paramName = (paramName == null ? "Arg" + param.getParameterIndex() : paramName);
				BindingResult bindingResult = new BeanPropertyBindingResult(target, paramName);
				bindingResult.addError(new ObjectError(paramName, "@Payload param is required"));
				throw new MethodArgumentNotValidException(message, param, bindingResult);
			}
			else {
				return null;
			}
		}

		if (annot != null) { // Only validate @Payload
			validate(message, param, target);
		}
		return target;
	}
{

		Class<?> sourceClass = message.getPayload().getClass();
		Class<?> targetClass = parameter.getParameterType();

		if (ClassUtils.isAssignable(targetClass,sourceClass)) {
			return message.getPayload();
		}

		Payload annot = parameter.getParameterAnnotation(Payload.class);

		if (isEmptyPayload(message)) {
			if ((annot != null) && !annot.required()) {
				return null;
			}
		}

		if ((annot != null) && StringUtils.hasText(annot.value())) {
			throw new IllegalStateException("@Payload SpEL expressions not supported by this resolver.");
		}

		Object target = this.converter.fromMessage(message, targetClass);
		validate(message, parameter, target);

		return target;
	}
{
        int charLength = s.length();
        if (charLength > 65535) {
            throw new IllegalArgumentException();
        }
        int len = length;
        if (len + 2 + charLength > data.length) {
            enlarge(2 + charLength);
        }
        byte[] data = this.data;
        // optimistic algorithm: instead of computing the byte length and then
        // serializing the string (which requires two loops), we assume the byte
        // length is equal to char length (which is the most frequent case), and
        // we start serializing the string right away. During the serialization,
        // if we find that this assumption is wrong, we continue with the
        // general method.
        data[len++] = (byte) (charLength >>> 8);
        data[len++] = (byte) charLength;
        for (int i = 0; i < charLength; ++i) {
            char c = s.charAt(i);
            if (c >= '\001' && c <= '\177') {
                data[len++] = (byte) c;
            } else {
                int byteLength = i;
                for (int j = i; j < charLength; ++j) {
                    c = s.charAt(j);
                    if (c >= '\001' && c <= '\177') {
                        byteLength++;
                    } else if (c > '\u07FF') {
                        byteLength += 3;
                    } else {
                        byteLength += 2;
                    }
                }
                if (byteLength > 65535) {
                    throw new IllegalArgumentException();
                }
                data[length] = (byte) (byteLength >>> 8);
                data[length + 1] = (byte) byteLength;
                if (length + 2 + byteLength > data.length) {
                    length = len;
                    enlarge(2 + byteLength);
                    data = this.data;
                }
                for (int j = i; j < charLength; ++j) {
                    c = s.charAt(j);
                    if (c >= '\001' && c <= '\177') {
                        data[len++] = (byte) c;
                    } else if (c > '\u07FF') {
                        data[len++] = (byte) (0xE0 | c >> 12 & 0xF);
                        data[len++] = (byte) (0x80 | c >> 6 & 0x3F);
                        data[len++] = (byte) (0x80 | c & 0x3F);
                    } else {
                        data[len++] = (byte) (0xC0 | c >> 6 & 0x1F);
                        data[len++] = (byte) (0x80 | c & 0x3F);
                    }
                }
                break;
            }
        }
        length = len;
        return this;
    }
{
		if (this.databasePopulator != null && this.enabled) {
			DatabasePopulatorUtils.execute(this.databasePopulator, this.dataSource);
		}
	}
{
		splitSqlScript(null, script, String.valueOf(delimiter), DEFAULT_COMMENT_PREFIX,
			DEFAULT_BLOCK_COMMENT_START_DELIMITER, DEFAULT_BLOCK_COMMENT_END_DELIMITER, statements);
	}
{
		LineNumberReader lnr = new LineNumberReader(resource.getReader());
		try {
			return readScript(lnr, commentPrefix, separator);
		}
		finally {
			lnr.close();
		}
	}
{

		if (logger.isInfoEnabled()) {
			logger.info("Executing SQL script from " + resource);
		}
		long startTime = System.currentTimeMillis();
		List<String> statements = new LinkedList<String>();
		String script;
		try {
			script = readScript(resource);
		}
		catch (IOException ex) {
			throw new CannotReadScriptException(resource, ex);
		}
		String delimiter = this.separator;
		if (delimiter == null) {
			delimiter = DEFAULT_STATEMENT_SEPARATOR;
			if (!containsSqlScriptDelimiters(script, delimiter)) {
				delimiter = "\n";
			}
		}
		splitSqlScript(script, delimiter, this.commentPrefix, statements);
		int lineNumber = 0;
		Statement stmt = connection.createStatement();
		try {
			for (String statement : statements) {
				lineNumber++;
				try {
					stmt.execute(statement);
					int rowsAffected = stmt.getUpdateCount();
					if (logger.isDebugEnabled()) {
						logger.debug(rowsAffected + " returned as updateCount for SQL: " + statement);
					}
				}
				catch (SQLException ex) {
					boolean dropStatement = StringUtils.startsWithIgnoreCase(statement.trim(), "drop");
					if (continueOnError || (dropStatement && ignoreFailedDrops)) {
						if (logger.isDebugEnabled()) {
							logger.debug("Failed to execute SQL script statement at line " + lineNumber +
									" of resource " + resource + ": " + statement, ex);
						}
					}
					else {
						throw new ScriptStatementFailedException(statement, lineNumber, resource, ex);
					}
				}
			}
		}
		finally {
			try {
				stmt.close();
			}
			catch (Throwable ex) {
				logger.debug("Could not close JDBC Statement", ex);
			}
		}
		long elapsedTime = System.currentTimeMillis() - startTime;
		if (logger.isInfoEnabled()) {
			logger.info("Done executing SQL script from " + resource + " in " + elapsedTime + " ms.");
		}
	}
{

		this.service.setWebSocketEnabled(false);
		handleRequest("GET", "/echo/server/session/websocket", HttpStatus.NOT_FOUND);

		this.service.setWebSocketEnabled(true);
		handleRequest("GET", "/echo/server/session/websocket", HttpStatus.OK);

		handleRequest("GET", "/echo//", HttpStatus.NOT_FOUND);
		handleRequest("GET", "/echo///", HttpStatus.NOT_FOUND);
		handleRequest("GET", "/echo/other", HttpStatus.NOT_FOUND);
		handleRequest("GET", "/echo//service/websocket", HttpStatus.NOT_FOUND);
		handleRequest("GET", "/echo/server//websocket", HttpStatus.NOT_FOUND);
		handleRequest("GET", "/echo/server/session/", HttpStatus.NOT_FOUND);
		handleRequest("GET", "/echo/s.erver/session/websocket", HttpStatus.NOT_FOUND);
		handleRequest("GET", "/echo/server/s.ession/websocket", HttpStatus.NOT_FOUND);
	}
{
		if (this.patterns.isEmpty()) {
			return this;
		}

		String lookupPath = this.pathHelper.getLookupPathForRequest(request);
		List<String> matches = new ArrayList<String>();
		for (String pattern : this.patterns) {
			String match = getMatchingPattern(pattern, lookupPath);
			if (match != null) {
				matches.add(match);
			}
		}
		Collections.sort(matches, this.pathMatcher.getPatternComparator(lookupPath));
		return matches.isEmpty() ? null :
			new PatternsRequestCondition(matches, this.pathHelper, this.pathMatcher, this.useSuffixPatternMatch,
					this.useTrailingSlashMatch, this.fileExtensions);
	}
{
		MimeType mimeType = getMimeType(headers);
		if (mimeType == null) {
			return true;
		}
		if (getSupportedMimeTypes().isEmpty()) {
			return true;
		}
		for (MimeType supported : getSupportedMimeTypes()) {
			if (supported.getType().equals(mimeType.getType()) &&
					supported.getSubtype().equals(mimeType.getSubtype())) {
				return true;
			}
		}
		return false;
	}
{

		Assert.notNull(annotationType, "Annotation type must not be null");

		if (clazz == null || clazz.equals(Object.class)) {
			return null;
		}

		// Declared locally?
		if (isAnnotationDeclaredLocally(annotationType, clazz)) {
			return new AnnotationDescriptor<T>(clazz, clazz.getAnnotation(annotationType));
		}

		// Declared on a composed annotation (i.e., as a meta-annotation)?
		if (!Annotation.class.isAssignableFrom(clazz)) {
			for (Annotation composedAnnotation : clazz.getAnnotations()) {
				T annotation = composedAnnotation.annotationType().getAnnotation(annotationType);
				if (annotation != null) {
					return new AnnotationDescriptor<T>(clazz, composedAnnotation, annotation);
				}
			}
		}

		// Declared on a superclass?
		return findAnnotationDescriptor(clazz.getSuperclass(), annotationType);
	}
{

		assertNonEmptyAnnotationTypeArray(annotationTypes, "The list of annotation types must not be empty");

		if (clazz == null || clazz.equals(Object.class)) {
			return null;
		}

		// Declared locally?
		for (Class<? extends Annotation> annotationType : annotationTypes) {
			if (isAnnotationDeclaredLocally(annotationType, clazz)) {
				return new UntypedAnnotationDescriptor(clazz, clazz.getAnnotation(annotationType));
			}
		}

		// Declared on a composed annotation (i.e., as a meta-annotation)?
		if (!Annotation.class.isAssignableFrom(clazz)) {
			for (Annotation composedAnnotation : clazz.getAnnotations()) {
				for (Class<? extends Annotation> annotationType : annotationTypes) {
					Annotation annotation = composedAnnotation.annotationType().getAnnotation(annotationType);
					if (annotation != null) {
						return new UntypedAnnotationDescriptor(clazz, composedAnnotation, annotation);
					}
				}
			}
		}

		// Declared on a superclass?
		return findAnnotationDescriptorForTypes(clazz.getSuperclass(), annotationTypes);
	}
{
		Class<HasMetaComponentAnnotation> startClass = HasMetaComponentAnnotation.class;
		assertAtComponentOnComposedAnnotationForMultipleCandidateTypes(startClass, startClass, "meta1", Meta1.class);
	}
{
		Class<Component> annotationType = Component.class;
		UntypedAnnotationDescriptor descriptor = findAnnotationDescriptorForTypes(startClass, Service.class,
			annotationType, Order.class, Transactional.class);
		assertNotNull(descriptor);
		assertEquals(declaringClass, descriptor.getRootDeclaringClass());
		assertEquals(annotationType, descriptor.getAnnotationType());
		assertEquals(name, ((Component) descriptor.getAnnotation()).value());
		assertNotNull(descriptor.getComposedAnnotation());
		assertEquals(composedAnnotationType, descriptor.getComposedAnnotationType());
	}
{
		Assert.notNull(clazz, "Class must not be null");
		A annotation = clazz.getAnnotation(annotationType);
		if (annotation != null) {
			return annotation;
		}
		for (Class<?> ifc : clazz.getInterfaces()) {
			annotation = findAnnotation(ifc, annotationType);
			if (annotation != null) {
				return annotation;
			}
		}
		if (!Annotation.class.isAssignableFrom(clazz)) {
			for (Annotation ann : clazz.getAnnotations()) {
				annotation = findAnnotation(ann.annotationType(), annotationType);
				if (annotation != null) {
					return annotation;
				}
			}
		}
		Class<?> superClass = clazz.getSuperclass();
		if (superClass == null || superClass.equals(Object.class)) {
			return null;
		}
		return findAnnotation(superClass, annotationType);
	}
{
		final Class<?> userType = ClassUtils.getUserClass(handlerType);
		Set<Method> methods = HandlerMethodSelector.selectMethods(userType, new MethodFilter() {
			@Override
			public boolean matches(Method method) {
				return getMappingForMethod(method, userType) != null;
			}
		});

		for (Method method : methods) {
			T mapping = getMappingForMethod(method, userType);
			registerHandlerMethod(handler, method, mapping);
		}
	}
{

		String destination = headers.getDestination();

		String targetUser;
		String targetDestination;
		Set<String> targetSessionIds;

		Principal principal = headers.getUser();
		SimpMessageType messageType = headers.getMessageType();

		if (SimpMessageType.SUBSCRIBE.equals(messageType) || SimpMessageType.UNSUBSCRIBE.equals(messageType)) {
			if (!checkDestination(destination, this.destinationPrefix)) {
				return null;
			}
			if (principal == null) {
				logger.error("Ignoring message, no principal info available");
				return null;
			}
			if (headers.getSessionId() == null) {
				logger.error("Ignoring message, no session id available");
				return null;
			}
			targetUser = principal.getName();
			targetDestination = destination.substring(this.destinationPrefix.length()-1);
			targetSessionIds = Collections.singleton(headers.getSessionId());
		}
		else if (SimpMessageType.MESSAGE.equals(messageType)) {
			if (!checkDestination(destination, this.destinationPrefix)) {
				return null;
			}
			int startIndex = this.destinationPrefix.length();
			int endIndex = destination.indexOf('/', startIndex);
			Assert.isTrue(endIndex > 0, "Expected destination pattern \"/principal/{userId}/**\"");
			targetUser = destination.substring(startIndex, endIndex);
			targetUser = StringUtils.replace(targetUser, "%2F", "/");
			targetDestination = destination.substring(endIndex);
			targetSessionIds = this.userSessionRegistry.getSessionIds(targetUser);
		}
		else {
			if (logger.isTraceEnabled()) {
				logger.trace("Ignoring " + messageType + " message");
			}
			return null;
		}

		return new UserDestinationInfo(targetUser, targetDestination, targetSessionIds);
	}
{
		Class<?> handlerType = (handler instanceof String) ?
				getApplicationContext().getType((String) handler) : handler.getClass();

		final Class<?> userType = ClassUtils.getUserClass(handlerType);

		Set<Method> methods = HandlerMethodSelector.selectMethods(userType, new MethodFilter() {
			@Override
			public boolean matches(Method method) {
				return getMappingForMethod(method, userType) != null;
			}
		});

		for (Method method : methods) {
			T mapping = getMappingForMethod(method, userType);
			registerHandlerMethod(handler, method, mapping);
		}
	}
{

		ProxyFactory proxyFactory = new ProxyFactory();
		// Copy our properties (proxyTargetClass etc) inherited from ProxyConfig.
		proxyFactory.copyFrom(this);

		if (!shouldProxyTargetClass(beanClass, beanName)) {
			// Must allow for introductions; can't just set interfaces to
			// the target's interfaces only.
			Class<?>[] targetInterfaces = ClassUtils.getAllInterfacesForClass(beanClass, this.proxyClassLoader);
			for (Class<?> targetInterface : targetInterfaces) {
				proxyFactory.addInterface(targetInterface);
			}
		}

		Advisor[] advisors = buildAdvisors(beanName, specificInterceptors);
		for (Advisor advisor : advisors) {
			proxyFactory.addAdvisor(advisor);
		}

		proxyFactory.setTargetSource(targetSource);
		customizeProxyFactory(proxyFactory);

		proxyFactory.setFrozen(this.freezeProxy);
		if (advisorsPreFiltered()) {
			proxyFactory.setPreFiltered(true);
		}

		return proxyFactory.getProxy(this.proxyClassLoader);
	}
{
		Method match = null;
		for (Method method : controllerType.getDeclaredMethods()) {
			if (method.getName().equals(methodName) && method.getParameterTypes().length == argumentValues.length) {
				if (match != null) {
					throw new IllegalStateException("Found two methods named '" + methodName + "' having " +
							Arrays.asList(argumentValues) + " arguments, controller " + controllerType.getName());
				}
				match = method;
			}
		}
		if (match == null) {
			throw new IllegalArgumentException("No method '" + methodName + "' with " + argumentValues.length +
					" parameters found in " + controllerType.getName());
		}
		return fromMethod(match, argumentValues);
	}
{
		try {
			if ("true".equalsIgnoreCase(System.getProperty(IGNORE_GETENV_PROPERTY_NAME))) {
				return Collections.emptyMap();
			}
		}
		catch (Throwable ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("Could not obtain system property '" + IGNORE_GETENV_PROPERTY_NAME + "': " + ex);
			}
		}

		try {
			return (Map) System.getenv();
		}
		catch (AccessControlException ex) {
			return (Map) new ReadOnlySystemAttributesMap() {
				@Override
				protected String getSystemAttribute(String variableName) {
					try {
						return System.getenv(variableName);
					}
					catch (AccessControlException ex) {
						if (logger.isInfoEnabled()) {
							logger.info(format("Caught AccessControlException when accessing system " +
									"environment variable [%s]; its value will be returned [null]. Reason: %s",
									variableName, ex.getMessage()));
						}
						return null;
					}
				}
			};
		}
	}
{

		updateRequest(request, response, frameFormat);
		try {
			this.asyncRequestControl.start(-1);
			scheduleHeartbeat();
			tryFlushCache();
		}
		catch (Throwable ex) {
			tryCloseWithSockJsTransportError(ex, CloseStatus.SERVER_ERROR);
			throw new SockJsTransportFailureException("Failed to flush messages", getId(), ex);
		}
	}
{
		try {
			URL url = getURL();
			if (ResourceUtils.isFileURL(url)) {
				// Proceed with file system resolution...
				return getFile().exists();
			}
			else {
				// Try a URL connection content-length header...
				URLConnection con = url.openConnection();
				ResourceUtils.useCachesIfNecessary(con);
				HttpURLConnection httpCon =
						(con instanceof HttpURLConnection ? (HttpURLConnection) con : null);
				if (httpCon != null) {
					httpCon.setRequestMethod("HEAD");
					int code = httpCon.getResponseCode();
					if (code == HttpURLConnection.HTTP_OK) {
						return true;
					}
					else if (code == HttpURLConnection.HTTP_NOT_FOUND) {
						return false;
					}
				}
				if (con.getContentLength() >= 0) {
					return true;
				}
				if (httpCon != null) {
					// no HTTP OK status, and no content-length header: give up
					httpCon.disconnect();
					return false;
				}
				else {
					// Fall back to stream existence: can we open the stream?
					InputStream is = getInputStream();
					is.close();
					return true;
				}
			}
		}
		catch (IOException ex) {
			return false;
		}
	}
{
		try {
			URL url = getURL();
			if (ResourceUtils.isFileURL(url)) {
				// Proceed with file system resolution...
				return getFile().exists();
			}
			else {
				// Try a URL connection content-length header...
				URLConnection con = url.openConnection();
				ResourceUtils.useCachesIfNecessary(con);
				HttpURLConnection httpCon =
						(con instanceof HttpURLConnection ? (HttpURLConnection) con : null);
				if (httpCon != null) {
					httpCon.setRequestMethod("HEAD");
					int code = httpCon.getResponseCode();
					if (code == HttpURLConnection.HTTP_OK) {
						return true;
					}
					else if (code == HttpURLConnection.HTTP_NOT_FOUND) {
						return false;
					}
				}
				if (con.getContentLength() >= 0) {
					return true;
				}
				if (httpCon != null) {
					// no HTTP OK status, and no content-length header: give up
					httpCon.disconnect();
					return false;
				}
				else {
					// Fall back to stream existence: can we open the stream?
					InputStream is = getInputStream();
					is.close();
					return true;
				}
			}
		}
		catch (IOException ex) {
			return false;
		}
	}
{
		String classNames = servletContext.getInitParameter(CONTEXT_INITIALIZER_CLASSES_PARAM);
		List<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>> classes =
			new ArrayList<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>>();
		if (classNames != null) {
			for (String className : StringUtils.tokenizeToStringArray(classNames, ",")) {
				try {
					Class<?> clazz = ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
					Assert.isAssignable(ApplicationContextInitializer.class, clazz,
							"class [" + className + "] must implement ApplicationContextInitializer");
					classes.add((Class<ApplicationContextInitializer<ConfigurableApplicationContext>>)clazz);
				}
				catch (ClassNotFoundException ex) {
					throw new ApplicationContextException(
							"Failed to load context initializer class [" + className + "]", ex);
				}
			}
		}
		return classes;
	}
{
		if (this.contextInitializerClasses != null) {
			String[] initializerClassNames =
					StringUtils.tokenizeToStringArray(this.contextInitializerClasses, INIT_PARAM_DELIMITERS);
			for (String initializerClassName : initializerClassNames) {
				ApplicationContextInitializer<ConfigurableApplicationContext> initializer;
				try {
					Class<?> initializerClass = ClassUtils.forName(initializerClassName, wac.getClassLoader());
					initializer = BeanUtils.instantiateClass(initializerClass, ApplicationContextInitializer.class);
				}
				catch (Exception ex) {
					throw new IllegalArgumentException(
							String.format("Could not instantiate class [%s] specified via " +
							"'contextInitializerClasses' init-param", initializerClassName), ex);
				}
				this.contextInitializers.add(initializer);
			}
		}
		AnnotationAwareOrderComparator.sort(this.contextInitializers);
		for (ApplicationContextInitializer<ConfigurableApplicationContext> initializer : this.contextInitializers) {
			initializer.initialize(wac);
		}
	}
{

		DefaultContentTypeResolver contentTypeResolver = new DefaultContentTypeResolver();

		List<MessageConverter> converters = new ArrayList<MessageConverter>();
		if (jackson2Present) {
			converters.add(new MappingJackson2MessageConverter());
			contentTypeResolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
		}
		converters.add(new StringMessageConverter());
		converters.add(new ByteArrayMessageConverter());

		return new CompositeMessageConverter(converters, contentTypeResolver);
	}
{
		AntPathStringMatcher matcher = null;
		Boolean cachePatterns = this.cachePatterns;
		if (cachePatterns == null || cachePatterns.booleanValue()) {
			matcher = this.stringMatcherCache.get(pattern);
		}
		if (matcher == null) {
			matcher = new AntPathStringMatcher(pattern);
			if (cachePatterns == null && this.stringMatcherCache.size() >= CACHE_TURNOFF_THRESHOLD) {
				// Try to adapt to the runtime situation that we're encountering:
				// There are obviously too many different paths coming in here...
				// So let's turn off the cache since the patterns are unlikely to be reoccurring.
				this.cachePatterns = false;
				this.stringMatcherCache.clear();
				return matcher;
			}
			if (cachePatterns == null || cachePatterns.booleanValue()) {
				this.stringMatcherCache.put(pattern, matcher);
			}
		}
		return matcher;
	}
{
		writeStartElement(localName);
		writeEndElement();
	}
{
		Assert.notNull(methodParameter, "MethodParameter must not be null");
		ResolvableType owner = forType(methodParameter.getContainingClass()).as(methodParameter.getDeclaringClass());
		return forType(null, new MethodParameterTypeProvider(methodParameter),
				owner.asVariableResolver()).getNested(methodParameter.getNestingLevel(),
				methodParameter.typeIndexesPerLevel);
	}
{

		String beanDefinitionName = BeanFactoryUtils.transformedBeanName(beanName);
		if (containsBeanDefinition(beanDefinitionName)) {
			return isAutowireCandidate(beanName, getMergedLocalBeanDefinition(beanDefinitionName), descriptor);
		}
		else if (containsSingleton(beanName)) {
			return isAutowireCandidate(beanName, new RootBeanDefinition(getType(beanName)), descriptor);
		}
		else if (getParentBeanFactory() instanceof ConfigurableListableBeanFactory) {
			// No bean definition found in this factory -> delegate to parent.
			return ((ConfigurableListableBeanFactory) getParentBeanFactory()).isAutowireCandidate(beanName, descriptor);
		}
		else {
			return true;
		}
	}
{
		Class<? extends Exception> exceptionType = exception.getClass();
		Method method = this.exceptionLookupCache.get(exceptionType);
		if (method == null) {
			method = getMappedMethod(exceptionType);
			this.exceptionLookupCache.put(exceptionType, method != null ? method : NO_METHOD_FOUND);
		}
		return method != NO_METHOD_FOUND ? method : null;
	}
{

		Class<? extends Annotation> type = annotation.annotationType();
		RootBeanDefinition bd = (RootBeanDefinition) bdHolder.getBeanDefinition();
		AutowireCandidateQualifier qualifier = bd.getQualifier(type.getName());
		if (qualifier == null) {
			qualifier = bd.getQualifier(ClassUtils.getShortName(type));
		}
		if (qualifier == null) {
			Annotation targetAnnotation = null;
			Method resolvedFactoryMethod = bd.getResolvedFactoryMethod();
			if (resolvedFactoryMethod != null) {
				targetAnnotation = AnnotationUtils.getAnnotation(resolvedFactoryMethod, type);
			}
			if (targetAnnotation == null) {
				// look for matching annotation on the target class
				if (getBeanFactory() != null) {
					Class<?> beanType = getBeanFactory().getType(bdHolder.getBeanName());
					if (beanType != null) {
						targetAnnotation = AnnotationUtils.getAnnotation(ClassUtils.getUserClass(beanType), type);
					}
				}
				if (targetAnnotation == null && bd.hasBeanClass()) {
					targetAnnotation = AnnotationUtils.getAnnotation(ClassUtils.getUserClass(bd.getBeanClass()), type);
				}
			}
			if (targetAnnotation != null && targetAnnotation.equals(annotation)) {
				return true;
			}
		}
		Map<String, Object> attributes = AnnotationUtils.getAnnotationAttributes(annotation);
		if (attributes.isEmpty() && qualifier == null) {
			// if no attributes, the qualifier must be present
			return false;
		}
		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();
			Object expectedValue = entry.getValue();
			Object actualValue = null;
			// check qualifier first
			if (qualifier != null) {
				actualValue = qualifier.getAttribute(attributeName);
			}
			if (actualValue == null) {
				// fall back on bean definition attribute
				actualValue = bd.getAttribute(attributeName);
			}
			if (actualValue == null && attributeName.equals(AutowireCandidateQualifier.VALUE_KEY) &&
					expectedValue instanceof String && bdHolder.matchesName((String) expectedValue)) {
				// fall back on bean name (or alias) match
				continue;
			}
			if (actualValue == null && qualifier != null) {
				// fall back on default, but only if the qualifier is present
				actualValue = AnnotationUtils.getDefaultValue(annotation, attributeName);
			}
			if (actualValue != null) {
				actualValue = typeConverter.convertIfNecessary(actualValue, expectedValue.getClass());
			}
			if (!expectedValue.equals(actualValue)) {
				return false;
			}
		}
		return true;
	}
{
		ResolvableType dependencyType = descriptor.getResolvableType();
		if (dependencyType.getType() instanceof Class) {
			// No generic type -> we know it's a Class type-match, so no need to check again.
			return true;
		}
		ResolvableType targetType = null;
		RootBeanDefinition rbd = null;
		if (bdHolder.getBeanDefinition() instanceof RootBeanDefinition) {
			rbd = (RootBeanDefinition) bdHolder.getBeanDefinition();
		}
		if (rbd != null && rbd.getFactoryMethodName() != null) {
			// Should typically be set for any kind of factory method, since the BeanFactory
			// pre-resolves them before reaching out to the AutowireCandidateResolver...
			Class<?> preResolved = rbd.resolvedFactoryMethodReturnType;
			if (preResolved != null) {
				targetType = ResolvableType.forClass(preResolved);
			}
			else {
				Method resolvedFactoryMethod = rbd.getResolvedFactoryMethod();
				if (resolvedFactoryMethod != null) {
					if (descriptor.getDependencyType().isAssignableFrom(resolvedFactoryMethod.getReturnType())) {
						// Only use factory method metadata if the return type is actually expressive enough
						// for our dependency. Otherwise, the returned instance type may have matched instead
						// in case of a singleton instance having been registered with the container already.
						targetType = ResolvableType.forMethodReturnType(resolvedFactoryMethod);
					}
				}
			}
		}
		if (targetType == null) {
			// Regular case: straight bean instance, with BeanFactory available.
			if (this.beanFactory != null) {
				Class<?> beanType = this.beanFactory.getType(bdHolder.getBeanName());
				if (beanType != null) {
					targetType = ResolvableType.forClass(ClassUtils.getUserClass(beanType));
				}
			}
			// Fallback: no BeanFactory set, or no type resolvable through it
			// -> best-effort match against the target class if applicable.
			if (targetType == null && rbd != null && rbd.hasBeanClass() && rbd.getFactoryMethodName() == null) {
				Class<?> beanClass = rbd.getBeanClass();
				if (!FactoryBean.class.isAssignableFrom(beanClass)) {
					targetType = ResolvableType.forClass(ClassUtils.getUserClass(beanClass));
				}
			}
		}
		if (targetType == null) {
			return true;
		}
		if (descriptor.fallbackMatchAllowed() && targetType.hasUnresolvableGenerics()) {
			return descriptor.getDependencyType().isAssignableFrom(targetType.getRawClass());
		}
		return dependencyType.isAssignableFrom(targetType);
	}
{
		loadBeanDefinitions("mvc-config-custom-validator.xml", 13);

		RequestMappingHandlerMapping mapping = appContext.getBean(RequestMappingHandlerMapping.class);
		assertNotNull(mapping);
		assertFalse(mapping.getUrlPathHelper().shouldRemoveSemicolonContent());

		RequestMappingHandlerAdapter adapter = appContext.getBean(RequestMappingHandlerAdapter.class);
		assertNotNull(adapter);
		assertEquals(true, new DirectFieldAccessor(adapter).getPropertyValue("ignoreDefaultModelOnRedirect"));

		// default web binding initializer behavior test
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("date", "2009-10-31");
		MockHttpServletResponse response = new MockHttpServletResponse();
		adapter.handle(request, response, handlerMethod);

		assertTrue(appContext.getBean(TestValidator.class).validatorInvoked);
		assertFalse(handler.recordedValidationError);
	}
{
		Assert.notNull(declaringClass, "declaringClass must not be null");

		String[] locations = contextConfiguration.locations();
		String[] valueLocations = contextConfiguration.value();

		if (!ObjectUtils.isEmpty(valueLocations) && !ObjectUtils.isEmpty(locations)) {
			String msg = String.format("Test class [%s] has been configured with @ContextConfiguration's 'value' %s "
					+ "and 'locations' %s attributes. Only one declaration of resource "
					+ "locations is permitted per @ContextConfiguration annotation.", declaringClass.getName(),
				ObjectUtils.nullSafeToString(valueLocations), ObjectUtils.nullSafeToString(locations));
			logger.error(msg);
			throw new IllegalStateException(msg);
		}
		else if (!ObjectUtils.isEmpty(valueLocations)) {
			locations = valueLocations;
		}

		return locations;
	}
{
		List<List<ContextConfigurationAttributes>> hierarchyAttributes = resolveContextHierarchyAttributes(TestClass2WithBareContextConfigurationInSubclass.class);
		assertEquals(2, hierarchyAttributes.size());

		List<ContextConfigurationAttributes> configAttributesListClassLevel1 = hierarchyAttributes.get(0);
		debugConfigAttributes(configAttributesListClassLevel1);
		assertEquals(1, configAttributesListClassLevel1.size());
		assertThat(configAttributesListClassLevel1.get(0).getLocations()[0], equalTo("one.xml"));

		List<ContextConfigurationAttributes> configAttributesListClassLevel2 = hierarchyAttributes.get(1);
		debugConfigAttributes(configAttributesListClassLevel2);
		assertEquals(1, configAttributesListClassLevel2.size());
		assertThat(configAttributesListClassLevel2.get(0).getLocations()[0], equalTo("two.xml"));
	}
{
		Mockito.<Class<?>> when(testContext.getTestClass()).thenReturn(clazz);
		Invocable instance = clazz.newInstance();
		when(testContext.getTestInstance()).thenReturn(instance);
		when(testContext.getTestMethod()).thenReturn(clazz.getDeclaredMethod("transactionalTest"));

		assertFalse(instance.invoked);
		listener.beforeTestMethod(testContext);
		assertTrue(instance.invoked);
	}
{

		CountDownLatch latch = registerAsyncInterceptors(request);
		getMvcResult(request).setAsyncResultLatch(latch);

		super.service(request, response);
	}
{
		this.decoder.apply(Buffer.wrap(stompFrame));
		return consumer.arguments.get(0);
	}
{

		String destination = headers.getDestination();
		if (destination == null) {
			logger.trace("Ignoring message, no destination");
			return null;
		}

		String targetUser;
		String targetDestination;

		Principal user = headers.getUser();
		SimpMessageType messageType = headers.getMessageType();

		if (SimpMessageType.SUBSCRIBE.equals(messageType) || SimpMessageType.UNSUBSCRIBE.equals(messageType)) {
			if (user == null) {
				logger.trace("Ignoring (un)subscribe message, no user information");
				return null;
			}
			if (!destination.startsWith(this.subscriptionDestinationPrefix)) {
				logger.trace("Ignoring (un)subscribe message, not a \"user\" destination");
				return null;
			}
			targetUser = user.getName();
			targetDestination = destination.substring(this.destinationPrefix.length()-1);
		}
		else if (SimpMessageType.MESSAGE.equals(messageType)) {
			if (!destination.startsWith(this.destinationPrefix)) {
				logger.trace("Ignoring message, not a \"user\" destination");
				return null;
			}
			int startIndex = this.destinationPrefix.length();
			int endIndex = destination.indexOf('/', startIndex);
			Assert.isTrue(endIndex > 0, "Expected destination pattern \"/user/{userId}/**\"");
			targetUser = destination.substring(startIndex, endIndex);
			targetDestination = destination.substring(endIndex);

		}
		else {
			logger.trace("Ignoring message, not of the right message type");
			return null;
		}

		return new UserDestinationInfo(targetUser, targetDestination);
	}
{
		if (this.invalid) {
			throw new IllegalStateException("The session has already been invalidated");
		}

		// else
		this.invalid = true;
		clearAttributes();
	}
{
		AntPathStringMatcher matcher = this.stringMatcherCache.get(pattern);
		if (matcher == null) {
			matcher = new AntPathStringMatcher(pattern);
			this.stringMatcherCache.put(pattern, matcher);
		}
		return matcher.matchStrings(str, uriTemplateVariables);
	}
{
		if (type == null) {
			return NONE;
		}
		// Check the cache, we may have a ResolvableType that may have already been resolved
		ResolvableType key = new ResolvableType(type, variableResolver, null);
		ResolvableType resolvableType = cache.get(key);
		if (resolvableType == null) {
			resolvableType = key;
			cache.put(key, resolvableType);
		}
		return resolvableType;
	}
{
		ResolvableType[] generics = getGenerics();
		Class<?>[] resolvedGenerics = new Class<?>[generics.length];
		for (int i = 0; i < generics.length; i++) {
			resolvedGenerics[i] = generics[i].resolve();
		}
		return resolvedGenerics;
	}
{
		listener.zeroCounter();
		parentListener.zeroCounter();
		assertTrue("0 events before publication", listener.getEventCount() == 0);
		assertTrue("0 parent events before publication", parentListener.getEventCount() == 0);
		this.applicationContext.publishEvent(new MyEvent(this));
		assertTrue("1 events after publication, not " + listener.getEventCount(), listener.getEventCount() == 1);
		assertTrue("1 parent events after publication", parentListener.getEventCount() == 1);
	}
{
		Method[] ms = getSortedClassMethods(clazz);
		String propertyMethodSuffix = getPropertyMethodSuffix(propertyName);

		// Try "get*" method...
		String getterName = "get" + propertyMethodSuffix;
		for (Method method : ms) {
			if (method.getName().equals(getterName) && method.getParameterTypes().length == 0 &&
					(!mustBeStatic || Modifier.isStatic(method.getModifiers()))) {
				return method;
			}
		}
		// Try "is*" method...
		getterName = "is" + propertyMethodSuffix;
		for (Method method : ms) {
			if (method.getName().equals(getterName) && method.getParameterTypes().length == 0 &&
					(boolean.class.equals(method.getReturnType()) || Boolean.class.equals(method.getReturnType())) &&
					(!mustBeStatic || Modifier.isStatic(method.getModifiers()))) {
				return method;
			}
		}
		return null;
	}
{
		ControllerAdvice annotation = AnnotationUtils.findAnnotation(beanType,ControllerAdvice.class);
		Class<Annotation>[] annotations = (Class<Annotation>[])AnnotationUtils.getValue(annotation,"annotations");
		return Arrays.asList(annotations);
	}
{
		ControllerAdvice annotation = AnnotationUtils.findAnnotation(beanType,ControllerAdvice.class);
		Class<Annotation>[] annotations = (Class<Annotation>[])AnnotationUtils.getValue(annotation,"annotations");
		return Arrays.asList(annotations);
	}
{
		D destination = resolveDestination(destinationName);
		super.convertAndSend(destination, message, postProcessor);
	}
{
		while (classToIntrospect != null) {
			if (genericIfc.isInterface()) {
				Type[] ifcs = classToIntrospect.getGenericInterfaces();
				for (Type ifc : ifcs) {
					Class[] result = doResolveTypeArguments(ownerClass, ifc, genericIfc);
					if (result != null) {
						return result;
					}
				}
			}
			else {
				try {
					Class[] result = doResolveTypeArguments(ownerClass, classToIntrospect.getGenericSuperclass(), genericIfc);
					if (result != null) {
						return result;
					}
				}
				catch (MalformedParameterizedTypeException ex) {
					// from getGenericSuperclass() - return null to skip further superclass traversal
					return null;
				}
			}
			classToIntrospect = classToIntrospect.getSuperclass();
		}
		return null;
	}
{
		for (ResourceTransformer transformer : this.resourceTransformers) {
			if (transformer.willTransform(request, resource)) {
				return applyTransformers(request, transformer.transform(resource));
			}
		}
		return resource;
	}
{
		ConversionService conversionService = (ConversionService) converterRegistry;
		converterRegistry.addConverter(new ObjectToObjectConverter());
		converterRegistry.addConverter(new IdToEntityConverter(conversionService));
		converterRegistry.addConverter(new FallbackObjectToStringConverter());
	}
{
		// Check request for pre-parsed or preset locale.
		Locale locale = (Locale) request.getAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME);
		if (locale != null) {
			return locale;
		}

		// Retrieve and parse cookie value.
		Cookie cookie = WebUtils.getCookie(request, getCookieName());
		if (cookie != null) {
			locale = StringUtils.parseLocaleString(cookie.getValue());
			if (logger.isDebugEnabled()) {
				logger.debug("Parsed cookie value [" + cookie.getValue() + "] into locale '" + locale + "'");
			}
			if (locale != null) {
				request.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME, locale);
				return locale;
			}
		}

		return determineDefaultLocale(request);
	}
{
		if (locale != null) {
			// Set request attribute and add cookie.
			request.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME, locale);
			addCookie(response, locale.toString());
		}
		else {
			// Set request attribute to fallback locale and remove cookie.
			request.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME, determineDefaultLocale(request));
			removeCookie(response);
		}
	}
{

		this.port = SocketUtils.findAvailableTcpPort(61613);

		createAndStartBroker();

		this.responseChannel = new ExecutorSubscribableChannel();
		this.responseHandler = new ExpectationMatchingMessageHandler();
		this.responseChannel.subscribe(this.responseHandler);

		this.eventPublisher = new ExpectationMatchingEventPublisher();

		this.relay = new StompBrokerRelayMessageHandler(this.responseChannel, Arrays.asList("/queue/", "/topic/"));
		this.relay.setRelayPort(port);
		this.relay.setApplicationEventPublisher(this.eventPublisher);
		this.relay.start();
	}
{

		String path = request.getURI().getPath();

		// Try SockJS prefix hints
		if (!this.validSockJsPrefixes.isEmpty()) {
			for (String prefix : this.validSockJsPrefixes) {
				int index = path.lastIndexOf(prefix);
				if (index != -1) {
					this.knownSockJsPrefixes.add(path.substring(0, index + prefix.length()));
					return path.substring(index + prefix.length());
				}
			}
			return null;
		}

		// Try SockJS info request
		if (path.endsWith("/info")) {
			this.knownSockJsPrefixes.add(path.substring(0, path.length() - "/info".length()));
			return "/info";
		}

		// Have we seen this prefix before (following the initial /info request)?
		String match = null;
		for (String sockJsPath : this.knownSockJsPrefixes) {
			if (path.startsWith(sockJsPath)) {
				if ((match == null) || (match.length() < sockJsPath.length())) {
					match = sockJsPath;
				}
			}
		}
		if (match != null) {
			String result = path.substring(match.length());
			Assert.isTrue(result.charAt(0)  == '/', "Invalid SockJS path extracted from incoming path \"" +
					path + "\". The extracted SockJS path is \"" + result +
					"\". It was extracted from these known SockJS prefixes " + this.knownSockJsPrefixes +
					". Consider setting 'validSockJsPrefixes' on DefaultSockJsService.");
			return result;
		}

		// Try SockJS greeting
		String pathNoSlash = path.endsWith("/")  ? path.substring(0, path.length() - 1) : path;
		String lastSegment = pathNoSlash.substring(pathNoSlash.lastIndexOf('/') + 1);

		if (!isValidTransportType(lastSegment) && !lastSegment.startsWith("iframe")) {
			this.knownSockJsPrefixes.add(path);
			return "";
		}

		return null;
	}
{
			if (logger.isTraceEnabled()) {
				logger.trace("Reading message " + message);
			}

			StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);
			if (StompCommand.CONNECTED == headers.getCommand()) {
				this.stompConnection.setReady();
				publishBrokerAvailableEvent();
			}

			headers.setSessionId(this.sessionId);
			message = MessageBuilder.withPayloadAndHeaders(message.getPayload(), headers).build();
			sendMessageToClient(message);
		}
{
			if (logger.isErrorEnabled()) {
				logger.error(message + ", sessionId=" + this.sessionId, ex);
			}
			this.stompConnection.setDisconnected();
			sendError(message);
			publishBrokerUnavailableEvent();
		}
{

		StompHeaderAccessor connectHeaders = StompHeaderAccessor.wrap(message);
		StompHeaderAccessor connectedHeaders = StompHeaderAccessor.create(StompCommand.CONNECTED);

		Set<String> acceptVersions = connectHeaders.getAcceptVersion();
		if (acceptVersions.contains("1.2")) {
			connectedHeaders.setVersion("1.2");
		}
		else if (acceptVersions.contains("1.1")) {
			connectedHeaders.setVersion("1.1");
		}
		else if (acceptVersions.isEmpty()) {
			// 1.0
		}
		else {
			throw new StompConversionException("Unsupported version '" + acceptVersions + "'");
		}
		connectedHeaders.setHeartbeat(0,0);

		Principal principal = session.getPrincipal();
		if (principal != null) {
			connectedHeaders.setNativeHeader(CONNECTED_USER_HEADER, principal.getName());
			connectedHeaders.setNativeHeader(QUEUE_SUFFIX_HEADER, session.getId());

			if (this.queueSuffixResolver != null) {
				String suffix = session.getId();
				this.queueSuffixResolver.addQueueSuffix(principal.getName(), session.getId(), suffix);
			}
		}

		Message<byte[]> connectedMessage = MessageBuilder.withPayloadAndHeaders(new byte[0], connectedHeaders).build();
		String payload = new String(this.stompEncoder.encode(connectedMessage), Charset.forName("UTF-8"));
		session.sendMessage(new TextMessage(payload));
	}
{

		int port = SocketUtils.findAvailableTcpPort(61613);

		this.activeMQBroker = new BrokerService();
		this.activeMQBroker.addConnector("stomp://localhost:" + port);
		this.activeMQBroker.setStartAsync(false);
		this.activeMQBroker.setDeleteAllMessagesOnStartup(true);
		this.activeMQBroker.start();

		this.responseChannel = new ExecutorSubscribableChannel();
		this.responseHandler = new ExpectationMatchingMessageHandler();
		this.responseChannel.subscribe(this.responseHandler);

		this.eventPublisher = new ExpectationMatchingEventPublisher();

		this.relay = new StompBrokerRelayMessageHandler(this.responseChannel, Arrays.asList("/queue/", "/topic/"));
		this.relay.setRelayPort(port);
		this.relay.setApplicationEventPublisher(this.eventPublisher);
		this.relay.start();
	}
{
		if (logger.isDebugEnabled()) {
			logger.debug("Creating CGLIB proxy: target source is " + this.advised.getTargetSource());
		}

		try {
			Class<?> rootClass = this.advised.getTargetClass();
			Assert.state(rootClass != null, "Target class must be available for creating a CGLIB proxy");

			Class<?> proxySuperClass = rootClass;
			if (ClassUtils.isCglibProxyClass(rootClass)) {
				proxySuperClass = rootClass.getSuperclass();
				Class<?>[] additionalInterfaces = rootClass.getInterfaces();
				for (Class<?> additionalInterface : additionalInterfaces) {
					this.advised.addInterface(additionalInterface);
				}
			}

			// Validate the class, writing log messages as necessary.
			validateClassIfNecessary(proxySuperClass);

			// Configure CGLIB Enhancer...
			Enhancer enhancer = createEnhancer();
			if (classLoader != null) {
				enhancer.setClassLoader(classLoader);
				if (classLoader instanceof SmartClassLoader &&
						((SmartClassLoader) classLoader).isClassReloadable(proxySuperClass)) {
					enhancer.setUseCache(false);
				}
			}
			enhancer.setSuperclass(proxySuperClass);
			enhancer.setStrategy(new MemorySafeUndeclaredThrowableStrategy(UndeclaredThrowableException.class));
			enhancer.setInterfaces(AopProxyUtils.completeProxiedInterfaces(this.advised));
			enhancer.setInterceptDuringConstruction(false);

			Callback[] callbacks = getCallbacks(rootClass);
			enhancer.setCallbacks(callbacks);
			enhancer.setCallbackFilter(new ProxyCallbackFilter(
					this.advised.getConfigurationOnlyCopy(), this.fixedInterceptorMap, this.fixedInterceptorOffset));

			Class<?>[] types = new Class[callbacks.length];
			for (int x = 0; x < types.length; x++) {
				types[x] = callbacks[x].getClass();
			}
			enhancer.setCallbackTypes(types);

			// Generate the proxy class and create a proxy instance.
			Object proxy;
			if (this.constructorArgs != null) {
				proxy = enhancer.create(this.constructorArgTypes, this.constructorArgs);
			}
			else {
				proxy = enhancer.create();
			}

			return proxy;
		}
		catch (CodeGenerationException ex) {
			throw new AopConfigException("Could not generate CGLIB subclass of class [" +
					this.advised.getTargetClass() + "]: " +
					"Common causes of this problem include using a final class or a non-visible class",
					ex);
		}
		catch (IllegalArgumentException ex) {
			throw new AopConfigException("Could not generate CGLIB subclass of class [" +
					this.advised.getTargetClass() + "]: " +
					"Common causes of this problem include using a final class or a non-visible class",
					ex);
		}
		catch (Exception ex) {
			// TargetSource.getTarget() failed
			throw new AopConfigException("Unexpected AOP exception", ex);
		}
	}
{

		StringBuffer requestUrl = servletRequest.getRequestURL();
		String path = servletRequest.getRequestURI(); // shouldn't matter
		Map<String, String> pathParams = Collections.<String, String> emptyMap();

		ServerEndpointRegistration endpointConfig = new ServerEndpointRegistration(path, endpoint);
		endpointConfig.setSubprotocols(Arrays.asList(acceptedProtocol));

		try {
			getContainer(servletRequest).doUpgrade(servletRequest, servletResponse, endpointConfig, pathParams);
		}
		catch (ServletException ex) {
			throw new HandshakeFailureException(
					"Servlet request failed to upgrade to WebSocket, uri=" + requestUrl, ex);
		}
		catch (IOException ex) {
			throw new HandshakeFailureException(
					"Response update failed during upgrade to WebSocket, uri=" + requestUrl, ex);
		}
	}
{
		this.protocolHandlers.clear();
		for (SubProtocolHandler handler: protocolHandlers) {
			List<String> protocols = handler.getSupportedProtocols();
			if (CollectionUtils.isEmpty(protocols)) {
				logger.warn("No sub-protocols, ignoring handler " + handler);
				continue;
			}
			for (String protocol: protocols) {
				SubProtocolHandler replaced = this.protocolHandlers.put(protocol, handler);
				if (replaced != null) {
					throw new IllegalStateException("Failed to map handler " + handler
							+ " to protocol '" + protocol + "', it is already mapped to handler " + replaced);
				}
			}
		}
		if ((this.protocolHandlers.size() == 1) &&(this.defaultProtocolHandler == null)) {
			this.defaultProtocolHandler = this.protocolHandlers.values().iterator().next();
		}
	}
{
		AnnotationMetadata metadata = abd.getMetadata();
		if (metadata.isAnnotated(Primary.class.getName())) {
			abd.setPrimary(true);
		}
		if (metadata.isAnnotated(Lazy.class.getName())) {
			abd.setLazyInit(attributesFor(metadata, Lazy.class).getBoolean("value"));
		}
		if (metadata.isAnnotated(DependsOn.class.getName())) {
			abd.setDependsOn(attributesFor(metadata, DependsOn.class).getStringArray("value"));
		}
		if (abd instanceof AbstractBeanDefinition) {
			if (metadata.isAnnotated(Role.class.getName())) {
				Integer role = attributesFor(metadata, Role.class).getNumber("value");
				((AbstractBeanDefinition)abd).setRole(role);
			}
		}
	}
{

		Assert.isTrue(request instanceof ServletServerHttpRequest);
		HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();

		Assert.isTrue(response instanceof ServletServerHttpResponse);
		HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();

		String path = servletRequest.getRequestURI(); // shouldn't matter
		Map<String, String> pathParams = Collections.<String, String> emptyMap();

		ServerEndpointRegistration endpointConfig = new ServerEndpointRegistration(path, endpoint);
		endpointConfig.setSubprotocols(Arrays.asList(acceptedProtocol));

		try {
			getContainer(servletRequest).doUpgrade(servletRequest, servletResponse, endpointConfig, pathParams);
		}
		catch (ServletException ex) {
			throw new HandshakeFailureException(
					"Servlet request failed to upgrade to WebSocket, uri=" + request.getURI(), ex);
		}
		catch (IOException ex) {
			throw new HandshakeFailureException(
					"Response update failed during upgrade to WebSocket, uri=" + request.getURI(), ex);
		}
	}
{

		Assert.isTrue(request instanceof ServletServerHttpRequest);
		HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();

		WsHttpUpgradeHandler upgradeHandler;
		try {
			upgradeHandler = servletRequest.upgrade(WsHttpUpgradeHandler.class);
		}
		catch (ServletException e) {
			throw new HandshakeFailureException("Unable to create UpgardeHandler", e);
		}

		WsHandshakeRequest webSocketRequest = new WsHandshakeRequest(servletRequest);
		try {
			Method method = ReflectionUtils.findMethod(WsHandshakeRequest.class, "finished");
			ReflectionUtils.makeAccessible(method);
			method.invoke(webSocketRequest);
		}
		catch (Exception ex) {
			throw new HandshakeFailureException("Failed to upgrade HttpServletRequest", ex);
		}

		String attribute = "javax.websocket.server.ServerContainer";
		ServletContext servletContext = servletRequest.getServletContext();
		WsServerContainer serverContainer = (WsServerContainer) servletContext.getAttribute(attribute);

		ServerEndpointConfig endpointConfig = new ServerEndpointRegistration("/shouldntmatter", endpoint);

		upgradeHandler.preInit(endpoint, endpointConfig, serverContainer, webSocketRequest,
				acceptedProtocol, Collections.<String, String> emptyMap(), servletRequest.isSecure());
	}
{
		RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
		rbta.setPropagationBehaviorName(RuleBasedTransactionAttribute.PREFIX_PROPAGATION + ann.value().toString());
		ArrayList<RollbackRuleAttribute> rollBackRules = new ArrayList<RollbackRuleAttribute>();
		Class[] rbf = ann.rollbackOn();
		for (Class rbRule : rbf) {
			RollbackRuleAttribute rule = new RollbackRuleAttribute(rbRule);
			rollBackRules.add(rule);
		}
		Class[] nrbf = ann.dontRollbackOn();
		for (Class rbRule : nrbf) {
			NoRollbackRuleAttribute rule = new NoRollbackRuleAttribute(rbRule);
			rollBackRules.add(rule);
		}
		rbta.getRollbackRules().addAll(rollBackRules);
		return rbta;
	}
{
		RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
		rbta.setPropagationBehavior(ann.propagation().value());
		rbta.setIsolationLevel(ann.isolation().value());
		rbta.setTimeout(ann.timeout());
		rbta.setReadOnly(ann.readOnly());
		rbta.setQualifier(ann.value());
		ArrayList<RollbackRuleAttribute> rollBackRules = new ArrayList<RollbackRuleAttribute>();
		Class[] rbf = ann.rollbackFor();
		for (Class rbRule : rbf) {
			RollbackRuleAttribute rule = new RollbackRuleAttribute(rbRule);
			rollBackRules.add(rule);
		}
		String[] rbfc = ann.rollbackForClassName();
		for (String rbRule : rbfc) {
			RollbackRuleAttribute rule = new RollbackRuleAttribute(rbRule);
			rollBackRules.add(rule);
		}
		Class[] nrbf = ann.noRollbackFor();
		for (Class rbRule : nrbf) {
			NoRollbackRuleAttribute rule = new NoRollbackRuleAttribute(rbRule);
			rollBackRules.add(rule);
		}
		String[] nrbfc = ann.noRollbackForClassName();
		for (String rbRule : nrbfc) {
			NoRollbackRuleAttribute rule = new NoRollbackRuleAttribute(rbRule);
			rollBackRules.add(rule);
		}
		rbta.getRollbackRules().addAll(rollBackRules);
		return rbta;
	}
{
			TcpConnection<String, String> localConnection = this.connection;
			if (localConnection == null) {
				return false;
			}

			if (logger.isTraceEnabled()) {
				logger.trace("Forwarding to STOMP broker, message: " + message);
			}

			byte[] bytes = stompMessageConverter.fromMessage(message);
			String payload = new String(bytes, Charset.forName("UTF-8"));

			final Deferred<Boolean, Promise<Boolean>> deferred = new DeferredPromiseSpec<Boolean>().get();
			localConnection.send(payload, new Consumer<Boolean>() {
				@Override
				public void accept(Boolean success) {
					deferred.accept(success);
				}
			});

			Boolean success = null;
			try {
				success = deferred.compose().await();
				if (success == null) {
					sendError(sessionId, "Timed out waiting for message to be forwarded to the broker");
				}
				else if (!success) {
					if (StompHeaderAccessor.wrap(message).getCommand() != StompCommand.DISCONNECT) {
						sendError(sessionId, "Failed to forward message to the broker");
					}
				}
			}
			catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
				sendError(sessionId, "Interrupted while forwarding message to the broker");
			}
			return (success != null) ? success : false;
		}
{
			brokerUnavailable();

			StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.ERROR);
			headers.setSessionId(sessionId);
			headers.setMessage(errorText);
			Message<?> errorMessage = MessageBuilder.withPayloadAndHeaders(new byte[0], headers).build();
			sendMessageToClient(errorMessage);
		}
{
		StaticApplicationContext context = new StaticApplicationContext();
		BeanDefinition processorDefinition = new RootBeanDefinition(ScheduledAnnotationBeanPostProcessor.class);
		BeanDefinition targetDefinition = new RootBeanDefinition(SeveralFixedRatesTestBean.class);
		context.registerBeanDefinition("postProcessor", processorDefinition);
		context.registerBeanDefinition("target", targetDefinition);
		context.refresh();
		Object postProcessor = context.getBean("postProcessor");
		Object target = context.getBean("target");
		ScheduledTaskRegistrar registrar = (ScheduledTaskRegistrar)
				new DirectFieldAccessor(postProcessor).getPropertyValue("registrar");
		@SuppressWarnings("unchecked")
		List<IntervalTask> fixedRateTasks = (List<IntervalTask>)
				new DirectFieldAccessor(registrar).getPropertyValue("fixedRateTasks");
		assertEquals(2, fixedRateTasks.size());
		IntervalTask task1 = fixedRateTasks.get(0);
		ScheduledMethodRunnable runnable1 = (ScheduledMethodRunnable) task1.getRunnable();
		Object targetObject = runnable1.getTarget();
		Method targetMethod = runnable1.getMethod();
		assertEquals(target, targetObject);
		assertEquals("fixedRate", targetMethod.getName());
		assertEquals(0, task1.getInitialDelay());
		assertEquals(4000L, task1.getInterval());
		IntervalTask task2 = fixedRateTasks.get(1);
		ScheduledMethodRunnable runnable2 = (ScheduledMethodRunnable) task2.getRunnable();
		targetObject = runnable2.getTarget();
		targetMethod = runnable2.getMethod();
		assertEquals(target, targetObject);
		assertEquals("fixedRate", targetMethod.getName());
		assertEquals(2000L, task2.getInitialDelay());
		assertEquals(4000L, task2.getInterval());
	}
{
		XStream xstream = new XStream(this.reflectionProvider, this.streamDriver,
				this.beanClassLoader, this.mapper, this.converterLookup, this.converterRegistry) {
			@Override
			protected MapperWrapper wrapMapper(MapperWrapper next) {
				MapperWrapper mapperToWrap = next;
				if (mapperWrappers != null) {
					for (Class<?> mapperWrapper : mapperWrappers) {
						Assert.isAssignable(MapperWrapper.class, mapperWrapper);
						Constructor<?> ctor;
						try {
							ctor = mapperWrapper.getConstructor(Mapper.class);
						}
						catch (NoSuchMethodException ex) {
							try {
								ctor = mapperWrapper.getConstructor(MapperWrapper.class);
							}
							catch (NoSuchMethodException ex2) {
								throw new IllegalStateException("No appropriate MapperWrapper constructor found: " + mapperWrapper);
							}
						}
						try {
							mapperToWrap = (MapperWrapper) ctor.newInstance(mapperToWrap);
						}
						catch (Exception ex) {
							throw new IllegalStateException("Failed to construct MapperWrapper: " + mapperWrapper);
						}
					}
				}
				return mapperToWrap;
			}
		};

		if (this.converters != null) {
			for (int i = 0; i < this.converters.length; i++) {
				if (this.converters[i] instanceof Converter) {
					xstream.registerConverter((Converter) this.converters[i], i);
				}
				else if (this.converters[i] instanceof SingleValueConverter) {
					xstream.registerConverter((SingleValueConverter) this.converters[i], i);
				}
				else {
					throw new IllegalArgumentException("Invalid ConverterMatcher [" + this.converters[i] + "]");
				}
			}
		}

		if (this.marshallingStrategy != null) {
			xstream.setMarshallingStrategy(this.marshallingStrategy);
		}
		if (this.mode != null) {
			xstream.setMode(this.mode);
		}

		try {
			if (this.aliases != null) {
				Map<String, Class<?>> classMap = toClassMap(this.aliases);
				for (Map.Entry<String, Class<?>> entry : classMap.entrySet()) {
					xstream.alias(entry.getKey(), entry.getValue());
				}
			}
			if (this.aliasesByType != null) {
				Map<String, Class<?>> classMap = toClassMap(this.aliasesByType);
				for (Map.Entry<String, Class<?>> entry : classMap.entrySet()) {
					xstream.aliasType(entry.getKey(), entry.getValue());
				}
			}
			if (this.fieldAliases != null) {
				for (Map.Entry<String, String> entry : this.fieldAliases.entrySet()) {
					String alias = entry.getValue();
					String field = entry.getKey();
					int idx = field.lastIndexOf('.');
					if (idx != -1) {
						String className = field.substring(0, idx);
						Class<?> clazz = ClassUtils.forName(className, this.beanClassLoader);
						String fieldName = field.substring(idx + 1);
						xstream.aliasField(alias, clazz, fieldName);
					}
					else {
						throw new IllegalArgumentException("Field name [" + field + "] does not contain '.'");
					}
				}
			}
		}
		catch (ClassNotFoundException ex) {
			throw new IllegalStateException("Failed to load specified alias class", ex);
		}

		if (this.useAttributeForTypes != null) {
			for (Class<?> type : this.useAttributeForTypes) {
				xstream.useAttributeFor(type);
			}
		}
		if (this.useAttributeFor != null) {
			for (Map.Entry<?, ?> entry : this.useAttributeFor.entrySet()) {
				if (entry.getKey() instanceof String) {
					if (entry.getValue() instanceof Class) {
						xstream.useAttributeFor((String) entry.getKey(), (Class) entry.getValue());
					}
					else {
						throw new IllegalArgumentException(
								"'useAttributesFor' takes Map<String, Class> when using a map key of type String");
					}
				}
				else if (entry.getKey() instanceof Class) {
					Class<?> key = (Class<?>) entry.getKey();
					if (entry.getValue() instanceof String) {
						xstream.useAttributeFor(key, (String) entry.getValue());
					}
					else if (entry.getValue() instanceof List) {
						List listValue = (List) entry.getValue();
						for (Object element : listValue) {
							if (element instanceof String) {
								xstream.useAttributeFor(key, (String) element);
							}
						}
					}
					else {
						throw new IllegalArgumentException("'useAttributesFor' property takes either Map<Class, String> " +
								"or Map<Class, List<String>> when using a map key of type Class");
					}
				}
				else {
					throw new IllegalArgumentException(
							"'useAttributesFor' property takes either a map key of type String or Class");
				}
			}
		}

		if (this.implicitCollections != null) {
			for (Map.Entry<Class<?>, String> entry : this.implicitCollections.entrySet()) {
				String[] collectionFields = StringUtils.commaDelimitedListToStringArray(entry.getValue());
				for (String collectionField : collectionFields) {
					xstream.addImplicitCollection(entry.getKey(), collectionField);
				}
			}
		}
		if (this.omittedFields != null) {
			for (Map.Entry<Class<?>, String> entry : this.omittedFields.entrySet()) {
				String[] fields = StringUtils.commaDelimitedListToStringArray(entry.getValue());
				for (String field : fields) {
					xstream.omitField(entry.getKey(), field);
				}
			}
		}

		if (this.annotatedClasses != null) {
			xstream.processAnnotations(this.annotatedClasses);
		}
		if (this.autodetectAnnotations) {
			xstream.autodetectAnnotations(this.autodetectAnnotations);
		}

		customizeXStream(xstream);
		return xstream;
	}
{
		resetResponse();
		this.servletRequest = new MockHttpServletRequest();
		this.servletRequest.setAsyncSupported(true);
		this.request = new AsyncServletServerHttpRequest(this.servletRequest, this.servletResponse);
	}
{
		boolean debugEnabled = logger.isDebugEnabled();
		if (logger.isTraceEnabled()) {
			logger.trace(String.format("getProperty(\"%s\", %s)", key, targetValueType.getSimpleName()));
		}
		if (this.propertySources != null) {
			for (PropertySource<?> propertySource : this.propertySources) {
				if (debugEnabled) {
					logger.debug(String.format("Searching for key '%s' in [%s]", key, propertySource.getName()));
				}
				Object value;
				if ((value = propertySource.getProperty(key)) != null) {
					Class<?> valueType = value.getClass();
					if (String.class.equals(valueType)) {
						value = this.resolveNestedPlaceholders((String) value);
					}
					if (debugEnabled) {
						logger.debug(String.format("Found key '%s' in [%s] with type [%s] and value '%s'",
								key, propertySource.getName(), valueType.getSimpleName(), value));
					}
					if (!this.conversionService.canConvert(valueType, targetValueType)) {
						throw new IllegalArgumentException(String.format(
								"Cannot convert value [%s] from source type [%s] to target type [%s]",
								value, valueType.getSimpleName(), targetValueType.getSimpleName()));
					}
					return conversionService.convert(value, targetValueType);
				}
			}
		}
		if (debugEnabled) {
			logger.debug(String.format("Could not find key '%s' in any property source. Returning [null]", key));
		}
		return null;
	}
{
		Class<?> paramType = parameter.getParameterType();
		if (Collection.class.equals(paramType) || List.class.isAssignableFrom(paramType)){
			Class<?> valueType = GenericCollectionTypeResolver.getCollectionParameterType(parameter);
			if (valueType != null && valueType.equals(MultipartFile.class)) {
				return true;
			}
		}
		return false;
	}
{
		Class<?> paramType = parameter.getParameterType();
		if (Collection.class.equals(paramType) || List.class.isAssignableFrom(paramType)){
			Class<?> valueType = GenericCollectionTypeResolver.getCollectionParameterType(parameter);
			if (valueType != null && valueType.equals(MultipartFile.class)) {
				return true;
			}
		}
		return false;
	}
{

		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(message);
		SimpMessageType messageType = headers.getMessageType();
		String destination = headers.getDestination();

		if (!SimpMessageType.MESSAGE.equals(messageType)) {
			return false;
		}

		if (!StringUtils.hasText(destination)) {
			if (logger.isErrorEnabled()) {
				logger.error("Ignoring message, no destination: " + headers);
			}
			return false;
		}
		else if (!destination.startsWith(this.prefix)) {
			return false;
		}

		return true;
	}
{
		if (SimpMessageType.CONNECT.equals(messageType)) {
			RelaySession session = new RelaySession(sessionId);
			this.relaySessions.put(sessionId, session);
			session.open(message);
		}
		else if (SimpMessageType.DISCONNECT.equals(messageType)) {
			RelaySession session = this.relaySessions.remove(sessionId);
			if (session == null) {
				if (logger.isTraceEnabled()) {
					logger.trace("Session already removed, sessionId=" + sessionId);
				}
				return;
			}
			session.forward(message);
		}
		else {
			RelaySession session = this.relaySessions.get(sessionId);
			if (session == null) {
				logger.warn("Session id=" + sessionId + " not found. Ignoring message: " + message);
				return;
			}
			session.forward(message);
		}
	}
{
		SimpMessageHeaderAccessor inputHeaders = SimpMessageHeaderAccessor.wrap(inputMessage);
		Principal user = inputHeaders.getUser();
		if (user == null) {
			throw new MissingSessionUserException(inputMessage);
		}
		return user;
	}
{
		List<String> headersToRemove = new ArrayList<String>();
		for (String pattern : headerPatterns) {
			if (StringUtils.hasLength(pattern)){
				if (pattern.contains("*")){
					for (String headerName : this.headers.keySet()) {
						if (PatternMatchUtils.simpleMatch(pattern, headerName)){
							headersToRemove.add(headerName);
						}
					}
				}
				else {
					headersToRemove.add(pattern);
				}
			}
		}
		for (String headerToRemove : headersToRemove) {
			removeHeader(headerToRemove);
		}
	}
{
		TypedValue currentContext = state.getActiveContextObject();
		Object[] arguments = new Object[getChildCount()];
		for (int i = 0; i < arguments.length; i++) {
			// Make the root object the active context again for evaluating the parameter
			// expressions
			try {
				state.pushActiveContextObject(state.getRootContextObject());
				arguments[i] = this.children[i].getValueInternal(state).getValue();
			}
			finally {
				state.popActiveContextObject();
			}
		}
		TypedValue activeContextObject = state.getActiveContextObject();
		TypeDescriptor target = (activeContextObject == null ? null
				: activeContextObject.getTypeDescriptor());
		List<TypeDescriptor> argumentTypes = getTypes(arguments);
		if (currentContext.getValue() == null) {
			if (this.nullSafe) {
				return TypedValue.NULL;
			}
			else {
				throw new SpelEvaluationException(getStartPosition(), SpelMessage.METHOD_CALL_ON_NULL_OBJECT_NOT_ALLOWED,
						FormatHelper.formatMethodForMessage(this.name, argumentTypes));
			}
		}

		MethodExecutor executorToUse = getCachedExecutor(target, argumentTypes);
		if (executorToUse != null) {
			try {
				return executorToUse.execute(state.getEvaluationContext(),
						state.getActiveContextObject().getValue(), arguments);
			}
			catch (AccessException ae) {
				// Two reasons this can occur:
				// 1. the method invoked actually threw a real exception
				// 2. the method invoked was not passed the arguments it expected and has become 'stale'

				// In the first case we should not retry, in the second case we should see if there is a
				// better suited method.

				// To determine which situation it is, the AccessException will contain a cause.
				// If the cause is an InvocationTargetException, a user exception was thrown inside the method.
				// Otherwise the method could not be invoked.
				throwSimpleExceptionIfPossible(state, ae);

				// at this point we know it wasn't a user problem so worth a retry if a better candidate can be found
				this.cachedExecutor = null;
			}
		}

		// either there was no accessor or it no longer existed
		executorToUse = findAccessorForMethod(this.name, argumentTypes, state);
		this.cachedExecutor = new CachedMethodExecutor(executorToUse, target, argumentTypes);
		try {
			return executorToUse.execute(state.getEvaluationContext(),
					state.getActiveContextObject().getValue(), arguments);
		}
		catch (AccessException ae) {
			// Same unwrapping exception handling as above in above catch block
			throwSimpleExceptionIfPossible(state, ae);
			throw new SpelEvaluationException( getStartPosition(), ae, SpelMessage.EXCEPTION_DURING_METHOD_INVOCATION,
					this.name, state.getActiveContextObject().getValue().getClass().getName(), ae.getMessage());
		}
	}
{
		TypedValue currentContext = state.getActiveContextObject();
		Object[] arguments = new Object[getChildCount()];
		for (int i = 0; i < arguments.length; i++) {
			// Make the root object the active context again for evaluating the parameter
			// expressions
			try {
				state.pushActiveContextObject(state.getRootContextObject());
				arguments[i] = this.children[i].getValueInternal(state).getValue();
			}
			finally {
				state.popActiveContextObject();
			}
		}
		if (currentContext.getValue() == null) {
			if (this.nullSafe) {
				return ValueRef.NullValueRef.instance;
			}
			else {
				throw new SpelEvaluationException(getStartPosition(), SpelMessage.METHOD_CALL_ON_NULL_OBJECT_NOT_ALLOWED,
						FormatHelper.formatMethodForMessage(this.name, getTypes(arguments)));
			}
		}
		return new MethodValueRef(state,state.getEvaluationContext(),state.getActiveContextObject().getValue(),arguments);
	}
{
		TypedValue currentContext = state.getActiveContextObject();
		Object[] arguments = new Object[getChildCount()];
		for (int i = 0; i < arguments.length; i++) {
			// Make the root object the active context again for evaluating the parameter
			// expressions
			try {
				state.pushActiveContextObject(state.getRootContextObject());
				arguments[i] = this.children[i].getValueInternal(state).getValue();
			}
			finally {
				state.popActiveContextObject();
			}
		}
		if (currentContext.getValue() == null) {
			if (this.nullSafe) {
				return ValueRef.NullValueRef.instance;
			}
			else {
				throw new SpelEvaluationException(getStartPosition(), SpelMessage.METHOD_CALL_ON_NULL_OBJECT_NOT_ALLOWED,
						FormatHelper.formatMethodForMessage(this.name, getTypes(arguments)));
			}
		}
		return new MethodValueRef(state,state.getEvaluationContext(),state.getActiveContextObject().getValue(),arguments);
	}
{
		return getFirstDate(IF_MODIFIED_SINCE);
	}
{

		PubSubHeaderAccesssor headers = PubSubHeaderAccesssor.wrap(message);
		Assert.notNull(headers.getSubscriptionId(), "No subscription id: " + message);

		PubSubHeaderAccesssor returnHeaders = PubSubHeaderAccesssor.wrap(returnMessage);
		returnHeaders.setSessionId(headers.getSessionId());
		returnHeaders.setSubscriptionId(headers.getSubscriptionId());

		if (returnHeaders.getDestination() == null) {
			returnHeaders.setDestination(headers.getDestination());
		}

		return createMessage(returnMessage.getPayload(), returnHeaders.toHeaders());
	}
{
		// check whether aspect is enabled
		// to cope with cases where the AJ is pulled in automatically
		if (!this.initialized) {
			return invoker.invoke();
		}

		// get backing class
		Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
		if (targetClass == null && target != null) {
			targetClass = target.getClass();
		}
		final Collection<CacheOperation> cacheOp = getCacheOperationSource().getCacheOperations(method, targetClass);

		// analyze caching information
		if (!CollectionUtils.isEmpty(cacheOp)) {
			Map<String, Collection<CacheOperationContext>> ops = createOperationContext(cacheOp, method, args, target, targetClass);

			// start with evictions
			inspectBeforeCacheEvicts(ops.get(EVICT));

			// follow up with cacheable
			CacheStatus status = inspectCacheables(ops.get(CACHEABLE));

			Object retVal = null;
			Map<CacheOperationContext, Object> updates = inspectCacheUpdates(ops.get(UPDATE));

			if (status != null) {
				if (status.updateRequired) {
					updates.putAll(status.cUpdates);
				}
				// return cached object
				else {
					return status.retVal;
				}
			}

			retVal = invoker.invoke();

			inspectAfterCacheEvicts(ops.get(EVICT), retVal);

			if (!updates.isEmpty()) {
				update(updates, retVal);
			}

			return retVal;
		}

		return invoker.invoke();
	}
{
		List<Class<? extends Throwable>> result = new ArrayList<Class<? extends Throwable>>();

		ExceptionHandler annotation = AnnotationUtils.findAnnotation(method, ExceptionHandler.class);
		result.addAll(Arrays.asList(annotation.value()));

		if (result.isEmpty()) {
			for (Class<?> paramType : method.getParameterTypes()) {
				if (Throwable.class.isAssignableFrom(paramType)) {
					result.add((Class<? extends Throwable>) paramType);
				}
			}
		}

		Assert.notEmpty(result, "No exception types mapped to {" + method + "}");

		return result;
	}
{

			if (StringUtils.isEmpty(stompFrame)) {
				// heartbeat?
				return;
			}

			M message = stompMessageConverter.toMessage(stompFrame, this.sessionId);
			if (logger.isTraceEnabled()) {
				logger.trace("Reading message " + message);
			}

			StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);
			if (StompCommand.CONNECTED == headers.getStompCommand()) {
				synchronized(this.monitor) {
					this.isConnected = true;
					flushMessages(promise.get());
				}
				return;
			}
			if (StompCommand.ERROR == headers.getStompCommand()) {
				if (logger.isDebugEnabled()) {
					logger.warn("STOMP ERROR: " + headers.getMessage() + ". Removing session: " + this.sessionId);
				}
				relaySessions.remove(this.sessionId);
			}
			clientChannel.send(message);
		}
{
		List<Registration<?>> list = this.subscriptionsBySession.get(sessionId);
		if (list == null) {
			list = new ArrayList<Registration<?>>();
			this.subscriptionsBySession.put(sessionId, list);
		}
		list.add(registration);
	}
{

		Message<?> returnMessage = (Message<?>) returnValue;
		if (returnMessage == null) {
			return;
		}

		PubSubHeaders inHeaders = PubSubHeaders.fromMessageHeaders(message.getHeaders());
		String sessionId = inHeaders.getSessionId();
		String subscriptionId = inHeaders.getSubscriptionId();
		Assert.notNull(subscriptionId, "No subscription id: " + message);

		PubSubHeaders outHeaders = PubSubHeaders.fromMessageHeaders(returnMessage.getHeaders());
		outHeaders.setSessionId(sessionId);
		outHeaders.setSubscriptionId(subscriptionId);
		returnMessage = messageFactory.createMessage(returnMessage.getPayload(), outHeaders.toMessageHeaders());

		this.clientChannel.send(returnMessage);
 	}
{
		RelaySession relaySession = this.relaySessions.remove(stompSessionId);
		if (relaySession != null) {
			// TODO: raise failure event so client session can be closed
			try {
				relaySession.getSocket().close();
			}
			catch (IOException e) {
				// ignore
			}
		}
	}
{

		StompSession stompSession = this.sessions.get(session.getId());
		Assert.notNull(stompSession, "No STOMP session for WebSocket session id=" + session.getId());

		try {
			StompMessage stompMessage = this.messageConverter.toStompMessage(message.getPayload());
			stompMessage.setSessionId(stompSession.getId());

			// TODO: validate size limits
			// http://stomp.github.io/stomp-specification-1.2.html#Size_Limits

			handleStompMessage(stompSession, stompMessage);

			// TODO: send RECEIPT message if incoming message has "receipt" header
			// http://stomp.github.io/stomp-specification-1.2.html#Header_receipt

		}
		catch (Throwable error) {
			StompHeaders headers = new StompHeaders();
			headers.setMessage(error.getMessage());
			StompMessage errorMessage = new StompMessage(StompCommand.ERROR, headers);
			try {
				stompSession.sendMessage(errorMessage);
			}
			catch (Throwable t) {
				// ignore
			}
		}
	}
{
		logger.error("Terminating STOMP session due to failure to send message: ", t);
		sendErrorMessage(session, t.getMessage());
		if (removeSubscriptions(session)) {
			// TODO: send error event including exception info
		}
	}
{

		if (this.configurationClasses.contains(configClass) && configClass.getBeanName() != null) {
			// Explicit bean definition found, probably replacing an import.
			// Let's remove the old one and go with the new one.
			this.configurationClasses.remove(configClass);
			for (Iterator<ConfigurationClass> it = this.knownSuperclasses.values().iterator(); it.hasNext();) {
				if (configClass.equals(it.next())) {
					it.remove();
				}
			}
		}

		// Recursively process the configuration class and its superclass hierarchy.
		AnnotationMetadata metadata = configClass.getMetadata();
		do {
			metadata = doProcessConfigurationClass(configClass, metadata);
		}
		while (metadata != null);

		this.configurationClasses.add(configClass);
	}
{
		if (this.invalid) {
			throw new IllegalStateException("The session has already been invalidated");
		}

		// else
		this.invalid = true;
		clearAttributes();
	}
{
		this.servletRequest = new MockHttpServletRequest();
		this.servletResponse = new MockHttpServletResponse();
		this.request = new AsyncServletServerHttpRequest(this.servletRequest, this.servletResponse);
		this.response = new ServletServerHttpResponse(this.servletResponse);
	}
{
		// Register annotations that the annotation type is annotated with.
		Set<String> metaAnnotationTypeNames = new LinkedHashSet<String>();
		for (Annotation metaAnnotation : annotationClass.getAnnotations()) {
			metaAnnotationTypeNames.add(metaAnnotation.annotationType().getName());
			if (!this.attributesMap.containsKey(metaAnnotation.annotationType().getName())) {
				this.attributesMap.put(metaAnnotation.annotationType().getName(),
						AnnotationUtils.getAnnotationAttributes(metaAnnotation, true, true));
			}
			for (Annotation metaMetaAnnotation : metaAnnotation.annotationType().getAnnotations()) {
				metaAnnotationTypeNames.add(metaMetaAnnotation.annotationType().getName());
			}
		}
		if (this.metaAnnotationMap != null) {
			this.metaAnnotationMap.put(annotationClass.getName(), metaAnnotationTypeNames);
		}
	}
{

		URI uri = uriComponents.toUri();

		StandardWebSocketSessionAdapter session = new StandardWebSocketSessionAdapter();
		session.setUri(uri);
		session.setRemoteHostName(uriComponents.getHost());
		Endpoint endpoint = new StandardEndpointAdapter(webSocketHandler, session);

		ClientEndpointConfig.Builder configBuidler = ClientEndpointConfig.Builder.create();
		if (httpHeaders != null) {
			List<String> protocols = httpHeaders.getSecWebSocketProtocol();
			if (!protocols.isEmpty()) {
				configBuidler.preferredSubprotocols(protocols);
			}
			configBuidler.configurator(new Configurator() {
				@Override
				public void beforeRequest(Map<String, List<String>> headers) {
					for (String headerName : httpHeaders.keySet()) {
						if (!EXCLUDED_HEADERS.contains(headerName)) {
							List<String> value = httpHeaders.get(headerName);
							if (logger.isTraceEnabled()) {
								logger.trace("Adding header [" + headerName + "=" + value + "]");
							}
							headers.put(headerName, value);
						}
					}
					if (logger.isTraceEnabled()) {
						logger.trace("Handshake request headers: " + headers);
					}
				}
				@Override
				public void afterResponse(HandshakeResponse handshakeResponse) {
					if (logger.isTraceEnabled()) {
						logger.trace("Handshake response headers: " + handshakeResponse.getHeaders());
					}
				}
			});
		}

		try {
			// TODO: do not block
			this.webSocketContainer.connectToServer(endpoint, configBuidler.build(), uri);
			return session;
		}
		catch (Exception e) {
			throw new WebSocketConnectFailureException("Failed to connect to " + uri, e);
		}
	}
{

		// TODO: populate headers

		URI uri = uriComponents.toUri();

		JettyWebSocketSessionAdapter session = new JettyWebSocketSessionAdapter();
		session.setUri(uri);
		session.setRemoteHostName(uriComponents.getHost());

		JettyWebSocketListenerAdapter listener = new JettyWebSocketListenerAdapter(webSocketHandler, session);

		try {
			// TODO: do not block
			this.client.connect(listener, uri).get();
			return session;
		}
		catch (Exception e) {
			throw new WebSocketConnectFailureException("Failed to connect to " + uri, e);
		}
	}
{

		Endpoint endpoint = new StandardEndpointAdapter(webSocketHandler);

		ClientEndpointConfig.Builder configBuidler = ClientEndpointConfig.Builder.create();
		if (httpHeaders != null) {
			List<String> protocols = httpHeaders.getSecWebSocketProtocol();
			if (!protocols.isEmpty()) {
				configBuidler.preferredSubprotocols(protocols);
			}
			configBuidler.configurator(new Configurator() {
				@Override
				public void beforeRequest(Map<String, List<String>> headers) {
					for (String headerName : httpHeaders.keySet()) {
						if (!EXCLUDED_HEADERS.contains(headerName)) {
							List<String> value = httpHeaders.get(headerName);
							if (logger.isTraceEnabled()) {
								logger.trace("Adding header [" + headerName + "=" + value + "]");
							}
							headers.put(headerName, value);
						}
					}
					if (logger.isTraceEnabled()) {
						logger.trace("Handshake request headers: " + headers);
					}
				}
				@Override
				public void afterResponse(HandshakeResponse handshakeResponse) {
					if (logger.isTraceEnabled()) {
						logger.trace("Handshake response headers: " + handshakeResponse.getHeaders());
					}
				}
			});
		}

		try {
			Session session = this.webSocketContainer.connectToServer(endpoint, configBuidler.build(), uri);
			return new StandardWebSocketSessionAdapter(session);
		}
		catch (Exception e) {
			throw new WebSocketConnectFailureException("Failed to connect to " + uri, e);
		}
	}
{

		// TODO: populate headers

		JettyWebSocketListenerAdapter listener = new JettyWebSocketListenerAdapter(webSocketHandler);

		try {
			// block for now
			Future<org.eclipse.jetty.websocket.api.Session> future = this.client.connect(listener, uri);
			Session session = future.get();
			return new JettyWebSocketSessionAdapter(session);
		}
		catch (Exception e) {
			throw new WebSocketConnectFailureException("Failed to connect to " + uri, e);
		}
	}
{

		Assert.state(this.factory.isUpgradeRequest(request, response), "Expected websocket upgrade request");

		request.setAttribute(HANDLER_PROVIDER_ATTR_NAME, webSocketHandler);

		if (!this.factory.acceptWebSocket(request, response)) {
			// should never happen
			throw new HandshakeFailureException("WebSocket request not accepted by Jetty");
		}
	}
{
		return mergeProperties();
	}
{
		synchronized (this.lifecycleMonitor) {
			if (!isRunning()) {
				this.taskExecutor.execute(new Runnable() {
					@Override
					public void run() {
						synchronized (lifecycleMonitor) {
							try {
								logger.info("Connecting to WebSocket at " + uri);
								openConnection();
								logger.info("Successfully connected");
							}
							catch (Throwable ex) {
								logger.error("Failed to connect", ex);
							}
						}
					}
				});
			}
		}
	}
{
		synchronized (this.lifecycleMonitor) {
			if (isRunning()) {
				try {
					closeConnection();
				}
				catch (Throwable e) {
					logger.error("Failed to stop WebSocket connection", e);
				}
			}
		}
	}
{
		Object valueToUse = attributeValue;
		try {
			Class<?> enumType = this.classLoader.loadClass(Type.getType(asmTypeDescriptor).getClassName());
			Field enumConstant = ReflectionUtils.findField(enumType, attributeValue);
			if (enumConstant != null) {
				valueToUse = enumConstant.get(null);
			}
		}
		catch (ClassNotFoundException ex) {
			this.logger.debug("Failed to classload enum type while reading annotation metadata", ex);
		}
		catch (IllegalAccessException ex) {
			this.logger.warn("Could not access enum value while reading annotation metadata", ex);
		}
		this.attributes.put(attributeName, valueToUse);
	}
{

		URI uri = UriComponentsBuilder.fromUriString(uriTemplate).buildAndExpand(uriVariables).encode().toUri();
		return doHandshake(handler, null, uri);
	}
{
		if (!isClosed()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Closing " + this + ", " + status);
			}
			try {
				closeInternal(status);
			}
			finally {
				this.state = State.CLOSED;
				try {
					this.handler.afterConnectionClosed(status, this);
				}
				finally {
					this.handlerProvider.destroy(this.handler);
				}
			}
		}
	}
{

		String[] messages = null;
		try {
			messages = readMessages(request);
		}
		catch (JsonMappingException ex) {
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().write("Payload expected.".getBytes("UTF-8"));
			return;
		}
		catch (IOException ex) {
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().write("Broken JSON encoding.".getBytes("UTF-8"));
			return;
		}

		if (logger.isTraceEnabled()) {
			logger.trace("Received messages: " + Arrays.asList(messages));
		}

		session.delegateMessages(messages);

		response.setStatusCode(getResponseStatus());
		response.getHeaders().setContentType(new MediaType("text", "plain", Charset.forName("UTF-8")));
	}
{

		logger.debug("Opening " + getTransportType() + " connection");
		session.setFrameFormat(getFrameFormat(request));
		session.writeFrame(response, SockJsFrame.openFrame());
		session.delegateConnectionEstablished();
	}
{
		frame = this.frameFormat.format(frame);
		if (logger.isTraceEnabled()) {
			logger.trace("Writing " + frame);
		}
		response.getBody().write(frame.getContentBytes());
	}
{
			Assert.state(this.session != null, "WebSocket not open");
			try {
				CloseStatus closeStatus = new CloseStatus(statusCode, reason);
				if (logger.isDebugEnabled()) {
					logger.debug("Connection closed, WebSocket session id="
							+ this.session.getId() + ", " + closeStatus);
				}
				this.handler.afterConnectionClosed(closeStatus, this.session);
			}
			catch (Exception ex) {
				onWebSocketError(ex);
			}
			finally {
				try {
					if (this.handler != null) {
						this.provider.destroy(this.handler);
					}
				}
				finally {
					this.session = null;
					this.handler = null;
				}
			}
		}
{
		if (this.bytes != null) {
			return this.bytes;
		}
		else if (getPayload() != null){
			byte[] result = new byte[getPayload().remaining()];
			getPayload().get(result);
			return result;
		}
		else {
			return null;
		}
	}
{
		return doHandshake(handler, null, uri);
	}
{

		// recursively process any member (nested) classes first
		for (String memberClassName : metadata.getMemberClassNames()) {
			MetadataReader reader = this.metadataReaderFactory.getMetadataReader(memberClassName);
			AnnotationMetadata memberClassMetadata = reader.getAnnotationMetadata();
			if (ConfigurationClassUtils.isConfigurationCandidate(memberClassMetadata)) {
				processConfigurationClass(new ConfigurationClass(reader, true));
			}
		}

		// process any @PropertySource annotations
		AnnotationAttributes propertySource =
				attributesFor(metadata, org.springframework.context.annotation.PropertySource.class);
		if (propertySource != null) {
			String name = propertySource.getString("name");
			String[] locations = propertySource.getStringArray("value");
			int nLocations = locations.length;
			if (nLocations == 0) {
				throw new IllegalArgumentException("At least one @PropertySource(value) location is required");
			}
			for (int i = 0; i < nLocations; i++) {
				locations[i] = this.environment.resolveRequiredPlaceholders(locations[i]);
			}
			ClassLoader classLoader = this.resourceLoader.getClassLoader();
			if (!StringUtils.hasText(name)) {
				for (String location : locations) {
					this.propertySources.push(new ResourcePropertySource(location, classLoader));
				}
			}
			else {
				if (nLocations == 1) {
					this.propertySources.push(new ResourcePropertySource(name, locations[0], classLoader));
				}
				else {
					CompositePropertySource ps = new CompositePropertySource(name);
					for (String location : locations) {
						ps.addPropertySource(new ResourcePropertySource(location, classLoader));
					}
					this.propertySources.push(ps);
				}
			}
		}

		// process any @ComponentScan annotions
		AnnotationAttributes componentScan = attributesFor(metadata, ComponentScan.class);
		if (componentScan != null) {
			// the config class is annotated with @ComponentScan -> perform the scan immediately
			Set<BeanDefinitionHolder> scannedBeanDefinitions =
					this.componentScanParser.parse(componentScan, metadata.getClassName());

			// check the set of scanned definitions for any further config classes and parse recursively if necessary
			for (BeanDefinitionHolder holder : scannedBeanDefinitions) {
				if (ConfigurationClassUtils.checkConfigurationClassCandidate(holder.getBeanDefinition(), this.metadataReaderFactory)) {
					this.parse(holder.getBeanDefinition().getBeanClassName(), holder.getBeanName());
				}
			}
		}

		// process any @Import annotations
		Set<String> imports = getImports(metadata.getClassName(), null, new HashSet<String>());
		if (!CollectionUtils.isEmpty(imports)) {
			processImport(configClass, imports.toArray(new String[imports.size()]), true);
		}

		// process any @ImportResource annotations
		if (metadata.isAnnotated(ImportResource.class.getName())) {
			AnnotationAttributes importResource = attributesFor(metadata, ImportResource.class);
			String[] resources = importResource.getStringArray("value");
			Class<? extends BeanDefinitionReader> readerClass = importResource.getClass("reader");
			for (String resource : resources) {
				configClass.addImportedResource(resource, readerClass);
			}
		}

		// process individual @Bean methods
		Set<MethodMetadata> beanMethods = metadata.getAnnotatedMethods(Bean.class.getName());
		for (MethodMetadata methodMetadata : beanMethods) {
			configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
		}

		// process superclass, if any
		if (metadata.hasSuperClass()) {
			String superclass = metadata.getSuperClassName();
			if (this.knownSuperclasses.add(superclass)) {
				// superclass found, return its annotation metadata and recurse
				if (metadata instanceof StandardAnnotationMetadata) {
					Class<?> clazz = ((StandardAnnotationMetadata) metadata).getIntrospectedClass();
					return new StandardAnnotationMetadata(clazz.getSuperclass(), true);
				}
				else if (superclass.startsWith("java")) {
					// never load core JDK classes via ASM, in particular not java.lang.Object!
					try {
						return new StandardAnnotationMetadata(
								this.resourceLoader.getClassLoader().loadClass(superclass), true);
					}
					catch (ClassNotFoundException ex) {
						throw new IllegalStateException(ex);
					}
				}
				else {
					MetadataReader reader = this.metadataReaderFactory.getMetadataReader(superclass);
					return reader.getAnnotationMetadata();
				}
			}
		}

		// no superclass, processing is complete
		return null;
	}
{

		// recursively process any member (nested) classes first
		for (String memberClassName : metadata.getMemberClassNames()) {
			MetadataReader reader = this.metadataReaderFactory.getMetadataReader(memberClassName);
			AnnotationMetadata memberClassMetadata = reader.getAnnotationMetadata();
			if (ConfigurationClassUtils.isConfigurationCandidate(memberClassMetadata)) {
				processConfigurationClass(new ConfigurationClass(reader, true));
			}
		}

		// process any @PropertySource annotations
		AnnotationAttributes propertySource =
				attributesFor(metadata, org.springframework.context.annotation.PropertySource.class);
		if (propertySource != null) {
			String name = propertySource.getString("name");
			String[] locations = propertySource.getStringArray("value");
			int nLocations = locations.length;
			if (nLocations == 0) {
				throw new IllegalArgumentException("At least one @PropertySource(value) location is required");
			}
			for (int i = 0; i < nLocations; i++) {
				locations[i] = this.environment.resolveRequiredPlaceholders(locations[i]);
			}
			ClassLoader classLoader = this.resourceLoader.getClassLoader();
			if (!StringUtils.hasText(name)) {
				for (String location : locations) {
					this.propertySources.push(new ResourcePropertySource(location, classLoader));
				}
			}
			else {
				if (nLocations == 1) {
					this.propertySources.push(new ResourcePropertySource(name, locations[0], classLoader));
				}
				else {
					CompositePropertySource ps = new CompositePropertySource(name);
					for (String location : locations) {
						ps.addPropertySource(new ResourcePropertySource(location, classLoader));
					}
					this.propertySources.push(ps);
				}
			}
		}

		// process any @ComponentScan annotions
		AnnotationAttributes componentScan = attributesFor(metadata, ComponentScan.class);
		if (componentScan != null) {
			// the config class is annotated with @ComponentScan -> perform the scan immediately
			Set<BeanDefinitionHolder> scannedBeanDefinitions =
					this.componentScanParser.parse(componentScan, metadata.getClassName());

			// check the set of scanned definitions for any further config classes and parse recursively if necessary
			for (BeanDefinitionHolder holder : scannedBeanDefinitions) {
				if (ConfigurationClassUtils.checkConfigurationClassCandidate(holder.getBeanDefinition(), this.metadataReaderFactory)) {
					this.parse(holder.getBeanDefinition().getBeanClassName(), holder.getBeanName());
				}
			}
		}

		// process any @Import annotations
		Set<String> imports = getImports(metadata.getClassName(), null, new HashSet<String>());
		if (!CollectionUtils.isEmpty(imports)) {
			processImport(configClass, imports.toArray(new String[imports.size()]), true);
		}

		// process any @ImportResource annotations
		if (metadata.isAnnotated(ImportResource.class.getName())) {
			AnnotationAttributes importResource = attributesFor(metadata, ImportResource.class);
			String[] resources = importResource.getStringArray("value");
			Class<? extends BeanDefinitionReader> readerClass = importResource.getClass("reader");
			for (String resource : resources) {
				configClass.addImportedResource(resource, readerClass);
			}
		}

		// process individual @Bean methods
		Set<MethodMetadata> beanMethods = metadata.getAnnotatedMethods(Bean.class.getName());
		for (MethodMetadata methodMetadata : beanMethods) {
			configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
		}

		// process superclass, if any
		if (metadata.hasSuperClass()) {
			String superclass = metadata.getSuperClassName();
			if (this.knownSuperclasses.add(superclass)) {
				// superclass found, return its annotation metadata and recurse
				if (metadata instanceof StandardAnnotationMetadata) {
					Class<?> clazz = ((StandardAnnotationMetadata) metadata).getIntrospectedClass();
					return new StandardAnnotationMetadata(clazz.getSuperclass(), true);
				}
				else if (superclass.startsWith("java")) {
					// never load core JDK classes via ASM, in particular not java.lang.Object!
					try {
						return new StandardAnnotationMetadata(
								this.resourceLoader.getClassLoader().loadClass(superclass), true);
					}
					catch (ClassNotFoundException ex) {
						throw new IllegalStateException(ex);
					}
				}
				else {
					MetadataReader reader = this.metadataReaderFactory.getMetadataReader(superclass);
					return reader.getAnnotationMetadata();
				}
			}
		}

		// no superclass, processing is complete
		return null;
	}
{

		AnnotationAttributes raw = this.attributeMap.get(annotationType);
		return convertClassValues(raw, classValuesAsString, nestedAttributesAsMap);
	}
{
		for (TransportHandler h : this.transportHandlers.values()) {
			if (h instanceof ConfigurableTransportHandler) {
				((ConfigurableTransportHandler) h).setSockJsConfiguration(this);
				if (!this.sockJsHandlers.isEmpty()) {
					((ConfigurableTransportHandler) h).registerSockJsHandlers(this.sockJsHandlers.keySet());
					if (h instanceof HandshakeHandler) {
						((HandshakeHandler) h).registerWebSocketHandlers(this.sockJsHandlers.values());
					}
				}
			}
		}
	}
{
		this.state = State.CLOSED;
		this.sockJsHandler.sessionClosed(this);
	}
{

		String[] messages = null;
		try {
			messages = readMessages(request);
		}
		catch (JsonMappingException ex) {
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().write("Payload expected.".getBytes("UTF-8"));
			return;
		}
		catch (IOException ex) {
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().write("Broken JSON encoding.".getBytes("UTF-8"));
			return;
		}

		if (logger.isTraceEnabled()) {
			logger.trace("Received messages: " + Arrays.asList(messages));
		}

		session.delegateMessages(messages);

		response.setStatusCode(getResponseStatus());
		response.getHeaders().setContentType(new MediaType("text", "plain", Charset.forName("UTF-8")));
	}
{

		logger.debug("Starting handshake for " + request.getURI());

		if (!HttpMethod.GET.equals(request.getMethod())) {
			response.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
			response.getHeaders().setAllow(Collections.singleton(HttpMethod.GET));
			logger.debug("Only HTTP GET is allowed, current method is " + request.getMethod());
			return false;
		}
		if (!validateUpgradeHeader(request, response)) {
			return false;
		}
		if (!validateConnectHeader(request, response)) {
			return false;
		}
		if (!validateWebSocketVersion(request, response)) {
			return false;
		}
		if (!validateOrigin(request, response)) {
			return false;
		}
		String wsKey = request.getHeaders().getSecWebSocketKey();
		if (wsKey == null) {
			logger.debug("Missing \"Sec-WebSocket-Key\" header");
			response.setStatusCode(HttpStatus.BAD_REQUEST);
			return false;
		}
		String protocol = selectProtocol(request.getHeaders().getSecWebSocketProtocol());
		// TODO: request.getHeaders().getSecWebSocketExtensions())

		response.setStatusCode(HttpStatus.SWITCHING_PROTOCOLS);
		response.getHeaders().setUpgrade("WebSocket");
		response.getHeaders().setConnection("Upgrade");
		response.getHeaders().setSecWebSocketProtocol(protocol);
		response.getHeaders().setSecWebSocketAccept(getWebSocketKeyHash(wsKey));
		// TODO: response.getHeaders().setSecWebSocketExtensions(extensions);

		logger.debug("Successfully negotiated WebSocket handshake");

		// TODO: surely there is a better way to flush the headers
		response.getBody();

		doHandshakeInternal(request, response, protocol);

		return true;
	}
{
		super(sessionId, delegate);
		Assert.notNull(sockJsConfig, "sockJsConfig is required");
		this.sockJsConfig = sockJsConfig;
	}
{

		AbstractHttpServerSession httpServerSession = (AbstractHttpServerSession) session;

		// Set content type before writing
		response.getHeaders().setContentType(getContentType());

		if (httpServerSession.isNew()) {
			handleNewSession(request, response, httpServerSession);
		}
		else if (httpServerSession.isActive()) {
			logger.debug("another " + getTransportType() + " connection still open: " + httpServerSession);
			httpServerSession.writeFrame(response.getBody(), SockJsFrame.closeFrameAnotherConnectionOpen());
		}
		else {
			logger.debug("starting " + getTransportType() + " async request");
			httpServerSession.setCurrentRequest(request, response, getFrameFormat(request));
		}
	}
{
		StringBuilder builder = new StringBuilder();
		for (Iterator<String> iterator = ifNoneMatchList.iterator(); iterator.hasNext();) {
			String ifNoneMatch = iterator.next();
			builder.append(ifNoneMatch);
			if (iterator.hasNext()) {
				builder.append(", ");
			}
		}
		set(IF_NONE_MATCH, builder.toString());
	}
{
		List<String> result = new ArrayList<String>();

		String value = getFirst(IF_NONE_MATCH);
		if (value != null) {
			String[] tokens = value.split(",\\s*");
			for (String token : tokens) {
				result.add(token);
			}
		}
		return result;
	}
{

		Assert.notNull(emf, "No EntityManagerFactory specified");

		EntityManagerHolder emHolder =
				(EntityManagerHolder) TransactionSynchronizationManager.getResource(emf);
		if (emHolder != null) {
			if (!emHolder.isSynchronizedWithTransaction() &&
					TransactionSynchronizationManager.isSynchronizationActive()) {
				// Try to explicitly synchronize the EntityManager itself
				// with an ongoing JTA transaction, if any.
				try {
					emHolder.getEntityManager().joinTransaction();
				}
				catch (TransactionRequiredException ex) {
					logger.debug("Could not join JTA transaction because none was active", ex);
				}
				Object transactionData = prepareTransaction(emHolder.getEntityManager(), emf);
				TransactionSynchronizationManager.registerSynchronization(
						new EntityManagerSynchronization(emHolder, emf, transactionData, false));
				emHolder.setSynchronizedWithTransaction(true);
			}
			return emHolder.getEntityManager();
		}

		if (!TransactionSynchronizationManager.isSynchronizationActive()) {
			// Indicate that we can't obtain a transactional EntityManager.
			return null;
		}

		// Create a new EntityManager for use within the current transaction.
		logger.debug("Opening JPA EntityManager");
		EntityManager em =
				(!CollectionUtils.isEmpty(properties) ? emf.createEntityManager(properties) : emf.createEntityManager());

		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			logger.debug("Registering transaction synchronization for JPA EntityManager");
			// Use same EntityManager for further JPA actions within the transaction.
			// Thread object will get removed by synchronization at transaction completion.
			emHolder = new EntityManagerHolder(em);
			Object transactionData = prepareTransaction(em, emf);
			TransactionSynchronizationManager.registerSynchronization(
					new EntityManagerSynchronization(emHolder, emf, transactionData, true));
			emHolder.setSynchronizedWithTransaction(true);
			TransactionSynchronizationManager.bindResource(emf, emHolder);
		}

		return em;
	}
{
		Assert.notNull(emf, "EntityManagerFactory must not be null");
		if (emf instanceof EntityManagerFactoryInfo) {
			EntityManagerFactoryInfo emfInfo = (EntityManagerFactoryInfo) emf;
			EntityManagerFactory nativeEmf = emfInfo.getNativeEntityManagerFactory();
			EntityManager rawEntityManager = (!CollectionUtils.isEmpty(properties) ?
					nativeEmf.createEntityManager(properties) : nativeEmf.createEntityManager());
			return createProxy(rawEntityManager, emfInfo, true);
		}
		else {
			EntityManager rawEntityManager = (!CollectionUtils.isEmpty(properties) ?
					emf.createEntityManager(properties) : emf.createEntityManager());
			return createProxy(rawEntityManager, null, null, null, null, null, true);
		}
	}
{
		Class[] emIfcs;
		if (emf instanceof EntityManagerFactoryInfo) {
			EntityManagerFactoryInfo emfInfo = (EntityManagerFactoryInfo) emf;
			Class emIfc = emfInfo.getEntityManagerInterface();
			if (emIfc == null) {
				emIfc = EntityManager.class;
			}
			JpaDialect jpaDialect = emfInfo.getJpaDialect();
			if (jpaDialect != null && jpaDialect.supportsEntityManagerPlusOperations()) {
				emIfcs = new Class[] {emIfc, EntityManagerPlus.class};
			}
			else {
				emIfcs = new Class[] {emIfc};
			}
		}
		else {
			emIfcs = new Class[] {EntityManager.class};
		}
		return createSharedEntityManager(emf, properties, emIfcs);
	}
{

		ClassLoader cl = null;
		if (emf instanceof EntityManagerFactoryInfo) {
			cl = ((EntityManagerFactoryInfo) emf).getBeanClassLoader();
		}
		Class[] ifcs = new Class[entityManagerInterfaces.length + 1];
		System.arraycopy(entityManagerInterfaces, 0, ifcs, 0, entityManagerInterfaces.length);
		ifcs[entityManagerInterfaces.length] = EntityManagerProxy.class;
		return (EntityManager) Proxy.newProxyInstance(
				(cl != null ? cl : SharedEntityManagerCreator.class.getClassLoader()),
				ifcs, new SharedEntityManagerInvocationHandler(emf, properties));
	}
{
		return this.disabled;
	}
{
		return this.readonly;
	}
{
		return this.disabled;
	}
{
		return this.disabled;
	}
{

		long startTime = System.currentTimeMillis();
		Throwable failureCause = null;

		LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
		LocaleContext localeContext = buildLocaleContext(request);

		RequestAttributes previousAttributes = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes requestAttributes = null;
		if (previousAttributes == null || (previousAttributes instanceof ServletRequestAttributes)) {
			requestAttributes = new ServletRequestAttributes(request);
		}

		initContextHolders(request, localeContext, requestAttributes);

		WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
		asyncManager.registerCallableInterceptor(FrameworkServlet.class.getName(), getRequestBindingInterceptor(request));

		try {
			doService(request, response);
		}
		catch (ServletException ex) {
			failureCause = ex;
			throw ex;
		}
		catch (IOException ex) {
			failureCause = ex;
			throw ex;
		}
		catch (Throwable ex) {
			failureCause = ex;
			throw new NestedServletException("Request processing failed", ex);
		}

		finally {
			resetContextHolders(request, previousLocaleContext, previousAttributes);
			if (requestAttributes != null) {
				requestAttributes.requestCompleted();
			}

			if (logger.isDebugEnabled()) {
				if (failureCause != null) {
					this.logger.debug("Could not complete request", failureCause);
				} else {
					if (asyncManager.isConcurrentHandlingStarted()) {
						if (logger.isDebugEnabled()) {
							logger.debug("Leaving response open for concurrent processing");
						}
					}
					else {
						this.logger.debug("Successfully completed request");
					}
				}
			}
			if (this.publishEvents) {
				// Whether or not we succeeded, publish an event.
				long processingTime = System.currentTimeMillis() - startTime;
				this.webApplicationContext.publishEvent(
						new ServletRequestHandledEvent(this,
								request.getRequestURI(), request.getRemoteAddr(),
								request.getMethod(), getServletConfig().getServletName(),
								WebUtils.getSessionId(request), getUsernameForRequest(request),
								processingTime, failureCause));
			}
		}
	}
{
		Assert.notNull(testClass, "Class must not be null");

		final List<ContextConfigurationAttributes> attributesList = new ArrayList<ContextConfigurationAttributes>();

		Class<ContextConfiguration> annotationType = ContextConfiguration.class;
		Class<?> declaringClass = findAnnotationDeclaringClass(annotationType, testClass);
		Assert.notNull(declaringClass, String.format(
			"Could not find an 'annotation declaring class' for annotation type [%s] and class [%s]",
			annotationType.getName(), testClass.getName()));

		while (declaringClass != null) {
			ContextConfiguration contextConfiguration = declaringClass.getAnnotation(annotationType);
			if (logger.isTraceEnabled()) {
				logger.trace(String.format("Retrieved @ContextConfiguration [%s] for declaring class [%s].",
					contextConfiguration, declaringClass.getName()));
			}

			ContextConfigurationAttributes attributes = new ContextConfigurationAttributes(declaringClass,
				contextConfiguration);
			if (logger.isTraceEnabled()) {
				logger.trace("Resolved context configuration attributes: " + attributes);
			}

			attributesList.add(attributes);

			declaringClass = findAnnotationDeclaringClass(annotationType, declaringClass.getSuperclass());
		}

		return attributesList;
	}
{
		// Remove the merged bean definition for the given bean, if already created.
		clearMergedBeanDefinition(beanName);

		// Remove corresponding bean from singleton cache, if any. Shouldn't usually
		// be necessary, rather just meant for overriding a context's default beans
		// (e.g. the default StaticMessageSource in a StaticApplicationContext).
		destroySingleton(beanName);

		// Remove any assumptions about by-type mappings
		this.singletonBeanNamesByType.clear();
		this.nonSingletonBeanNamesByType.clear();

		// Reset all bean definitions that have the given bean as parent (recursively).
		for (String bdName : this.beanDefinitionNames) {
			if (!beanName.equals(bdName)) {
				BeanDefinition bd = this.beanDefinitionMap.get(bdName);
				if (beanName.equals(bd.getParentName())) {
					resetBeanDefinition(bdName);
				}
			}
		}
	}
{
		DefaultConversionService.addDefaultConverters(conversionService);

		DateFormatterRegistrar registrar = new DateFormatterRegistrar();
		registrar.registerFormatters(conversionService);

		SimpleDateBean bean = new SimpleDateBean();
		bean.getChildren().add(new SimpleDateBean());
		binder = new DataBinder(bean);
		binder.setConversionService(conversionService);

		LocaleContextHolder.setLocale(Locale.US);
	}
{
		return suppressNamespaces;
	}
{

		OutputStream stream = this.updateContentLength ? createTemporaryOutputStream() : response.getOutputStream();

		Object value = filterModel(model);
		JsonGenerator generator = this.objectMapper.getJsonFactory().createJsonGenerator(stream, this.encoding);

		// A workaround for JsonGenerators not applying serialization features
		// https://github.com/FasterXML/jackson-databind/issues/12
		if (this.objectMapper.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
			generator.useDefaultPrettyPrinter();
		}

		if (this.prefixJson) {
			generator.writeRaw("{} && ");
		}
		this.objectMapper.writeValue(generator, value);

		if (this.updateContentLength) {
			writeToResponse(response, (ByteArrayOutputStream) stream);
		}
	}
{

		OutputStream stream = this.updateContentLength ? createTemporaryOutputStream() : response.getOutputStream();

		Object value = filterModel(model);
		JsonGenerator generator = this.objectMapper.getJsonFactory().createJsonGenerator(stream, this.encoding);

		// A workaround for JsonGenerators not applying serialization features
		// https://github.com/FasterXML/jackson-databind/issues/12
		if (this.objectMapper.getSerializationConfig().isEnabled(SerializationConfig.Feature.INDENT_OUTPUT)) {
			generator.useDefaultPrettyPrinter();
		}

		if (this.prefixJson) {
			generator.writeRaw("{} && ");
		}
		this.objectMapper.writeValue(generator, value);

		if (this.updateContentLength) {
			writeToResponse(response, (ByteArrayOutputStream) stream);
		}
	}
{
			if (this.index >= this.collection.size()) {
				if (this.growCollection) {
					growCollection(this.collectionEntryTypeDescriptor, this.index, this.collection);
				}
				else {
					throw new SpelEvaluationException(getStartPosition(), SpelMessage.COLLECTION_INDEX_OUT_OF_BOUNDS,
							this.collection.size(), this.index);
				}
			}
			if (this.collection instanceof List) {
				Object o = ((List) this.collection).get(this.index);
				return new TypedValue(o, this.collectionEntryTypeDescriptor.elementTypeDescriptor(o));
			}
			int pos = 0;
			for (Object o : this.collection) {
				if (pos == this.index) {
					return new TypedValue(o, this.collectionEntryTypeDescriptor.elementTypeDescriptor(o));
				}
				pos++;
			}
			throw new IllegalStateException("Failed to find indexed element " + this.index + ": " + this.collection);
		}
{
		Properties props = new Properties();
		fillProperties(props, resource, new DefaultPropertiesPersister());
		return props;
	}
{
		long now = System.currentTimeMillis();

		if (this.taskScheduler == null) {
			this.localExecutor = Executors.newSingleThreadScheduledExecutor();
			this.taskScheduler = new ConcurrentTaskScheduler(this.localExecutor);
		}
		if (this.triggerTasks != null) {
			for (TriggerTask task : triggerTasks) {
				this.scheduledFutures.add(this.taskScheduler.schedule(
						task.getRunnable(), task.getTrigger()));
			}
		}
		if (this.cronTasks != null) {
			for (CronTask task : cronTasks) {
				this.scheduledFutures.add(this.taskScheduler.schedule(
						task.getRunnable(), task.getTrigger()));
			}
		}
		if (this.fixedRateTasks != null) {
			for (IntervalTask task : fixedRateTasks) {
				if (task.getInitialDelay() > 0) {
					Date startTime = new Date(now + task.getInitialDelay());
					this.scheduledFutures.add(this.taskScheduler.scheduleAtFixedRate(
							task.getRunnable(), startTime, task.getInterval()));
				}
				else {
					this.scheduledFutures.add(this.taskScheduler.scheduleAtFixedRate(
							task.getRunnable(), task.getInterval()));
				}
			}
		}
		if (this.fixedDelayTasks != null) {
			for (IntervalTask task : fixedDelayTasks) {
				if (task.getInitialDelay() > 0) {
					Date startTime = new Date(now + task.getInitialDelay());
					this.scheduledFutures.add(this.taskScheduler.scheduleWithFixedDelay(
							task.getRunnable(), startTime, task.getInterval()));
				}
				else {
					this.scheduledFutures.add(this.taskScheduler.scheduleWithFixedDelay(
							task.getRunnable(), task.getInterval()));
				}
			}
		}
	}
{

		Map<String, T> beansOfType = beansOfTypeIncludingAncestors(lbf, type);
		if (beansOfType.size() == 1) {
			return beansOfType.values().iterator().next();
		}
		else {
			throw new NoSuchBeanDefinitionException(type, "expected single bean but found " + beansOfType.size());
		}
	}
{
		SpelNodeImpl expr = eatLogicalAndExpression();
		while (peekIdentifierToken("or") || peekToken(TokenKind.SYMBOLIC_OR)) {
			Token t = nextToken(); //consume OR
			SpelNodeImpl rhExpr = eatLogicalAndExpression();
			checkRightOperand(t,rhExpr);
			expr = new OpOr(toPos(t),expr,rhExpr);
		}
		return expr;
	}
{
		Method[] methods = clazz.getMethods();
		String setterName = "set" + getPropertyMethodSuffix(propertyName);
		for (Method method : methods) {
			if (!method.isBridge() && method.getName().equals(setterName) && method.getParameterTypes().length == 1 &&
					(!mustBeStatic || Modifier.isStatic(method.getModifiers()))) {
				return method;
			}
		}
		return null;
	}
{
		if (validator != null && (getTarget() != null && !validator.supports(getTarget().getClass()))) {
			throw new IllegalStateException("Invalid target for Validator [" + validator + "]: " + getTarget());
		}
		this.validator = validator;
	}
{
		HandlerMethod handlerMethod;
		if (handler instanceof String) {
			String beanName = (String) handler;
			handlerMethod = new HandlerMethod(beanName, getApplicationContext(), method);
		}
		else {
			handlerMethod = new HandlerMethod(handler, method);
		}

		HandlerMethod oldHandlerMethod = handlerMethods.get(mapping);
		if (oldHandlerMethod != null && !oldHandlerMethod.equals(handlerMethod)) {
			throw new IllegalStateException("Ambiguous mapping found. Cannot map '" + handlerMethod.getBean()
					+ "' bean method \n" + handlerMethod + "\nto " + mapping + ": There is already '"
					+ oldHandlerMethod.getBean() + "' bean method\n" + oldHandlerMethod + " mapped.");
		}

		this.handlerMethods.put(mapping, handlerMethod);
		if (logger.isInfoEnabled()) {
			logger.info("Mapped \"" + mapping + "\" onto " + handlerMethod);
		}

		Set<String> patterns = getMappingPathPatterns(mapping);
		for (String pattern : patterns) {
			if (!getPathMatcher().isPattern(pattern)) {
				this.urlMap.add(pattern, mapping);
			}
		}
	}
{
		String currentStatement = lineNumberReader.readLine();
		StringBuilder scriptBuilder = new StringBuilder();
		while (currentStatement != null) {
			if (StringUtils.hasText(currentStatement)
					&& (commentPrefix != null && !currentStatement.startsWith(commentPrefix))) {
				if (scriptBuilder.length() > 0) {
					scriptBuilder.append('\n');
				}
				scriptBuilder.append(currentStatement);
			}
			currentStatement = lineNumberReader.readLine();
		}
		return scriptBuilder.toString();
	}
{
		StringBuilder sb = new StringBuilder();
		boolean inLiteral = false;
		boolean inEscape = false;
		char[] content = script.toCharArray();
		for (int i = 0; i < script.length(); i++) {
			char c = content[i];
			if (inEscape) {
				inEscape = false;
				sb.append(c);
				continue;
			}
			// MySQL style escapes
			if (c == '\\') {
				inEscape = true;
				sb.append(c);
				continue;
			}
			if (c == '\'') {
				inLiteral = !inLiteral;
			}
			if (!inLiteral) {
				if (script.startsWith(delim, i)) {
					// we've reached the end of the current statement
					if (sb.length() > 0) {
						statements.add(sb.toString());
						sb = new StringBuilder();
					}
					i += delim.length() - 1;
					continue;
				}
				else if (script.startsWith(commentPrefix, i)) {
					// skip over any content from the start of the comment to the EOL
					int indexOfNextNewline = script.indexOf("\n", i);
					if (indexOfNextNewline > i) {
						i = indexOfNextNewline;
						continue;
					}
					else {
						// if there's no newline after the comment, we must be at the end
						// of the script, so stop here.
						break;
					}
				}
				else if (c == ' ' || c == '\n' || c == '\t') {
					// avoid multiple adjacent whitespace characters
					if (sb.length() > 0 && sb.charAt(sb.length() - 1) != ' ') {
						c = ' ';
					}
					else {
						continue;
					}
				}
			}
			sb.append(c);
		}
		if (StringUtils.hasText(sb)) {
			statements.add(sb.toString());
		}
	}
{
		StringBuilder builder = new StringBuilder();
		builder.append(beanDefinition.getBeanClassName());
		builder.append(" ['");
		builder.append(beanName);
		builder.append('\'');
		String resourceDescription = beanDefinition.getResourceDescription();
		if (StringUtils.hasLength(resourceDescription)) {
			builder.append(" in ");
			builder.append(resourceDescription);
		}
		builder.append(" ] has been deprecated");
		logger.warn(builder.toString());
	}
{
		Assert.notNull(propertyName, "propertyName may not be null");
		propertyName = pd == null ? propertyName : pd.getName();
		for (PropertyDescriptor existingPD : this.propertyDescriptors) {
			if (existingPD.getName().equals(propertyName)) {
				// is there already a descriptor that captures this read method or its corresponding write method?
				if (readMethodFor(existingPD) != null) {
					if (readMethod != null && readMethodFor(existingPD).getReturnType() != readMethod.getReturnType()
							|| writeMethod != null && readMethodFor(existingPD).getReturnType() != writeMethod.getParameterTypes()[0]) {
						// no -> add a new descriptor for it below
						break;
					}
				}
				// update the existing descriptor's read method
				if (readMethod != null) {
					try {
						existingPD.setReadMethod(readMethod);
					} catch (IntrospectionException ex) {
						// there is a conflicting setter method present -> null it out and try again
						existingPD.setWriteMethod(null);
						existingPD.setReadMethod(readMethod);
					}
				}

				// is there already a descriptor that captures this write method or its corresponding read method?
				if (writeMethodFor(existingPD) != null) {
					if (readMethod != null && writeMethodFor(existingPD).getParameterTypes()[0] != readMethod.getReturnType()
							|| writeMethod != null && writeMethodFor(existingPD).getParameterTypes()[0] != writeMethod.getParameterTypes()[0]) {
						// no -> add a new descriptor for it below
						break;
					}
				}
				// update the existing descriptor's write method
				if (writeMethod != null
						&& !(existingPD instanceof IndexedPropertyDescriptor &&
								!writeMethod.getParameterTypes()[0].isArray())) {
					existingPD.setWriteMethod(writeMethod);
				}

				// is this descriptor indexed?
				if (existingPD instanceof IndexedPropertyDescriptor) {
					IndexedPropertyDescriptor existingIPD = (IndexedPropertyDescriptor) existingPD;

					// is there already a descriptor that captures this indexed read method or its corresponding indexed write method?
					if (indexedReadMethodFor(existingIPD) != null) {
						if (indexedReadMethod != null && indexedReadMethodFor(existingIPD).getReturnType() != indexedReadMethod.getReturnType()
								|| indexedWriteMethod != null && indexedReadMethodFor(existingIPD).getReturnType() != indexedWriteMethod.getParameterTypes()[1]) {
							// no -> add a new descriptor for it below
							break;
						}
					}
					// update the existing descriptor's indexed read method
					try {
						if (indexedReadMethod != null) {
							existingIPD.setIndexedReadMethod(indexedReadMethod);
						}
					} catch (IntrospectionException ex) {
						// there is a conflicting indexed setter method present -> null it out and try again
						existingIPD.setIndexedWriteMethod(null);
						existingIPD.setIndexedReadMethod(indexedReadMethod);
					}

					// is there already a descriptor that captures this indexed write method or its corresponding indexed read method?
					if (indexedWriteMethodFor(existingIPD) != null) {
						if (indexedReadMethod != null && indexedWriteMethodFor(existingIPD).getParameterTypes()[1] != indexedReadMethod.getReturnType()
								|| indexedWriteMethod != null && indexedWriteMethodFor(existingIPD).getParameterTypes()[1] != indexedWriteMethod.getParameterTypes()[1]) {
							// no -> add a new descriptor for it below
							break;
						}
					}
					// update the existing descriptor's indexed write method
					if (indexedWriteMethod != null) {
						existingIPD.setIndexedWriteMethod(indexedWriteMethod);
					}
				}

				// the descriptor has been updated -> return immediately
				return;
			}
		}

		// we haven't yet seen read or write methods for this property -> add a new descriptor
		if (pd == null) {
			try {
				if (indexedReadMethod == null && indexedWriteMethod == null) {
					pd = new PropertyDescriptor(propertyName, readMethod, writeMethod);
				}
				else {
					pd = new IndexedPropertyDescriptor(propertyName, readMethod, writeMethod, indexedReadMethod, indexedWriteMethod);
				}
				this.propertyDescriptors.add(pd);
			} catch (IntrospectionException ex) {
				logger.debug(format("Could not create new PropertyDescriptor for readMethod [%s] writeMethod [%s] " +
						"indexedReadMethod [%s] indexedWriteMethod [%s] for property [%s]. Reason: %s",
						readMethod, writeMethod, indexedReadMethod, indexedWriteMethod, propertyName, ex.getMessage()));
				// suppress exception and attempt to continue
			}
		}
		else {
			pd.setWriteMethod(null);
			pd.setReadMethod(readMethod);
			try {
				pd.setWriteMethod(writeMethod);
			} catch (IntrospectionException ex) {
				logger.debug(format("Could not add write method [%s] for property [%s]. Reason: %s",
						writeMethod, propertyName, ex.getMessage()));
				// fall through -> add property descriptor as best we can
			}
			if (pd instanceof IndexedPropertyDescriptor) {
				((IndexedPropertyDescriptor)pd).setIndexedWriteMethod(null);
				((IndexedPropertyDescriptor)pd).setIndexedReadMethod(indexedReadMethod);
				try {
					((IndexedPropertyDescriptor)pd).setIndexedWriteMethod(indexedWriteMethod);
				} catch (IntrospectionException ex) {
					logger.debug(format("Could not add indexed write method [%s] for property [%s]. Reason: %s",
							indexedWriteMethod, propertyName, ex.getMessage()));
					// fall through -> add property descriptor as best we can
				}
			}
			this.propertyDescriptors.add(pd);
		}
	}
{
		boolean leftValue;
		boolean rightValue;

		try {
			TypedValue typedValue = getLeftOperand().getValueInternal(state);
			this.assertTypedValueNotNull(typedValue);
			leftValue = (Boolean)state.convertValue(typedValue, TypeDescriptor.valueOf(Boolean.class));
		}
		catch (SpelEvaluationException ee) {
			ee.setPosition(getLeftOperand().getStartPosition());
			throw ee;
		}

		if (leftValue == false) {
			return BooleanTypedValue.forValue(false); // no need to evaluate right operand
		}

		try {
			TypedValue typedValue = getRightOperand().getValueInternal(state);
			this.assertTypedValueNotNull(typedValue);
			rightValue = (Boolean)state.convertValue(typedValue, TypeDescriptor.valueOf(Boolean.class));
		}
		catch (SpelEvaluationException ee) {
			ee.setPosition(getRightOperand().getStartPosition());
			throw ee;
		}

		return /* leftValue && */BooleanTypedValue.forValue(rightValue);
	}
{
		boolean leftValue;
		boolean rightValue;
		try {
			TypedValue typedValue = getLeftOperand().getValueInternal(state);
			this.assertTypedValueNotNull(typedValue);
			leftValue = (Boolean)state.convertValue(typedValue, TypeDescriptor.valueOf(Boolean.class));
		}
		catch (SpelEvaluationException see) {
			see.setPosition(getLeftOperand().getStartPosition());
			throw see;
		}

		if (leftValue == true) {
			return BooleanTypedValue.TRUE; // no need to evaluate right operand
		}

		try {
			TypedValue typedValue = getRightOperand().getValueInternal(state);
			this.assertTypedValueNotNull(typedValue);
			rightValue = (Boolean)state.convertValue(typedValue, TypeDescriptor.valueOf(Boolean.class));
		}
		catch (SpelEvaluationException see) {
			see.setPosition(getRightOperand().getStartPosition()); // TODO end positions here and in similar situations
			throw see;
		}

		return BooleanTypedValue.forValue(leftValue || rightValue);
	}
{
		HttpServletRequest request = this.mockRequest;
		if (request.isAsyncStarted()) {
			if (!awaitAsyncResult(request)) {
				throw new IllegalStateException(
						"Gave up waiting on async result from handler [" + this.handler + "] to complete");
			}
		}
		return this.asyncResult;
	}
{
		if (con == null) {
			return;
		}

		if (dataSource != null) {
			ConnectionHolder conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
			if (conHolder != null && connectionEquals(conHolder, con)) {
				// It's the transactional Connection: Don't close it.
				conHolder.released();
				return;
			}
		}

		// Leave the Connection open only if the DataSource is our
		// special SmartDataSoruce and it wants the Connection left open.
		if (!(dataSource instanceof SmartDataSource) || ((SmartDataSource) dataSource).shouldClose(con)) {
			logger.debug("Returning JDBC Connection to DataSource");
			con.close();
		}
	}
{
		factory.setStyle("SS");
		assertThat(applyLocale(factory.getDateTimeFormatter()).print(dateTime), is("10/21/09 12:10 PM"));

		factory.setIso(ISO.DATE);
		assertThat(applyLocale(factory.getDateTimeFormatter()).print(dateTime), is("2009-10-21"));

		factory.setPattern("yyyyMMddHHmmss");
		assertThat(factory.getDateTimeFormatter().print(dateTime), is("20091021121000"));
	}
{
		DefaultConversionService.addDefaultConverters(conversionService);

		JodaTimeFormatterRegistrar registrar = new JodaTimeFormatterRegistrar();
		registrar.registerFormatters(conversionService);

		JodaTimeBean bean = new JodaTimeBean();
		bean.getChildren().add(new JodaTimeBean());
		binder = new DataBinder(bean);
		binder.setConversionService(conversionService);

		LocaleContextHolder.setLocale(Locale.US);
		JodaTimeContext context = new JodaTimeContext();
		context.setTimeZone(DateTimeZone.forID("-05:00"));
		JodaTimeContextHolder.setJodaTimeContext(context);
	}
{
		HttpStatus statusCode = getStatusCode(response);
		HttpHeaders headers = response.getHeaders();
		MediaType contentType = headers.getContentType();
		Charset charset = contentType != null ? contentType.getCharSet() : null;
		byte[] body = getResponseBody(response);
		switch (statusCode.series()) {
			case CLIENT_ERROR:
				throw new HttpClientErrorException(statusCode, response.getStatusText(), headers, body, charset);
			case SERVER_ERROR:
				throw new HttpServerErrorException(statusCode, response.getStatusText(), headers, body, charset);
			default:
				throw new RestClientException("Unknown status code [" + statusCode + "]");
		}
	}
{
		Set<String> excluded =  new HashSet<String>(Arrays.asList(excludedProperties));
		Method[] annotationProperties = ann.annotationType().getDeclaredMethods();
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		for (Method annotationProperty : annotationProperties) {
			String propertyName = annotationProperty.getName();
			if ((!excluded.contains(propertyName)) && bw.isWritableProperty(propertyName)) {
				Object value = ReflectionUtils.invokeMethod(annotationProperty, ann);
				bw.setPropertyValue(propertyName, value);
			}
		}
	}
{
		ConfigurableWebBindingInitializer webBindingInitializer = new ConfigurableWebBindingInitializer();
		webBindingInitializer.setConversionService(mvcConversionService());
		webBindingInitializer.setValidator(mvcValidator());
		webBindingInitializer.setMessageCodesResolver(getMessageCodesResolver());

		List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
		addArgumentResolvers(argumentResolvers);

		List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<HandlerMethodReturnValueHandler>();
		addReturnValueHandlers(returnValueHandlers);

		RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
		adapter.setContentNegotiationManager(mvcContentNegotiationManager());
		adapter.setMessageConverters(getMessageConverters());
		adapter.setWebBindingInitializer(webBindingInitializer);
		adapter.setCustomArgumentResolvers(argumentResolvers);
		adapter.setCustomReturnValueHandlers(returnValueHandlers);

		AsyncSupportConfigurer configurer = new AsyncSupportConfigurer();
		configureAsyncSupport(configurer);

		if (configurer.getTaskExecutor() != null) {
			adapter.setTaskExecutor(configurer.getTaskExecutor());
		}
		if (configurer.getTimeout() != null) {
			adapter.setAsyncRequestTimeout(configurer.getTimeout());
		}
		adapter.setCallableInterceptors(configurer.getCallableInterceptors());
		adapter.setDeferredResultInterceptors(configurer.getDeferredResultInterceptors());

		return adapter;
	}
{

		Callable<Object> task = new StubCallable();

		CallableProcessingInterceptor interceptor = createStrictMock(CallableProcessingInterceptor.class);
		interceptor.preProcess(this.asyncWebRequest, task);
		interceptor.postProcess(this.asyncWebRequest, task, new Integer(1));
		replay(interceptor);

		this.asyncWebRequest.startAsync();
		expect(this.asyncWebRequest.isAsyncComplete()).andReturn(false);
		this.asyncWebRequest.dispatch();
		replay(this.asyncWebRequest);

		this.asyncManager.registerCallableInterceptor("interceptor", interceptor);
		this.asyncManager.startCallableProcessing(task);

		verify(interceptor, this.asyncWebRequest);
	}
{
		TypedValue result = null;
		SpelNodeImpl nextNode = null;
		try {
			nextNode = children[0];
			result = nextNode.getValueInternal(state);
			for (int i = 1; i < getChildCount(); i++) {
				try {
					state.pushActiveContextObject(result);
					nextNode = children[i];
					result = nextNode.getValueInternal(state);
				} finally {
					state.popActiveContextObject();
				}
			}
		} catch (SpelEvaluationException ee) {
			// Correct the position for the error before rethrowing
			ee.setPosition(nextNode.getStartPosition());
			throw ee;
		}
		return result;
	}
{
		TypedValue context = state.getActiveContextObject();
		Object targetObject = context.getValue();
		TypeDescriptor targetObjectTypeDescriptor = context.getTypeDescriptor();
		TypedValue indexValue = null;
		Object index = null;
		
		// This first part of the if clause prevents a 'double dereference' of the property (SPR-5847)
		if (targetObject instanceof Map && (children[0] instanceof PropertyOrFieldReference)) {
			PropertyOrFieldReference reference = (PropertyOrFieldReference)children[0];
			index = reference.getName();
			indexValue = new TypedValue(index);
		}
		else {
			// In case the map key is unqualified, we want it evaluated against the root object so 
			// temporarily push that on whilst evaluating the key
			try {
				state.pushActiveContextObject(state.getRootContextObject());
				indexValue = children[0].getValueInternal(state);
				index = indexValue.getValue();
			}
			finally {
				state.popActiveContextObject();
			}
		}

		// Indexing into a Map
		if (targetObject instanceof Map) {
			Object key = index;
			if (targetObjectTypeDescriptor.getMapKeyTypeDescriptor() != null) {
				key = state.convertValue(key, targetObjectTypeDescriptor.getMapKeyTypeDescriptor());
			}
			Object value = ((Map<?, ?>) targetObject).get(key);
			return new TypedValue(value, targetObjectTypeDescriptor.getMapValueTypeDescriptor(value));
		}
		
		if (targetObject == null) {
			throw new SpelEvaluationException(getStartPosition(),SpelMessage.CANNOT_INDEX_INTO_NULL_VALUE);
		}
		
		// if the object is something that looks indexable by an integer, attempt to treat the index value as a number
		if (targetObject instanceof Collection || targetObject.getClass().isArray() || targetObject instanceof String) {
			int idx = (Integer) state.convertValue(index, TypeDescriptor.valueOf(Integer.class));		
			if (targetObject.getClass().isArray()) {
				Object arrayElement = accessArrayElement(targetObject, idx);
				return new TypedValue(arrayElement, targetObjectTypeDescriptor.elementTypeDescriptor(arrayElement));
			} else if (targetObject instanceof Collection) {
				Collection c = (Collection) targetObject;
				if (idx >= c.size()) {
					if (!growCollection(state, targetObjectTypeDescriptor, idx, c)) {
						throw new SpelEvaluationException(getStartPosition(),SpelMessage.COLLECTION_INDEX_OUT_OF_BOUNDS, c.size(), idx);
					}
				}
				int pos = 0;
				for (Object o : c) {
					if (pos == idx) {
						return new TypedValue(o, targetObjectTypeDescriptor.elementTypeDescriptor(o));
					}
					pos++;
				}
			} else if (targetObject instanceof String) {
				String ctxString = (String) targetObject;
				if (idx >= ctxString.length()) {
					throw new SpelEvaluationException(getStartPosition(),SpelMessage.STRING_INDEX_OUT_OF_BOUNDS, ctxString.length(), idx);
				}
				return new TypedValue(String.valueOf(ctxString.charAt(idx)));
			}
		}
		
		// Try and treat the index value as a property of the context object
		// TODO could call the conversion service to convert the value to a String		
		if (indexValue.getTypeDescriptor().getType()==String.class) {
			Class<?> targetObjectRuntimeClass = getObjectClass(targetObject);
			String name = (String)indexValue.getValue();
			EvaluationContext eContext = state.getEvaluationContext();

			try {
				if (cachedReadName!=null && cachedReadName.equals(name) && cachedReadTargetType!=null && cachedReadTargetType.equals(targetObjectRuntimeClass)) {
					// it is OK to use the cached accessor
					return cachedReadAccessor.read(eContext, targetObject, name);
				}
				
				List<PropertyAccessor> accessorsToTry = AstUtils.getPropertyAccessorsToTry(targetObjectRuntimeClass, state);
		
				if (accessorsToTry != null) {			
					for (PropertyAccessor accessor : accessorsToTry) {
							if (accessor.canRead(eContext, targetObject, name)) {
								if (accessor instanceof ReflectivePropertyAccessor) {
									accessor = ((ReflectivePropertyAccessor)accessor).createOptimalAccessor(eContext, targetObject, name);
								}
								this.cachedReadAccessor = accessor;
								this.cachedReadName = name;
								this.cachedReadTargetType = targetObjectRuntimeClass;
								return accessor.read(eContext, targetObject, name);
							}
					}
				}
			} catch (AccessException e) {
				throw new SpelEvaluationException(getStartPosition(), e, SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, targetObjectTypeDescriptor.toString());
			}
		}
			
		throw new SpelEvaluationException(getStartPosition(),SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, targetObjectTypeDescriptor.toString());
	}
{

		TypedValue context = state.getActiveContextObject();
		Object contextObject = context.getValue();
		EvaluationContext eContext = state.getEvaluationContext();

		List<MethodResolver> mResolvers = eContext.getMethodResolvers();
		if (mResolvers != null) {
			for (MethodResolver methodResolver : mResolvers) {
				try {
					MethodExecutor cEx = methodResolver.resolve(
							state.getEvaluationContext(), contextObject, name, argumentTypes);
					if (cEx != null) {
						return cEx;
					}
				}
				catch (AccessException ex) {
					throw new SpelEvaluationException(getStartPosition(),ex, SpelMessage.PROBLEM_LOCATING_METHOD, name, contextObject.getClass());
				}
			}
		}
		throw new SpelEvaluationException(getStartPosition(),SpelMessage.METHOD_NOT_FOUND, FormatHelper.formatMethodForMessage(name, argumentTypes),
				FormatHelper.formatClassNameForMessage(contextObject instanceof Class ? ((Class<?>) contextObject) : contextObject.getClass()));
	}
{
		TypedValue op = state.getActiveContextObject();

		Object operand = op.getValue();
		boolean operandIsArray = ObjectUtils.isArray(operand);
		// TypeDescriptor operandTypeDescriptor = op.getTypeDescriptor();
		
		// When the input is a map, we push a special context object on the stack
		// before calling the specified operation. This special context object
		// has two fields 'key' and 'value' that refer to the map entries key
		// and value, and they can be referenced in the operation
		// eg. {'a':'y','b':'n'}.!{value=='y'?key:null}" == ['a', null]
		if (operand instanceof Map) {
			Map<?, ?> mapData = (Map<?, ?>) operand;
			List<Object> result = new ArrayList<Object>();
			for (Map.Entry entry : mapData.entrySet()) {
				try {
					state.pushActiveContextObject(new TypedValue(entry));
					result.add(this.children[0].getValueInternal(state).getValue());
				}
				finally {
					state.popActiveContextObject();
				}
			}
			return new TypedValue(result); // TODO unable to build correct type descriptor
		}
		else if (operand instanceof Collection || operandIsArray) {
			Collection<?> data = (operand instanceof Collection ? (Collection<?>) operand :
					Arrays.asList(ObjectUtils.toObjectArray(operand)));
			List<Object> result = new ArrayList<Object>();
			int idx = 0;
			Class<?> arrayElementType = null;
			for (Object element : data) {
				try {
					state.pushActiveContextObject(new TypedValue(element));
					state.enterScope("index", idx);
					Object value = children[0].getValueInternal(state).getValue();
					if (value != null && operandIsArray) {
						arrayElementType = determineCommonType(arrayElementType, value.getClass());
					}
					result.add(value);
				}
				finally {
					state.exitScope();
					state.popActiveContextObject();
				}
				idx++;
			}
			if (operandIsArray) {
				if (arrayElementType == null) {
					arrayElementType = Object.class;
				}
				Object resultArray = Array.newInstance(arrayElementType, result.size());
				System.arraycopy(result.toArray(), 0, resultArray, 0, result.size());
				return new TypedValue(resultArray);
			}
			return new TypedValue(result);
		}
		else {
			if (operand==null) {
				if (this.nullSafe) {
					return TypedValue.NULL;
				}
				else {
					throw new SpelEvaluationException(getStartPosition(),
							SpelMessage.PROJECTION_NOT_SUPPORTED_ON_TYPE, "null");
				}
			}
			else {
				throw new SpelEvaluationException(getStartPosition(),
						SpelMessage.PROJECTION_NOT_SUPPORTED_ON_TYPE, operand.getClass().getName());
			}
		}
	}
{
		TypedValue result = readProperty(state, this.name);
		
		// Dynamically create the objects if the user has requested that optional behaviour
		if (result.getValue() == null && state.getConfiguration().isAutoGrowNullReferences() &&
				nextChildIs(Indexer.class, PropertyOrFieldReference.class)) {
			TypeDescriptor resultDescriptor = result.getTypeDescriptor();
			// Creating lists and maps
			if ((resultDescriptor.getType().equals(List.class) || resultDescriptor.getType().equals(Map.class))) {
				// Create a new collection or map ready for the indexer
				if (resultDescriptor.getType().equals(List.class)) {
					try { 
						if (isWritable(state)) {
							List newList = ArrayList.class.newInstance();
							writeProperty(state, this.name, newList);
							result = readProperty(state, this.name);
						}
					}
					catch (InstantiationException ex) {
						throw new SpelEvaluationException(getStartPosition(), ex,
								SpelMessage.UNABLE_TO_CREATE_LIST_FOR_INDEXING);
					}
					catch (IllegalAccessException ex) {
						throw new SpelEvaluationException(getStartPosition(), ex,
								SpelMessage.UNABLE_TO_CREATE_LIST_FOR_INDEXING);
					}
				}
				else {
					try { 
						if (isWritable(state)) {
							Map newMap = HashMap.class.newInstance();
							writeProperty(state, name, newMap);
							result = readProperty(state, this.name);
						}
					}
					catch (InstantiationException ex) {
						throw new SpelEvaluationException(getStartPosition(), ex,
								SpelMessage.UNABLE_TO_CREATE_MAP_FOR_INDEXING);
					}
					catch (IllegalAccessException ex) {
						throw new SpelEvaluationException(getStartPosition(), ex,
								SpelMessage.UNABLE_TO_CREATE_MAP_FOR_INDEXING);
					}
				}
			}
			else {
				// 'simple' object
				try { 
					if (isWritable(state)) {
						Object newObject  = result.getTypeDescriptor().getType().newInstance();
						writeProperty(state, name, newObject);
						result = readProperty(state, this.name);
					}
				}
				catch (InstantiationException ex) {
					throw new SpelEvaluationException(getStartPosition(), ex,
							SpelMessage.UNABLE_TO_DYNAMICALLY_CREATE_OBJECT, result.getTypeDescriptor().getType());
				}
				catch (IllegalAccessException ex) {
					throw new SpelEvaluationException(getStartPosition(), ex,
							SpelMessage.UNABLE_TO_DYNAMICALLY_CREATE_OBJECT, result.getTypeDescriptor().getType());
				}				
			}
		}
		return result;
	}
{
		TypedValue op = state.getActiveContextObject();
		Object operand = op.getValue();
		
		SpelNodeImpl selectionCriteria = children[0];
		if (operand instanceof Map) {
			Map<?, ?> mapdata = (Map<?, ?>) operand;
			// TODO don't lose generic info for the new map
			Map<Object,Object> result = new HashMap<Object,Object>();
			Object lastKey = null;
			for (Map.Entry entry : mapdata.entrySet()) {
				try {
					TypedValue kvpair = new TypedValue(entry);
					state.pushActiveContextObject(kvpair);
					Object o = selectionCriteria.getValueInternal(state).getValue();
					if (o instanceof Boolean) {
						if (((Boolean) o).booleanValue() == true) {
							if (variant == FIRST) {
								result.put(entry.getKey(),entry.getValue());
								return new TypedValue(result);
							}
							result.put(entry.getKey(),entry.getValue());
							lastKey = entry.getKey();
						}
					} else {
						throw new SpelEvaluationException(selectionCriteria.getStartPosition(),
								SpelMessage.RESULT_OF_SELECTION_CRITERIA_IS_NOT_BOOLEAN);// ,selectionCriteria.stringifyAST());
					}
				} finally {
					state.popActiveContextObject();
				}
			}
			if ((variant == FIRST || variant == LAST) && result.size() == 0) {
				return new TypedValue(null);
			}
			if (variant == LAST) {
				Map resultMap = new HashMap();
				Object lastValue = result.get(lastKey);
				resultMap.put(lastKey,lastValue);
				return new TypedValue(resultMap);
			}
			return new TypedValue(result);
		} else if ((operand instanceof Collection) || ObjectUtils.isArray(operand)) {
			List<Object> data = new ArrayList<Object>();
			Collection<?> c = (operand instanceof Collection) ?
					(Collection<?>) operand : Arrays.asList(ObjectUtils.toObjectArray(operand));
			data.addAll(c);
			List<Object> result = new ArrayList<Object>();
			int idx = 0;
			for (Object element : data) {
				try {
					state.pushActiveContextObject(new TypedValue(element));
					state.enterScope("index", idx);
					Object o = selectionCriteria.getValueInternal(state).getValue();
					if (o instanceof Boolean) {
						if (((Boolean) o).booleanValue() == true) {
							if (variant == FIRST) {
								return new TypedValue(element);
							}
							result.add(element);
						}
					} else {
						throw new SpelEvaluationException(selectionCriteria.getStartPosition(),
								SpelMessage.RESULT_OF_SELECTION_CRITERIA_IS_NOT_BOOLEAN);// ,selectionCriteria.stringifyAST());
					}
					idx++;
				} finally {
					state.exitScope();
					state.popActiveContextObject();
				}
			}
			if ((variant == FIRST || variant == LAST) && result.size() == 0) {
				return TypedValue.NULL;
			}
			if (variant == LAST) {
				return new TypedValue(result.get(result.size() - 1));
			}
			if (operand instanceof Collection) {
				return new TypedValue(result);
			}
			else {
				Class<?> elementType = ClassUtils.resolvePrimitiveIfNecessary(op.getTypeDescriptor().getElementTypeDescriptor().getType());
				Object resultArray = Array.newInstance(elementType, result.size());
				System.arraycopy(result.toArray(), 0, resultArray, 0, result.size());
				return new TypedValue(resultArray);
			}
		} else {
			if (operand==null) {
				if (nullSafe) { 
					return TypedValue.NULL;
				} else {
					throw new SpelEvaluationException(getStartPosition(), SpelMessage.INVALID_TYPE_FOR_SELECTION,
							"null");
				}
			} else {
				throw new SpelEvaluationException(getStartPosition(), SpelMessage.INVALID_TYPE_FOR_SELECTION,
						operand.getClass().getName());
			}				
		}
	}
{
		SpelNodeImpl expr = eatPowerExpression();
		while (peekToken(TokenKind.STAR,TokenKind.DIV,TokenKind.MOD)) {
			Token t = nextToken(); // consume STAR/DIV/MOD
			SpelNodeImpl rhExpr = eatPowerExpression();
			checkRightOperand(t,rhExpr);
			if (t.kind==TokenKind.STAR) {
				expr = new OpMultiply(toPos(t),expr,rhExpr);
			} else if (t.kind==TokenKind.DIV) {
				expr = new OpDivide(toPos(t),expr,rhExpr);
			} else {
				Assert.isTrue(t.kind==TokenKind.MOD);
				expr = new OpModulus(toPos(t),expr,rhExpr);
			}
		}
		return expr;
	}
{

		WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);

		TestCallableInterceptor callableInterceptor = new TestCallableInterceptor();
		asyncManager.registerCallableInterceptor("mock-mvc", callableInterceptor);

		TestDeferredResultInterceptor deferredResultInterceptor = new TestDeferredResultInterceptor();
		asyncManager.registerDeferredResultInterceptor("mock-mvc", deferredResultInterceptor);

		super.service(request, response);

		// TODO: add CountDownLatch to DeferredResultInterceptor and wait in request().asyncResult(..)

		Object handler = getMvcResult(request).getHandler();
		if (asyncManager.isConcurrentHandlingStarted() && !deferredResultInterceptor.wasInvoked) {
			if (!callableInterceptor.await()) {
				throw new ServletException(
						"Gave up waiting on Callable from [" + handler.getClass().getName() + "] to complete");
			}
		}
	}
{
		String currentStatement = lineNumberReader.readLine();
		StringBuilder scriptBuilder = new StringBuilder();
		while (currentStatement != null) {
			if (StringUtils.hasText(currentStatement)) {
				if (scriptBuilder.length() > 0) {
					scriptBuilder.append('\n');
				}
				scriptBuilder.append(currentStatement);
			}
			currentStatement = lineNumberReader.readLine();
		}
		return scriptBuilder.toString();
	}
{
		StringBuilder sb = new StringBuilder();
		boolean inLiteral = false;
		char[] content = script.toCharArray();
		for (int i = 0; i < script.length(); i++) {
			if (content[i] == '\'') {
				inLiteral = !inLiteral;
			}
			if (content[i] == delim && !inLiteral) {
				if (sb.length() > 0) {
					statements.add(sb.toString());
					sb = new StringBuilder();
				}
			}
			else {
				sb.append(content[i]);
			}
		}
		if (sb.length() > 0) {
			statements.add(sb.toString());
		}
	}
{
		if (target == null) {
			return false;
		}
		Class<?> type = (target instanceof Class ? (Class<?>) target : target.getClass());
		if (type.isArray() && name.equals("length")) {
			return true;
		}
		CacheKey cacheKey = new CacheKey(type, name);
		if (this.readerCache.containsKey(cacheKey)) {
			return true;
		}
		Method method = findGetterForProperty(name, type, target instanceof Class);
		if (method != null) {
			// Treat it like a property
			// The readerCache will only contain gettable properties (let's not worry about setters for now)
			Property property = new Property(type, method, null);
			TypeDescriptor typeDescriptor = new TypeDescriptor(property);
			this.readerCache.put(cacheKey, new InvokerPair(method, typeDescriptor));
			this.typeDescriptorCache.put(cacheKey, typeDescriptor);
			return true;
		}
		else {
			Field field = findField(name, type, target instanceof Class);
			if (field != null) {
				TypeDescriptor typeDescriptor = new TypeDescriptor(field);
				this.readerCache.put(cacheKey, new InvokerPair(field,typeDescriptor));
				this.typeDescriptorCache.put(cacheKey, typeDescriptor);
				return true;
			}
		}
		return false;
	}
{
		if (target == null) {
			return false;
		}
		Class<?> type = (target instanceof Class ? (Class<?>) target : target.getClass());
		CacheKey cacheKey = new CacheKey(type, name);
		if (this.writerCache.containsKey(cacheKey)) {
			return true;
		}
		Method method = findSetterForProperty(name, type, target instanceof Class);
		if (method != null) {
			// Treat it like a property
			Property property = new Property(type, null, method);
			TypeDescriptor typeDescriptor = new TypeDescriptor(property);
			this.writerCache.put(cacheKey, method);
			this.typeDescriptorCache.put(cacheKey, typeDescriptor);
			return true;
		}
		else {
			Field field = findField(name, type, target instanceof Class);
			if (field != null) {
				this.writerCache.put(cacheKey, field);
				this.typeDescriptorCache.put(cacheKey, new TypeDescriptor(field));
				return true;
			}
		}
		return false;
	}
{
		if (target == null) {
			return false;
		}
		Class<?> type = (target instanceof Class ? (Class<?>) target : target.getClass());
		if (type.isArray() && name.equals("length")) {
			return true;
		}
		CacheKey cacheKey = new CacheKey(type, name);
		if (this.readerCache.containsKey(cacheKey)) {
			return true;
		}
		Method method = findGetterForProperty(name, type, target instanceof Class);
		if (method != null) {
			// Treat it like a property
			// The readerCache will only contain gettable properties (let's not worry about setters for now)
			Property property = new Property(type, method, null);
			TypeDescriptor typeDescriptor = new TypeDescriptor(property);
			this.readerCache.put(cacheKey, new InvokerPair(method, typeDescriptor));
			this.typeDescriptorCache.put(cacheKey, typeDescriptor);
			return true;
		}
		else {
			Field field = findField(name, type, target instanceof Class);
			if (field != null) {
				TypeDescriptor typeDescriptor = new TypeDescriptor(field);
				this.readerCache.put(cacheKey, new InvokerPair(field,typeDescriptor));
				this.typeDescriptorCache.put(cacheKey, typeDescriptor);
				return true;
			}
		}
		return false;
	}
{
		if (!HTTP_PATCH_AVAILABLE) {
			throw new IllegalArgumentException(
					"HTTP method PATCH not available before Apache HttpComponents HttpClient 4.2");
		}
		else {
			return new HttpPatch(uri);
		}
	}
{

		prepareContext(applicationContext);

		applicationContext.getEnvironment().setActiveProfiles(mergedConfig.getActiveProfiles());

		Set<Class<? extends ApplicationContextInitializer<? extends ConfigurableApplicationContext>>> initializerClasses = mergedConfig.getContextInitializerClasses();

		if (initializerClasses.size() == 0) {
			// no ApplicationContextInitializers have been declared -> nothing to do
			return;
		}

		final List<ApplicationContextInitializer<ConfigurableApplicationContext>> initializerInstances = new ArrayList<ApplicationContextInitializer<ConfigurableApplicationContext>>();
		final Class<?> contextClass = applicationContext.getClass();

		for (Class<? extends ApplicationContextInitializer<? extends ConfigurableApplicationContext>> initializerClass : initializerClasses) {
			Class<?> initializerContextClass = GenericTypeResolver.resolveTypeArgument(initializerClass,
				ApplicationContextInitializer.class);
			Assert.isAssignable(initializerContextClass, contextClass, String.format(
				"Could not add context initializer [%s] since its generic parameter [%s] "
						+ "is not assignable from the type of application context used by this "
						+ "context loader [%s]: ", initializerClass.getName(), initializerContextClass.getName(),
				contextClass.getName()));
			initializerInstances.add((ApplicationContextInitializer<ConfigurableApplicationContext>) BeanUtils.instantiateClass(initializerClass));
		}

		Collections.sort(initializerInstances, new AnnotationAwareOrderComparator());
		for (ApplicationContextInitializer<ConfigurableApplicationContext> initializer : initializerInstances) {
			initializer.initialize(applicationContext);
		}
	}
{
		Assert.notNull(clazz, "Class must not be null");
		String className = clazz.getName();
		int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
		return (lastDotIndex != -1 ? className.substring(0, lastDotIndex) : "");
	}
{

		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		return new ModelAndView();
	}
{
		Assert.notNull(factoryClass, "'factoryClass' must not be null");

		if (classLoader == null) {
			classLoader = ClassUtils.getDefaultClassLoader();
		}
		if (factoriesLocation == null) {
			factoriesLocation = DEFAULT_FACTORIES_LOCATION;
		}

		List<String> factoryNames =
				loadFactoryNames(factoryClass, classLoader, factoriesLocation);

		if (logger.isTraceEnabled()) {
			logger.trace(
					"Loaded [" + factoryClass.getName() + "] names: " + factoryNames);
		}

		List<T> result = new ArrayList<T>(factoryNames.size());
		for (String factoryName : factoryNames) {
			result.add(instantiateFactory(factoryName, factoryClass, classLoader));
		}

		Collections.sort(result, new AnnotationAwareOrderComparator());

		return result;
	}
{
		super(handler, method);

		ResponseStatus annotation = getMethodAnnotation(ResponseStatus.class);
		if (annotation != null) {
			this.responseStatus = annotation.value();
			this.responseReason = annotation.reason();
		}
	}
{
		Set<String> allowedMethods = new HashSet<String>(6);
		Set<MediaType> consumableMediaTypes = new HashSet<MediaType>();
		Set<MediaType> producibleMediaTypes = new HashSet<MediaType>();
		for (RequestMappingInfo info : requestMappingInfos) {
			if (info.getPatternsCondition().getMatchingCondition(request) != null) {
				if (info.getMethodsCondition().getMatchingCondition(request) == null) {
					for (RequestMethod method : info.getMethodsCondition().getMethods()) {
						allowedMethods.add(method.name());
					}
				}
				if (info.getConsumesCondition().getMatchingCondition(request) == null) {
					consumableMediaTypes.addAll(info.getConsumesCondition().getConsumableMediaTypes());
				}
				if (info.getProducesCondition().getMatchingCondition(request) == null) {
					producibleMediaTypes.addAll(info.getProducesCondition().getProducibleMediaTypes());
				}
			}
		}
		if (!allowedMethods.isEmpty()) {
			throw new HttpRequestMethodNotSupportedException(request.getMethod(), allowedMethods);
		}
		else if (!consumableMediaTypes.isEmpty()) {
			MediaType contentType = null;
			if (StringUtils.hasLength(request.getContentType())) {
				contentType = MediaType.parseMediaType(request.getContentType());
			}
			throw new HttpMediaTypeNotSupportedException(contentType, new ArrayList<MediaType>(consumableMediaTypes));
		}
		else if (!producibleMediaTypes.isEmpty()) {
			throw new HttpMediaTypeNotAcceptableException(new ArrayList<MediaType>(producibleMediaTypes));
		}
		else {
			return null;
		}
	}
{
		Set<String> allowedMethods = new HashSet<String>(6);
		Set<MediaType> consumableMediaTypes = new HashSet<MediaType>();
		Set<MediaType> producibleMediaTypes = new HashSet<MediaType>();
		for (RequestMappingInfo info : requestMappingInfos) {
			if (info.getPatternsCondition().getMatchingCondition(request) != null) {
				if (info.getMethodsCondition().getMatchingCondition(request) == null) {
					for (RequestMethod method : info.getMethodsCondition().getMethods()) {
						allowedMethods.add(method.name());
					}
				}
				if (info.getConsumesCondition().getMatchingCondition(request) == null) {
					consumableMediaTypes.addAll(info.getConsumesCondition().getConsumableMediaTypes());
				}
				if (info.getProducesCondition().getMatchingCondition(request) == null) {
					producibleMediaTypes.addAll(info.getProducesCondition().getProducibleMediaTypes());
				}
			}
		}
		if (!allowedMethods.isEmpty()) {
			throw new HttpRequestMethodNotSupportedException(request.getMethod(), allowedMethods);
		}
		else if (!consumableMediaTypes.isEmpty()) {
			MediaType contentType = null;
			if (StringUtils.hasLength(request.getContentType())) {
				contentType = MediaType.parseMediaType(request.getContentType());
			}
			throw new HttpMediaTypeNotSupportedException(contentType, new ArrayList<MediaType>(consumableMediaTypes));
		}
		else if (!producibleMediaTypes.isEmpty()) {
			throw new HttpMediaTypeNotAcceptableException(new ArrayList<MediaType>(producibleMediaTypes));
		}
		else {
			return null;
		}
	}
{
		JavaType javaType = getJavaType(clazz);
		return (this.objectMapper.canDeserialize(javaType) && canRead(mediaType));
	}
{

		JavaType javaType = getJavaType(clazz);
		try {
			return this.objectMapper.readValue(inputMessage.getBody(), javaType);
		}
		catch (IOException ex) {
			throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
		}
	}
{
		JavaType javaType = getJavaType(clazz);
		return (this.objectMapper.canDeserialize(javaType) && canRead(mediaType));
	}
{

		JavaType javaType = getJavaType(clazz);
		try {
			return this.objectMapper.readValue(inputMessage.getBody(), javaType);
		}
		catch (IOException ex) {
			throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
		}
	}
{
		if (!hasMessageBody(response)) {
			return null;
		}
		MediaType contentType = response.getHeaders().getContentType();
		if (contentType == null) {
			if (logger.isTraceEnabled()) {
				logger.trace("No Content-Type header found, defaulting to application/octet-stream");
			}
			contentType = MediaType.APPLICATION_OCTET_STREAM;
		}
		for (HttpMessageConverter messageConverter : messageConverters) {
			if (messageConverter.canRead(responseType, contentType)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Reading [" + responseType.getName() + "] as \"" + contentType
							+"\" using [" + messageConverter + "]");
				}
				return (T) messageConverter.read(this.responseType, response);
			}
		}
		throw new RestClientException(
				"Could not extract response: no suitable HttpMessageConverter found for response type [" +
						this.responseType.getName() + "] and content type [" + contentType + "]");
	}
{
			if (responseType != null) {
				List<MediaType> allSupportedMediaTypes = new ArrayList<MediaType>();
				for (HttpMessageConverter<?> messageConverter : getMessageConverters()) {
					if (messageConverter.canRead(responseType, null)) {
						List<MediaType> supportedMediaTypes = messageConverter.getSupportedMediaTypes();
						for (MediaType supportedMediaType : supportedMediaTypes) {
							if (supportedMediaType.getCharSet() != null) {
								supportedMediaType =
										new MediaType(supportedMediaType.getType(), supportedMediaType.getSubtype());
							}
							allSupportedMediaTypes.add(supportedMediaType);
						}
					}
				}
				if (!allSupportedMediaTypes.isEmpty()) {
					MediaType.sortBySpecificity(allSupportedMediaTypes);
					if (logger.isDebugEnabled()) {
						logger.debug("Setting request Accept header to " + allSupportedMediaTypes);
					}
					request.getHeaders().setAccept(allSupportedMediaTypes);
				}
			}
		}
{

		HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
		assertIsMultipartRequest(servletRequest);
		
		MultipartHttpServletRequest multipartRequest = 
			WebUtils.getNativeRequest(servletRequest, MultipartHttpServletRequest.class);

		String partName = getPartName(parameter);
		Object arg;

		if (MultipartFile.class.equals(parameter.getParameterType())) {
			Assert.notNull(multipartRequest, "Expected MultipartHttpServletRequest: is a MultipartResolver configured?");
			arg = multipartRequest.getFile(partName);
		}
		else if (isMultipartFileCollection(parameter)) {
			Assert.notNull(multipartRequest, "Expected MultipartHttpServletRequest: is a MultipartResolver configured?");
			arg = multipartRequest.getFiles(partName);
		}
		else if ("javax.servlet.http.Part".equals(parameter.getParameterType().getName())) {
			arg = servletRequest.getPart(partName);
		}
		else {
			try {
				HttpInputMessage inputMessage = new RequestPartServletServerHttpRequest(servletRequest, partName);
				arg = readWithMessageConverters(inputMessage, parameter, parameter.getParameterType());
				if (arg != null) {
					Annotation[] annotations = parameter.getParameterAnnotations();
					for (Annotation annot : annotations) {
						if (annot.annotationType().getSimpleName().startsWith("Valid")) {
							WebDataBinder binder = binderFactory.createBinder(request, arg, partName);
							Object hints = AnnotationUtils.getValue(annot);
							binder.validate(hints instanceof Object[] ? (Object[]) hints : new Object[] {hints});
							BindingResult bindingResult = binder.getBindingResult();
							if (bindingResult.hasErrors()) {
								throw new MethodArgumentNotValidException(parameter, bindingResult);
							}
						}
					}
				}
			}
			catch (MissingServletRequestPartException ex) {
				// handled below
				arg = null;
			}
		}

		RequestPart annot = parameter.getParameterAnnotation(RequestPart.class);
		boolean isRequired = (annot == null || annot.required());

		if (arg == null && isRequired) {
			throw new MissingServletRequestPartException(partName);
		}
		
		return arg;
	}
{
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Loading ApplicationContext for merged context configuration [%s].",
				mergedConfig));
		}
		GenericApplicationContext context = new GenericApplicationContext();
		context.getEnvironment().setActiveProfiles(mergedConfig.getActiveProfiles());
		prepareContext(context);
		customizeBeanFactory(context.getDefaultListableBeanFactory());
		loadBeanDefinitions(context, mergedConfig);
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		customizeContext(context);
		context.refresh();
		context.registerShutdownHook();
		return context;
	}
{
		Assert.notNull(mergedConfig, "mergedConfig must not be null");

		List<SmartContextLoader> candidates = Arrays.asList(xmlLoader, annotationConfigLoader);

		for (SmartContextLoader loader : candidates) {
			// Determine if each loader can load a context from the
			// mergedConfig. If it can, let it; otherwise, keep iterating.
			if (supports(loader, mergedConfig)) {
				if (logger.isDebugEnabled()) {
					logger.debug(String.format("Delegating to %s to load context from %s.", name(loader), mergedConfig));
				}
				return loader.loadContext(mergedConfig);
			}
		}

		throw new IllegalStateException(String.format(
			"Neither %s nor %s was able to load an ApplicationContext from %s.", name(xmlLoader),
			name(annotationConfigLoader), mergedConfig));
	}
{
		String participateAttributeName = getParticipateAttributeName();
		Integer count = (Integer) request.getAttribute(participateAttributeName, WebRequest.SCOPE_REQUEST);
		if (count != null) {
			// Do not modify the EntityManager: just clear the marker.
			if (count > 1) {
				request.setAttribute(participateAttributeName, count - 1, WebRequest.SCOPE_REQUEST);
			}
			else {
				request.removeAttribute(participateAttributeName, WebRequest.SCOPE_REQUEST);
			}
		}
		else {
			EntityManagerHolder emHolder = (EntityManagerHolder)
					TransactionSynchronizationManager.unbindResource(getEntityManagerFactory());
			logger.debug("Closing JPA EntityManager in OpenEntityManagerInViewInterceptor");
			EntityManagerFactoryUtils.closeEntityManager(emHolder.getEntityManager());
		}
	}
{
		for (Method method : exceptionHandlerMethods) {
			for (Class<? extends Throwable> exceptionType : detectMappedExceptions(method)) {
				addExceptionMapping(exceptionType, method);
			}
		}
	}
{
		for (Entry<Object, ExceptionHandlerMethodResolver> entry : this.globalExceptionHandlers.entrySet()) {
			Method method = entry.getValue().resolveMethod(exception);
			if (method != null) {
				return new ServletInvocableHandlerMethod(entry.getKey(), method);
			}
		}
		return null;
	}
{
		SessionAttributesHandler sessionAttrHandler = getSessionAttributesHandler(handlerMethod);
		Class<?> handlerType = handlerMethod.getBeanType();
		Set<Method> methods = this.modelFactoryCache.get(handlerType);
		if (methods == null) {
			methods = HandlerMethodSelector.selectMethods(handlerType, MODEL_ATTRIBUTE_METHODS);
			this.modelFactoryCache.put(handlerType, methods);
		}
		List<InvocableHandlerMethod> attrMethods = new ArrayList<InvocableHandlerMethod>();
		for (Method method : methods) {
			InvocableHandlerMethod attrMethod = new InvocableHandlerMethod(handlerMethod.getBean(), method);
			attrMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
			attrMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
			attrMethod.setDataBinderFactory(binderFactory);
			attrMethods.add(attrMethod);
		}
		return new ModelFactory(attrMethods, binderFactory, sessionAttrHandler);
	}
{
		Class<?> handlerType = handlerMethod.getBeanType();
		Set<Method> methods = this.dataBinderFactoryCache.get(handlerType);
		if (methods == null) {
			methods = HandlerMethodSelector.selectMethods(handlerType, INIT_BINDER_METHODS);
			this.dataBinderFactoryCache.put(handlerType, methods);
		}
		List<InvocableHandlerMethod> binderMethods = new ArrayList<InvocableHandlerMethod>();
		for (Method method : methods) {
			InvocableHandlerMethod binderMethod = new InvocableHandlerMethod(handlerMethod.getBean(), method);
			binderMethod.setHandlerMethodArgumentResolvers(this.initBinderArgumentResolvers);
			binderMethod.setDataBinderFactory(new DefaultDataBinderFactory(this.webBindingInitializer));
			binderMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
			binderMethods.add(binderMethod);
		}
		return createDataBinderFactory(binderMethods);
	}
{
		HierarchicalStreamReader streamReader;
		if (node instanceof Document) {
			streamReader = new DomReader((Document) node);
		}
		else if (node instanceof Element) {
			streamReader = new DomReader((Element) node);
		}
		else {
			throw new IllegalArgumentException("DOMSource contains neither Document nor Element");
		}
		try {
			return getXStream().unmarshal(streamReader);
		}
		catch (Exception ex) {
			throw convertXStreamException(ex, false);
		}
	}
{
		final Method testMethod = testContext.getTestMethod();
		Assert.notNull(testMethod, "The test method of the supplied TestContext must not be null");

		if (this.transactionContextCache.remove(testMethod) != null) {
			throw new IllegalStateException("Cannot start new transaction without ending existing transaction: "
					+ "Invoke endTransaction() before startNewTransaction().");
		}

		if (testMethod.isAnnotationPresent(NotTransactional.class)) {
			return;
		}

		TransactionAttribute transactionAttribute = this.attributeSource.getTransactionAttribute(testMethod,
			testContext.getTestClass());
		TransactionDefinition transactionDefinition = null;
		if (transactionAttribute != null) {
			transactionDefinition = new DelegatingTransactionAttribute(transactionAttribute) {

				public String getName() {
					return testMethod.getName();
				}
			};
		}

		if (transactionDefinition != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Explicit transaction definition [" + transactionDefinition + "] found for test context ["
						+ testContext + "]");
			}
			String qualifier = transactionAttribute.getQualifier();
			PlatformTransactionManager tm;
			if (StringUtils.hasLength(qualifier)) {
				// Use autowire-capable factory in order to support extended
				// qualifier matching (only exposed on the internal BeanFactory,
				// not on the ApplicationContext).
				BeanFactory bf = testContext.getApplicationContext().getAutowireCapableBeanFactory();
				tm = BeanFactoryAnnotationUtils.qualifiedBeanOfType(bf, PlatformTransactionManager.class, qualifier);
			}
			else {
				tm = getTransactionManager(testContext);
			}
			TransactionContext txContext = new TransactionContext(tm, transactionDefinition);
			runBeforeTransactionMethods(testContext);
			startNewTransaction(testContext, txContext);
			this.transactionContextCache.put(testMethod, txContext);
		}
	}
{
		CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), parserContext.extractSource(element));
		parserContext.pushContainingComponent(compDefinition);
		
		List<Element> interceptors = DomUtils.getChildElementsByTagName(element, new String[] { "bean", "ref", "interceptor" });
		for (Element interceptor : interceptors) {
			RootBeanDefinition mappedInterceptorDef = new RootBeanDefinition(MappedInterceptor.class);
			mappedInterceptorDef.setSource(parserContext.extractSource(interceptor));
			mappedInterceptorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			
			String[] pathPatterns;
			Object interceptorBean;
			if ("interceptor".equals(interceptor.getLocalName())) {
				List<Element> paths = DomUtils.getChildElementsByTagName(interceptor, "mapping");
				pathPatterns = new String[paths.size()];
				for (int i = 0; i < paths.size(); i++) {
					pathPatterns[i] = paths.get(i).getAttribute("path");
				}
				Element beanElem = DomUtils.getChildElementsByTagName(interceptor, new String[] { "bean", "ref"}).get(0);
				interceptorBean = parserContext.getDelegate().parsePropertySubElement(beanElem, null);
			}
			else {
				pathPatterns = null;
				interceptorBean = parserContext.getDelegate().parsePropertySubElement(interceptor, null);
			}
			mappedInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(0, pathPatterns);
			mappedInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(1, interceptorBean);

			String beanName = parserContext.getReaderContext().registerWithGeneratedName(mappedInterceptorDef);
			parserContext.registerComponent(new BeanComponentDefinition(mappedInterceptorDef, beanName));
		}
		
		parserContext.popAndRegisterContainingComponent();
		return null;
	}
{
		Assert.state(this.asyncWebRequest != null, "An AsyncWebRequest is required to start async processing");
		this.asyncWebRequest.startAsync();
	}
{
		ExceptionHandlerMethodResolver resolver = this.exceptionHandlerMethodResolvers.get(handlerType);
		if (resolver == null) {
			resolver = new ExceptionHandlerMethodResolver(handlerType);
			this.exceptionHandlerMethodResolvers.put(handlerType, resolver);
		}
		return resolver;
	}
{
		try {
			return this.getXStream().unmarshal(streamReader);
		}
		catch (Exception ex) {
			throw convertXStreamException(ex, false);
		}
	}
{
		try {
			return this.getXStream().unmarshal(streamReader);
		}
		catch (Exception ex) {
			throw convertXStreamException(ex, false);
		}
	}
{
		try {
			return this.getXStream().unmarshal(streamReader);
		}
		catch (Exception ex) {
			throw convertXStreamException(ex, false);
		}
	}
{
		List<String> result = new ArrayList<String>();

		// Check all bean definitions.
		String[] beanDefinitionNames = getBeanDefinitionNames();
		for (String beanName : beanDefinitionNames) {
			// Only consider bean as eligible if the bean name
			// is not defined as alias for some other bean.
			if (!isAlias(beanName)) {
				try {
					RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
					// Only check bean definition if it is complete.
					if (!mbd.isAbstract() && (allowEagerInit ||
							((mbd.hasBeanClass() || !mbd.isLazyInit() || this.allowEagerClassLoading)) &&
									!requiresEagerInitForType(mbd.getFactoryBeanName()))) {
						// In case of FactoryBean, match object created by FactoryBean.
						boolean isFactoryBean = isFactoryBean(beanName, mbd);
						boolean matchFound = (allowEagerInit || !isFactoryBean || containsSingleton(beanName)) &&
								(includeNonSingletons || isSingleton(beanName)) && isTypeMatch(beanName, type);
						if (!matchFound && isFactoryBean) {
							// In case of FactoryBean, try to match FactoryBean instance itself next.
							beanName = FACTORY_BEAN_PREFIX + beanName;
							matchFound = (includeNonSingletons || mbd.isSingleton()) && isTypeMatch(beanName, type);
						}
						if (matchFound) {
							result.add(beanName);
						}
					}
				}
				catch (CannotLoadBeanClassException ex) {
					if (allowEagerInit) {
						throw ex;
					}
					// Probably contains a placeholder: let's ignore it for type matching purposes.
					if (this.logger.isDebugEnabled()) {
						this.logger.debug("Ignoring bean class loading failure for bean '" + beanName + "'", ex);
					}
					onSuppressedException(ex);
				}
				catch (BeanDefinitionStoreException ex) {
					if (allowEagerInit) {
						throw ex;
					}
					// Probably contains a placeholder: let's ignore it for type matching purposes.
					if (this.logger.isDebugEnabled()) {
						this.logger.debug("Ignoring unresolvable metadata in bean definition '" + beanName + "'", ex);
					}
					onSuppressedException(ex);
				}
			}
		}

		// Check singletons too, to catch manually registered singletons.
		String[] singletonNames = getSingletonNames();
		for (String beanName : singletonNames) {
			// Only check if manually registered.
			if (!containsBeanDefinition(beanName)) {
				// In case of FactoryBean, match object created by FactoryBean.
				if (isFactoryBean(beanName)) {
					if ((includeNonSingletons || isSingleton(beanName)) && isTypeMatch(beanName, type)) {
						result.add(beanName);
						// Match found for this bean: do not match FactoryBean itself anymore.
						continue;
					}
					// In case of FactoryBean, try to match FactoryBean itself next.
					beanName = FACTORY_BEAN_PREFIX + beanName;
				}
				// Match raw bean instance (might be raw FactoryBean).
				if (isTypeMatch(beanName, type)) {
					result.add(beanName);
				}
			}
		}

		return StringUtils.toStringArray(result);
	}
{

		Object arg = readWithMessageConverters(webRequest, parameter, parameter.getParameterType());
		Annotation[] annotations = parameter.getParameterAnnotations();
		for (Annotation annot : annotations) {
			if (annot.annotationType().getSimpleName().startsWith("Valid")) {
				String name = Conventions.getVariableNameForParameter(parameter);
				WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
				Object hints = AnnotationUtils.getValue(annot);
				binder.validate(hints instanceof Object[] ? (Object[]) hints : new Object[] {hints});
				BindingResult bindingResult = binder.getBindingResult();
				if (bindingResult.hasErrors()) {
					throw new MethodArgumentNotValidException(parameter, bindingResult);
				}
			}
		}
		return arg;
	}
{
		if (this.urlDecode) {
			String enc = determineEncoding(request);
			try {
				return UriUtils.decode(source, enc);
			}
			catch (UnsupportedEncodingException ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Could not decode request string [" + source + "] with encoding '" + enc +
							"': falling back to platform default encoding; exception message: " + ex.getMessage());
				}
				return URLDecoder.decode(source);
			}
		}
		return source;
	}
{
		Map<WebMvcConfigurer, Validator> validators = new HashMap<WebMvcConfigurer, Validator>();
		for (WebMvcConfigurer delegate : delegates) {
			Validator validator = delegate.getValidator();
			if (validator != null) {
				validators.put(delegate, validator);
			}
		}
		if (validators.size() == 0) {
			return null;
		}
		else if (validators.size() == 1) {
			return validators.values().iterator().next();
		}
		else {
			throw new IllegalStateException(
					"Multiple custom validators provided from [" + validators.keySet() + "]");
		}
	}
{
		this.asyncCompleted.set(true);
	}
{
		assertEquals("Cannot use async request after completion", ex.getMessage());
	}
{

		ServletRequestAttributes attributes = new ServletRequestAttributes(request);
		LocaleContextHolder.setLocale(request.getLocale(), this.threadContextInheritable);
		RequestContextHolder.setRequestAttributes(attributes, this.threadContextInheritable);
		if (logger.isDebugEnabled()) {
			logger.debug("Bound request context to thread: " + request);
		}
		try {
			filterChain.doFilter(request, response);
		}
		finally {
			LocaleContextHolder.resetLocaleContext();
			RequestContextHolder.resetRequestAttributes();
			attributes.requestCompleted();
			if (logger.isDebugEnabled()) {
				logger.debug("Cleared thread-bound request context: " + request);
			}
		}
	}
{

		ServletRequestAttributes attributes = new ServletRequestAttributes(request);
		LocaleContextHolder.setLocale(request.getLocale(), this.threadContextInheritable);
		RequestContextHolder.setRequestAttributes(attributes, this.threadContextInheritable);
		if (logger.isDebugEnabled()) {
			logger.debug("Bound request context to thread: " + request);
		}
		try {
			filterChain.doFilter(request, response);
		}
		finally {
			LocaleContextHolder.resetLocaleContext();
			RequestContextHolder.resetRequestAttributes();
			attributes.requestCompleted();
			if (logger.isDebugEnabled()) {
				logger.debug("Cleared thread-bound request context: " + request);
			}
		}
	}
{

		ShallowEtagResponseWrapper responseWrapper = new ShallowEtagResponseWrapper(response);
		filterChain.doFilter(request, responseWrapper);

		byte[] body = responseWrapper.toByteArray();
		int statusCode = responseWrapper.getStatusCode();

		if (isEligibleForEtag(request, responseWrapper, statusCode, body)) {
			String responseETag = generateETagHeaderValue(body);
			response.setHeader(HEADER_ETAG, responseETag);

			String requestETag = request.getHeader(HEADER_IF_NONE_MATCH);
			if (responseETag.equals(requestETag)) {
				if (logger.isTraceEnabled()) {
					logger.trace("ETag [" + responseETag + "] equal to If-None-Match, sending 304");
				}
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			}
			else {
				if (logger.isTraceEnabled()) {
					logger.trace("ETag [" + responseETag + "] not equal to If-None-Match [" + requestETag +
							"], sending normal response");
				}
				copyBodyToResponse(body, response);
			}
		}
		else {
			if (logger.isTraceEnabled()) {
				logger.trace("Response with status code [" + statusCode + "] not eligible for ETag");
			}
			copyBodyToResponse(body, response);
		}
	}
{
		HttpServletRequest processedRequest = request;
		HandlerExecutionChain mappedHandler = null;
		int interceptorIndex = -1;

		try {
			ModelAndView mv;
			boolean errorView = false;

			try {
				processedRequest = checkMultipart(request);

				// Determine handler for the current request.
				mappedHandler = getHandler(processedRequest, false);
				if (mappedHandler == null || mappedHandler.getHandler() == null) {
					noHandlerFound(processedRequest, response);
					return;
				}

				// Determine handler adapter for the current request.
				HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

                // Process last-modified header, if supported by the handler.
				String method = request.getMethod();
				boolean isGet = "GET".equals(method);
				if (isGet || "HEAD".equals(method)) {
					long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
					if (logger.isDebugEnabled()) {
						String requestUri = urlPathHelper.getRequestUri(request);
						logger.debug("Last-Modified value for [" + requestUri + "] is: " + lastModified);
					}
					if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
						return;
					}
				}

				// Apply preHandle methods of registered interceptors.
				HandlerInterceptor[] interceptors = mappedHandler.getInterceptors();
				if (interceptors != null) {
					for (int i = 0; i < interceptors.length; i++) {
						HandlerInterceptor interceptor = interceptors[i];
						if (!interceptor.preHandle(processedRequest, response, mappedHandler.getHandler())) {
							triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, null);
							return;
						}
						interceptorIndex = i;
					}
				}

				// Actually invoke the handler.
				mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

				// Do we need view name translation?
				if (mv != null && !mv.hasView()) {
					mv.setViewName(getDefaultViewName(request));
				}

				// Apply postHandle methods of registered interceptors.
				if (interceptors != null) {
					for (int i = interceptors.length - 1; i >= 0; i--) {
						HandlerInterceptor interceptor = interceptors[i];
						interceptor.postHandle(processedRequest, response, mappedHandler.getHandler(), mv);
					}
				}
			}
			catch (ModelAndViewDefiningException ex) {
				logger.debug("ModelAndViewDefiningException encountered", ex);
				mv = ex.getModelAndView();
			}
			catch (Exception ex) {
				Object handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
				mv = processHandlerException(processedRequest, response, handler, ex);
				errorView = (mv != null);
			}

			// Did the handler return a view to render?
			if (mv != null && !mv.wasCleared()) {
				render(mv, processedRequest, response);
				if (errorView) {
					WebUtils.clearErrorRequestAttributes(request);
				}
			}
			else {
				if (logger.isDebugEnabled()) {
					logger.debug("Null ModelAndView returned to DispatcherServlet with name '" + getServletName() +
							"': assuming HandlerAdapter completed request handling");
				}
			}

			// Trigger after-completion for successful outcome.
			triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, null);
		}

		catch (Exception ex) {
			// Trigger after-completion for thrown exception.
			triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, ex);
			throw ex;
		}
		catch (Error err) {
			ServletException ex = new NestedServletException("Handler processing failed", err);
			// Trigger after-completion for thrown exception.
			triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, ex);
			throw ex;
		}

		finally {
			// Clean up any resources used by a multipart request.
			if (processedRequest != request) {
				cleanupMultipart(processedRequest);
			}
		}
	}
{
		HttpServletRequest processedRequest = request;
		HandlerExecutionChain mappedHandler = null;
		int interceptorIndex = -1;

		try {
			ModelAndView mv;
			boolean errorView = false;

			try {
				processedRequest = checkMultipart(request);

				// Determine handler for the current request.
				mappedHandler = getHandler(processedRequest, false);
				if (mappedHandler == null || mappedHandler.getHandler() == null) {
					noHandlerFound(processedRequest, response);
					return;
				}

				// Determine handler adapter for the current request.
				HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

                // Process last-modified header, if supported by the handler.
				String method = request.getMethod();
				boolean isGet = "GET".equals(method);
				if (isGet || "HEAD".equals(method)) {
					long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
					if (logger.isDebugEnabled()) {
						String requestUri = urlPathHelper.getRequestUri(request);
						logger.debug("Last-Modified value for [" + requestUri + "] is: " + lastModified);
					}
					if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
						return;
					}
				}

				// Apply preHandle methods of registered interceptors.
				HandlerInterceptor[] interceptors = mappedHandler.getInterceptors();
				if (interceptors != null) {
					for (int i = 0; i < interceptors.length; i++) {
						HandlerInterceptor interceptor = interceptors[i];
						if (!interceptor.preHandle(processedRequest, response, mappedHandler.getHandler())) {
							triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, null);
							return;
						}
						interceptorIndex = i;
					}
				}

				// Actually invoke the handler.
				mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

				// Do we need view name translation?
				if (mv != null && !mv.hasView()) {
					mv.setViewName(getDefaultViewName(request));
				}

				// Apply postHandle methods of registered interceptors.
				if (interceptors != null) {
					for (int i = interceptors.length - 1; i >= 0; i--) {
						HandlerInterceptor interceptor = interceptors[i];
						interceptor.postHandle(processedRequest, response, mappedHandler.getHandler(), mv);
					}
				}
			}
			catch (ModelAndViewDefiningException ex) {
				logger.debug("ModelAndViewDefiningException encountered", ex);
				mv = ex.getModelAndView();
			}
			catch (Exception ex) {
				Object handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
				mv = processHandlerException(processedRequest, response, handler, ex);
				errorView = (mv != null);
			}

			// Did the handler return a view to render?
			if (mv != null && !mv.wasCleared()) {
				render(mv, processedRequest, response);
				if (errorView) {
					WebUtils.clearErrorRequestAttributes(request);
				}
			}
			else {
				if (logger.isDebugEnabled()) {
					logger.debug("Null ModelAndView returned to DispatcherServlet with name '" + getServletName() +
							"': assuming HandlerAdapter completed request handling");
				}
			}

			// Trigger after-completion for successful outcome.
			triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, null);
		}

		catch (Exception ex) {
			// Trigger after-completion for thrown exception.
			triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, ex);
			throw ex;
		}
		catch (Error err) {
			ServletException ex = new NestedServletException("Handler processing failed", err);
			// Trigger after-completion for thrown exception.
			triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, ex);
			throw ex;
		}

		finally {
			// Clean up any resources used by a multipart request.
			if (processedRequest != request) {
				cleanupMultipart(processedRequest);
			}
		}
	}
{
		HttpServletRequest processedRequest = request;
		HandlerExecutionChain mappedHandler = null;
		int interceptorIndex = -1;

		try {
			ModelAndView mv;
			boolean errorView = false;

			try {
				processedRequest = checkMultipart(request);

				// Determine handler for the current request.
				mappedHandler = getHandler(processedRequest, false);
				if (mappedHandler == null || mappedHandler.getHandler() == null) {
					noHandlerFound(processedRequest, response);
					return;
				}

				// Determine handler adapter for the current request.
				HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

                // Process last-modified header, if supported by the handler.
				String method = request.getMethod();
				boolean isGet = "GET".equals(method);
				if (isGet || "HEAD".equals(method)) {
					long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
					if (logger.isDebugEnabled()) {
						String requestUri = urlPathHelper.getRequestUri(request);
						logger.debug("Last-Modified value for [" + requestUri + "] is: " + lastModified);
					}
					if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
						return;
					}
				}

				// Apply preHandle methods of registered interceptors.
				HandlerInterceptor[] interceptors = mappedHandler.getInterceptors();
				if (interceptors != null) {
					for (int i = 0; i < interceptors.length; i++) {
						HandlerInterceptor interceptor = interceptors[i];
						if (!interceptor.preHandle(processedRequest, response, mappedHandler.getHandler())) {
							triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, null);
							return;
						}
						interceptorIndex = i;
					}
				}

				// Actually invoke the handler.
				mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

				// Do we need view name translation?
				if (mv != null && !mv.hasView()) {
					mv.setViewName(getDefaultViewName(request));
				}

				// Apply postHandle methods of registered interceptors.
				if (interceptors != null) {
					for (int i = interceptors.length - 1; i >= 0; i--) {
						HandlerInterceptor interceptor = interceptors[i];
						interceptor.postHandle(processedRequest, response, mappedHandler.getHandler(), mv);
					}
				}
			}
			catch (ModelAndViewDefiningException ex) {
				logger.debug("ModelAndViewDefiningException encountered", ex);
				mv = ex.getModelAndView();
			}
			catch (Exception ex) {
				Object handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
				mv = processHandlerException(processedRequest, response, handler, ex);
				errorView = (mv != null);
			}

			// Did the handler return a view to render?
			if (mv != null && !mv.wasCleared()) {
				render(mv, processedRequest, response);
				if (errorView) {
					WebUtils.clearErrorRequestAttributes(request);
				}
			}
			else {
				if (logger.isDebugEnabled()) {
					logger.debug("Null ModelAndView returned to DispatcherServlet with name '" + getServletName() +
							"': assuming HandlerAdapter completed request handling");
				}
			}

			// Trigger after-completion for successful outcome.
			triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, null);
		}

		catch (Exception ex) {
			// Trigger after-completion for thrown exception.
			triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, ex);
			throw ex;
		}
		catch (Error err) {
			ServletException ex = new NestedServletException("Handler processing failed", err);
			// Trigger after-completion for thrown exception.
			triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, ex);
			throw ex;
		}

		finally {
			// Clean up any resources used by a multipart request.
			if (processedRequest != request) {
				cleanupMultipart(processedRequest);
			}
		}
	}
{
		HttpServletRequest processedRequest = request;
		HandlerExecutionChain mappedHandler = null;
		int interceptorIndex = -1;

		try {
			ModelAndView mv;
			boolean errorView = false;

			try {
				processedRequest = checkMultipart(request);

				// Determine handler for the current request.
				mappedHandler = getHandler(processedRequest, false);
				if (mappedHandler == null || mappedHandler.getHandler() == null) {
					noHandlerFound(processedRequest, response);
					return;
				}

				// Determine handler adapter for the current request.
				HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

                // Process last-modified header, if supported by the handler.
				String method = request.getMethod();
				boolean isGet = "GET".equals(method);
				if (isGet || "HEAD".equals(method)) {
					long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
					if (logger.isDebugEnabled()) {
						String requestUri = urlPathHelper.getRequestUri(request);
						logger.debug("Last-Modified value for [" + requestUri + "] is: " + lastModified);
					}
					if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
						return;
					}
				}

				// Apply preHandle methods of registered interceptors.
				HandlerInterceptor[] interceptors = mappedHandler.getInterceptors();
				if (interceptors != null) {
					for (int i = 0; i < interceptors.length; i++) {
						HandlerInterceptor interceptor = interceptors[i];
						if (!interceptor.preHandle(processedRequest, response, mappedHandler.getHandler())) {
							triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, null);
							return;
						}
						interceptorIndex = i;
					}
				}

				// Actually invoke the handler.
				mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

				// Do we need view name translation?
				if (mv != null && !mv.hasView()) {
					mv.setViewName(getDefaultViewName(request));
				}

				// Apply postHandle methods of registered interceptors.
				if (interceptors != null) {
					for (int i = interceptors.length - 1; i >= 0; i--) {
						HandlerInterceptor interceptor = interceptors[i];
						interceptor.postHandle(processedRequest, response, mappedHandler.getHandler(), mv);
					}
				}
			}
			catch (ModelAndViewDefiningException ex) {
				logger.debug("ModelAndViewDefiningException encountered", ex);
				mv = ex.getModelAndView();
			}
			catch (Exception ex) {
				Object handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
				mv = processHandlerException(processedRequest, response, handler, ex);
				errorView = (mv != null);
			}

			// Did the handler return a view to render?
			if (mv != null && !mv.wasCleared()) {
				render(mv, processedRequest, response);
				if (errorView) {
					WebUtils.clearErrorRequestAttributes(request);
				}
			}
			else {
				if (logger.isDebugEnabled()) {
					logger.debug("Null ModelAndView returned to DispatcherServlet with name '" + getServletName() +
							"': assuming HandlerAdapter completed request handling");
				}
			}

			// Trigger after-completion for successful outcome.
			triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, null);
		}

		catch (Exception ex) {
			// Trigger after-completion for thrown exception.
			triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, ex);
			throw ex;
		}
		catch (Error err) {
			ServletException ex = new NestedServletException("Handler processing failed", err);
			// Trigger after-completion for thrown exception.
			triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, ex);
			throw ex;
		}

		finally {
			// Clean up any resources used by a multipart request.
			if (processedRequest != request) {
				cleanupMultipart(processedRequest);
			}
		}
	}
{

		long startTime = System.currentTimeMillis();
		Throwable failureCause = null;

		// Expose current LocaleResolver and request as LocaleContext.
		LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
		LocaleContextHolder.setLocaleContext(buildLocaleContext(request), this.threadContextInheritable);

		// Expose current RequestAttributes to current thread.
		RequestAttributes previousRequestAttributes = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes requestAttributes = null;
		if (previousRequestAttributes == null || previousRequestAttributes.getClass().equals(ServletRequestAttributes.class)) {
			requestAttributes = new ServletRequestAttributes(request);
			RequestContextHolder.setRequestAttributes(requestAttributes, this.threadContextInheritable);
		}

		if (logger.isTraceEnabled()) {
			logger.trace("Bound request context to thread: " + request);
		}

		try {
			doService(request, response);
		}
		catch (ServletException ex) {
			failureCause = ex;
			throw ex;
		}
		catch (IOException ex) {
			failureCause = ex;
			throw ex;
		}
		catch (Throwable ex) {
			failureCause = ex;
			throw new NestedServletException("Request processing failed", ex);
		}

		finally {
			// Clear request attributes and reset thread-bound context.
			LocaleContextHolder.setLocaleContext(previousLocaleContext, this.threadContextInheritable);
			if (requestAttributes != null) {
				RequestContextHolder.setRequestAttributes(previousRequestAttributes, this.threadContextInheritable);
				requestAttributes.requestCompleted();
			}
			if (logger.isTraceEnabled()) {
				logger.trace("Cleared thread-bound request context: " + request);
			}

			if (logger.isDebugEnabled()) {
				if (failureCause != null) {
					this.logger.debug("Could not complete request", failureCause);
				}
				else {
					this.logger.debug("Successfully completed request");
				}
			}
			if (this.publishEvents) {
				// Whether or not we succeeded, publish an event.
				long processingTime = System.currentTimeMillis() - startTime;
				this.webApplicationContext.publishEvent(
						new ServletRequestHandledEvent(this,
								request.getRequestURI(), request.getRemoteAddr(),
								request.getMethod(), getServletConfig().getServletName(),
								WebUtils.getSessionId(request), getUsernameForRequest(request),
								processingTime, failureCause));
			}
		}
	}
{

		long startTime = System.currentTimeMillis();
		Throwable failureCause = null;

		// Expose current LocaleResolver and request as LocaleContext.
		LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
		LocaleContextHolder.setLocaleContext(buildLocaleContext(request), this.threadContextInheritable);

		// Expose current RequestAttributes to current thread.
		RequestAttributes previousRequestAttributes = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes requestAttributes = null;
		if (previousRequestAttributes == null || previousRequestAttributes.getClass().equals(ServletRequestAttributes.class)) {
			requestAttributes = new ServletRequestAttributes(request);
			RequestContextHolder.setRequestAttributes(requestAttributes, this.threadContextInheritable);
		}

		if (logger.isTraceEnabled()) {
			logger.trace("Bound request context to thread: " + request);
		}

		try {
			doService(request, response);
		}
		catch (ServletException ex) {
			failureCause = ex;
			throw ex;
		}
		catch (IOException ex) {
			failureCause = ex;
			throw ex;
		}
		catch (Throwable ex) {
			failureCause = ex;
			throw new NestedServletException("Request processing failed", ex);
		}

		finally {
			// Clear request attributes and reset thread-bound context.
			LocaleContextHolder.setLocaleContext(previousLocaleContext, this.threadContextInheritable);
			if (requestAttributes != null) {
				RequestContextHolder.setRequestAttributes(previousRequestAttributes, this.threadContextInheritable);
				requestAttributes.requestCompleted();
			}
			if (logger.isTraceEnabled()) {
				logger.trace("Cleared thread-bound request context: " + request);
			}

			if (logger.isDebugEnabled()) {
				if (failureCause != null) {
					this.logger.debug("Could not complete request", failureCause);
				}
				else {
					this.logger.debug("Successfully completed request");
				}
			}
			if (this.publishEvents) {
				// Whether or not we succeeded, publish an event.
				long processingTime = System.currentTimeMillis() - startTime;
				this.webApplicationContext.publishEvent(
						new ServletRequestHandledEvent(this,
								request.getRequestURI(), request.getRemoteAddr(),
								request.getMethod(), getServletConfig().getServletName(),
								WebUtils.getSessionId(request), getUsernameForRequest(request),
								processingTime, failureCause));
			}
		}
	}
{

		long startTime = System.currentTimeMillis();
		Throwable failureCause = null;

		// Expose current LocaleResolver and request as LocaleContext.
		LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
		LocaleContextHolder.setLocaleContext(buildLocaleContext(request), this.threadContextInheritable);

		// Expose current RequestAttributes to current thread.
		RequestAttributes previousRequestAttributes = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes requestAttributes = null;
		if (previousRequestAttributes == null || previousRequestAttributes.getClass().equals(ServletRequestAttributes.class)) {
			requestAttributes = new ServletRequestAttributes(request);
			RequestContextHolder.setRequestAttributes(requestAttributes, this.threadContextInheritable);
		}

		if (logger.isTraceEnabled()) {
			logger.trace("Bound request context to thread: " + request);
		}

		try {
			doService(request, response);
		}
		catch (ServletException ex) {
			failureCause = ex;
			throw ex;
		}
		catch (IOException ex) {
			failureCause = ex;
			throw ex;
		}
		catch (Throwable ex) {
			failureCause = ex;
			throw new NestedServletException("Request processing failed", ex);
		}

		finally {
			// Clear request attributes and reset thread-bound context.
			LocaleContextHolder.setLocaleContext(previousLocaleContext, this.threadContextInheritable);
			if (requestAttributes != null) {
				RequestContextHolder.setRequestAttributes(previousRequestAttributes, this.threadContextInheritable);
				requestAttributes.requestCompleted();
			}
			if (logger.isTraceEnabled()) {
				logger.trace("Cleared thread-bound request context: " + request);
			}

			if (logger.isDebugEnabled()) {
				if (failureCause != null) {
					this.logger.debug("Could not complete request", failureCause);
				}
				else {
					this.logger.debug("Successfully completed request");
				}
			}
			if (this.publishEvents) {
				// Whether or not we succeeded, publish an event.
				long processingTime = System.currentTimeMillis() - startTime;
				this.webApplicationContext.publishEvent(
						new ServletRequestHandledEvent(this,
								request.getRequestURI(), request.getRemoteAddr(),
								request.getMethod(), getServletConfig().getServletName(),
								WebUtils.getSessionId(request), getUsernameForRequest(request),
								processingTime, failureCause));
			}
		}
	}
{
		for (MethodParameter methodParameter : methodParameters) {
			if (! this.argumentResolvers.supportsParameter(methodParameter)) {
				return false;
			}
		}
		return true;
	}
{
		
		ServletWebRequest webRequest = new ServletWebRequest(request, response);

		WebDataBinderFactory binderFactory = getDataBinderFactory(handlerMethod);
		ModelFactory modelFactory = getModelFactory(handlerMethod, binderFactory);
		ServletInvocableHandlerMethod requestMappingMethod = createRequestMappingMethod(handlerMethod, binderFactory);

		ModelAndViewContainer mavContainer = new ModelAndViewContainer();
		mavContainer.addAllAttributes(RequestContextUtils.getInputFlashMap(request));
		modelFactory.initModel(webRequest, mavContainer, requestMappingMethod);
		mavContainer.setIgnoreDefaultModelOnRedirect(this.ignoreDefaultModelOnRedirect);

		requestMappingMethod.invokeAndHandle(webRequest, mavContainer);
		modelFactory.updateModel(webRequest, mavContainer);

		if (mavContainer.isRequestHandled()) {
			return null;
		}
		else {
			ModelMap model = mavContainer.getModel();
			ModelAndView mav = new ModelAndView(mavContainer.getViewName(), model);
			if (!mavContainer.isViewReference()) {
				mav.setView((View) mavContainer.getView());
			}
			if (model instanceof RedirectAttributes) {
				Map<String, ?> flashAttributes = ((RedirectAttributes) model).getFlashAttributes();
				RequestContextUtils.getOutputFlashMap(request).putAll(flashAttributes);
			}
			return mav;
		}
	}
{
		Map<String, Object> attrs = new HashMap<String, Object>();
		Method[] methods = annotation.annotationType().getDeclaredMethods();
		for (Method method : methods) {
			if (method.getParameterTypes().length == 0 && method.getReturnType() != void.class) {
				try {
					Object value = method.invoke(annotation);
					if (classValuesAsString) {
						if (value instanceof Class) {
							value = ((Class<?>) value).getName();
						}
						else if (value instanceof Class[]) {
							Class<?>[] clazzArray = (Class[]) value;
							String[] newValue = new String[clazzArray.length];
							for (int i = 0; i < clazzArray.length; i++) {
								newValue[i] = clazzArray[i].getName();
							}
							value = newValue;
						}
					}
					attrs.put(method.getName(), value);
				}
				catch (Exception ex) {
					throw new IllegalStateException("Could not obtain annotation attribute values", ex);
				}
			}
		}
		return attrs;
	}
{

		HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
		if (!isMultipartRequest(servletRequest)) {
			throw new MultipartException("The current request is not a multipart request");
		}
		
		MultipartHttpServletRequest multipartRequest = 
			WebUtils.getNativeRequest(servletRequest, MultipartHttpServletRequest.class);

		String partName = getPartName(parameter);
		Object arg;

		if (MultipartFile.class.equals(parameter.getParameterType())) {
			Assert.notNull(multipartRequest, "Expected MultipartHttpServletRequest: is a MultipartResolver configured?");
			arg = multipartRequest.getFile(partName);
		}
		else if (isMultipartFileCollection(parameter)) {
			Assert.notNull(multipartRequest, "Expected MultipartHttpServletRequest: is a MultipartResolver configured?");
			arg = multipartRequest.getFiles(partName);
		}
		else if ("javax.servlet.http.Part".equals(parameter.getParameterType().getName())) {
			arg = servletRequest.getPart(partName);
		}
		else {
			try {
				HttpInputMessage inputMessage = new RequestPartServletServerHttpRequest(servletRequest, partName);
				arg = readWithMessageConverters(inputMessage, parameter, parameter.getParameterType());
				if (arg != null) {
					Annotation[] annotations = parameter.getParameterAnnotations();
					for (Annotation annot : annotations) {
						if (annot.annotationType().getSimpleName().startsWith("Valid")) {
							WebDataBinder binder = binderFactory.createBinder(request, arg, partName);
							Object hints = AnnotationUtils.getValue(annot);
							binder.validate(hints instanceof Object[] ? (Object[]) hints : new Object[] {hints});
							BindingResult bindingResult = binder.getBindingResult();
							if (bindingResult.hasErrors()) {
								throw new MethodArgumentNotValidException(parameter, bindingResult);
							}
						}
					}
				}
			}
			catch (MissingServletRequestPartException ex) {
				// handled below
				arg = null;
			}
		}

		RequestPart annot = parameter.getParameterAnnotation(RequestPart.class);
		boolean isRequired = (annot == null || annot.required());

		if (arg == null && isRequired) {
			throw new MissingServletRequestPartException(partName);
		}
		
		return arg;
	}
{
		if (!"post".equals(request.getMethod().toLowerCase())) {
			return false;
		}
		String contentType = request.getContentType();
		return (contentType != null && contentType.toLowerCase().startsWith("multipart/"));
	}
{
		RedirectView rv = new RedirectView();
		rv.setUrl("http://url.somewhere.com");
		rv.setHttp10Compatible(false);
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		rv.render(new HashMap<String, Object>(), request, response);
		assertEquals(303, response.getStatus());
		assertEquals("http://url.somewhere.com", response.getHeader("Location"));
	}
{
		List<T> mappings = urlMap.get(lookupPath);
		if (mappings == null) {
			mappings = new ArrayList<T>(handlerMethods.keySet());
		}
			
		List<Match> matches = new ArrayList<Match>();
		
		for (T mapping : mappings) {
			T match = getMatchingMapping(mapping, request);
			if (match != null) {
				matches.add(new Match(match, handlerMethods.get(mapping)));
			}
		}

		if (!matches.isEmpty()) {
			Comparator<Match> comparator = new MatchComparator(getMappingComparator(request));
			Collections.sort(matches, comparator);

			if (logger.isTraceEnabled()) {
				logger.trace("Found " + matches.size() + " matching mapping(s) for [" + lookupPath + "] : " + matches);
			}

			Match bestMatch = matches.get(0);
			if (matches.size() > 1) {
				Match secondBestMatch = matches.get(1);
				if (comparator.compare(bestMatch, secondBestMatch) == 0) {
					Method m1 = bestMatch.handlerMethod.getMethod();
					Method m2 = secondBestMatch.handlerMethod.getMethod();
					throw new IllegalStateException(
							"Ambiguous handler methods mapped for HTTP path '" + request.getRequestURL() + "': {" +
							m1 + ", " + m2 + "}");
				}
			}

			handleMatch(bestMatch.mapping, lookupPath, request);
			return bestMatch.handlerMethod;
		}
		else {
			return handleNoMatch(handlerMethods.keySet(), lookupPath, request);
		}
	}
{
		FlashMap flashMap = (FlashMap) request.getAttribute(OUTPUT_FLASH_MAP_ATTRIBUTE);
		if (flashMap == null) {
			throw new IllegalStateException("requestCompleted called but \"output\" FlashMap was never created");
		}
		if (!flashMap.isEmpty() && flashMap.isCreatedBy(this.hashCode())) {
			if (logger.isDebugEnabled()) {
				logger.debug("Saving FlashMap=" + flashMap);
			}
			onSaveFlashMap(flashMap, request);
			retrieveFlashMaps(request, true).add(flashMap);
		}
	}
{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("annotationDrivenProxyTargetClassTests.xml", getClass());
		CallCountingTransactionManager tm1 = context.getBean("transactionManager1", CallCountingTransactionManager.class);
		CallCountingTransactionManager tm2 = context.getBean("transactionManager2", CallCountingTransactionManager.class);
		TransactionalService service = context.getBean("service", TransactionalService.class);
		assertTrue(AopUtils.isCglibProxy(service));
		service.setSomething("someName");
		assertEquals(1, tm1.commits);
		assertEquals(0, tm2.commits);
		service.doSomething();
		assertEquals(1, tm1.commits);
		assertEquals(1, tm2.commits);
		service.setSomething("someName");
		assertEquals(2, tm1.commits);
		assertEquals(1, tm2.commits);
		service.doSomething();
		assertEquals(2, tm1.commits);
		assertEquals(2, tm2.commits);
	}
{
		Assert.notNull(validator, "Validator must not be null");
		Assert.notNull(errors, "Errors object must not be null");
		if (logger.isDebugEnabled()) {
			logger.debug("Invoking validator [" + validator + "]");
		}
		if (obj != null && !validator.supports(obj.getClass())) {
			throw new IllegalArgumentException(
					"Validator [" + validator.getClass() + "] does not support [" + obj.getClass() + "]");
		}
		validator.validate(obj, errors);
		if (logger.isDebugEnabled()) {
			if (errors.hasErrors()) {
				logger.debug("Validator found " + errors.getErrorCount() + " errors");
			}
			else {
				logger.debug("Validator found no errors");
			}
		}
	}
{
		Set<ConstraintViolation<Object>> result = this.targetValidator.validate(target);
		for (ConstraintViolation<Object> violation : result) {
			String field = violation.getPropertyPath().toString();
			FieldError fieldError = errors.getFieldError(field);
			if (fieldError == null || !fieldError.isBindingFailure()) {
				try {
					String errorCode = violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
					Object[] errorArgs = getArgumentsForConstraint(errors.getObjectName(), field, violation.getConstraintDescriptor());
					if (errors instanceof BindingResult) {
						// can do custom FieldError registration with invalid value from ConstraintViolation,
						// as necessary for Hibernate Validator compatibility (non-indexed set path in field)
						BindingResult bindingResult = (BindingResult) errors;
						String[] errorCodes = bindingResult.resolveMessageCodes(errorCode, field);
						String nestedField = bindingResult.getNestedPath() + field;
						ObjectError error;
						if ("".equals(nestedField)) {
							error = new ObjectError(
									errors.getObjectName(), errorCodes, errorArgs, violation.getMessage());
						}
						else {
							error = new FieldError(
									errors.getObjectName(), nestedField, violation.getInvalidValue(), false,
									errorCodes, errorArgs, violation.getMessage());
						}
						bindingResult.addError(error);
					}
					else {
						// got no BindingResult - can only do standard rejectValue call
						// with automatic extraction of the current field value
						errors.rejectValue(field, errorCode, errorArgs, violation.getMessage());
					}
				}
				catch (NotReadablePropertyException ex) {
					throw new IllegalStateException("JSR-303 validated property '" + field +
							"' does not have a corresponding accessor for Spring data binding - " +
							"check your DataBinder's configuration (bean property versus direct field access)", ex);
				}
			}
		}
	}
{
		Annotation[] annotations = parameter.getParameterAnnotations();
		for (Annotation annot : annotations) {
			if ("Valid".equals(annot.annotationType().getSimpleName())) {
				return true;
			}
		}
		return false;
	}
{
		if (this.body == null) {
			int contentLength = (int) headers.getContentLength();
			if (contentLength >= 0) {
				this.connection.setFixedLengthStreamingMode(contentLength);
			}
			else {
				this.connection.setChunkedStreamingMode(this.chunkSize);
			}
			for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
				String headerName = entry.getKey();
				for (String headerValue : entry.getValue()) {
					this.connection.addRequestProperty(headerName, headerValue);
				}
			}
			this.connection.connect();
			this.body = this.connection.getOutputStream();
		}
		return new NonClosingOutputStream(this.body);
	}
{
		Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
		this.registry = registry;

		// Inherit Environment if possible
		if (this.registry instanceof EnvironmentCapable) {
			this.environment = ((EnvironmentCapable) this.registry).getEnvironment();
		}

		AnnotationConfigUtils.registerAnnotationConfigProcessors(this.registry);
	}
{
		this.reader.setEnvironment(environment);
		this.scanner.setEnvironment(environment);
	}
{
		super(useDefaultFilters);

		Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
		this.registry = registry;

		// Determine ResourceLoader to use.
		if (this.registry instanceof ResourceLoader) {
			setResourceLoader((ResourceLoader) this.registry);
		}

		// Inherit Environment if possible
		if (this.registry instanceof EnvironmentCapable) {
			setEnvironment(((EnvironmentCapable) this.registry).getEnvironment());
		}
	}
{
		HeaderValueHolder header = HeaderValueHolder.getByName(this.headers, name);
		Assert.notNull(value, "Header value must not be null");
		if (header == null) {
			header = new HeaderValueHolder();
			this.headers.put(name, header);
		}
		if (value instanceof Collection) {
			header.addValues((Collection) value);
		}
		else if (value.getClass().isArray()) {
			header.addValueArray(value);
		}
		else {
			header.addValue(value);
		}
	}
{
		// check whether aspect is enabled
		// to cope with cases where the AJ is pulled in automatically
		if (!this.initialized) {
			return invoker.invoke();
		}

		boolean log = logger.isTraceEnabled();

		// get backing class
		Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
		if (targetClass == null && target != null) {
			targetClass = target.getClass();
		}
		final CacheOperation cacheOp = getCacheOperationSource().getCacheOperation(method, targetClass);

		Object retVal = null;

		// analyze caching information
		if (cacheOp != null) {
			CacheOperationContext context = getOperationContext(cacheOp, method, args, target, targetClass);
			Collection<Cache> caches = context.getCaches();

			if (context.hasConditionPassed()) {
				// check operation
				if (cacheOp instanceof CacheUpdateOperation) {
					Object key = context.generateKey();
					if (log) {
						logger.trace("Computed cache key " + key + " for definition " + cacheOp);
					}
					if (key == null) {
						throw new IllegalArgumentException(
								"Null key returned for cache definition (maybe you are using named params on classes without debug info?) "
										+ cacheOp);
					}

					// for each cache
					boolean cacheHit = false;

					for (Iterator<Cache> iterator = caches.iterator(); iterator.hasNext() && !cacheHit;) {
						Cache cache = iterator.next();
						Cache.ValueWrapper wrapper = cache.get(key);
						if (wrapper != null) {
							cacheHit = true;
							retVal = wrapper.get();
						}
					}

					if (!cacheHit) {
						if (log) {
							logger.trace("Key " + key + " NOT found in cache(s), invoking cached target method  "
									+ method);
						}
						retVal = invoker.invoke();
						// update all caches
						for (Cache cache : caches) {
							cache.put(key, retVal);
						}
					}
					else {
						if (log) {
							logger.trace("Key " + key + " found in cache, returning value " + retVal);
						}
					}
				}

				if (cacheOp instanceof CacheEvictOperation) {
					CacheEvictOperation evictOp = (CacheEvictOperation) cacheOp;

					// for each cache
					// lazy key initialization
					Object key = null;

					for (Cache cache : caches) {
						// flush the cache (ignore arguments)
						if (evictOp.isCacheWide()) {
							cache.clear();
							if (log) {
								logger.trace("Invalidating entire cache for definition " + cacheOp +
										" on method " + method);
							}
						}
						else {
							// check key
							if (key == null) {
								key = context.generateKey();
							}
							if (log) {
								logger.trace("Invalidating cache key " + key + " for definition " + cacheOp
										+ " on method " + method);
							}
							cache.evict(key);
						}
					}
					retVal = invoker.invoke();
				}
				return retVal;
			}
			else {
				if (log) {
					logger.trace("Cache condition failed on method " + method + " for definition " + cacheOp);
				}
			}
		}

		return invoker.invoke();
	}
{
		// check whether aspect is enabled
		// to cope with cases where the AJ is pulled in automatically
		if (!this.initialized) {
			return invoker.invoke();
		}

		boolean log = logger.isTraceEnabled();

		// get backing class
		Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
		if (targetClass == null && target != null) {
			targetClass = target.getClass();
		}
		final CacheOperation cacheOp = getCacheOperationSource().getCacheOperation(method, targetClass);

		Object retVal = null;

		// analyze caching information
		if (cacheOp != null) {
			CacheOperationContext context = getOperationContext(cacheOp, method, args, target, targetClass);
			Collection<Cache> caches = context.getCaches();

			if (context.hasConditionPassed()) {
				// check operation
				if (cacheOp instanceof CacheUpdateOperation) {
					Object key = context.generateKey();
					if (log) {
						logger.trace("Computed cache key " + key + " for definition " + cacheOp);
					}
					if (key == null) {
						throw new IllegalArgumentException(
								"Null key returned for cache definition (maybe you are using named params on classes without debug info?) "
										+ cacheOp);
					}

					// for each cache
					boolean cacheHit = false;

					for (Iterator<Cache> iterator = caches.iterator(); iterator.hasNext() && !cacheHit;) {
						Cache cache = iterator.next();
						Cache.ValueWrapper wrapper = cache.get(key);
						if (wrapper != null) {
							cacheHit = true;
							retVal = wrapper.get();
						}
					}

					if (!cacheHit) {
						if (log) {
							logger.trace("Key " + key + " NOT found in cache(s), invoking cached target method  "
									+ method);
						}
						retVal = invoker.invoke();
						// update all caches
						for (Cache cache : caches) {
							cache.put(key, retVal);
						}
					}
					else {
						if (log) {
							logger.trace("Key " + key + " found in cache, returning value " + retVal);
						}
					}
				}

				if (cacheOp instanceof CacheEvictOperation) {
					CacheEvictOperation evictOp = (CacheEvictOperation) cacheOp;

					// for each cache
					// lazy key initialization
					Object key = null;

					for (Cache cache : caches) {
						// flush the cache (ignore arguments)
						if (evictOp.isCacheWide()) {
							cache.clear();
							if (log) {
								logger.trace("Invalidating entire cache for definition " + cacheOp +
										" on method " + method);
							}
						}
						else {
							// check key
							if (key == null) {
								key = context.generateKey();
							}
							if (log) {
								logger.trace("Invalidating cache key " + key + " for definition " + cacheOp
										+ " on method " + method);
							}
							cache.evict(key);
						}
					}
					retVal = invoker.invoke();
				}
				return retVal;
			}
			else {
				if (log) {
					logger.trace("Cache condition failed on method " + method + " for definition " + cacheOp);
				}
			}
		}

		return invoker.invoke();
	}
{
		// check whether aspect is enabled
		// to cope with cases where the AJ is pulled in automatically
		if (!this.initialized) {
			return invoker.invoke();
		}

		boolean log = logger.isTraceEnabled();

		// get backing class
		Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
		if (targetClass == null && target != null) {
			targetClass = target.getClass();
		}
		final CacheOperation cacheOp = getCacheOperationSource().getCacheOperation(method, targetClass);

		Object retVal = null;

		// analyze caching information
		if (cacheOp != null) {
			CacheOperationContext context = getOperationContext(cacheOp, method, args, target, targetClass);
			Collection<Cache> caches = context.getCaches();

			if (context.hasConditionPassed()) {
				// check operation
				if (cacheOp instanceof CacheUpdateOperation) {
					Object key = context.generateKey();
					if (log) {
						logger.trace("Computed cache key " + key + " for definition " + cacheOp);
					}
					if (key == null) {
						throw new IllegalArgumentException(
								"Null key returned for cache definition (maybe you are using named params on classes without debug info?) "
										+ cacheOp);
					}

					// for each cache
					boolean cacheHit = false;

					for (Iterator<Cache> iterator = caches.iterator(); iterator.hasNext() && !cacheHit;) {
						Cache cache = iterator.next();
						Cache.ValueWrapper wrapper = cache.get(key);
						if (wrapper != null) {
							cacheHit = true;
							retVal = wrapper.get();
						}
					}

					if (!cacheHit) {
						if (log) {
							logger.trace("Key " + key + " NOT found in cache(s), invoking cached target method  "
									+ method);
						}
						retVal = invoker.invoke();
						// update all caches
						for (Cache cache : caches) {
							cache.put(key, retVal);
						}
					}
					else {
						if (log) {
							logger.trace("Key " + key + " found in cache, returning value " + retVal);
						}
					}
				}

				if (cacheOp instanceof CacheEvictOperation) {
					CacheEvictOperation evictOp = (CacheEvictOperation) cacheOp;

					// for each cache
					// lazy key initialization
					Object key = null;

					for (Cache cache : caches) {
						// flush the cache (ignore arguments)
						if (evictOp.isCacheWide()) {
							cache.clear();
							if (log) {
								logger.trace("Invalidating entire cache for definition " + cacheOp +
										" on method " + method);
							}
						}
						else {
							// check key
							if (key == null) {
								key = context.generateKey();
							}
							if (log) {
								logger.trace("Invalidating cache key " + key + " for definition " + cacheOp
										+ " on method " + method);
							}
							cache.evict(key);
						}
					}
					retVal = invoker.invoke();
				}
				return retVal;
			}
			else {
				if (log) {
					logger.trace("Cache condition failed on method " + method + " for definition " + cacheOp);
				}
			}
		}

		return invoker.invoke();
	}
{
		for (MethodParameter parameter : handlerMethod.getMethodParameters()) {
			if (parameter.hasParameterAnnotation(ModelAttribute.class)) {
				String attrName = getNameForParameter(parameter);
				if (!mavContainer.containsAttribute(attrName)) {
					if (sessionAttributesHandler.isHandlerSessionAttribute(attrName, parameter.getParameterType())) {
						Object attrValue = sessionAttributesHandler.retrieveAttribute(request, attrName);
						if (attrValue == null){
							throw new HttpSessionRequiredException(
									"Session attribute '" + attrName + "' not found in session: " + handlerMethod);
						}
						mavContainer.addAttribute(attrName, attrValue);
					}
				}
			}
		}
	}
{

		String targetUrl = createTargetUrl(model, request);

		if (getWebApplicationContext() != null) {
			RequestContext requestContext = createRequestContext(request, response, model);
			RequestDataValueProcessor processor = requestContext.getRequestDataValueProcessor();
			if (processor != null) {
				targetUrl = processor.processUrl(request, targetUrl);
			}
		}
		
		FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
		if (!CollectionUtils.isEmpty(flashMap)) {
			UriComponents uriComponents = UriComponentsBuilder.fromUriString(targetUrl).build();
			flashMap.setTargetRequestPath(uriComponents.getPath());
			flashMap.addTargetRequestParams(uriComponents.getQueryParams());
		}
		
		sendRedirect(request, response, targetUrl.toString(), this.http10Compatible);
	}
{
		Assert.notNull(sql, "SQL must not be null");

		Set<String> namedParameters = new HashSet<String>();
		ParsedSql parsedSql = new ParsedSql(sql);

		char[] statement = sql.toCharArray();
		int namedParameterCount = 0;
		int unnamedParameterCount = 0;
		int totalParameterCount = 0;

		int i = 0;
		while (i < statement.length) {
			int skipToPosition = skipCommentsAndQuotes(statement, i);
			if (i != skipToPosition) {
				if (skipToPosition >= statement.length) {
					break;
				}
				i = skipToPosition;
			}
			char c = statement[i];
			if (c == ':' || c == '&') {
				int j = i + 1;
				if (j < statement.length && statement[j] == ':' && c == ':') {
					// Postgres-style "::" casting operator - to be skipped.
					i = i + 2;
					continue;
				}
				while (j < statement.length && !isParameterSeparator(statement[j])) {
					j++;
				}
				if (j - i > 1) {
					String parameter = sql.substring(i + 1, j);
					if (!namedParameters.contains(parameter)) {
						namedParameters.add(parameter);
						namedParameterCount++;
					}
					parsedSql.addNamedParameter(parameter, i, j);
					totalParameterCount++;
				}
				i = j - 1;
			}
			else {
				if (c == '?') {
					unnamedParameterCount++;
					totalParameterCount++;
				}
			}
			i++;
		}
		parsedSql.setNamedParameterCount(namedParameterCount);
		parsedSql.setUnnamedParameterCount(unnamedParameterCount);
		parsedSql.setTotalParameterCount(totalParameterCount);
		return parsedSql;
	}
{
		Assert.notNull(sql, "SQL must not be null");

		Set<String> namedParameters = new HashSet<String>();
		ParsedSql parsedSql = new ParsedSql(sql);

		char[] statement = sql.toCharArray();
		int namedParameterCount = 0;
		int unnamedParameterCount = 0;
		int totalParameterCount = 0;

		int i = 0;
		while (i < statement.length) {
			int skipToPosition = skipCommentsAndQuotes(statement, i);
			if (i != skipToPosition) {
				if (skipToPosition >= statement.length) {
					break;
				}
				i = skipToPosition;
			}
			char c = statement[i];
			if (c == ':' || c == '&') {
				int j = i + 1;
				if (j < statement.length && statement[j] == ':' && c == ':') {
					// Postgres-style "::" casting operator - to be skipped.
					i = i + 2;
					continue;
				}
				while (j < statement.length && !isParameterSeparator(statement[j])) {
					j++;
				}
				if (j - i > 1) {
					String parameter = sql.substring(i + 1, j);
					if (!namedParameters.contains(parameter)) {
						namedParameters.add(parameter);
						namedParameterCount++;
					}
					parsedSql.addNamedParameter(parameter, i, j);
					totalParameterCount++;
				}
				i = j - 1;
			}
			else {
				if (c == '?') {
					unnamedParameterCount++;
					totalParameterCount++;
				}
			}
			i++;
		}
		parsedSql.setNamedParameterCount(namedParameterCount);
		parsedSql.setUnnamedParameterCount(unnamedParameterCount);
		parsedSql.setTotalParameterCount(totalParameterCount);
		return parsedSql;
	}
{
		
		Map<String, String> uriVariables = getUriTemplateVariables(request);
		
		if (uriVariables.containsKey(attributeName)) {
			DataBinder binder = binderFactory.createBinder(request, null, attributeName);
			return binder.convertIfNecessary(uriVariables.get(attributeName), parameter.getParameterType());
		}
		
		return super.createAttribute(attributeName, parameter, binderFactory, request);
	}
{
		Map<String, String> uriTemplateVars = 
			(Map<String, String>) request.getAttribute(
					HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);

		if (uriTemplateVars != null && uriTemplateVars.containsKey(attributeName)) {
			try {
				String var = uriTemplateVars.get(attributeName);
				DataBinder binder = binderFactory.createBinder(request, null, attributeName);
				return binder.convertIfNecessary(var, parameter.getParameterType());

			} catch (Exception exception) {
				logger.info("Model attribute '" + attributeName + "' matches to a URI template variable name. "
						+ "The URI template variable however couldn't converted to a model attribute instance: "
						+ exception.getMessage());
			}
		}
		
		return super.createAttribute(attributeName, parameter, binderFactory, request);
	}
{
		Assert.notNull(uriVariables, "'uriVariables' must not be null");

		String expandedScheme = expandUriComponent(this.scheme, uriVariables);
		String expandedUserInfo = expandUriComponent(this.userInfo, uriVariables);
		String expandedHost = expandUriComponent(this.host, uriVariables);
		PathComponent expandedPath = path.expand(uriVariables);
		MultiValueMap<String, String> expandedQueryParams =
				new LinkedMultiValueMap<String, String>(this.queryParams.size());
		for (Map.Entry<String, List<String>> entry : this.queryParams.entrySet()) {
			String expandedName = expandUriComponent(entry.getKey(), uriVariables);
			List<String> expandedValues = new ArrayList<String>(entry.getValue().size());
			for (String value : entry.getValue()) {
				String expandedValue = expandUriComponent(value, uriVariables);
				expandedValues.add(expandedValue);
			}
			expandedQueryParams.put(expandedName, expandedValues);
		}
		String expandedFragment = expandUriComponent(this.fragment, uriVariables);

		return new UriComponents(expandedScheme, expandedUserInfo, expandedHost, this.port, expandedPath,
				expandedQueryParams, expandedFragment, false);
	}
{
		FlashMap flashMap = (FlashMap) request.getAttribute(OUTPUT_FLASH_MAP_ATTRIBUTE);
		if (flashMap == null) {
			throw new IllegalStateException(
					"Did not find a FlashMap exposed as the request attribute " + OUTPUT_FLASH_MAP_ATTRIBUTE);
		}
		if (!flashMap.isEmpty() && flashMap.isCreatedBy(this.hashCode())) {
			if (logger.isDebugEnabled()) {
				logger.debug("Saving FlashMap=" + flashMap);
			}
			decodeAndNormalizeTargetPath(flashMap, request);
			flashMap.startExpirationPeriod(this.flashTimeout);
			retrieveFlashMaps(request, true).add(flashMap);
		}
	}
{
		RequestPart annot = param.getParameterAnnotation(RequestPart.class);
		boolean isRequired = (annot != null) ? annot.required() : true;
		if (isRequired) {
			String paramType = param.getParameterType().getName();
			throw new ServletRequestBindingException(
					"Missing request part '" + partName + "' for method parameter type [" + paramType + "]");
		}
	}
{

		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		HttpEntity<TestData> jsonEntity = new HttpEntity<TestData>(new TestData("Jason"));
		parts.add("json-data", jsonEntity);
		parts.add("file-data", new ClassPathResource("logo.jpg", this.getClass()));

		URI location = restTemplate.postForLocation(baseUrl + "/commons/test", parts);
		assertEquals("http://localhost:8080/test/Jason/logo.jpg", location.toString());
	}
{
		Assert.notNull(uri, "'uri' must not be null");
		Assert.hasLength(encoding, "'encoding' must not be empty");
		Matcher m = URI_PATTERN.matcher(uri);
		if (m.matches()) {
			String scheme = m.group(2);
			String authority = m.group(3);
			String userinfo = m.group(5);
			String host = m.group(6);
			String port = m.group(8);
			String path = m.group(9);
			String query = m.group(11);
			String fragment = m.group(13);

			return encodeUriComponents(scheme, authority, userinfo, host, port, path, query, fragment, encoding);
		}
		else {
			throw new IllegalArgumentException("[" + uri + "] is not a valid URI");
		}
	}
{
		Assert.notNull(httpUrl, "'httpUrl' must not be null");
		Assert.hasLength(encoding, "'encoding' must not be empty");
		Matcher m = HTTP_URL_PATTERN.matcher(httpUrl);
		if (m.matches()) {
			String scheme = m.group(1);
			String authority = m.group(2);
			String userinfo = m.group(4);
			String host = m.group(5);
			String portString = m.group(7);
			String path = m.group(8);
			String query = m.group(10);

			return encodeUriComponents(scheme, authority, userinfo, host, portString, path, query, null, encoding);
		}
		else {
			throw new IllegalArgumentException("[" + httpUrl + "] is not a valid HTTP URL");
		}
	}
{

		Assert.hasLength(encoding, "'encoding' must not be empty");
		StringBuilder sb = new StringBuilder();

		if (scheme != null) {
			sb.append(encodeScheme(scheme, encoding));
			sb.append(':');
		}

		if (userinfo != null || host != null || port != null) {
			sb.append("//");
			if (userinfo != null) {
				sb.append(encodeUserInfo(userinfo, encoding));
				sb.append('@');
			}
			if (host != null) {
				sb.append(encodeHost(host, encoding));
			}
			if (port != null) {
				sb.append(':');
				sb.append(encodePort(port, encoding));
			}
		} else if (authority != null) {
			sb.append("//");
			sb.append(encodeAuthority(authority, encoding));
		}

		if (path != null) {
			sb.append(encodePath(path, encoding));
		}

		if (query != null) {
			sb.append('?');
			sb.append(encodeQuery(query, encoding));
		}

		if (fragment != null) {
			sb.append('#');
			sb.append(encodeFragment(fragment, encoding));
		}

		return sb.toString();
	}
{
		Assert.notNull(uriVariables, "'uriVariables' must not be null");
		Object[] values = new Object[this.variableNames.size()];
		for (int i = 0; i < this.variableNames.size(); i++) {
			String name = this.variableNames.get(i);
			if (!uriVariables.containsKey(name)) {
				throw new IllegalArgumentException("'uriVariables' Map has no value for '" + name + "'");
			}
			values[i] = uriVariables.get(name);
		}
		return expand(values);
	}
{
		Assert.notNull(uriVariableValues, "'uriVariableValues' must not be null");
		if (uriVariableValues.length != this.variableNames.size()) {
			throw new IllegalArgumentException(
					"Invalid amount of variables values in [" + this.uriTemplate + "]: expected " +
							this.variableNames.size() + "; got " + uriVariableValues.length);
		}
		Matcher matcher = NAMES_PATTERN.matcher(this.uriTemplate);
		StringBuffer buffer = new StringBuffer();
		int i = 0;
		while (matcher.find()) {
			Object uriVariable = uriVariableValues[i++];
			String replacement = Matcher.quoteReplacement(uriVariable != null ? uriVariable.toString() : "");
			matcher.appendReplacement(buffer, replacement);
		}
		matcher.appendTail(buffer);
		return encodeUri(buffer.toString());
	}
{
		RequestMapping methodAnnot = AnnotationUtils.findAnnotation(method, RequestMapping.class);
		if (methodAnnot != null) {
			RequestMapping typeAnnot = AnnotationUtils.findAnnotation(handlerType, RequestMapping.class);
			RequestMappingInfo methodInfo = createRequestMappingInfo(methodAnnot, true, method, handlerType);
			if (typeAnnot != null) {
				RequestMappingInfo typeInfo = createRequestMappingInfo(typeAnnot, false, method, handlerType);
				return typeInfo.combine(methodInfo);
			}
			else {
				return methodInfo;
			}
		}
		return null;
	}
{
		String acceptHeader = request.getHeader("Accept");
		List<MediaType> acceptedMediaTypes = MediaType.parseMediaTypes(acceptHeader);
		MediaType.sortByQualityValue(acceptedMediaTypes);

		for (MediaType acceptedMediaType : acceptedMediaTypes) {
			if (acceptedMediaType.equals(MediaType.ALL)) {
				if (isOneEmptyButNotBoth(other)) {
					return this.isEmpty() ? -1 : 1;
				}
			}
			int thisIndex = this.indexOfMediaType(acceptedMediaType);
			int otherIndex = other.indexOfMediaType(acceptedMediaType);
			if (thisIndex != otherIndex) {
				return otherIndex - thisIndex;
			} else if (thisIndex != -1 && otherIndex != -1) {
				ProduceMediaTypeExpression thisExpr = this.expressions.get(thisIndex);
				ProduceMediaTypeExpression otherExpr = other.expressions.get(otherIndex);
				int result = thisExpr.compareTo(otherExpr);
				if (result != 0) {
					return result;
				}
			}
		}
		
		if (acceptedMediaTypes.isEmpty()) {
			if (isOneEmptyButNotBoth(other)) {
				return this.isEmpty() ? -1 : 1;
			}
		}
		
		return 0;
	}
{
		if (request.getAttribute(CURRENT_FLASH_MAP_ATTRIBUTE) != null) {
			return false;
		}

		FlashMap currentFlashMap = new FlashMap();
		request.setAttribute(CURRENT_FLASH_MAP_ATTRIBUTE, currentFlashMap);
		
		FlashMap previousFlashMap = lookupPreviousFlashMap(request);
		if (previousFlashMap != null) {
			WebUtils.exposeRequestAttributes(request, previousFlashMap);
			request.setAttribute(PREVIOUS_FLASH_MAP_ATTRIBUTE, previousFlashMap);
		}
		
		// Remove expired flash maps
		List<FlashMap> allMaps = retrieveFlashMaps(request, false);
		if (allMaps != null && !allMaps.isEmpty()) {
			List<FlashMap> expiredMaps = new ArrayList<FlashMap>();
			for (FlashMap flashMap : allMaps) {
				if (flashMap.isExpired()) {
					if (logger.isDebugEnabled()) {
						logger.debug("Removing expired FlashMap: " + flashMap);
					}
					expiredMaps.add(flashMap);
				}
			}
			allMaps.removeAll(expiredMaps);
		}
		
		return true;
	}
{
		if (request.getAttribute(CURRENT_FLASH_MAP_ATTRIBUTE) != null) {
			return false;
		}

		FlashMap currentFlashMap = new FlashMap();
		request.setAttribute(CURRENT_FLASH_MAP_ATTRIBUTE, currentFlashMap);
		
		FlashMap previousFlashMap = lookupPreviousFlashMap(request);
		if (previousFlashMap != null) {
			WebUtils.exposeRequestAttributes(request, previousFlashMap);
			request.setAttribute(PREVIOUS_FLASH_MAP_ATTRIBUTE, previousFlashMap);
		}
		
		// Remove expired flash maps
		List<FlashMap> allMaps = retrieveFlashMaps(request, false);
		if (allMaps != null && !allMaps.isEmpty()) {
			List<FlashMap> expiredMaps = new ArrayList<FlashMap>();
			for (FlashMap flashMap : allMaps) {
				if (flashMap.isExpired()) {
					if (logger.isDebugEnabled()) {
						logger.debug("Removing expired FlashMap: " + flashMap);
					}
					expiredMaps.add(flashMap);
				}
			}
			allMaps.removeAll(expiredMaps);
		}
		
		return true;
	}
{
		HttpSession session = request.getSession(allowCreate);
		if (session == null) {
			return null;
		} 
		Map<String, FlashMap> result = (Map<String, FlashMap>) session.getAttribute(FLASH_MAPS_SESSION_ATTRIBUTE);
		if (result == null && allowCreate) {
			synchronized (DefaultFlashMapManager.class) {
				result = (Map<String, FlashMap>) session.getAttribute(FLASH_MAPS_SESSION_ATTRIBUTE);
				if (result == null) {
					result = new ConcurrentHashMap<String, FlashMap>(5);
					session.setAttribute(FLASH_MAPS_SESSION_ATTRIBUTE, result);
				}
			}
		}
		return result;
	}
{
			if (useTypeLevelMapping(request)) {
				String[] typeLevelPatterns = getTypeLevelMapping().value();
				for (String typeLevelPattern : typeLevelPatterns) {
					if (!typeLevelPattern.startsWith("/")) {
						typeLevelPattern = "/" + typeLevelPattern;
					}
					String combinedPattern = pathMatcher.combine(typeLevelPattern, methodLevelPattern);
					if (isPathMatchInternal(combinedPattern, lookupPath)) {
						return combinedPattern;
					}
				}
				return null;
			}
			String bestMatchingPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
			if (StringUtils.hasText(bestMatchingPattern) && bestMatchingPattern.endsWith("*")) {
				String combinedPattern = pathMatcher.combine(bestMatchingPattern, methodLevelPattern);
				if (!combinedPattern.equals(bestMatchingPattern) &&
						(isPathMatchInternal(combinedPattern, lookupPath))) {
					return combinedPattern;
				}
			}
			if (isPathMatchInternal(methodLevelPattern, lookupPath)) {
				return methodLevelPattern;
			}
			return null;
		}
{
		boolean triggerExists = (getScheduler().getTrigger(trigger.getName(), trigger.getGroup()) != null);
		if (!triggerExists || this.overwriteExistingJobs) {
			// Check if the Trigger is aware of an associated JobDetail.
			if (trigger instanceof JobDetailAwareTrigger) {
				JobDetail jobDetail = ((JobDetailAwareTrigger) trigger).getJobDetail();
				// Automatically register the JobDetail too.
				if (!this.jobDetails.contains(jobDetail) && addJobToScheduler(jobDetail)) {
					this.jobDetails.add(jobDetail);
				}
			}
			if (!triggerExists) {
				try {
					getScheduler().scheduleJob(trigger);
				}
				catch (ObjectAlreadyExistsException ex) {
					if (logger.isDebugEnabled()) {
						logger.debug("Unexpectedly found existing trigger, assumably due to cluster race condition: " +
								ex.getMessage() + " - can safely be ignored");
					}
					if (this.overwriteExistingJobs) {
						getScheduler().rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
					}
				}
			}
			else {
				getScheduler().rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
			}
			return true;
		}
		else {
			return false;
		}
	}
{
		processConfigBeanDefinitions(registry);
		enhanceConfigurationClasses((ConfigurableListableBeanFactory)registry);
	}
{
		processConfigBeanDefinitions(registry);
		enhanceConfigurationClasses((ConfigurableListableBeanFactory)registry);
	}
{
		return (clazz != null && isCglibProxyClassName(clazz.getName()));
	}
{
		return (className != null && className.contains(ClassUtils.CGLIB_CLASS_SEPARATOR));
	}
{
		if (logger.isTraceEnabled()) {
			logger.trace("Rendering view with name '" + this.beanName + "' with model " + model +
				" and static attributes " + this.staticAttributes);
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Object> pathVars = this.exposePathVariables ?
			(Map<String, Object>) request.getAttribute(View.PATH_VARIABLES) : null;

		// Consolidate static and dynamic model attributes.
		int size = this.staticAttributes.size();
		size += (model != null) ? model.size() : 0;
		size += (pathVars != null) ? pathVars.size() : 0;
		Map<String, Object> mergedModel = new HashMap<String, Object>(size);
		mergedModel.putAll(this.staticAttributes);
		if (pathVars != null) {
			mergedModel.putAll(pathVars);
		}
		if (model != null) {
			mergedModel.putAll(model);
		}

		// Expose RequestContext?
		if (this.requestContextAttribute != null) {
			mergedModel.put(this.requestContextAttribute, createRequestContext(request, response, mergedModel));
		}

		prepareResponse(request, response);
		renderMergedOutputModel(mergedModel, request, response);
	}
{

		// Prepare target URL.
		StringBuilder targetUrl = new StringBuilder();
		if (this.contextRelative && getUrl().startsWith("/")) {
			// Do not apply context path to relative URLs.
			targetUrl.append(request.getContextPath());
		}
		targetUrl.append(getUrl());

		String enc = this.encodingScheme;
		if (enc == null) {
			enc = request.getCharacterEncoding();
		}
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}

		UriTemplate redirectUri = createUriTemplate(targetUrl, enc);
		if (redirectUri.getVariableNames().size() > 0) {
			targetUrl = new StringBuilder(redirectUri.expand(model).toString());
			model = removeKeys(model, redirectUri.getVariableNames());
		}
		if (this.exposeModelAttributes) {
			List<String> pathVarNames = getPathVarNames(request);
			if (!pathVarNames.isEmpty()) {
				model = removeKeys(model, pathVarNames);
			}
			appendQueryProperties(targetUrl, model, enc);
		}

		sendRedirect(request, response, targetUrl.toString(), this.http10Compatible);
	}
{
		Throwable targetException = ex.getTargetException();
		if (targetException instanceof RuntimeException) {
			throw (RuntimeException) targetException;
		}
		if (targetException instanceof Error) {
			throw (Error) targetException;
		}
		if (targetException instanceof Exception) {
			throw (Exception) targetException;
		}
	}
{
		MediaType contentType = MediaType.TEXT_PLAIN;
		servletRequest.addHeader("Content-Type", contentType.toString());

		HttpMessageConverter<ValidBean> beanConverter = createMock(HttpMessageConverter.class);
		expect(beanConverter.getSupportedMediaTypes()).andReturn(Collections.singletonList(MediaType.TEXT_PLAIN));
		expect(beanConverter.canRead(ValidBean.class, contentType)).andReturn(true);
		expect(beanConverter.read(eq(ValidBean.class), isA(HttpInputMessage.class))).andReturn(new ValidBean(null));
		replay(beanConverter);

		processor = new RequestResponseBodyMethodProcessor(Collections.<HttpMessageConverter<?>>singletonList(beanConverter));
		try {
			processor.resolveArgument(paramValidBean, mavContainer, webRequest, new ValidatingBinderFactory());
			fail("Expected exception");
		} catch (RequestBodyNotValidException e) {
			assertEquals("validBean", e.getErrors().getObjectName());
			assertEquals(1, e.getErrors().getErrorCount());
			assertNotNull(e.getErrors().getFieldError("name"));
		}
	}
{
		PointcutParser parser = initializePointcutParser();
		PointcutParameter[] pointcutParameters = new PointcutParameter[this.pointcutParameterNames.length];
		for (int i = 0; i < pointcutParameters.length; i++) {
			pointcutParameters[i] = parser.createPointcutParameter(
					this.pointcutParameterNames[i], this.pointcutParameterTypes[i]);
		}
		return parser.parsePointcutExpression(
				replaceBooleanOperators(getExpression()), this.pointcutDeclarationScope, pointcutParameters);
	}
{
		return key.startsWith(BindingResult.MODEL_KEY_PREFIX);
	}
{
		Assert.notNull(contextLoader, "ContextLoader must not be null");
		Assert.notNull(clazz, "Class must not be null");

		boolean processConfigurationClasses = (contextLoader instanceof ResourceTypeAwareContextLoader)
				&& ResourceType.CLASSES == ((ResourceTypeAwareContextLoader) contextLoader).getResourceType();
		LocationsResolver locationsResolver = processConfigurationClasses ? classNameLocationsResolver
				: resourcePathLocationsResolver;

		Class<ContextConfiguration> annotationType = ContextConfiguration.class;
		Class<?> declaringClass = AnnotationUtils.findAnnotationDeclaringClass(annotationType, clazz);
		Assert.notNull(declaringClass, String.format(
			"Could not find an 'annotation declaring class' for annotation type [%s] and class [%s]", annotationType,
			clazz));

		final List<String> locationsList = new ArrayList<String>();

		while (declaringClass != null) {
			ContextConfiguration contextConfiguration = declaringClass.getAnnotation(annotationType);

			if (logger.isTraceEnabled()) {
				logger.trace(String.format("Retrieved @ContextConfiguration [%s] for declaring class [%s].",
					contextConfiguration, declaringClass));
			}

			String[] resolvedLocations = locationsResolver.resolveLocations(contextConfiguration, declaringClass);
			String[] processedLocations = contextLoader.processLocations(declaringClass, resolvedLocations);
			locationsList.addAll(0, Arrays.asList(processedLocations));

			declaringClass = contextConfiguration.inheritLocations() ? AnnotationUtils.findAnnotationDeclaringClass(
				annotationType, declaringClass.getSuperclass()) : null;
		}

		return StringUtils.toStringArray(locationsList);
	}
{
		createBeanDefinitionReader(context).loadBeanDefinitions(locations);
	}
{
		if (isEmpty()) {
			return this;
		}
		Set<ConsumeRequestCondition> matchingConditions = new LinkedHashSet<ConsumeRequestCondition>(getConditions());
		for (Iterator<ConsumeRequestCondition> iterator = matchingConditions.iterator(); iterator.hasNext();) {
			ConsumeRequestCondition condition = iterator.next();
			if (!condition.match(request)) {
				iterator.remove();
			}
		}
		if (matchingConditions.isEmpty()) {
			return null;
		}
		else {
			return new ConsumesRequestCondition(matchingConditions);
		}
	}
{
		if (isEmpty()) {
			return this;
		}
		Set<ProduceRequestCondition> matchingConditions = new LinkedHashSet<ProduceRequestCondition>(getConditions());
		for (Iterator<ProduceRequestCondition> iterator = matchingConditions.iterator(); iterator.hasNext();) {
			ProduceRequestCondition condition = iterator.next();
			if (!condition.match(request)) {
				iterator.remove();
			}
		}
		if (matchingConditions.isEmpty()) {
			return null;
		}
		else {
			return new ProducesRequestCondition(matchingConditions);
		}
	}
{
		Map<String, HttpRequestHandler> urlMap = new HashMap<String, HttpRequestHandler>();
		if (requestHandler != null) {
			urlMap.put("/**", requestHandler);
		}
		return urlMap ;
	}
{
		ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver = new ExceptionHandlerExceptionResolver();
		exceptionHandlerExceptionResolver.setMessageConverters(getMessageConverters());
		exceptionHandlerExceptionResolver.afterPropertiesSet();

		List<HandlerExceptionResolver> exceptionResolvers = new ArrayList<HandlerExceptionResolver>();
		exceptionResolvers.add(exceptionHandlerExceptionResolver);
		exceptionResolvers.add(new ResponseStatusExceptionResolver());
		exceptionResolvers.add(new DefaultHandlerExceptionResolver());

		HandlerExceptionResolverComposite composite = new HandlerExceptionResolverComposite();
		composite.setOrder(0);
		composite.setExceptionResolvers(exceptionResolvers);
		return composite;
	}
{
		return true;
	}
{
		LoadTimeWeaver weaverToUse = this.loadTimeWeaver;
		if (weaverToUse == null) {
			if (InstrumentationLoadTimeWeaver.isInstrumentationAvailable()) {
				weaverToUse = new InstrumentationLoadTimeWeaver(this.beanClassLoader);
			}
			else {
				throw new IllegalStateException("No LoadTimeWeaver available");
			}
		}
		weaverToUse.addTransformer(new AspectJClassBypassingClassFileTransformer(
					new ClassPreProcessorAgentAdapter()));
	}
{
		if (this.enabled) {
			Assert.state(this.dataSource != null, "DataSource must be provided");
			Assert.state(this.databasePopulator != null, "DatabasePopulator must be provided");
			try {
				Connection connection = this.dataSource.getConnection();
				try {
					this.databasePopulator.populate(connection);
				}
				finally {
					try {
						connection.close();
					}
					catch (SQLException ex) {
						// ignore
					}
				}
			}
			catch (Exception ex) {
				throw new DataAccessResourceFailureException("Failed to populate database", ex);
			}
		}
	}
{
		if (!isCollection() && !isArray()) {
			throw new IllegalStateException("Not a java.util.Collection or Array");
		}
		return this.elementType;
	}
{
		assertNotNull(sourceType, targetType);
		if (logger.isDebugEnabled()) {
			logger.debug("Converting value " + StylerUtils.style(source) + " of " + sourceType + " to " + targetType);
		}
		if (sourceType == TypeDescriptor.NULL) {
			Assert.isTrue(source == null, "The value must be null if sourceType == TypeDescriptor.NULL");
			Object result = convertNullSource(sourceType, targetType);
			if (result == null) {
				assertNotPrimitiveTargetType(sourceType, targetType);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Converted to " + StylerUtils.style(result));
			}			
			return result;
		}
		if (targetType == TypeDescriptor.NULL) {
			logger.debug("Converted to null");
			return null;
		}
		Assert.isTrue(source == null || sourceType.getObjectType().isInstance(source));
		GenericConverter converter = getConverter(sourceType, targetType);
		if (converter == null) {
			return handleConverterNotFound(source, sourceType, targetType);
		}
		Object result = ConversionUtils.invokeConverter(converter, source, sourceType, targetType);
		if (result == null) {
			assertNotPrimitiveTargetType(sourceType, targetType);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Converted to " + StylerUtils.style(result));
		}
		return result;
	}
{

		try {
			for (String dataSourceName : dataSources) {
				DataSource dataSource = context.getBean(dataSourceName, DataSource.class);
				JdbcTemplate t = new JdbcTemplate(dataSource);
				assertEquals(1, t.queryForInt("select count(*) from T_TEST"));
			}
		} finally {
			context.close();
		}
	}
{
		assertEquals("Keith", jdbcTemplate.queryForObject("select NAME from T_TEST", String.class));
	}
{
		Class<?> contextClass = determineContextClass(sc);
		if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass)) {
			throw new ApplicationContextException("Custom context class [" + contextClass.getName() +
					"] is not of type [" + ConfigurableWebApplicationContext.class.getName() + "]");
		}
		ConfigurableWebApplicationContext wac =
				(ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);

		// Assign the best possible id value.
		if (sc.getMajorVersion() == 2 && sc.getMinorVersion() < 5) {
			// Servlet <= 2.4: resort to name specified in web.xml, if any.
			String servletContextName = sc.getServletContextName();
			wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX +
					ObjectUtils.getDisplayString(servletContextName));
		}
		else {
			// Servlet 2.5's getContextPath available!
			try {
				String contextPath = (String) ServletContext.class.getMethod("getContextPath").invoke(sc);
				wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX +
						ObjectUtils.getDisplayString(contextPath));
			}
			catch (Exception ex) {
				throw new IllegalStateException("Failed to invoke Servlet 2.5 getContextPath method", ex);
			}
		}

		wac.setParent(parent);
		wac.setServletContext(sc);
		wac.setConfigLocation(sc.getInitParameter(CONFIG_LOCATION_PARAM));
		customizeContext(sc, wac);
		wac.refresh();
		return wac;
	}
{
		if (servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null) {
			throw new IllegalStateException(
					"Cannot initialize context because there is already a root application context present - " +
					"check whether you have multiple ContextLoader* definitions in your web.xml!");
		}

		Log logger = LogFactory.getLog(ContextLoader.class);
		servletContext.log("Initializing Spring root WebApplicationContext");
		if (logger.isInfoEnabled()) {
			logger.info("Root WebApplicationContext: initialization started");
		}
		long startTime = System.currentTimeMillis();

		try {
			// Determine parent for root web application context, if any.
			ApplicationContext parent = loadParentContext(servletContext);

			// Store context in local instance variable, to guarantee that
			// it is available on ServletContext shutdown.
			this.context = createWebApplicationContext(servletContext, parent);
			servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);

			ClassLoader ccl = Thread.currentThread().getContextClassLoader();
			if (ccl == ContextLoader.class.getClassLoader()) {
				currentContext = this.context;
			}
			else if (ccl != null) {
				currentContextPerThread.put(ccl, this.context);
			}

			if (logger.isDebugEnabled()) {
				logger.debug("Published root WebApplicationContext as ServletContext attribute with name [" +
						WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE + "]");
			}
			if (logger.isInfoEnabled()) {
				long elapsedTime = System.currentTimeMillis() - startTime;
				logger.info("Root WebApplicationContext: initialization completed in " + elapsedTime + " ms");
			}

			return this.context;
		}
		catch (RuntimeException ex) {
			logger.error("Context initialization failed", ex);
			servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ex);
			throw ex;
		}
		catch (Error err) {
			logger.error("Context initialization failed", err);
			servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, err);
			throw err;
		}
	}
{
		Class<?> contextClass = getContextClass();
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Servlet with name '" + getServletName() +
					"' will try to create custom WebApplicationContext context of class '" +
					contextClass.getName() + "'" + ", using parent context [" + parent + "]");
		}
		if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass)) {
			throw new ApplicationContextException(
					"Fatal initialization error in servlet with name '" + getServletName() +
					"': custom WebApplicationContext class [" + contextClass.getName() +
					"] is not of type ConfigurableWebApplicationContext");
		}
		ConfigurableWebApplicationContext wac =
				(ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);

		// Assign the best possible id value.
		ServletContext sc = getServletContext();
		if (sc.getMajorVersion() == 2 && sc.getMinorVersion() < 5) {
			// Servlet <= 2.4: resort to name specified in web.xml, if any.
			String servletContextName = sc.getServletContextName();
			if (servletContextName != null) {
				wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX + servletContextName +
						"." + getServletName());
			}
			else {
				wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX + getServletName());
			}
		}
		else {
			// Servlet 2.5's getContextPath available!
			wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX + sc.getContextPath() +
					"/" + getServletName());
		}

		wac.setParent(parent);
		wac.setServletContext(getServletContext());
		wac.setServletConfig(getServletConfig());
		wac.setNamespace(getNamespace());
		wac.setConfigLocation(getContextConfigLocation());
		wac.addApplicationListener(new SourceFilteringListener(wac, new ContextRefreshListener()));

		postProcessWebApplicationContext(wac);

		initializeWebApplicationContext(wac);

		wac.refresh();

		return wac;
	}
{
		assertNotNull(sourceType, targetType);
		if (logger.isDebugEnabled()) {
			logger.debug("Converting value " + StylerUtils.style(source) + " of " + sourceType + " to " + targetType);
		}
		if (sourceType == TypeDescriptor.NULL) {
			Assert.isTrue(source == null, "The value must be null if sourceType == TypeDescriptor.NULL");
			Object result = convertNullSource(sourceType, targetType);
			if (logger.isDebugEnabled()) {
				logger.debug("Converted to " + StylerUtils.style(result));
			}			
			return result;
		}
		if (targetType == TypeDescriptor.NULL) {
			logger.debug("Converted to null");
			return null;
		}
		Assert.isTrue(source == null || sourceType.getObjectType().isInstance(source));
		GenericConverter converter = getConverter(sourceType, targetType);
		if (converter == null) {
			if (source == null || sourceType.isAssignableTo(targetType)) {
				logger.debug("No converter found - returning assignable source object as-is");
				return source;
			}
			else {
				throw new ConverterNotFoundException(sourceType, targetType);
			}
		}
		Object result = ConversionUtils.invokeConverter(converter, source, sourceType, targetType);
		if (logger.isDebugEnabled()) {
			logger.debug("Converted to " + StylerUtils.style(result));
		}
		return result;
	}
{
		AnnotationMetadata metadata = null;

		// Check already loaded Class if present...
		// since we possibly can't even load the class file for this Class.
		if (beanDef instanceof AbstractBeanDefinition && ((AbstractBeanDefinition) beanDef).hasBeanClass()) {
			metadata = new StandardAnnotationMetadata(((AbstractBeanDefinition) beanDef).getBeanClass());
		}
		else {
			String className = beanDef.getBeanClassName();
			if (className != null) {
				try {
					MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(className);
					metadata = metadataReader.getAnnotationMetadata();
				}
				catch (IOException ex) {
					if (logger.isDebugEnabled()) {
						logger.debug("Could not find class file for introspecting factory methods: " + className, ex);
					}
					return false;
				}
			}
		}

		if (metadata != null) {
			if (metadata.isAnnotated(Configuration.class.getName())) {
				beanDef.setAttribute(CONFIGURATION_CLASS_ATTRIBUTE, CONFIGURATION_CLASS_FULL);
				return true;
			}
			else if (metadata.isAnnotated(Component.class.getName()) ||
					metadata.hasAnnotatedMethods(Bean.class.getName())) {
				beanDef.setAttribute(CONFIGURATION_CLASS_ATTRIBUTE, CONFIGURATION_CLASS_LITE);
				return true;
			}
		}
		return false;
	}
{
		AnnotationMetadata metadata = null;

		// Check already loaded Class if present...
		// since we possibly can't even load the class file for this Class.
		if (beanDef instanceof AbstractBeanDefinition && ((AbstractBeanDefinition) beanDef).hasBeanClass()) {
			metadata = new StandardAnnotationMetadata(((AbstractBeanDefinition) beanDef).getBeanClass());
		}
		else {
			String className = beanDef.getBeanClassName();
			if (className != null) {
				try {
					MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(className);
					metadata = metadataReader.getAnnotationMetadata();
				}
				catch (IOException ex) {
					if (logger.isDebugEnabled()) {
						logger.debug("Could not find class file for introspecting factory methods: " + className, ex);
					}
					return false;
				}
			}
		}

		if (metadata != null) {
			if (metadata.isAnnotated(Configuration.class.getName())) {
				beanDef.setAttribute(CONFIGURATION_CLASS_ATTRIBUTE, CONFIGURATION_CLASS_FULL);
				return true;
			}
			else if (metadata.isAnnotated(Component.class.getName()) ||
					metadata.hasAnnotatedMethods(Bean.class.getName())) {
				beanDef.setAttribute(CONFIGURATION_CLASS_ATTRIBUTE, CONFIGURATION_CLASS_LITE);
				return true;
			}
		}
		return false;
	}
{
		RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();

		ConfigurableWebBindingInitializer bindingInitializer = new ConfigurableWebBindingInitializer();
		bindingInitializer.setConversionService(conversionService());
		bindingInitializer.setValidator(validator());
		adapter.setWebBindingInitializer(bindingInitializer);

		List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
		configurers.addArgumentResolvers(argumentResolvers);
		adapter.setCustomArgumentResolvers(argumentResolvers);

		List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<HandlerMethodReturnValueHandler>();
		configurers.addReturnValueHandlers(returnValueHandlers);
		adapter.setCustomReturnValueHandlers(returnValueHandlers);

		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		configurers.configureMessageConverters(converters);
		if (converters.size() == 0) {
			addDefaultHttpMessageConverters(converters);
		}
		adapter.setMessageConverters(converters);

		return adapter;
	}
{
		List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
		if (acceptedMediaTypes.isEmpty()) {
			acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
		}

		MediaType.sortByQualityValue(acceptedMediaTypes);
		return acceptedMediaTypes;
	}
{
		
		List<MediaType> acceptedMediaTypes = getAcceptedMediaTypes(inputMessage);

		List<MediaType> allSupportedMediaTypes = new ArrayList<MediaType>();
		if (this.messageConverters != null) {
			for (MediaType acceptedMediaType : acceptedMediaTypes) {
				for (HttpMessageConverter<?> messageConverter : this.messageConverters) {
					if (!messageConverter.canWrite(returnValue.getClass(), acceptedMediaType)) {
						continue;
					}
					((HttpMessageConverter<T>) messageConverter).write(returnValue, acceptedMediaType, outputMessage);
					if (logger.isDebugEnabled()) {
						MediaType contentType = outputMessage.getHeaders().getContentType();
						if (contentType == null) {
							contentType = acceptedMediaType;
						}
						logger.debug("Written [" + returnValue + "] as \"" + contentType + "\" using [" +
								messageConverter + "]");
					}
					return;
				}
			}
			for (HttpMessageConverter<?> messageConverter : messageConverters) {
				allSupportedMediaTypes.addAll(messageConverter.getSupportedMediaTypes());
			}
		}
		throw new HttpMediaTypeNotAcceptableException(allSupportedMediaTypes);
	}
{
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(appContext);
		reader.loadBeanDefinitions(new ClassPathResource("mvc-config.xml", getClass()));
		assertEquals(8, appContext.getBeanDefinitionCount());
		appContext.refresh();

		RequestMappingHandlerMapping mapping = appContext.getBean(RequestMappingHandlerMapping.class);
		assertNotNull(mapping);
		assertEquals(0, mapping.getOrder());
		mapping.setDefaultHandler(handlerMethod);
		
		RequestMappingHandlerAdapter adapter = appContext.getBean(RequestMappingHandlerAdapter.class);
		assertNotNull(adapter);
		
		List<HttpMessageConverter<?>> messageConverters = adapter.getMessageConverters();
		assertTrue(messageConverters.size() > 0);

		assertNotNull(appContext.getBean(FormattingConversionServiceFactoryBean.class));
		assertNotNull(appContext.getBean(ConversionService.class));
		assertNotNull(appContext.getBean(LocalValidatorFactoryBean.class));
		assertNotNull(appContext.getBean(Validator.class));

		// default web binding initializer behavior test
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
		request.addParameter("date", "2009-10-31");
		MockHttpServletResponse response = new MockHttpServletResponse();

		HandlerExecutionChain chain = mapping.getHandler(request);
		assertEquals(1, chain.getInterceptors().length);
		assertTrue(chain.getInterceptors()[0] instanceof ConversionServiceExposingInterceptor);
		ConversionServiceExposingInterceptor interceptor = (ConversionServiceExposingInterceptor) chain.getInterceptors()[0];
		interceptor.preHandle(request, response, handlerMethod);
		assertSame(appContext.getBean(ConversionService.class), request.getAttribute(ConversionService.class.getName()));
		
		adapter.handle(request, response, handlerMethod);
		assertTrue(handler.recordedValidationError);
	}
{
		String generatedName = generateBeanName(definition, registry, false);
		registry.registerBeanDefinition(generatedName, definition);
		return generatedName;
	}
{
		if (argumentResolvers == null) {
			argumentResolvers = new HandlerMethodArgumentResolverComposite();
			argumentResolvers.addResolvers(customArgumentResolvers);
			argumentResolvers.addResolvers(getDefaultArgumentResolvers(messageConverters, beanFactory));
		}
		
		if (returnValueHandlers == null) {
			returnValueHandlers = new HandlerMethodReturnValueHandlerComposite();
			returnValueHandlers.addHandlers(customReturnValueHandlers);
			returnValueHandlers.addHandlers(getDefaultReturnValueHandlers(messageConverters, modelAndViewResolvers));
		}
		
		if (initBinderArgumentResolvers == null) {
			initBinderArgumentResolvers = new HandlerMethodArgumentResolverComposite();
			initBinderArgumentResolvers.addResolvers(customArgumentResolvers);
			initBinderArgumentResolvers.addResolvers(getDefaultInitBinderArgumentResolvers(beanFactory));
		}
	}
{
		if (argumentResolvers == null) {
			argumentResolvers = new HandlerMethodArgumentResolverComposite();
			argumentResolvers.addResolvers(customArgumentResolvers);
			argumentResolvers.addResolvers(getDefaultArgumentResolvers(messageConverters, beanFactory));
		}
		
		if (returnValueHandlers == null) {
			returnValueHandlers = new HandlerMethodReturnValueHandlerComposite();
			returnValueHandlers.addHandlers(customReturnValueHandlers);
			returnValueHandlers.addHandlers(getDefaultReturnValueHandlers(messageConverters, modelAndViewResolvers));
		}
		
		if (initBinderArgumentResolvers == null) {
			initBinderArgumentResolvers = new HandlerMethodArgumentResolverComposite();
			initBinderArgumentResolvers.addResolvers(customArgumentResolvers);
			initBinderArgumentResolvers.addResolvers(getDefaultInitBinderArgumentResolvers(beanFactory));
		}
	}
{
		if (argumentResolvers == null) {
			argumentResolvers = new HandlerMethodArgumentResolverComposite();
			argumentResolvers.addResolvers(customArgumentResolvers);
			argumentResolvers.addResolvers(getDefaultArgumentResolvers(messageConverters, beanFactory));
		}
		
		if (returnValueHandlers == null) {
			returnValueHandlers = new HandlerMethodReturnValueHandlerComposite();
			returnValueHandlers.addHandlers(customReturnValueHandlers);
			returnValueHandlers.addHandlers(getDefaultReturnValueHandlers(messageConverters, modelAndViewResolvers));
		}
		
		if (initBinderArgumentResolvers == null) {
			initBinderArgumentResolvers = new HandlerMethodArgumentResolverComposite();
			initBinderArgumentResolvers.addResolvers(customArgumentResolvers);
			initBinderArgumentResolvers.addResolvers(getDefaultInitBinderArgumentResolvers(beanFactory));
		}
	}
{
		NamedValueInfo result = namedValueInfoCache.get(parameter);
		if (result == null) {
			NamedValueInfo info = createNamedValueInfo(parameter);
			String name = info.name;
			if (name.length() == 0) {
				name = parameter.getParameterName();
				if (name == null) {
					throw new IllegalStateException("No parameter name specified for argument of type [" +
							parameter.getParameterType().getName() +
							"], and no parameter name information found in class file either.");
				}
			}
			boolean required = info.required;
			String defaultValue = (ValueConstants.DEFAULT_NONE.equals(info.defaultValue) ? null : info.defaultValue);

			result = new NamedValueInfo(name, required, defaultValue);
			namedValueInfoCache.put(parameter, result);
		}
		return result;
	}
{
		if (argumentResolvers != null) {
			return;
		}
		argumentResolvers = new HandlerMethodArgumentResolverComposite();
		
		// Annotation-based resolvers
		argumentResolvers.registerArgumentResolver(new RequestParamMethodArgumentResolver(beanFactory, false));
		argumentResolvers.registerArgumentResolver(new RequestParamMapMethodArgumentResolver());
		argumentResolvers.registerArgumentResolver(new PathVariableMethodArgumentResolver(beanFactory));
		argumentResolvers.registerArgumentResolver(new ServletModelAttributeMethodProcessor(false));
		argumentResolvers.registerArgumentResolver(new RequestResponseBodyMethodProcessor(messageConverters));
		argumentResolvers.registerArgumentResolver(new RequestHeaderMethodArgumentResolver(beanFactory));
		argumentResolvers.registerArgumentResolver(new RequestHeaderMapMethodArgumentResolver());
		argumentResolvers.registerArgumentResolver(new ServletCookieValueMethodArgumentResolver(beanFactory));
		argumentResolvers.registerArgumentResolver(new ExpressionValueMethodArgumentResolver(beanFactory));

		if (customArgumentResolvers != null) {
			for (WebArgumentResolver customResolver : customArgumentResolvers) {
				argumentResolvers.registerArgumentResolver(new ServletWebArgumentResolverAdapter(customResolver));
			}
		}
		
		// Type-based resolvers
		argumentResolvers.registerArgumentResolver(new ServletRequestMethodArgumentResolver());
		argumentResolvers.registerArgumentResolver(new ServletResponseMethodArgumentResolver());
		argumentResolvers.registerArgumentResolver(new HttpEntityMethodProcessor(messageConverters));
		argumentResolvers.registerArgumentResolver(new ModelMethodProcessor());
		argumentResolvers.registerArgumentResolver(new ErrorsMethodArgumentResolver());
		
		// Default-mode resolution
		argumentResolvers.registerArgumentResolver(new RequestParamMethodArgumentResolver(beanFactory, true));
		argumentResolvers.registerArgumentResolver(new ServletModelAttributeMethodProcessor(true));
	}
{
		if (argumentResolvers != null) {
			return;
		}
		argumentResolvers = new HandlerMethodArgumentResolverComposite();
		
		argumentResolvers.registerArgumentResolver(new ServletRequestMethodArgumentResolver());
		argumentResolvers.registerArgumentResolver(new ServletResponseMethodArgumentResolver());

		if (customArgumentResolvers != null) {
			for (WebArgumentResolver customResolver : customArgumentResolvers) {
				argumentResolvers.registerArgumentResolver(new ServletWebArgumentResolverAdapter(customResolver));
			}
		}	
	}
{
		try {
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			Object result ;
			if (requestAttributes instanceof NativeWebRequest) {
				result = adaptee.resolveArgument(parameter, (NativeWebRequest) requestAttributes);
			}
			else {
				result = adaptee.resolveArgument(parameter, null);
			}
			if (result == WebArgumentResolver.UNRESOLVED) {
				return false;
			}
			else {
				return ClassUtils.isAssignableValue(parameter.getParameterType(), result);
			}
		}
		catch (Exception ex) {
			// ignore
			return false;
		}
	}
{
		if (customArgumentResolvers != null) {
			for (WebArgumentResolver customResolver : customArgumentResolvers) {
				argumentResolvers.registerArgumentResolver(new WebArgumentResolverAdapter(customResolver));
			}
		}	

		argumentResolvers.registerArgumentResolver(new ServletRequestMethodArgumentResolver());
		argumentResolvers.registerArgumentResolver(new ServletResponseMethodArgumentResolver());

		returnValueHandlers.registerReturnValueHandler(new RequestResponseBodyMethodProcessor(messageConverters));
		returnValueHandlers.registerReturnValueHandler(new ModelAttributeMethodProcessor(false));
		returnValueHandlers.registerReturnValueHandler(new ModelAndViewMethodReturnValueHandler());
		returnValueHandlers.registerReturnValueHandler(new ModelMethodProcessor());
		returnValueHandlers.registerReturnValueHandler(new ViewMethodReturnValueHandler());
		returnValueHandlers.registerReturnValueHandler(new HttpEntityMethodProcessor(messageConverters));
		returnValueHandlers.registerReturnValueHandler(new DefaultMethodReturnValueHandler(customModelAndViewResolvers));
	}
{
		if (customArgumentResolvers != null) {
			for (WebArgumentResolver customResolver : customArgumentResolvers) {
				argumentResolvers.registerArgumentResolver(new WebArgumentResolverAdapter(customResolver));
			}
		}	

		argumentResolvers.registerArgumentResolver(new ServletRequestMethodArgumentResolver());
		argumentResolvers.registerArgumentResolver(new ServletResponseMethodArgumentResolver());

		returnValueHandlers.registerReturnValueHandler(new RequestResponseBodyMethodProcessor(messageConverters));
		returnValueHandlers.registerReturnValueHandler(new ModelAttributeMethodProcessor(false));
		returnValueHandlers.registerReturnValueHandler(new ModelAndViewMethodReturnValueHandler());
		returnValueHandlers.registerReturnValueHandler(new ModelMethodProcessor());
		returnValueHandlers.registerReturnValueHandler(new ViewMethodReturnValueHandler());
		returnValueHandlers.registerReturnValueHandler(new HttpEntityMethodProcessor(messageConverters));
		returnValueHandlers.registerReturnValueHandler(new DefaultMethodReturnValueHandler(customModelAndViewResolvers));
	}
{

		List<String> locationsList = new ArrayList<String>();

		while (declaringClass != null) {
			ContextConfiguration contextConfiguration = declaringClass.getAnnotation(annotationType);
			if (logger.isTraceEnabled()) {
				logger.trace(String.format("Retrieved @ContextConfiguration [%s] for declaring class [%s].",
					contextConfiguration, declaringClass));
			}

			String[] valueLocations = contextConfiguration.value();
			String[] locations = contextConfiguration.locations();
			if (!ObjectUtils.isEmpty(valueLocations) && !ObjectUtils.isEmpty(locations)) {
				String msg = String.format(
					"Test class [%s] has been configured with @ContextConfiguration's 'value' [%s] and 'locations' [%s] attributes. Only one declaration of resource locations is permitted per @ContextConfiguration annotation.",
					declaringClass, ObjectUtils.nullSafeToString(valueLocations),
					ObjectUtils.nullSafeToString(locations));
				logger.error(msg);
				throw new IllegalStateException(msg);
			}
			else if (!ObjectUtils.isEmpty(valueLocations)) {
				locations = valueLocations;
			}

			locations = contextLoader.processLocations(declaringClass, locations);
			locationsList.addAll(0, Arrays.<String> asList(locations));
			declaringClass = contextConfiguration.inheritLocations() ? AnnotationUtils.findAnnotationDeclaringClass(
				annotationType, declaringClass.getSuperclass()) : null;
		}

		return locationsList.toArray(new String[locationsList.size()]);
	}
{
		Assert.notNull(contextLoader, "ContextLoader must not be null");
		Assert.notNull(clazz, "Class must not be null");

		List<String> locationsList = new ArrayList<String>();
		Class<ContextConfiguration> annotationType = ContextConfiguration.class;
		Class<?> declaringClass = AnnotationUtils.findAnnotationDeclaringClass(annotationType, clazz);
		Assert.notNull(declaringClass, "Could not find an 'annotation declaring class' for annotation type ["
				+ annotationType + "] and class [" + clazz + "]");

		// --- configuration class resources ----------------------------

		// TODO [SPR-6184] Implement recursive search for configuration classes.
		// This needs to integrate seamlessly (i.e., analogous yet mutually
		// exclusive) with the existing locations search.
		if ((contextLoader instanceof ResourceTypeAwareContextLoader)
				&& ((ResourceTypeAwareContextLoader) contextLoader).supportsClassResources()) {

			ContextConfiguration cc = declaringClass.getAnnotation(annotationType);
			if (logger.isTraceEnabled()) {
				logger.trace(String.format("Retrieved @ContextConfiguration [%s] for declaring class [%s].", cc,
					declaringClass));
			}

			String[] classNames = null;

			Class<?>[] configClasses = cc.classes();
			if (!ObjectUtils.isEmpty(configClasses)) {
				classNames = new String[configClasses.length];

				for (int i = 0; i < configClasses.length; i++) {
					classNames[i] = configClasses[i].getName();
				}
			}

			return contextLoader.processLocations(declaringClass, classNames);
		}

		// --- location/value resources ---------------------------------

		while (declaringClass != null) {
			ContextConfiguration contextConfiguration = declaringClass.getAnnotation(annotationType);
			if (logger.isTraceEnabled()) {
				logger.trace(String.format("Retrieved @ContextConfiguration [%s] for declaring class [%s].",
					contextConfiguration, declaringClass));
			}

			String[] valueLocations = contextConfiguration.value();
			String[] locations = contextConfiguration.locations();
			if (!ObjectUtils.isEmpty(valueLocations) && !ObjectUtils.isEmpty(locations)) {
				String msg = String.format(
					"Test class [%s] has been configured with @ContextConfiguration's 'value' [%s] and 'locations' [%s] attributes. Only one declaration of resource locations is permitted per @ContextConfiguration annotation.",
					declaringClass, ObjectUtils.nullSafeToString(valueLocations),
					ObjectUtils.nullSafeToString(locations));
				logger.error(msg);
				throw new IllegalStateException(msg);
			}
			else if (!ObjectUtils.isEmpty(valueLocations)) {
				locations = valueLocations;
			}

			locations = contextLoader.processLocations(declaringClass, locations);
			locationsList.addAll(0, Arrays.<String> asList(locations));
			declaringClass = contextConfiguration.inheritLocations() ? AnnotationUtils.findAnnotationDeclaringClass(
				annotationType, declaringClass.getSuperclass()) : null;
		}

		return locationsList.toArray(new String[locationsList.size()]);
	}
{
		Assert.notNull(contextLoader, "ContextLoader must not be null");
		Assert.notNull(clazz, "Class must not be null");

		List<String> locationsList = new ArrayList<String>();
		Class<ContextConfiguration> annotationType = ContextConfiguration.class;
		Class<?> declaringClass = AnnotationUtils.findAnnotationDeclaringClass(annotationType, clazz);
		Assert.notNull(declaringClass, "Could not find an 'annotation declaring class' for annotation type ["
				+ annotationType + "] and class [" + clazz + "]");

		// --- configuration class resources ----------------------------

		// TODO [SPR-6184] Implement recursive search for configuration classes.
		// This needs to integrate seamlessly (i.e., analogous yet mutually
		// exclusive) with the existing locations search.
		if ((contextLoader instanceof ResourceTypeAwareContextLoader)
				&& ((ResourceTypeAwareContextLoader) contextLoader).supportsClassResources()) {

			ContextConfiguration cc = declaringClass.getAnnotation(annotationType);
			if (logger.isTraceEnabled()) {
				logger.trace(String.format("Retrieved @ContextConfiguration [%s] for declaring class [%s].", cc,
					declaringClass));
			}

			String[] classNames = null;

			Class<?>[] configClasses = cc.classes();
			if (!ObjectUtils.isEmpty(configClasses)) {
				classNames = new String[configClasses.length];

				for (int i = 0; i < configClasses.length; i++) {
					classNames[i] = configClasses[i].getName();
				}
			}

			return contextLoader.processLocations(declaringClass, classNames);
		}

		// --- location/value resources ---------------------------------

		while (declaringClass != null) {
			ContextConfiguration contextConfiguration = declaringClass.getAnnotation(annotationType);
			if (logger.isTraceEnabled()) {
				logger.trace(String.format("Retrieved @ContextConfiguration [%s] for declaring class [%s].",
					contextConfiguration, declaringClass));
			}

			String[] valueLocations = contextConfiguration.value();
			String[] locations = contextConfiguration.locations();
			if (!ObjectUtils.isEmpty(valueLocations) && !ObjectUtils.isEmpty(locations)) {
				String msg = String.format(
					"Test class [%s] has been configured with @ContextConfiguration's 'value' [%s] and 'locations' [%s] attributes. Only one declaration of resource locations is permitted per @ContextConfiguration annotation.",
					declaringClass, ObjectUtils.nullSafeToString(valueLocations),
					ObjectUtils.nullSafeToString(locations));
				logger.error(msg);
				throw new IllegalStateException(msg);
			}
			else if (!ObjectUtils.isEmpty(valueLocations)) {
				locations = valueLocations;
			}

			locations = contextLoader.processLocations(declaringClass, locations);
			locationsList.addAll(0, Arrays.<String> asList(locations));
			declaringClass = contextConfiguration.inheritLocations() ? AnnotationUtils.findAnnotationDeclaringClass(
				annotationType, declaringClass.getSuperclass()) : null;
		}

		return locationsList.toArray(new String[locationsList.size()]);
	}
{
		Assert.notNull(contextLoader, "ContextLoader must not be null");
		Assert.notNull(clazz, "Class must not be null");

		List<String> locationsList = new ArrayList<String>();
		Class<ContextConfiguration> annotationType = ContextConfiguration.class;
		Class<?> declaringClass = AnnotationUtils.findAnnotationDeclaringClass(annotationType, clazz);
		Assert.notNull(declaringClass, "Could not find an 'annotation declaring class' for annotation type ["
				+ annotationType + "] and class [" + clazz + "]");

		// --- configuration class resources ----------------------------

		// TODO [SPR-6184] Implement recursive search for configuration classes.
		// This needs to integrate seamlessly (i.e., analogous yet mutually
		// exclusive) with the existing locations search.
		if ((contextLoader instanceof ResourceTypeAwareContextLoader)
				&& ((ResourceTypeAwareContextLoader) contextLoader).supportsClassResources()) {

			ContextConfiguration cc = declaringClass.getAnnotation(annotationType);
			if (logger.isTraceEnabled()) {
				logger.trace(String.format("Retrieved @ContextConfiguration [%s] for declaring class [%s].", cc,
					declaringClass));
			}

			String[] classNames = null;

			Class<?>[] configClasses = cc.classes();
			if (!ObjectUtils.isEmpty(configClasses)) {
				classNames = new String[configClasses.length];

				for (int i = 0; i < configClasses.length; i++) {
					classNames[i] = configClasses[i].getName();
				}
			}

			return contextLoader.processLocations(declaringClass, classNames);
		}

		// --- location/value resources ---------------------------------

		while (declaringClass != null) {
			ContextConfiguration contextConfiguration = declaringClass.getAnnotation(annotationType);
			if (logger.isTraceEnabled()) {
				logger.trace(String.format("Retrieved @ContextConfiguration [%s] for declaring class [%s].",
					contextConfiguration, declaringClass));
			}

			String[] valueLocations = contextConfiguration.value();
			String[] locations = contextConfiguration.locations();
			if (!ObjectUtils.isEmpty(valueLocations) && !ObjectUtils.isEmpty(locations)) {
				String msg = String.format(
					"Test class [%s] has been configured with @ContextConfiguration's 'value' [%s] and 'locations' [%s] attributes. Only one declaration of resource locations is permitted per @ContextConfiguration annotation.",
					declaringClass, ObjectUtils.nullSafeToString(valueLocations),
					ObjectUtils.nullSafeToString(locations));
				logger.error(msg);
				throw new IllegalStateException(msg);
			}
			else if (!ObjectUtils.isEmpty(valueLocations)) {
				locations = valueLocations;
			}

			locations = contextLoader.processLocations(declaringClass, locations);
			locationsList.addAll(0, Arrays.<String> asList(locations));
			declaringClass = contextConfiguration.inheritLocations() ? AnnotationUtils.findAnnotationDeclaringClass(
				annotationType, declaringClass.getSuperclass()) : null;
		}

		return locationsList.toArray(new String[locationsList.size()]);
	}
{
		if (logger.isDebugEnabled()) {
			logger.debug("Loading ApplicationContext for locations [" +
					StringUtils.arrayToCommaDelimitedString(locations) + "].");
		}
		GenericApplicationContext context = new GenericApplicationContext();
		prepareContext(context);
		customizeBeanFactory(context.getDefaultListableBeanFactory());
		createBeanDefinitionReader(context).loadBeanDefinitions(locations);
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		customizeContext(context);
		context.refresh();
		context.registerShutdownHook();
		return context;
	}
{

		String generatedName = generateBeanName(definition, registry, false);
		registry.registerBeanDefinition(generatedName, definition);
		return generatedName;
	}
{
		XmlReaderContext readerContext = parserContext.getReaderContext();

		boolean useDefaultFilters = true;
		if (element.hasAttribute(USE_DEFAULT_FILTERS_ATTRIBUTE)) {
			useDefaultFilters = Boolean.valueOf(element.getAttribute(USE_DEFAULT_FILTERS_ATTRIBUTE));
		}

		// Delegate bean definition registration to scanner class.
		ClassPathBeanDefinitionScanner scanner = createScanner(readerContext, useDefaultFilters);
		scanner.setResourceLoader(readerContext.getResourceLoader());
		scanner.setEnvironment(parserContext.getDelegate().getEnvironment());
		scanner.setBeanDefinitionDefaults(parserContext.getDelegate().getBeanDefinitionDefaults());
		scanner.setAutowireCandidatePatterns(parserContext.getDelegate().getAutowireCandidatePatterns());

		if (element.hasAttribute(RESOURCE_PATTERN_ATTRIBUTE)) {
			scanner.setResourcePattern(element.getAttribute(RESOURCE_PATTERN_ATTRIBUTE));
		}

		try {
			parseBeanNameGenerator(element, scanner);
		}
		catch (Exception ex) {
			readerContext.error(ex.getMessage(), readerContext.extractSource(element), ex.getCause());
		}

		try {
			parseScope(element, scanner);
		}
		catch (Exception ex) {
			readerContext.error(ex.getMessage(), readerContext.extractSource(element), ex.getCause());
		}

		parseTypeFilters(element, scanner, readerContext, parserContext);

		return scanner;
	}
{
		Set<Class<?>> rawFieldTypes = new HashSet<Class<?>>(8);
		rawFieldTypes.add(LocalDate.class);
		rawFieldTypes.add(LocalTime.class);
		rawFieldTypes.add(LocalDateTime.class);
		rawFieldTypes.add(DateTime.class);
		rawFieldTypes.add(DateMidnight.class);
		rawFieldTypes.add(Date.class);
		rawFieldTypes.add(Calendar.class);
		rawFieldTypes.add(Long.class);
		this.fieldTypes = Collections.unmodifiableSet(rawFieldTypes);
	}
{
		this.conversionService = new FormattingConversionService();
		this.conversionService.setEmbeddedValueResolver(this.embeddedValueResolver);
		ConversionServiceFactory.addDefaultConverters(this.conversionService);
		ConversionServiceFactory.registerConverters(this.converters, this.conversionService);
		installFormatters(this.conversionService);
	}
{
		if (nestedType == null) {
			nestedType = Object.class;
		}
		if (this.methodParameter != null) {
			MethodParameter nested = new MethodParameter(this.methodParameter);
			nested.increaseNestingLevel();
			return newNestedTypeDescriptor(nestedType, nested);				
		}
		else if (this.field != null) {
			return new TypeDescriptor(nestedType, this.field, this.fieldNestingLevel + 1);
		}
		else {
			return TypeDescriptor.valueOf(nestedType);
		}
	}
{
		for (PropertySource<?> propertySource : propertySources.asList()) {
			this.addLast(propertySource);
		}
	}
{
		if (s == null) {
			return null;
		}
		if (s.startsWith("\"")) {
			s = s.substring(1);
		}
		if (s.endsWith("\"")) {
			s = s.substring(0, s.length() - 1);
		}
		return s;
	}
{
		super.setEnvironment(environment);
		this.reader.setEnvironment(environment);
		this.scanner.setEnvironment(environment);
	}
{
		return delegate.get(key);
	}
{
		if (explicitlySetProfiles)
			return;

		String profiles = getProperty(ACTIVE_PROFILES_PROPERTY_NAME);
		if (profiles == null || profiles.equals("")) {
			return;
		}

		this.activeProfiles = commaDelimitedListToSet(trimAllWhitespace(profiles));
	}
{
		this.readerContext = readerContext;

		logger.debug("Loading bean definitions");
		Element root = doc.getDocumentElement();

		BeanDefinitionParserDelegate delegate = createHelper(readerContext, root);

		preProcessXml(root);
		parseBeanDefinitions(root, delegate);
		postProcessXml(root);
	}
{
		// Tell the internal bean factory to use the context's class loader etc.
		beanFactory.setBeanClassLoader(getClassLoader());
		beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver());
		beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this));

		// Configure the bean factory with context callbacks.
		beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
		beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
		beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
		beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
		beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);

		// BeanFactory interface not registered as resolvable type in a plain factory.
		// MessageSource registered (and found for autowiring) as a bean.
		beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
		beanFactory.registerResolvableDependency(ResourceLoader.class, this);
		beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
		beanFactory.registerResolvableDependency(ApplicationContext.class, this);

		// Detect a LoadTimeWeaver and prepare for weaving, if found.
		if (beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
			beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
			// Set a temporary ClassLoader for type matching.
			beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
		}

		// Register default environment beans.
		if (!beanFactory.containsBean(SYSTEM_PROPERTIES_BEAN_NAME)) {
			Map systemProperties;
			try {
				systemProperties = System.getProperties();
			}
			catch (AccessControlException ex) {
				systemProperties = new ReadOnlySystemAttributesMap() {
					@Override
					protected String getSystemAttribute(String propertyName) {
						try {
							return System.getProperty(propertyName);
						}
						catch (AccessControlException ex) {
							if (logger.isInfoEnabled()) {
								logger.info("Not allowed to obtain system property [" + propertyName + "]: " +
										ex.getMessage());
							}
							return null;
						}
					}
				};
			}
			beanFactory.registerSingleton(SYSTEM_PROPERTIES_BEAN_NAME, systemProperties);
		}

		if (!beanFactory.containsBean(SYSTEM_ENVIRONMENT_BEAN_NAME)) {
			Map<String,String> systemEnvironment;
			try {
				systemEnvironment = System.getenv();
			}
			catch (AccessControlException ex) {
				systemEnvironment = new ReadOnlySystemAttributesMap() {
					@Override
					protected String getSystemAttribute(String variableName) {
						try {
							return System.getenv(variableName);
						}
						catch (AccessControlException ex) {
							if (logger.isInfoEnabled()) {
								logger.info("Not allowed to obtain system environment variable [" + variableName + "]: " +
										ex.getMessage());
							}
							return null;
						}
					}
				};
			}
			beanFactory.registerSingleton(SYSTEM_ENVIRONMENT_BEAN_NAME, systemEnvironment);
		}
	}
{
		for (Map.Entry<String, ?> entry : aliases.entrySet()) {
			String alias = entry.getKey();
			Object value = entry.getValue();
			Class type;
			if (value instanceof Class) {
				type = (Class) value;
			} else if (value instanceof String) {
				String s = (String) value;
				type = ClassUtils.forName(s, classLoader);
			} else {
				throw new IllegalArgumentException("Unknown value [" + value + "], expected String or Class");
			}
			this.getXStream().alias(alias, type);
		}
	}
{
		// TODO: update this code once Derby adds a proper way to remove an in-memory db
		// (see http://wiki.apache.org/db-derby/InMemoryBackEndPrimer for details)
		try {
			VFMemoryStorageFactory.purgeDatabase(new File(databaseName).getCanonicalPath());
		}
		catch (IOException ex) {
			logger.warn("Could not purge in-memory Derby database", ex);
		}
	}
{
		try {
			return (TableMetaDataProvider) JdbcUtils.extractDatabaseMetaData(
					dataSource, new DatabaseMetaDataCallback() {

				public Object processMetaData(DatabaseMetaData databaseMetaData)
						throws SQLException, MetaDataAccessException {
					String databaseProductName =
							JdbcUtils.commonDatabaseName(databaseMetaData.getDatabaseProductName());
					boolean accessTableColumnMetaData = context.isAccessTableColumnMetaData();
					TableMetaDataProvider provider;
					if ("Oracle".equals(databaseProductName)) {
						provider = new OracleTableMetaDataProvider(databaseMetaData,
								context.isOverrideIncludeSynonymsDefault());
					}
					else if ("HSQL Database Engine".equals(databaseProductName)) {
						provider = new HsqlTableMetaDataProvider(databaseMetaData);
					}
					else if ("PostgreSQL".equals(databaseProductName)) {
						provider = new PostgresTableMetaDataProvider(databaseMetaData);
					}
					else if ("Apache Derby".equals(databaseProductName)) {
						provider = new DerbyTableMetaDataProvider(databaseMetaData);
					}
					else {
						provider = new GenericTableMetaDataProvider(databaseMetaData);
					}
					if (logger.isDebugEnabled()) {
						logger.debug("Using " + provider.getClass().getName());
					}
					provider.initializeWithMetaData(databaseMetaData);
					if (accessTableColumnMetaData) {
						provider.initializeWithTableColumnMetaData(databaseMetaData, context.getCatalogName(),
								context.getSchemaName(), context.getTableName());
					}
					return provider;
				}
			});
		} catch (MetaDataAccessException e) {
			throw new DataAccessResourceFailureException("Error retreiving database metadata", e);
		}

	}
{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < classes.length; ++i) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(ClassUtils.getQualifiedName(classes[i]));
		}
		return sb.toString();
	}
{
		if (logger.isDebugEnabled()) {
			logger.debug("DispatcherPortlet with name '" + getPortletName() + "' received action request");
		}

		ActionRequest processedRequest = request;
		HandlerExecutionChain mappedHandler = null;
		int interceptorIndex = -1;

		try {
			processedRequest = checkMultipart(request);

			// Determine handler for the current request.
			mappedHandler = getHandler(processedRequest);
			if (mappedHandler == null || mappedHandler.getHandler() == null) {
				noHandlerFound(processedRequest, response);
				return;
			}

			// Apply preHandle methods of registered interceptors.
			HandlerInterceptor[] interceptors = mappedHandler.getInterceptors();
			if (interceptors != null) {
				for (int i = 0; i < interceptors.length; i++) {
					HandlerInterceptor interceptor = interceptors[i];
					if (!interceptor.preHandleAction(processedRequest, response, mappedHandler.getHandler())) {
						triggerAfterActionCompletion(mappedHandler, interceptorIndex, processedRequest, response, null);
						return;
					}
					interceptorIndex = i;
				}
			}

			// Actually invoke the handler.
			HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
			ha.handleAction(processedRequest, response, mappedHandler.getHandler());

			// Trigger after-completion for successful outcome.
			triggerAfterActionCompletion(mappedHandler, interceptorIndex, processedRequest, response, null);
		}

		catch (Exception ex) {
			// Trigger after-completion for thrown exception.
			triggerAfterActionCompletion(mappedHandler, interceptorIndex, processedRequest, response, ex);
			// Forward the exception to the render phase to be displayed.
			try {
				// Copy all parameters unless overridden in the action handler.
				Enumeration<String> paramNames = request.getParameterNames();
				while (paramNames.hasMoreElements()) {
					String paramName = paramNames.nextElement();
					String[] paramValues = request.getParameterValues(paramName);
					if (paramValues != null && !response.getRenderParameterMap().containsKey(paramName)) {
						response.setRenderParameter(paramName, paramValues);
					}
				}
				response.setRenderParameter(ACTION_EXCEPTION_RENDER_PARAMETER, Boolean.TRUE.toString());
				request.getPortletSession().setAttribute(ACTION_EXCEPTION_SESSION_ATTRIBUTE, ex);
				logger.debug("Caught exception during action phase - forwarding to render phase", ex);
			}
			catch (IllegalStateException ex2) {
				// Probably sendRedirect called... need to rethrow exception immediately.
				throw ex;
			}
		}
		catch (Error err) {
			PortletException ex =
					new PortletException("Error occured during request processing: " + err.getMessage(), err);
			// Trigger after-completion for thrown exception.
			triggerAfterActionCompletion(mappedHandler, interceptorIndex, processedRequest, response, ex);
			throw ex;
		}

		finally {
			// Clean up any resources used by a multipart request.
			if (processedRequest instanceof MultipartActionRequest && processedRequest != request) {
				this.multipartResolver.cleanupMultipart((MultipartActionRequest) processedRequest);
			}
		}
	}
{
		if (jaxp14Available) {
			return Jaxp14StaxHandler.createStaxSource(streamReader);
		}
		else {
			return new StaxSource(streamReader);
		}
	}
{
		if (jaxp14Available) {
			return Jaxp14StaxHandler.createStaxResult(streamWriter);
		}
		else {
			return new StaxResult(streamWriter);
		}
	}
{
		String enc = this.encodingScheme;
		if (enc == null) {
			enc = request.getCharacterEncoding();
		}
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		return enc;
	}
{

		// Prepare target URL.
		StringBuilder targetUrl = new StringBuilder();
		if (this.contextRelative && getUrl().startsWith("/")) {
			// Do not apply context path to relative URLs.
			targetUrl.append(request.getContextPath());
		}
		targetUrl.append(getUrl());
		if (this.exposeModelAttributes) {
			String enc = this.encodingScheme;
			if (enc == null) {
				enc = request.getCharacterEncoding();
			}
			if (enc == null) {
				enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
			}
			appendQueryProperties(targetUrl, model, enc);
		}

		sendRedirect(request, response, targetUrl.toString(), this.http10Compatible);
	}
{
		while (classToIntrospect != null) {
			Type[] ifcs = classToIntrospect.getGenericInterfaces();
			for (Type ifc : ifcs) {
				if (ifc instanceof ParameterizedType) {
					ParameterizedType paramIfc = (ParameterizedType) ifc;
					Type rawType = paramIfc.getRawType();
					if (genericIfc.equals(rawType)) {
						Type[] typeArgs = paramIfc.getActualTypeArguments();
						Class[] result = new Class[typeArgs.length];
						for (int i = 0; i < typeArgs.length; i++) {
							Type arg = typeArgs[i];
							result[i] = extractClass(ownerClass, arg);
						}
						return result;
					}
					else if (genericIfc.isAssignableFrom((Class) rawType)) {
						return doResolveTypeArguments(ownerClass, (Class) rawType, genericIfc);
					}
				}
				else if (genericIfc.isAssignableFrom((Class) ifc)) {
					return doResolveTypeArguments(ownerClass, (Class) ifc, genericIfc);
				}
			}
			classToIntrospect = classToIntrospect.getSuperclass();
		}
		return null;
	}
{
			String lookupPath = urlPathHelper.getLookupPathForRequest(request);
			Comparator<String> pathComparator = pathMatcher.getPatternComparator(lookupPath);
			Map<RequestMappingInfo, Method> targetHandlerMethods = new LinkedHashMap<RequestMappingInfo, Method>();
			Set<String> allowedMethods = new LinkedHashSet<String>(7);
			String resolvedMethodName = null;
			for (Method handlerMethod : getHandlerMethods()) {
				RequestMappingInfo mappingInfo = new RequestMappingInfo();
				RequestMapping mapping = AnnotationUtils.findAnnotation(handlerMethod, RequestMapping.class);
				mappingInfo.paths = mapping.value();
				if (!hasTypeLevelMapping() || !Arrays.equals(mapping.method(), getTypeLevelMapping().method())) {
					mappingInfo.methods = mapping.method();
				}
				if (!hasTypeLevelMapping() || !Arrays.equals(mapping.params(), getTypeLevelMapping().params())) {
					mappingInfo.params = mapping.params();
				}
				if (!hasTypeLevelMapping() || !Arrays.equals(mapping.headers(), getTypeLevelMapping().headers())) {
					mappingInfo.headers = mapping.headers();
				}
				boolean match = false;
				if (mappingInfo.paths.length > 0) {
					List<String> matchedPaths = new ArrayList<String>(mappingInfo.paths.length);
					for (String mappedPattern : mappingInfo.paths) {
						if (!hasTypeLevelMapping() && !mappedPattern.startsWith("/")) {
							mappedPattern = "/" + mappedPattern;
						}
						String matchedPattern = getMatchedPattern(mappedPattern, lookupPath, request);
						if (matchedPattern != null) {
							if (mappingInfo.matches(request)) {
								match = true;
								matchedPaths.add(matchedPattern);
							}
							else {
								for (RequestMethod requestMethod : mappingInfo.methods) {
									allowedMethods.add(requestMethod.toString());
								}
								break;
							}
						}
					}
					Collections.sort(matchedPaths, pathComparator);
					mappingInfo.matchedPaths = matchedPaths;
				}
				else {
					// No paths specified: parameter match sufficient.
					match = mappingInfo.matches(request);
					if (match && mappingInfo.methods.length == 0 && mappingInfo.params.length == 0 &&
							resolvedMethodName != null && !resolvedMethodName.equals(handlerMethod.getName())) {
						match = false;
					}
					else {
						for (RequestMethod requestMethod : mappingInfo.methods) {
							allowedMethods.add(requestMethod.toString());
						}
					}
				}
				if (match) {
					Method oldMappedMethod = targetHandlerMethods.put(mappingInfo, handlerMethod);
					if (oldMappedMethod != null && oldMappedMethod != handlerMethod) {
						if (methodNameResolver != null && mappingInfo.paths.length == 0) {
							if (!oldMappedMethod.getName().equals(handlerMethod.getName())) {
								if (resolvedMethodName == null) {
									resolvedMethodName = methodNameResolver.getHandlerMethodName(request);
								}
								if (!resolvedMethodName.equals(oldMappedMethod.getName())) {
									oldMappedMethod = null;
								}
								if (!resolvedMethodName.equals(handlerMethod.getName())) {
									if (oldMappedMethod != null) {
										targetHandlerMethods.put(mappingInfo, oldMappedMethod);
										oldMappedMethod = null;
									}
									else {
										targetHandlerMethods.remove(mappingInfo);
									}
								}
							}
						}
						if (oldMappedMethod != null) {
							throw new IllegalStateException(
									"Ambiguous handler methods mapped for HTTP path '" + lookupPath + "': {" +
											oldMappedMethod + ", " + handlerMethod +
											"}. If you intend to handle the same path in multiple methods, then factor " +
											"them out into a dedicated handler class with that path mapped at the type level!");
						}
					}
				}
			}
			if (!targetHandlerMethods.isEmpty()) {
				List<RequestMappingInfo> matches = new ArrayList<RequestMappingInfo>(targetHandlerMethods.keySet());
				RequestMappingInfoComparator requestMappingInfoComparator =
						new RequestMappingInfoComparator(pathComparator, request);
				Collections.sort(matches, requestMappingInfoComparator);
				RequestMappingInfo bestMappingMatch = matches.get(0);
				String bestMatchedPath = bestMappingMatch.bestMatchedPath();
				if (bestMatchedPath != null) {
					extractHandlerMethodUriTemplates(bestMatchedPath, lookupPath, request);
				}
				return targetHandlerMethods.get(bestMappingMatch);
			}
			else {
				if (!allowedMethods.isEmpty()) {
					throw new HttpRequestMethodNotSupportedException(request.getMethod(),
							StringUtils.toStringArray(allowedMethods));
				}
				else {
					throw new NoSuchRequestHandlingMethodException(lookupPath, request.getMethod(),
							request.getParameterMap());
				}
			}
		}
{
		// do something nice for arrays
		if (value == null) {
			return "null";
		}
		if (value.getClass().isArray()) {
			StringBuilder sb = new StringBuilder();
			if (value.getClass().getComponentType().isPrimitive()) {
				Class<?> primitiveType = value.getClass().getComponentType();
				if (primitiveType == Integer.TYPE) {
					int[] l = (int[]) value;
					sb.append("int[").append(l.length).append("]{");
					for (int j = 0; j < l.length; j++) {
						if (j > 0) {
							sb.append(",");
						}
						sb.append(stringValueOf(l[j]));
					}
					sb.append("}");
				} else if (primitiveType == Long.TYPE) {
					long[] l = (long[]) value;
					sb.append("long[").append(l.length).append("]{");
					for (int j = 0; j < l.length; j++) {
						if (j > 0) {
							sb.append(",");
						}
						sb.append(stringValueOf(l[j]));
					}
					sb.append("}");
				} else {
					throw new RuntimeException("Please implement support for type " + primitiveType.getName()
							+ " in ExpressionTestCase.stringValueOf()");
				}
			} else {
				List<Object> l = Arrays.asList((Object[]) value);
				sb.append(value.getClass().getComponentType().getName()).append("[").append(l.size()).append("]{");
				int i = 0;
				for (Object object : l) {
					if (i > 0) {
						sb.append(",");
					}
					i++;
					sb.append(stringValueOf(object));
				}
				sb.append("}");
			}
			return sb.toString();
		} else {
			return value.toString();
		}
	}
{
		if (this.propertyAccessors == null) {
			this.propertyAccessors = new ArrayList<PropertyAccessor>();
			this.propertyAccessors.add(new ReflectivePropertyAccessor());
		}
	}
{
		if (this.methodResolvers == null) {
			this.methodResolvers = new ArrayList<MethodResolver>();
			this.methodResolvers.add(reflectiveMethodResolver=new ReflectiveMethodResolver());
		}
	}
{
		if (this.constructorResolvers == null) {
			this.constructorResolvers = new ArrayList<ConstructorResolver>();
			this.constructorResolvers.add(new ReflectiveConstructorResolver());
		}
	}
{
		Set<Class<?>> fieldTypes = new HashSet<Class<?>>(7);
		fieldTypes.add(Short.class);
		fieldTypes.add(Integer.class);
		fieldTypes.add(Long.class);
		fieldTypes.add(Float.class);
		fieldTypes.add(Double.class);
		fieldTypes.add(BigDecimal.class);
		fieldTypes.add(BigInteger.class);
		return fieldTypes;
	}
{
		formattingService.addConverter(new Converter<Date, Long>() {
			public Long convert(Date source) {
				return source.getTime();
			}
		});
		formattingService.addConverter(new Converter<DateTime, Date>() {
			public Date convert(DateTime source) {
				return source.toDate();
			}
		});
		formattingService.addFormatterForFieldAnnotation(new JodaDateTimeFormatAnnotationFormatterFactory());
		String formatted = (String) formattingService.convert(new LocalDate(2009, 10, 31).toDateTimeAtCurrentTime()
				.toDate(), new TypeDescriptor(Model.class.getField("date")), TypeDescriptor.valueOf(String.class));
		assertEquals("10/31/09", formatted);
		LocalDate date = new LocalDate(formattingService.convert("10/31/09", TypeDescriptor.valueOf(String.class),
				new TypeDescriptor(Model.class.getField("date"))));
		assertEquals(new LocalDate(2009, 10, 31), date);
	}
{
		ResourceEditor baseEditor = new ResourceEditor(this.resourceLoader);
		registry.registerCustomEditor(Resource.class, baseEditor);
		registry.registerCustomEditor(InputStream.class, new InputStreamEditor(baseEditor));
		registry.registerCustomEditor(InputSource.class, new InputSourceEditor(baseEditor));
		registry.registerCustomEditor(File.class, new FileEditor(baseEditor));
		registry.registerCustomEditor(URL.class, new URLEditor(baseEditor));

		ClassLoader classLoader = this.resourceLoader.getClassLoader();
		registry.registerCustomEditor(Class.class, new ClassEditor(classLoader));
		registry.registerCustomEditor(URI.class, new URIEditor(classLoader));

		if (this.resourceLoader instanceof ResourcePatternResolver) {
			registry.registerCustomEditor(Resource[].class,
					new ResourceArrayPropertyEditor((ResourcePatternResolver) this.resourceLoader));
		}
	}
{
		ApplicationContext ctx = new ClassPathXmlApplicationContext("conversionService.xml", getClass());
		ResourceTestBean tb = ctx.getBean("resourceTestBean", ResourceTestBean.class);
		assertTrue(tb.getResource() instanceof ClassPathResource);
		assertTrue(tb.getResourceArray().length > 0);
		assertTrue(tb.getResourceArray()[0] instanceof ClassPathResource);
		assertTrue(tb.getResourceMap().size() == 1);
		assertTrue(tb.getResourceMap().get("key1") instanceof ClassPathResource);
		assertTrue(tb.getResourceArrayMap().size() == 1);
		assertTrue(tb.getResourceArrayMap().get("key1").length > 0);
		assertTrue(tb.getResourceArrayMap().get("key1")[0] instanceof ClassPathResource);
	}
{
		Map<String, PlatformTransactionManager> tms =
				BeanFactoryUtils.beansOfTypeIncludingAncestors(bf, PlatformTransactionManager.class);
		PlatformTransactionManager chosen = null;
		for (String beanName : tms.keySet()) {
			if (bf.containsBeanDefinition(beanName)) {
				BeanDefinition bd = bf.getBeanDefinition(beanName);
				if (bd instanceof AbstractBeanDefinition) {
					AbstractBeanDefinition abd = (AbstractBeanDefinition) bd;
					AutowireCandidateQualifier candidate = abd.getQualifier(Qualifier.class.getName());
					if ((candidate != null && qualifier.equals(candidate.getAttribute(AutowireCandidateQualifier.VALUE_KEY))) ||
							qualifier.equals(beanName) || ObjectUtils.containsElement(bf.getAliases(beanName), qualifier)) {
						if (chosen != null) {
							throw new IllegalStateException("No unique PlatformTransactionManager bean found " +
									"for qualifier '" + qualifier + "'");
						}
						chosen = tms.get(beanName);
					}
				}
			}
		}
		if (chosen != null) {
			return chosen;
		}
		else {
			throw new IllegalStateException("No matching PlatformTransactionManager bean found for qualifier '" +
					qualifier + "' - neither qualifier match nor bean name match!");
		}
	}
{
		String propertyName = tokens.canonicalName;
		String actualName = tokens.actualName;

		if (tokens.keys != null) {
			// Apply indexes and map keys: fetch value for all keys but the last one.
			PropertyTokenHolder getterTokens = new PropertyTokenHolder();
			getterTokens.canonicalName = tokens.canonicalName;
			getterTokens.actualName = tokens.actualName;
			getterTokens.keys = new String[tokens.keys.length - 1];
			System.arraycopy(tokens.keys, 0, getterTokens.keys, 0, tokens.keys.length - 1);
			Object propValue;
			try {
				propValue = getPropertyValue(getterTokens);
			}
			catch (NotReadablePropertyException ex) {
				throw new NotWritablePropertyException(getRootClass(), this.nestedPath + propertyName,
						"Cannot access indexed value in property referenced " +
						"in indexed property path '" + propertyName + "'", ex);
			}
			// Set value for last key.
			String key = tokens.keys[tokens.keys.length - 1];
			if (propValue == null) {
				throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + propertyName,
						"Cannot access indexed value in property referenced " +
						"in indexed property path '" + propertyName + "': returned null");
			}
			else if (propValue.getClass().isArray()) {
				Class requiredType = propValue.getClass().getComponentType();
				int arrayIndex = Integer.parseInt(key);
				Object oldValue = null;
				try {
					if (isExtractOldValueForEditor()) {
						oldValue = Array.get(propValue, arrayIndex);
					}
					Object convertedValue = this.typeConverterDelegate.convertIfNecessary(
							propertyName, oldValue, pv.getValue(), requiredType);
					Array.set(propValue, Integer.parseInt(key), convertedValue);
				}
				catch (IllegalArgumentException ex) {
					PropertyChangeEvent pce =
							new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, pv.getValue());
					throw new TypeMismatchException(pce, requiredType, ex);
				}
				catch (IllegalStateException ex) {
					PropertyChangeEvent pce =
							new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, pv.getValue());
					throw new ConversionNotSupportedException(pce, requiredType, ex);
				}
				catch (IndexOutOfBoundsException ex) {
					throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName,
							"Invalid array index in property path '" + propertyName + "'", ex);
				}
			}
			else if (propValue instanceof List) {
				PropertyDescriptor pd = getCachedIntrospectionResults().getPropertyDescriptor(actualName);
				Class requiredType = GenericCollectionTypeResolver.getCollectionReturnType(
						pd.getReadMethod(), tokens.keys.length);
				List list = (List) propValue;
				int index = Integer.parseInt(key);
				Object oldValue = null;
				if (isExtractOldValueForEditor() && index < list.size()) {
					oldValue = list.get(index);
				}
				try {
					Object convertedValue = this.typeConverterDelegate.convertIfNecessary(
							propertyName, oldValue, pv.getValue(), requiredType);
					if (index < list.size()) {
						list.set(index, convertedValue);
					}
					else if (index >= list.size()) {
						for (int i = list.size(); i < index; i++) {
							try {
								list.add(null);
							}
							catch (NullPointerException ex) {
								throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName,
										"Cannot set element with index " + index + " in List of size " +
										list.size() + ", accessed using property path '" + propertyName +
										"': List does not support filling up gaps with null elements");
							}
						}
						list.add(convertedValue);
					}
				}
				catch (IllegalArgumentException ex) {
					PropertyChangeEvent pce =
							new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, pv.getValue());
					throw new TypeMismatchException(pce, requiredType, ex);
				}
			}
			else if (propValue instanceof Map) {
				PropertyDescriptor pd = getCachedIntrospectionResults().getPropertyDescriptor(actualName);
				Class mapKeyType = GenericCollectionTypeResolver.getMapKeyReturnType(
						pd.getReadMethod(), tokens.keys.length);
				Class mapValueType = GenericCollectionTypeResolver.getMapValueReturnType(
						pd.getReadMethod(), tokens.keys.length);
				Map map = (Map) propValue;
				Object convertedMapKey;
				Object convertedMapValue;
				try {
					// IMPORTANT: Do not pass full property name in here - property editors
					// must not kick in for map keys but rather only for map values.
					convertedMapKey = this.typeConverterDelegate.convertIfNecessary(key, mapKeyType);
				}
				catch (IllegalArgumentException ex) {
					PropertyChangeEvent pce =
							new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, null, pv.getValue());
					throw new TypeMismatchException(pce, mapKeyType, ex);
				}
				Object oldValue = null;
				if (isExtractOldValueForEditor()) {
					oldValue = map.get(convertedMapKey);
				}
				try {
					// Pass full property name and old value in here, since we want full
					// conversion ability for map values.
					convertedMapValue = this.typeConverterDelegate.convertIfNecessary(
							propertyName, oldValue, pv.getValue(), mapValueType,
							new MethodParameter(pd.getReadMethod(), -1, tokens.keys.length + 1));
				}
				catch (IllegalArgumentException ex) {
					PropertyChangeEvent pce =
							new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, pv.getValue());
					throw new TypeMismatchException(pce, mapValueType, ex);
				}
				map.put(convertedMapKey, convertedMapValue);
			}
			else {
				throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName,
						"Property referenced in indexed property path '" + propertyName +
						"' is neither an array nor a List nor a Map; returned value was [" + pv.getValue() + "]");
			}
		}

		else {
			PropertyDescriptor pd = pv.resolvedDescriptor;
			if (pd == null || !pd.getWriteMethod().getDeclaringClass().isInstance(this.object)) {
				pd = getCachedIntrospectionResults().getPropertyDescriptor(actualName);
				if (pd == null || pd.getWriteMethod() == null) {
					if (pv.isOptional()) {
						logger.debug("Ignoring optional value for property '" + actualName +
								"' - property not found on bean class [" + getRootClass().getName() + "]");
						return;
					}
					else {
						PropertyMatches matches = PropertyMatches.forProperty(propertyName, getRootClass());
						throw new NotWritablePropertyException(
								getRootClass(), this.nestedPath + propertyName,
								matches.buildErrorMessage(), matches.getPossibleMatches());
					}
				}
				pv.getOriginalPropertyValue().resolvedDescriptor = pd;
			}

			Object oldValue = null;
			try {
				Object originalValue = pv.getValue();
				Object valueToApply = originalValue;
				if (!Boolean.FALSE.equals(pv.conversionNecessary)) {
					if (pv.isConverted()) {
						valueToApply = pv.getConvertedValue();
					}
					else {
						if (isExtractOldValueForEditor() && pd.getReadMethod() != null) {
							final Method readMethod = pd.getReadMethod();
							if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers()) && !readMethod.isAccessible()) {
								if (System.getSecurityManager()!= null) {
									AccessController.doPrivileged(new PrivilegedAction<Object>() {
										public Object run() {
											readMethod.setAccessible(true);
											return null;
										}
									});
								}
								else {
									readMethod.setAccessible(true);
								}
							}
							try {
								if (System.getSecurityManager() != null) {
									oldValue = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
										public Object run() throws Exception {
											return readMethod.invoke(object);
										}
									},acc);
								}
								else {
									oldValue = readMethod.invoke(object);
								}
							}
							catch (Exception ex) {
								if (ex instanceof PrivilegedActionException) {
									ex = ((PrivilegedActionException) ex).getException();
								}
								if (logger.isDebugEnabled()) {
									logger.debug("Could not read previous value of property '" +
											this.nestedPath + propertyName + "'", ex);
								}
							}
						}
						valueToApply = this.typeConverterDelegate.convertIfNecessary(oldValue, originalValue, pd);
					}
					pv.getOriginalPropertyValue().conversionNecessary = (valueToApply != originalValue);
				}
				final Method writeMethod = (pd instanceof GenericTypeAwarePropertyDescriptor ?
						((GenericTypeAwarePropertyDescriptor) pd).getWriteMethodForActualAccess() :
						pd.getWriteMethod());
				if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers()) && !writeMethod.isAccessible()) {
					if (System.getSecurityManager()!= null) {
						AccessController.doPrivileged(new PrivilegedAction<Object>() {
							public Object run() {
								writeMethod.setAccessible(true);
								return null;
							}
						});
					}
					else {
						writeMethod.setAccessible(true);
					}
				}
				final Object value = valueToApply;
				
				if (System.getSecurityManager() != null) {
					try {
						AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
							public Object run() throws Exception {
								writeMethod.invoke(object, value);
								return null;
							}
						}, acc);
					} catch (PrivilegedActionException ex) {
						throw ex.getException();
					}
				}
				else {
					writeMethod.invoke(object, value);
				}
					
			}
			catch (InvocationTargetException ex) {
				PropertyChangeEvent propertyChangeEvent =
						new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, pv.getValue());
				if (ex.getTargetException() instanceof ClassCastException) {
					throw new TypeMismatchException(propertyChangeEvent, pd.getPropertyType(), ex.getTargetException());
				}
				else {
					throw new MethodInvocationException(propertyChangeEvent, ex.getTargetException());
				}
			}
			catch (ConverterNotFoundException ex) {
				PropertyChangeEvent pce =
						new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, pv.getValue());
				throw new ConversionNotSupportedException(pce, pd.getPropertyType(), ex);
			}
			catch (ConversionException ex) {
				PropertyChangeEvent pce =
						new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, pv.getValue());
				throw new TypeMismatchException(pce, pd.getPropertyType(), ex);
			}
			catch (IllegalStateException ex) {
				PropertyChangeEvent pce =
						new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, pv.getValue());
				throw new ConversionNotSupportedException(pce, pd.getPropertyType(), ex);
			}
			catch (IllegalArgumentException ex) {
				PropertyChangeEvent pce =
						new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, pv.getValue());
				throw new TypeMismatchException(pce, pd.getPropertyType(), ex);
			}
			catch (IllegalAccessException ex) {
				PropertyChangeEvent pce =
						new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, pv.getValue());
				throw new MethodInvocationException(pce, ex);
			}
			catch (Exception ex) {
				PropertyChangeEvent pce =
					new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, pv.getValue());
				throw new MethodInvocationException(pce, ex);
			}
		}
	}
{
		GenericApplicationContext ac = new GenericApplicationContext();

		GenericBeanDefinition serviceDef = new GenericBeanDefinition();
		serviceDef.setBeanClass(OrderServiceImpl.class);
		ac.registerBeanDefinition("service", serviceDef);

		GenericBeanDefinition exporterDef = new GenericBeanDefinition();
		exporterDef.setBeanClass(SimpleJaxWsServiceExporter.class);
		exporterDef.getPropertyValues().add("baseAddress", "http://localhost:9999/");
		ac.registerBeanDefinition("exporter", exporterDef);

		GenericBeanDefinition clientDef = new GenericBeanDefinition();
		clientDef.setBeanClass(JaxWsPortProxyFactoryBean.class);
		clientDef.getPropertyValues().add("wsdlDocumentUrl", "http://localhost:9999/OrderService?wsdl");
		clientDef.getPropertyValues().add("namespaceUri", "http://jaxws.remoting.springframework.org/");
		clientDef.getPropertyValues().add("username", "juergen");
		clientDef.getPropertyValues().add("password", "hoeller");
		clientDef.getPropertyValues().add("serviceName", "OrderService");
		clientDef.getPropertyValues().add("serviceInterface", OrderService.class);
		clientDef.getPropertyValues().add("lookupServiceOnStartup", Boolean.FALSE);
		ac.registerBeanDefinition("client", clientDef);

		GenericBeanDefinition serviceFactoryDef = new GenericBeanDefinition();
		serviceFactoryDef.setBeanClass(LocalJaxWsServiceFactoryBean.class);
		serviceFactoryDef.getPropertyValues().add("wsdlDocumentUrl", "http://localhost:9999/OrderService?wsdl");
		serviceFactoryDef.getPropertyValues().add("namespaceUri", "http://jaxws.remoting.springframework.org/");
		serviceFactoryDef.getPropertyValues().add("serviceName", "OrderService");
		ac.registerBeanDefinition("orderService", serviceFactoryDef);

		ac.registerBeanDefinition("accessor", new RootBeanDefinition(ServiceAccessor.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(ac);

		try {
			ac.refresh();

			OrderService orderService = ac.getBean("client", OrderService.class);
			assertTrue(orderService instanceof BindingProvider);
			((BindingProvider) orderService).getRequestContext();

			String order = orderService.getOrder(1000);
			assertEquals("order 1000", order);
			try {
				orderService.getOrder(0);
				fail("Should have thrown OrderNotFoundException");
			}
			catch (OrderNotFoundException ex) {
				// expected
			}

			ServiceAccessor serviceAccessor = ac.getBean("accessor", ServiceAccessor.class);
			order = serviceAccessor.orderService.getOrder(1000);
			assertEquals("order 1000", order);
			try {
				serviceAccessor.orderService.getOrder(0);
				fail("Should have thrown OrderNotFoundException");
			}
			catch (OrderNotFoundException ex) {
				// expected
			}
		}
		catch (BeanCreationException ex) {
			if ("exporter".equals(ex.getBeanName()) && ex.getRootCause() instanceof ClassNotFoundException) {
				// ignore - probably running on JDK < 1.6 without the JAX-WS impl present
			}
			else {
				throw ex;
			}
		}
		finally {
			ac.close();
		}
	}
{
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		Assert.isInstanceOf(ServletRequestAttributes.class, attrs);
		ServletRequestAttributes servletAttrs = (ServletRequestAttributes) attrs;

		List<MediaType> requestedMediaTypes = getMediaTypes(servletAttrs.getRequest());

		List<View> candidateViews = new ArrayList<View>();
		for (ViewResolver viewResolver : this.viewResolvers) {
			View view = viewResolver.resolveViewName(viewName, locale);
			if (view != null) {
				candidateViews.add(view);
			}
		}
		if (!CollectionUtils.isEmpty(this.defaultViews)) {
			candidateViews.addAll(this.defaultViews);
		}

		MediaType bestRequestedMediaType = null;
		View bestView = null;
		for (MediaType requestedMediaType : requestedMediaTypes) {
			for (View candidateView : candidateViews) {
				if (StringUtils.hasText(candidateView.getContentType())) {
					MediaType candidateContentType = MediaType.parseMediaType(candidateView.getContentType());
					if (requestedMediaType.includes(candidateContentType)) {
						bestRequestedMediaType = requestedMediaType;
						bestView = candidateView;
						break;
					}
				}
			}
			if (bestView != null) {
				break;
			}
		}

		if (bestView != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Returning [" + bestView + "] based on requested media type '" +
						bestRequestedMediaType + "'");
			}
			return bestView;
		}
		else {
			if (useNotAcceptableStatusCode) {
				if (logger.isDebugEnabled()) {
					logger.debug("No acceptable view found; returning 406 (Not Acceptable) status code");
				}
				return NOT_ACCEPTABLE_VIEW;
			}
			else {
				if (logger.isDebugEnabled()) {
					logger.debug("No acceptable view found; returning null");
				}
				return null;
			}
		}
	}
{
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		Assert.isInstanceOf(ServletRequestAttributes.class, attrs);
		ServletRequestAttributes servletAttrs = (ServletRequestAttributes) attrs;

		List<MediaType> requestedMediaTypes = getMediaTypes(servletAttrs.getRequest());

		List<View> candidateViews = new ArrayList<View>();
		for (ViewResolver viewResolver : this.viewResolvers) {
			View view = viewResolver.resolveViewName(viewName, locale);
			if (view != null) {
				candidateViews.add(view);
			}
		}
		if (!CollectionUtils.isEmpty(this.defaultViews)) {
			candidateViews.addAll(this.defaultViews);
		}

		MediaType bestRequestedMediaType = null;
		View bestView = null;
		for (MediaType requestedMediaType : requestedMediaTypes) {
			for (View candidateView : candidateViews) {
				if (StringUtils.hasText(candidateView.getContentType())) {
					MediaType candidateContentType = MediaType.parseMediaType(candidateView.getContentType());
					if (requestedMediaType.includes(candidateContentType)) {
						bestRequestedMediaType = requestedMediaType;
						bestView = candidateView;
						break;
					}
				}
			}
			if (bestView != null) {
				break;
			}
		}

		if (bestView != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Returning [" + bestView + "] based on requested media type '" +
						bestRequestedMediaType + "'");
			}
			return bestView;
		}
		else {
			if (useNotAcceptableStatusCode) {
				if (logger.isDebugEnabled()) {
					logger.debug("No acceptable view found; returning 406 (Not Acceptable) status code");
				}
				return NOT_ACCEPTABLE_VIEW;
			}
			else {
				if (logger.isDebugEnabled()) {
					logger.debug("No acceptable view found; returning null");
				}
				return null;
			}
		}
	}
{
		if (this.field != null) {
			// not caching
			return this.field.getAnnotations();
		}
		else if (this.methodParameter != null) {
			if (this.methodParameter.getParameterIndex() < 0) {
				// not caching
				return this.methodParameter.getMethodAnnotations();
			}
			else {
				return this.methodParameter.getParameterAnnotations();
			}
		}
		else {
			return EMPTY_ANNOTATION_ARRAY;
		}
	}
{
		Assert.notNull(source, "'source' must not be null");
		Assert.hasLength(encoding, "'encoding' must not be empty");
		ByteArrayOutputStream bos = new ByteArrayOutputStream(source.length() * 2);

		for (int i = 0; i < source.length(); i++) {
			int ch = source.charAt(i);
			if (notEncoded.get(ch)) {
				bos.write(ch);
			}
			else {
				bos.write('%');
				char hex1 = Character.toUpperCase(Character.forDigit((ch >> 4) & 0xF, 16));
				char hex2 = Character.toUpperCase(Character.forDigit(ch & 0xF, 16));
				bos.write(hex1);
				bos.write(hex2);
			}
		}
		return new String(bos.toByteArray(), encoding);
	}
{
		PropertyEditor uriEditor = new URIEditor();
		uriEditor.setAsText("mailto:juergen.hoeller@interface21.com");
		Object value = uriEditor.getValue();
		assertTrue(value instanceof URI);
		URI uri = (URI) value;
		assertEquals(uri.toString(), uriEditor.getAsText());
	}
{
		ReflectionUtils.makeAccessible(method);
		try {
			return method.invoke(target, args);
		}
		catch (InvocationTargetException ex) {
			ReflectionUtils.rethrowException(ex.getTargetException());
		}
		throw new IllegalStateException("Should never get here");
	}
{
		ReflectionUtils.makeAccessible(method);
		try {
			return method.invoke(target, args);
		}
		catch (InvocationTargetException ex) {
			ReflectionUtils.rethrowException(ex.getTargetException());
		}
		throw new IllegalStateException("Should never get here");
	}
{
			assertEquals("Invalid Content-Type", new MediaType("text", "html"), headers.getContentType());
			for (Iterator<Map.Entry<String, List<String>>> it1 = headers.entrySet().iterator(); it1.hasNext();) {
				Map.Entry<String, List<String>> entry = it1.next();
				writer.write(entry.getKey() + "=[");
				for (Iterator<String> it2 = entry.getValue().iterator(); it2.hasNext();) {
					String value = it2.next();
					writer.write(value);
					if (it2.hasNext()) {
						writer.write(',');
					}
				}
				writer.write(']');
				if (it1.hasNext()) {
					writer.write(',');
				}
			}
		}
{
			for (Iterator<Map.Entry<String, List<String>>> it1 = headers.entrySet().iterator(); it1.hasNext();) {
				Map.Entry<String, List<String>> entry = it1.next();
				writer.write(entry.getKey() + "=[");
				for (Iterator<String> it2 = entry.getValue().iterator(); it2.hasNext();) {
					String value = it2.next();
					writer.write(value);
					if (it2.hasNext()) {
						writer.write(',');
					}
				}
				writer.write(']');
				if (it1.hasNext()) {
					writer.write(',');
				}
			}
		}
{
			if (returnValue == null) {
				return;
			}

			HttpInputMessage inputMessage = new ServletServerHttpRequest(webRequest.getRequest());
			List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
			if (acceptedMediaTypes.isEmpty()) {
				acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
			}
			MediaType.sortBySpecificity(acceptedMediaTypes);
			HttpOutputMessage outputMessage = new ServletServerHttpResponse(webRequest.getResponse());
			Class<?> returnValueType = returnValue.getClass();
			List<MediaType> allSupportedMediaTypes = new ArrayList<MediaType>();
			if (getMessageConverters() != null) {
				for (MediaType acceptedMediaType : acceptedMediaTypes) {
					for (HttpMessageConverter messageConverter : getMessageConverters()) {
						if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {
							messageConverter.write(returnValue, acceptedMediaType, outputMessage);
							if (logger.isDebugEnabled()) {
								MediaType contentType = outputMessage.getHeaders().getContentType();
								if (contentType == null) {
									contentType = acceptedMediaType;
								}
								logger.debug("Written [" + returnValue + "] as \"" + contentType +
										"\" using [" + messageConverter + "]");
							}
							this.responseArgumentUsed = true;
							return;
						}
					}
				}
				for (HttpMessageConverter messageConverter : messageConverters) {
					allSupportedMediaTypes.addAll(messageConverter.getSupportedMediaTypes());
				}
			}
			throw new HttpMediaTypeNotAcceptableException(allSupportedMediaTypes);
		}
{

		HttpInputMessage inputMessage = createHttpInputMessage(webRequest);
		Class paramType = methodParam.getParameterType();
		MediaType contentType = inputMessage.getHeaders().getContentType();
		if (contentType == null) {
			StringBuilder builder = new StringBuilder(ClassUtils.getShortName(methodParam.getParameterType()));
			String paramName = methodParam.getParameterName();
			if (paramName != null) {
				builder.append(' ');
				builder.append(paramName);
			}
			throw new HttpMediaTypeNotSupportedException(
					"Cannot extract @RequestBody parameter (" + builder.toString() + "): no Content-Type found");
		}
		List<MediaType> allSupportedMediaTypes = new ArrayList<MediaType>();
		if (this.messageConverters != null) {
			for (HttpMessageConverter<?> messageConverter : this.messageConverters) {
				allSupportedMediaTypes.addAll(messageConverter.getSupportedMediaTypes());
				if (messageConverter.canRead(paramType, contentType)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Reading [" + paramType.getName() + "] as \"" + contentType
								+"\" using [" + messageConverter + "]");
					}
					return messageConverter.read(paramType, inputMessage);
				}
			}
		}
		throw new HttpMediaTypeNotSupportedException(contentType, allSupportedMediaTypes);
	}
{
		TypedValue context = state.getActiveContextObject();
		Object targetObject = context.getValue();
		TypeDescriptor targetObjectTypeDescriptor = context.getTypeDescriptor();
		TypedValue indexValue = null;
		Object index = null;
		
		// This first part of the if clause prevents a 'double dereference' of the property (SPR-5847)
		if (targetObject instanceof Map && (children[0] instanceof PropertyOrFieldReference)) {
			PropertyOrFieldReference reference = (PropertyOrFieldReference)children[0];
			index = reference.getName();
			indexValue = new TypedValue(index);
		}
		else {
			// In case the map key is unqualified, we want it evaluated against the root object so 
			// temporarily push that on whilst evaluating the key
			try {
				state.pushActiveContextObject(state.getRootContextObject());
				indexValue = children[0].getValueInternal(state);
				index = indexValue.getValue();
			}
			finally {
				state.popActiveContextObject();
			}
		}

		// Indexing into a Map
		if (targetObjectTypeDescriptor.isMap()) {
			if (targetObject == null) {
			    // Current decision: attempt to index into null map == exception and does not just return null
				throw new SpelEvaluationException(getStartPosition(),SpelMessage.CANNOT_INDEX_INTO_NULL_VALUE);
			}
			Object possiblyConvertedKey = index;
			if (targetObjectTypeDescriptor.isMapEntryTypeKnown()) {
				possiblyConvertedKey = state.convertValue(index,TypeDescriptor.valueOf(targetObjectTypeDescriptor.getMapKeyType()));
			}
			Object o = ((Map<?, ?>) targetObject).get(possiblyConvertedKey);
			if (targetObjectTypeDescriptor.isMapEntryTypeKnown()) {
				return new TypedValue(o, targetObjectTypeDescriptor.getMapValueTypeDescriptor());
			} else {
				return new TypedValue(o);
			}
		}
		
		if (targetObject == null) {
			throw new SpelEvaluationException(getStartPosition(),SpelMessage.CANNOT_INDEX_INTO_NULL_VALUE);
		}
		
		// if the object is something that looks indexable by an integer, attempt to treat the index value as a number
		if ((targetObject instanceof Collection ) || targetObject.getClass().isArray() || targetObject instanceof String) {
			int idx = (Integer)state.convertValue(index, TypeDescriptor.valueOf(Integer.class));		
			if (targetObject.getClass().isArray()) {
				return new TypedValue(accessArrayElement(targetObject, idx),TypeDescriptor.valueOf(targetObjectTypeDescriptor.getElementType()));
			} else if (targetObject instanceof Collection) {
				Collection c = (Collection) targetObject;
				if (idx >= c.size()) {
					if (state.getConfiguration().isAutoGrowCollections()) {
						// Grow the collection
						Object newCollectionElement = null;
						try {
							int newElements = idx-c.size();
							Class elementClass = targetObjectTypeDescriptor.getElementType();
							if (elementClass == null) {
								throw new SpelEvaluationException(getStartPosition(), SpelMessage.UNABLE_TO_GROW_COLLECTION_UNKNOWN_ELEMENT_TYPE);	
							}
							while (newElements>0) {
								c.add(elementClass.newInstance());
								newElements--;
							}
							newCollectionElement = targetObjectTypeDescriptor.getElementType().newInstance();
						}
						catch (Exception ex) {
							throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.UNABLE_TO_GROW_COLLECTION);
						}
						c.add(newCollectionElement);
						return new TypedValue(newCollectionElement,TypeDescriptor.valueOf(targetObjectTypeDescriptor.getElementType()));
					}
					else {
						throw new SpelEvaluationException(getStartPosition(),SpelMessage.COLLECTION_INDEX_OUT_OF_BOUNDS, c.size(), idx);
					}
				}
				int pos = 0;
				for (Object o : c) {
					if (pos == idx) {
						return new TypedValue(o,TypeDescriptor.valueOf(targetObjectTypeDescriptor.getElementType()));
					}
					pos++;
				}
			} else if (targetObject instanceof String) {
				String ctxString = (String) targetObject;
				if (idx >= ctxString.length()) {
					throw new SpelEvaluationException(getStartPosition(),SpelMessage.STRING_INDEX_OUT_OF_BOUNDS, ctxString.length(), idx);
				}
				return new TypedValue(String.valueOf(ctxString.charAt(idx)));
			}
		}
		
		// Try and treat the index value as a property of the context object
		// TODO could call the conversion service to convert the value to a String		
		if (indexValue.getTypeDescriptor().getType()==String.class) {
			Class<?> targetObjectRuntimeClass = getObjectClass(targetObject);
			String name = (String)indexValue.getValue();
			EvaluationContext eContext = state.getEvaluationContext();

			try {
				if (cachedReadName!=null && cachedReadName.equals(name) && cachedReadTargetType!=null && cachedReadTargetType.equals(targetObjectRuntimeClass)) {
					// it is OK to use the cached accessor
					return cachedReadAccessor.read(eContext, targetObject, name);
				}
				
				List<PropertyAccessor> accessorsToTry = AstUtils.getPropertyAccessorsToTry(targetObjectRuntimeClass, state);
		
				if (accessorsToTry != null) {			
					for (PropertyAccessor accessor : accessorsToTry) {
							if (accessor.canRead(eContext, targetObject, name)) {
								if (accessor instanceof ReflectivePropertyAccessor) {
									accessor = ((ReflectivePropertyAccessor)accessor).createOptimalAccessor(eContext, targetObject, name);
								}
								this.cachedReadAccessor = accessor;
								this.cachedReadName = name;
								this.cachedReadTargetType = targetObjectRuntimeClass;
								return accessor.read(eContext, targetObject, name);
							}
					}
				}
			} catch (AccessException e) {
				throw new SpelEvaluationException(getStartPosition(), e, SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, targetObjectTypeDescriptor.asString());
			}
		}
			
		throw new SpelEvaluationException(getStartPosition(),SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, targetObjectTypeDescriptor.asString());
	}
{
		if (this.configAttributes == null) {
			this.configAttributes = retrieveTransactionConfigurationAttributes(testContext.getTestClass());
		}
		String transactionManagerName = this.configAttributes.getTransactionManagerName();
		try {
			return (PlatformTransactionManager) testContext.getApplicationContext().getBean(
					transactionManagerName, PlatformTransactionManager.class);
		}
		catch (BeansException ex) {
			if (logger.isWarnEnabled()) {
				logger.warn("Caught exception while retrieving transaction manager with bean name [" +
						transactionManagerName + "] for test context [" + testContext + "]", ex);
			}
			throw ex;
		}
	}
{
		while (classToIntrospect != null) {
			Type[] ifcs = classToIntrospect.getGenericInterfaces();
			for (Type ifc : ifcs) {
				if (ifc instanceof ParameterizedType) {
					ParameterizedType paramIfc = (ParameterizedType) ifc;
					Type rawType = paramIfc.getRawType();
					if (genericIfc.equals(rawType)) {
						Type[] typeArgs = paramIfc.getActualTypeArguments();
						Class[] result = new Class[typeArgs.length];
						for (int i = 0; i < typeArgs.length; i++) {
							Type arg = typeArgs[i];
							if (arg instanceof TypeVariable) {
								TypeVariable tv = (TypeVariable) arg;
								arg = getTypeVariableMap(ownerClass).get(tv);
								if (arg == null) {
									arg = extractBoundForTypeVariable(tv);
								}
							}
							result[i] = (arg instanceof Class ? (Class) arg : Object.class);
						}
						return result;
					}
					else if (genericIfc.isAssignableFrom((Class) rawType)) {
						return doResolveTypeArguments(ownerClass, (Class) rawType, genericIfc);
					}
				}
				else if (genericIfc.isAssignableFrom((Class) ifc)) {
					return doResolveTypeArguments(ownerClass, (Class) ifc, genericIfc);
				}
			}
			classToIntrospect = classToIntrospect.getSuperclass();
		}
		return null;
	}
{
		byte[] boundary = generateBoundary();
		HttpHeaders headers = outputMessage.getHeaders();
		MediaType contentType = headers.getContentType();
		if (contentType != null) {
			String boundaryString = new String(boundary, "US-ASCII");
			Map<String, String> params = Collections.singletonMap("boundary", boundaryString);
			contentType = new MediaType(contentType.getType(), contentType.getSubtype(), params);
			headers.setContentType(contentType);
		}
		OutputStream os = outputMessage.getBody();
		for (Map.Entry<String, List<Part>> entry : map.entrySet()) {
			String name = entry.getKey();
			for (Part part : entry.getValue()) {
				part.write(boundary, name, os);
			}
		}
		os.write('-');
		os.write('-');
		os.write(boundary);
		os.write('-');
		os.write('-');
		os.write('\r');
		os.write('\n');
	}
{
		byte[] boundary = generateBoundary();
		HttpHeaders headers = outputMessage.getHeaders();
		MediaType contentType = headers.getContentType();
		if (contentType != null) {
			String boundaryString = new String(boundary, "US-ASCII");
			Map<String, String> params = Collections.singletonMap("boundary", boundaryString);
			contentType = new MediaType(contentType.getType(), contentType.getSubtype(), params);
			headers.setContentType(contentType);
		}
		OutputStream os = outputMessage.getBody();
		for (Map.Entry<String, List<Part>> entry : map.entrySet()) {
			String name = entry.getKey();
			for (Part part : entry.getValue()) {
				part.write(boundary, name, os);
			}
		}
		os.write('-');
		os.write('-');
		os.write(boundary);
		os.write('-');
		os.write('-');
		os.write('\r');
		os.write('\n');
	}
{
		byte[] boundary = generateBoundary();
		HttpHeaders headers = outputMessage.getHeaders();
		MediaType contentType = headers.getContentType();
		if (contentType != null) {
			String boundaryString = new String(boundary, "US-ASCII");
			Map<String, String> params = Collections.singletonMap("boundary", boundaryString);
			contentType = new MediaType(contentType.getType(), contentType.getSubtype(), params);
			headers.setContentType(contentType);
		}
		OutputStream os = outputMessage.getBody();
		for (Map.Entry<String, List<Part>> entry : map.entrySet()) {
			String name = entry.getKey();
			for (Part part : entry.getValue()) {
				part.write(boundary, name, os);
			}
		}
		os.write('-');
		os.write('-');
		os.write(boundary);
		os.write('-');
		os.write('-');
		os.write('\r');
		os.write('\n');
	}
{
		this.declaredRowMappers.put(parameterName, rowMapper);
		if (logger.isDebugEnabled()) {
			logger.debug("Added row mapper for [" + getProcedureName() + "]: " + parameterName);
		}
	}
{
		TypedValue currentContext = state.getActiveContextObject();
		Object[] arguments = new Object[getChildCount()];
		for (int i = 0; i < arguments.length; i++) {
			// Make the root object the active context again for evaluating the parameter
			// expressions
			try {
				state.pushActiveContextObject(state.getRootContextObject());
				arguments[i] = children[i].getValueInternal(state).getValue();
			} finally {
				state.popActiveContextObject();	
			}
		}
		if (currentContext.getValue() == null) {
			if (nullSafe) {
				return TypedValue.NULL;
			} else {
				throw new SpelEvaluationException(getStartPosition(), SpelMessage.METHOD_CALL_ON_NULL_OBJECT_NOT_ALLOWED,
						FormatHelper.formatMethodForMessage(name, getTypes(arguments)));
			}
		}

		MethodExecutor executorToUse = this.cachedExecutor;
		if (executorToUse != null) {
			try {
				return executorToUse.execute(
						state.getEvaluationContext(), state.getActiveContextObject().getValue(), arguments);
			}
			catch (AccessException ae) {
				// Two reasons this can occur:
				// 1. the method invoked actually threw a real exception
				// 2. the method invoked was not passed the arguments it expected and has become 'stale'
				
				// In the first case we should not retry, in the second case we should see if there is a 
				// better suited method.
				
				// To determine which situation it is, the AccessException will contain a cause - this
				// will be the exception thrown by the reflective invocation.  Inside this exception there
				// may or may not be a root cause.  If there is a root cause it is a user created exception.
				// If there is no root cause it was a reflective invocation problem.
				
				Throwable causeOfAccessException = ae.getCause();
				Throwable rootCause = (causeOfAccessException==null?null:causeOfAccessException.getCause());
				if (rootCause!=null) {
					// User exception was the root cause - exit now
					if (rootCause instanceof RuntimeException) {
						throw (RuntimeException)rootCause;
					} else {
						throw new SpelEvaluationException( getStartPosition(), rootCause, SpelMessage.EXCEPTION_DURING_METHOD_INVOCATION,
							this.name, state.getActiveContextObject().getValue().getClass().getName(), rootCause.getMessage());
					}
				}
				
				// at this point we know it wasn't a user problem so worth a retry if a better candidate can be found
				this.cachedExecutor = null;
			}
		}

		// either there was no accessor or it no longer existed
		executorToUse = findAccessorForMethod(this.name, getTypes(arguments), state);
		this.cachedExecutor = executorToUse;
		try {
			return executorToUse.execute(
					state.getEvaluationContext(), state.getActiveContextObject().getValue(), arguments);
		} catch (AccessException ae) {
			throw new SpelEvaluationException( getStartPosition(), ae, SpelMessage.EXCEPTION_DURING_METHOD_INVOCATION,
					this.name, state.getActiveContextObject().getValue().getClass().getName(), ae.getMessage());
		}
	}
{
		Assert.notNull(uri, "'uri' must not be null");
		Assert.hasLength(encoding, "'encoding' must not be empty");
		Matcher m = URI_PATTERN.matcher(uri);
		if (m.matches()) {
			String scheme = m.group(2);
			String authority = m.group(3);
			String userinfo = m.group(5);
			String host = m.group(6);
			String port = m.group(8);
			String path = m.group(9);
			String query = m.group(11);
			String fragment = m.group(13);

			StringBuilder sb = new StringBuilder();

			if (scheme != null) {
				sb.append(encodeScheme(scheme, encoding));
				sb.append(':');
			}

			if (authority != null) {
				sb.append("//");
				if (userinfo != null) {
					sb.append(encodeUserInfo(userinfo, encoding));
					sb.append('@');
				}
				if (host != null) {
					sb.append(encodeHost(host, encoding));
				}
				if (port != null) {
					sb.append(':');
					sb.append(encodePort(port, encoding));
				}
			}

			sb.append(encodePath(path, encoding));

			if (query != null) {
				sb.append('?');
				sb.append(encodeQuery(query, encoding));
			}

			if (fragment != null) {
				sb.append('#');
				sb.append(encodeFragment(fragment, encoding));
			}

			return sb.toString();
		} else {
			throw new IllegalArgumentException("[" + uri + "] is not a valid URI");
		}
	}
{
		try {
			// JiBX does not support DOM natively, so we write to a buffer first, and transform that to the Node
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			marshalOutputStream(graph, os);
			ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
			Transformer transformer = this.transformerFactory.newTransformer();
			transformer.transform(new StreamSource(is), new DOMResult(node));
		}
		catch (Exception ex) {
			throw new MarshallingFailureException("JiBX marshalling exception", ex);
		}
	}
{
		try {
			Transformer transformer = transformerFactory.newTransformer();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			transformer.transform(new DOMSource(node), new StreamResult(os));
			ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
			return unmarshalInputStream(is);
		}
		catch (Exception ex) {
			throw new UnmarshallingFailureException("JiBX unmarshalling exception", ex);
		}
	}
{

		runTestClassAndAssertStats(CleanTestCase.class, 1);
		assertCacheStats("after clean test class", 1, cacheHits.get(), cacheMisses.incrementAndGet());

		runTestClassAndAssertStats(ClassLevelDirtiesContextWithCleanMethodsAndDefaultModeTestCase.class, 1);
		assertCacheStats("after class-level @DirtiesContext with clean test method and default class mode", 0,
			cacheHits.incrementAndGet(), cacheMisses.get());

		runTestClassAndAssertStats(CleanTestCase.class, 1);
		assertCacheStats("after clean test class", 1, cacheHits.get(), cacheMisses.incrementAndGet());

		runTestClassAndAssertStats(ClassLevelDirtiesContextWithCleanMethodsAndAfterClassModeTestCase.class, 1);
		assertCacheStats("after class-level @DirtiesContext with clean test method and AFTER_CLASS mode", 0,
			cacheHits.incrementAndGet(), cacheMisses.get());

		runTestClassAndAssertStats(CleanTestCase.class, 1);
		assertCacheStats("after clean test class", 1, cacheHits.get(), cacheMisses.incrementAndGet());

		runTestClassAndAssertStats(ClassLevelDirtiesContextWithAfterEachTestMethodModeTestCase.class, 3);
		assertCacheStats("after class-level @DirtiesContext with clean test method and AFTER_EACH_TEST_METHOD mode", 0,
			cacheHits.incrementAndGet(), cacheMisses.addAndGet(2));

		runTestClassAndAssertStats(CleanTestCase.class, 1);
		assertCacheStats("after clean test class", 1, cacheHits.get(), cacheMisses.incrementAndGet());

		runTestClassAndAssertStats(ClassLevelDirtiesContextWithDirtyMethodsTestCase.class, 1);
		assertCacheStats("after class-level @DirtiesContext with dirty test method", 0, cacheHits.incrementAndGet(),
			cacheMisses.get());

		runTestClassAndAssertStats(ClassLevelDirtiesContextWithDirtyMethodsTestCase.class, 1);
		assertCacheStats("after class-level @DirtiesContext with dirty test method", 0, cacheHits.get(),
			cacheMisses.incrementAndGet());

		runTestClassAndAssertStats(ClassLevelDirtiesContextWithDirtyMethodsTestCase.class, 1);
		assertCacheStats("after class-level @DirtiesContext with dirty test method", 0, cacheHits.get(),
			cacheMisses.incrementAndGet());

		runTestClassAndAssertStats(CleanTestCase.class, 1);
		assertCacheStats("after clean test class", 1, cacheHits.get(), cacheMisses.incrementAndGet());

		runTestClassAndAssertStats(ClassLevelDirtiesContextWithCleanMethodsAndAfterClassModeTestCase.class, 1);
		assertCacheStats("after class-level @DirtiesContext with clean test method and AFTER_CLASS mode", 0,
			cacheHits.incrementAndGet(), cacheMisses.get());
	}
{

		if (targetType == null || ClassUtils.isAssignableValue(targetType, value)) {
			return (T) value;
		}
		if (context != null) {
			return (T) context.getTypeConverter().convertValue(value, TypeDescriptor.valueOf(targetType));
		}
		throw new EvaluationException("Cannot convert value '" + value + "' to type '" + targetType.getName() + "'");
	}
{
		builder.append(this.type);
		builder.append('/');
		builder.append(this.subtype);
		for (Map.Entry<String, String> entry : this.parameters.entrySet()) {
			builder.append(';');
			builder.append(entry.getKey());
			builder.append('=');
			builder.append(entry.getValue());
		}
	}
{
			this.targetFactory = target;
			this.properties = properties;
			if (this.targetFactory instanceof EntityManagerFactoryInfo) {
				this.proxyClassLoader = ((EntityManagerFactoryInfo) this.targetFactory).getBeanClassLoader();
			}
			else {
				this.proxyClassLoader = EntityManagerFactory.class.getClassLoader();
			}
		}
{
		MetadataReader reader = this.metadataReaderFactory.getMetadataReader(classToImport);
		AnnotationMetadata metadata = reader.getAnnotationMetadata();
		if (!metadata.isAnnotated(Configuration.class.getName())) {
			this.problemReporter.error(
					new NonAnnotatedConfigurationProblem(metadata.getClassName(), reader.getResource(), metadata));
		}
		else {
			processConfigurationClass(new ConfigurationClass(reader, null));
		}
	}
{

		ShallowEtagResponseWrapper responseWrapper = new ShallowEtagResponseWrapper(response);
		filterChain.doFilter(request, responseWrapper);

		byte[] body = responseWrapper.toByteArray();
		String responseETag = generateETagHeaderValue(body);
		response.setHeader(HEADER_ETAG, responseETag);

		String requestETag = request.getHeader(HEADER_IF_NONE_MATCH);
		if (responseETag.equals(requestETag)) {
			if (logger.isTraceEnabled()) {
				logger.trace("ETag [" + responseETag + "] equal to If-None-Match, sending 304");
			}
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
		}
		else {
			if (logger.isTraceEnabled()) {
				logger.trace("ETag [" + responseETag + "] not equal to If-None-Match [" + requestETag +
						"], sending normal response");
			}
			response.setContentLength(body.length);
			FileCopyUtils.copy(body, response.getOutputStream());
		}
	}
{
		if (JAXBElement.class.isAssignableFrom(clazz)) {
			return true;
		}
		else if (AnnotationUtils.findAnnotation(clazz, XmlRootElement.class) != null) {
			return true;
		}
		if (StringUtils.hasLength(this.contextPath)) {
			String packageName = ClassUtils.getPackageName(clazz);
			String[] contextPaths = StringUtils.tokenizeToStringArray(this.contextPath, ":");
			for (String contextPath : contextPaths) {
				if (contextPath.equals(packageName)) {
					return true;
				}
			}
			return false;
		}
		else if (!ObjectUtils.isEmpty(this.classesToBeBound)) {
			return Arrays.asList(this.classesToBeBound).contains(clazz);
		}
		return false;
	}
{
		try {
			Class mbeanClass = JBossWorkManagerUtils.class.getClassLoader().loadClass(JBOSS_WORK_MANAGER_MBEAN_CLASS_NAME);
			InitialContext jndiContext = new InitialContext();
			MBeanServerConnection mconn = (MBeanServerConnection) jndiContext.lookup(MBEAN_SERVER_CONNECTION_JNDI_NAME);
			ObjectName objectName = ObjectName.getInstance(WORK_MANAGER_OBJECT_NAME);
			Object workManagerMBean = MBeanServerInvocationHandler.newProxyInstance(mconn, objectName, mbeanClass, false);
			Method getInstanceMethod = workManagerMBean.getClass().getMethod("getInstance", new Class[0]);
			return (WorkManager) getInstanceMethod.invoke(workManagerMBean, new Object[0]);
		}
		catch (Exception ex) {
			throw new IllegalStateException(
					"Could not initialize JBossWorkManagerTaskExecutor because JBoss API is not available: " + ex);
		}
	}
{
		Object source = parserContext.extractSource(element);
		if (this.handlerAdapterBeanName == null) {
			RootBeanDefinition handlerAdapterDef = new RootBeanDefinition(SimpleControllerHandlerAdapter.class);
			handlerAdapterDef.setSource(source);
			handlerAdapterDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			this.handlerAdapterBeanName = parserContext.getReaderContext().registerWithGeneratedName(handlerAdapterDef);
			parserContext.registerComponent(new BeanComponentDefinition(handlerAdapterDef, handlerAdapterBeanName));
		}
		RootBeanDefinition handlerMappingDef;
		if (this.handlerMappingBeanName == null) {
			handlerMappingDef = new RootBeanDefinition(SimpleUrlHandlerMapping.class);
			handlerMappingDef.setSource(source);
			handlerMappingDef.getPropertyValues().add("order", "1");
			handlerMappingDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			this.handlerMappingBeanName = parserContext.getReaderContext().registerWithGeneratedName(handlerMappingDef);
			parserContext.registerComponent(new BeanComponentDefinition(handlerMappingDef, handlerMappingBeanName));
		} else {
			handlerMappingDef = (RootBeanDefinition) parserContext.getReaderContext().getRegistry().getBeanDefinition(this.handlerMappingBeanName);
		}
		RootBeanDefinition viewControllerDef = new RootBeanDefinition(ParameterizableViewController.class);
		viewControllerDef.setSource(source);
		if (element.hasAttribute("view-name")) {
			viewControllerDef.getPropertyValues().add("viewName", element.getAttribute("view-name"));			
		}
		Map<String, BeanDefinition> urlMap;
		if (handlerMappingDef.getPropertyValues().contains("urlMap")) {
			urlMap = (Map<String, BeanDefinition>) handlerMappingDef.getPropertyValues().getPropertyValue("urlMap").getValue();
		} else {
			urlMap = new ManagedMap<String, BeanDefinition>();
			handlerMappingDef.getPropertyValues().add("urlMap", urlMap);			
		}
		urlMap.put(element.getAttribute("path"), viewControllerDef);
		return null;
	}
{
		Object source = parserContext.extractSource(element);
		if (this.handlerAdapterBeanName == null) {
			RootBeanDefinition handlerAdapterDef = new RootBeanDefinition(SimpleControllerHandlerAdapter.class);
			handlerAdapterDef.setSource(source);
			handlerAdapterDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			this.handlerAdapterBeanName = parserContext.getReaderContext().registerWithGeneratedName(handlerAdapterDef);
			parserContext.registerComponent(new BeanComponentDefinition(handlerAdapterDef, handlerAdapterBeanName));
		}
		RootBeanDefinition handlerMappingDef;
		if (this.handlerMappingBeanName == null) {
			handlerMappingDef = new RootBeanDefinition(SimpleUrlHandlerMapping.class);
			handlerMappingDef.setSource(source);
			handlerMappingDef.getPropertyValues().add("order", "1");
			handlerMappingDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			this.handlerMappingBeanName = parserContext.getReaderContext().registerWithGeneratedName(handlerMappingDef);
			parserContext.registerComponent(new BeanComponentDefinition(handlerMappingDef, handlerMappingBeanName));
		} else {
			handlerMappingDef = (RootBeanDefinition) parserContext.getReaderContext().getRegistry().getBeanDefinition(this.handlerMappingBeanName);
		}
		RootBeanDefinition viewControllerDef = new RootBeanDefinition(ParameterizableViewController.class);
		viewControllerDef.setSource(source);
		if (element.hasAttribute("view-name")) {
			viewControllerDef.getPropertyValues().add("viewName", element.getAttribute("view-name"));			
		}
		Map<String, BeanDefinition> urlMap;
		if (handlerMappingDef.getPropertyValues().contains("urlMap")) {
			urlMap = (Map<String, BeanDefinition>) handlerMappingDef.getPropertyValues().getPropertyValue("urlMap").getValue();
		} else {
			urlMap = new ManagedMap<String, BeanDefinition>();
			handlerMappingDef.getPropertyValues().add("urlMap", urlMap);			
		}
		urlMap.put(element.getAttribute("path"), viewControllerDef);
		return null;
	}
{
		Set<ConstraintViolation<Object>> result = this.validator.validate(bean);
		if (!result.isEmpty()) {
			StringBuilder sb = new StringBuilder("Bean state is invalid: ");
			for (Iterator<ConstraintViolation<Object>> it = result.iterator(); it.hasNext();) {
				ConstraintViolation<Object> violation = it.next();
				sb.append(violation.getPropertyPath()).append(" - ").append(violation.getMessage());
				if (it.hasNext()) {
					sb.append("; ");
				}
			}
			throw new BeanInitializationException(sb.toString());
		}
		return bean;
	}
{

		Map<String, Object> results = new LinkedHashMap<String, Object>();
		for (String beanName : getBeanNamesForType(Object.class, includeNonSingletons, allowEagerInit)) {
			if (findAnnotationOnBean(beanName, annotationType) != null) {
				results.put(beanName, getBean(beanName));
			}
		}
		return results;
	}
{

		Map<String, Object> results = new LinkedHashMap<String, Object>();
		for (String beanName : this.beans.keySet()) {
			if (findAnnotationOnBean(beanName, annotationType) != null) {
				results.put(beanName, getBean(beanName));
			}
		}
		return results;
	}
{
		if (logger.isDebugEnabled()) {
			logger.debug("Looking for Converter to convert from " + sourceType + " to " + targetType);
		}
		Class<?> sourceObjectType = sourceType.getObjectType();
		if (sourceObjectType.isInterface()) {
			LinkedList<Class<?>> classQueue = new LinkedList<Class<?>>();
			classQueue.addFirst(sourceObjectType);
			while (!classQueue.isEmpty()) {
				Class<?> currentClass = classQueue.removeLast();
				Map<Class<?>, MatchableConverters> converters = getTargetConvertersForSource(currentClass);
				GenericConverter converter = getMatchingConverterForTarget(sourceType, targetType, converters);
				if (converter != null) {
					return converter;
				}
				Class<?>[] interfaces = currentClass.getInterfaces();
				for (Class<?> ifc : interfaces) {
					classQueue.addFirst(ifc);
				}
			}
			Map<Class<?>, MatchableConverters> objectConverters = getTargetConvertersForSource(Object.class);
			return getMatchingConverterForTarget(sourceType, targetType, objectConverters);
		}
		else {
			LinkedList<Class<?>> classQueue = new LinkedList<Class<?>>();
			classQueue.addFirst(sourceObjectType);
			while (!classQueue.isEmpty()) {
				Class<?> currentClass = classQueue.removeLast();
				Map<Class<?>, MatchableConverters> converters = getTargetConvertersForSource(currentClass);
				GenericConverter converter = getMatchingConverterForTarget(sourceType, targetType, converters);
				if (converter != null) {
					return converter;
				}
				if (currentClass.isArray()) {
					Class<?> componentType = ClassUtils.resolvePrimitiveIfNecessary(currentClass.getComponentType());
					if (componentType.getSuperclass() != null) {
						classQueue.addFirst(Array.newInstance(componentType.getSuperclass(), 0).getClass());
					}
				}
				else {
					Class<?>[] interfaces = currentClass.getInterfaces();
					for (Class<?> ifc : interfaces) {
						classQueue.addFirst(ifc);
					}
					if (currentClass.getSuperclass() != null) {
						classQueue.addFirst(currentClass.getSuperclass());
					}
				}
			}
			return null;
		}
	}
{
		Map<String, Lifecycle> lifecycleBeans = getLifecycleBeans();
		Map<Integer, LifecycleGroup> phases = new HashMap<Integer, LifecycleGroup>();
		for (Map.Entry<String, Lifecycle> entry : lifecycleBeans.entrySet()) {
			Lifecycle lifecycle = entry.getValue();
			int shutdownOrder = getPhase(lifecycle);
			LifecycleGroup group = phases.get(shutdownOrder);
			if (group == null) {
				group = new LifecycleGroup(shutdownOrder, this.timeoutPerShutdownPhase, lifecycleBeans);
				phases.put(shutdownOrder, group);
			}
			group.add(entry.getKey(), lifecycle);
		}
		if (phases.size() > 0) {
			List<Integer> keys = new ArrayList<Integer>(phases.keySet());
			Collections.sort(keys, Collections.reverseOrder());
			for (Integer key : keys) {
				phases.get(key).stop();
			}
		}
		this.running = false;
	}
{

		boolean actualNewSynchronization = newSynchronization &&
				!TransactionSynchronizationManager.isSynchronizationActive();
		return new DefaultTransactionStatus(
				transaction, newTransaction, actualNewSynchronization,
				definition.isReadOnly(), debug, suspendedResources);
	}
{

		boolean actualNewSynchronization = newSynchronization &&
				!TransactionSynchronizationManager.isSynchronizationActive();
		try {
			if (actualNewSynchronization) {
				TransactionSynchronizationManager.setActualTransactionActive(transaction != null);
				TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(
						(definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT) ?
								definition.getIsolationLevel() : null);
				TransactionSynchronizationManager.setCurrentTransactionReadOnly(definition.isReadOnly());
				TransactionSynchronizationManager.setCurrentTransactionName(definition.getName());
				TransactionSynchronizationManager.initSynchronization();
			}
			return new DefaultTransactionStatus(
					transaction, newTransaction, actualNewSynchronization,
					definition.isReadOnly(), debug, suspendedResources);
		}
		catch (Error err) {
			// Can only really be an OutOfMemoryError...
			if (actualNewSynchronization) {
				TransactionSynchronizationManager.clear();
			}
			throw err;
		}
	}
{

		boolean actualNewSynchronization = newSynchronization &&
				!TransactionSynchronizationManager.isSynchronizationActive();
		try {
			if (actualNewSynchronization) {
				TransactionSynchronizationManager.setActualTransactionActive(transaction != null);
				TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(
						(definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT) ?
								definition.getIsolationLevel() : null);
				TransactionSynchronizationManager.setCurrentTransactionReadOnly(definition.isReadOnly());
				TransactionSynchronizationManager.setCurrentTransactionName(definition.getName());
				TransactionSynchronizationManager.initSynchronization();
			}
			return new DefaultTransactionStatus(
					transaction, newTransaction, actualNewSynchronization,
					definition.isReadOnly(), debug, suspendedResources);
		}
		catch (Error err) {
			// Can only really be an OutOfMemoryError...
			if (actualNewSynchronization) {
				TransactionSynchronizationManager.clear();
			}
			throw err;
		}
	}
{
		if (other != null) {
			for (Map.Entry<Integer, ValueHolder> entry : other.indexedArgumentValues.entrySet()) {
				addOrMergeIndexedArgumentValue(entry.getKey(), entry.getValue().copy());
			}
			for (ValueHolder valueHolder : other.genericArgumentValues) {
				if (!this.genericArgumentValues.contains(valueHolder)) {
					this.genericArgumentValues.add(valueHolder.copy());
				}
			}
		}
	}
{
		GenericConversionService conversionService = new GenericConversionService();
		conversionService.addGenericConverter(new ArrayToArrayConverter(conversionService));
		conversionService.addGenericConverter(new ArrayToCollectionConverter(conversionService));
		conversionService.addGenericConverter(new ArrayToMapConverter(conversionService));
		conversionService.addGenericConverter(new ArrayToObjectConverter(conversionService));
		conversionService.addGenericConverter(new CollectionToCollectionConverter(conversionService));
		conversionService.addGenericConverter(new CollectionToArrayConverter(conversionService));
		conversionService.addGenericConverter(new CollectionToMapConverter(conversionService));
		conversionService.addGenericConverter(new CollectionToObjectConverter(conversionService));
		conversionService.addGenericConverter(new MapToMapConverter(conversionService));
		conversionService.addGenericConverter(new MapToArrayConverter(conversionService));
		conversionService.addGenericConverter(new MapToCollectionConverter(conversionService));
		conversionService.addGenericConverter(new MapToObjectConverter(conversionService));
		conversionService.addGenericConverter(new ObjectToArrayConverter(conversionService));
		conversionService.addGenericConverter(new ObjectToCollectionConverter(conversionService));
		conversionService.addGenericConverter(new ObjectToMapConverter(conversionService));
		conversionService.addConverter(new StringToBooleanConverter());
		conversionService.addConverter(new StringToCharacterConverter());
		conversionService.addConverter(new StringToLocaleConverter());
		conversionService.addConverter(new NumberToCharacterConverter());
		conversionService.addConverterFactory(new StringToNumberConverterFactory());
		conversionService.addConverterFactory(new StringToEnumConverterFactory());
		conversionService.addConverterFactory(new NumberToNumberConverterFactory());
		conversionService.addConverterFactory(new CharacterToNumberFactory());
		conversionService.addConverter(new ObjectToStringConverter());
		conversionService.addGenericConverter(new ObjectToObjectGenericConverter());
		conversionService.addGenericConverter(new IdToEntityConverter(conversionService));
		return conversionService;
	}
{
		synchronized(this) {
			if (DEFAULT_INSTANCE == null) {
				DEFAULT_INSTANCE = ConversionServiceFactory.createDefaultConversionService();
			}
		}
		return DEFAULT_INSTANCE;
	}
{
		ParameterizedBeanPropertyRowMapper<T> newInstance = new ParameterizedBeanPropertyRowMapper<T>();
		newInstance.setMappedClass(mappedClass);
		newInstance.setPrimitivesDefaultedForNullValue(primitivesDefaultedForNullValue);
		return newInstance;
	}
{
		Map<String, Lifecycle> lifecycleBeans = getLifecycleBeans();
		for (String beanName : new LinkedHashSet<String>(lifecycleBeans.keySet())) {
			doStart(lifecycleBeans, beanName);
		}
		this.running = true;
	}
{
		// Quick check on the concurrent map first, with minimal locking.
		InjectionMetadata metadata = this.injectionMetadataCache.get(clazz);
		if (metadata == null) {
			synchronized (this.injectionMetadataCache) {
				metadata = this.injectionMetadataCache.get(clazz);
				if (metadata == null) {
					LinkedList<InjectionMetadata.InjectedElement> elements = new LinkedList<InjectionMetadata.InjectedElement>();
					Class<?> targetClass = clazz;

					do {
						LinkedList<InjectionMetadata.InjectedElement> currElements = new LinkedList<InjectionMetadata.InjectedElement>();
						for (Field field : targetClass.getDeclaredFields()) {
							Annotation annotation = findAutowiredAnnotation(field);
							if (annotation != null) {
								if (Modifier.isStatic(field.getModifiers())) {
									if (logger.isWarnEnabled()) {
										logger.warn("Autowired annotation is not supported on static fields: " + field);
									}
									continue;
								}
								boolean required = determineRequiredStatus(annotation);
								currElements.add(new AutowiredFieldElement(field, required));
							}
						}
						for (Method method : targetClass.getDeclaredMethods()) {
							Annotation annotation = findAutowiredAnnotation(method);
							if (annotation != null && method.equals(ClassUtils.getMostSpecificMethod(method, clazz))) {
								if (Modifier.isStatic(method.getModifiers())) {
									if (logger.isWarnEnabled()) {
										logger.warn("Autowired annotation is not supported on static methods: " + method);
									}
									continue;
								}
								if (method.getParameterTypes().length == 0) {
									if (logger.isWarnEnabled()) {
										logger.warn("Autowired annotation should be used on methods with actual parameters: " + method);
									}
								}
								boolean required = determineRequiredStatus(annotation);
								PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
								currElements.add(new AutowiredMethodElement(method, required, pd));
							}
						}
						elements.addAll(0, currElements);
						targetClass = targetClass.getSuperclass();
					}
					while (targetClass != null && targetClass != Object.class);

					metadata = new InjectionMetadata(clazz, elements);
					this.injectionMetadataCache.put(clazz, metadata);
				}
			}
		}
		return metadata;
	}
{
		if (value == null) {
			return null;
		}
		try {
			String encoding = pageContext.getResponse().getCharacterEncoding();
			return UriUtils.encode(value, encoding);
		}
		catch (UnsupportedEncodingException ex) {
			throw new JspException(ex);
		}
	}
{
		if (value == null) {
			return null;
		}
		try {
			String encoding = pageContext.getResponse().getCharacterEncoding();
			return UriUtils.encode(value, encoding);
		}
		catch (UnsupportedEncodingException ex) {
			throw new JspException(ex);
		}
	}
{
		if (this.args != null) {
			for (int i = 0; i < this.args.length; i++) {
				Object arg = this.args[i];
				if (arg instanceof SqlParameterValue) {
					SqlParameterValue paramValue = (SqlParameterValue) arg;
					StatementCreatorUtils.setParameterValue(ps, i + 1, paramValue, paramValue.getValue());
				}
				else {
					StatementCreatorUtils.setParameterValue(ps, i + 1, SqlTypeValue.TYPE_UNKNOWN, arg);
				}
			}
		}
	}
{
		DocumentDefaultsDefinition defaults = new DocumentDefaultsDefinition();
		defaults.setLazyInit(root.getAttribute(DEFAULT_LAZY_INIT_ATTRIBUTE));
		defaults.setMerge(root.getAttribute(DEFAULT_MERGE_ATTRIBUTE));
		defaults.setAutowire(root.getAttribute(DEFAULT_AUTOWIRE_ATTRIBUTE));
		defaults.setDependencyCheck(root.getAttribute(DEFAULT_DEPENDENCY_CHECK_ATTRIBUTE));
		if (root.hasAttribute(DEFAULT_AUTOWIRE_CANDIDATES_ATTRIBUTE)) {
			defaults.setAutowireCandidates(root.getAttribute(DEFAULT_AUTOWIRE_CANDIDATES_ATTRIBUTE));
		}
		if (root.hasAttribute(DEFAULT_INIT_METHOD_ATTRIBUTE)) {
			defaults.setInitMethod(root.getAttribute(DEFAULT_INIT_METHOD_ATTRIBUTE));
		}
		if (root.hasAttribute(DEFAULT_DESTROY_METHOD_ATTRIBUTE)) {
			defaults.setDestroyMethod(root.getAttribute(DEFAULT_DESTROY_METHOD_ATTRIBUTE));
		}
		defaults.setSource(this.readerContext.extractSource(root));

		this.defaults = defaults;
		this.readerContext.fireDefaultsRegistered(defaults);
	}
{
		Annotation[] anns = getIntrospectedClass().getAnnotations();
		for (Annotation ann : anns) {
			if (ann.annotationType().getName().equals(annotationType)) {
				return AnnotationUtils.getAnnotationAttributes(ann, true);
			}
			for (Annotation metaAnn : ann.annotationType().getAnnotations()) {
				if (metaAnn.annotationType().getName().equals(annotationType)) {
					return AnnotationUtils.getAnnotationAttributes(metaAnn, true);
				}
			}
		}
		return null;
	}
{
		BeanDefinitionBuilder builder = createBeanBuilder(ConfigurableWebBindingInitializer.class, source);
		if (context.getRegistry().containsBeanDefinition("conversionService")) {
			builder.addPropertyReference("conversionService", "conversionService");
		} else {
			builder.addPropertyValue("conversionService", createFormattingConversionService(element, source, context));
		}
		return builder.getBeanDefinition();
	}
{
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(DefaultAnnotationHandlerMapping.class);
		builder.addPropertyValue("order", 0);
		builder.getRawBeanDefinition().setSource(source);
		return registerBeanDefinition(new BeanDefinitionHolder(builder.getBeanDefinition(), "defaultAnnotationHandlerMapping"), context);
	}
{

		Assert.notNull(url, "'url' must not be null");
		Assert.notNull(method, "'method' must not be null");
		ClientHttpResponse response = null;
		try {
			ClientHttpRequest request = createRequest(url, method);
			if (requestCallback != null) {
				requestCallback.doWithRequest(request);
			}
			response = request.execute();
			if (getErrorHandler().hasError(response)) {
				getErrorHandler().handleError(response);
			}
			if (responseExtractor != null) {
				return responseExtractor.extractData(response);
			}
			else {
				return null;
			}
		}
		catch (IOException ex) {
			throw new ResourceAccessException("I/O error: " + ex.getMessage(), ex);
		}
		finally {
			if (response != null) {
				response.close();
			}
		}
	}
{
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);

		// Truncate to the next whole second
		calendar.add(Calendar.SECOND, 1);
		calendar.set(Calendar.MILLISECOND, 0);

		List<Integer> resets = new ArrayList<Integer>();

		int second = calendar.get(Calendar.SECOND);
		int updateSecond = findNext(this.seconds, second, 60, calendar, Calendar.SECOND, Collections.<Integer> emptyList());
		if (second == updateSecond) {
			resets.add(Calendar.SECOND);
		}

		int minute = calendar.get(Calendar.MINUTE);
		int updateMinute = findNext(this.minutes, minute, 60, calendar, Calendar.MINUTE, resets);
		if (minute == updateMinute) {
			resets.add(Calendar.MINUTE);
		}

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int updateHour = findNext(this.hours, hour, 24, calendar, Calendar.HOUR_OF_DAY, resets);
		if (hour == updateHour) {
			resets.add(Calendar.HOUR_OF_DAY);
		}

		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int updateDayOfMonth = findNextDay(calendar, this.daysOfMonth, dayOfMonth, daysOfWeek, dayOfWeek, 366, resets);
		if (dayOfMonth == updateDayOfMonth) {
			resets.add(Calendar.DAY_OF_MONTH);
		}

		int month = calendar.get(Calendar.MONTH);
		month = findNext(this.months, month, 12, calendar, Calendar.MONTH, resets);

		return calendar.getTime();
	}
{
		for (Iterator<InjectedElement> it = members.iterator(); it.hasNext();) {
			Member member = it.next().getMember();
			if (!beanDefinition.isExternallyManagedConfigMember(member)) {
				beanDefinition.registerExternallyManagedConfigMember(member);
			}
			else {
				it.remove();
			}
		}
	}
{
		if (logger.isDebugEnabled()) {
			logger.debug(MappingContextHolder.getLevel() + mapping);
		}
		mapping.map(sourceContext, targetContext, failures);
	}
{
		Expression sourceExp;
		try {
			sourceExp = sourceExpressionParser.parseExpression(sourceFieldExpression);
		} catch (ParseException e) {
			throw new IllegalArgumentException("The mapping source '" + sourceFieldExpression
					+ "' is not a parseable value expression", e);
		}
		Expression targetExp;
		try {
			targetExp = targetExpressionParser.parseExpression(targetFieldExpression);
		} catch (ParseException e) {
			throw new IllegalArgumentException("The mapping target '" + targetFieldExpression
					+ "' is not a parseable property expression", e);
		}
		SpelMapping mapping = new SpelMapping(sourceExp, targetExp);
		this.mappings.add(mapping);
		return mapping;
	}
{
		Expression sourceExp;
		try {
			sourceExp = sourceExpressionParser.parseExpression(sourceFieldExpression);
		} catch (ParseException e) {
			throw new IllegalArgumentException("The mapping source '" + sourceFieldExpression
					+ "' is not a parseable value expression", e);
		}
		Expression targetExp;
		try {
			targetExp = targetExpressionParser.parseExpression(targetFieldExpression);
		} catch (ParseException e) {
			throw new IllegalArgumentException("The mapping target '" + targetFieldExpression
					+ "' is not a parseable property expression", e);
		}
		SpelMapping mapping = new SpelMapping(sourceExp, targetExp);
		this.mappings.add(mapping);
		return mapping;
	}
{

		Assert.notNull(bean, "Bean must not be null");
		this.bean = bean;
		this.beanName = beanName;
		this.invokeDisposableBean =
				(this.bean instanceof DisposableBean && !beanDefinition.isExternallyManagedDestroyMethod("destroy"));
		this.nonPublicAccessAllowed = beanDefinition.isNonPublicAccessAllowed();
		this.acc = acc;
		
		final String destroyMethodName = beanDefinition.getDestroyMethodName();
		if (destroyMethodName != null && !(this.invokeDisposableBean && "destroy".equals(destroyMethodName)) &&
				!beanDefinition.isExternallyManagedDestroyMethod(destroyMethodName)) {
			this.destroyMethodName = destroyMethodName;
			try {
				if (System.getSecurityManager() != null) {
					AccessController.doPrivileged(new PrivilegedAction<Object>() {
						public Object run() {
							destroyMethod = (nonPublicAccessAllowed ?
									BeanUtils.findMethodWithMinimalParameters(bean.getClass(), destroyMethodName) :
									BeanUtils.findMethodWithMinimalParameters(bean.getClass().getMethods(), destroyMethodName));
							return null;
						}
					});
				}
				else {
					this.destroyMethod = (this.nonPublicAccessAllowed ?
							BeanUtils.findMethodWithMinimalParameters(bean.getClass(), destroyMethodName) :
							BeanUtils.findMethodWithMinimalParameters(bean.getClass().getMethods(), destroyMethodName));
				}
			}
			catch (IllegalArgumentException ex) {
				throw new BeanDefinitionValidationException("Couldn't find a unique destroy method on bean with name '" +
						this.beanName + ": " + ex.getMessage());
			}
			if (this.destroyMethod == null) {
				if (beanDefinition.isEnforceDestroyMethod()) {
					throw new BeanDefinitionValidationException("Couldn't find a destroy method named '" +
							destroyMethodName + "' on bean with name '" + beanName + "'");
				}
			}
			else {
				Class[] paramTypes = this.destroyMethod.getParameterTypes();
				if (paramTypes.length > 1) {
					throw new BeanDefinitionValidationException("Method '" + destroyMethodName + "' of bean '" +
							beanName + "' has more than one parameter - not supported as destroy method");
				}
				else if (paramTypes.length == 1 && !paramTypes[0].equals(boolean.class)) {
					throw new BeanDefinitionValidationException("Method '" + destroyMethodName + "' of bean '" +
							beanName + "' has a non-boolean parameter - not supported as destroy method");
				}
			}
		}
		this.beanPostProcessors = filterPostProcessors(postProcessors);
	}
{
		if (source == null) {
			return null;
		}
		// TODO - could detect cyclical reference here if had a mapping context? (source should not equal currently mapped object) 
		Object target = this.mappingTargetFactory.createTarget(source, sourceType, targetType);
		return this.mapper.map(source, target);
	}
{
		EvaluationContext sourceContext = getMappingContext(source);
		EvaluationContext targetContext = getMappingContext(target);
		List<MappingFailure> failures = new LinkedList<MappingFailure>();
		for (SpelMapping mapping : this.mappings) {
			mapping.map(sourceContext, targetContext, failures);
		}
		Set<SpelMapping> autoMappings = getAutoMappings(sourceContext, targetContext);
		for (SpelMapping mapping : autoMappings) {
			mapping.map(sourceContext, targetContext, failures);
		}
		if (!failures.isEmpty()) {
			throw new MappingException(failures);
		}
		return target;
	}
{
		GenericConverter converter = findConverterByClassPair(sourceType.getObjectType(), targetType.getObjectType());
		if (converter != null) {
			return converter;
		} else if (this.parent != null && this.parent.canConvert(sourceType, targetType)) {
			return this.parentConverterAdapter;
		} else {
			if (sourceType.isAssignableTo(targetType)) {
				return NO_OP_CONVERTER;
			} else {
				return null;
			}
		}
	}
{
		Assert.notNull(sourceType, "The sourceType to convert from is required");
		Assert.notNull(targetType, "The targetType to convert to is required");
		if (targetType == TypeDescriptor.NULL) {
			return true;
		}
		return getConverter(sourceType, targetType) != null;
	}
{
		addConverter(new StringToBooleanConverter());
		addConverter(new StringToCharacterConverter());
		addConverter(new StringToLocaleConverter());
		addConverter(new NumberToCharacterConverter());
		addConverter(new ObjectToStringConverter());
		addConverterFactory(new StringToNumberConverterFactory());
		addConverterFactory(new StringToEnumConverterFactory());
		addConverterFactory(new NumberToNumberConverterFactory());
		addConverterFactory(new CharacterToNumberFactory());
	}
{
		addGenericConverter(Object[].class, Object[].class, new ArrayToArrayGenericConverter(this));
		addGenericConverter(Object[].class, Collection.class, new ArrayToCollectionGenericConverter(this));
		addGenericConverter(Object[].class, Map.class, new ArrayToMapGenericConverter(this));
		addGenericConverter(Object[].class, Object.class, new ArrayToObjectGenericConverter(this));
		addGenericConverter(Collection.class, Collection.class, new CollectionToCollectionGenericConverter(this));
		addGenericConverter(Collection.class, Object[].class, new CollectionToArrayGenericConverter(this));
		addGenericConverter(Collection.class, Map.class, new CollectionToMapGenericConverter(this));
		addGenericConverter(Collection.class, Object.class, new CollectionToObjectGenericConverter(this));
		addGenericConverter(Map.class, Map.class, new MapToMapGenericConverter(this));
		addGenericConverter(Map.class, Object[].class, new MapToArrayGenericConverter(this));
		addGenericConverter(Map.class, Collection.class, new MapToCollectionGenericConverter(this));
		addGenericConverter(Map.class, Object.class, new MapToObjectGenericConverter(this));
		addGenericConverter(Object.class, Object[].class, new ObjectToArrayGenericConverter(this));
		addGenericConverter(Object.class, Collection.class, new ObjectToCollectionGenericConverter(this));
		addGenericConverter(Object.class, Map.class, new ObjectToMapGenericConverter(this));
	}
{
		return request.getHeader(headerName) != null;
	}
{
		Class classToIntrospect = factoryClass;
		while (classToIntrospect != null) {
			Type[] ifcs = classToIntrospect.getGenericInterfaces();
			for (Type ifc : ifcs) {
				if (ifc instanceof ParameterizedType) {
					ParameterizedType paramIfc = (ParameterizedType) ifc;
					Type rawType = paramIfc.getRawType();
					if (AnnotationFormatterFactory.class.equals(rawType)) {
						Type arg = paramIfc.getActualTypeArguments()[0];
						if (arg instanceof TypeVariable) {
							arg = GenericTypeResolver.resolveTypeVariable((TypeVariable) arg, factoryClass);
						}
						if (arg instanceof Class) {
							return (Class) arg;
						}
					} else if (AnnotationFormatterFactory.class.isAssignableFrom((Class) rawType)) {
						return getAnnotationType((Class) rawType);
					}
				} else if (AnnotationFormatterFactory.class.isAssignableFrom((Class) ifc)) {
					return getAnnotationType((Class) ifc);
				}
			}
			classToIntrospect = classToIntrospect.getSuperclass();
		}
		throw new IllegalArgumentException(
				"Unable to extract Annotation type A argument from AnnotationFormatterFactory [" +
						factoryClass.getName() + "]; does the factory parameterize the <A> generic type?");
	}
{
		Assert.notNull(sourceType, "The source type to convert to is required");
		Assert.notNull(targetType, "The targetType to convert to is required");
		if (source == null) {
			return null;
		}
		Assert.isTrue(sourceType != TypeDescriptor.NULL,
				"The source TypeDescriptor must not be TypeDescriptor.NULL when source != null");
		if (targetType == TypeDescriptor.NULL) {
			return null;
		}
		GenericConverter converter = getConverter(sourceType, targetType);
		if (converter != null) {
			try {
				return converter.convert(source, sourceType, targetType);
			} catch (ConversionFailedException e) {
				throw e;
			} catch (Exception e) {
				throw new ConversionFailedException(sourceType, targetType, source, e);
			}
		} else {
			if (this.parent != null) {
				return this.parent.convert(source, sourceType, targetType);
			} else {
				if (targetType.isAssignableValue(source)) {
					return source;
				} else {
					throw new ConverterNotFoundException(sourceType, targetType);
				}
			}
		}
	}
{
		Collection sourceCollection = (Collection) source;
		TypeDescriptor targetElementType = targetType.getElementTypeDescriptor();
		if (targetElementType == TypeDescriptor.NULL) {
			return compatibleCollectionWithoutElementConversion(sourceCollection, targetType);
		}
		TypeDescriptor sourceElementType = sourceType.getElementTypeDescriptor();
		if (sourceElementType == TypeDescriptor.NULL) {
			sourceElementType = getElementType(sourceCollection);
		}
		if (sourceElementType == TypeDescriptor.NULL || sourceElementType.isAssignableTo(targetElementType)) {
			return compatibleCollectionWithoutElementConversion(sourceCollection, targetType);
		}
		Collection targetCollection = CollectionFactory.createCollection(targetType.getType(), sourceCollection.size());
		GenericConverter elementConverter = conversionService.getConverter(sourceElementType, targetElementType);
		for (Object element : sourceCollection) {
			targetCollection.add(elementConverter.convert(element, sourceElementType, targetElementType));
		}
		return targetCollection;
	}
{
		Collection sourceCollection = (Collection) source;
		TypeDescriptor targetElementType = targetType.getElementTypeDescriptor();
		if (targetElementType == TypeDescriptor.NULL) {
			return compatibleCollectionWithoutElementConversion(sourceCollection, targetType);
		}
		TypeDescriptor sourceElementType = sourceType.getElementTypeDescriptor();
		if (sourceElementType == TypeDescriptor.NULL) {
			sourceElementType = getElementType(sourceCollection);
		}
		if (sourceElementType == TypeDescriptor.NULL || sourceElementType.isAssignableTo(targetElementType)) {
			return compatibleCollectionWithoutElementConversion(sourceCollection, targetType);
		}
		Collection targetCollection = CollectionFactory.createCollection(targetType.getType(), sourceCollection.size());
		GenericConverter elementConverter = conversionService.getConverter(sourceElementType, targetElementType);
		for (Object element : sourceCollection) {
			targetCollection.add(elementConverter.convert(element, sourceElementType, targetElementType));
		}
		return targetCollection;
	}
{
		Collection sourceCollection = (Collection) source;
		TypeDescriptor targetElementType = targetType.getElementTypeDescriptor();
		if (targetElementType == TypeDescriptor.NULL) {
			return compatibleCollectionWithoutElementConversion(sourceCollection, targetType);
		}
		TypeDescriptor sourceElementType = sourceType.getElementTypeDescriptor();
		if (sourceElementType == TypeDescriptor.NULL) {
			sourceElementType = getElementType(sourceCollection);
		}
		if (sourceElementType == TypeDescriptor.NULL || sourceElementType.isAssignableTo(targetElementType)) {
			return compatibleCollectionWithoutElementConversion(sourceCollection, targetType);
		}
		Collection targetCollection = CollectionFactory.createCollection(targetType.getType(), sourceCollection.size());
		GenericConverter elementConverter = conversionService.getConverter(sourceElementType, targetElementType);
		for (Object element : sourceCollection) {
			targetCollection.add(elementConverter.convert(element, sourceElementType, targetElementType));
		}
		return targetCollection;
	}
{
		Collection sourceCollection = (Collection) source;
		TypeDescriptor targetElementType = targetType.getElementTypeDescriptor();
		if (targetElementType == TypeDescriptor.NULL) {
			return compatibleCollectionWithoutElementConversion(sourceCollection, targetType);
		}
		TypeDescriptor sourceElementType = sourceType.getElementTypeDescriptor();
		if (sourceElementType == TypeDescriptor.NULL) {
			sourceElementType = getElementType(sourceCollection);
		}
		if (sourceElementType == TypeDescriptor.NULL || sourceElementType.isAssignableTo(targetElementType)) {
			return compatibleCollectionWithoutElementConversion(sourceCollection, targetType);
		}
		Collection targetCollection = CollectionFactory.createCollection(targetType.getType(), sourceCollection.size());
		GenericConverter elementConverter = conversionService.getConverter(sourceElementType, targetElementType);
		for (Object element : sourceCollection) {
			targetCollection.add(elementConverter.convert(element, sourceElementType, targetElementType));
		}
		return targetCollection;
	}
{
		if (sourceType.isAssignableTo(targetType)) {
			return source;
		}
		TypeDescriptor targetElementType = targetType.getElementTypeDescriptor();
		TypeDescriptor sourceElementType = sourceType.getElementTypeDescriptor();
		Object target = Array.newInstance(targetElementType.getType(), Array.getLength(source));
		GenericConverter converter = conversionService.getConverter(sourceElementType, targetElementType);
		for (int i = 0; i < Array.getLength(target); i++) {
			Array.set(target, i, converter.convert(Array.get(source, i), sourceElementType, targetElementType));
		}
		return target;
	}
{
		addConverter(new StringToBooleanConverter());
		addConverter(new StringToCharacterConverter());
		addConverter(new StringToLocaleConverter());
		addConverter(new NumberToCharacterConverter());
		addConverter(new ObjectToStringConverter());
		addConverterFactory(new StringToNumberConverterFactory());
		addConverterFactory(new StringToEnumConverterFactory());
		addConverterFactory(new NumberToNumberConverterFactory());
		addConverterFactory(new CharacterToNumberFactory());
	}
{
		addGenericConverter(new CollectionGenericConverter(this));
		addGenericConverter(new MapGenericConverter(this));
		addGenericConverter(new ArrayGenericConverter(this));
	}
{
			return this.service.canConvert(sourceType, typeDescriptor);
		}
{

		Assert.notNull(sourceClass, "Source type to convert from is required");
		Assert.notNull(targetType, "Target type to convert to is required");
		if (targetType.getType() == null) {
			return NoOpConversionExecutor.INSTANCE;
		}
		// TODO clean this if/else code up
		TypeDescriptor sourceType = TypeDescriptor.valueOf(sourceClass);
		if (sourceType.isArray()) {
			if (targetType.isArray()) {
				return new ArrayToArray(sourceType, targetType, this);
			}
			else if (targetType.isCollection()) {
				if (targetType.isAbstractClass()) {
					throw new IllegalArgumentException("Conversion target class [" + targetType.getName()
							+ "] is invalid; cannot convert to abstract collection types. "
							+ "Request an interface or a concrete implementation instead!");
				}
				return new ArrayToCollection(sourceType, targetType, this);
			}
			else if (targetType.isMap()) {
				if (sourceType.getElementType().equals(String.class)) {
					return new StringArrayToMap(sourceType, targetType, this);
				}
				else {
					// array to map
					return null;
				}
			}
			else {
				if (sourceType.getElementType().equals(String.class)) {
					return new StringArrayToObject(targetType, this);
				}
				else {
					// array to object
					return null;
				}
			}
		}
		if (sourceType.isCollection()) {
			if (targetType.isCollection()) {	
				return new CollectionToCollection(sourceType, targetType, this);
			}
			else if (targetType.isArray()) {
				return new CollectionToArray(sourceType, targetType, this);
			}
			else if (targetType.isMap()) {
				if (sourceType.getElementType().equals(String.class)) {
					return new StringCollectionToMap(sourceType, targetType, this);
				}
				else {
					// object collection to map
					return null;
				}
			}
			else {
				if (targetType.getType().equals(String.class)) {
					// collection to string
					return null;
				}
				else {
					// collection to object
					return null;
				}
			}
		}
		if (sourceType.isMap()) {
			if (targetType.isMap()) {
				return new MapToMap(sourceType, targetType, this);
			}
			else if (targetType.isArray()) {
				if (targetType.getElementType().equals(String.class)) {
					return new MapToStringArray(targetType, this);
				}
				else {
					// map to object array
					return null;
				}				
			}
			else if (targetType.isCollection()) {
				if (targetType.getElementType().equals(String.class)) {
					return new MapToStringCollection(targetType, this);
				}
				else {
					// map to object collection
					return null;
				}				
			}
			else {
				// map to object
				return null;
			}
		}
		if (targetType.isArray()) {
			if (sourceType.getType().equals(String.class)) {
				return new StringToArray(targetType, this);
			}
			else {
				return new ObjectToArray(sourceType, targetType, this);
			}
		}
		if (targetType.isCollection()) {
			if (sourceType.getType().equals(String.class)) {
				return new StringToCollection(sourceType, targetType, this);
			}
			else {
				return new ObjectToCollection(sourceType, targetType, this);
			}
		}
		if (targetType.isMap()) {
			if (sourceType.getType().equals(String.class)) {
				return new StringToMap(sourceType, targetType, this);
			}
			else {
				// object to map
				return null;
			}
		}
		if (sourceType.isAssignableTo(targetType)) {
			return NoOpConversionExecutor.INSTANCE;
		}
		Converter converter = findConverter(sourceClass, targetType);
		if (converter != null) {
			return new StaticConversionExecutor(sourceType, targetType, converter);
		}
		else if (this.parent instanceof GenericConversionService) {
			return ((GenericConversionService) this.parent).getConversionExecutor(sourceClass, targetType);
		}
		else if (this.parent != null && this.parent.canConvert(sourceClass, targetType)){
			return new ConversionExecutor() {
				public Object execute(Object source) {
					return parent.convert(source, targetType);
				}
			};
		}
		else {
			return null;
		}
	}
{

		Object convertedValue = newValue;

		// Custom editor for this type?
		PropertyEditor editor = this.propertyEditorRegistry.findCustomEditor(requiredType, propertyName);

		// No custom editor but custom ConversionService specified?
		ConversionService conversionService = this.propertyEditorRegistry.getConversionService();
		if (editor == null && conversionService != null && convertedValue != null) {
			TypeDescriptor typeDesc;
			if (methodParam != null) {
				typeDesc = (descriptor != null ?
						new BeanTypeDescriptor(methodParam, descriptor) : new TypeDescriptor(methodParam));
			}
			else {
				typeDesc = TypeDescriptor.valueOf(requiredType);
			}
			if (conversionService.canConvert(convertedValue.getClass(), typeDesc)) {
				return (T) conversionService.convert(convertedValue, typeDesc);
			}
		}

		// Value not of required type?
		if (editor != null || (requiredType != null && !ClassUtils.isAssignableValue(requiredType, convertedValue))) {
			if (editor == null) {
				editor = findDefaultEditor(requiredType, descriptor);
			}
			convertedValue = doConvertValue(oldValue, convertedValue, requiredType, editor);
		}

		if (requiredType != null) {
			// Try to apply some standard type conversion rules if appropriate.

			if (convertedValue != null) {
				if (String.class.equals(requiredType) && ClassUtils.isPrimitiveOrWrapper(convertedValue.getClass())) {
					// We can stringify any primitive value...
					return (T) convertedValue.toString();
				}
				else if (requiredType.isArray()) {
					// Array required -> apply appropriate conversion of elements.
					return (T) convertToTypedArray(convertedValue, propertyName, requiredType.getComponentType());
				}
				else if (convertedValue instanceof Collection) {
					// Convert elements to target type, if determined.
					convertedValue = convertToTypedCollection(
							(Collection) convertedValue, propertyName, requiredType, methodParam);
				}
				else if (convertedValue instanceof Map) {
					// Convert keys and values to respective target type, if determined.
					convertedValue = convertToTypedMap(
							(Map) convertedValue, propertyName, requiredType, methodParam);
				}
				else if (convertedValue instanceof String && !requiredType.isInstance(convertedValue)) {
					if (!requiredType.isInterface() && !requiredType.isEnum()) {
						try {
							Constructor strCtor = requiredType.getConstructor(String.class);
							return (T) BeanUtils.instantiateClass(strCtor, convertedValue);
						}
						catch (NoSuchMethodException ex) {
							// proceed with field lookup
							if (logger.isTraceEnabled()) {
								logger.trace("No String constructor found on type [" + requiredType.getName() + "]", ex);
							}
						}
						catch (Exception ex) {
							if (logger.isDebugEnabled()) {
								logger.debug("Construction via String failed for type [" + requiredType.getName() + "]", ex);
							}
						}
					}
					String trimmedValue = ((String) convertedValue).trim();
					if (requiredType.isEnum() && "".equals(trimmedValue)) {
						// It's an empty enum identifier: reset the enum value to null.
						return null;
					}
					// Try field lookup as fallback: for JDK 1.5 enum or custom enum
					// with values defined as static fields. Resulting value still needs
					// to be checked, hence we don't return it right away.
					try {
						Field enumField = requiredType.getField(trimmedValue);
						convertedValue = enumField.get(null);
					}
					catch (Throwable ex) {
						if (logger.isTraceEnabled()) {
							logger.trace("Field [" + convertedValue + "] isn't an enum value", ex);
						}
					}
				}
			}

			if (!ClassUtils.isAssignableValue(requiredType, convertedValue)) {
				// Definitely doesn't match: throw IllegalArgumentException/IllegalStateException
				StringBuilder msg = new StringBuilder();
				msg.append("Cannot convert value of type [").append(ClassUtils.getDescriptiveType(newValue));
				msg.append("] to required type [").append(ClassUtils.getQualifiedName(requiredType)).append("]");
				if (propertyName != null) {
					msg.append(" for property '").append(propertyName).append("'");
				}
				if (editor != null) {
					msg.append(": PropertyEditor [").append(editor.getClass().getName()).append(
							"] returned inappropriate value");
					throw new IllegalArgumentException(msg.toString());
				}
				else {
					msg.append(": no matching editors or conversion strategy found");
					throw new IllegalStateException(msg.toString());
				}
			}
		}

		return (T) convertedValue;
	}
{

		descriptor.initParameterNameDiscovery(getParameterNameDiscoverer());
		Class<?> type = descriptor.getDependencyType();

		Object value = getAutowireCandidateResolver().getSuggestedValue(descriptor);
		if (value != null) {
			if (value instanceof String) {
				String strVal = resolveEmbeddedValue((String) value);
				value = evaluateBeanDefinitionString(strVal, getMergedBeanDefinition(beanName));
			}
			return typeConverter.convertIfNecessary(value, type);
		}

		if (type.isArray()) {
			Class componentType = type.getComponentType();
			Map<String, Object> matchingBeans = findAutowireCandidates(beanName, componentType, descriptor);
			if (matchingBeans.isEmpty()) {
				if (descriptor.isRequired()) {
					raiseNoSuchBeanDefinitionException(componentType, "array of " + componentType.getName(), descriptor);
				}
				return null;
			}
			if (autowiredBeanNames != null) {
				autowiredBeanNames.addAll(matchingBeans.keySet());
			}
			TypeConverter converter = (typeConverter != null ? typeConverter : getTypeConverter());
			return converter.convertIfNecessary(matchingBeans.values(), type);
		}
		else if (Collection.class.isAssignableFrom(type) && type.isInterface()) {
			Class elementType = descriptor.getCollectionType();
			if (elementType == null) {
				if (descriptor.isRequired()) {
					throw new FatalBeanException("No element type declared for collection [" + type.getName() + "]");
				}
				return null;
			}
			Map<String, Object> matchingBeans = findAutowireCandidates(beanName, elementType, descriptor);
			if (matchingBeans.isEmpty()) {
				if (descriptor.isRequired()) {
					raiseNoSuchBeanDefinitionException(elementType, "collection of " + elementType.getName(), descriptor);
				}
				return null;
			}
			if (autowiredBeanNames != null) {
				autowiredBeanNames.addAll(matchingBeans.keySet());
			}
			TypeConverter converter = (typeConverter != null ? typeConverter : getTypeConverter());
			return converter.convertIfNecessary(matchingBeans.values(), type);
		}
		else if (Map.class.isAssignableFrom(type) && type.isInterface()) {
			Class keyType = descriptor.getMapKeyType();
			if (keyType == null || !String.class.isAssignableFrom(keyType)) {
				if (descriptor.isRequired()) {
					throw new FatalBeanException("Key type [" + keyType + "] of map [" + type.getName() +
							"] must be assignable to [java.lang.String]");
				}
				return null;
			}
			Class valueType = descriptor.getMapValueType();
			if (valueType == null) {
				if (descriptor.isRequired()) {
					throw new FatalBeanException("No value type declared for map [" + type.getName() + "]");
				}
				return null;
			}
			Map<String, Object> matchingBeans = findAutowireCandidates(beanName, valueType, descriptor);
			if (matchingBeans.isEmpty()) {
				if (descriptor.isRequired()) {
					raiseNoSuchBeanDefinitionException(valueType, "map with value type " + valueType.getName(), descriptor);
				}
				return null;
			}
			if (autowiredBeanNames != null) {
				autowiredBeanNames.addAll(matchingBeans.keySet());
			}
			return matchingBeans;
		}
		else {
			Map<String, Object> matchingBeans = findAutowireCandidates(beanName, type, descriptor);
			if (matchingBeans.isEmpty()) {
				if (descriptor.isRequired()) {
					throw new NoSuchBeanDefinitionException(type,
							"Unsatisfied dependency of type [" + type + "]: expected at least 1 matching bean");
				}
				return null;
			}
			if (matchingBeans.size() > 1) {
				String primaryBeanName = determinePrimaryCandidate(matchingBeans, descriptor);
				if (primaryBeanName == null) {
					throw new NoSuchBeanDefinitionException(type,
							"expected single matching bean but found " + matchingBeans.size() + ": " + matchingBeans.keySet());
				}
				if (autowiredBeanNames != null) {
					autowiredBeanNames.add(primaryBeanName);
				}
				return matchingBeans.get(primaryBeanName);
			}
			// We have exactly one match.
			Map.Entry<String, Object> entry = matchingBeans.entrySet().iterator().next();
			if (autowiredBeanNames != null) {
				autowiredBeanNames.add(entry.getKey());
			}
			return entry.getValue();
		}
	}
{
		StringBuilder result = new StringBuilder(value);

		int startIndex = result.indexOf(PLACEHOLDER_PREFIX);
		while (startIndex != -1) {
			int endIndex = result.indexOf(PLACEHOLDER_SUFFIX, startIndex + PLACEHOLDER_PREFIX.length());
			if (endIndex != -1) {
				String placeholder = result.substring(startIndex + PLACEHOLDER_PREFIX.length(), endIndex);
				int nextIndex = endIndex + PLACEHOLDER_SUFFIX.length();

				String propVal = placeholderResolver.resolvePlaceholder(placeholder);
				if (propVal != null) {
					result.replace(startIndex, endIndex + PLACEHOLDER_SUFFIX.length(), propVal);
					nextIndex = startIndex + propVal.length();
				}

				startIndex = result.indexOf(PLACEHOLDER_PREFIX, nextIndex);
			}
			else {
				startIndex = -1;
			}
		}

		return result.toString();
	}
{
		Annotation[] annotations = type.getAnnotations();
		for (Annotation a : annotations) {
			AnnotationFormatterFactory factory = annotationFormatters.get(a.annotationType());
			if (factory != null) {
				return factory.getFormatter(a);
			}
		}
		return null;
	}
{
		Assert.notNull(testInstance, "testInstance must not be null");
		if (logger.isTraceEnabled()) {
			logger.trace("afterTestMethod(): instance [" + testInstance + "], method [" + testMethod + "], exception ["
					+ exception + "]");
		}
		getTestContext().updateState(testInstance, testMethod, exception);

		// Traverse the TestExecutionListeners in reverse order to ensure proper
		// "wrapper"-style execution of listeners.
		List<TestExecutionListener> listenersReversed = new ArrayList<TestExecutionListener>(
			getTestExecutionListeners());
		Collections.reverse(listenersReversed);

		Exception afterTestMethodException = null;
		for (TestExecutionListener testExecutionListener : listenersReversed) {
			try {
				testExecutionListener.afterTestMethod(getTestContext());
			}
			catch (Exception ex) {
				logger.warn("Caught exception while allowing TestExecutionListener [" + testExecutionListener
						+ "] to process 'after' execution for test: method [" + testMethod + "], instance ["
						+ testInstance + "], exception [" + exception + "]", ex);
				if (afterTestMethodException == null) {
					afterTestMethodException = ex;
				}
			}
		}
		if (afterTestMethodException != null) {
			throw afterTestMethodException;
		}
	}
{
		StaticWebApplicationContext wac = new StaticWebApplicationContext();
		wac.setServletContext(new MockServletContext());
		wac.getServletContext().setAttribute(WebUtils.TEMP_DIR_CONTEXT_ATTRIBUTE, new File("mytemp"));
		wac.refresh();
		MockCommonsMultipartResolver resolver = new MockCommonsMultipartResolver();
		resolver.setMaxUploadSize(1000);
		resolver.setMaxInMemorySize(100);
		resolver.setDefaultEncoding("enc");
		if (lazy) {
			resolver.setResolveLazily(false);
		}
		resolver.setServletContext(wac.getServletContext());
		assertEquals(1000, resolver.getFileUpload().getSizeMax());
		assertEquals(100, resolver.getFileItemFactory().getSizeThreshold());
		assertEquals("enc", resolver.getFileUpload().getHeaderEncoding());
		assertTrue(resolver.getFileItemFactory().getRepository().getAbsolutePath().endsWith("mytemp"));

		MockHttpServletRequest originalRequest = new MockHttpServletRequest();
		originalRequest.setMethod("POST");
		originalRequest.setContentType("multipart/form-data");
		originalRequest.addHeader("Content-type", "multipart/form-data");
		originalRequest.addParameter("getField", "getValue");
		assertTrue(resolver.isMultipart(originalRequest));
		MultipartHttpServletRequest request = resolver.resolveMultipart(originalRequest);

		Set parameterNames = new HashSet();
		Enumeration parameterEnum = request.getParameterNames();
		while (parameterEnum.hasMoreElements()) {
			parameterNames.add(parameterEnum.nextElement());
		}
		assertEquals(3, parameterNames.size());
		assertTrue(parameterNames.contains("field3"));
		assertTrue(parameterNames.contains("field4"));
		assertTrue(parameterNames.contains("getField"));
		assertEquals("value3", request.getParameter("field3"));
		List parameterValues = Arrays.asList(request.getParameterValues("field3"));
		assertEquals(1, parameterValues.size());
		assertTrue(parameterValues.contains("value3"));
		assertEquals("value4", request.getParameter("field4"));
		parameterValues = Arrays.asList(request.getParameterValues("field4"));
		assertEquals(2, parameterValues.size());
		assertTrue(parameterValues.contains("value4"));
		assertTrue(parameterValues.contains("value5"));
		assertEquals("value4", request.getParameter("field4"));
		assertEquals("getValue", request.getParameter("getField"));

		List parameterMapKeys = new ArrayList();
		List parameterMapValues = new ArrayList();
		for (Iterator parameterMapIter = request.getParameterMap().keySet().iterator(); parameterMapIter.hasNext();) {
			String key = (String) parameterMapIter.next();
			parameterMapKeys.add(key);
			parameterMapValues.add(request.getParameterMap().get(key));
		}
		assertEquals(3, parameterMapKeys.size());
		assertEquals(3, parameterMapValues.size());
		int field3Index = parameterMapKeys.indexOf("field3");
		int field4Index = parameterMapKeys.indexOf("field4");
		int getFieldIndex = parameterMapKeys.indexOf("getField");
		assertTrue(field3Index != -1);
		assertTrue(field4Index != -1);
		assertTrue(getFieldIndex != -1);
		parameterValues = Arrays.asList((String[]) parameterMapValues.get(field3Index));
		assertEquals(1, parameterValues.size());
		assertTrue(parameterValues.contains("value3"));
		parameterValues = Arrays.asList((String[]) parameterMapValues.get(field4Index));
		assertEquals(2, parameterValues.size());
		assertTrue(parameterValues.contains("value4"));
		assertTrue(parameterValues.contains("value5"));
		parameterValues = Arrays.asList((String[]) parameterMapValues.get(getFieldIndex));
		assertEquals(1, parameterValues.size());
		assertTrue(parameterValues.contains("getValue"));

		Set fileNames = new HashSet();
		Iterator fileIter = request.getFileNames();
		while (fileIter.hasNext()) {
			fileNames.add(fileIter.next());
		}
		assertEquals(3, fileNames.size());
		assertTrue(fileNames.contains("field1"));
		assertTrue(fileNames.contains("field2"));
		assertTrue(fileNames.contains("field2x"));
		CommonsMultipartFile file1 = (CommonsMultipartFile) request.getFile("field1");
		CommonsMultipartFile file2 = (CommonsMultipartFile) request.getFile("field2");
		CommonsMultipartFile file2x = (CommonsMultipartFile) request.getFile("field2x");
		Map fileMap = request.getFileMap();
		assertEquals(3, fileMap.size());
		assertTrue(fileMap.containsKey("field1"));
		assertTrue(fileMap.containsKey("field2"));
		assertTrue(fileMap.containsKey("field2x"));
		assertEquals(file1, fileMap.get("field1"));
		assertEquals(file2, fileMap.get("field2"));
		assertEquals(file2x, fileMap.get("field2x"));

		assertEquals("type1", file1.getContentType());
		assertEquals("type2", file2.getContentType());
		assertEquals("type2", file2x.getContentType());
		assertEquals("field1.txt", file1.getOriginalFilename());
		assertEquals("field2.txt", file2.getOriginalFilename());
		assertEquals("field2x.txt", file2x.getOriginalFilename());
		assertEquals("text1", new String(file1.getBytes()));
		assertEquals("text2", new String(file2.getBytes()));
		assertEquals(5, file1.getSize());
		assertEquals(5, file2.getSize());
		assertTrue(file1.getInputStream() instanceof ByteArrayInputStream);
		assertTrue(file2.getInputStream() instanceof ByteArrayInputStream);
		File transfer1 = new File("C:/transfer1");
		File transfer2 = new File("C:/transfer2");
		file1.transferTo(transfer1);
		file2.transferTo(transfer2);
		assertEquals(transfer1, ((MockFileItem) file1.getFileItem()).writtenFile);
		assertEquals(transfer2, ((MockFileItem) file2.getFileItem()).writtenFile);

		MultipartTestBean1 mtb1 = new MultipartTestBean1();
		assertEquals(null, mtb1.getField1());
		assertEquals(null, mtb1.getField2());
		ServletRequestDataBinder binder = new ServletRequestDataBinder(mtb1, "mybean");
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		binder.bind(request);
		assertEquals(file1, mtb1.getField1());
		assertEquals(new String(file2.getBytes()), new String(mtb1.getField2()));

		MultipartTestBean2 mtb2 = new MultipartTestBean2();
		assertEquals(null, mtb2.getField1());
		assertEquals(null, mtb2.getField2());
		binder = new ServletRequestDataBinder(mtb2, "mybean");
		binder.registerCustomEditor(String.class, "field1", new StringMultipartFileEditor());
		binder.registerCustomEditor(String.class, "field2", new StringMultipartFileEditor("UTF-16"));
		binder.bind(request);
		assertEquals(new String(file1.getBytes()), mtb2.getField1());
		assertEquals(new String(file2.getBytes(), "UTF-16"), mtb2.getField2());

		resolver.cleanupMultipart(request);
		assertTrue(((MockFileItem) file1.getFileItem()).deleted);
		assertTrue(((MockFileItem) file2.getFileItem()).deleted);

		resolver.setEmpty(true);
		request = resolver.resolveMultipart(originalRequest);
		binder.setBindEmptyMultipartFiles(false);
		String firstBound = mtb2.getField1();
		binder.bind(request);
		assertTrue(mtb2.getField1().length() > 0);
		assertEquals(firstBound, mtb2.getField1());

		request = resolver.resolveMultipart(originalRequest);
		binder.setBindEmptyMultipartFiles(true);
		binder.bind(request);
		assertTrue(mtb2.getField1().length() == 0);
	}
{
		StaticWebApplicationContext wac = new StaticWebApplicationContext();
		wac.setServletContext(new MockServletContext());
		wac.getServletContext().setAttribute(WebUtils.TEMP_DIR_CONTEXT_ATTRIBUTE, new File("mytemp"));
		wac.refresh();
		MockCommonsMultipartResolver resolver = new MockCommonsMultipartResolver();
		resolver.setMaxUploadSize(1000);
		resolver.setMaxInMemorySize(100);
		resolver.setDefaultEncoding("enc");
		if (lazy) {
			resolver.setResolveLazily(false);
		}
		resolver.setServletContext(wac.getServletContext());
		assertEquals(1000, resolver.getFileUpload().getSizeMax());
		assertEquals(100, resolver.getFileItemFactory().getSizeThreshold());
		assertEquals("enc", resolver.getFileUpload().getHeaderEncoding());
		assertTrue(resolver.getFileItemFactory().getRepository().getAbsolutePath().endsWith("mytemp"));

		MockHttpServletRequest originalRequest = new MockHttpServletRequest();
		originalRequest.setMethod("POST");
		originalRequest.setContentType("multipart/form-data");
		originalRequest.addHeader("Content-type", "multipart/form-data");
		originalRequest.addParameter("getField", "getValue");
		assertTrue(resolver.isMultipart(originalRequest));
		MultipartHttpServletRequest request = resolver.resolveMultipart(originalRequest);

		Set parameterNames = new HashSet();
		Enumeration parameterEnum = request.getParameterNames();
		while (parameterEnum.hasMoreElements()) {
			parameterNames.add(parameterEnum.nextElement());
		}
		assertEquals(3, parameterNames.size());
		assertTrue(parameterNames.contains("field3"));
		assertTrue(parameterNames.contains("field4"));
		assertTrue(parameterNames.contains("getField"));
		assertEquals("value3", request.getParameter("field3"));
		List parameterValues = Arrays.asList(request.getParameterValues("field3"));
		assertEquals(1, parameterValues.size());
		assertTrue(parameterValues.contains("value3"));
		assertEquals("value4", request.getParameter("field4"));
		parameterValues = Arrays.asList(request.getParameterValues("field4"));
		assertEquals(2, parameterValues.size());
		assertTrue(parameterValues.contains("value4"));
		assertTrue(parameterValues.contains("value5"));
		assertEquals("value4", request.getParameter("field4"));
		assertEquals("getValue", request.getParameter("getField"));

		List parameterMapKeys = new ArrayList();
		List parameterMapValues = new ArrayList();
		for (Iterator parameterMapIter = request.getParameterMap().keySet().iterator(); parameterMapIter.hasNext();) {
			String key = (String) parameterMapIter.next();
			parameterMapKeys.add(key);
			parameterMapValues.add(request.getParameterMap().get(key));
		}
		assertEquals(3, parameterMapKeys.size());
		assertEquals(3, parameterMapValues.size());
		int field3Index = parameterMapKeys.indexOf("field3");
		int field4Index = parameterMapKeys.indexOf("field4");
		int getFieldIndex = parameterMapKeys.indexOf("getField");
		assertTrue(field3Index != -1);
		assertTrue(field4Index != -1);
		assertTrue(getFieldIndex != -1);
		parameterValues = Arrays.asList((String[]) parameterMapValues.get(field3Index));
		assertEquals(1, parameterValues.size());
		assertTrue(parameterValues.contains("value3"));
		parameterValues = Arrays.asList((String[]) parameterMapValues.get(field4Index));
		assertEquals(2, parameterValues.size());
		assertTrue(parameterValues.contains("value4"));
		assertTrue(parameterValues.contains("value5"));
		parameterValues = Arrays.asList((String[]) parameterMapValues.get(getFieldIndex));
		assertEquals(1, parameterValues.size());
		assertTrue(parameterValues.contains("getValue"));

		Set fileNames = new HashSet();
		Iterator fileIter = request.getFileNames();
		while (fileIter.hasNext()) {
			fileNames.add(fileIter.next());
		}
		assertEquals(3, fileNames.size());
		assertTrue(fileNames.contains("field1"));
		assertTrue(fileNames.contains("field2"));
		assertTrue(fileNames.contains("field2x"));
		CommonsMultipartFile file1 = (CommonsMultipartFile) request.getFile("field1");
		CommonsMultipartFile file2 = (CommonsMultipartFile) request.getFile("field2");
		CommonsMultipartFile file2x = (CommonsMultipartFile) request.getFile("field2x");
		Map fileMap = request.getFileMap();
		assertEquals(3, fileMap.size());
		assertTrue(fileMap.containsKey("field1"));
		assertTrue(fileMap.containsKey("field2"));
		assertTrue(fileMap.containsKey("field2x"));
		assertEquals(file1, fileMap.get("field1"));
		assertEquals(file2, fileMap.get("field2"));
		assertEquals(file2x, fileMap.get("field2x"));

		assertEquals("type1", file1.getContentType());
		assertEquals("type2", file2.getContentType());
		assertEquals("type2", file2x.getContentType());
		assertEquals("field1.txt", file1.getOriginalFilename());
		assertEquals("field2.txt", file2.getOriginalFilename());
		assertEquals("field2x.txt", file2x.getOriginalFilename());
		assertEquals("text1", new String(file1.getBytes()));
		assertEquals("text2", new String(file2.getBytes()));
		assertEquals(5, file1.getSize());
		assertEquals(5, file2.getSize());
		assertTrue(file1.getInputStream() instanceof ByteArrayInputStream);
		assertTrue(file2.getInputStream() instanceof ByteArrayInputStream);
		File transfer1 = new File("C:/transfer1");
		File transfer2 = new File("C:/transfer2");
		file1.transferTo(transfer1);
		file2.transferTo(transfer2);
		assertEquals(transfer1, ((MockFileItem) file1.getFileItem()).writtenFile);
		assertEquals(transfer2, ((MockFileItem) file2.getFileItem()).writtenFile);

		MultipartTestBean1 mtb1 = new MultipartTestBean1();
		assertEquals(null, mtb1.getField1());
		assertEquals(null, mtb1.getField2());
		ServletRequestDataBinder binder = new ServletRequestDataBinder(mtb1, "mybean");
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		binder.bind(request);
		assertEquals(file1, mtb1.getField1());
		assertEquals(new String(file2.getBytes()), new String(mtb1.getField2()));

		MultipartTestBean2 mtb2 = new MultipartTestBean2();
		assertEquals(null, mtb2.getField1());
		assertEquals(null, mtb2.getField2());
		binder = new ServletRequestDataBinder(mtb2, "mybean");
		binder.registerCustomEditor(String.class, "field1", new StringMultipartFileEditor());
		binder.registerCustomEditor(String.class, "field2", new StringMultipartFileEditor("UTF-16"));
		binder.bind(request);
		assertEquals(new String(file1.getBytes()), mtb2.getField1());
		assertEquals(new String(file2.getBytes(), "UTF-16"), mtb2.getField2());

		resolver.cleanupMultipart(request);
		assertTrue(((MockFileItem) file1.getFileItem()).deleted);
		assertTrue(((MockFileItem) file2.getFileItem()).deleted);

		resolver.setEmpty(true);
		request = resolver.resolveMultipart(originalRequest);
		binder.setBindEmptyMultipartFiles(false);
		String firstBound = mtb2.getField1();
		binder.bind(request);
		assertTrue(mtb2.getField1().length() > 0);
		assertEquals(firstBound, mtb2.getField1());

		request = resolver.resolveMultipart(originalRequest);
		binder.setBindEmptyMultipartFiles(true);
		binder.bind(request);
		assertTrue(mtb2.getField1().length() == 0);
	}
{
		StaticWebApplicationContext wac = new StaticWebApplicationContext();
		wac.setServletContext(new MockServletContext());
		wac.getServletContext().setAttribute(WebUtils.TEMP_DIR_CONTEXT_ATTRIBUTE, new File("mytemp"));
		wac.refresh();
		MockCommonsMultipartResolver resolver = new MockCommonsMultipartResolver();
		resolver.setMaxUploadSize(1000);
		resolver.setMaxInMemorySize(100);
		resolver.setDefaultEncoding("enc");
		if (lazy) {
			resolver.setResolveLazily(false);
		}
		resolver.setServletContext(wac.getServletContext());
		assertEquals(1000, resolver.getFileUpload().getSizeMax());
		assertEquals(100, resolver.getFileItemFactory().getSizeThreshold());
		assertEquals("enc", resolver.getFileUpload().getHeaderEncoding());
		assertTrue(resolver.getFileItemFactory().getRepository().getAbsolutePath().endsWith("mytemp"));

		MockHttpServletRequest originalRequest = new MockHttpServletRequest();
		originalRequest.setMethod("POST");
		originalRequest.setContentType("multipart/form-data");
		originalRequest.addHeader("Content-type", "multipart/form-data");
		originalRequest.addParameter("getField", "getValue");
		assertTrue(resolver.isMultipart(originalRequest));
		MultipartHttpServletRequest request = resolver.resolveMultipart(originalRequest);

		Set parameterNames = new HashSet();
		Enumeration parameterEnum = request.getParameterNames();
		while (parameterEnum.hasMoreElements()) {
			parameterNames.add(parameterEnum.nextElement());
		}
		assertEquals(3, parameterNames.size());
		assertTrue(parameterNames.contains("field3"));
		assertTrue(parameterNames.contains("field4"));
		assertTrue(parameterNames.contains("getField"));
		assertEquals("value3", request.getParameter("field3"));
		List parameterValues = Arrays.asList(request.getParameterValues("field3"));
		assertEquals(1, parameterValues.size());
		assertTrue(parameterValues.contains("value3"));
		assertEquals("value4", request.getParameter("field4"));
		parameterValues = Arrays.asList(request.getParameterValues("field4"));
		assertEquals(2, parameterValues.size());
		assertTrue(parameterValues.contains("value4"));
		assertTrue(parameterValues.contains("value5"));
		assertEquals("value4", request.getParameter("field4"));
		assertEquals("getValue", request.getParameter("getField"));

		List parameterMapKeys = new ArrayList();
		List parameterMapValues = new ArrayList();
		for (Iterator parameterMapIter = request.getParameterMap().keySet().iterator(); parameterMapIter.hasNext();) {
			String key = (String) parameterMapIter.next();
			parameterMapKeys.add(key);
			parameterMapValues.add(request.getParameterMap().get(key));
		}
		assertEquals(3, parameterMapKeys.size());
		assertEquals(3, parameterMapValues.size());
		int field3Index = parameterMapKeys.indexOf("field3");
		int field4Index = parameterMapKeys.indexOf("field4");
		int getFieldIndex = parameterMapKeys.indexOf("getField");
		assertTrue(field3Index != -1);
		assertTrue(field4Index != -1);
		assertTrue(getFieldIndex != -1);
		parameterValues = Arrays.asList((String[]) parameterMapValues.get(field3Index));
		assertEquals(1, parameterValues.size());
		assertTrue(parameterValues.contains("value3"));
		parameterValues = Arrays.asList((String[]) parameterMapValues.get(field4Index));
		assertEquals(2, parameterValues.size());
		assertTrue(parameterValues.contains("value4"));
		assertTrue(parameterValues.contains("value5"));
		parameterValues = Arrays.asList((String[]) parameterMapValues.get(getFieldIndex));
		assertEquals(1, parameterValues.size());
		assertTrue(parameterValues.contains("getValue"));

		Set fileNames = new HashSet();
		Iterator fileIter = request.getFileNames();
		while (fileIter.hasNext()) {
			fileNames.add(fileIter.next());
		}
		assertEquals(3, fileNames.size());
		assertTrue(fileNames.contains("field1"));
		assertTrue(fileNames.contains("field2"));
		assertTrue(fileNames.contains("field2x"));
		CommonsMultipartFile file1 = (CommonsMultipartFile) request.getFile("field1");
		CommonsMultipartFile file2 = (CommonsMultipartFile) request.getFile("field2");
		CommonsMultipartFile file2x = (CommonsMultipartFile) request.getFile("field2x");
		Map fileMap = request.getFileMap();
		assertEquals(3, fileMap.size());
		assertTrue(fileMap.containsKey("field1"));
		assertTrue(fileMap.containsKey("field2"));
		assertTrue(fileMap.containsKey("field2x"));
		assertEquals(file1, fileMap.get("field1"));
		assertEquals(file2, fileMap.get("field2"));
		assertEquals(file2x, fileMap.get("field2x"));

		assertEquals("type1", file1.getContentType());
		assertEquals("type2", file2.getContentType());
		assertEquals("type2", file2x.getContentType());
		assertEquals("field1.txt", file1.getOriginalFilename());
		assertEquals("field2.txt", file2.getOriginalFilename());
		assertEquals("field2x.txt", file2x.getOriginalFilename());
		assertEquals("text1", new String(file1.getBytes()));
		assertEquals("text2", new String(file2.getBytes()));
		assertEquals(5, file1.getSize());
		assertEquals(5, file2.getSize());
		assertTrue(file1.getInputStream() instanceof ByteArrayInputStream);
		assertTrue(file2.getInputStream() instanceof ByteArrayInputStream);
		File transfer1 = new File("C:/transfer1");
		File transfer2 = new File("C:/transfer2");
		file1.transferTo(transfer1);
		file2.transferTo(transfer2);
		assertEquals(transfer1, ((MockFileItem) file1.getFileItem()).writtenFile);
		assertEquals(transfer2, ((MockFileItem) file2.getFileItem()).writtenFile);

		MultipartTestBean1 mtb1 = new MultipartTestBean1();
		assertEquals(null, mtb1.getField1());
		assertEquals(null, mtb1.getField2());
		ServletRequestDataBinder binder = new ServletRequestDataBinder(mtb1, "mybean");
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		binder.bind(request);
		assertEquals(file1, mtb1.getField1());
		assertEquals(new String(file2.getBytes()), new String(mtb1.getField2()));

		MultipartTestBean2 mtb2 = new MultipartTestBean2();
		assertEquals(null, mtb2.getField1());
		assertEquals(null, mtb2.getField2());
		binder = new ServletRequestDataBinder(mtb2, "mybean");
		binder.registerCustomEditor(String.class, "field1", new StringMultipartFileEditor());
		binder.registerCustomEditor(String.class, "field2", new StringMultipartFileEditor("UTF-16"));
		binder.bind(request);
		assertEquals(new String(file1.getBytes()), mtb2.getField1());
		assertEquals(new String(file2.getBytes(), "UTF-16"), mtb2.getField2());

		resolver.cleanupMultipart(request);
		assertTrue(((MockFileItem) file1.getFileItem()).deleted);
		assertTrue(((MockFileItem) file2.getFileItem()).deleted);

		resolver.setEmpty(true);
		request = resolver.resolveMultipart(originalRequest);
		binder.setBindEmptyMultipartFiles(false);
		String firstBound = mtb2.getField1();
		binder.bind(request);
		assertTrue(mtb2.getField1().length() > 0);
		assertEquals(firstBound, mtb2.getField1());

		request = resolver.resolveMultipart(originalRequest);
		binder.setBindEmptyMultipartFiles(true);
		binder.bind(request);
		assertTrue(mtb2.getField1().length() == 0);
	}
{
		Assert.notNull(type, "The TypeDescriptor is required");
		Annotation[] annotations = type.getAnnotations();
		for (Annotation a : annotations) {
			AnnotationFormatterFactory factory = annotationFormatters.get(a.annotationType());
			if (factory != null) {
				return factory.getFormatter(a);
			}
		}
		return getFormatter(type.getType());
	}
{
		if (type.isPrimitive()) {
			if (type.equals(int.class)) {
				return Integer.class;
			} else if (type.equals(short.class)) {
				return Short.class;
			} else if (type.equals(long.class)) {
				return Long.class;
			} else if (type.equals(float.class)) {
				return Float.class;
			} else if (type.equals(double.class)) {
				return Double.class;
			} else if (type.equals(byte.class)) {
				return Byte.class;
			} else if (type.equals(boolean.class)) {
				return Boolean.class;
			} else if (type.equals(char.class)) {
				return Character.class;
			} else {
				throw new IllegalStateException("Should never happen - primitive type is not a primitive?");
			}
		} else {
			return type;
		}
	}
{
		return this.typeConverter.canConvert(sourceType, targetType);
	}
{
		Class<?> elementType = targetCollectionType.getElementType();
		if (elementType != null) { 
			this.elementConverter = typeConverter.getConversionExecutor(sourceObjectType.getType(), TypeDescriptor.valueOf(elementType));
		} else {
			this.elementConverter = NoOpConversionExecutor.INSTANCE;
		}
	}
{
		String fullPath = this.basePath + annotation.serviceName();
		HttpContext httpContext = this.server.createContext(fullPath);
		if (this.filters != null) {
			httpContext.getFilters().addAll(this.filters);
		}
		if (this.authenticator != null) {
			httpContext.setAuthenticator(this.authenticator);
		}
		endpoint.publish(httpContext);
	}
{
		String fullAddress = this.baseAddress + annotation.serviceName();
		if (endpoint.getClass().getName().startsWith("weblogic.")) {
			// Workaround for WebLogic 10.3 
			fullAddress = fullAddress + "/";
		}
		endpoint.publish(fullAddress);
	}
{
		if (bean instanceof BeanNameAware) {
			((BeanNameAware) bean).setBeanName(beanName);
		}

		if (bean instanceof BeanClassLoaderAware) {
			((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
		}

		if (bean instanceof BeanFactoryAware) {
			((BeanFactoryAware) bean).setBeanFactory(this);
		}

		Object wrappedBean = bean;
		if (mbd == null || !mbd.isSynthetic()) {
			wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
		}

		try {
			invokeInitMethods(beanName, wrappedBean, mbd);
		}
		catch (Throwable ex) {
			throw new BeanCreationException(
					(mbd != null ? mbd.getResourceDescription() : null),
					beanName, "Invocation of init method failed", ex);
		}

		if (mbd == null || !mbd.isSynthetic()) {
			wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
		}
		return wrappedBean;
	}
{

		final String beanName = transformedBeanName(name);
		Object bean;

		// Eagerly check singleton cache for manually registered singletons.
		Object sharedInstance = getSingleton(beanName);
		if (sharedInstance != null && args == null) {
			if (logger.isDebugEnabled()) {
				if (isSingletonCurrentlyInCreation(beanName)) {
					logger.debug("Returning eagerly cached instance of singleton bean '" + beanName +
							"' that is not fully initialized yet - a consequence of a circular reference");
				}
				else {
					logger.debug("Returning cached instance of singleton bean '" + beanName + "'");
				}
			}
			bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
		}

		else {
			// Fail if we're already creating this bean instance:
			// We're assumably within a circular reference.
			if (isPrototypeCurrentlyInCreation(beanName)) {
				throw new BeanCurrentlyInCreationException(beanName);
			}

			// Check if bean definition exists in this factory.
			BeanFactory parentBeanFactory = getParentBeanFactory();
			if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
				// Not found -> check parent.
				String nameToLookup = originalBeanName(name);
				if (args != null) {
					// Delegation to parent with explicit args.
					return (T) parentBeanFactory.getBean(nameToLookup, args);
				}
				else {
					// No args -> delegate to standard getBean method.
					return parentBeanFactory.getBean(nameToLookup, requiredType);
				}
			}

			if (!typeCheckOnly) {
				markBeanAsCreated(beanName);
			}

			final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
			checkMergedBeanDefinition(mbd, beanName, args);

			// Guarantee initialization of beans that the current bean depends on.
			String[] dependsOn = mbd.getDependsOn();
			if (dependsOn != null) {
				for (String dependsOnBean : dependsOn) {
					getBean(dependsOnBean);
					registerDependentBean(dependsOnBean, beanName);
				}
			}

			// Create bean instance.
			if (mbd.isSingleton()) {
				sharedInstance = getSingleton(beanName, new ObjectFactory() {
					public Object getObject() throws BeansException {
						try {
							return createBean(beanName, mbd, args);
						}
						catch (BeansException ex) {
							// Explicitly remove instance from singleton cache: It might have been put there
							// eagerly by the creation process, to allow for circular reference resolution.
							// Also remove any beans that received a temporary reference to the bean.
							destroySingleton(beanName);
							throw ex;
						}
					}
				});
				bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
			}

			else if (mbd.isPrototype()) {
				// It's a prototype -> create a new instance.
				Object prototypeInstance = null;
				try {
					beforePrototypeCreation(beanName);
					prototypeInstance = createBean(beanName, mbd, args);
				}
				finally {
					afterPrototypeCreation(beanName);
				}
				bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
			}

			else {
				String scopeName = mbd.getScope();
				final Scope scope = this.scopes.get(scopeName);
				if (scope == null) {
					throw new IllegalStateException("No Scope registered for scope '" + scopeName + "'");
				}
				try {
					Object scopedInstance = scope.get(beanName, new ObjectFactory() {
						public Object getObject() throws BeansException {
							beforePrototypeCreation(beanName);
							try {
								return createBean(beanName, mbd, args);
							}
							finally {
								afterPrototypeCreation(beanName);
							}
						}
					});
					bean = getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
				}
				catch (IllegalStateException ex) {
					throw new BeanCreationException(beanName,
							"Scope '" + scopeName + "' is not active for the current thread; " +
							"consider defining a scoped proxy for this bean if you intend to refer to it from a singleton",
							ex);
				}
			}
		}

		// Check if required type matches the type of the actual bean instance.
		if (requiredType != null && bean != null && !requiredType.isAssignableFrom(bean.getClass())) {
			throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
		}
		return (T) bean;
	}
{
		try {
			if (mbd.hasBeanClass()) {
				return mbd.getBeanClass();
			}
			if (typesToMatch != null) {
				ClassLoader tempClassLoader = getTempClassLoader();
				if (tempClassLoader != null) {
					if (tempClassLoader instanceof DecoratingClassLoader) {
						DecoratingClassLoader dcl = (DecoratingClassLoader) tempClassLoader;
						for (Class typeToMatch : typesToMatch) {
							dcl.excludeClass(typeToMatch.getName());
						}
					}
					String className = mbd.getBeanClassName();
					return (className != null ? ClassUtils.forName(className, tempClassLoader) : null);
				}
			}
			return mbd.resolveBeanClass(getBeanClassLoader());
		}
		catch (ClassNotFoundException ex) {
			throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex);
		}
		catch (LinkageError err) {
			throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), err);
		}
	}
{
		if (bean instanceof ResourceLoaderAware) {
			((ResourceLoaderAware) bean).setResourceLoader(this.applicationContext);
		}
		if (bean instanceof ApplicationEventPublisherAware) {
			((ApplicationEventPublisherAware) bean).setApplicationEventPublisher(this.applicationContext);
		}
		if (bean instanceof MessageSourceAware) {
			((MessageSourceAware) bean).setMessageSource(this.applicationContext);
		}
		if (bean instanceof ApplicationContextAware) {
			((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
		}
		return bean;
	}
{

			if (handlerMethod.isAnnotationPresent(ResponseStatus.class)) {
				ResponseStatus responseStatus = handlerMethod.getAnnotation(ResponseStatus.class);
				HttpServletResponse response = webRequest.getResponse();
				response.setStatus(responseStatus.value().value());
				responseArgumentUsed = true;
			}

			// Invoke custom resolvers if present...
			if (customModelAndViewResolvers != null) {
				for (ModelAndViewResolver mavResolver : customModelAndViewResolvers) {
					ModelAndView mav = mavResolver
							.resolveModelAndView(handlerMethod, handlerType, returnValue, implicitModel, webRequest);
					if (mav != ModelAndViewResolver.UNRESOLVED) {
						return mav;
					}
				}
			}

			if (returnValue != null && handlerMethod.isAnnotationPresent(ResponseBody.class)) {
				Class returnValueType = returnValue.getClass();
				HttpOutputMessage outputMessage = new ServletServerHttpResponse(webRequest.getResponse());
				for (HttpMessageConverter messageConverter : messageConverters) {
					if (messageConverter.supports(returnValueType)) {
						messageConverter.write(returnValue, outputMessage);
						responseArgumentUsed = true;
						return null;
					}
				}
			}

			if (returnValue instanceof ModelAndView) {
				ModelAndView mav = (ModelAndView) returnValue;
				mav.getModelMap().mergeAttributes(implicitModel);
				return mav;
			}
			else if (returnValue instanceof Model) {
				return new ModelAndView().addAllObjects(implicitModel).addAllObjects(((Model) returnValue).asMap());
			}
			else if (returnValue instanceof View) {
				return new ModelAndView((View) returnValue).addAllObjects(implicitModel);
			}
			else if (handlerMethod.isAnnotationPresent(ModelAttribute.class)) {
				addReturnValueAsModelAttribute(handlerMethod, handlerType, returnValue, implicitModel);
				return new ModelAndView().addAllObjects(implicitModel);
			}
			else if (returnValue instanceof Map) {
				return new ModelAndView().addAllObjects(implicitModel).addAllObjects((Map) returnValue);
			}
			else if (returnValue instanceof String) {
				return new ModelAndView((String) returnValue).addAllObjects(implicitModel);
			}
			else if (returnValue == null) {
				// Either returned null or was 'void' return.
				if (this.responseArgumentUsed || webRequest.isNotModified()) {
					return null;
				}
				else {
					// Assuming view name translation...
					return new ModelAndView().addAllObjects(implicitModel);
				}
			}
			else if (!BeanUtils.isSimpleProperty(returnValue.getClass())) {
				// Assume a single model attribute...
				addReturnValueAsModelAttribute(handlerMethod, handlerType, returnValue, implicitModel);
				return new ModelAndView().addAllObjects(implicitModel);
			}
			else {
				throw new IllegalArgumentException("Invalid handler method return value: " + returnValue);
			}
		}
{
		defaultMessage = message;
		return this;
	}
{
			GenericBindingRule rule = nestedBindingRules.get(property);
			if (rule == null) {
				rule = new GenericBindingRule(property, this.property.getPropertyType());
				nestedBindingRules.put(property, rule);
			}
			return rule;
		}
{
		Formatter formatter;
		if (isList() || isMap()) {
			formatter = bindingContext.getElementFormatter();
		} else {
			formatter = bindingContext.getFormatter();
		}
		Class<?> formattedType = getFormattedObjectType(formatter.getClass());
		value = bindingContext.getTypeConverter().convert(value, formattedType);
		return formatter.format(value, getLocale());
	}
{
		Annotation[] annotations = propertyType.getAnnotations();
		for (Annotation a : annotations) {
			AnnotationFormatterFactory factory = annotationFormatters.get(a.annotationType());
			if (factory != null) {
				return factory.getFormatter(a);
			}
		}
		Formatter<?> formatter = null;
		Class<?> type;
		if (propertyType.isCollection()) {
			formatter = collectionTypeFormatters.get(new GenericCollectionPropertyType(propertyType.getType(), propertyType.getElementType()));
			if (formatter != null) {
				return formatter;
			} else {
				type = propertyType.getElementType();
			}
		} else {
			type = propertyType.getType();
		}
		formatter = typeFormatters.get(type);
		if (formatter != null) {
			return formatter;
		} else {
			Formatted formatted = AnnotationUtils.findAnnotation(type, Formatted.class);
			if (formatted != null) {
				Class formatterClass = formatted.value();
				try {
					formatter = (Formatter) formatterClass.newInstance();
				} catch (InstantiationException e) {
					// TODO better runtime exception
					throw new IllegalStateException(e);
				} catch (IllegalAccessException e) {
					throw new IllegalStateException(e);
				}
				typeFormatters.put(type, formatter);
				return formatter;
			} else {
				return null;
			}
		}
	}
{
		Assert.notNull(testClass, "Test class must not be null");
		Assert.notNull(contextCache, "ContextCache must not be null");

		if (!StringUtils.hasText(defaultContextLoaderClassName)) {
			defaultContextLoaderClassName = STANDARD_DEFAULT_CONTEXT_LOADER_CLASS_NAME;
		}

		ContextConfiguration contextConfiguration = testClass.getAnnotation(ContextConfiguration.class);
		String[] locations = null;
		ContextLoader contextLoader = null;

		if (contextConfiguration == null) {
			if (logger.isInfoEnabled()) {
				logger.info("@ContextConfiguration not found for class [" + testClass + "]");
			}
		}
		else {
			if (logger.isTraceEnabled()) {
				logger.trace("Retrieved @ContextConfiguration [" + contextConfiguration + "] for class [" + testClass
						+ "]");
			}

			Class<? extends ContextLoader> contextLoaderClass = contextConfiguration.loader();
			if (ContextLoader.class.equals(contextLoaderClass)) {
				try {
					if (logger.isTraceEnabled()) {
						logger.trace("Using default ContextLoader class [" + defaultContextLoaderClassName
								+ "] for @ContextConfiguration [" + contextConfiguration + "] and class [" + testClass
								+ "]");
					}
					contextLoaderClass = (Class<? extends ContextLoader>) getClass().getClassLoader().loadClass(
						defaultContextLoaderClassName);
				}
				catch (ClassNotFoundException ex) {
					throw new IllegalStateException("Could not load default ContextLoader class ["
							+ defaultContextLoaderClassName + "]. Specify @ContextConfiguration's 'loader' "
							+ "attribute or make the default loader class available.");
				}
			}
			contextLoader = (ContextLoader) BeanUtils.instantiateClass(contextLoaderClass);
			locations = retrieveContextLocations(contextLoader, testClass);
		}

		this.testClass = testClass;
		this.contextCache = contextCache;
		this.contextLoader = contextLoader;
		this.locations = locations;
	}
{
		this.propertyDescriptor = findPropertyDescriptor(property, model);
		this.property = property;
		this.model = model;
		this.typeConverter = typeConverter;
		this.buffer = new ValueBuffer(getModel());
		status = BindingStatus.CLEAN;
	}
{
		sourceValues = filter(sourceValues);
		checkRequired(sourceValues);
		ArrayListBindingResults results = new ArrayListBindingResults(sourceValues.size());
		for (Map.Entry<String, ? extends Object> sourceValue : sourceValues.entrySet()) {
			String property = sourceValue.getKey();
			Object value = sourceValue.getValue();
			results.add(getBinding(property).setValue(value));
		}
		return results;
	}
{
		List typeInfo = new ArrayList(2);
		if (converter instanceof ConverterInfo) {
			ConverterInfo info = (ConverterInfo) converter;
			typeInfo.add(info.getSourceType());
			typeInfo.add(info.getTargetType());
			return typeInfo;
		}
		Class classToIntrospect = converter.getClass();
		while (classToIntrospect != null) {
			Type[] genericInterfaces = classToIntrospect.getGenericInterfaces();
			for (Type genericInterface : genericInterfaces) {
				if (genericInterface instanceof ParameterizedType) {
					ParameterizedType pInterface = (ParameterizedType) genericInterface;
					if (Converter.class.isAssignableFrom((Class) pInterface.getRawType())
							|| ConverterFactory.class.isAssignableFrom((Class) pInterface.getRawType())) {
						Class s = getParameterClass(pInterface.getActualTypeArguments()[0], converter.getClass());
						Class t = getParameterClass(pInterface.getActualTypeArguments()[1], converter.getClass());
						typeInfo.add(getParameterClass(s, converter.getClass()));
						typeInfo.add(getParameterClass(t, converter.getClass()));
						break;
					}
				}
			}
			classToIntrospect = classToIntrospect.getSuperclass();
		}
		if (typeInfo.size() != 2) {
			throw new IllegalArgumentException("Unable to extract source and target class arguments from Converter ["
					+ converter.getClass().getName() + "]; does the Converter specify the <S, T> generic types?");
		}
		return typeInfo;
	}
{
			if (parameterType instanceof TypeVariable) {
				parameterType = GenericTypeResolver.resolveTypeVariable((TypeVariable) parameterType, converterClass);
			}
			if (parameterType instanceof Class) {
				return (Class) parameterType;
			}
			throw new IllegalArgumentException("Unable to obtain the java.lang.Class for parameterType [" + parameterType
					+ "] on Formatter [" + converterClass.getName() + "]");
		}
{
		ServletContextAdapter adaptedContext = new ServletContextAdapter(new DelegatingServletConfig());
		TilesContainerFactory factory = TilesContainerFactory.getFactory(adaptedContext);
		return factory.createContainer(adaptedContext);
	}
{
		Method testMethod = testContext.getTestMethod();
		Assert.notNull(testMethod, "The test method of the supplied TestContext must not be null");

		boolean dirtiesContext = testMethod.isAnnotationPresent(DirtiesContext.class);
		if (logger.isDebugEnabled()) {
			logger.debug("After test method: context [" + testContext + "], dirtiesContext [" + dirtiesContext + "].");
		}

		if (dirtiesContext) {
			testContext.markApplicationContextDirty();
			testContext.setAttribute(DependencyInjectionTestExecutionListener.REINJECT_DEPENDENCIES_ATTRIBUTE,
					Boolean.TRUE);
		}
	}
{
		ArrayListBindingResults results = new ArrayListBindingResults(values.size());
		for (UserValue value : values) {
			BindingImpl binding = (BindingImpl) getBinding(value.getProperty());
			if (binding != null) {
				results.add(binding.setValue(value.getValue()));
			} else {
				results.add(new NoSuchBindingResult(value));
			}
		}
		return results;
	}
{
		if (StringUtils.hasLength(this.contextPath) && !ObjectUtils.isEmpty(this.classesToBeBound)) {
			throw new IllegalArgumentException("Specify either 'contextPath' or 'classesToBeBound property'; not both");
		}
		if (StringUtils.hasLength(this.contextPath)) {
			return createJaxbContextFromContextPath();
		}
		else if (!ObjectUtils.isEmpty(this.classesToBeBound)) {
			return createJaxbContextFromClasses();
		}
		else {
			throw new IllegalArgumentException("setting either contextPath or classesToBeBound is required");
		}
	}
{
			Class type;
			try { 
				type = getValueType();
			} catch (EvaluationException e) {
				throw new IllegalArgumentException("Failed to get property expression value type - this should not happen", e);
			}
			TypeDescriptor<?> typeDesc = TypeDescriptor.valueOf(type);
			return typeDesc.isCollection() || typeDesc.isArray();
		}
{
		Assert.isTrue(index >= 0, "Index must not be negative");
		ValueHolder valueHolder = this.indexedArgumentValues.get(index);
		if (valueHolder != null) {
			if (valueHolder.getType() == null ||
					(requiredType != null && requiredType.getName().equals(valueHolder.getType()))) {
				return valueHolder;
			}
		}
		return null;
	}
{
		Token t = peekToken();
		SpelNodeImpl expr = null;
		if (t!=null && peekToken(TokenKind.DOT,TokenKind.SAFE_NAVI)) {
			expr = eatDottedNode();
		} else {
			expr = maybeEatNonDottedNode();
		}
		if (expr==null) {
			return false;
		} else {
			push(expr);
			return true;
		}
	}
{
		ManagedAttribute gma =
				(getter == null) ? ManagedAttribute.EMPTY : this.attributeSource.getManagedAttribute(getter);
		ManagedAttribute sma =
				(setter == null) ? ManagedAttribute.EMPTY : this.attributeSource.getManagedAttribute(setter);

		applyCurrencyTimeLimit(desc, resolveIntDescriptor(gma.getCurrencyTimeLimit(), sma.getCurrencyTimeLimit()));

		Object defaultValue = resolveObjectDescriptor(gma.getDefaultValue(), sma.getDefaultValue());
		desc.setField(FIELD_DEFAULT, defaultValue);

		String persistPolicy = resolveStringDescriptor(gma.getPersistPolicy(), sma.getPersistPolicy());
		if (StringUtils.hasLength(persistPolicy)) {
			desc.setField(FIELD_PERSIST_POLICY, persistPolicy);
		}
		int persistPeriod = resolveIntDescriptor(gma.getPersistPeriod(), sma.getPersistPeriod());
		if (persistPeriod >= 0) {
			desc.setField(FIELD_PERSIST_PERIOD, Integer.toString(persistPeriod));
		}
	}
{
		if (logger.isWarnEnabled()) {
			logger.warn("Could not shutdown in-memory Derby database", e);
		}
	}
{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("org/springframework/jdbc/config/jdbc-config.xml");
		DataSource ds = context.getBean("dataSource", DataSource.class);
		JdbcTemplate t = new JdbcTemplate(ds);
		assertEquals(1, t.queryForInt("select count(*) from T_TEST"));
	}
{
		EmbeddedDatabase db = EmbeddedDatabaseBuilder.buildDefault();
		JdbcTemplate template = new JdbcTemplate(db);
		assertEquals("Keith", template.queryForObject("select NAME from T_TEST", String.class));
		db.shutdown();
	}
{
		beanFactory.registerScope(WebApplicationContext.SCOPE_REQUEST, new RequestScope());
		beanFactory.registerScope(WebApplicationContext.SCOPE_SESSION, new SessionScope(false));
		beanFactory.registerScope(WebApplicationContext.SCOPE_GLOBAL_SESSION, new SessionScope(true));

		beanFactory.registerResolvableDependency(ServletRequest.class, new ObjectFactory<ServletRequest>() {
			public ServletRequest getObject() {
				RequestAttributes requestAttr = RequestContextHolder.currentRequestAttributes();
				if (!(requestAttr instanceof ServletRequestAttributes)) {
					throw new IllegalStateException("Current request is not a servlet request");
				}
				return ((ServletRequestAttributes) requestAttr).getRequest();
			}
		});
		beanFactory.registerResolvableDependency(HttpSession.class, new ObjectFactory<HttpSession>() {
			public HttpSession getObject() {
				RequestAttributes requestAttr = RequestContextHolder.currentRequestAttributes();
				if (!(requestAttr instanceof ServletRequestAttributes)) {
					throw new IllegalStateException("Current request is not a servlet request");
				}
				return ((ServletRequestAttributes) requestAttr).getRequest().getSession();
			}
		});
	}
{
			if (this.skip == null && this.pd != null && pvs != null && pvs.contains(this.pd.getName())) {
				// Explicit value provided as part of the bean definition.
				this.skip = Boolean.TRUE;
			}
			if (this.skip != null && this.skip) {
				return;
			}
			Method method = (Method) this.member;
			try {
				Object[] arguments = null;
				if (this.cached) {
					if (this.cachedMethodArguments != null) {
						arguments = new Object[this.cachedMethodArguments.length];
						for (int i = 0; i < arguments.length; i++) {
							Object cachedArg = this.cachedMethodArguments[i];
							if (cachedArg instanceof DependencyDescriptor) {
								DependencyDescriptor descriptor = (DependencyDescriptor) cachedArg;
								TypeConverter typeConverter = beanFactory.getTypeConverter();
								arguments[i] = beanFactory.resolveDependency(descriptor, beanName, null, typeConverter);
							}
							else if (cachedArg instanceof RuntimeBeanReference) {
								arguments[i] = beanFactory.getBean(((RuntimeBeanReference) cachedArg).getBeanName());
							}
							else {
								arguments[i] = cachedArg;
							}
						}
					}
				}
				else {
					Class[] paramTypes = method.getParameterTypes();
					arguments = new Object[paramTypes.length];
					Set<String> autowiredBeanNames = new LinkedHashSet<String>(arguments.length);
					TypeConverter typeConverter = beanFactory.getTypeConverter();
					this.cachedMethodArguments = new Object[arguments.length];
					for (int i = 0; i < arguments.length; i++) {
						MethodParameter methodParam = new MethodParameter(method, i);
						GenericTypeResolver.resolveParameterType(methodParam, bean.getClass());
						DependencyDescriptor descriptor = new DependencyDescriptor(methodParam, this.required);
						this.cachedMethodArguments[i] = descriptor;
						arguments[i] = beanFactory.resolveDependency(
								descriptor, beanName, autowiredBeanNames, typeConverter);
						if (arguments[i] == null) {
							arguments = null;
							break;
						}
					}
					if (arguments != null) {
						registerDependentBeans(beanName, autowiredBeanNames);
						if (autowiredBeanNames.size() == paramTypes.length) {
							Iterator<String> it = autowiredBeanNames.iterator();
							for (int i = 0; i < paramTypes.length; i++) {
								String autowiredBeanName = it.next();
								if (beanFactory.containsBean(autowiredBeanName)) {
									if (beanFactory.isTypeMatch(autowiredBeanName, paramTypes[i])) {
										this.cachedMethodArguments[i] = new RuntimeBeanReference(autowiredBeanName);
									}
								}
								else {
									this.cachedMethodArguments[i] = arguments[i];
								}
							}
						}
					}
					else {
						this.cachedMethodArguments = null;
					}
					this.cached = true;
				}
				if (this.skip == null) {
					if (this.pd != null && pvs instanceof MutablePropertyValues) {
						((MutablePropertyValues) pvs).registerProcessedProperty(this.pd.getName());
					}
					this.skip = Boolean.FALSE;
				}
				if (arguments != null) {
					ReflectionUtils.makeAccessible(method);
					method.invoke(bean, arguments);
				}
			}
			catch (InvocationTargetException ex) {
				throw ex.getTargetException();
			}
			catch (Throwable ex) {
				throw new BeanCreationException("Could not autowire method: " + method, ex);
			}
		}
{
		Map<String, Object> attrs = new HashMap<String, Object>();
		Method[] methods = annotation.annotationType().getDeclaredMethods();
		for (int j = 0; j < methods.length; j++) {
			Method method = methods[j];
			if (method.getParameterTypes().length == 0 && method.getReturnType() != void.class) {
				try {
					attrs.put(method.getName(), method.invoke(annotation));
				}
				catch (Exception ex) {
					throw new IllegalStateException("Could not obtain annotation attribute values", ex);
				}
			}
		}
		return attrs;
	}
{
		Assert.notNull(type, "The TestDatabaseType is required");
		dataSourceConfigurer = EmbeddedDatabaseConfigurerFactory.getConfigurer(type);
	}
{
		executeSqlScript(template, new EncodedResource(schemaLocation, sqlScriptEncoding), false);
	}
{		int nextPossibility = -1;		boolean foundOne = false;		while (!foundOne) {			nextPossibility = expressionString.indexOf(toSearchFor, startIdx);			if (nextPossibility==-1) {				return -1;			} else {				if (nextPossibility>0) {					// check for the escape character					if (expressionString.charAt(nextPossibility-1)!='\\') {						// it was escaped, do not treat this as a real prefix						foundOne = true;					} else {						startIdx = nextPossibility+toSearchFor.length();					}				} else {					foundOne = true;				}			}		}		return nextPossibility;	}
{
		SpelAntlrExpressionParser parser = new SpelAntlrExpressionParser();
		Expression ex = parser.parseExpression("#{'Unable to render embedded object: File ({#this == 2\\}'}", TemplateExpressionParsingTests.HASH_DELIMITED_PARSER_CONTEXT);
		assertEquals("Unable to render embedded object: File ({#this == 2}",ex.getValue());
		
		ex = parser.parseExpression("This is the last odd number in the list: ${listOfNumbersUpToTen.$[#this%2==1]}",TemplateExpressionParsingTests.DEFAULT_TEMPLATE_PARSER_CONTEXT);
		assertEquals("This is the last odd number in the list: 9",ex.getValue(TestScenarioCreator.getTestEvaluationContext()));

		ex = parser.parseExpression("Hello ${'here is a curly bracket \\}'}",TemplateExpressionParsingTests.DEFAULT_TEMPLATE_PARSER_CONTEXT);
		assertEquals("Hello here is a curly bracket }",ex.getValue());
	}
{
		// Expose model to JSP tags (as request attributes).
		exposeModelAsRequestAttributes(model, request);

		// Expose all standard FreeMarker hash models.
		model.put(FreemarkerServlet.KEY_JSP_TAGLIBS, this.taglibFactory);
		model.put(FreemarkerServlet.KEY_APPLICATION, this.servletContextHashModel);
		model.put(FreemarkerServlet.KEY_SESSION, buildSessionModel(request, response));
		model.put(FreemarkerServlet.KEY_REQUEST, new HttpRequestHashModel(request, response, getObjectWrapper()));
		model.put(FreemarkerServlet.KEY_REQUEST_PARAMETERS, new HttpRequestParametersHashModel(request));

		if (logger.isDebugEnabled()) {
			logger.debug("Rendering FreeMarker template [" + getUrl() + "] in FreeMarkerView '" + getBeanName() + "'");
		}
		// Grab the locale-specific version of the template.
		Locale locale = RequestContextUtils.getLocale(request);
		processTemplate(getTemplate(locale), model, response);
	}
{
		String defaultTypeClassName = collectionEle.getAttribute(VALUE_TYPE_ATTRIBUTE);
		NodeList nl = collectionEle.getChildNodes();
		ManagedSet set = new ManagedSet(nl.getLength());
		set.setSource(extractSource(collectionEle));
		set.setMergeEnabled(parseMergeAttribute(collectionEle));
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node instanceof Element && !DomUtils.nodeNameEquals(node, DESCRIPTION_ELEMENT)) {
				set.add(parsePropertySubElement((Element) node, bd, defaultTypeClassName));
			}
		}
		return set;
	}
{
		if (type != null) {
			return type;
		} else if (field != null) {
			return field.getType();
		} else if (methodParameter != null) {
			return methodParameter.getParameterType();
		} else {
			return null;
		}
	}
{
		try {
			// TODO shouldn't do all this if generic info is null - should cache executor after first iteration?
			Map map = (Map) source;
			Map targetMap = (Map) getImpl(targetType.getType()).newInstance();
			Iterator<Map.Entry<?, ?>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = it.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				key = conversionService.executeConversion(key, TypeDescriptor.valueOf(targetType.getMapKeyType()));
				value = conversionService.executeConversion(value, TypeDescriptor.valueOf(targetType.getMapValueType()));
				targetMap.put(key, value);
			}
			return targetMap;
		} catch (Exception e) {
			throw new ConversionExecutionException(source, sourceType.getType(), targetType, e);
		}
	}
{
		Collection sourceCollection = (Collection) source;
		Class targetCollectionType = getTargetCollectionType();
		Class implClass = CollectionConversionUtils.getImpl(targetCollectionType);
		Collection targetCollection = (Collection) implClass.newInstance();
		ConversionExecutor elementConverter = getElementConverter();
		Class elementType;
		if (elementConverter == null) {
			elementType = getTargetElementType();
		} else {
			elementType = null;
		}
		Iterator it = sourceCollection.iterator();
		while (it.hasNext()) {
			Object value = it.next();
			if (elementConverter == null && elementType != null) {
				elementConverter = getConversionService().getConversionExecutor(value.getClass(), TypeDescriptor.valueOf(elementType)); 			
			}
			value = elementConverter.execute(value);
			targetCollection.add(value);
		}
		return targetCollection;
	}
{
		Class<?> sourceElementType = getSourceType().getElementType();
		Class<?> targetElementType = getTargetType().getElementType();
		return (sourceElementType != null && targetElementType != null) ? conversionService.getElementConverter(sourceElementType, targetElementType) : null;
	}
{
		@SuppressWarnings("serial") DispatcherServlet servlet = new DispatcherServlet() {
			@Override
			protected WebApplicationContext createWebApplicationContext(WebApplicationContext parent) {
				GenericWebApplicationContext wac = new GenericWebApplicationContext();
				wac.registerBeanDefinition("controller", new RootBeanDefinition(MyController.class));
				wac.refresh();
				return wac;
			}
		};
		servlet.init(new MockServletConfig());

		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/myPath.do");
		MockHttpServletResponse response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals("test", response.getContentAsString());
	}
{
		ParameterizedBeanPropertyRowMapper<T> newInstance = new ParameterizedBeanPropertyRowMapper<T>();
		newInstance.setMappedClass(mappedClass);
		return newInstance;
	}
{
		if (uriTemplateVariables != null) {
			String varName = new String(patArr, curlyIdxStart + 1, curlyIdxEnd - curlyIdxStart - 1);
			String varValue = new String(strArr, valIdxStart, valIdxEnd - valIdxStart + 1);
			uriTemplateVariables.put(varName, varValue);
		}
	}
{
		if (!bdHolder.getBeanDefinition().isAutowireCandidate()) {
			// if explicitly false, do not proceed with qualifier check
			return false;
		}
		if (descriptor == null || ObjectUtils.isEmpty(descriptor.getAnnotations())) {
			// no qualification necessary
			return true;
		}
		AbstractBeanDefinition bd = (AbstractBeanDefinition) bdHolder.getBeanDefinition();
		SimpleTypeConverter typeConverter = new SimpleTypeConverter();
		Annotation[] annotations = descriptor.getAnnotations();
		for (Annotation annotation : annotations) {
			Class<? extends Annotation> type = annotation.annotationType();
			if (isQualifier(type)) {
				AutowireCandidateQualifier qualifier = bd.getQualifier(type.getName());
				if (qualifier == null) {
					qualifier = bd.getQualifier(ClassUtils.getShortName(type));
				}
				if (qualifier == null && bd.hasBeanClass()) {
					// look for matching annotation on the target class
					Class<?> beanClass = bd.getBeanClass();
					Annotation targetAnnotation = beanClass.getAnnotation(type);
					if (targetAnnotation != null && targetAnnotation.equals(annotation)) {
						return true;
					}
				}
				Map<String, Object> attributes = AnnotationUtils.getAnnotationAttributes(annotation);
				if (attributes.isEmpty() && qualifier == null) {
					// if no attributes, the qualifier must be present
					return false;
				}
				for (Map.Entry<String, Object> entry : attributes.entrySet()) {
					String attributeName = entry.getKey();
					Object expectedValue = entry.getValue();
					Object actualValue = null;
					// check qualifier first
					if (qualifier != null) {
						actualValue = qualifier.getAttribute(attributeName);
					}
					if (actualValue == null) {
						// fall back on bean definition attribute
						actualValue = bd.getAttribute(attributeName);
					}
					if (actualValue == null && attributeName.equals(AutowireCandidateQualifier.VALUE_KEY) &&
							(expectedValue.equals(bdHolder.getBeanName()) ||
									ObjectUtils.containsElement(bdHolder.getAliases(), expectedValue))) {
						// fall back on bean name (or alias) match
						continue;
					}
					if (actualValue == null && qualifier != null) {
						// fall back on default, but only if the qualifier is present
						actualValue = AnnotationUtils.getDefaultValue(annotation, attributeName);
					}
					if (actualValue != null) {
						actualValue = typeConverter.convertIfNecessary(actualValue, expectedValue.getClass());
					}
					if (!expectedValue.equals(actualValue)) {
						return false;
					}
				}
			}
		}
		return true;
	}
{
		for (Annotation annotation : descriptor.getAnnotations()) {
			if (this.valueAnnotationType.isInstance(annotation)) {
				Object value = AnnotationUtils.getValue(annotation);
				if (value == null) {
					throw new IllegalStateException("Value annotation must have a value attribute");
				}
				return value;
			}
		}
		return null;
	}
{

		BeanWrapperImpl bw = new BeanWrapperImpl();
		this.beanFactory.initBeanWrapper(bw);

		Constructor constructorToUse = null;
		Object[] argsToUse = null;

		if (explicitArgs != null) {
			argsToUse = explicitArgs;
		}
		else {
			constructorToUse = (Constructor) mbd.resolvedConstructorOrFactoryMethod;
			if (constructorToUse != null) {
				// Found a cached constructor...
				argsToUse = mbd.resolvedConstructorArguments;
				if (argsToUse == null) {
					Class[] paramTypes = constructorToUse.getParameterTypes();
					Object[] argsToResolve = mbd.preparedConstructorArguments;
					TypeConverter converter = (this.typeConverter != null ? this.typeConverter : bw);
					BeanDefinitionValueResolver valueResolver =
							new BeanDefinitionValueResolver(this.beanFactory, beanName, mbd, converter);
					argsToUse = new Object[argsToResolve.length];
					for (int i = 0; i < argsToResolve.length; i++) {
						Object argValue = argsToResolve[i];
						MethodParameter methodParam = new MethodParameter(constructorToUse, i);
						GenericTypeResolver.resolveParameterType(methodParam, constructorToUse.getDeclaringClass());
						if (argValue instanceof AutowiredArgumentMarker) {
							argValue = resolveAutowiredArgument(methodParam, beanName, null, converter);
						}
						else if (argValue instanceof BeanMetadataElement) {
							argValue = valueResolver.resolveValueIfNecessary("constructor argument", argValue);
						}
						argsToUse[i] = converter.convertIfNecessary(argValue, paramTypes[i], methodParam);
					}
				}
			}
		}

		if (constructorToUse == null) {
			// Need to resolve the constructor.
			boolean autowiring = (chosenCtors != null ||
					mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_CONSTRUCTOR);
			ConstructorArgumentValues resolvedValues = null;

			int minNrOfArgs;
			if (explicitArgs != null) {
				minNrOfArgs = explicitArgs.length;
			}
			else {
				ConstructorArgumentValues cargs = mbd.getConstructorArgumentValues();
				resolvedValues = new ConstructorArgumentValues();
				minNrOfArgs = resolveConstructorArguments(beanName, mbd, bw, cargs, resolvedValues);
			}

			// Take specified constructors, if any.
			Constructor[] candidates = chosenCtors;
			if (candidates == null) {
				Class beanClass = mbd.getBeanClass();
				try {
					candidates = beanClass.getDeclaredConstructors();
				}
				catch (Throwable ex) {
					throw new BeanCreationException(mbd.getResourceDescription(), beanName,
							"Resolution of declared constructors on bean Class [" + beanClass.getName() +
									"] from ClassLoader [" + beanClass.getClassLoader() + "] failed", ex);
				}
			}
			AutowireUtils.sortConstructors(candidates);
			int minTypeDiffWeight = Integer.MAX_VALUE;

			for (int i = 0; i < candidates.length; i++) {
				Constructor candidate = candidates[i];
				Class[] paramTypes = candidate.getParameterTypes();

				if (constructorToUse != null && argsToUse.length > paramTypes.length) {
					// Already found greedy constructor that can be satisfied ->
					// do not look any further, there are only less greedy constructors left.
					break;
				}
				if (paramTypes.length < minNrOfArgs) {
					throw new BeanCreationException(mbd.getResourceDescription(), beanName,
							minNrOfArgs + " constructor arguments specified but no matching constructor found in bean '" +
							beanName + "' " +
							"(hint: specify index and/or type arguments for simple parameters to avoid type ambiguities)");
				}

				ArgumentsHolder args;
				List<Exception> causes = null;

				if (resolvedValues != null) {
					// Try to resolve arguments for current constructor.
					try {
						args = createArgumentArray(
								beanName, mbd, resolvedValues, bw, paramTypes, candidate, autowiring);
					}
					catch (UnsatisfiedDependencyException ex) {
						if (this.beanFactory.logger.isTraceEnabled()) {
							this.beanFactory.logger.trace(
									"Ignoring constructor [" + candidate + "] of bean '" + beanName + "': " + ex);
						}
						if (i == candidates.length - 1 && constructorToUse == null) {
							if (causes != null) {
								for (Exception cause : causes) {
									this.beanFactory.onSuppressedException(cause);
								}
							}
							throw ex;
						}
						else {
							// Swallow and try next constructor.
							if (causes == null) {
								causes = new LinkedList<Exception>();
							}
							causes.add(ex);
							continue;
						}
					}
				}

				else {
					// Explicit arguments given -> arguments length must match exactly.
					if (paramTypes.length != explicitArgs.length) {
						continue;
					}
					args = new ArgumentsHolder(explicitArgs);
				}

				int typeDiffWeight = args.getTypeDifferenceWeight(paramTypes);
				// Choose this constructor if it represents the closest match.
				if (typeDiffWeight < minTypeDiffWeight) {
					constructorToUse = candidate;
					argsToUse = args.arguments;
					minTypeDiffWeight = typeDiffWeight;
				}
			}

			if (constructorToUse == null) {
				throw new BeanCreationException(
						mbd.getResourceDescription(), beanName, "Could not resolve matching constructor");
			}

			if (explicitArgs == null) {
				mbd.resolvedConstructorOrFactoryMethod = constructorToUse;
			}
		}

		try {
			Object beanInstance = this.instantiationStrategy.instantiate(
					mbd, beanName, this.beanFactory, constructorToUse, argsToUse);
			bw.setWrappedInstance(beanInstance);
			return bw;
		}
		catch (Throwable ex) {
			throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Instantiation of bean failed", ex);
		}
	}
{

		ConfigurationParser parser = createConfigurationParser();
		
		for(String beanName : configBeanDefinitions.getBeanDefinitionNames()) {
			BeanDefinition beanDef = configBeanDefinitions.getBeanDefinition(beanName);
			String className = beanDef.getBeanClassName();
			
			parser.parse(className, beanName);
		}
		
	    return parser.getConfigurationModel();
	}
{
		return validating;
	}
{
		if (!readOnly) {
			Session session = getCurrentSession();
			// Read-write transaction -> flush the Hibernate Session.
			// Further check: only flush when not FlushMode.NEVER/MANUAL.
			if (!session.getFlushMode().lessThan(FlushMode.COMMIT)) {
				try {
					SessionFactoryUtils.logger.debug("Flushing Hibernate Session on transaction synchronization");
					session.flush();
				}
				catch (HibernateException ex) {
					if (this.jdbcExceptionTranslator != null && ex instanceof JDBCException) {
						JDBCException jdbcEx = (JDBCException) ex;
						throw this.jdbcExceptionTranslator.translate(
								"Hibernate flushing: " + jdbcEx.getMessage(), jdbcEx.getSQL(), jdbcEx.getSQLException());
					}
					throw SessionFactoryUtils.convertHibernateAccessException(ex);
				}
			}
		}
	}
{
		MockControl utControl = MockControl.createControl(UserTransaction.class);
		UserTransaction ut = (UserTransaction) utControl.getMock();
		ut.getStatus();
		utControl.setReturnValue(Status.STATUS_NO_TRANSACTION, 1);
		ut.begin();
		utControl.setVoidCallable(1);
		ut.getStatus();
		utControl.setReturnValue(Status.STATUS_ACTIVE, 1);
		ut.rollback();
		utControl.setVoidCallable(1);
		utControl.replay();

		MockControl sfControl = MockControl.createControl(SessionFactory.class);
		final SessionFactory sf = (SessionFactory) sfControl.getMock();
		final MockControl sessionControl = MockControl.createControl(Session.class);
		final Session session = (Session) sessionControl.getMock();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		sfControl.replay();
		sessionControl.replay();

		JtaTransactionManager ptm = new JtaTransactionManager(ut);
		TransactionTemplate tt = new TransactionTemplate(ptm);
		final List l = new ArrayList();
		l.add("test");
		assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());

		Object result = tt.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus status) {
				try {
					assertTrue("JTA synchronizations active", TransactionSynchronizationManager.isSynchronizationActive());
					HibernateTemplate ht = new HibernateTemplate(sf);
					List htl = ht.executeFind(new HibernateCallback() {
						public Object doInHibernate(org.hibernate.Session session) {
							return l;
						}
					});
					status.setRollbackOnly();
					sessionControl.verify();
					sessionControl.reset();
					session.close();
					sessionControl.setReturnValue(null, 1);
					sessionControl.replay();
					return htl;
				}
				catch (Error err) {
					err.printStackTrace();
					throw err;
				}
			}
		});
		assertTrue("Correct result list", result == l);

		assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());
		utControl.verify();
		sfControl.verify();
		sessionControl.verify();
	}
{
		List<String> aliases = new ArrayList<String>();
		synchronized (this.aliasMap) {
			for (Map.Entry<String, String> entry : this.aliasMap.entrySet()) {
				String registeredName = entry.getValue();
				if (registeredName.equals(name)) {
					aliases.add(entry.getKey());
				}
			}
		}
		return StringUtils.toStringArray(aliases);
	}
{
		if (isFrozen()) {
			throw new AopConfigException("Cannot add advisor: Configuration is frozen.");
		}
		if (!ObjectUtils.isEmpty(advisors)) {
			for (Advisor advisor : advisors) {
				if (advisor instanceof IntroductionAdvisor) {
					validateIntroductionAdvisor((IntroductionAdvisor) advisor);
				}
				Assert.notNull(advisor, "Advisor must not be null");
				this.advisors.add(advisor);
			}
			updateAdvisorArray();
			adviceChanged();
		}
	}
{
		int end = urlPath.indexOf(';');
		if (end == -1) {
			end = urlPath.indexOf('?');
			if (end == -1) {
				end = urlPath.length();
			}
		}
		int begin = urlPath.lastIndexOf('/', end) + 1;
		String filename = urlPath.substring(begin, end);
		int dotIndex = filename.lastIndexOf('.');
		if (dotIndex != -1) {
			filename = filename.substring(0, dotIndex);
		}
		return filename;
	}
{
		Assert.notNull(request, "Request must not be null");
		Assert.notNull(response, "Response must not be null");
		if (!(response instanceof MockRenderResponse)) {
			throw new IllegalArgumentException("MockPortletRequestDispatcher requires MockRenderResponse");
		}
		((MockRenderResponse) response).setIncludedUrl(this.url);
		if (logger.isDebugEnabled()) {
			logger.debug("MockPortletRequestDispatcher: including URL [" + this.url + "]");
	}
	}
{
		if (checkForXmlRootElement && clazz.getAnnotation(XmlRootElement.class) == null) {
			return false;
		}
		if (clazz.getAnnotation(XmlType.class) == null) {
			return false;
		}
		if (StringUtils.hasLength(getContextPath())) {
			String className = ClassUtils.getQualifiedName(clazz);
			int lastDotIndex = className.lastIndexOf('.');
			if (lastDotIndex == -1) {
				return false;
			}
			String packageName = className.substring(0, lastDotIndex);
			String[] contextPaths = StringUtils.tokenizeToStringArray(getContextPath(), ":");
			for (String contextPath : contextPaths) {
				if (contextPath.equals(packageName)) {
					return true;
				}
			}
			return false;
		}
		else if (!ObjectUtils.isEmpty(classesToBeBound)) {
			return Arrays.asList(classesToBeBound).contains(clazz);
		}
		return false;
	}
{
		return mergeProperties();
	}
{
		Assert.notNull(this.skeletonInvoker, "Hessian exporter has not been initialized");
		ClassLoader originalClassLoader = overrideThreadContextClassLoader();
		try {
			this.skeletonInvoker.invoke(inputStream, outputStream);
		}
		finally {
			resetThreadContextClassLoader(originalClassLoader);
		}
	}
{
		if (shortcutPossible()) {
			return doShortcut();
		}
		if (patternContainsOnlyStar()) {
			return true;
		}
		if (patternContainsOneTemplateVariable()) {
			addTemplateVariable(0, patIdxEnd, 0, strIdxEnd);
			return true;
		}
		if (!matchBeforeFirstStarOrCurly()) {
			return false;
		}
		if (allCharsUsed()) {
			return onlyStarsLeft();
		}
		if (!matchAfterLastStarOrCurly()) {
			return false;
		}
		if (allCharsUsed()) {
			return onlyStarsLeft();
		}
		// process pattern between stars. padIdxStart and patIdxEnd point
		// always to a '*'.
		while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
			int patIdxTmp = findNextStar();
			if (patIdxTmp == patIdxStart + 1 && patArr[patIdxTmp] == '*') {
				// Two stars next to each other, skip the first one.
				patIdxStart++;
				continue;
			}
			// Find the pattern between padIdxStart & padIdxTmp in str between
			// strIdxStart & strIdxEnd
			int patLength = (patIdxTmp - patIdxStart - 1);
			int strLength = (strIdxEnd - strIdxStart + 1);
			int foundIdx = -1;
			strLoop:
			for (int i = 0; i <= strLength - patLength; i++) {
				for (int j = 0; j < patLength; j++) {
					ch = patArr[patIdxStart + j + 1];
					if (ch != '?') {
						if (ch != strArr[strIdxStart + i + j]) {
							continue strLoop;
						}
					}
				}

				foundIdx = strIdxStart + i;
				break;
			}

			if (foundIdx == -1) {
				return false;
			}

			patIdxStart = patIdxTmp;
			strIdxStart = foundIdx + patLength;
		}

		return onlyStarsLeft();
	}
{
        node_return retval = new node_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token DOT74=null;
        methodOrProperty_return methodOrProperty66 = null;

        functionOrVar_return functionOrVar67 = null;

        indexer_return indexer68 = null;

        projection_return projection69 = null;

        selection_return selection70 = null;

        firstSelection_return firstSelection71 = null;

        lastSelection_return lastSelection72 = null;

        exprList_return exprList73 = null;


        Object DOT74_tree=null;

        try {
            // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:124:5: ( ( methodOrProperty | functionOrVar | indexer | projection | selection | firstSelection | lastSelection | exprList | DOT )+ )
            // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:125:2: ( methodOrProperty | functionOrVar | indexer | projection | selection | firstSelection | lastSelection | exprList | DOT )+
            {
            root_0 = (Object)adaptor.nil();

            // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:125:2: ( methodOrProperty | functionOrVar | indexer | projection | selection | firstSelection | lastSelection | exprList | DOT )+
            int cnt16=0;
            loop16:
            do {
                int alt16=10;
                switch ( input.LA(1) ) {
                case ID:
                    {
                    alt16=1;
                    }
                    break;
                case POUND:
                    {
                    alt16=2;
                    }
                    break;
                case LBRACKET:
                    {
                    alt16=3;
                    }
                    break;
                case PROJECT:
                    {
                    alt16=4;
                    }
                    break;
                case SELECT:
                    {
                    alt16=5;
                    }
                    break;
                case SELECT_FIRST:
                    {
                    alt16=6;
                    }
                    break;
                case SELECT_LAST:
                    {
                    alt16=7;
                    }
                    break;
                case LPAREN:
                    {
                    alt16=8;
                    }
                    break;
                case DOT:
                    {
                    alt16=9;
                    }
                    break;

                }

                switch (alt16) {
            	case 1 :
            	    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:125:4: methodOrProperty
            	    {
            	    pushFollow(FOLLOW_methodOrProperty_in_node707);
            	    methodOrProperty66=methodOrProperty();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) adaptor.addChild(root_0, methodOrProperty66.getTree());

            	    }
            	    break;
            	case 2 :
            	    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:126:4: functionOrVar
            	    {
            	    pushFollow(FOLLOW_functionOrVar_in_node713);
            	    functionOrVar67=functionOrVar();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) adaptor.addChild(root_0, functionOrVar67.getTree());

            	    }
            	    break;
            	case 3 :
            	    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:127:7: indexer
            	    {
            	    pushFollow(FOLLOW_indexer_in_node721);
            	    indexer68=indexer();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) adaptor.addChild(root_0, indexer68.getTree());

            	    }
            	    break;
            	case 4 :
            	    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:128:7: projection
            	    {
            	    pushFollow(FOLLOW_projection_in_node729);
            	    projection69=projection();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) adaptor.addChild(root_0, projection69.getTree());

            	    }
            	    break;
            	case 5 :
            	    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:129:7: selection
            	    {
            	    pushFollow(FOLLOW_selection_in_node738);
            	    selection70=selection();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) adaptor.addChild(root_0, selection70.getTree());

            	    }
            	    break;
            	case 6 :
            	    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:130:7: firstSelection
            	    {
            	    pushFollow(FOLLOW_firstSelection_in_node747);
            	    firstSelection71=firstSelection();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) adaptor.addChild(root_0, firstSelection71.getTree());

            	    }
            	    break;
            	case 7 :
            	    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:131:7: lastSelection
            	    {
            	    pushFollow(FOLLOW_lastSelection_in_node756);
            	    lastSelection72=lastSelection();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) adaptor.addChild(root_0, lastSelection72.getTree());

            	    }
            	    break;
            	case 8 :
            	    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:132:7: exprList
            	    {
            	    pushFollow(FOLLOW_exprList_in_node765);
            	    exprList73=exprList();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) adaptor.addChild(root_0, exprList73.getTree());

            	    }
            	    break;
            	case 9 :
            	    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:133:7: DOT
            	    {
            	    DOT74=(Token)input.LT(1);
            	    match(input,DOT,FOLLOW_DOT_in_node773); if (failed) return retval;
            	    if ( backtracking==0 ) {
            	    DOT74_tree = (Object)adaptor.create(DOT74);
            	    adaptor.addChild(root_0, DOT74_tree);
            	    }

            	    }
            	    break;

            	default :
            	    if ( cnt16 >= 1 ) break loop16;
            	    if (backtracking>0) {failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(16, input);
                        throw eee;
                }
                cnt16++;
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( backtracking==0 ) {
                retval.tree = (Object)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

                catch(RecognitionException e) {
                        //reportError(e);
                        throw e;
                }
        finally {
        }
        return retval;
    }
{
        node_return retval = new node_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token DOT74=null;
        methodOrProperty_return methodOrProperty66 = null;

        functionOrVar_return functionOrVar67 = null;

        indexer_return indexer68 = null;

        projection_return projection69 = null;

        selection_return selection70 = null;

        firstSelection_return firstSelection71 = null;

        lastSelection_return lastSelection72 = null;

        exprList_return exprList73 = null;


        Object DOT74_tree=null;

        try {
            // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:124:5: ( ( methodOrProperty | functionOrVar | indexer | projection | selection | firstSelection | lastSelection | exprList | DOT )+ )
            // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:125:2: ( methodOrProperty | functionOrVar | indexer | projection | selection | firstSelection | lastSelection | exprList | DOT )+
            {
            root_0 = (Object)adaptor.nil();

            // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:125:2: ( methodOrProperty | functionOrVar | indexer | projection | selection | firstSelection | lastSelection | exprList | DOT )+
            int cnt16=0;
            loop16:
            do {
                int alt16=10;
                switch ( input.LA(1) ) {
                case ID:
                    {
                    alt16=1;
                    }
                    break;
                case POUND:
                    {
                    alt16=2;
                    }
                    break;
                case LBRACKET:
                    {
                    alt16=3;
                    }
                    break;
                case PROJECT:
                    {
                    alt16=4;
                    }
                    break;
                case SELECT:
                    {
                    alt16=5;
                    }
                    break;
                case SELECT_FIRST:
                    {
                    alt16=6;
                    }
                    break;
                case SELECT_LAST:
                    {
                    alt16=7;
                    }
                    break;
                case LPAREN:
                    {
                    alt16=8;
                    }
                    break;
                case DOT:
                    {
                    alt16=9;
                    }
                    break;

                }

                switch (alt16) {
            	case 1 :
            	    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:125:4: methodOrProperty
            	    {
            	    pushFollow(FOLLOW_methodOrProperty_in_node707);
            	    methodOrProperty66=methodOrProperty();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) adaptor.addChild(root_0, methodOrProperty66.getTree());

            	    }
            	    break;
            	case 2 :
            	    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:126:4: functionOrVar
            	    {
            	    pushFollow(FOLLOW_functionOrVar_in_node713);
            	    functionOrVar67=functionOrVar();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) adaptor.addChild(root_0, functionOrVar67.getTree());

            	    }
            	    break;
            	case 3 :
            	    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:127:7: indexer
            	    {
            	    pushFollow(FOLLOW_indexer_in_node721);
            	    indexer68=indexer();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) adaptor.addChild(root_0, indexer68.getTree());

            	    }
            	    break;
            	case 4 :
            	    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:128:7: projection
            	    {
            	    pushFollow(FOLLOW_projection_in_node729);
            	    projection69=projection();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) adaptor.addChild(root_0, projection69.getTree());

            	    }
            	    break;
            	case 5 :
            	    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:129:7: selection
            	    {
            	    pushFollow(FOLLOW_selection_in_node738);
            	    selection70=selection();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) adaptor.addChild(root_0, selection70.getTree());

            	    }
            	    break;
            	case 6 :
            	    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:130:7: firstSelection
            	    {
            	    pushFollow(FOLLOW_firstSelection_in_node747);
            	    firstSelection71=firstSelection();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) adaptor.addChild(root_0, firstSelection71.getTree());

            	    }
            	    break;
            	case 7 :
            	    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:131:7: lastSelection
            	    {
            	    pushFollow(FOLLOW_lastSelection_in_node756);
            	    lastSelection72=lastSelection();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) adaptor.addChild(root_0, lastSelection72.getTree());

            	    }
            	    break;
            	case 8 :
            	    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:132:7: exprList
            	    {
            	    pushFollow(FOLLOW_exprList_in_node765);
            	    exprList73=exprList();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) adaptor.addChild(root_0, exprList73.getTree());

            	    }
            	    break;
            	case 9 :
            	    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:133:7: DOT
            	    {
            	    DOT74=(Token)input.LT(1);
            	    match(input,DOT,FOLLOW_DOT_in_node773); if (failed) return retval;
            	    if ( backtracking==0 ) {
            	    DOT74_tree = (Object)adaptor.create(DOT74);
            	    adaptor.addChild(root_0, DOT74_tree);
            	    }

            	    }
            	    break;

            	default :
            	    if ( cnt16 >= 1 ) break loop16;
            	    if (backtracking>0) {failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(16, input);
                        throw eee;
                }
                cnt16++;
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( backtracking==0 ) {
                retval.tree = (Object)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

                catch(RecognitionException e) {
                        //reportError(e);
                        throw e;
                }
        finally {
        }
        return retval;
    }
{
		try {
			Expression expr = parser.parseExpression(expression);
			if (expr == null) {
				fail("Parser returned null for expression");
			}
			@SuppressWarnings("unused")
			Object value = expr.getValue(eContext);
			fail("Should have failed with message " + expectedMessage);
		} catch (EvaluationException ee) {
			SpelException ex = (SpelException) ee;
			if (ex.getMessageUnformatted() != expectedMessage) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
				assertEquals("Failed to get expected message", expectedMessage, ex.getMessageUnformatted());
			}
			if (otherProperties != null && otherProperties.length != 0) {
				// first one is expected position of the error within the string
				int pos = ((Integer) otherProperties[0]).intValue();
				assertEquals("Did not get correct position reported in error ", pos, ex.getPosition());
				if (otherProperties.length > 1) {
					// Check inserts match
					Object[] inserts = ex.getInserts();
					if (inserts == null) {
						inserts = new Object[0];
					}
					if (inserts.length < otherProperties.length - 1) {
						ex.printStackTrace();
						fail("Cannot check " + (otherProperties.length - 1)
								+ " properties of the exception, it only has " + inserts.length + " inserts");
					}
					for (int i = 1; i < otherProperties.length; i++) {
						if (!inserts[i - 1].equals(otherProperties[i])) {
							ex.printStackTrace();
							fail("Insert does not match, expected '" + otherProperties[i] + "' but insert value was '"
									+ inserts[i - 1] + "'");
						}
					}
				}
			}
		} catch (ParseException pe) {
			pe.printStackTrace();
			fail("Unexpected Exception: " + pe.getMessage());
		}
	}

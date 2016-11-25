{
		set(CONTENT_DISPOSITION, contentDisposition.toString());
	}
{
		WebExchangeDataBinder binder = new WebExchangeDataBinder(null);
		if (initializer != null) {
			initializer.initBinder(binder);
		}
		return binder;
	}
{
		return (T) this;
	}
{
		this.dispatcherServletCustomizers.add(customizer);
		return (T) this;
	}
{
		Assert.notNull(exchange, "'exchange' is required.");
		try {
			return new URI(exchange.getRequestScheme(), null,
					exchange.getHostName(), exchange.getHostPort(),
					exchange.getRequestURI(), exchange.getQueryString(), null);
		}
		catch (URISyntaxException ex) {
			throw new IllegalStateException("Could not get URI: " + ex.getMessage(), ex);
		}
	}
{
		Assert.notNull(method, "Method must not be null");
		return (clazz != null ? clazz : method.getDeclaringClass()).getName() + '.' + method.getName();
	}
{
		this.initializer = initializer;
		this.simpleValueDataBinder = new WebExchangeDataBinder(null);
		if (initializer != null) {
			initializer.initBinder(this.simpleValueDataBinder);
		}
	}
{
		ServerHttpRequest request = new MockServerHttpRequest(httpMethod, url);
		MockServerHttpResponse response = new MockServerHttpResponse();
		MockWebSessionManager sessionManager = new MockWebSessionManager();
		return new DefaultServerWebExchange(request, response, sessionManager);
	}
{
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(this.eventLoopGroup).channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel channel) throws Exception {
						configureChannel(channel.config());
						ChannelPipeline pipeline = channel.pipeline();
						if (isSecure) {
							Assert.notNull(sslContext, "sslContext should not be null");
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
		return bootstrap;
	}
{
		String contextPath = request.getContextPath();
		String path = request.getURI().getRawPath();
		if (!StringUtils.hasText(contextPath)) {
			return path;
		}
		return (path.length() > contextPath.length() ? path.substring(contextPath.length()) : "");
	}
{
		try {
			Resource resource = location.createRelative(resourcePath);
			if (resource.exists() && resource.isReadable()) {
				if (checkResource(resource, location)) {
					if (logger.isTraceEnabled()) {
						logger.trace("Found match: " + resource);
					}
					return Mono.just(resource);
				}
				else if (logger.isTraceEnabled()) {
					logger.trace("Resource path=\"" + resourcePath + "\" was successfully resolved " +
							"but resource=\"" + resource.getURL() + "\" is neither under the " +
							"current location=\"" + location.getURL() + "\" nor under any of the " +
							"allowed locations=" + Arrays.asList(getAllowedLocations()));
				}
			}
			else if (logger.isTraceEnabled()) {
				logger.trace("No match for location: " + location);
			}
			return Mono.empty();
		}
		catch (IOException ex) {
			if (logger.isTraceEnabled()) {
				logger.trace("Failure checking for relative resource under location + " + location, ex);
			}
			return Mono.error(ex);
		}
	}
{
		Resource location = new ClassPathResource("test/", GzipResourceResolverTests.class);
		Resource fileResource = new FileSystemResource(location.createRelative(filePath).getFile());
		Resource gzFileResource = location.createRelative(filePath + ".gz");

		if (gzFileResource.getFile().createNewFile()) {
			GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(gzFileResource.getFile()));
			FileCopyUtils.copy(fileResource.getInputStream(), out);
		}

		assertTrue(gzFileResource.exists());
	}
{
		Set<BeanDefinition> candidates = new LinkedHashSet<>();
		try {
			Set<String> types = new HashSet<>();
			for (TypeFilter filter : this.includeFilters) {
				String stereotype = extractStereotype(filter);
				if (stereotype == null) {
					throw new IllegalArgumentException("Failed to extract stereotype from "+ filter);
				}
				types.addAll(this.componentsIndex.getCandidateTypes(basePackage, stereotype));
			}
			boolean traceEnabled = logger.isTraceEnabled();
			boolean debugEnabled = logger.isDebugEnabled();
			for (String type : types) {
				MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(type);
				if (isCandidateComponent(metadataReader)) {
					AnnotatedGenericBeanDefinition sbd = new AnnotatedGenericBeanDefinition(
							metadataReader.getAnnotationMetadata());
					if (isCandidateComponent(sbd)) {
						if (debugEnabled) {
							logger.debug("Using candidate component class from index: " + type);
						}
						candidates.add(sbd);
					}
					else {
						if (debugEnabled) {
							logger.debug("Ignored because not a concrete top-level class: " + type);
						}
					}
				}
				else {
					if (traceEnabled) {
						logger.trace("Ignored because matching an exclude filter: " + type);
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
				builder.append(StringUtils.encodeHttpHeaderFieldParam(filename, charset));
			}
		}
		set(CONTENT_DISPOSITION, builder.toString());
	}
{

		if (descriptor instanceof MultiElementDependencyDescriptor || containsSingleton(candidateName)) {
			candidates.put(candidateName, descriptor.resolveCandidate(candidateName, requiredType, this));
		}
		else {
			candidates.put(candidateName, getType(candidateName));
		}
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
			if (!autowireCandidates.isEmpty()) {
				beanNames = autowireCandidates.toArray(new String[autowireCandidates.size()]);
			}
		}
		if (beanNames.length == 1) {
			String beanName = beanNames[0];
			return new NamedBeanHolder<>(beanName, getBean(beanName, requiredType, args));
		}
		else if (beanNames.length > 1) {
			Map<String, Object> candidates = new LinkedHashMap<>();
			for (String beanName : beanNames) {
				candidates.put(beanName, getBean(beanName, requiredType, args));
			}
			String primaryCandidate = determinePrimaryCandidate(candidates, requiredType);
			if (primaryCandidate != null) {
				return new NamedBeanHolder<>(primaryCandidate, getBean(primaryCandidate, requiredType, args));
			}
			String priorityCandidate = determineHighestPriorityCandidate(candidates, requiredType);
			if (priorityCandidate != null) {
				return new NamedBeanHolder<>(priorityCandidate, getBean(priorityCandidate, requiredType, args));
			}
			throw new NoUniqueBeanDefinitionException(requiredType, candidates.keySet());
		}
		return null;
	}
{
			if (ServletServerHttpResponse.this.flushOnNext) {
				if (logger.isTraceEnabled()) {
					logger.trace("flush");
				}
				flush();
			}

			boolean ready = this.outputStream.isReady();

			if (this.logger.isTraceEnabled()) {
				this.logger.trace("write: " + dataBuffer + " ready: " + ready);
			}

			if (ready) {
				int total = dataBuffer.readableByteCount();
				int written = writeDataBuffer(dataBuffer);

				if (this.logger.isTraceEnabled()) {
					this.logger.trace("written: " + written + " total: " + total);
				}
				return written == total;
			}
			else {
				return false;
			}
		}
{
		if (source == null) {
			return null;
		}
		byte[] bytes = encodeBytes(source.getBytes(charset), type);
		return new String(bytes, StandardCharsets.US_ASCII);
	}
{
		if (classLoader != null) {
			try {
				return (Class<? extends Annotation>) classLoader.loadClass(annotationType);
			}
			catch (ClassNotFoundException ex) {
				// Annotation Class not resolvable
			}
		}
		return null;
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
		for (Field field : testClass.getFields()) {
			int modifiers = field.getModifiers();
			if (!Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers) &&
					SpringMethodRule.class.isAssignableFrom(field.getType())) {
				return field;
			}
		}
		return null;
	}
{
		throw new IllegalArgumentException(
				(StringUtils.hasLength(message) ? message + " " : "") +
				"Object of class [" + (obj != null ? obj.getClass().getName() : "null") +
				"] must be an instance of " + type);
	}
{
		throw new IllegalArgumentException((StringUtils.hasLength(message) ? message + " " : "") +
				subType + " is not assignable to " + superType);
	}
{
		Throwable testResultException = testResult.getThrowable();
		if (testResultException instanceof InvocationTargetException) {
			testResultException = ((InvocationTargetException) testResultException).getCause();
		}
		return testResultException;
	}
{
		Assert.notNull(testInstance, "Test instance must not be null");
		if (logger.isTraceEnabled()) {
			logger.trace(String.format("%s(): instance [%s], method [%s]", callbackName, testInstance, testMethod));
		}
		getTestContext().updateState(testInstance, testMethod, null);
	}
{
		Assert.notNull(testInstance, "Test instance must not be null");
		if (logger.isTraceEnabled()) {
			logger.trace(String.format("%s(): instance [%s], method [%s], exception [%s]", callbackName, testInstance,
					testMethod, exception));
		}
		getTestContext().updateState(testInstance, testMethod, exception);
	}
{
		logException(ex, callbackName, testExecutionListener, testInstance, testMethod);
		ReflectionUtils.rethrowException(ex);
	}
{
		ResponseEntity<Void> entity = performPost("/publisher-create", JSON,
				asList(new Person("Robert"), new Person("Marie")), null, Void.class);

		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertEquals(2, this.wac.getBean(TestRestController.class).persons.size());
	}
{
		ResponseEntity<Void> entity = performPost("/flux-create", JSON,
				asList(new Person("Robert"), new Person("Marie")), null, Void.class);

		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertEquals(2, this.wac.getBean(TestRestController.class).persons.size());
	}
{
		ResponseEntity<Void> entity = performPost("/observable-create", JSON,
				asList(new Person("Robert"), new Person("Marie")), null, Void.class);

		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertEquals(2, this.wac.getBean(TestRestController.class).persons.size());
	}
{
		People people = new People(new Person("Robert"), new Person("Marie"));
		ResponseEntity<Void> response = performPost("/publisher-create", APPLICATION_XML, people, null, Void.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, this.wac.getBean(TestRestController.class).persons.size());
	}
{
		People people = new People(new Person("Robert"), new Person("Marie"));
		ResponseEntity<Void> response = performPost("/flux-create", APPLICATION_XML, people, null, Void.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, this.wac.getBean(TestRestController.class).persons.size());
	}
{
		People people = new People(new Person("Robert"), new Person("Marie"));
		ResponseEntity<Void> response = performPost("/observable-create", APPLICATION_XML, people, null, Void.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, this.wac.getBean(TestRestController.class).persons.size());
	}
{
		if (executable instanceof Method) {
			return new MethodParameter((Method) executable, parameterIndex);
		}
		else if (executable instanceof Constructor) {
			return new MethodParameter((Constructor<?>) executable, parameterIndex);
		}
		else {
			throw new IllegalArgumentException("Not a Method/Constructor: " + executable);
		}
	}
{
		Assert.state(this.targetConnectionFactory != null, "'targetConnectionFactory' is required");
		return this.targetConnectionFactory;
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
		MultipartHttpServletRequest unwrapped = WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
		if (unwrapped != null) {
			return unwrapped;
		}
		return new StandardMultipartHttpServletRequest(request);
	}
{
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Found key '%s' in [%s] with type [%s] and value '%s'",
					key, propertySource.getName(), value.getClass().getSimpleName(), value));
		}
	}
{
		String charset = getCharset();
		String httpMethod = this.webRequest.getHttpMethod().name();
		UriComponents uriComponents = uriComponents();

		MockHttpServletRequest request = new HtmlUnitMockHttpServletRequest(
				servletContext, httpMethod, uriComponents.getPath());
		parent(request, this.parentBuilder);
		request.setServerName(uriComponents.getHost()); // needs to be first for additional headers
		authType(request);
		request.setCharacterEncoding(charset);
		content(request, charset);
		contextPath(request, uriComponents);
		contentType(request);
		cookies(request);
		headers(request);
		locales(request);
		servletPath(uriComponents, request);
		params(request, uriComponents);
		ports(uriComponents, request);
		request.setProtocol("HTTP/1.1");
		request.setQueryString(uriComponents.getQuery());
		request.setScheme(uriComponents.getScheme());
		request.setPathInfo(null);

		return postProcess(request);
	}
{
		Method method = this.exceptionLookupCache.get(exceptionType);
		if (method == null) {
			method = getMappedMethod(exceptionType);
			this.exceptionLookupCache.put(exceptionType, method != null ? method : NO_METHOD_FOUND);
		}
		return method != NO_METHOD_FOUND ? method : null;
	}
{
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

		return objectDecoder.decode(inputStream, elementType, mimeType, hints)
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
		MediaType contentType = inputMessage.getHeaders().getContentType();
		return (contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM);
	}
{
		MethodJmsListenerEndpoint endpoint = createDefaultMethodJmsEndpoint(
				this.listener.getClass(), "handleIt", String.class, String.class);
		Message message = new StubTextMessage("foo-bar");
		message.setStringProperty("my-header", "my-value");

		invokeListener(endpoint, message);
		assertListenerMethodInvocation("handleIt");
	}
{
		Class<?> containingClass = parameter.getContainingClass();
		return (AnnotationUtils.findAnnotation(containingClass, ResponseBody.class) != null ||
				parameter.getMethodAnnotation(ResponseBody.class) != null);
	}
{
		TestBean value = new TestBean("Joe");
		ResolvableType type = ResolvableType.forClass(TestBean.class);
		HandlerResult handlerResult = new HandlerResult(new Object(), value, type, new ExtendedModelMap());

		this.request.getHeaders().setAccept(Collections.singletonList(APPLICATION_JSON));
		this.request.setUri(new URI("/account"));

		TestView defaultView = new TestView("jsonView", APPLICATION_JSON);

		createResultHandler(Collections.singletonList(defaultView), new TestViewResolver("account"))
				.handleResult(this.exchange, handlerResult)
				.block(Duration.ofSeconds(5));

		assertEquals(APPLICATION_JSON, this.response.getHeaders().getContentType());
		assertResponseBody("jsonView: {testBean=TestBean[name=Joe]}");
	}
{
		HandlerMethod hm = handlerMethod(controller, method);
		ResolvableType type = ResolvableType.forMethodParameter(hm.getReturnType());
		HandlerResult handlerResult = new HandlerResult(hm, null, type, new ExtendedModelMap());
		assertEquals(result, this.resultHandler.supports(handlerResult));
	}
{
		if (this.beanFactory instanceof ConfigurableBeanFactory) {
			this.adviceMonitor = ((ConfigurableBeanFactory) this.beanFactory).getSingletonMutex();
		}
		else {
			this.adviceMonitor = new Object();
		}
	}
{
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
		return false;
	}
{
		return this.conversionService;
	}
{
		List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

		// Annotation-based argument resolution
		ConversionService cs = getConversionService();
		resolvers.add(new RequestParamMethodArgumentResolver(cs, getBeanFactory(), false));
		resolvers.add(new RequestParamMapMethodArgumentResolver());
		resolvers.add(new PathVariableMethodArgumentResolver(cs, getBeanFactory()));
		resolvers.add(new PathVariableMapMethodArgumentResolver());
		resolvers.add(new RequestBodyArgumentResolver(getMessageConverters(), cs));
		resolvers.add(new RequestHeaderMethodArgumentResolver(cs, getBeanFactory()));
		resolvers.add(new RequestHeaderMapMethodArgumentResolver());
		resolvers.add(new CookieValueMethodArgumentResolver(cs, getBeanFactory()));
		resolvers.add(new ExpressionValueMethodArgumentResolver(cs, getBeanFactory()));
		resolvers.add(new SessionAttributeMethodArgumentResolver(cs, getBeanFactory()));
		resolvers.add(new RequestAttributeMethodArgumentResolver(cs , getBeanFactory()));

		// Type-based argument resolution
		resolvers.add(new ModelArgumentResolver());

		// Custom resolvers
		if (getCustomArgumentResolvers() != null) {
			resolvers.addAll(getCustomArgumentResolvers());
		}

		// Catch-all
		resolvers.add(new RequestParamMethodArgumentResolver(cs, getBeanFactory(), true));
		return resolvers;
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
		return new com.gargoylesoftware.htmlunit.util.Cookie(result);
	}
{
		return client.getWebConnection().getResponse(request);
	}
{
		for (Map.Entry<String, Cache> entry : this.cacheMap.entrySet()) {
			entry.setValue(createConcurrentMapCache(entry.getKey()));
		}
	}
{
		for (SourceClass ifc : sourceClass.getInterfaces()) {
			Set<MethodMetadata> beanMethods = ifc.getMetadata().getAnnotatedMethods(Bean.class.getName());
			for (MethodMetadata methodMetadata : beanMethods) {
				if (!methodMetadata.isAbstract()) {
					// A default method or other concrete method on a Java 8+ interface...
					configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
				}
			}
			processInterfaces(configClass, ifc);
		}
	}
{
		ServerHttpRequest request = new MockServerHttpRequest(HttpMethod.GET, new URI(path));
		this.response = new MockServerHttpResponse();
		WebSessionManager sessionManager = new DefaultWebSessionManager();
		return new DefaultServerWebExchange(request, this.response, sessionManager);
	}
{
		ScheduledTask scheduledTask = this.unresolvedTasks.remove(task);
		boolean newTask = false;
		if (scheduledTask == null) {
			scheduledTask = new ScheduledTask();
			newTask = true;
		}
		if (this.taskScheduler != null) {
			if (task.getInitialDelay() > 0) {
				Date startTime = new Date(System.currentTimeMillis() + task.getInitialDelay());
				scheduledTask.future =
						this.taskScheduler.scheduleAtFixedRate(task.getRunnable(), startTime, task.getInterval());
			}
			else {
				scheduledTask.future =
						this.taskScheduler.scheduleAtFixedRate(task.getRunnable(), task.getInterval());
			}
		}
		else {
			addFixedRateTask(task);
			this.unresolvedTasks.put(task, scheduledTask);
		}
		return (newTask ? scheduledTask : null);
	}
{
		ScheduledTask scheduledTask = this.unresolvedTasks.remove(task);
		boolean newTask = false;
		if (scheduledTask == null) {
			scheduledTask = new ScheduledTask();
			newTask = true;
		}
		if (this.taskScheduler != null) {
			if (task.getInitialDelay() > 0) {
				Date startTime = new Date(System.currentTimeMillis() + task.getInitialDelay());
				scheduledTask.future =
						this.taskScheduler.scheduleWithFixedDelay(task.getRunnable(), startTime, task.getInterval());
			}
			else {
				scheduledTask.future =
						this.taskScheduler.scheduleWithFixedDelay(task.getRunnable(), task.getInterval());
			}
		}
		else {
			addFixedDelayTask(task);
			this.unresolvedTasks.put(task, scheduledTask);
		}
		return (newTask ? scheduledTask : null);
	}
{
		return (this.beanFactory instanceof ConfigurableBeanFactory ?
				((ConfigurableBeanFactory) this.beanFactory).getSingletonMutex() : this);
	}
{
		return this;
	}
{
		return this;
	}
{
			return this;
		}
{
		try {
			return new URI(uriComponents.toUriString());
		}
		catch (URISyntaxException ex) {
			throw new IllegalStateException("Could not create URI object: " + ex.getMessage(), ex);
		}
	}
{
		WebHandler webHandler = this.targetHandler;
		if (!this.filters.isEmpty()) {
			WebFilter[] array = new WebFilter[this.filters.size()];
			webHandler = new FilteringWebHandler(webHandler, this.filters.toArray(array));
		}
		if (!this.exceptionHandlers.isEmpty()) {
			WebExceptionHandler[] array = new WebExceptionHandler[this.exceptionHandlers.size()];
			webHandler = new ExceptionHandlingWebHandler(webHandler,  this.exceptionHandlers.toArray(array));
		}
		HttpWebHandlerAdapter httpHandler = new HttpWebHandlerAdapter(webHandler);
		if (this.sessionManager != null) {
			httpHandler.setSessionManager(this.sessionManager);
		}
		return httpHandler;
	}
{

		List<MediaType> acceptableMediaTypes = getAcceptableMediaTypes(request);
		List<MediaType> producibleMediaTypes = getProducibleMediaTypes(elementType);

		Set<MediaType> compatibleMediaTypes = new LinkedHashSet<>();
		for (MediaType acceptableMediaType : acceptableMediaTypes) {
			compatibleMediaTypes.addAll(producibleMediaTypes.stream().
					filter(acceptableMediaType::isCompatibleWith).
					map(producibleType -> getMostSpecificMediaType(acceptableMediaType,
							producibleType)).collect(Collectors.toList()));
		}

		List<MediaType> result = new ArrayList<>(compatibleMediaTypes);
		MediaType.sortBySpecificityAndQuality(result);
		return result;
	}
{
		try {
			return URLDecoder.decode(value, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
{
		String charSet = getParameter(PARAM_CHARSET);
		return (charSet != null ? Charset.forName(unquote(charSet)) : null);
	}
{
		if (cause != null && !(cause instanceof JsonMappingException && cause.getMessage().startsWith("Can not find"))) {
			String msg = "Failed to evaluate Jackson " + (type instanceof JavaType ? "de" : "") +
					"serialization for type [" + type + "]";
			if (logger.isDebugEnabled()) {
				logger.warn(msg, cause);
			}
			else {
				logger.warn(msg + ": " + cause);
			}
		}
	}
{
		if (cause != null && !(cause instanceof JsonMappingException && cause.getMessage().startsWith("Can not find"))) {
			String msg = "Failed to evaluate Jackson " + (type instanceof JavaType ? "de" : "") +
					"serialization for type [" + type + "]";
			if (logger.isDebugEnabled()) {
				logger.warn(msg, cause);
			}
			else {
				logger.warn(msg + ": " + cause);
			}
		}
	}
{
		String headerValue = getFirst(headerName);
		if (headerValue == null) {
			// No header value sent at all
			return -1;
		}
		if (headerValue.length() >= 3) {
			// Short "0" or "-1" like values are never valid HTTP date headers...
			// Let's only bother with SimpleDateFormat parsing for long enough values.
			for (String dateFormat : DATE_FORMATS) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
				simpleDateFormat.setTimeZone(GMT);
				try {
					return simpleDateFormat.parse(headerValue).getTime();
				}
				catch (ParseException ex) {
					// ignore
				}
			}
		}
		if (rejectInvalid) {
			throw new IllegalArgumentException("Cannot parse date value \"" + headerValue +
					"\" for \"" + headerName + "\" header");
		}
		return -1;
	}
{

		Assert.notNull(environment, "environment must not be null");
		Assert.notNull(resourceLoader, "resourceLoader must not be null");
		Assert.notNull(locations, "locations must not be null");
		try {
			for (String location : locations) {
				String resolvedLocation = environment.resolveRequiredPlaceholders(location);
				Resource resource = resourceLoader.getResource(resolvedLocation);
				environment.getPropertySources().addFirst(new ResourcePropertySource(resource));
			}
		}
		catch (IOException ex) {
			throw new IllegalStateException("Failed to add PropertySource to Environment", ex);
		}
	}
{
		//noinspection unchecked
		return webResponse -> (Mono<T>) webResponse.getClientResponse()
				.flatMap(resp -> decodeResponseBody(resp, bodyType, webResponse.getMessageDecoders()))
				.next();
	}
{
		return webResponse -> webResponse.getClientResponse()
				.flatMap(resp -> decodeResponseBody(resp, bodyType, webResponse.getMessageDecoders()));
	}
{
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
		return webResponse -> webResponse.getClientResponse()
				.map(response -> new ResponseEntity<>(
						decodeResponseBody(response, type, webResponse.getMessageDecoders()),
						response.getHeaders(), response.getStatusCode()));
	}
{
		try {
			Unmarshaller unmarshaller = this.jaxbContexts.createUnmarshaller(outputClass);
			XMLEventReader eventReader = new ListBasedXMLEventReader(eventFlux);
			if (outputClass.isAnnotationPresent(XmlRootElement.class)) {
				return unmarshaller.unmarshal(eventReader);
			}
			else {
				JAXBElement<?> jaxbElement =
						unmarshaller.unmarshal(eventReader, outputClass);
				return jaxbElement.getValue();
			}
		}
		catch (JAXBException ex) {
			throw new CodecException(ex.getMessage(), ex);
		}
	}
{
		super.writeFrame(frame);
		resetRequest();
	}
{
		String[] codes = new String[] {objectName + Errors.NESTED_PATH_SEPARATOR + field, field};
		return new DefaultMessageSourceResolvable(codes, field);
	}
{
		if (composed == null) {
			return null;
		}

		try {
			final Set<String> types = new LinkedHashSet<String>();
			searchWithGetSemantics(composed.annotationType(), null, null, null, new SimpleAnnotationProcessor<Object>(true) {
					@Override
					public Object process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth) {
						types.add(annotation.annotationType().getName());
						return CONTINUE;
					}
				}, new HashSet<AnnotatedElement>(), 1);
			return (!types.isEmpty() ? types : null);
		}
		catch (Throwable ex) {
			AnnotationUtils.rethrowAnnotationConfigurationException(ex);
			throw new IllegalStateException("Failed to introspect annotations on " + element, ex);
		}
	}
{

		try {
			return searchWithGetSemantics(element, annotationType, annotationName, containerType, processor,
				new HashSet<AnnotatedElement>(), 0);
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
	}
{
		exception.expect(AnnotationConfigurationException.class);
		exception.expectMessage(startsWith("Invalid declaration of container type"));
		exception.expectMessage(containsString(ContainerMissingValueAttribute.class.getName()));
		exception.expectMessage(containsString("for repeatable annotation"));
		exception.expectMessage(containsString(InvalidRepeatable.class.getName()));
		exception.expectCause(isA(NoSuchMethodException.class));
	}
{
		exception.expect(AnnotationConfigurationException.class);
		exception.expectMessage(startsWith("Container type"));
		exception.expectMessage(containsString(ContainerWithNonArrayValueAttribute.class.getName()));
		exception.expectMessage(containsString("must declare a 'value' attribute for an array of type"));
		exception.expectMessage(containsString(InvalidRepeatable.class.getName()));
	}
{
		exception.expect(AnnotationConfigurationException.class);
		exception.expectMessage(startsWith("Container type"));
		exception.expectMessage(containsString(ContainerWithArrayValueAttributeButWrongComponentType.class.getName()));
		exception.expectMessage(containsString("must declare a 'value' attribute for an array of type"));
		exception.expectMessage(containsString(InvalidRepeatable.class.getName()));
	}
{
		String forwardedHeader = headers.getFirst("Forwarded");
		if (StringUtils.hasText(forwardedHeader)) {
			String forwardedToUse = StringUtils.commaDelimitedListToStringArray(forwardedHeader)[0];
			Matcher matcher = FORWARDED_HOST_PATTERN.matcher(forwardedToUse);
			if (matcher.find()) {
				host(matcher.group(1).trim());
			}
			matcher = FORWARDED_PROTO_PATTERN.matcher(forwardedToUse);
			if (matcher.find()) {
				scheme(matcher.group(1).trim());
			}
		}
		else {
			String hostHeader = headers.getFirst("X-Forwarded-Host");
			if (StringUtils.hasText(hostHeader)) {
				String[] hosts = StringUtils.commaDelimitedListToStringArray(hostHeader);
				String hostToUse = hosts[0];
				if (hostToUse.contains(":")) {
					String[] hostAndPort = StringUtils.split(hostToUse, ":");
					host(hostAndPort[0]);
					port(Integer.parseInt(hostAndPort[1]));
				}
				else {
					host(hostToUse);
					port(null);
				}
			}

			String portHeader = headers.getFirst("X-Forwarded-Port");
			if (StringUtils.hasText(portHeader)) {
				String[] ports = StringUtils.commaDelimitedListToStringArray(portHeader);
				port(Integer.parseInt(ports[0]));
			}

			String protocolHeader = headers.getFirst("X-Forwarded-Proto");
			if (StringUtils.hasText(protocolHeader)) {
				String[] protocols = StringUtils.commaDelimitedListToStringArray(protocolHeader);
				scheme(protocols[0]);
			}
		}

		if ((this.scheme.equals("http") && "80".equals(this.port)) ||
				(this.scheme.equals("https") && "443".equals(this.port))) {
			this.port = null;
		}

		return this;
	}
{
		return new UriComponentsBuilder(this);
	}
{

		Set<A> annotations = new LinkedHashSet<A>();
		for (AnnotationAttributes attributes : aggregatedResults) {
			AnnotationUtils.postProcessAnnotationAttributes(element, attributes, false, false);
			annotations.add(AnnotationUtils.synthesizeAnnotation(attributes, annotationType, element));
		}
		return annotations;
	}
{
		Class<? extends Annotation> containerType = AnnotationUtils.resolveContainerAnnotationType(annotationType);
		if (containerType == null) {
			throw new IllegalArgumentException(
				"annotationType must be a repeatable annotation: failed to resolve container type for "
						+ annotationType.getName());
		}
		return containerType;
	}
{

		if (containerType != null && !processor.aggregates()) {
			throw new IllegalArgumentException(
				"Searches for repeatable annotations must supply an aggregating Processor");
		}

		try {
			return searchWithFindSemantics(
					element, annotationType, annotationName, containerType, processor, new HashSet<AnnotatedElement>(), 0);
		}
		catch (Throwable ex) {
			AnnotationUtils.rethrowAnnotationConfigurationException(ex);
			throw new IllegalStateException("Failed to introspect annotations on " + element, ex);
		}
	}
{
		super(beanName, targetClass, method);
		this.annotation = AnnotatedElementUtils.findMergedAnnotation(method, TransactionalEventListener.class);
		if (this.annotation == null) {
			throw new IllegalStateException("No TransactionalEventListener annotation found on '" + method + "'");
		}
	}
{
		try {
			InputStream in = resource.getInputStream();
			try {
				StreamUtils.copy(in, outputMessage.getBody());
			}
			catch (NullPointerException ex) {
				// ignore, see SPR-13620
			}
			finally {
				try {
					in.close();
				}
				catch (Throwable ex) {
					// ignore, see SPR-12999
				}
			}
		}
		catch (FileNotFoundException ex) {
			// ignore, see SPR-12999
		}
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

		// Check the media type for the resource
		MediaType mediaType = getMediaType(request, resource);
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

		ServletServerHttpResponse outputMessage = new ServletServerHttpResponse(response);
		if (request.getHeader(HttpHeaders.RANGE) == null) {
			setHeaders(response, resource, mediaType);
			this.resourceHttpMessageConverter.write(resource, mediaType, outputMessage);
		}
		else {
			ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(request);
			try {
				List<HttpRange> httpRanges = inputMessage.getHeaders().getRange();
				HttpRangeResource rangeResource = new HttpRangeResource(httpRanges, resource);
				response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
				this.resourceHttpMessageConverter.write(rangeResource, mediaType, outputMessage);
			}
			catch (IllegalArgumentException ex) {
				Long contentLength = resource.contentLength();
				if (contentLength != null) {
					response.addHeader("Content-Range", "bytes */" + resource.contentLength());
				}
				response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
			}
		}
	}
{
		given(messageConverter.canWrite(String.class, null)).willReturn(true);
		given(messageConverter.getSupportedMediaTypes()).willReturn(Collections.singletonList(MediaType.TEXT_PLAIN));
		given(messageConverter.canWrite(String.class, accepted)).willReturn(true);
	}
{
			Object value = AnnotationUtils.getValue(annotation, sourceAttributeName);
			return AnnotationUtils.adaptValue(element, value, this.classValuesAsString, this.nestedAnnotationsAsMap);
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
		boolean addDecoratingProxy = (decoratingProxy && !advised.isInterfaceProxied(DecoratingProxy.class));
		int nonUserIfcCount = 0;
		if (addSpringProxy) {
			nonUserIfcCount++;
		}
		if (addAdvised) {
			nonUserIfcCount++;
		}
		if (addDecoratingProxy) {
			nonUserIfcCount++;
		}
		Class<?>[] proxiedInterfaces = new Class<?>[specifiedInterfaces.length + nonUserIfcCount];
		System.arraycopy(specifiedInterfaces, 0, proxiedInterfaces, 0, specifiedInterfaces.length);
		int index = specifiedInterfaces.length;
		if (addSpringProxy) {
			proxiedInterfaces[index] = SpringProxy.class;
			index++;
		}
		if (addAdvised) {
			proxiedInterfaces[index] = Advised.class;
			index++;
		}
		if (addDecoratingProxy) {
			proxiedInterfaces[index] = DecoratingProxy.class;
		}
		return proxiedInterfaces;
	}
{
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
		return number.longValue();
	}
{
		MockMvcWebConnection mockMvcWebConnection = new MockMvcWebConnection(this.mockMvc, webClient, this.contextPath);

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
		// Set reflectively
		setField(person, "id", new Long(99), long.class);
		setField(person, "name", "Tom");
		setField(person, "age", new Integer(42));
		setField(person, "eyeColor", "blue", String.class);
		setField(person, "likesPets", Boolean.TRUE);
		setField(person, "favoriteNumber", PI, Number.class);

		// Get reflectively
		assertEquals(new Long(99), getField(person, "id"));
		assertEquals("Tom", getField(person, "name"));
		assertEquals(new Integer(42), getField(person, "age"));
		assertEquals("blue", getField(person, "eyeColor"));
		assertEquals(Boolean.TRUE, getField(person, "likesPets"));
		assertEquals(PI, getField(person, "favoriteNumber"));

		// Get directly
		assertEquals("ID (private field in a superclass)", 99, person.getId());
		assertEquals("name (protected field)", "Tom", person.getName());
		assertEquals("age (private field)", 42, person.getAge());
		assertEquals("eye color (package private field)", "blue", person.getEyeColor());
		assertEquals("'likes pets' flag (package private boolean field)", true, person.likesPets());
		assertEquals("'favorite number' (package field)", PI, person.getFavoriteNumber());
	}
{
		ObjectName oname = ObjectNameManager.getInstance(objectName);
		assertNotNull(server.getObjectInstance(oname));
		String name = (String) server.getAttribute(oname, "Name");
		assertEquals("Invalid name returned", expected, name);
	}
{
		if (beanFactory == null) {
			throw new IllegalStateException("BeanFactory must be set on " + getClass().getSimpleName() +
					" to access qualified executor '" + qualifier + "'");
		}
		return BeanFactoryAnnotationUtils.qualifiedBeanOfType(beanFactory, Executor.class, qualifier);
	}
{

		List<ContextConfigurationAttributes> defaultConfigAttributesList
			= Collections.singletonList(new ContextConfigurationAttributes(testClass));

		ContextLoader contextLoader = resolveContextLoader(testClass, defaultConfigAttributesList);
		if (logger.isInfoEnabled()) {
			logger.info(String.format(
				"Neither @ContextConfiguration nor @ContextHierarchy found for test class [%s], using %s",
				testClass.getName(), contextLoader.getClass().getSimpleName()));
		}
		return buildMergedContextConfiguration(testClass, defaultConfigAttributesList, null,
			cacheAwareContextLoaderDelegate, false);
	}
{
		MultiValueMap<String, Object> attributesMultiMap = AnnotatedElementUtils.getAllAnnotationAttributes(
				testClass, BootstrapWith.class.getName());
		List<Object> values = (attributesMultiMap == null ? null : attributesMultiMap.get(AnnotationUtils.VALUE));
		if (values == null) {
			return null;
		}
		Assert.state(values.size() == 1, String.format("Configuration error: found multiple declarations of "
				+ "@BootstrapWith on test class [%s] with values %s", testClass.getName(), values));
		return (Class<?>) values.get(0);
	}
{
		AnnotationAttributes attributes = findMergedAnnotationAttributes(element, ComponentScan.class);

		assertNotNull("Should find @ComponentScan on " + element, attributes);
		assertArrayEquals("value: ", expected, attributes.getStringArray("value"));
		assertArrayEquals("basePackages: ", expected, attributes.getStringArray("basePackages"));

		return attributes;
	}
{

		// For backwards compatibility
		MediaType mediaType = getMediaType(resource);
		if (mediaType != null) {
			return mediaType;
		}

		Class<PathExtensionContentNegotiationStrategy> clazz = PathExtensionContentNegotiationStrategy.class;
		PathExtensionContentNegotiationStrategy strategy = this.contentNegotiationManager.getStrategy(clazz);
		if (strategy != null) {
			mediaType = strategy.getMediaTypeForResource(resource);
		}

		if (mediaType == null) {
			ServletWebRequest webRequest = new ServletWebRequest(request);
			try {
				getContentNegotiationManager().resolveMediaTypes(webRequest);
			}
			catch (HttpMediaTypeNotAcceptableException ex) {
				// Ignore
			}
		}

		return mediaType;
	}
{
			Map<String, List<String>> headers = new LinkedHashMap<String, List<String>>();
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String name = headerNames.nextElement();
				headers.put(name, Collections.list(request.getHeaders(name)));
			}
			for (String name : FORWARDED_HEADER_NAMES) {
				headers.remove(name);
			}
			return headers;
		}
{
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
			inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
			inputFactory.setXMLResolver(NO_OP_XML_RESOLVER);
			return inputFactory;
		}
{
		long dateValue = -1;
		try {
			dateValue = getRequest().getDateHeader(headerName);
		}
		catch (IllegalArgumentException ex) {
			String headerValue = getRequest().getHeader(headerName);
			// Possibly an IE 10 style value: "Wed, 09 Apr 2014 09:57:42 GMT; length=13774"
			int separatorIndex = headerValue.indexOf(';');
			if (separatorIndex != -1) {
				String datePart = headerValue.substring(0, separatorIndex);
				try {
					dateValue = Date.parse(datePart);
				}
				catch (IllegalArgumentException ex2) {
					// Giving up
				}
			}
		}
		return dateValue;
	}
{

		Class<?> clazz = ComposedAnnotationController.class;
		Method method = clazz.getMethod(methodName);
		RequestMappingInfo info = this.handlerMapping.getMappingForMethod(method, clazz);

		assertNotNull(info);

		Set<String> paths = info.getPatternsCondition().getPatterns();
		assertEquals(1, paths.size());
		assertEquals(path, paths.iterator().next());

		Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
		assertEquals(1, methods.size());
		assertEquals(requestMethod, methods.iterator().next());

		return info;
	}
{
		RequestExpectation expectation = this.remainingExpectations.findExpectation(request);
		if (expectation != null) {
			ClientHttpResponse response = expectation.createResponse(request);
			this.remainingExpectations.update(expectation);
			return response;
		}
		throw createUnexpectedRequestError(request);
	}
{
		Assert.notNull(restGateway, "'gatewaySupport' must not be null");
		return new DefaultBuilder(restGateway.getRestTemplate());
	}
{

			if (this.requestIterator == null) {
				this.requestIterator = MockRestServiceServer.this.responseActions.iterator();
			}
			if (!this.requestIterator.hasNext()) {
				throw new AssertionError("No further requests expected: HTTP " +
						request.getMethod() + " " + request.getURI());
			}

			DefaultResponseActions responseActions = this.requestIterator.next();
			responseActions.match(request);

			return responseActions.createResponse(request);
		}
{
		RequestMethod requestMethod = getRequestMethod(httpMethod);
		if (requestMethod != null) {
			for (RequestMethod method : getMethods()) {
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
		String url = "http://url.somewhere.com";
		Map<String, String> model = Collections.singletonMap("foo", "bar");

		TestRedirectView rv = new TestRedirectView(url, false, model);
		rv.setExposeModelAttributes(false);
		rv.render(model, request, response);

		assertEquals(url, this.response.getRedirectedUrl());
	}
{

		TestRedirectView rv = new TestRedirectView(url, contextRelative, map);
		rv.render(map, request, response);

		assertTrue("queryProperties() should have been called.", rv.queryPropertiesCalled);
		assertEquals(expectedUrl, this.response.getRedirectedUrl());
	}
{
		Byte[] bytesObjects = new Byte[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			bytesObjects[i] = bytes[i];
		}
		return bytesObjects;
	}
{
		return methodParam.getParameterAnnotation(RequestBody.class).required();
	}
{
		if (this.headersWritten) {
			return;
		}
		this.headersWritten = true;
		this.writtenHeaders.putAll(this.headers);
	}
{
		assertNotNull(bean);
		Object value = new DirectFieldAccessor(bean).getPropertyValue("customArgumentResolvers");
		assertNotNull(value);
		assertTrue(value instanceof List);
		@SuppressWarnings("unchecked")
		List<HandlerMethodArgumentResolver> resolvers = (List<HandlerMethodArgumentResolver>) value;
		assertEquals(3, resolvers.size());
		assertTrue(resolvers.get(0) instanceof ServletWebArgumentResolverAdapter);
		assertTrue(resolvers.get(1) instanceof TestHandlerMethodArgumentResolver);
		assertTrue(resolvers.get(2) instanceof TestHandlerMethodArgumentResolver);
		assertNotSame(resolvers.get(1), resolvers.get(2));
	}
{
		assertNotNull(bean);
		Object value = new DirectFieldAccessor(bean).getPropertyValue("customReturnValueHandlers");
		assertNotNull(value);
		assertTrue(value instanceof List);
		@SuppressWarnings("unchecked")
		List<HandlerMethodReturnValueHandler> handlers = (List<HandlerMethodReturnValueHandler>) value;
		assertEquals(2, handlers.size());
		assertEquals(TestHandlerMethodReturnValueHandler.class, handlers.get(0).getClass());
		assertEquals(TestHandlerMethodReturnValueHandler.class, handlers.get(1).getClass());
		assertNotSame(handlers.get(0), handlers.get(1));
	}
{

		Class<?> type = descriptor.getDependencyType();
		if (type.isArray()) {
			Class<?> componentType = type.getComponentType();
			DependencyDescriptor targetDesc = new DependencyDescriptor(descriptor);
			targetDesc.increaseNestingLevel();
			Map<String, Object> matchingBeans = findAutowireCandidates(beanName, componentType, targetDesc);
			if (matchingBeans.isEmpty()) {
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
				return null;
			}
			DependencyDescriptor targetDesc = new DependencyDescriptor(descriptor);
			targetDesc.increaseNestingLevel();
			Map<String, Object> matchingBeans = findAutowireCandidates(beanName, elementType, targetDesc);
			if (matchingBeans.isEmpty()) {
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
				return null;
			}
			Class<?> valueType = descriptor.getMapValueType();
			if (valueType == null) {
				return null;
			}
			DependencyDescriptor targetDesc = new DependencyDescriptor(descriptor);
			targetDesc.increaseNestingLevel();
			Map<String, Object> matchingBeans = findAutowireCandidates(beanName, valueType, targetDesc);
			if (matchingBeans.isEmpty()) {
				return null;
			}
			if (autowiredBeanNames != null) {
				autowiredBeanNames.addAll(matchingBeans.keySet());
			}
			return matchingBeans;
		}
		else {
			return NOT_MULTIPLE_BEANS;
		}
	}
{
		try {
			for (Entry<String, List<String>> entry : map.entrySet()) {
				for (String value : entry.getValue()) {
					value = (value != null) ? UriUtils.decode(value, "UTF-8") : null;
					request.addParameter(UriUtils.decode(entry.getKey(), "UTF-8"), value);
				}
			}
		}
		catch (UnsupportedEncodingException ex) {
			// shouldn't happen
		}
	}
{
		SendTo ann = AnnotationUtils.getAnnotation(specificMethod, SendTo.class);
		if (ann != null) {
			return ann;
		}
		else {
			return AnnotationUtils.getAnnotation(specificMethod.getDeclaringClass(), SendTo.class);
		}
	}
{
		SendTo sendTo = returnType.getMethodAnnotation(SendTo.class);
		if (sendTo != null && !ObjectUtils.isEmpty((sendTo.value()))) {
			return sendTo;
		}
		else {
			return AnnotationUtils.getAnnotation(returnType.getDeclaringClass(), SendTo.class);
		}
	}
{
		SimpMessageHeaderAccessor accessor = getCapturedAccessor(index);
		assertEquals(sessionId, accessor.getSessionId());
		assertEquals(destination, accessor.getDestination());
		assertEquals(MIME_TYPE, accessor.getContentType());
		assertNull("Subscription id should not be copied", accessor.getSubscriptionId());
		assertEquals(methodParameter, accessor.getHeader(SimpMessagingTemplate.CONVERSION_HINT_HEADER));
	}
{

		StringWriter writer = new StringWriter();
		objectWriter.writeValue(writer, object);
		return session.createTextMessage(writer.toString());
	}
{

		ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
		OutputStreamWriter writer = new OutputStreamWriter(bos, this.encoding);
		objectWriter.writeValue(writer, object);

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
		try {
			callback.onSuccess((T) this.result);
		}
		catch (Throwable ex) {
			// Ignore
		}
	}
{
		try {
			callback.onFailure((Throwable) this.result);
		}
		catch (Throwable ex) {
			// Ignore
		}
	}
{
		if (ObjectUtils.isEmpty(getMethodParameters())) {
			return NO_ARGS;
		}
		try {
			List<Mono<Object>> monos = Stream.of(getMethodParameters())
					.map(param -> {
						param.initParameterNameDiscovery(this.parameterNameDiscoverer);
						GenericTypeResolver.resolveParameterType(param, getBean().getClass());
						if (!ObjectUtils.isEmpty(providedArgs)) {
							for (Object providedArg : providedArgs) {
								if (param.getParameterType().isInstance(providedArg)) {
									return Mono.just(providedArg);
								}
							}
						}
						HandlerMethodArgumentResolver resolver = this.resolvers.stream()
								.filter(r -> r.supportsParameter(param))
								.findFirst()
								.orElseThrow(() -> getArgError("No resolver for ", param, null));
						try {
							return resolver.resolveArgument(param, request)
									.defaultIfEmpty(NO_VALUE)
									.otherwise(ex -> Mono.error(getArgError("Error resolving ", param, ex)));
						}
						catch (Exception ex) {
							throw getArgError("Error resolving ", param, ex);
						}
					})
					.collect(Collectors.toList());

			return Mono.when(monos).map(args ->
					Stream.of(args.toArray()).map(o -> o != NO_VALUE ? o : null).toArray());
		}
		catch (Throwable ex) {
			return Mono.error(ex);
		}
	}
{
		return this.request;
	}
{
		return this.exchange;
	}
{
		while (!future.isDone()) {
		}
	}
{

		if (HAS_DO_UPGRADE) {
			HttpServletRequest servletRequest = getHttpServletRequest(request);
			HttpServletResponse servletResponse = getHttpServletResponse(response);

			StringBuffer requestUrl = servletRequest.getRequestURL();
			String path = servletRequest.getRequestURI();  // shouldn't matter
			Map<String, String> pathParams = Collections.<String, String>emptyMap();

			ServerEndpointRegistration endpointConfig = new ServerEndpointRegistration(path, endpoint);
			endpointConfig.setSubprotocols(Collections.singletonList(selectedProtocol));
			endpointConfig.setExtensions(selectedExtensions);

			try {
				getContainer(servletRequest).doUpgrade(servletRequest, servletResponse,
						endpointConfig, pathParams);
			}
			catch (ServletException ex) {
				throw new HandshakeFailureException(
						"Servlet request failed to upgrade to WebSocket: " + requestUrl, ex);
			}
			catch (IOException ex) {
				throw new HandshakeFailureException(
						"Response update failed during upgrade to WebSocket: " + requestUrl, ex);
			}
		}
		else {
			FALLBACK_STRATEGY.upgradeInternal(request, response, selectedProtocol,
					selectedExtensions, endpoint);
		}
	}
{
		// Check name as-is
		if (containsKey(name)) {
			return name;
		}
		// Check name with just dots replaced
		String noDotName = name.replace('.', '_');
		if (!name.equals(noDotName) && containsKey(noDotName)) {
			return noDotName;
		}
		// Check name with just hyphens replaced
		String noHyphenName = name.replace('-', '_');
		if (!name.equals(noHyphenName) && containsKey(noHyphenName)) {
			return noHyphenName;
		}
		// Check name with dots and hyphens replaced
		String noDotNoHyphenName = noDotName.replace('-', '_');
		if (!noDotName.equals(noDotNoHyphenName) && containsKey(noDotNoHyphenName)) {
			return noDotNoHyphenName;
		}
		// Give up
		return null;
	}
{
		AnnotatedClass target = new AnnotatedClass();
		Method method = ReflectionUtils.findMethod(AnnotatedClass.class, "multipleCaching", Object.class,
				Object.class);
		Object[] args = new Object[] { new Object(), new Object() };
		Collection<ConcurrentMapCache> caches = Collections.singleton(new ConcurrentMapCache("test"));
		return eval.createEvaluationContext(caches, method, args, target, target.getClass(), result, beanFactory);
	}
{
		Assert.notNull(prefix, "No prefix given");
		Assert.notNull(namespaceUri, "No namespaceUri given");
		if (XMLConstants.DEFAULT_NS_PREFIX.equals(prefix)) {
			this.defaultNamespaceUri = namespaceUri;
		}
		else {
			this.prefixToNamespaceUri.put(prefix, namespaceUri);
			Set<String> prefixes = this.namespaceUriToPrefixes.get(namespaceUri);
			if (prefixes == null) {
				prefixes = new LinkedHashSet<String>();
				this.namespaceUriToPrefixes.put(namespaceUri, prefixes);
			}
			prefixes.add(prefix);
		}
	}
{
		EntityManagerFactory emf = createNativeEntityManagerFactory();
		if (emf == null) {
			throw new IllegalStateException(
					"JPA PersistenceProvider returned null EntityManagerFactory - check your JPA provider setup!");
		}
		if (this.jpaVendorAdapter != null) {
			this.jpaVendorAdapter.postProcessEntityManagerFactory(emf);
		}
		if (logger.isInfoEnabled()) {
			logger.info("Initialized JPA EntityManagerFactory for persistence unit '" + getPersistenceUnitName() + "'");
		}
		System.out.println("Done: " + System.currentTimeMillis());
		return emf;
	}
{
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
		dateFormat.setTimeZone(GMT);
		return dateFormat.format(new Date(date));
	}
{

		Object value = result.getValue();
		if (value == null) {
			return Publishers.empty();
		}

		HandlerMethod hm = (HandlerMethod) result.getHandler();
		ResolvableType returnType = ResolvableType.forMethodParameter(hm.getReturnValueType(value));

		List<MediaType> requestedMediaTypes = getAcceptableMediaTypes(request);
		List<MediaType> producibleMediaTypes = getProducibleMediaTypes(returnType);

		if (producibleMediaTypes.isEmpty()) {
			Publishers.error(new IllegalArgumentException(
					"No encoder found for return value of type: " + returnType));
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
			return Publishers.error(new HttpMediaTypeNotAcceptableException(producibleMediaTypes));
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
			Publisher<?> publisher;
			ResolvableType elementType;
			if (this.conversionService.canConvert(returnType.getRawClass(), Publisher.class)) {
				publisher = this.conversionService.convert(value, Publisher.class);
				elementType = returnType.getGeneric(0);
			}
			else {
				publisher = Publishers.just(value);
				elementType = returnType;
			}
			Encoder<?> encoder = resolveEncoder(elementType, selectedMediaType);
			if (encoder != null) {
				response.getHeaders().setContentType(selectedMediaType);
				return response.setBody(encoder.encode((Publisher) publisher, elementType, selectedMediaType));
			}
		}

		return Publishers.error(new HttpMediaTypeNotAcceptableException(this.allSupportedMediaTypes));
	}
{
		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.copyFrom(this);
		proxyFactory.setTarget(bean);
		return proxyFactory;
	}
{
		Map<Method, MessageExceptionHandler> methods = MethodIntrospector.selectMethods(handlerType,
				new MethodIntrospector.MetadataLookup<MessageExceptionHandler>() {
					@Override
					public MessageExceptionHandler inspect(Method method) {
						return AnnotationUtils.findAnnotation(method, MessageExceptionHandler.class);
					}
				});

		Map<Class<? extends Throwable>, Method> result = new HashMap<Class<? extends Throwable>, Method>();
		for (Map.Entry<Method, MessageExceptionHandler> entry : methods.entrySet()) {
			Method method = entry.getKey();
			List<Class<? extends Throwable>> exceptionTypes = new ArrayList<Class<? extends Throwable>>();
			exceptionTypes.addAll(Arrays.asList(entry.getValue().value()));
			if (exceptionTypes.isEmpty()) {
				exceptionTypes.addAll(getExceptionsFromMethodSignature(method));
			}
			for (Class<? extends Throwable> exceptionType : exceptionTypes) {
				Method oldMethod = result.put(exceptionType, method);
				if (oldMethod != null && !oldMethod.equals(method)) {
					throw new IllegalStateException("Ambiguous @ExceptionHandler method mapped for [" +
							exceptionType + "]: {" + oldMethod + ", " + method + "}");
				}
			}
		}
		return result;
	}
{

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
		List<MediaType> mediaTypes = null;
		try {
			mediaTypes = this.pathStrategy.resolveMediaTypeKey(null, extension);
		}
		catch (HttpMediaTypeNotAcceptableException e) {
			// Ignore
		}
		if (CollectionUtils.isEmpty(mediaTypes)) {
			return false;
		}
		for (MediaType mediaType : mediaTypes) {
			if (!safeMediaType(mediaType)) {
				return false;
			}
		}
		return true;
	}
{
		assertTrue(mavContainer.isRequestHandled());
		assertEquals(HttpStatus.NOT_MODIFIED.value(), servletResponse.getStatus());
		assertEquals(0, servletResponse.getContentAsByteArray().length);
	}
{
		assertTrue(mavContainer.isRequestHandled());
		assertEquals(HttpStatus.OK.value(), servletResponse.getStatus());
		ArgumentCaptor<HttpOutputMessage> outputMessage = ArgumentCaptor.forClass(HttpOutputMessage.class);
		verify(messageConverter).write(eq("body"), eq(MediaType.TEXT_PLAIN), outputMessage.capture());
	}
{
		if (this == other) {
			return true;
		}
		if (!(other instanceof ResolvableType)) {
			return false;
		}

		ResolvableType otherType = (ResolvableType) other;
		if (!ObjectUtils.nullSafeEquals(this.type, otherType.type)) {
			return false;
		}
		if (this.typeProvider != otherType.typeProvider &&
				(this.typeProvider == null || otherType.typeProvider == null ||
				!ObjectUtils.nullSafeEquals(this.typeProvider.getSource(), otherType.typeProvider.getSource()))) {
			return false;
		}
		if (this.variableResolver != otherType.variableResolver &&
				(this.variableResolver == null || otherType.variableResolver == null ||
				!ObjectUtils.nullSafeEquals(this.variableResolver.getSource(), otherType.variableResolver.getSource()))) {
			return false;
		}
		if (!ObjectUtils.nullSafeEquals(this.componentType, otherType.componentType)) {
			return false;
		}
		return true;
	}
{
		int hashCode = ObjectUtils.nullSafeHashCode(this.type);
		if (this.typeProvider != null) {
			hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode(this.typeProvider.getSource());
		}
		if (this.variableResolver != null) {
			hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode(this.variableResolver.getSource());
		}
		if (this.componentType != null) {
			hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode(this.componentType);
		}
		return hashCode;
	}
{
		if (tyrusEndpoint != null) {
			try {
				getEndpointHelper().unregister(engine, tyrusEndpoint);
			}
			catch (Throwable ex) {
				// ignore
			}
		}
	}
{
		JSONAssert.assertEquals(expected, actual, strict);
	}
{
		JSONAssert.assertNotEquals(expected, actual, strict);
	}
{
			if(BackpressureUtils.getAndSub(REQUESTED, this, 1L) > 0) {
				Buffer buffer = new Buffer();
				buffer.append(prev);
				if (count > 1) {
					buffer.append("]");
				}
				buffer.flip();
				subscriber.onNext(buffer.byteBuffer());
				super.doComplete();
			}
		}
{
		JsonpPollingTransportHandler transportHandler = new JsonpPollingTransportHandler();
		transportHandler.initialize(this.sockJsConfig);
		PollingSockJsSession session = transportHandler.createSession("1", this.webSocketHandler, null);

		resetRequestAndResponse();
		setRequest("POST", "/");

		if (callbackValue != null) {
			this.servletRequest.setQueryString("c=" + callbackValue);
			this.servletRequest.addParameter("c", callbackValue);
		}

		try {
			transportHandler.handleRequest(this.request, this.response, this.webSocketHandler, session);
		}
		catch (SockJsTransportFailureException ex) {
			if (expectSuccess) {
				throw new AssertionError("Unexpected transport failure", ex);
			}
		}

		if (expectSuccess) {
			assertEquals(200, this.servletResponse.getStatus());
			assertEquals("application/javascript;charset=UTF-8", this.response.getHeaders().getContentType().toString());
			verify(this.webSocketHandler).afterConnectionEstablished(session);
		}
		else {
			assertEquals(500, this.servletResponse.getStatus());
			verifyNoMoreInteractions(this.webSocketHandler);
		}
	}
{
		if (getApplicationContext() == null) {
			if (this.logger.isInfoEnabled()) {
				this.logger.info("ApplicationContext has not been configured for test [" + getClass().getName()
						+ "]: dependency injection will NOT be performed.");
			}
		}
		else {
			Assert.state(getApplicationContext() != null,
					"injectDependencies() called without first configuring an ApplicationContext");

			getApplicationContext().getBeanFactory().autowireBeanProperties(this, getAutowireMode(), isDependencyCheck());
		}
	}
{
		RestTemplate restTemplate = new RestTemplate();

		URI url = new URI(requestUrl);
		RequestEntity<Void> request = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
		ResponseEntity<Person> response = restTemplate.exchange(request, Person.class);

		assertEquals(new Person("Robert"), response.getBody());
	}
{
		RestTemplate restTemplate = new RestTemplate();

		URI url = new URI(requestUrl);
		RequestEntity<Void> request = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
		List<Person> results = restTemplate.exchange(request, new ParameterizedTypeReference<List<Person>>(){}).getBody();

		assertEquals(2, results.size());
		assertEquals(new Person("Robert"), results.get(0));
		assertEquals(new Person("Marie"), results.get(1));
	}
{
		Collection<? extends Cache> caches = loadCaches();

		synchronized (this.cacheMap) {
			this.cacheNames = Collections.emptySet();
			this.cacheMap.clear();
			Set<String> cacheNames = new LinkedHashSet<String>(caches.size());
			for (Cache cache : caches) {
				String name = cache.getName();
				this.cacheMap.put(name, decorateCache(cache));
				cacheNames.add(name);
			}
			this.cacheNames = Collections.unmodifiableSet(cacheNames);
		}
	}
{
		try {
			if (this.scriptEngine == null) {
				this.scriptEngine = retrieveScriptEngine(scriptSource);
				if (this.scriptEngine == null) {
					throw new IllegalStateException("Could not determine script engine for " + scriptSource);
				}
			}
			return this.scriptEngine.eval(scriptSource.getScriptAsString());
		}
		catch (Exception ex) {
			throw new ScriptCompilationException(scriptSource, ex);
		}
	}
{
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
		return (synthesize ? synthesizeAnnotation(result, clazz) : result);
	}
{
		if (contentDisposition == null) {
			return null;
		}
		int startIndex = contentDisposition.indexOf(key);
		if (startIndex == -1) {
			return null;
		}
		String filename = contentDisposition.substring(startIndex + key.length());
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
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(getTarget(),
				getObjectName(), isAutoGrowNestedPaths(), getAutoGrowCollectionLimit());
		if (this.conversionService != null) {
			result.initConversion(this.conversionService);
		}
		return result;
	}
{
		DirectFieldBindingResult result = new DirectFieldBindingResult(getTarget(),
				getObjectName(), isAutoGrowNestedPaths());
		if (this.conversionService != null) {
			result.initConversion(this.conversionService);
		}
		return result;
	}
{
		new JsonPathRequestMatchers("$.arr").isArray().match(request);
	}
{
		return Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
	}
{
		Object value = evaluateJsonPath(content);
		String reason = "No value for JSON path \"" + this.expression + "\"";
		assertTrue(reason, value != null);
		if (List.class.isInstance(value)) {
			assertTrue(reason, !((List<?>) value).isEmpty());
		}
		return value;
	}
{

		ServletWebRequest webRequest = new ServletWebRequest(request, response);

		WebDataBinderFactory binderFactory = getDataBinderFactory(handlerMethod);
		ModelFactory modelFactory = getModelFactory(handlerMethod, binderFactory);

		ServletInvocableHandlerMethod invocableMethod = createInvocableHandlerMethod(handlerMethod);
		invocableMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
		invocableMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
		invocableMethod.setDataBinderFactory(binderFactory);
		invocableMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);

		ModelAndViewContainer mavContainer = new ModelAndViewContainer();
		mavContainer.addAllAttributes(RequestContextUtils.getInputFlashMap(request));
		modelFactory.initModel(webRequest, mavContainer, invocableMethod);
		mavContainer.setIgnoreDefaultModelOnRedirect(this.ignoreDefaultModelOnRedirect);

		AsyncWebRequest asyncWebRequest = WebAsyncUtils.createAsyncWebRequest(request, response);
		asyncWebRequest.setTimeout(this.asyncRequestTimeout);

		WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
		asyncManager.setTaskExecutor(this.taskExecutor);
		asyncManager.setAsyncWebRequest(asyncWebRequest);
		asyncManager.registerCallableInterceptors(this.callableInterceptors);
		asyncManager.registerDeferredResultInterceptors(this.deferredResultInterceptors);

		if (asyncManager.hasConcurrentResult()) {
			Object result = asyncManager.getConcurrentResult();
			mavContainer = (ModelAndViewContainer) asyncManager.getConcurrentResultContext()[0];
			asyncManager.clearConcurrentResult();
			if (logger.isDebugEnabled()) {
				logger.debug("Found concurrent result value [" + result + "]");
			}
			invocableMethod = invocableMethod.wrapConcurrentResult(result);
		}

		invocableMethod.invokeAndHandle(webRequest, mavContainer);
		if (asyncManager.isConcurrentHandlingStarted()) {
			return null;
		}

		return getModelAndView(mavContainer, modelFactory, webRequest);
	}
{
		if (!canConvertFrom(message, targetClass)) {
			return null;
		}
		return convertFromInternal(message, targetClass, conversionHint);
	}
{
		if (!canConvertTo(payload, headers)) {
			return null;
		}

		payload = convertToInternal(payload, headers, conversionHint);
		if (payload == null) {
			return null;
		}

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
		Class<?>[] classes = annotation.value();
		if (classes.length != 1) {
			throw new IllegalArgumentException(
					"@JsonView only supported for handler methods with exactly 1 class argument: " + conversionHint);
		}
		return classes[0];
	}
{
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		assertThat(requestAttributes, instanceOf(ServletRequestAttributes.class));
		assertRequestAttributes(((ServletRequestAttributes) requestAttributes).getRequest(), withinMockMvc);
	}
{
		if (withinMockMvc) {
			assertThat(request.getAttribute(FROM_TCF_MOCK), is(nullValue()));
			assertThat(request.getAttribute(FROM_MVC_TEST_DEFAULT), is(FROM_MVC_TEST_DEFAULT));
			assertThat(request.getAttribute(FROM_MVC_TEST_MOCK), is(FROM_MVC_TEST_MOCK));
			assertThat(request.getAttribute(FROM_REQUEST_FILTER), is(FROM_REQUEST_FILTER));
			assertThat(request.getAttribute(FROM_REQUEST_ATTRIBUTES_FILTER), is(FROM_REQUEST_ATTRIBUTES_FILTER));
		}
		else {
			assertThat(request.getAttribute(FROM_TCF_MOCK), is(FROM_TCF_MOCK));
			assertThat(request.getAttribute(FROM_MVC_TEST_DEFAULT), is(nullValue()));
			assertThat(request.getAttribute(FROM_MVC_TEST_MOCK), is(nullValue()));
			assertThat(request.getAttribute(FROM_REQUEST_FILTER), is(nullValue()));
			assertThat(request.getAttribute(FROM_REQUEST_ATTRIBUTES_FILTER), is(nullValue()));
		}
	}
{
		String origin = request.getHeaders().getOrigin();
		if (origin == null) {
			return true;
		}
		UriComponents actualUrl = UriComponentsBuilder.fromHttpRequest(request).build();
		UriComponents originUrl = UriComponentsBuilder.fromOriginHeader(origin).build();
		return (actualUrl.getHost().equals(originUrl.getHost()) && getPort(actualUrl) == getPort(originUrl));
	}
{
		ResolvableType payloadType = null;
		if (event instanceof PayloadApplicationEvent) {
			PayloadApplicationEvent<?> payloadEvent = (PayloadApplicationEvent<?>) event;
			payloadType = payloadEvent.getResolvableType().as(
					PayloadApplicationEvent.class).getGeneric(0);
		}
		for (ResolvableType declaredEventType : this.declaredEventTypes) {
			if (!ApplicationEvent.class.isAssignableFrom(declaredEventType.getRawClass())
					&& payloadType != null) {
				if (declaredEventType.isAssignableFrom(payloadType)) {
					return declaredEventType;
				}
			}
			if (declaredEventType.getRawClass().isAssignableFrom(event.getClass())) {
				return declaredEventType;
			}
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
	}
{
		TestContextManager testContextManager = new TestContextManager(testClass);
		assertEquals("Num registered TELs for " + testClass, expected,
			testContextManager.getTestExecutionListeners().size());
	}
{
		if (this.content.size() > 0) {
			HttpServletResponse rawResponse = (HttpServletResponse) getResponse();
			if ((complete || this.contentLength != null) && !rawResponse.isCommitted()){
				rawResponse.setContentLength(complete ? this.content.size() : this.contentLength);
				this.contentLength = null;
			}
			this.content.writeTo(rawResponse.getOutputStream());
			this.content.reset();
		}
	}
{
		Type type;
		if (HttpEntity.class.isAssignableFrom(returnType.getParameterType())) {
			returnType.increaseNestingLevel();
			type = returnType.getNestedGenericParameterType();
		}
		else {
			type = returnType.getGenericParameterType();
		}
		return type;
	}
{
		Set<MediaType> mediaTypes = (Set<MediaType>) request.getAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
		if (!CollectionUtils.isEmpty(mediaTypes)) {
			return new ArrayList<MediaType>(mediaTypes);
		}
		else if (!this.allSupportedMediaTypes.isEmpty()) {
			List<MediaType> result = new ArrayList<MediaType>();
			for (HttpMessageConverter<?> converter : this.messageConverters) {
				if (converter instanceof GenericHttpMessageConverter && returnValueType != null) {
					if (((GenericHttpMessageConverter<?>) converter).canWrite(returnValueType, returnValueClass, null)) {
						result.addAll(converter.getSupportedMediaTypes());
					}
				}
				else if (converter.canWrite(returnValueClass, null)) {
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
		return list;
	}
{

		if (headers.getContentType() == null) {
			MediaType contentTypeToUse = contentType;
			if (contentType == null || contentType.isWildcardType() || contentType.isWildcardSubtype()) {
				contentTypeToUse = getDefaultContentType(t);
			}
			else if (MediaType.APPLICATION_OCTET_STREAM.equals(contentType)) {
				MediaType mediaType = getDefaultContentType(t);
				contentTypeToUse = (mediaType != null ? mediaType : contentTypeToUse);
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
	}
{
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(startsWith("Attributes map"));
		exception.expectMessage(containsString("returned null for required attribute [text]"));
		exception.expectMessage(containsString("defined by annotation type [" + AnnotationWithoutDefaults.class.getName() + "]"));
		synthesizeAnnotation(attributes, AnnotationWithoutDefaults.class, null);
	}
{
		try {
			return searchWithFindSemantics(element, annotationName, processor, new HashSet<AnnotatedElement>(), 0);
		}
		catch (Throwable ex) {
			AnnotationUtils.rethrowAnnotationConfigurationException(ex);
			throw new IllegalStateException("Failed to introspect annotations on " + element, ex);
		}
	}
{

		Assert.hasText(attributeName, "attributeName must not be null or empty");
		Assert.notNull(annotationType, "annotationType must not be null");
		Assert.notNull(expectedType, "expectedType must not be null");
		Assert.isTrue(expectedType.isArray(), "expectedType must be an array");

		T attributeValue = getAttribute(attributeName, expectedType);
		String aliasName = AnnotationUtils.getAttributeAliasMap(annotationType).get(attributeName);
		T aliasValue = getAttribute(aliasName, expectedType);
		boolean attributeDeclared = !ObjectUtils.isEmpty((Object[]) attributeValue);
		boolean aliasDeclared = !ObjectUtils.isEmpty((Object[]) aliasValue);

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
		if (attributeValue == null) {
			throw new IllegalArgumentException(String.format(
				"Attribute '%s' not found in attributes for annotation [%s]", attributeName, this.displayName));
		}
	}
{
		if (!expectedType.isInstance(attributeValue)) {
			throw new IllegalArgumentException(String.format(
				"Attribute '%s' is of type [%s], but [%s] was expected in attributes for annotation [%s]",
				attributeName, attributeValue.getClass().getSimpleName(), expectedType.getSimpleName(),
				this.displayName));
		}
	}
{
		String attributeName = attributeMethod.getName();
		Object value = this.valueCache.get(attributeName);
		if (value == null) {
			value = this.attributeExtractor.getAttributeValue(attributeMethod);
			if (value == null) {
				throw new IllegalStateException(String.format(
					"%s returned null for attribute name [%s] from attribute source [%s]",
					this.attributeExtractor.getClass().getName(), attributeName, this.attributeExtractor.getSource()));
			}

			// Synthesize nested annotations before returning them.
			if (value instanceof Annotation) {
				value = synthesizeAnnotation((Annotation) value, this.attributeExtractor.getAnnotatedElement());
			}
			else if (value instanceof Annotation[]) {
				Annotation[] orig = (Annotation[]) value;
				Annotation[] clone = (Annotation[]) Array.newInstance(orig.getClass().getComponentType(), orig.length);
				for (int i = 0; i < orig.length; i++) {
					clone[i] = synthesizeAnnotation(orig[i], this.attributeExtractor.getAnnotatedElement());
				}
				value = clone;
			}

			this.valueCache.put(attributeName, value);
		}

		// Clone arrays so that users cannot alter the contents of values in our cache.
		if (value.getClass().isArray()) {
			value = cloneArray(value);
		}

		return value;
	}
{

			Object value = AnnotationUtils.getValue(annotation, sourceAttributeName);
			Object adaptedValue = AnnotationUtils.adaptValue(element, value, this.classValuesAsString,
				this.nestedAnnotationsAsMap);
			attributes.put(targetAttributeName, adaptedValue);
		}
{
		if (annotation == null) {
			return null;
		}
		if (annotation instanceof SynthesizedAnnotation) {
			return annotation;
		}

		Class<? extends Annotation> annotationType = annotation.annotationType();

		// No need to synthesize?
		if (!isSynthesizable(annotationType)) {
			return annotation;
		}

		InvocationHandler handler = new SynthesizedAnnotationInvocationHandler(annotatedElement, annotation, getAliasMap(annotationType));
		A synthesizedAnnotation = (A) Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(), new Class<?>[] {
			(Class<A>) annotationType, SynthesizedAnnotation.class }, handler);

		return synthesizedAnnotation;
	}
{
		try {
			int startOffset = path.startsWith("/") ? 1 : 0;
			int endOffset = path.indexOf("/", 1);
			if (endOffset != -1) {
				String webjar = path.substring(startOffset, endOffset);
				String partialPath = path.substring(endOffset);
				String webJarPath = webJarAssetLocator.getFullPath(webjar, partialPath);
				return webJarPath.substring(WEBJARS_LOCATION_LENGTH);
			}
		} catch (MultipleMatchesException ex) {
			logger.warn("WebJar version conflict for \"" + path + "\"", ex);
		}
		catch (IllegalArgumentException ex) {
			if (logger.isTraceEnabled()) {
				logger.trace("No WebJar resource found for \"" + path + "\"");
			}
		}
		return null;
	}
{
		baseUrl = getBaseUrlToUse(baseUrl);
		String typePath = getTypeRequestMapping(controllerType);
		String methodPath = getMethodRequestMapping(method);
		String path = pathMatcher.combine(typePath, methodPath);
		baseUrl.path(path);
		UriComponents uriComponents = applyContributors(baseUrl, method, args);
		return UriComponentsBuilder.newInstance().uriComponents(uriComponents);
	}
{
		return this.errorHandler;
	}
{
		if (getErrorHandler() == null) {
			sendErrorMessage(session, ex);
			return;
		}
		Message<byte[]> message = getErrorHandler().handleClientMessageProcessingError(clientMessage, ex);
		if (message == null) {
			return;
		}
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		Assert.notNull(accessor, "Expected STOMP headers.");
		sendToClient(session, accessor, message.getPayload());
	}
{
		StompCommand command = stompAccessor.getCommand();
		try {
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
		return this.uriTemplateHandler;
	}
{

		// Search in annotations
		for (Annotation annotation : annotations) {
			if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotation)
					&& (annotation.annotationType().getName().equals(annotationType) || metaDepth > 0)) {
				T result = processor.process(annotation, metaDepth);
				if (result != null) {
					return result;
				}
			}
		}

		// Recursively search in meta-annotations
		for (Annotation annotation : annotations) {
			if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotation)) {
				T result = searchWithGetSemantics(annotation.annotationType(), annotationType, processor, visited,
					metaDepth + 1);
				if (result != null) {
					processor.postProcess(annotation, result);
					return result;
				}
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
		return fieldType;
	}
{
		Class<? extends Annotation> annotationType = (Class<? extends Annotation>)
				GenericTypeResolver.resolveTypeArgument(factory.getClass(), AnnotationFormatterFactory.class);
		if (annotationType == null) {
			throw new IllegalArgumentException("Unable to extract parameterized Annotation type argument from " +
					"AnnotationFormatterFactory [" + factory.getClass().getName() +
					"]; does the factory parameterize the <A extends Annotation> generic type?");
		}
		return annotationType;
	}
{

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
		return urlMap;
	}
{
		return this.userDestinationBroadcast;
	}
{
		Assert.hasText(annotationType, "annotationType must not be null or empty");
		return annotationType.startsWith("java.lang.annotation");
	}
{
		boolean hasAllowOrigin = false;
		try {
			hasAllowOrigin = (response.getHeaders().getAccessControlAllowOrigin() != null);
		}
		catch (NullPointerException npe) {
			// SPR-11919 and https://issues.jboss.org/browse/WFLY-3474
		}
		if (hasAllowOrigin) {
			logger.debug("Skip adding CORS headers, response already contains \"Access-Control-Allow-Origin\"");
		}
		return hasAllowOrigin;
	}
{
		response.setStatusCode(HttpStatus.FORBIDDEN);
		response.getBody().write("Invalid CORS request".getBytes(UTF8_CHARSET));
	}
{
		int size = this.queryParams.size();
		MultiValueMap<String, String> result = new LinkedMultiValueMap<String, String>(size);
		for (Map.Entry<String, List<String>> entry : this.queryParams.entrySet()) {
			String name = encodeUriComponent(entry.getKey(), encoding, Type.QUERY_PARAM);
			List<String> values = new ArrayList<String>(entry.getValue().size());
			for (String value : entry.getValue()) {
				values.add(encodeUriComponent(value, encoding, Type.QUERY_PARAM));
			}
			result.put(name, values);
		}
		return result;
	}
{
		int size = this.queryParams.size();
		MultiValueMap<String, String> result = new LinkedMultiValueMap<String, String>(size);
		for (Map.Entry<String, List<String>> entry : this.queryParams.entrySet()) {
			String name = expandUriComponent(entry.getKey(), variables);
			List<String> values = new ArrayList<String>(entry.getValue().size());
			for (String value : entry.getValue()) {
				values.add(expandUriComponent(value, variables));
			}
			result.put(name, values);
		}
		return result;
	}
{
		Assert.notNull(connectionHandler, "'connectionHandler' must not be null");

		TcpClient<Message<P>, Message<P>> tcpClient;
		synchronized (this.tcpClients) {
			if (this.stopping) {
				IllegalStateException ex = new IllegalStateException("Shutting down.");
				connectionHandler.afterConnectFailure(ex);
				return new PassThroughPromiseToListenableFutureAdapter<Void>(Promises.<Void>error(ex));
			}
			tcpClient = NetStreams.tcpClient(REACTOR_TCP_CLIENT_TYPE, this.tcpClientSpecFactory);
			this.tcpClients.add(tcpClient);
		}

		Promise<Void> promise = tcpClient.start(new MessageChannelStreamHandler<P>(connectionHandler));

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
		Assert.notNull(connectionHandler, "'connectionHandler' must not be null");
		Assert.notNull(strategy, "'reconnectStrategy' must not be null");

		TcpClient<Message<P>, Message<P>> tcpClient;
		synchronized (this.tcpClients) {
			if (this.stopping) {
				IllegalStateException ex = new IllegalStateException("Shutting down.");
				connectionHandler.afterConnectFailure(ex);
				return new PassThroughPromiseToListenableFutureAdapter<Void>(Promises.<Void>error(ex));
			}
			tcpClient = NetStreams.tcpClient(REACTOR_TCP_CLIENT_TYPE, this.tcpClientSpecFactory);
			this.tcpClients.add(tcpClient);
		}

		Stream<Tuple2<InetSocketAddress, Integer>> stream = tcpClient.start(
				new MessageChannelStreamHandler<P>(connectionHandler),
				new ReactorReconnectAdapter(strategy));

		return new PassThroughPromiseToListenableFutureAdapter<Void>(stream.next().after());
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
		RequestMapping annotation;
		AnnotationAttributes attributes;
		RequestCondition<?> customCondition;
		String annotationType = RequestMapping.class.getName();
		if (annotatedElement instanceof Class<?>) {
			Class<?> type = (Class<?>) annotatedElement;
			annotation = AnnotationUtils.findAnnotation(type, RequestMapping.class);
			attributes = AnnotatedElementUtils.getAnnotationAttributes(type, annotationType);
			customCondition = getCustomTypeCondition(type);
		}
		else {
			Method method = (Method) annotatedElement;
			annotation = AnnotationUtils.findAnnotation(method, RequestMapping.class);
			attributes = AnnotatedElementUtils.getAnnotationAttributes(method, annotationType);
			customCondition = getCustomMethodCondition(method);
		}
		RequestMappingInfo info = null;
		if (annotation != null) {
			info = createRequestMappingInfo(annotation, customCondition);
			if (info == null) {
				info = createRequestMappingInfo(attributes, customCondition);
			}
		}
		return info;
	}
{
		int ioThreadCount;
		try {
			ioThreadCount = Integer.parseInt(System.getProperty("reactor.tcp.ioThreadCount"));
		}
		catch (Exception i) {
			ioThreadCount = -1;
		}
		if (ioThreadCount <= 0l) {
			ioThreadCount = Runtime.getRuntime().availableProcessors();
		}

		return new NioEventLoopGroup(ioThreadCount,
				new NamedDaemonThreadFactory("reactor-tcp-io"));
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
		return new MethodArgumentBuilder(builder, handlerMethods.get(0).getMethod());
	}
{
		Message<?> message = this.messageCaptor.getAllValues().get(index);
		return MessageHeaderAccessor.getAccessor(message, SimpMessageHeaderAccessor.class);
	}
{

		EvaluationContext context = null;
		MultiValueMap<String, String> result = new LinkedMultiValueMap<String, String>(allMatches.size());
		for (String sessionId : allMatches.keySet()) {
			for (String subId : allMatches.get(sessionId)) {
				SessionSubscriptionInfo info = this.subscriptionRegistry.getSubscriptions(sessionId);
				Subscription sub = info.getSubscription(subId);
				Expression expression = sub.getSelectorExpression();
				if (expression == null) {
					result.add(sessionId, subId);
					continue;
				}
				if (context == null) {
					context = new StandardEvaluationContext(message);
					context.getPropertyAccessors().add(new SimpMessageHeaderPropertyAccessor());
				}
				try {
					if (expression.getValue(context, boolean.class)) {
						result.add(sessionId, subId);
					}
				}
				catch (SpelEvaluationException ex) {
					if (logger.isDebugEnabled()) {
						logger.debug("Failed to evaluate selector: " + ex.getMessage());
					}
				}
				catch (Throwable ex) {
					if (logger.isDebugEnabled()) {
						logger.debug("Failed to evaluate selector.", ex);
					}
				}
			}
		}
		return result;
	}
{
		SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create(SimpMessageType.SUBSCRIBE);
		accessor.setSessionId(sessionId);
		accessor.setSubscriptionId(subId);
		if (dest != null) {
			accessor.setDestination(dest);
		}
		if (selector != null) {
			accessor.setNativeHeader("selector", selector);
		}
		return MessageBuilder.createMessage("", accessor.getMessageHeaders());
	}
{
		CacheAwareContextLoaderDelegate cacheAwareContextLoaderDelegate = new DefaultCacheAwareContextLoaderDelegate();
		return new DefaultBootstrapContext(testClass, cacheAwareContextLoaderDelegate);
	}
{
		MessageHeaders headers = message.getHeaders();
		String destination = SimpMessageHeaderAccessor.getDestination(headers);
		if (destination == null || !checkDestination(destination, this.prefix)) {
			return null;
		}
		SimpMessageType messageType = SimpMessageHeaderAccessor.getMessageType(headers);
		Principal principal = SimpMessageHeaderAccessor.getUser(headers);
		String sessionId = SimpMessageHeaderAccessor.getSessionId(headers);
		if (SimpMessageType.SUBSCRIBE.equals(messageType) || SimpMessageType.UNSUBSCRIBE.equals(messageType)) {
			if (sessionId == null) {
				logger.error("No session id. Ignoring " + message);
				return null;
			}
			int prefixEnd = this.prefix.length() - 1;
			String actualDestination = destination.substring(prefixEnd);
			String user = (principal != null ? principal.getName() : null);
			return new ParseResult(actualDestination, destination, Collections.singleton(sessionId), user);
		}
		else if (SimpMessageType.MESSAGE.equals(messageType)) {
			int prefixEnd = this.prefix.length();
			int userEnd = destination.indexOf('/', prefixEnd);
			Assert.isTrue(userEnd > 0, "Expected destination pattern \"/user/{userId}/**\"");
			String actualDestination = destination.substring(userEnd);
			String subscribeDestination = this.prefix.substring(0, prefixEnd - 1) + actualDestination;
			String user = destination.substring(prefixEnd, userEnd);
			user = StringUtils.replace(user, "%2F", "/");
			Set<String> sessionIds;
			if (user.equals(sessionId)) {
				user = null;
				sessionIds = Collections.singleton(sessionId);
			}
			else if (this.sessionRegistry.getSessionIds(user).contains(sessionId)) {
				sessionIds = Collections.singleton(sessionId);
			}
			else {
				sessionIds = this.sessionRegistry.getSessionIds(user);
			}
			return new ParseResult(actualDestination, subscribeDestination, sessionIds, user);
		}
		else {
			return null;
		}
	}
{
		if (this.beanFactory instanceof ConfigurableBeanFactory) {
			return ((ConfigurableBeanFactory) this.beanFactory).resolveEmbeddedValue(value);
		}
		return value;
	}
{
		Assert.isTrue(targetObject != null || targetClass != null,
			"Either targetObject or targetClass for the field must be specified");

		if (targetClass == null) {
			targetClass = targetObject.getClass();
		}
		Field field = ReflectionUtils.findField(targetClass, name, type);

		// Inline Assert.notNull() to avoid invoking toString() on a non-null target.
		if (field == null) {
			throw new IllegalArgumentException(String.format(
				"Could not find field [%s] of type [%s] on target object [%s] or target class [%s]", name, type,
				targetObject, targetClass));
		}

		if (logger.isDebugEnabled()) {
			logger.debug(String.format(
				"Setting field [%s] of type [%s] on target object [%s] or target class [%s] to value [%s]", name, type,
				targetObject, targetClass, value));
		}
		ReflectionUtils.makeAccessible(field);
		ReflectionUtils.setField(field, targetObject, value);
	}
{
		Assert.isTrue(targetObject != null || targetClass != null,
			"Either targetObject or targetClass for the field must be specified");

		if (targetClass == null) {
			targetClass = targetObject.getClass();
		}
		Field field = ReflectionUtils.findField(targetClass, name);

		// Inline Assert.notNull() to avoid invoking toString() on a non-null target.
		if (field == null) {
			throw new IllegalArgumentException(
				String.format("Could not find field [%s] on target object [%s] or target class [%s]", name,
					targetObject, targetClass));
		}

		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Getting field [%s] from target object [%s] or target class [%s]", name,
				targetObject, targetClass));
		}
		ReflectionUtils.makeAccessible(field);
		return ReflectionUtils.getField(field, targetObject);
	}
{
		this.sessions.remove(sessionId);
		this.subscriptionRegistry.unregisterAllSubscriptions(sessionId);
		SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create(SimpMessageType.DISCONNECT_ACK);
		accessor.setSessionId(sessionId);
		accessor.setUser(user);
		initHeaders(accessor);
		Message<byte[]> message = MessageBuilder.createMessage(EMPTY_PAYLOAD, accessor.getMessageHeaders());
		getClientOutboundChannel().send(message);
	}
{

		if (hasAllowOriginHeader(response)) {
			logger.debug("Skip adding CORS headers, response already contains \"Access-Control-Allow-Origin\"");
			return false;
		}
		if (!checkOrigin(request, config.getAllowedOrigins()) || !checkMethod(request, config.getAllowedMethods()) ||
				!checkHeaders(request, config.getAllowedHeaders())) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CORS request");
			return false;
		}
		return true;
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
		if (StringUtils.hasLength(etag)) {
			String ifNoneMatch = getRequest().getHeader(HEADER_IF_NONE_MATCH);
			if (StringUtils.hasLength(ifNoneMatch)) {
				String[] clientETags = StringUtils.delimitedListToStringArray(ifNoneMatch, ",", " ");
				for (String clientETag : clientETags) {
					// compare weak/strong ETags as per https://tools.ietf.org/html/rfc7232#section-2.3
					if (StringUtils.hasLength(clientETag) &&
							(clientETag.replaceFirst("^W/", "").equals(etag.replaceFirst("^W/", ""))
								|| clientETag.equals("*"))) {
						return true;
					}
				}
			}
		}
		return false;
	}
{
		Class<?> testClass = testContext.getTestClass();
		Assert.notNull(testClass, "The test class of the supplied TestContext must not be null");
		Method testMethod = testContext.getTestMethod();
		Assert.notNull(testMethod, "The test method of the supplied TestContext must not be null");

		final String annotationType = DirtiesContext.class.getName();
		AnnotationAttributes methodAnnAttrs = AnnotatedElementUtils.getAnnotationAttributes(testMethod, annotationType);
		AnnotationAttributes classAnnAttrs = AnnotatedElementUtils.getAnnotationAttributes(testClass, annotationType);
		boolean methodAnnotated = methodAnnAttrs != null;
		boolean classAnnotated = classAnnAttrs != null;
		MethodMode methodMode = methodAnnotated ? methodAnnAttrs.<MethodMode> getEnum("methodMode") : null;
		ClassMode classMode = classAnnotated ? classAnnAttrs.<ClassMode> getEnum("classMode") : null;

		if (logger.isDebugEnabled()) {
			logger.debug(String.format(
				"%s test method: context %s, class annotated with @DirtiesContext [%s] with mode [%s], method annotated with @DirtiesContext [%s] with mode [%s].",
				phase, testContext, classAnnotated, classMode, methodAnnotated, methodMode));
		}

		if ((methodMode == requiredMethodMode) || (classMode == requiredClassMode)) {
			HierarchyMode hierarchyMode = methodAnnotated ? methodAnnAttrs.<HierarchyMode> getEnum("hierarchyMode")
					: classAnnAttrs.<HierarchyMode> getEnum("hierarchyMode");
			dirtyContext(testContext, hierarchyMode);
		}
	}
{
		Class<?> testClass = testContext.getTestClass();
		Assert.notNull(testClass, "The test class of the supplied TestContext must not be null");

		final String annotationType = DirtiesContext.class.getName();
		AnnotationAttributes classAnnAttrs = AnnotatedElementUtils.getAnnotationAttributes(testClass, annotationType);
		boolean classAnnotated = classAnnAttrs != null;
		ClassMode classMode = classAnnotated ? classAnnAttrs.<ClassMode> getEnum("classMode") : null;

		if (logger.isDebugEnabled()) {
			logger.debug(String.format(
				"%s test class: context %s, class annotated with @DirtiesContext [%s] with mode [%s].", phase,
				testContext, classAnnotated, classMode));
		}

		if (classMode == requiredClassMode) {
			HierarchyMode hierarchyMode = classAnnAttrs.<HierarchyMode> getEnum("hierarchyMode");
			dirtyContext(testContext, hierarchyMode);
		}
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
		byte[] boundary = new byte[RND.nextInt(11) + 30];
		for (int i = 0; i < boundary.length; i++) {
			boundary[i] = BOUNDARY_CHARS[RND.nextInt(BOUNDARY_CHARS.length)];
		}
		return boundary;
	}
{
		if (logger.isDebugEnabled()) {
			logger.debug("Searching methods to handle " + exception.getClass().getSimpleName());
		}
		Class<?> beanType = handlerMethod.getBeanType();
		AbstractExceptionHandlerMethodResolver resolver = this.exceptionHandlerCache.get(beanType);
		if (resolver == null) {
			resolver = createExceptionHandlerMethodResolverFor(beanType);
			this.exceptionHandlerCache.put(beanType, resolver);
		}
		Method method = resolver.resolveMethod(exception);
		if (method != null) {
			return new InvocableHandlerMethod(handlerMethod.getBean(), method);
		}
		for (MessagingAdviceBean advice : this.exceptionHandlerAdviceCache.keySet()) {
			if (advice.isApplicableToBeanType(beanType)) {
				resolver = this.exceptionHandlerAdviceCache.get(advice);
				method = resolver.resolveMethod(exception);
				if (method != null) {
					return new InvocableHandlerMethod(advice.resolveBean(), method);
				}
			}
		}
		return null;
	}
{
		String beanName = transformedBeanName(name);

		// Check manually registered singletons.
		Object beanInstance = getSingleton(beanName, false);
		if (beanInstance != null) {
			if (beanInstance instanceof FactoryBean) {
				if (!BeanFactoryUtils.isFactoryDereference(name)) {
					Class<?> type = getTypeForFactoryBean((FactoryBean<?>) beanInstance);
					return (type != null && typeToMatch.isAssignableFrom(type));
				}
				else {
					return typeToMatch.isInstance(beanInstance);
				}
			}
			else {
				return (!BeanFactoryUtils.isFactoryDereference(name) && typeToMatch.isInstance(beanInstance));
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
				return parentBeanFactory.isTypeMatch(originalBeanName(name), typeToMatch);
			}

			// Retrieve corresponding bean definition.
			RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);

			Class<?> classToMatch = typeToMatch.getRawClass();
			Class<?>[] typesToMatch = (FactoryBean.class.equals(classToMatch) ?
					new Class<?>[] {classToMatch} : new Class<?>[] {FactoryBean.class, classToMatch});

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
		boolean isFactoryType = (type != null && FactoryBean.class.isAssignableFrom(type.getRawClass()));
		List<String> matches = new ArrayList<String>();
		for (Map.Entry<String, Object> entry : this.beans.entrySet()) {
			String name = entry.getKey();
			Object beanInstance = entry.getValue();
			if (beanInstance instanceof FactoryBean && !isFactoryType) {
				Class<?> objectType = ((FactoryBean<?>) beanInstance).getObjectType();
				if (objectType != null && (type == null || type.isAssignableFrom(objectType))) {
					matches.add(name);
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
		if (builder != null) {
			builder = (UriComponentsBuilder) builder.clone();
		}
		else {
			builder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		}
		String mapping = getTypeRequestMapping(controllerType);
		return builder.path(mapping);
	}
{
		Assert.isInstanceOf(MethodInvocationInfo.class, invocationInfo);
		MethodInvocationInfo info = (MethodInvocationInfo) invocationInfo;
		return fromMethod(builder, info.getControllerMethod(), info.getArgumentValues());
	}
{
		if (builder != null) {
			builder = (UriComponentsBuilder) builder.clone();
		}
		else {
			builder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		}
		String typePath = getTypeRequestMapping(method.getDeclaringClass());
		String methodPath = getMethodRequestMapping(method);
		String path = pathMatcher.combine(typePath, methodPath);
		builder.path(path);
		UriComponents uriComponents = applyContributors(builder, method, args);
		return UriComponentsBuilder.newInstance().uriComponents(uriComponents);
	}
{
		Object invalidValue = violation.getInvalidValue();
		if (!"".equals(field) && (invalidValue == violation.getLeafBean() ||
				(field.contains(".") && !field.contains("[]")))) {
			// Possibly a bean constraint with property path: retrieve the actual property value.
			// However, explicitly avoid this for "address[]" style paths that we can't handle.
			invalidValue = bindingResult.getRawFieldValue(field);
		}
		return invalidValue;
	}
{

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
		assertFalse("handler should never be called with Future return type", exceptionHandler.isCalled());
	}
{

		if (contentType == null || contentType.isWildcardType() || contentType.isWildcardSubtype()) {
			contentType = getDefaultContentType();
		}
		Assert.notNull(contentType,
				"Count not determine Content-Type, set one using the 'defaultContentType' property");
		headers.setContentType(contentType);
		ImageOutputStream imageOutputStream = null;
		ImageWriter imageWriter = null;
		try {
			Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByMIMEType(contentType.toString());
			if (imageWriters.hasNext()) {
				imageWriter = imageWriters.next();
				ImageWriteParam iwp = imageWriter.getDefaultWriteParam();
				process(iwp);
				imageOutputStream = createImageOutputStream(body);
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
		synchronized (this.listenerContainers) {
			Assert.state(!this.listenerContainers.containsKey(id),
					"Another endpoint is already registered with id '" + id + "'");
			MessageListenerContainer container = createListenerContainer(endpoint, factory);
			this.listenerContainers.put(id, container);
			if (startImmediately) {
				startIfNecessary(container);
			}
		}
	}
{
		if (listenerContainer.isAutoStartup()) {
			listenerContainer.start();
		}
	}
{

		HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
		assertIsMultipartRequest(servletRequest);

		MultipartHttpServletRequest multipartRequest =
				WebUtils.getNativeRequest(servletRequest, MultipartHttpServletRequest.class);

		Class<?> paramType = parameter.getParameterType();
		boolean optional = paramType.getName().equals("java.util.Optional");
		if (optional) {
			parameter.increaseNestingLevel();
			paramType = parameter.getNestedParameterType();
		}

		String partName = getPartName(parameter);
		Object arg;

		if (MultipartFile.class.equals(paramType)) {
			Assert.notNull(multipartRequest, "Expected MultipartHttpServletRequest: is a MultipartResolver configured?");
			arg = multipartRequest.getFile(partName);
		}
		else if (isMultipartFileCollection(parameter)) {
			Assert.notNull(multipartRequest, "Expected MultipartHttpServletRequest: is a MultipartResolver configured?");
			arg = multipartRequest.getFiles(partName);
		}
		else if (isMultipartFileArray(parameter)) {
			Assert.notNull(multipartRequest, "Expected MultipartHttpServletRequest: is a MultipartResolver configured?");
			List<MultipartFile> files = multipartRequest.getFiles(partName);
			arg = files.toArray(new MultipartFile[files.size()]);
		}
		else if ("javax.servlet.http.Part".equals(paramType.getName())) {
			assertIsMultipartRequest(servletRequest);
			arg = servletRequest.getPart(partName);
		}
		else if (isPartCollection(parameter)) {
			assertIsMultipartRequest(servletRequest);
			arg = new ArrayList<Object>(servletRequest.getParts());
		}
		else if (isPartArray(parameter)) {
			assertIsMultipartRequest(servletRequest);
			arg = RequestPartResolver.resolvePart(servletRequest);
		}
		else {
			try {
				HttpInputMessage inputMessage = new RequestPartServletServerHttpRequest(servletRequest, partName);
				arg = readWithMessageConverters(inputMessage, parameter, parameter.getNestedGenericParameterType());
				WebDataBinder binder = binderFactory.createBinder(request, arg, partName);
				if (arg != null) {
					validateIfApplicable(binder, parameter);
					if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
						throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
					}
				}
				mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + partName, binder.getBindingResult());
			}
			catch (MissingServletRequestPartException ex) {
				// handled below
				arg = null;
			}
		}

		RequestPart ann = parameter.getParameterAnnotation(RequestPart.class);
		boolean isRequired = ((ann == null || ann.required()) && !optional);

		if (arg == null && isRequired) {
			throw new MissingServletRequestPartException(partName);
		}
		if (optional) {
			arg = Optional.ofNullable(arg);
		}

		return arg;
	}
{

		Object arg = readWithMessageConverters(webRequest, parameter, parameter.getGenericParameterType());
		String name = Conventions.getVariableNameForParameter(parameter);
		WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
		if (arg != null) {
			validateIfApplicable(binder, parameter);
			if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
				throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
			}
		}
		mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
		return arg;
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
		ResolvableType type = (eventType != null ? eventType : resolveDefaultEventType(event));
		for (final ApplicationListener<?> listener : getApplicationListeners(event, type)) {
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
		getApplicationEventMulticaster().multicastEvent(event, eventType);
		if (this.parent != null) {
			if (this.parent instanceof AbstractApplicationContext) {
				((AbstractApplicationContext) this.parent).publishEvent(event, eventType);
			}
			else {
				this.parent.publishEvent(event);
			}
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
		PlatformTransactionManager txManager = this.transactionManagerCache.get(qualifier);
		if (txManager == null) {
			txManager = BeanFactoryAnnotationUtils.qualifiedBeanOfType(
					this.beanFactory, PlatformTransactionManager.class, qualifier);
			this.transactionManagerCache.putIfAbsent(qualifier, txManager);
		}
		return txManager;
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

		return HttpClientBuilder.create().setConnectionManager(connectionManager).build();
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
		PlatformTransactionManager txManager = this.transactionManagerCache.get(qualifier);
		if (txManager == null) {
			txManager = BeanFactoryAnnotationUtils.qualifiedBeanOfType(
					this.beanFactory, PlatformTransactionManager.class, qualifier);
			this.transactionManagerCache.putIfAbsent(qualifier, txManager);
		}
		return txManager;
	}
{

		LinkedList<ApplicationListener<?>> allListeners = new LinkedList<ApplicationListener<?>>();
		Set<ApplicationListener<?>> listeners;
		Set<String> listenerBeans;
		synchronized (this.defaultRetriever) {
			listeners = new LinkedHashSet<ApplicationListener<?>>(this.defaultRetriever.applicationListeners);
			listenerBeans = new LinkedHashSet<String>(this.defaultRetriever.applicationListenerBeans);
		}
		for (ApplicationListener<?> listener : listeners) {
			if (supportsEvent(listener, event.getClass(), sourceType)) {
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
						if (!allListeners.contains(listener) && supportsEvent(listener, event.getClass(), sourceType)) {
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
		return allListeners;
	}
{
		if (org.apache.http.impl.client.AbstractHttpClient.class.isInstance(client)) {
			client.getParams().setIntParameter(
					org.apache.http.params.CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
		}
	}
{
		if (org.apache.http.impl.client.AbstractHttpClient.class.isInstance(client)) {
			client.getParams().setIntParameter(
					org.apache.http.params.CoreConnectionPNames.SO_TIMEOUT, timeout);
		}
	}
{
		TransactionInterceptor ti = new TransactionInterceptor();
		if (beanFactory != null) {
			ti.setBeanFactory(beanFactory);
		}
		if (transactionManager != null) {
			ti.setTransactionManager(transactionManager);
		}
		ti.setTransactionAttributeSource(new NameMatchTransactionAttributeSource());
		ti.afterPropertiesSet();
		return ti;
	}
{
		PlatformTransactionManager transactionManager = mock(PlatformTransactionManager.class);
		given(beanFactory.containsBean(name)).willReturn(true);
		given(beanFactory.getBean(name, PlatformTransactionManager.class)).willReturn(transactionManager);
		return transactionManager;
	}
{

		this.uri = request.getURI();
		this.handshakeHeaders = request.getHeaders();
		this.principal = request.getPrincipal();
		this.localAddress = request.getLocalAddress();
		this.remoteAddress = request.getRemoteAddress();

		synchronized (this.responseLock) {
			try {
				this.response = response;
				this.frameFormat = frameFormat;
				this.asyncRequestControl = request.getAsyncRequestControl(response);
				this.asyncRequestControl.start(-1);

				// Let "our" handler know before sending the open frame to the remote handler
				delegateConnectionEstablished();

				if (isStreaming()) {
					writePrelude(request, response);
					writeFrame(SockJsFrame.openFrame());
					flushCache();
					this.readyToSend = true;
				}
				else {
					writeFrame(SockJsFrame.openFrame());
				}
			}
			catch (Throwable ex) {
				tryCloseWithSockJsTransportError(ex, CloseStatus.SERVER_ERROR);
				throw new SockJsTransportFailureException("Failed to open session", getId(), ex);
			}
		}
	}
{

		synchronized (this.responseLock) {
			try {
				if (isClosed()) {
					response.getBody().write(SockJsFrame.closeFrameGoAway().getContentBytes());
					return;
				}
				this.response = response;
				this.frameFormat = frameFormat;
				this.asyncRequestControl = request.getAsyncRequestControl(response);
				this.asyncRequestControl.start(-1);

				if (isStreaming()) {
					writePrelude(request, response);
					flushCache();
					this.readyToSend = true;
				}
				else {
					if (this.messageCache.isEmpty()) {
						scheduleHeartbeat();
						this.readyToSend = true;
					}
					else {
						flushCache();
					}
				}
			}
			catch (Throwable ex) {
				tryCloseWithSockJsTransportError(ex, CloseStatus.SERVER_ERROR);
				throw new SockJsTransportFailureException("Failed to handle SockJS receive request", getId(), ex);
			}
		}
	}
{
		synchronized (this.responseLock) {
			this.messageCache.add(message);
			if (logger.isTraceEnabled()) {
				logger.trace(this.messageCache.size() + " message(s) to flush in session " + this.getId());
			}
			if (isActive() && this.readyToSend) {
				if (logger.isTraceEnabled()) {
					logger.trace("Session is active, ready to flush.");
				}
				cancelHeartbeat();
				flushCache();
				return;
			}
			else {
				if (logger.isTraceEnabled()) {
					logger.trace("Session is not active, not ready to flush.");
				}
				return;
			}
		}
	}
{
		Assert.notNull(method, "Method must not be null");
		PropertyDescriptor[] pds = getPropertyDescriptors(clazz);
		for (PropertyDescriptor pd : pds) {
			if (method.equals(pd.getReadMethod()) || method.equals(pd.getWriteMethod())) {
				return pd;
			}
		}
		return null;
	}
{
		String canonicalName = canonicalName(beanName);
		if (alreadySeen != null && alreadySeen.contains(beanName)) {
			return false;
		}
		Set<String> dependentBeans = this.dependentBeanMap.get(canonicalName);
		if (dependentBeans == null) {
			return false;
		}
		if (dependentBeans.contains(dependentBeanName)) {
			return true;
		}
		for (String transitiveDependency : dependentBeans) {
			if (alreadySeen == null) {
				alreadySeen = new HashSet<String>();
			}
			alreadySeen.add(beanName);
			if (isDependent(transitiveDependency, dependentBeanName, alreadySeen)) {
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

		ServletUriComponentsBuilder builder = new ServletUriComponentsBuilder();
		builder.scheme(scheme);
		builder.host(host);
		if (scheme.equals("http") && port != 80 || scheme.equals("https") && port != 443) {
			builder.port(port);
		}
		return builder;
	}
{
		String prefix = request.getHeader("X-Forwarded-Prefix");
		if (StringUtils.hasText(prefix)) {
			path = prefix + path;
		}
		return path;
	}
{
		DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
		resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setContentTypeResolver(resolver);
		return converter;
	}
{
		if (!this.features.containsKey(MapperFeature.DEFAULT_VIEW_INCLUSION)) {
			configureFeature(objectMapper, MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		}
		if (!this.features.containsKey(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)) {
			configureFeature(objectMapper, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
	}
{
		if (logger.isDebugEnabled()) {
			SimpMessageHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, SimpMessageHeaderAccessor.class);
			accessor = (accessor != null ? accessor : SimpMessageHeaderAccessor.wrap(message));
			logger.debug("Processing " + accessor.getShortLogMessage(message.getPayload()));
		}
	}
{
		if (collectionClass.isInterface()) {
			if (Set.class.equals(collectionClass) || Collection.class.equals(collectionClass)) {
				return new LinkedHashSet<E>(capacity);
			}
			else if (List.class.equals(collectionClass)) {
				return new ArrayList<E>(capacity);
			}
			else if (SortedSet.class.equals(collectionClass) || NavigableSet.class.equals(collectionClass)) {
				return new TreeSet<E>();
			}
			else {
				throw new IllegalArgumentException("Unsupported Collection interface: " + collectionClass.getName());
			}
		}
		else if (EnumSet.class.equals(collectionClass)) {
			Assert.notNull(elementType, "Cannot create EnumSet for unknown element type");
			return EnumSet.noneOf((Class) elementType);
		}
		else {
			if (!Collection.class.isAssignableFrom(collectionClass)) {
				throw new IllegalArgumentException("Unsupported Collection type: " + collectionClass.getName());
			}
			try {
				return (Collection<E>) collectionClass.newInstance();
			}
			catch (Exception ex) {
				throw new IllegalArgumentException(
						"Could not instantiate Collection type: " + collectionClass.getName(), ex);
			}
		}
	}
{
		if (mapClass.isInterface()) {
			if (Map.class.equals(mapClass)) {
				return new LinkedHashMap<K, V>(capacity);
			}
			else if (SortedMap.class.equals(mapClass) || NavigableMap.class.equals(mapClass)) {
				return new TreeMap<K, V>();
			}
			else if (MultiValueMap.class.equals(mapClass)) {
				return new LinkedMultiValueMap();
			}
			else {
				throw new IllegalArgumentException("Unsupported Map interface: " + mapClass.getName());
			}
		}
		else if (EnumMap.class.equals(mapClass)) {
			Assert.notNull(keyType, "Cannot create EnumMap for unknown key type");
			return new EnumMap(keyType);
		}
		else {
			if (!Map.class.isAssignableFrom(mapClass)) {
				throw new IllegalArgumentException("Unsupported Map type: " + mapClass.getName());
			}
			try {
				return (Map<K, V>) mapClass.newInstance();
			}
			catch (Exception ex) {
				throw new IllegalArgumentException(
						"Could not instantiate Map type: " + mapClass.getName(), ex);
			}
		}
	}
{
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

		return new InjectionMetadata(clazz, elements);
	}
{
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

		return new InjectionMetadata(clazz, elements);
	}
{
		return this.allowBeanDefinitionOverriding;
	}
{
		return this.beanFactory.evaluateBeanDefinitionString(value, this.beanDefinition);
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
			synchronized (connectionMonitor) {
				if (!this.locallyStarted) {
					this.locallyStarted = true;
					if (startedCount == 0 && connection != null) {
						connection.start();
					}
					startedCount++;
				}
			}
		}
{
		session.disconnect();
	}
{
		Log loggerToUse = logger;
		if (loggerToUse == null) {
			loggerToUse = LogFactory.getLog(AnnotationUtils.class);
			logger = loggerToUse;
		}
		if (loggerToUse.isInfoEnabled()) {
			loggerToUse.info("Failed to introspect annotations on [" + annotatedElement + "]: " + ex);
		}
	}
{
			String name = ResourceUrlProviderExposingInterceptor.RESOURCE_URL_PROVIDER_ATTR;
			return (ResourceUrlProvider) this.request.getAttribute(name);
		}
{
		ResourceHttpRequestHandler handler = new ResourceHttpRequestHandler();
		handler.setLocations(Arrays.asList(new ClassPathResource("test/", getClass())));
		handler.setResourceResolvers(resolvers);
		ResourceUrlProvider urlProvider = new ResourceUrlProvider();
		urlProvider.setHandlerMap(Collections.singletonMap("/resources/**", handler));
		return urlProvider;
	}
{
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			return servletRequest.getServletRequest().getSession(false);
		}
		return null;
	}
{
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

		Transport transport = getTransport(getSession());
		transport.connect(getHost(), getPort(), username, password);
		return transport;
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
	}
{
		CachedMethodExecutor executorToCheck = this.cachedExecutor;
		if (executorToCheck.get() instanceof ReflectiveMethodExecutor) {
			Method method = ((ReflectiveMethodExecutor) executorToCheck.get()).getMethod();
			this.exitTypeDescriptor = CodeFlow.toDescriptor(method.getReturnType());
		}
	}
{
		Set<Resource> result = new LinkedHashSet<Resource>(16);
		ClassLoader cl = getClassLoader();
		Enumeration<URL> resourceUrls = (cl != null ? cl.getResources(path) : ClassLoader.getSystemResources(path));
		while (resourceUrls.hasMoreElements()) {
			URL url = resourceUrls.nextElement();
			result.add(convertClassLoaderURL(url));
		}
		if ("".equals(path)) {
			// The above result is likely to be incomplete, i.e. only containing file system references.
			// We need to have pointers to each of the jar files on the classpath as well...
			addAllClassLoaderJarRoots(cl, result);
		}
		return result;
	}
{
		for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
			String headerName = entry.getKey();
			if (HttpHeaders.COOKIE.equalsIgnoreCase(headerName)) {  // RFC 6265
				String headerValue = StringUtils.collectionToDelimitedString(entry.getValue(), "; ");
				connection.setRequestProperty(headerName, headerValue);
			}
			else {
				for (String headerValue : entry.getValue()) {
					connection.addRequestProperty(headerName, headerValue);
				}
			}
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
			if (this.dependencyComparator != null && result instanceof List) {
				Collections.sort((List<?>) result, adaptDependencyComparator(matchingBeans));
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
		boolean p1 = (o1 instanceof PriorityOrdered);
		boolean p2 = (o2 instanceof PriorityOrdered);
		if (p1 && !p2) {
			return -1;
		}
		else if (p2 && !p1) {
			return 1;
		}

		// Direct evaluation instead of Integer.compareTo to avoid unnecessary object creation.
		int i1 = getOrder(o1, sourceProvider);
		int i2 = getOrder(o2, sourceProvider);
		return (i1 < i2) ? -1 : (i1 > i2) ? 1 : 0;
	}
{

		if (trackedConditionEvaluator.shouldSkip(configClass)) {
			String beanName = configClass.getBeanName();
			if (StringUtils.hasLength(beanName) && this.registry.containsBeanDefinition(beanName)) {
				this.registry.removeBeanDefinition(beanName);
			}
			this.importRegistry.removeImportingClassFor(configClass.getMetadata().getClassName());
			return;
		}

		if (configClass.isImported()) {
			registerBeanDefinitionForImportedConfigurationClass(configClass);
		}
		for (BeanMethod beanMethod : configClass.getBeanMethods()) {
			loadBeanDefinitionsForBeanMethod(beanMethod);
		}
		loadBeanDefinitionsFromImportedResources(configClass.getImportedResources());
		loadBeanDefinitionsFromRegistrars(configClass.getImportBeanDefinitionRegistrars());
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
		return targetType;
	}
{

		Session session = getSession(entityManager);

		if (definition.getTimeout() != TransactionDefinition.TIMEOUT_DEFAULT) {
			session.getTransaction().setTimeout(definition.getTimeout());
		}

		boolean isolationLevelNeeded = (definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT);
		Integer previousIsolationLevel = null;
		boolean resetConnection = false;

		if (isolationLevelNeeded || definition.isReadOnly()) {
			if (this.prepareConnection) {
				Connection con = HibernateConnectionHandle.doGetConnection(session);
				previousIsolationLevel = DataSourceUtils.prepareConnectionForTransaction(con, definition);
				resetConnection = true;
			}
			else if (isolationLevelNeeded) {
				throw new InvalidIsolationLevelException(getClass().getSimpleName() +
						" does not support custom isolation levels since the 'prepareConnection' flag is off. " +
						"This is the case on Hibernate 3.6 by default; either switch that flag at your own risk " +
						"or upgrade to Hibernate 4.x, with 4.2+ recommended.");
			}
		}

		// Standard JPA transaction begin call for full JPA context setup...
		entityManager.getTransaction().begin();

		// Adapt flush mode and store previous isolation level, if any.
		FlushMode previousFlushMode = prepareFlushMode(session, definition.isReadOnly());
		return new SessionTransactionData(session, previousFlushMode, resetConnection, previousIsolationLevel);
	}
{
		if (this.beanFactory != null && this.beanFactory instanceof ConfigurableBeanFactory) {
			return ((ConfigurableBeanFactory) this.beanFactory).resolveEmbeddedValue(value);
		}
		return value;
	}
{

		String autoRegistration = element.getAttribute("auto-registration");
		boolean isAutoRegistration = !(StringUtils.hasText(autoRegistration) && "false".equals(autoRegistration));

		ManagedList<? super Object> resourceResolvers = new ManagedList<Object>();
		resourceResolvers.setSource(source);
		ManagedList<? super Object> resourceTransformers = new ManagedList<Object>();
		resourceTransformers.setSource(source);

		parseResourceCache(resourceResolvers, resourceTransformers, element, source);
		parseResourceResolversTransformers(isAutoRegistration, resourceResolvers
				, resourceTransformers, parserContext, element, source);

		if (!resourceResolvers.isEmpty()) {
			resourceHandlerDef.getPropertyValues().add("resourceResolvers", resourceResolvers);
		}
		if (!resourceTransformers.isEmpty()) {
			resourceHandlerDef.getPropertyValues().add("resourceTransformers", resourceTransformers);
		}
	}
{
		if (!ObjectUtils.nullSafeEquals(this.cacheBuilder, cacheBuilder)) {
			this.cacheBuilder = cacheBuilder;
			refreshKnownCaches();
		}
	}
{
		String name = propertySource.getString("name");
		String[] locations = propertySource.getStringArray("value");
		boolean ignoreResourceNotFound = propertySource.getBoolean("ignoreResourceNotFound");
		Assert.isTrue(locations.length > 0, "At least one @PropertySource(value) location is required");
		for (String location : locations) {
			try {
				String resolvedLocation = this.environment.resolveRequiredPlaceholders(location);
				Resource resource = this.resourceLoader.getResource(resolvedLocation);
				ResourcePropertySource rps = (StringUtils.hasText(name) ?
						new ResourcePropertySource(name, resource) : new ResourcePropertySource(resource));
				addPropertySource(rps);
			}
			catch (IllegalArgumentException ex) {
				// from resolveRequiredPlaceholders
				if (!ignoreResourceNotFound) {
					throw ex;
				}
			}
			catch (FileNotFoundException ex) {
				// from ResourcePropertySource constructor
				if (!ignoreResourceNotFound) {
					throw ex;
				}
			}
		}
	}
{
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
			return getBean(beanNames[0], requiredType, args);
		}
		else if (beanNames.length > 1) {
			Map<String, Object> candidates = new HashMap<String, Object>();
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
		return this.jmsMessageConverter;
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
			try {
				if (connectionMethodToUse == null) {
					// Reflective lookup trying to find SessionImpl's connection() on Hibernate 4.x
					connectionMethodToUse = session.getClass().getMethod("connection");
				}
				return (Connection) ReflectionUtils.invokeMethod(connectionMethodToUse, session);
			}
			catch (NoSuchMethodException ex) {
				throw new IllegalStateException("Cannot find connection() method on Hibernate Session", ex);
			}
		}
{
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
			return getAnnotationAttributes((Annotation) value, classValuesAsString, true);
		}
		else if (nestedAnnotationsAsMap && value instanceof Annotation[]) {
			Annotation[] realAnnotations = (Annotation[]) value;
			AnnotationAttributes[] mappedAnnotations = new AnnotationAttributes[realAnnotations.length];
			for (int i = 0; i < realAnnotations.length; i++) {
				mappedAnnotations[i] = getAnnotationAttributes(realAnnotations[i], classValuesAsString, true);
			}
			return mappedAnnotations;
		}
		else {
			return value;
		}
	}
{
			for (Class<?> implementedInterface : type.getInterfaces()) {
				addToClassHierarchy(hierarchy.size(), implementedInterface, asArray, hierarchy, visited);
			}
		}
{
		List<DefaultTransportRequest> requests = new ArrayList<DefaultTransportRequest>(this.transports.size());
		for (Transport transport : this.transports) {
			for (TransportType type : transport.getTransportTypes()) {
				if (serverInfo.isWebSocketEnabled() || !TransportType.WEBSOCKET.equals(type)) {
					requests.add(new DefaultTransportRequest(urlInfo, headers, transport, type, getMessageCodec()));
				}
			}
		}
		Assert.notEmpty(requests, "No transports, " + urlInfo + ", wsEnabled=" + serverInfo.isWebSocketEnabled());
		for (int i = 0; i < requests.size() - 1; i++) {
			DefaultTransportRequest request = requests.get(i);
			request.setUser(getUser());
			if (this.connectTimeoutScheduler != null) {
				request.setTimeoutValue(serverInfo.getRetransmissionTimeout());
				request.setTimeoutScheduler(this.connectTimeoutScheduler);
			}
			request.setFallbackRequest(requests.get(i + 1));
		}
		return requests.get(0);
	}
{
		List<String> result = new ArrayList<String>();
		result.add(AutoProxyRegistrar.class.getName());
		result.add(ProxyCachingConfiguration.class.getName());
		if (jsr107Present && jCacheImplPresent) {
			result.add(PROXY_JCACHE_CONFIGURATION_CLASS);
		}
		return result.toArray(new String[result.size()]);
	}
{
		List<String> result = new ArrayList<String>();
		result.add(CACHE_ASPECT_CONFIGURATION_CLASS_NAME);
		if (jsr107Present && jCacheImplPresent) {
			result.add(JCACHE_ASPECT_CONFIGURATION_CLASS_NAME);
		}
		return result.toArray(new String[result.size()]);
	}
{
		return MessageBuilder
				.withPayload(payload).setHeader("foo", "bar").build();
	}
{

		HttpServletRequest servletRequest = getHttpServletRequest(request);
		HttpServletResponse servletResponse = getHttpServletResponse(response);

		TyrusServerContainer serverContainer = (TyrusServerContainer) getContainer(servletRequest);
		TyrusWebSocketEngine engine = (TyrusWebSocketEngine) serverContainer.getWebSocketEngine();
		TyrusEndpointWrapper tyrusEndpoint = null;

		try {
			tyrusEndpoint = createTyrusEndpoint(endpoint, subProtocol, extensions, serverContainer);
			endpointRegistrationMethod.invoke(engine, tyrusEndpoint);

			String endpointPath = tyrusEndpoint.getEndpointPath();
			HttpHeaders headers = request.getHeaders();

			RequestContext requestContext = createRequestContext(servletRequest, endpointPath, headers);
			TyrusUpgradeResponse upgradeResponse = new TyrusUpgradeResponse();
			UpgradeInfo upgradeInfo = engine.upgrade(requestContext, upgradeResponse);

			switch (upgradeInfo.getStatus()) {
				case SUCCESS:
					TyrusHttpUpgradeHandler handler = servletRequest.upgrade(TyrusHttpUpgradeHandler.class);
					Writer servletWriter = createTyrusServletWriter(handler);
					handler.preInit(upgradeInfo, servletWriter, servletRequest.getUserPrincipal() != null);
					servletResponse.setStatus(upgradeResponse.getStatus());
					for (Map.Entry<String, List<String>> entry : upgradeResponse.getHeaders().entrySet()) {
						servletResponse.addHeader(entry.getKey(), Utils.getHeaderFromList(entry.getValue()));
					}
					servletResponse.flushBuffer();
					if (logger.isTraceEnabled()) {
						logger.trace("Successful upgrade uri=" + servletRequest.getRequestURI() +
								", response headers=" + upgradeResponse.getHeaders());
					}
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
				engine.unregister(tyrusEndpoint);
			}
		}
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
		CompositeComponentDefinition compositeDef =
				new CompositeComponentDefinition(element.getTagName(), parserContext.extractSource(element));
		parserContext.pushContainingComponent(compositeDef);

		PropertyValues commonProperties = parseCommonContainerProperties(element, parserContext);
		PropertyValues specificProperties = parseSpecificContainerProperties(element, parserContext);

		String factoryId = element.getAttribute(FACTORY_ID_ATTRIBUTE);
		if (StringUtils.hasText(factoryId)) {
			RootBeanDefinition beanDefinition = createContainerFactory(
					factoryId, element, parserContext, commonProperties, specificProperties);
			if (beanDefinition != null) {
				beanDefinition.setSource(parserContext.extractSource(element));
				parserContext.registerBeanComponent(new BeanComponentDefinition(beanDefinition, factoryId));
			}
		}

		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				String localName = parserContext.getDelegate().getLocalName(child);
				if (LISTENER_ELEMENT.equals(localName)) {
					parseListener(element, (Element) child, parserContext, commonProperties, specificProperties);
				}
			}
		}

		parserContext.popAndRegisterContainingComponent();
		return null;
	}
{
		for (MessageListenerContainer listenerContainer : getListenerContainers()) {
			if (listenerContainer instanceof DisposableBean) {
				try {
					((DisposableBean) listenerContainer).destroy();
				}
				catch (Throwable ex) {
					logger.warn("Failed to destroy message listener container", ex);
				}
			}
		}
	}
{
		Map<String, ?> urlMap = getHandlerMapping().getUrlMap();
		ParameterizableViewController controller = (ParameterizableViewController) urlMap.get(path);
		assertNotNull(controller);
		return controller;
	}
{
		try {
			successCallback.onSuccess(this.value);
		} catch(Throwable t) {
			failureCallback.onFailure(t);
		}
	}
{
		ListenableFuture<S> listenableAdaptee = (ListenableFuture<S>) getAdaptee();
		listenableAdaptee.addCallback(new ListenableFutureCallback<S>() {
			@Override
			public void onSuccess(S result) {
				try {
					successCallback.onSuccess(adaptInternal(result));
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
				failureCallback.onFailure(t);
			}
		});
	}
{
		// Apply settings from the container
		containerDef.getPropertyValues().addPropertyValues(context.getContainerValues());

		// Clone the activationSpecConfig property value as it is mutable
		PropertyValue pv = containerDef.getPropertyValues().getPropertyValue("activationSpecConfig");
		RootBeanDefinition activationSpecConfig = new RootBeanDefinition((RootBeanDefinition) pv.getValue());
		containerDef.getPropertyValues().add("activationSpecConfig", activationSpecConfig);
	}
{
		logger.info("Unregistering JMX-exposed beans on shutdown");
		unregisterNotificationListeners();
		unregisterBeans();
	}
{
		final Method testMethod = testContext.getTestMethod();
		final Class<?> testClass = testContext.getTestClass();
		Assert.notNull(testMethod, "The test method of the supplied TestContext must not be null");

		TransactionContext txContext = TransactionContextHolder.removeCurrentTransactionContext();
		if (txContext != null) {
			throw new IllegalStateException("Cannot start a new transaction without ending the existing transaction.");
		}

		PlatformTransactionManager tm = null;
		TransactionAttribute transactionAttribute = this.attributeSource.getTransactionAttribute(testMethod, testClass);

		if (transactionAttribute != null) {
			transactionAttribute = TestContextTransactionUtils.createDelegatingTransactionAttribute(testContext,
				transactionAttribute);

			if (logger.isDebugEnabled()) {
				logger.debug("Explicit transaction definition [" + transactionAttribute + "] found for test context "
						+ testContext);
			}

			if (transactionAttribute.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NOT_SUPPORTED) {
				return;
			}

			tm = getTransactionManager(testContext, transactionAttribute.getQualifier());
		}

		if (tm != null) {
			txContext = new TransactionContext(testContext, tm, transactionAttribute, isRollback(testContext));
			runBeforeTransactionMethods(testContext);
			txContext.startTransaction();
			TransactionContextHolder.setCurrentTransactionContext(txContext);
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
		MessageHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, MessageHeaderAccessor.class);
		if (accessor == null) {
			// Shouldn't happen (only broker broadcasts directly to clients)
			throw new IllegalStateException("No header accessor in " + message + ".");
		}
		StompHeaderAccessor stompAccessor;
		if (accessor instanceof StompHeaderAccessor) {
			stompAccessor = (StompHeaderAccessor) accessor;
		}
		else if (accessor instanceof SimpMessageHeaderAccessor) {
			stompAccessor = StompHeaderAccessor.wrap(message);
			if (SimpMessageType.CONNECT_ACK.equals(stompAccessor.getMessageType())) {
				stompAccessor = convertConnectAcktoStompConnected(stompAccessor);
			}
			else if (stompAccessor.getCommand() == null || StompCommand.SEND.equals(stompAccessor.getCommand())) {
				stompAccessor.updateStompCommandAsServerMessage();
			}
		}
		else {
			// Shouldn't happen (only broker broadcasts directly to clients)
			throw new IllegalStateException(
					"Unexpected header accessor type: " + accessor.getClass() + " in " + message + ".");
		}
		return stompAccessor;
	}
{
		return this.rootObject;
	}
{
		unregisterNotificationListeners();
		unregisterBeans();
		this.running = false;
	}
{

		this.uri = request.getURI();
		this.handshakeHeaders = request.getHeaders();
		this.principal = request.getPrincipal();
		this.localAddress = request.getLocalAddress();
		this.remoteAddress = request.getRemoteAddress();

		this.response = response;
		this.frameFormat = frameFormat;
		this.asyncRequestControl = request.getAsyncRequestControl(response);

		try {
			// Let "our" handler know before sending the open frame to the remote handler
			delegateConnectionEstablished();
			writePrelude(request, response);
			writeFrame(SockJsFrame.openFrame());
			if (isStreaming() && !isClosed()) {
				startAsyncRequest();
			}
		}
		catch (Throwable ex) {
			tryCloseWithSockJsTransportError(ex, CloseStatus.SERVER_ERROR);
			throw new SockJsTransportFailureException("Failed to open session", getId(), ex);
		}
	}
{

		synchronized (this.responseLock) {
			try {
				if (isClosed()) {
					response.getBody().write(SockJsFrame.closeFrameGoAway().getContentBytes());
				}
				this.response = response;
				this.frameFormat = frameFormat;
				this.asyncRequestControl = request.getAsyncRequestControl(response);
				writePrelude(request, response);
				startAsyncRequest();
			}
			catch (Throwable ex) {
				tryCloseWithSockJsTransportError(ex, CloseStatus.SERVER_ERROR);
				throw new SockJsTransportFailureException("Failed to handle SockJS receive request", getId(), ex);
			}
		}
	}
{
		do {
			String message = getMessageCache().poll();
			SockJsMessageCodec messageCodec = getSockJsServiceConfig().getMessageCodec();
			SockJsFrame frame = SockJsFrame.messageFrame(messageCodec, message);
			writeFrame(frame);

			this.byteCount += frame.getContentBytes().length + 1;
			if (logger.isTraceEnabled()) {
				logger.trace(this.byteCount + " bytes written so far, "
						+ getMessageCache().size() + " more messages not flushed");
			}
			if (this.byteCount >= getSockJsServiceConfig().getStreamBytesLimit()) {
				if (logger.isTraceEnabled()) {
					logger.trace("Streamed bytes limit reached. Recycling current request");
				}
				resetRequest();
				this.byteCount = 0;
				break;
			}
		} while (!getMessageCache().isEmpty());
		scheduleHeartbeat();
	}
{
		Assert.notNull(uri, "uri must not be null");
		String scheme = uri.getScheme();
		Assert.isTrue(scheme != null && ("ws".equals(scheme) || "wss".equals(scheme)), "Invalid scheme: " + scheme);
	}
{
		Description description = describeChild(frameworkMethod);
		if (isTestMethodIgnored(frameworkMethod)) {
			notifier.fireTestIgnored(description);
		}
		else {
			runLeaf(methodBlock(frameworkMethod), description, notifier);
		}
	}
{

		OutputStream stream = (this.updateContentLength ? createTemporaryOutputStream() : response.getOutputStream());

		Class<?> serializationView = (Class<?>)model.get(JsonView.class.getName());
		String jsonpParameterValue = getJsonpParameterValue(request);
		Object value = filterModel(model);
		if(serializationView != null || jsonpParameterValue != null) {
			MappingJacksonValue container = new MappingJacksonValue(value);
			container.setSerializationView(serializationView);
			container.setJsonpFunction(jsonpParameterValue);
			value = container;
		}

		writeContent(stream, value, this.jsonPrefix);
		if (this.updateContentLength) {
			writeToResponse(response, (ByteArrayOutputStream) stream);
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
		Class<?> serializationView = null;
		String jsonpFunction = null;
		if (value instanceof MappingJacksonValue) {
			MappingJacksonValue container = (MappingJacksonValue) value;
			value = container.getValue();
			serializationView = container.getSerializationView();
			jsonpFunction = container.getJsonpFunction();
		}
		if (jsonpFunction != null) {
			generator.writeRaw(jsonpFunction + "(" );
		}
		if (serializationView != null) {
			this.objectMapper.writerWithView(serializationView).writeValue(generator, value);
		}
		else {
			this.objectMapper.writeValue(generator, value);
		}
		if (jsonpFunction != null) {
			generator.writeRaw(");");
			generator.flush();
		}
	}
{
		if (this.dependencyComparator instanceof OrderProviderComparator) {
			((OrderProviderComparator) this.dependencyComparator)
					.sortArray(items, createFactoryAwareOrderProvider(matchingBeans));
		} else {
			Arrays.sort(items, this.dependencyComparator);
		}
	}
{
		if (this.dependencyComparator instanceof OrderProviderComparator) {
			((OrderProviderComparator) this.dependencyComparator)
					.sortList(items, createFactoryAwareOrderProvider(matchingBeans));
		} else {
			Collections.sort(items, this.dependencyComparator);
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
		return message;
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
		try {
			return handlerMethod.invoke(message, jmsMessage, session);
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
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);
		if (getHeaderInitializer() != null) {
			getHeaderInitializer().initHeaders(headerAccessor);
		}
		headerAccessor.setSessionId(session.getId());
		headerAccessor.setSessionAttributes(session.getAttributes());
		return MessageBuilder.createMessage(EMPTY_PAYLOAD, headerAccessor.getMessageHeaders());
	}
{
		DefaultMessageListenerContainer container = this.context.getBean(containerBeanName, DefaultMessageListenerContainer.class);
		return (BackOff) new DirectFieldAccessor(container).getPropertyValue("backOff");
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
		return wac;
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
		Boolean eligible = this.eligibleBeans.get(targetClass);
		if (eligible != null) {
			return eligible;
		}
		eligible = AopUtils.canApply(this.advisor, targetClass);
		this.eligibleBeans.put(targetClass, eligible);
		return eligible;
	}
{
		if (getHeaderInitializer() != null) {
			getHeaderInitializer().initHeaders(headerAccessor);
		}
	}
{
		MethodJmsListenerEndpoint endpoint = new MethodJmsListenerEndpoint();
		endpoint.setBean(sample);
		endpoint.setMethod(method);
		endpoint.setJmsHandlerMethodFactory(factory);
		return endpoint.createMessageListener(container);
	}
{
		ShallowEtagResponseWrapper responseWrapper =
				WebUtils.getNativeResponse(response, ShallowEtagResponseWrapper.class);
		Assert.notNull(responseWrapper, "ShallowEtagResponseWrapper not found");

		HttpServletResponse rawResponse = (HttpServletResponse) responseWrapper.getResponse();
		int statusCode = responseWrapper.getStatusCode();
		byte[] body = responseWrapper.toByteArray();

		if (rawResponse.isCommitted()) {
			if (body.length > 0) {
				StreamUtils.copy(body, rawResponse.getOutputStream());
			}
		}
		else if (isEligibleForEtag(request, responseWrapper, statusCode, body)) {
			String responseETag = generateETagHeaderValue(body);
			rawResponse.setHeader(HEADER_ETAG, responseETag);
			String requestETag = request.getHeader(HEADER_IF_NONE_MATCH);
			if (responseETag.equals(requestETag)) {
				if (logger.isTraceEnabled()) {
					logger.trace("ETag [" + responseETag + "] equal to If-None-Match, sending 304");
				}
				rawResponse.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			}
			else {
				if (logger.isTraceEnabled()) {
					logger.trace("ETag [" + responseETag + "] not equal to If-None-Match [" + requestETag +
							"], sending normal response");
				}
				if (body.length > 0) {
					rawResponse.setContentLength(body.length);
					StreamUtils.copy(body, rawResponse.getOutputStream());
				}
			}
		}
		else {
			if (logger.isTraceEnabled()) {
				logger.trace("Response with status code [" + statusCode + "] not eligible for ETag");
			}
			if (body.length > 0) {
				rawResponse.setContentLength(body.length);
				StreamUtils.copy(body, rawResponse.getOutputStream());
			}
		}
	}
{
		if (Object.class.getName().equals(typeName)) {
			return false;
		}
		else if (typeName.startsWith("java.")) {
			try {
				Class<?> clazz = getClass().getClassLoader().loadClass(typeName);
				return ((this.considerMetaAnnotations ? AnnotationUtils.getAnnotation(clazz, this.annotationType) :
						clazz.getAnnotation(this.annotationType)) != null);
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
		return this.locations;
	}
{
		this.headerInitializer = headerInitializer;
	}
{

		String destination = SimpMessageHeaderAccessor.getDestination(message.getHeaders());
		Assert.notNull(destination);

		long timeout = this.sendTimeout;
		boolean sent = (timeout >= 0)
				? this.messageChannel.send(message, timeout)
				: this.messageChannel.send(message);

		if (!sent) {
			throw new MessageDeliveryException(message,
					"Failed to send message to destination '" + destination + "' within timeout: " + timeout);
		}
	}
{
		Principal principal = SimpMessageHeaderAccessor.getUser(headers);
		if (principal == null) {
			throw new MissingSessionUserException(message);
		}
		if (principal instanceof DestinationUserNameProvider) {
			return ((DestinationUserNameProvider) principal).getDestinationUserName();
		}
		return principal.getName();
	}
{
		Assert.notNull(headers, "'headers' is required");
		Assert.notNull(payload, "'payload' is required");
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(128 + payload.length);
			DataOutputStream output = new DataOutputStream(baos);

			if (SimpMessageType.HEARTBEAT.equals(SimpMessageHeaderAccessor.getMessageType(headers))) {
				logger.trace("Encoded heartbeat");
				output.write(StompDecoder.HEARTBEAT_PAYLOAD);
			}
			else {
				StompCommand command = StompHeaderAccessor.getCommand(headers);
				Assert.notNull(command, "Missing STOMP command: " + headers);
				output.write(command.toString().getBytes(StompDecoder.UTF8_CHARSET));
				output.write(LF);
				writeHeaders(command, headers, payload, output);
				output.write(LF);
				writeBody(payload, output);
				output.write((byte) 0);
			}

			return baos.toByteArray();
		}
		catch (IOException e) {
			throw new StompConversionException("Failed to encode STOMP frame",  e);
		}
	}
{
		super(message);
		if (message != null) {
			@SuppressWarnings("unchecked")
			Map<String, List<String>> map = (Map<String, List<String>>) getHeader(NATIVE_HEADERS);
			if (map != null) {
				// Force removal since setHeader checks for equality
				removeHeader(NATIVE_HEADERS);
				setHeader(NATIVE_HEADERS, new LinkedMultiValueMap<String, String>(map));
			}
		}
	}
{
		ErrorHandler errorHandler = getErrorHandler();
		if (errorHandler != null) {
			try {
				listener.onApplicationEvent(event);
			}
			catch (Throwable err) {
				errorHandler.handleError(err);
			}
		}
		else {
			listener.onApplicationEvent(event);
		}
	}
{
		try {
			synchronized (this.documentBuilderFactoryMonitor) {
				if (this.documentBuilderFactory == null) {
					this.documentBuilderFactory = createDocumentBuilderFactory();
				}
			}
			DocumentBuilder documentBuilder = createDocumentBuilder(this.documentBuilderFactory);
			return documentBuilder.newDocument();
		}
		catch (ParserConfigurationException ex) {
			throw new UnmarshallingFailureException(
					"Could not create document placeholder for DOMSource: " + ex.getMessage(), ex);
		}
	}
{
		this.cacheManager = config.cacheManager();
		this.keyGenerator = config.keyGenerator();
	}
{
		return (element != null ? new SimpleValueWrapper(element.getObjectValue()) : null);
	}
{
		return (value != null ? new SimpleValueWrapper(fromStoreValue(value)) : null);
	}
{
		return (value != null ? new SimpleValueWrapper(fromStoreValue(value)) : null);
	}
{
		StaticApplicationContext context = new StaticApplicationContext();
		BeanDefinition targetDefinition =
				new RootBeanDefinition(AsyncAnnotationBeanPostProcessorTests.TestBean.class);
		context.registerBeanDefinition("postProcessor", asyncAnnotationBeanPostProcessorDefinition);
		context.registerBeanDefinition("target", targetDefinition);
		context.refresh();
		return context;
	}
{
		if (channelName.equals("brokerChannel")) {
			return null;
		}
		RootBeanDefinition executorDef = new RootBeanDefinition(ThreadPoolTaskExecutor.class);
		executorDef.getPropertyValues().add("corePoolSize", Runtime.getRuntime().availableProcessors() * 2);
		executorDef.getPropertyValues().add("maxPoolSize", Integer.MAX_VALUE);
		executorDef.getPropertyValues().add("queueCapacity", Integer.MAX_VALUE);
		return executorDef;
	}
{
		return this.cacheMap.get(name);
	}
{
		String highestPriorityBeanName = null;
		Integer highestPriority = null;
		for (Map.Entry<String, Object> entry : candidateBeans.entrySet()) {
			String candidateBeanName = entry.getKey();
			Object beanInstance = entry.getValue();
			Integer candidatePriority = getPriority(beanInstance);
			if (candidatePriority != null) {
				if (highestPriorityBeanName != null) {
					if (candidatePriority.equals(highestPriority)) {
						throw new NoUniqueBeanDefinitionException(requiredType, candidateBeans.size(),
								"Multiple beans found with the same priority ('" + highestPriority + "') " +
										"among candidates: " + candidateBeans.keySet());
					} else if (candidatePriority > highestPriority) {
						highestPriorityBeanName = candidateBeanName;
						highestPriority = candidatePriority;
					}
				} else {
					highestPriorityBeanName = candidateBeanName;
					highestPriority = candidatePriority;
				}
			}
		}
		return highestPriorityBeanName;
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
		try {
			Object jobObject = createJobInstance(bundle);
			return adaptJob(jobObject);
		}
		catch (Exception ex) {
			throw new SchedulerException("Job instantiation failed", ex);
		}
	}
{
		try {
			this.eventPublisher.publishEvent(event);
		}
		catch (Throwable ex) {
			logger.error("Failed to publish event " + event, ex);
		}
	}
{
		if (headers.containsKey(StompHeaderAccessor.STOMP_CONTENT_LENGTH_HEADER)) {
			String rawContentLength = headers.getFirst(StompHeaderAccessor.STOMP_CONTENT_LENGTH_HEADER);
			try {
				return Integer.valueOf(rawContentLength);
			}
			catch (NumberFormatException ex) {
				logger.warn("Ignoring invalid content-length header value: '" + rawContentLength + "'");
			}
		}
		return null;
	}
{
		this.sessions.remove(session.getId());
		findProtocolHandler(session).afterSessionEnded(session, closeStatus, this.clientInboundChannel);
	}
{

		Payload annot = param.getParameterAnnotation(Payload.class);
		if ((annot != null) && StringUtils.hasText(annot.value())) {
			throw new IllegalStateException("@Payload SpEL expressions not supported by this resolver.");
		}

		Object payload = message.getPayload();

		if (isEmptyPayload(payload)) {
			if (annot == null || annot.required()) {
				String paramName = getParameterName(param);
				BindingResult bindingResult = new BeanPropertyBindingResult(payload, paramName);
				bindingResult.addError(new ObjectError(paramName, "@Payload param is required"));
				throw new MethodArgumentNotValidException(message, param, bindingResult);
			}
			else {
				return null;
			}
		}

		Class<?> targetClass = param.getParameterType();
		if (ClassUtils.isAssignable(targetClass, payload.getClass())) {
			validate(message, param, payload);
			return payload;
		}
		else {
			payload = this.converter.fromMessage(message, targetClass);
			if (payload == null) {
				throw new MessageConversionException(message,
						"No converter found to convert to " + targetClass + ", message=" + message, null);
			}
			validate(message, param, payload);
			return payload;
		}
	}
{
		String paramName = param.getParameterName();
		return (paramName == null ? "Arg " + param.getParameterIndex() : paramName);
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
        int charLength = s.length();
        int byteLength = i;
        char c;
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
        if (byteLength > maxByteLength) {
            throw new IllegalArgumentException();
        }
        int start = length - i - 2;
        if (start >= 0) {
          data[start] = (byte) (byteLength >>> 8);
          data[start + 1] = (byte) byteLength;
        }
        if (length + byteLength - i > data.length) {
            enlarge(byteLength - i);
        }
        int len = length;
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
        length = len;
        return this;
    }
{
		Assert.state(dataSource != null, "DataSource must be set");
		if (this.enabled && populator != null) {
			DatabasePopulatorUtils.execute(populator, this.dataSource);
		}
	}
{
		splitSqlScript(null, script, separator, DEFAULT_COMMENT_PREFIX, DEFAULT_BLOCK_COMMENT_START_DELIMITER,
			DEFAULT_BLOCK_COMMENT_END_DELIMITER, statements);
	}
{

		if (logger.isInfoEnabled()) {
			logger.info("Executing SQL script from " + resource);
		}
		long startTime = System.currentTimeMillis();
		List<String> statements = new LinkedList<String>();
		String script;
		try {
			script = readScript(resource, commentPrefix, separator);
		}
		catch (IOException ex) {
			throw new CannotReadScriptException(resource, ex);
		}

		if (separator == null) {
			separator = DEFAULT_STATEMENT_SEPARATOR;
			if (!containsSqlScriptDelimiters(script, separator)) {
				separator = "\n";
			}
		}

		splitSqlScript(resource, script, separator, commentPrefix, blockCommentStartDelimiter,
			blockCommentEndDelimiter, statements);
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
							logger.debug("Failed to execute SQL script statement at line " + lineNumber
									+ " of resource " + resource + ": " + statement, ex);
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
			logger.info("Executed SQL script from " + resource + " in " + elapsedTime + " ms.");
		}
	}
{
		Statement statement = null;
		try {
			statement = connection.createStatement();
			final Statement stmt = statement; 
			for (Resource script : this.scripts) {
				ScriptUtils.executeSqlScript(
						new ScriptStatementExecutor() {
							
							@Override
							public int executeScriptStatement(String statement) throws DataAccessException {
								try {
									stmt.execute(statement);
									return stmt.getUpdateCount();
								}
								catch (SQLException e) {
									throw new UncategorizedSQLException(getClass().getName(), statement, e);
								}
							}
						}, 
						applyEncodingIfNecessary(script), this.continueOnError, this.ignoreFailedDrops, 
						this.commentPrefix, this.separator,	this.blockCommentStartDelimiter, 
						this.blockCommentEndDelimiter);
			}
		}
		finally {
			if (statement != null) {
				statement.close();
			}
		}
	}
{
		resetResponse();
		handleRequest(httpMethod, uri, httpStatus);
	}
{
		List<String> matches = new ArrayList<String>();
		for (String pattern : this.patterns) {
			String match = getMatchingPattern(pattern, lookupPath);
			if (match != null) {
				matches.add(match);
			}
		}
		Collections.sort(matches, this.pathMatcher.getPatternComparator(lookupPath));
		return matches;
	}
{
		return this.strictContentTypeMatch;
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
		for (Annotation composedAnnotation : clazz.getDeclaredAnnotations()) {
			if (visited.add(composedAnnotation)) {
				AnnotationDescriptor<T> descriptor = findAnnotationDescriptor(composedAnnotation.annotationType(),
					visited, annotationType);
				if (descriptor != null) {
					return new AnnotationDescriptor<T>(clazz, descriptor.getDeclaringClass(), composedAnnotation,
						descriptor.getAnnotation());
				}
			}
		}

		// Declared on a superclass?
		return findAnnotationDescriptor(clazz.getSuperclass(), visited, annotationType);
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
		for (Annotation composedAnnotation : clazz.getDeclaredAnnotations()) {
			if (visited.add(composedAnnotation)) {
				UntypedAnnotationDescriptor descriptor = findAnnotationDescriptorForTypes(
					composedAnnotation.annotationType(), visited, annotationTypes);
				if (descriptor != null) {
					return new UntypedAnnotationDescriptor(clazz, descriptor.getDeclaringClass(), composedAnnotation,
						descriptor.getAnnotation());
				}
			}
		}

		// Declared on a superclass?
		return findAnnotationDescriptorForTypes(clazz.getSuperclass(), visited, annotationTypes);
	}
{
		assertAtComponentOnComposedAnnotationForMultipleCandidateTypes(startClass, startClass, name,
			composedAnnotationType);
	}
{
		Class<Component> annotationType = Component.class;
		UntypedAnnotationDescriptor descriptor = findAnnotationDescriptorForTypes(startClass, Service.class,
			annotationType, Order.class, Transactional.class);
		assertNotNull("UntypedAnnotationDescriptor should not be null", descriptor);
		assertEquals("rootDeclaringClass", rootDeclaringClass, descriptor.getRootDeclaringClass());
		assertEquals("declaringClass", declaringClass, descriptor.getDeclaringClass());
		assertEquals("annotationType", annotationType, descriptor.getAnnotationType());
		assertEquals("component name", name, ((Component) descriptor.getAnnotation()).value());
		assertNotNull("composedAnnotation should not be null", descriptor.getComposedAnnotation());
		assertEquals("composedAnnotationType", composedAnnotationType, descriptor.getComposedAnnotationType());
	}
{
		Assert.notNull(clazz, "Class must not be null");

		A annotation = clazz.getAnnotation(annotationType);
		if (annotation != null) {
			return annotation;
		}
		for (Class<?> ifc : clazz.getInterfaces()) {
			annotation = findAnnotation(ifc, annotationType, visitedAnnotations);
			if (annotation != null) {
				return annotation;
			}
		}
		for (Annotation ann : clazz.getAnnotations()) {
			if (!visitedAnnotations.contains(ann)) {
				visitedAnnotations.add(ann);
				annotation = findAnnotation(ann.annotationType(), annotationType, visitedAnnotations);
				if (annotation != null) {
					return annotation;
				}
			}
		}
		Class<?> superclass = clazz.getSuperclass();
		if (superclass == null || superclass.equals(Object.class)) {
			return null;
		}
		return findAnnotation(superclass, annotationType, visitedAnnotations);
	}
{
		Class<?> handlerType =
				(handler instanceof String ? getApplicationContext().getType((String) handler) : handler.getClass());

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

		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(message);
		DestinationInfo info = parseUserDestination(headers);
		if (info == null) {
			return null;
		}

		Set<String> targetDestinations = new HashSet<String>();
		for (String sessionId : info.getSessionIds()) {
			targetDestinations.add(getTargetDestination(
					headers.getDestination(), info.getDestinationWithoutPrefix(), sessionId, info.getUser()));
		}

		return new UserDestinationResult(headers.getDestination(),
				targetDestinations, info.getSubscribeDestination(), info.getUser());
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
		Class<?>[] targetInterfaces = ClassUtils.getAllInterfacesForClass(beanClass, this.proxyClassLoader);
		boolean hasReasonableProxyInterface = false;
		for (Class<?> ifc : targetInterfaces) {
			if (!isConfigurationCallbackInterface(ifc) && ifc.getMethods().length > 0) {
				hasReasonableProxyInterface = true;
				break;
			}
		}
		if (hasReasonableProxyInterface) {
			// Must allow for introductions; can't just set interfaces to the target's interfaces only.
			for (Class<?> ifc : targetInterfaces) {
				proxyFactory.addInterface(ifc);
			}
		}
		else {
			proxyFactory.setProxyTargetClass(true);
		}
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
		return match;
	}
{
		try {
			return "true".equalsIgnoreCase(System.getProperty(IGNORE_GETENV_PROPERTY_NAME));
		}
		catch (Throwable ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("Could not obtain system property '" + IGNORE_GETENV_PROPERTY_NAME + "': " + ex);
			}
			return false;
		}
	}
{
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
		ResourceUtils.useCachesIfNecessary(con);
		if (con instanceof HttpURLConnection) {
			customizeConnection((HttpURLConnection) con);
		}
	}
{
		con.setRequestMethod("HEAD");
	}
{
		try {
			Class<?> clazz = ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
			Assert.isAssignable(ApplicationContextInitializer.class, clazz);
			return (Class<ApplicationContextInitializer<ConfigurableApplicationContext>>) clazz;
		}
		catch (ClassNotFoundException ex) {
			throw new ApplicationContextException("Failed to load context initializer class [" + className + "]", ex);
		}
	}
{
		try {
			Class<?> initializerClass = ClassUtils.forName(className, wac.getClassLoader());
			Class<?> initializerContextClass =
					GenericTypeResolver.resolveTypeArgument(initializerClass, ApplicationContextInitializer.class);
			if (initializerContextClass != null) {
				Assert.isAssignable(initializerContextClass, wac.getClass(), String.format(
						"Could not add context initializer [%s] since its generic parameter [%s] " +
						"is not assignable from the type of application context used by this " +
						"framework servlet [%s]: ", initializerClass.getName(), initializerContextClass.getName(),
						wac.getClass().getName()));
			}
			return BeanUtils.instantiateClass(initializerClass, ApplicationContextInitializer.class);
		}
		catch (Exception ex) {
			throw new IllegalArgumentException(String.format("Could not instantiate class [%s] specified " +
					"via 'contextInitializerClasses' init-param", className), ex);
		}
	}
{
		DefaultContentTypeResolver contentTypeResolver = new DefaultContentTypeResolver();
		if (jackson2Present) {
			contentTypeResolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
		}
		return contentTypeResolver;
	}
{
		this.cachePatterns = false;
		this.tokenizedPatternCache.clear();
		this.stringMatcherCache.clear();
	}
{
		if (this.emptyElement) {
			this.emptyElement = false;
			writeEndElement();
		}
	}
{
		Assert.notNull(methodParameter, "MethodParameter must not be null");
		ResolvableType owner = forType(methodParameter.getContainingClass()).as(methodParameter.getDeclaringClass());
		return forType(targetType, new MethodParameterTypeProvider(methodParameter),
				owner.asVariableResolver()).getNested(methodParameter.getNestingLevel(),
				methodParameter.typeIndexesPerLevel);
	}
{

		String beanDefinitionName = BeanFactoryUtils.transformedBeanName(beanName);
		if (containsBeanDefinition(beanDefinitionName)) {
			return isAutowireCandidate(beanName, getMergedLocalBeanDefinition(beanDefinitionName), descriptor, resolver);
		}
		else if (containsSingleton(beanName)) {
			return isAutowireCandidate(beanName, new RootBeanDefinition(getType(beanName)), descriptor, resolver);
		}
		else if (getParentBeanFactory() instanceof DefaultListableBeanFactory) {
			// No bean definition found in this factory -> delegate to parent.
			return ((DefaultListableBeanFactory) getParentBeanFactory()).isAutowireCandidate(beanName, descriptor, resolver);
		}
		else if (getParentBeanFactory() instanceof ConfigurableListableBeanFactory) {
			// If no DefaultListableBeanFactory, can't pass the resolver along.
			return ((ConfigurableListableBeanFactory) getParentBeanFactory()).isAutowireCandidate(beanName, descriptor);
		}
		else {
			return true;
		}
	}
{
		Method method = this.exceptionLookupCache.get(exceptionType);
		if (method == null) {
			method = getMappedMethod(exceptionType);
			this.exceptionLookupCache.put(exceptionType, method != null ? method : NO_METHOD_FOUND);
		}
		return method != NO_METHOD_FOUND ? method : null;
	}
{
		Method resolvedFactoryMethod = bd.getResolvedFactoryMethod();
		return (resolvedFactoryMethod != null ? AnnotationUtils.getAnnotation(resolvedFactoryMethod, type) : null);
	}
{
		// Should typically be set for any kind of factory method, since the BeanFactory
		// pre-resolves them before reaching out to the AutowireCandidateResolver...
		Class<?> preResolved = rbd.resolvedFactoryMethodReturnType;
		if (preResolved != null) {
			return ResolvableType.forClass(preResolved);
		}
		else {
			Method resolvedFactoryMethod = rbd.getResolvedFactoryMethod();
			if (resolvedFactoryMethod != null) {
				if (descriptor.getDependencyType().isAssignableFrom(resolvedFactoryMethod.getReturnType())) {
					// Only use factory method metadata if the return type is actually expressive enough
					// for our dependency. Otherwise, the returned instance type may have matched instead
					// in case of a singleton instance having been registered with the container already.
					return ResolvableType.forMethodReturnType(resolvedFactoryMethod);
				}
			}
			return null;
		}
	}
{
		loadBeanDefinitions(xml, 13);

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

		if (!ObjectUtils.isEmpty(value) && !ObjectUtils.isEmpty(locations)) {
			String msg = String.format("Test class [%s] has been configured with @ContextConfiguration's 'value' %s "
					+ "and 'locations' %s attributes. Only one declaration of resource "
					+ "locations is permitted per @ContextConfiguration annotation.", declaringClass.getName(),
				ObjectUtils.nullSafeToString(value), ObjectUtils.nullSafeToString(locations));
			logger.error(msg);
			throw new IllegalStateException(msg);
		}
		else if (!ObjectUtils.isEmpty(value)) {
			locations = value;
		}

		return locations;
	}
{
		assertEquals(2, hierarchyAttributes.size());

		List<ContextConfigurationAttributes> configAttributesListClassLevel1 = hierarchyAttributes.get(0);
		List<ContextConfigurationAttributes> configAttributesListClassLevel2 = hierarchyAttributes.get(1);
		debugConfigAttributes(configAttributesListClassLevel1);
		debugConfigAttributes(configAttributesListClassLevel2);

		assertEquals(1, configAttributesListClassLevel1.size());
		assertThat(configAttributesListClassLevel1.get(0).getLocations()[0], equalTo("one.xml"));

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
		assertEquals(invokedInTx, instance.invoked);
	}
{
		final CountDownLatch latch = new CountDownLatch(1);
		((MockAsyncContext) request.getAsyncContext()).addDispatchHandler(new Runnable() {
			@Override
			public void run() {
				latch.countDown();
			}
		});
		getMvcResult(request).setAsyncResultLatch(latch);
	}
{
		this.decoder.apply(buffer);
		if (consumer.arguments.isEmpty()) {
			return null;
		} else {
			return consumer.arguments.get(0);
		}
	}
{
		if (destination == null) {
			logger.trace("Ignoring message, no destination");
			return false;
		}
		if (!destination.startsWith(requiredPrefix)) {
			if (logger.isTraceEnabled()) {
				logger.trace("Ignoring message to " + destination + ", not a \"user\" destination");
			}
			return false;
		}
		return true;
	}
{
		if (isInvalid()) {
			throw new IllegalStateException("The session has already been invalidated");
		}
	}
{
		AntPathStringMatcher matcher = null;
		Boolean cachePatterns = this.cachePatterns;
		if (cachePatterns == null || cachePatterns.booleanValue()) {
			matcher = this.stringMatcherCache.get(pattern);
		}
		if (matcher == null) {
			matcher = new AntPathStringMatcher(pattern);
			if (cachePatterns == null && this.stringMatcherCache.size() == CACHE_TURNOFF_THRESHOLD) {
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
		if (type == null && typeProvider != null) {
			type = SerializableTypeWrapper.forTypeProvider(typeProvider);
		}
		if (type == null) {
			return NONE;
		}
		// Check the cache, we may have a ResolvableType that may have already been resolved
		ResolvableType key = new ResolvableType(type, typeProvider, variableResolver, null);
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
			resolvedGenerics[i] = generics[i].resolve(fallback);
		}
		return resolvedGenerics;
	}
{
		listener.zeroCounter();
		parentListener.zeroCounter();
		assertTrue("0 events before publication", listener.getEventCount() == 0);
		assertTrue("0 parent events before publication", parentListener.getEventCount() == 0);
		this.applicationContext.publishEvent(event);
		assertTrue("1 events after publication, not " + listener.getEventCount(), listener.getEventCount() == 1);
		assertTrue("1 parent events after publication", parentListener.getEventCount() == 1);
	}
{
		Method[] methods = getSortedClassMethods(clazz);
		for (String methodSuffix : methodSuffixes) {
			for (String prefix : prefixes) {
				for (Method method : methods) {
					if (method.getName().equals(prefix + methodSuffix)
							&& method.getParameterTypes().length == numberOfParams
							&& (!mustBeStatic || Modifier.isStatic(method.getModifiers()))) {
						return method;
					}
				}
			}
		}
		return null;

	}
{
		Assert.hasText(beanName, "'beanName' must not be null");
		Assert.notNull(beanFactory, "'beanFactory' must not be null");
		Assert.isTrue(beanFactory.containsBean(beanName),
				"Bean factory [" + beanFactory + "] does not contain bean " + "with name [" + beanName + "]");

		this.bean = beanName;
		this.beanFactory = beanFactory;

		Class<?> beanType = this.beanFactory.getType(beanName);
		this.order = initOrderFromBeanType(beanType);

		ControllerAdvice annotation = AnnotationUtils.findAnnotation(beanType,ControllerAdvice.class);
		Assert.notNull(annotation, "BeanType [" + beanType.getName() + "] is not annotated @ControllerAdvice");

		this.basePackages.addAll(initBasePackagesFromBeanType(beanType, annotation));
		this.annotations.addAll(Arrays.asList(annotation.annotations()));
		this.assignableTypes.addAll(Arrays.asList(annotation.assignableTypes()));
	}
{
		Assert.notNull(bean, "'bean' must not be null");
		this.bean = bean;
		this.order = initOrderFromBean(bean);

		Class<? extends Object> beanType = bean.getClass();
		ControllerAdvice annotation = AnnotationUtils.findAnnotation(beanType,ControllerAdvice.class);
		Assert.notNull(annotation, "BeanType [" + beanType.getName() + "] is not annotated @ControllerAdvice");

		this.basePackages.addAll(initBasePackagesFromBeanType(beanType, annotation));
		this.annotations.addAll(Arrays.asList(annotation.annotations()));
		this.assignableTypes.addAll(Arrays.asList(annotation.assignableTypes()));
		this.beanFactory = null;
	}
{

		D destination = resolveDestination(destinationName);
		super.convertAndSend(destination, payload, headers, postProcessor);
	}
{
		ResolvableType type = ResolvableType.forClass(clazz).as(genericIfc);
		if(!type.hasGenerics()) {
			return null;
		}
		return type.resolveGenerics();
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
		addScalarConverters(converterRegistry);
		addCollectionConverters(converterRegistry);

		converterRegistry.addConverter(new ByteBufferConverter((ConversionService) converterRegistry));
		if (zoneIdAvailable) {
			ZoneIdConverterRegistrar.registerZoneIdConverters(converterRegistry);
		}

		converterRegistry.addConverter(new ObjectToObjectConverter());
		converterRegistry.addConverter(new IdToEntityConverter((ConversionService) converterRegistry));
		converterRegistry.addConverter(new FallbackObjectToStringConverter());
	}
{
		if (request.getAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME) == null) {
			// Retrieve and parse cookie value.
			Cookie cookie = WebUtils.getCookie(request, getCookieName());
			Locale locale = null;
			TimeZone timeZone = null;
			if (cookie != null) {
				String value = cookie.getValue();
				String localePart = value;
				String timeZonePart = null;
				int spaceIndex = localePart.indexOf(' ');
				if (spaceIndex != -1) {
					localePart = value.substring(0, spaceIndex);
					timeZonePart = value.substring(spaceIndex + 1);
				}
				locale = (!"-".equals(localePart) ? StringUtils.parseLocaleString(localePart) : null);
				if (timeZonePart != null) {
					timeZone = StringUtils.parseTimeZoneString(timeZonePart);
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Parsed cookie value [" + cookie.getValue() + "] into locale '" + locale +
							"'" + (timeZone != null ? " and time zone '" + timeZone.getID() + "'" : ""));
				}
			}
			request.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME,
					(locale != null ? locale: determineDefaultLocale(request)));
			request.setAttribute(TIME_ZONE_REQUEST_ATTRIBUTE_NAME,
					(timeZone != null ? timeZone : determineDefaultTimeZone(request)));
		}
	}
{
		Locale locale = null;
		TimeZone timeZone = null;
		if (localeContext != null) {
			locale = localeContext.getLocale();
			if (localeContext instanceof TimeZoneAwareLocaleContext) {
				timeZone = ((TimeZoneAwareLocaleContext) localeContext).getTimeZone();
			}
			addCookie(response, (locale != null ? locale : "-") + (timeZone != null ? ' ' + timeZone.getID() : ""));
		}
		else {
			removeCookie(response);
		}
		request.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME,
				(locale != null ? locale: determineDefaultLocale(request)));
		request.setAttribute(TIME_ZONE_REQUEST_ATTRIBUTE_NAME,
				(timeZone != null ? timeZone : determineDefaultTimeZone(request)));
	}
{
		this.relay = new StompBrokerRelayMessageHandler(this.responseChannel, Arrays.asList("/queue/", "/topic/"));
		this.relay.setRelayPort(port);
		this.relay.setApplicationEventPublisher(this.eventPublisher);

		this.eventPublisher.expect(true);
		this.relay.start();
		this.eventPublisher.awaitAndAssert();
	}
{
		if (this.knownSockJsPrefixes.size() > MAX_KNOWN_SOCKJS_PREFIX_COUNT) {
			String removed = this.knownSockJsPrefixes.remove(0);
			if (logger.isWarnEnabled()) {
				logger.warn("MAX_KNOWN_SOCKJS_PREFIX_COUNT reached, removed prefix " + removed);
			}
		}
		this.knownSockJsPrefixes.add(path);
	}
{
			this.stompConnection.setReady();
			publishBrokerAvailableEvent();
		}
{
			this.stompConnection.setDisconnected();
			sendError(errorMessage);
			publishBrokerUnavailableEvent();
		}
{
		Principal principal = session.getPrincipal();
		if (principal != null) {
			headers.setNativeHeader(CONNECTED_USER_HEADER, principal.getName());
			headers.setNativeHeader(QUEUE_SUFFIX_HEADER, session.getId());

			if (this.queueSuffixResolver != null) {
				String suffix = session.getId();
				this.queueSuffixResolver.addQueueSuffix(principal.getName(), session.getId(), suffix);
			}
		}
	}
{
		this.activeMQBroker = new BrokerService();
		this.activeMQBroker.addConnector("stomp://localhost:" + port);
		this.activeMQBroker.setStartAsync(false);
		this.activeMQBroker.setDeleteAllMessagesOnStartup(true);
		this.activeMQBroker.start();
	}
{

		enhancer.setInterceptDuringConstruction(false);
		enhancer.setCallbacks(callbacks);

		return this.constructorArgs == null ? enhancer.create() : enhancer.create(
				this.constructorArgTypes, this.constructorArgs);
	}
{

		Assert.isTrue(request instanceof ServletServerHttpRequest);
		HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();

		Assert.isTrue(response instanceof ServletServerHttpResponse);
		HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();

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
		List<String> protocols = handler.getSupportedProtocols();
		if (CollectionUtils.isEmpty(protocols)) {
			logger.warn("No sub-protocols, ignoring handler " + handler);
			return;
		}
		for (String protocol: protocols) {
			SubProtocolHandler replaced = this.protocolHandlers.put(protocol, handler);
			if ((replaced != null) && (replaced != handler) ) {
				throw new IllegalStateException("Failed to map handler " + handler
						+ " to protocol '" + protocol + "', it is already mapped to handler " + replaced);
			}
		}
	}
{
		if (metadata.isAnnotated(Lazy.class.getName())) {
			abd.setLazyInit(attributesFor(metadata, Lazy.class).getBoolean("value"));
		}
		else if (abd.getMetadata().isAnnotated(Lazy.class.getName())) {
			abd.setLazyInit(attributesFor(abd.getMetadata(), Lazy.class).getBoolean("value"));
		}

		if (metadata.isAnnotated(Primary.class.getName())) {
			abd.setPrimary(true);
		}
		if (metadata.isAnnotated(DependsOn.class.getName())) {
			abd.setDependsOn(attributesFor(metadata, DependsOn.class).getStringArray("value"));
		}

		if (abd instanceof AbstractBeanDefinition) {
			AbstractBeanDefinition absBd = (AbstractBeanDefinition) abd;
			if (metadata.isAnnotated(Role.class.getName())) {
				absBd.setRole(attributesFor(metadata, Role.class).getNumber("value").intValue());
			}
			if (metadata.isAnnotated(Description.class.getName())) {
				absBd.setDescription(attributesFor(metadata, Description.class).getString("value"));
			}
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
		String attribute = "javax.websocket.server.ServerContainer";
		ServletContext servletContext = servletRequest.getServletContext();
		return (WsServerContainer) servletContext.getAttribute(attribute);
	}
{
		RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
		rbta.setPropagationBehaviorName(
				RuleBasedTransactionAttribute.PREFIX_PROPAGATION + attributes.getEnum("value").toString());
		ArrayList<RollbackRuleAttribute> rollBackRules = new ArrayList<RollbackRuleAttribute>();
		Class[] rbf = attributes.getClassArray("rollbackOn");
		for (Class rbRule : rbf) {
			RollbackRuleAttribute rule = new RollbackRuleAttribute(rbRule);
			rollBackRules.add(rule);
		}
		Class[] nrbf = attributes.getClassArray("dontRollbackOn");
		for (Class rbRule : nrbf) {
			NoRollbackRuleAttribute rule = new NoRollbackRuleAttribute(rbRule);
			rollBackRules.add(rule);
		}
		rbta.getRollbackRules().addAll(rollBackRules);
		return rbta;
	}
{
		RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
		Propagation propagation = attributes.getEnum("propagation");
		rbta.setPropagationBehavior(propagation.value());
		Isolation isolation = attributes.getEnum("isolation");
		rbta.setIsolationLevel(isolation.value());
		rbta.setTimeout(attributes.getNumber("timeout").intValue());
		rbta.setReadOnly(attributes.getBoolean("readOnly"));
		rbta.setQualifier(attributes.getString("value"));
		ArrayList<RollbackRuleAttribute> rollBackRules = new ArrayList<RollbackRuleAttribute>();
		Class[] rbf = attributes.getClassArray("rollbackFor");
		for (Class rbRule : rbf) {
			RollbackRuleAttribute rule = new RollbackRuleAttribute(rbRule);
			rollBackRules.add(rule);
		}
		String[] rbfc = attributes.getStringArray("rollbackForClassName");
		for (String rbRule : rbfc) {
			RollbackRuleAttribute rule = new RollbackRuleAttribute(rbRule);
			rollBackRules.add(rule);
		}
		Class[] nrbf = attributes.getClassArray("noRollbackFor");
		for (Class rbRule : nrbf) {
			NoRollbackRuleAttribute rule = new NoRollbackRuleAttribute(rbRule);
			rollBackRules.add(rule);
		}
		String[] nrbfc = attributes.getStringArray("noRollbackForClassName");
		for (String rbRule : nrbfc) {
			NoRollbackRuleAttribute rule = new NoRollbackRuleAttribute(rbRule);
			rollBackRules.add(rule);
		}
		rbta.getRollbackRules().addAll(rollBackRules);
		return rbta;
	}
{

			if (logger.isTraceEnabled()) {
				logger.trace("Forwarding to STOMP broker, message: " + message);
			}

			byte[] bytes = stompMessageConverter.fromMessage(message);
			String payload = new String(bytes, Charset.forName("UTF-8"));

			final Deferred<Boolean, Promise<Boolean>> deferred = new DeferredPromiseSpec<Boolean>().get();
			tcpConnection.send(payload, new Consumer<Boolean>() {
				@Override
				public void accept(Boolean success) {
					deferred.accept(success);
				}
			});

			Boolean success = null;
			try {
				success = deferred.compose().await();
				if (success == null) {
					handleTcpClientFailure("Timed out waiting for message to be forwarded to the broker", null);
				}
				else if (!success) {
					if (StompHeaderAccessor.wrap(message).getCommand() != StompCommand.DISCONNECT) {
						handleTcpClientFailure("Failed to forward message to the broker", null);
					}
				}
			}
			catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
				handleTcpClientFailure("Interrupted while forwarding message to the broker", ex);
			}
			return (success != null) ? success : false;
		}
{
			this.isConnected = false;
			this.connection.close();
			this.connection = null;

			brokerUnavailable();
		}
{
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
	}
{
		this.servletRequest = new MockHttpServletRequest();
		this.servletRequest.setAsyncSupported(true);
		this.request = new ServletServerHttpRequest(this.servletRequest);
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
					if (resolveNestedPlaceholders && value instanceof String) {
						value = resolveNestedPlaceholders((String) value);
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
					return this.conversionService.convert(value, targetValueType);
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
			if (valueType != null) {
				return valueType;
			}
		}
		return null;
	}
{
		Class<?> paramType = parameter.getParameterType();
		if (Collection.class.equals(paramType) || List.class.isAssignableFrom(paramType)){
			Class<?> valueType = GenericCollectionTypeResolver.getCollectionParameterType(parameter);
			if (valueType != null) {
				return valueType;
			}
		}
		return null;
	}
{

		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(message);
		SimpMessageType messageType = headers.getMessageType();
		String destination = headers.getDestination();

		if (!SimpMessageType.MESSAGE.equals(messageType)) {
			return;
		}

		if (!checkDestination(destination)) {
			return;
		}

		if (logger.isTraceEnabled()) {
			logger.trace("Processing message to destination " + destination);
		}

		UserDestinationParser destinationParser = new UserDestinationParser(destination);
		String user = destinationParser.getUser();

		if (user == null) {
			if (logger.isErrorEnabled()) {
				logger.error("Ignoring message, expected destination pattern \"" + this.destinationPrefix
						+ "{userId}/**\": " + destination);
			}
			return;
		}

		for (String sessionId : this.userQueueSuffixResolver.getUserQueueSuffixes(user)) {

			String targetDestination = destinationParser.getTargetDestination(sessionId);
			headers.setDestination(targetDestination);
			message = MessageBuilder.withPayloadAndHeaders(message.getPayload(), headers).build();

			if (logger.isTraceEnabled()) {
				logger.trace("Sending message to resolved target destination " + targetDestination);
			}
			this.messagingTemplate.send(targetDestination, message);
		}
	}
{

		StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);
		String sessionId = headers.getSessionId();
		String destination = headers.getDestination();
		StompCommand command = headers.getCommand();
		SimpMessageType messageType = headers.getMessageType();

		if (!this.running) {
			if (logger.isTraceEnabled()) {
				logger.trace("STOMP broker relay not running. Ignoring message id=" + headers.getId());
			}
			return;
		}

		if (SimpMessageType.MESSAGE.equals(messageType)) {
			sessionId = (sessionId == null) ? STOMP_RELAY_SYSTEM_SESSION_ID : sessionId;
			headers.setSessionId(sessionId);
			command = (command == null) ? StompCommand.SEND : command;
			headers.setCommandIfNotSet(command);
			message = MessageBuilder.withPayloadAndHeaders(message.getPayload(), headers).build();
		}

		if (headers.getCommand() == null) {
			logger.error("Ignoring message, no STOMP command: " + message);
			return;
		}
		if (sessionId == null) {
			logger.error("Ignoring message, no sessionId: " + message);
			return;
		}
		if (command.requiresDestination() && (destination == null)) {
			logger.error("Ignoring " + command + " message, no destination: " + message);
			return;
		}

		try {
			if ((destination == null) || supportsDestination(destination)) {

				if (logger.isTraceEnabled()) {
					logger.trace("Processing message: " + message);
				}

				if (SimpMessageType.CONNECT.equals(messageType)) {
					headers.setHeartbeat(0, 0); // TODO: disable for now
					message = MessageBuilder.withPayloadAndHeaders(message.getPayload(), headers).build();
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
		}
		catch (Throwable t) {
			logger.error("Failed to handle message " + message, t);
		}
	}
{

		if (returnValue == null) {
			return;
		}

		SimpMessageHeaderAccessor inputHeaders = SimpMessageHeaderAccessor.wrap(inputMessage);

		String sessionId = inputHeaders.getSessionId();
		MessagePostProcessor postProcessor = new SessionHeaderPostProcessor(sessionId);

		ReplyTo replyTo = returnType.getMethodAnnotation(ReplyTo.class);
		if (replyTo != null) {
			for (String destination : replyTo.value()) {
				this.messagingTemplate.convertAndSend(destination, returnValue, postProcessor);
			}
		}

		ReplyToUser replyToUser = returnType.getMethodAnnotation(ReplyToUser.class);
		if (replyToUser != null) {
			if (inputHeaders.getUser() == null) {
				throw new MissingSessionUserException(inputMessage);
			}
			String user = inputHeaders.getUser().getName();
			for (String destination : replyToUser.value()) {
				this.messagingTemplate.convertAndSendToUser(user, destination, returnValue, postProcessor);
			}
		}
	}
{
		List<String> matchingHeaderNames = new ArrayList<String>();
		if (headers != null) {
			for (Map.Entry<String, Object> header: headers.entrySet()) {
				if (PatternMatchUtils.simpleMatch(pattern,  header.getKey())) {
					matchingHeaderNames.add(header.getKey());
				}
			}
		}
		return matchingHeaderNames;
	}
{
		List<TypeDescriptor> argumentTypes = getArgumentTypes(arguments);

		if (value == null) {
			throwIfNotNullSafe(argumentTypes);
			return TypedValue.NULL;
		}

		MethodExecutor executorToUse = getCachedExecutor(targetType, argumentTypes);

		if (executorToUse != null) {
			try {
				return executorToUse.execute(evaluationContext, value, arguments);
			}
			catch (AccessException ae) {
				// Two reasons this can occur:
				// 1. the method invoked actually threw a real exception
				// 2. the method invoked was not passed the arguments it expected and
				//    has become 'stale'

				// In the first case we should not retry, in the second case we should see
				// if there is a better suited method.

				// To determine the situation, the AccessException will contain a cause.
				// If the cause is an InvocationTargetException, a user exception was
				// thrown inside the method.
				// Otherwise the method could not be invoked.
				throwSimpleExceptionIfPossible(value, ae);

				// at this point we know it wasn't a user problem so worth a retry if a
				// better candidate can be found
				this.cachedExecutor = null;
			}
		}

		// either there was no accessor or it no longer existed
		executorToUse = findAccessorForMethod(this.name, argumentTypes, value, evaluationContext);
		this.cachedExecutor = new CachedMethodExecutor(executorToUse, targetType,
				argumentTypes);
		try {
			return executorToUse.execute(evaluationContext,
					value, arguments);
		}
		catch (AccessException ex) {
			// Same unwrapping exception handling as above in above catch block
			throwSimpleExceptionIfPossible(value, ex);
			throw new SpelEvaluationException(getStartPosition(), ex,
					SpelMessage.EXCEPTION_DURING_METHOD_INVOCATION, this.name,
					value.getClass().getName(),
					ex.getMessage());
		}
	}
{
		if (!this.nullSafe) {
			throw new SpelEvaluationException(getStartPosition(),
					SpelMessage.METHOD_CALL_ON_NULL_OBJECT_NOT_ALLOWED,
					FormatHelper.formatMethodForMessage(this.name, argumentTypes));
		}
	}
{
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
		return arguments;
	}
{
		return getFirstDate(IF_MODIFIED_SINCE);
	}
{

		Assert.notNull(this.clientChannel, "No clientChannel to send messages to");

		if (message == null) {
			return;
		}

		PubSubHeaderAccesssor headers = PubSubHeaderAccesssor.wrap(message);
		Assert.notNull(headers.getSubscriptionId(), "No subscription id: " + message);

		PubSubHeaderAccesssor returnHeaders = PubSubHeaderAccesssor.wrap(message);
		returnHeaders.setSessionId(headers.getSessionId());
		returnHeaders.setSubscriptionId(headers.getSubscriptionId());

		if (returnHeaders.getDestination() == null) {
			returnHeaders.setDestination(headers.getDestination());
		}

		Message<?> returnMessage = MessageBuilder.withPayload(
				message.getPayload()).copyHeaders(headers.toHeaders()).build();

		this.clientChannel.send(returnMessage);
 	}
{
		Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
		if (targetClass == null && target != null) {
			targetClass = target.getClass();
		}
		return targetClass;
	}
{
		ExceptionHandler annotation = AnnotationUtils.findAnnotation(method, ExceptionHandler.class);
		result.addAll(Arrays.asList(annotation.value()));
	}
{
			clientChannel.send(message);
		}
{

		if (logger.isDebugEnabled()) {
			logger.debug("Subscribe " + message);
		}

		PubSubHeaders headers = PubSubHeaders.fromMessageHeaders(message.getHeaders());
		String subscriptionId = headers.getSubscriptionId();
		BroadcastingConsumer consumer = new BroadcastingConsumer(subscriptionId);

		String key = getPublishKey(headers.getDestination());
		Registration<?> registration = this.reactor.on(new ObjectSelector<String>(key), consumer);

		String sessionId = headers.getSessionId();
		List<Registration<?>> list = this.subscriptionsBySession.get(sessionId);
		if (list == null) {
			list = new ArrayList<Registration<?>>();
			this.subscriptionsBySession.put(sessionId, list);
		}
		list.add(registration);
	}
{

		PubSubHeaders headers = PubSubHeaders.fromMessageHeaders(message.getHeaders());
		String sessionId = headers.getSessionId();
		String subscriptionId = headers.getSubscriptionId();

		Assert.notNull(subscriptionId, "No subscription id: " + message);

		PubSubHeaders returnHeaders = PubSubHeaders.fromMessageHeaders(returnMessage.getHeaders());
		returnHeaders.setSessionId(sessionId);
		returnHeaders.setSubscriptionId(subscriptionId);

		return MessageBuilder.fromPayloadAndHeaders(returnMessage.getPayload(), returnHeaders.toMessageHeaders()).build();
	}
{

		StompHeaders stompHeaders = StompHeaders.fromMessageHeaders(message.getHeaders());
		String sessionId = stompHeaders.getSessionId();
		byte[] bytesToWrite;

		try {
			stompHeaders.setStompCommandIfNotSet(StompCommand.SEND);

			MediaType contentType = stompHeaders.getContentType();
			byte[] payload = this.payloadConverter.convertToPayload(message.getPayload(), contentType);
			Message<byte[]> byteMessage = new GenericMessage<byte[]>(payload, stompHeaders.toMessageHeaders());
			bytesToWrite = this.stompMessageConverter.fromMessage(byteMessage);
		}
		catch (Throwable ex) {
			logger.error("Failed to forward message " + message, ex);
			return;
		}

		TcpConnection<String, String> connection = getConnection(sessionId);
		Assert.notNull(connection, "TCP connection to message broker not found, sessionId=" + sessionId);
		try {
			if (logger.isTraceEnabled()) {
				logger.trace("Forwarding STOMP " + stompHeaders.getStompCommand() + " message");
			}
			connection.out().accept(new String(bytesToWrite, Charset.forName("UTF-8")));
		}
		catch (Throwable ex) {
			logger.error("Could not get TCP connection " + sessionId, ex);
			try {
				if (connection != null) {
					connection.close();
				}
			}
			catch (Throwable t) {
				// ignore
			}
		}
	}
{

		StompHeaders stompHeaders = new StompHeaders(StompCommand.ERROR);
		stompHeaders.setMessage(error.getMessage());

		Message<byte[]> errorMessage = new GenericMessage<byte[]>(new byte[0], stompHeaders.getMessageHeaders());
		byte[] bytes = this.stompMessageConverter.fromMessage(errorMessage);

		try {
			session.sendMessage(new TextMessage(new String(bytes, Charset.forName("UTF-8"))));
		}
		catch (Throwable t) {
			// ignore
		}
	}
{

		if (logger.isTraceEnabled()) {
			logger.trace("Processing: " + message);
		}

		try {
			StompHeaders stompHeaders = new StompHeaders(message.getHeaders(), true);
			MessageType messageType = stompHeaders.getMessageType();
			if (MessageType.CONNECT.equals(messageType)) {
				handleConnect(session, message);
			}
			else if (MessageType.MESSAGE.equals(messageType)) {
				handleMessage(message);
			}
			else if (MessageType.SUBSCRIBE.equals(messageType)) {
				handleSubscribe(message);
			}
			else if (MessageType.UNSUBSCRIBE.equals(messageType)) {
				handleUnsubscribe(message);
			}
			else if (MessageType.DISCONNECT.equals(messageType)) {
				handleDisconnect(message);
			}
			this.eventBus.send(AbstractMessageService.CLIENT_TO_SERVER_MESSAGE_KEY, message);
		}
		catch (Throwable t) {
			logger.error("Terminating STOMP session due to failure to send message: ", t);
			sendErrorMessage(session, t);
		}
	}
{
		try {
			AnnotationMetadata metadata = configurationClass.getMetadata();
			if (metadata instanceof StandardAnnotationMetadata) {
				return asSourceClass(((StandardAnnotationMetadata) metadata).getIntrospectedClass());
			}
			return asSourceClass(configurationClass.getMetadata().getClassName());
		}
		catch (ClassNotFoundException ex) {
			throw new IllegalStateException(ex);
		}
	}
{
		if (isInvalid()) {
			throw new IllegalStateException("The session has already been invalidated");
		}
	}
{
		resetResponse();
		this.servletRequest = new MockHttpServletRequest();
		this.servletRequest.setAsyncSupported(true);
		this.request = new AsyncServletServerHttpRequest(this.servletRequest, this.servletResponse);
	}
{
		super.doVisitEnd(annotationClass);
		List<AnnotationAttributes> attributes = this.attributesMap.get(this.annotationType);
		if (attributes == null) {
			this.attributesMap.add(this.annotationType, this.attributes);
		}
		else {
			attributes.add(0, this.attributes);
		}
		Set<String> metaAnnotationTypeNames = new LinkedHashSet<String>();
		for (Annotation metaAnnotation : annotationClass.getAnnotations()) {
			recusivelyCollectMetaAnnotations(metaAnnotationTypeNames, metaAnnotation);
		}
		if (this.metaAnnotationMap != null) {
			this.metaAnnotationMap.put(annotationClass.getName(), metaAnnotationTypeNames);
		}
	}
{

		StandardWebSocketSessionAdapter session = new StandardWebSocketSessionAdapter();
		session.setUri(uri);
		session.setRemoteHostName(uri.getHost());
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

		JettyWebSocketSessionAdapter session = new JettyWebSocketSessionAdapter();
		session.setUri(uri);
		session.setRemoteHostName(uri.getHost());

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

		Assert.isInstanceOf(ServletServerHttpRequest.class, request);
		HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();

		Assert.isInstanceOf(ServletServerHttpResponse.class, response);
		HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();

		if (!this.factory.isUpgradeRequest(servletRequest, servletResponse)) {
			// should never happen
			throw new HandshakeFailureException("Not a WebSocket request");
		}

		JettyWebSocketSessionAdapter session = new JettyWebSocketSessionAdapter();
		this.wsSessionInitializer.initialize(request, response, session);
		JettyWebSocketListenerAdapter listener = new JettyWebSocketListenerAdapter(webSocketHandler, session);

		servletRequest.setAttribute(WEBSOCKET_LISTENER_ATTR_NAME, listener);

		if (!this.factory.acceptWebSocket(servletRequest, servletResponse)) {
			// should never happen
			throw new HandshakeFailureException("WebSocket request not accepted by Jetty");
		}
	}
{
		return mergeProperties();
	}
{
		if (logger.isDebugEnabled()) {
			logger.debug("Starting " + this.getClass().getSimpleName());
		}
		this.isRunning = true;
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
{
		if (logger.isDebugEnabled()) {
			logger.debug("Stopping " + this.getClass().getSimpleName());
		}
		try {
			if (isConnected()) {
				closeConnection();
			}
		}
		catch (Throwable e) {
			logger.error("Failed to stop WebSocket connection", e);
		}
		finally {
			this.isRunning = false;
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
		return valueToUse;
	}
{

		URI uri = UriComponentsBuilder.fromUriString(uriTemplate).buildAndExpand(uriVariables).encode().toUri();
		return doHandshake(webSocketHandler, null, uri);
	}
{
		try {
			if (this.handler != null) {
				this.handlerProvider.destroy(this.handler);
			}
		}
		catch (Throwable t) {
			logger.warn("Error while destroying handler", t);
		}
		finally {
			this.handler = null;
		}
	}
{

		try {
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().write(error.getBytes("UTF-8"));
		}
		catch (Throwable t) {
			throw new TransportErrorException("Failed to send error message to client", t, sessionId);
		}
	}
{

		if (httpServerSession.isNew()) {
			logger.debug("Opening " + getTransportType() + " connection");
			httpServerSession.setInitialRequest(request, response, getFrameFormat(request));
		}
		else if (!httpServerSession.isActive()) {
			logger.debug("starting " + getTransportType() + " async request");
			httpServerSession.setLongPollingRequest(request, response, getFrameFormat(request));
		}
		else {
			try {
				logger.debug("another " + getTransportType() + " connection still open: " + httpServerSession);
				SockJsFrame closeFrame = SockJsFrame.closeFrameAnotherConnectionOpen();
				response.getBody().write(getFrameFormat(request).format(closeFrame).getContentBytes());
			}
			catch (IOException e) {
				throw new TransportErrorException("Failed to send SockJS close frame", e, httpServerSession.getId());
			}
		}
	}
{
		if (isActive()) {
			frame = this.frameFormat.format(frame);
			if (logger.isTraceEnabled()) {
				logger.trace("Writing " + frame);
			}
			this.response.getBody().write(frame.getContentBytes());
		}
	}
{
			try {
				if (this.handler != null) {
					this.provider.destroy(this.handler);
				}
			}
			catch (Throwable t) {
				logger.warn("Error while destroying handler", t);
			}
			finally {
				this.session = null;
				this.handler = null;
			}
		}
{
		byte[] result = new byte[getPayload().remaining()];
		getPayload().get(result);
		return result;
	}
{

		URI uri = UriComponentsBuilder.fromUriString(uriTemplate).buildAndExpand(uriVariables).encode().toUri();
		return doHandshake(handler, null, uri);
	}
{
		if (metadata instanceof StandardAnnotationMetadata) {
			for (Class<?> memberClass : ((StandardAnnotationMetadata) metadata).getIntrospectedClass().getDeclaredClasses()) {
				if (ConfigurationClassUtils.isConfigurationCandidate(new StandardAnnotationMetadata(memberClass))) {
					processConfigurationClass(new ConfigurationClass(memberClass, true));
				}
			}
		}
		else {
			for (String memberClassName : metadata.getMemberClassNames()) {
				MetadataReader reader = this.metadataReaderFactory.getMetadataReader(memberClassName);
				AnnotationMetadata memberClassMetadata = reader.getAnnotationMetadata();
				if (ConfigurationClassUtils.isConfigurationCandidate(memberClassMetadata)) {
					processConfigurationClass(new ConfigurationClass(reader, true));
				}
			}
		}
	}
{
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
{
		AnnotationAttributes raw = this.attributeMap.get(annotationType);
		return convertClassValues(raw, classValuesAsString);
	}
{

		super.afterPropertiesSet();

		if (this.transportHandlers.isEmpty()) {
			if (isWebSocketEnabled() && (this.transportHandlerOverrides.get(TransportType.WEBSOCKET) == null)) {
				this.transportHandlers.put(TransportType.WEBSOCKET,
						new WebSocketTransportHandler(new DefaultHandshakeHandler()));
			}
			this.transportHandlers.put(TransportType.XHR, new XhrPollingTransportHandler());
			this.transportHandlers.put(TransportType.XHR_SEND, new XhrTransportHandler());
			this.transportHandlers.put(TransportType.JSONP, new JsonpPollingTransportHandler());
			this.transportHandlers.put(TransportType.JSONP_SEND, new JsonpTransportHandler());
			this.transportHandlers.put(TransportType.XHR_STREAMING, new XhrStreamingTransportHandler());
			this.transportHandlers.put(TransportType.EVENT_SOURCE, new EventSourceTransportHandler());
			this.transportHandlers.put(TransportType.HTML_FILE, new HtmlFileTransportHandler());
		}

		if (!this.transportHandlerOverrides.isEmpty()) {
			for (TransportHandler transportHandler : this.transportHandlerOverrides.values()) {
				this.transportHandlers.put(transportHandler.getTransportType(), transportHandler);
			}
		}

		for (TransportHandler h : this.transportHandlers.values()) {
			if (h instanceof ConfigurableTransportHandler) {
				((ConfigurableTransportHandler) h).setSockJsConfiguration(this);
			}
		}

		this.sessionTimeoutSchedulerHolder.initialize();

		this.sessionTimeoutSchedulerHolder.getScheduler().scheduleAtFixedRate(new Runnable() {
			public void run() {
				try {
					int count = sessions.size();
					if (logger.isTraceEnabled() && (count != 0)) {
						logger.trace("Checking " + count + " session(s) for timeouts [" + getName() + "]");
					}
					for (AbstractSockJsSession session : sessions.values()) {
						if (session.getTimeSinceLastActive() > getDisconnectDelay()) {
							if (logger.isTraceEnabled()) {
								logger.trace("Removing " + session + " for [" + getName() + "]");
							}
							session.close();
							sessions.remove(session.getId());
						}
					}
					if (logger.isTraceEnabled() && (count != 0)) {
						logger.trace(sessions.size() + " remaining session(s) [" + getName() + "]");
					}
				}
				catch (Throwable t) {
					logger.error("Failed to complete session timeout checks for [" + getName() + "]", t);
				}
			}
		}, getDisconnectDelay());
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
				this.sockJsHandler.afterConnectionClosed(status, this);
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
		logger.debug("WebSocket version not supported " + request.getHeaders().get("Sec-WebSocket-Version"));
		response.setStatusCode(HttpStatus.UPGRADE_REQUIRED);
		response.getHeaders().setSecWebSocketVersion(StringUtils.arrayToCommaDelimitedString(getSupportedVerions()));
	}
{
		Assert.notNull(sockJsConfig, "sockJsConfig is required");
		return sockJsConfig.getSockJsHandler();
	}
{

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
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String ifNoneMatch = iterator.next();
			builder.append(ifNoneMatch);
			if (iterator.hasNext()) {
				builder.append(", ");
			}
		}
		return builder.toString();
	}
{
		List<String> result = new ArrayList<String>();

		String value = getFirst(header);
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
			if (synchronizedWithTransaction) {
				if (!emHolder.isSynchronizedWithTransaction() &&
						TransactionSynchronizationManager.isSynchronizationActive()) {
					// Try to explicitly synchronize the EntityManager itself
					// with an ongoing JTA transaction, if any.
					try {
						emHolder.getEntityManager().joinTransaction();
					}
					catch (TransactionRequiredException ex) {
						logger.debug("Could not join transaction because none was actually active", ex);
					}
					Object transactionData = prepareTransaction(emHolder.getEntityManager(), emf);
					TransactionSynchronizationManager.registerSynchronization(
							new TransactionalEntityManagerSynchronization(emHolder, emf, transactionData, false));
					emHolder.setSynchronizedWithTransaction(true);
				}
				// Use holder's reference count to track synchronizedWithTransaction access.
				// isOpen() check used below to find out about it.
				emHolder.requested();
				return emHolder.getEntityManager();
			}
			else {
				// unsynchronized EntityManager demanded
				if (emHolder.isTransactionActive() && !emHolder.isOpen()) {
					if (!TransactionSynchronizationManager.isSynchronizationActive()) {
						return null;
					}
					// EntityManagerHolder with an active transaction coming from JpaTransactionManager,
					// with no synchronized EntityManager having been requested by application code before.
					// Unbind in order to register a new unsynchronized EntityManager instead.
					TransactionSynchronizationManager.unbindResource(emf);
				}
				else {
					// Either a previously bound unsynchronized EntityManager, or the application
					// has requested a synchronized EntityManager before and therefore upgraded
					// this transaction's EntityManager to synchronized before.
					return emHolder.getEntityManager();
				}
			}
		}
		else if (!TransactionSynchronizationManager.isSynchronizationActive()) {
			// Indicate that we can't obtain a transactional EntityManager.
			return null;
		}

		// Create a new EntityManager for use within the current transaction.
		logger.debug("Opening JPA EntityManager");
		EntityManager em = null;
		if (!synchronizedWithTransaction && createEntityManagerWithSynchronizationTypeMethod != null) {
			try {
				em = (EntityManager) ReflectionUtils.invokeMethod(createEntityManagerWithSynchronizationTypeMethod,
						emf, synchronizationTypeUnsynchronized, properties);
			}
			catch (AbstractMethodError err) {
				// JPA 2.1 API available but method not actually implemented in persistence provider:
				// falling back to regular createEntityManager method.
			}
		}
		if (em == null) {
			em = (!CollectionUtils.isEmpty(properties) ? emf.createEntityManager(properties) : emf.createEntityManager());
		}

		// Use same EntityManager for further JPA actions within the transaction.
		// Thread object will get removed by synchronization at transaction completion.
		logger.debug("Registering transaction synchronization for JPA EntityManager");
		emHolder = new EntityManagerHolder(em);
		if (synchronizedWithTransaction) {
			Object transactionData = prepareTransaction(em, emf);
			TransactionSynchronizationManager.registerSynchronization(
					new TransactionalEntityManagerSynchronization(emHolder, emf, transactionData, true));
			emHolder.setSynchronizedWithTransaction(true);
		}
		else {
			// unsynchronized - just scope it for the transaction, as demanded by the JPA 2.1 spec
			TransactionSynchronizationManager.registerSynchronization(
					new TransactionScopedEntityManagerSynchronization(emHolder, emf));
		}
		TransactionSynchronizationManager.bindResource(emf, emHolder);

		return em;
	}
{

		Assert.notNull(emf, "EntityManagerFactory must not be null");
		if (emf instanceof EntityManagerFactoryInfo) {
			EntityManagerFactoryInfo emfInfo = (EntityManagerFactoryInfo) emf;
			EntityManagerFactory nativeEmf = emfInfo.getNativeEntityManagerFactory();
			EntityManager rawEntityManager = (!CollectionUtils.isEmpty(properties) ?
					nativeEmf.createEntityManager(properties) : nativeEmf.createEntityManager());
			return createProxy(rawEntityManager, emfInfo, true, synchronizedWithTransaction);
		}
		else {
			EntityManager rawEntityManager = (!CollectionUtils.isEmpty(properties) ?
					emf.createEntityManager(properties) : emf.createEntityManager());
			return createProxy(rawEntityManager, null, null, null, null, null, true, synchronizedWithTransaction);
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
		return createSharedEntityManager(emf, properties, synchronizedWithTransaction, emIfcs);
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
				ifcs, new SharedEntityManagerInvocationHandler(emf, properties, synchronizedWithTransaction));
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
{
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
	}
{
		this.allBeanNamesByType.clear();
		this.singletonBeanNamesByType.clear();
	}
{
		DefaultConversionService.addDefaultConverters(conversionService);
		registrar.registerFormatters(conversionService);

		SimpleDateBean bean = new SimpleDateBean();
		bean.getChildren().add(new SimpleDateBean());
		binder = new DataBinder(bean);
		binder.setConversionService(conversionService);

		LocaleContextHolder.setLocale(Locale.US);
	}
{
		marshaller.setValidation(this.validating);
		marshaller.setSuppressNamespaces(this.suppressNamespaces);
		marshaller.setSuppressXSIType(this.suppressXsiType);
		marshaller.setMarshalAsDocument(this.marshalAsDocument);
		marshaller.setMarshalExtendedType(this.marshalExtendedType);
		marshaller.setRootElement(this.rootElement);
		marshaller.setNoNamespaceSchemaLocation(this.noNamespaceSchemaLocation);
		marshaller.setSchemaLocation(this.schemaLocation);
		marshaller.setUseXSITypeAtRoot(this.useXSITypeAtRoot);
		if (this.doctypes != null) {
			for (Map.Entry<String, String> doctype : this.doctypes.entrySet()) {
				marshaller.setDoctype(doctype.getKey(), doctype.getValue());
			}
		}
		if (this.processingInstructions != null) {
			for (Map.Entry<String, String> processingInstruction : this.processingInstructions.entrySet()) {
				marshaller.addProcessingInstruction(processingInstruction.getKey(), processingInstruction.getValue());
			}
		}
		if (this.namespaceMappings != null) {
			for (Map.Entry<String, String> entry : this.namespaceMappings.entrySet()) {
				marshaller.setNamespaceMapping(entry.getKey(), entry.getValue());
			}
		}
	}
{
		JsonGenerator generator = this.objectMapper.getJsonFactory().createJsonGenerator(stream, this.encoding);

		// A workaround for JsonGenerators not applying serialization features
		// https://github.com/FasterXML/jackson-databind/issues/12
		if (this.objectMapper.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
			generator.useDefaultPrettyPrinter();
		}

		if (prefixJson) {
			generator.writeRaw("{} && ");
		}
		this.objectMapper.writeValue(generator, value);
	}
{
		JsonGenerator generator = this.objectMapper.getJsonFactory().createJsonGenerator(stream, this.encoding);

		// A workaround for JsonGenerators not applying serialization features
		// https://github.com/FasterXML/jackson-databind/issues/12
		if (this.objectMapper.getSerializationConfig().isEnabled(SerializationConfig.Feature.INDENT_OUTPUT)) {
			generator.useDefaultPrettyPrinter();
		}

		if (prefixJson) {
			generator.writeRaw("{} && ");
		}
		this.objectMapper.writeValue(generator, value);
	}
{
			if (this.index >= this.collection.size()) {

				if (!this.growCollection) {
					throw new SpelEvaluationException(getStartPosition(), SpelMessage.COLLECTION_INDEX_OUT_OF_BOUNDS,
							this.collection.size(), this.index);
				}

				if(this.index >= this.maximumSize) {
					throw new SpelEvaluationException(getStartPosition(), SpelMessage.UNABLE_TO_GROW_COLLECTION);
				}

				if (this.collectionEntryTypeDescriptor.getElementTypeDescriptor() == null) {
					throw new SpelEvaluationException(getStartPosition(), SpelMessage.UNABLE_TO_GROW_COLLECTION_UNKNOWN_ELEMENT_TYPE);
				}

				TypeDescriptor elementType = this.collectionEntryTypeDescriptor.getElementTypeDescriptor();
				try {
					int newElements = this.index - this.collection.size();
					while (newElements >= 0) {
						(this.collection).add(elementType.getType().newInstance());
						newElements--;
					}
				}
				catch (Exception ex) {
					throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.UNABLE_TO_GROW_COLLECTION);
				}
			}
		}
{

		fillProperties(props, resource, new DefaultPropertiesPersister());
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
		int nrFound = matchingBeans.size();
		if (nrFound == 1) {
			return matchingBeans.values().iterator().next();
		}
		else if (nrFound > 1) {
			throw new NoUniqueBeanDefinitionException(type, matchingBeans.keySet());
		}
		else {
			throw new NoSuchBeanDefinitionException(type);
		}
	}
{
		checkLeftOperand(token, left);
		checkRightOperand(token, right);
	}
{
		Method[] methods = clazz.getMethods();
		Arrays.sort(methods, new Comparator<Method>() {
			@Override
			public int compare(Method o1, Method o2) {
				return (o1.isBridge() == o2.isBridge()) ? 0 : (o1.isBridge() ? 1 : -1);
			}
		});
		return methods;
	}
{
		Assert.notNull(validators, "Validators required");
		for (Validator validator : validators) {
			if (validator != null && (getTarget() != null && !validator.supports(getTarget().getClass()))) {
				throw new IllegalStateException("Invalid target for Validator [" + validator + "]: " + getTarget());
			}
		}
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
		return handlerMethod;
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
		logger.warn(message);
	}
{

		for (PropertyDescriptor pd : this.propertyDescriptors) {
			final Class<?> candidateType;
			final String candidateName = pd.getName();
			if (pd instanceof IndexedPropertyDescriptor) {
				IndexedPropertyDescriptor ipd = (IndexedPropertyDescriptor) pd;
				candidateType = ipd.getIndexedPropertyType();
				if (candidateName.equals(propertyName) &&
						(candidateType.equals(propertyType) ||
								candidateType.equals(propertyType.getComponentType()))) {
					return pd;
				}
			}
			else {
				candidateType = pd.getPropertyType();
				if (candidateName.equals(propertyName) &&
						(candidateType.equals(propertyType) ||
								propertyType.equals(candidateType.getComponentType()))) {
					return pd;
				}
			}
		}
		return null;
	}
{
		try {
			Boolean value = operand.getValue(state, Boolean.class);
			assertValueNotNull(value);
			return value;
		}
		catch (SpelEvaluationException ee) {
			ee.setPosition(operand.getStartPosition());
			throw ee;
		}
	}
{
		try {
			Boolean value = operand.getValue(state, Boolean.class);
			assertValueNotNull(value);
			return value;
		}
		catch (SpelEvaluationException ee) {
			ee.setPosition(operand.getStartPosition());
			throw ee;
		}
	}
{
		HttpServletRequest request = this.mockRequest;
		if ((timeout != 0) && request.isAsyncStarted()) {
			if (!awaitAsyncResult(request, timeout)) {
				throw new IllegalStateException(
						"Gave up waiting on async result from handler [" + this.handler + "] to complete");
			}
		}
		return this.asyncResult;
	}
{
		if (!(dataSource instanceof SmartDataSource) || ((SmartDataSource) dataSource).shouldClose(con)) {
			con.close();
		}
	}
{
		return createDateTimeFormatter(DateTimeFormat.mediumDateTime());
	}
{
		conversionService = new FormattingConversionService();
		DefaultConversionService.addDefaultConverters(conversionService);

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
		HttpHeaders headers = response.getHeaders();
		MediaType contentType = headers.getContentType();
		return contentType != null ? contentType.getCharSet() : null;
	}
{
		Set<String> excluded =  new HashSet<String>(Arrays.asList(excludedProperties));
		Method[] annotationProperties = ann.annotationType().getDeclaredMethods();
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		for (Method annotationProperty : annotationProperties) {
			String propertyName = annotationProperty.getName();
			if ((!excluded.contains(propertyName)) && bw.isWritableProperty(propertyName)) {
				Object value = ReflectionUtils.invokeMethod(annotationProperty, ann);
				if (valueResolver != null && value instanceof String) {
					value = valueResolver.resolveStringValue((String) value);
				}
				bw.setPropertyValue(propertyName, value);
			}
		}
	}
{
		ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
		initializer.setConversionService(mvcConversionService());
		initializer.setValidator(mvcValidator());
		initializer.setMessageCodesResolver(getMessageCodesResolver());
		return initializer;
	}
{
		this.asyncWebRequest.setTimeoutHandler((Runnable) notNull());
		this.asyncWebRequest.addCompletionHandler((Runnable) notNull());
		this.asyncWebRequest.startAsync();
		expect(this.asyncWebRequest.isAsyncComplete()).andReturn(false);
		this.asyncWebRequest.dispatch();
		replay(this.asyncWebRequest);
	}
{
		if (getChildCount()==1) {
			return children[0].getValueRef(state);
		}
		TypedValue result = null;
		SpelNodeImpl nextNode = null;
		try {
			nextNode = children[0];
			result = nextNode.getValueInternal(state);
			int cc = getChildCount();
			for (int i = 1; i < cc-1; i++) {
				try {
					state.pushActiveContextObject(result);
					nextNode = children[i];
					result = nextNode.getValueInternal(state);
				} finally {
					state.popActiveContextObject();
				}
			}
			try {
				state.pushActiveContextObject(result);
				nextNode = children[cc-1];
				return nextNode.getValueRef(state);
			} finally {
				state.popActiveContextObject();
			}
		} catch (SpelEvaluationException ee) {
			// Correct the position for the error before re-throwing
			ee.setPosition(nextNode.getStartPosition());
			throw ee;
		}
	}
{
		TypedValue context = state.getActiveContextObject();
		Object targetObject = context.getValue();
		TypeDescriptor targetObjectTypeDescriptor = context.getTypeDescriptor();
		TypedValue indexValue = null;
		Object index = null;

		// This first part of the if clause prevents a 'double dereference' of
		// the property (SPR-5847)
		if (targetObject instanceof Map && (children[0] instanceof PropertyOrFieldReference)) {
			PropertyOrFieldReference reference = (PropertyOrFieldReference) children[0];
			index = reference.getName();
			indexValue = new TypedValue(index);
		} else {
			// In case the map key is unqualified, we want it evaluated against
			// the root object so temporarily push that on whilst evaluating the key
			try {
				state.pushActiveContextObject(state.getRootContextObject());
				indexValue = children[0].getValueInternal(state);
				index = indexValue.getValue();
			} finally {
				state.popActiveContextObject();
			}
		}

		// Indexing into a Map
		if (targetObject instanceof Map) {
			Object key = index;
			if (targetObjectTypeDescriptor.getMapKeyTypeDescriptor() != null) {
				key = state.convertValue(key,
						targetObjectTypeDescriptor.getMapKeyTypeDescriptor());
			}
			return new MapIndexingValueRef(state.getTypeConverter(),
					(Map<?, ?>) targetObject, key, targetObjectTypeDescriptor);
		}

		if (targetObject == null) {
			throw new SpelEvaluationException(getStartPosition(),
					SpelMessage.CANNOT_INDEX_INTO_NULL_VALUE);
		}

		// if the object is something that looks indexable by an integer,
		// attempt to treat the index value as a number
		if (targetObject instanceof Collection
				|| targetObject.getClass().isArray()
				|| targetObject instanceof String) {
			int idx = (Integer) state.convertValue(index,
					TypeDescriptor.valueOf(Integer.class));
			if (targetObject.getClass().isArray()) {
				return new ArrayIndexingValueRef(state.getTypeConverter(),
						targetObject, idx, targetObjectTypeDescriptor);
			} else if (targetObject instanceof Collection) {
				return new CollectionIndexingValueRef(
						(Collection<?>) targetObject, idx,
						targetObjectTypeDescriptor,state.getTypeConverter(),
						state.getConfiguration().isAutoGrowCollections());
			} else if (targetObject instanceof String) {
				return new StringIndexingLValue((String) targetObject, idx,
						targetObjectTypeDescriptor);
			}
		}

		// Try and treat the index value as a property of the context object
		// TODO could call the conversion service to convert the value to a
		// String
		if (indexValue.getTypeDescriptor().getType() == String.class) {
			return new PropertyIndexingValueRef(targetObject,
					(String) indexValue.getValue(),
					state.getEvaluationContext(), targetObjectTypeDescriptor);
		}

		throw new SpelEvaluationException(getStartPosition(),
				SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE,
				targetObjectTypeDescriptor.toString());
	}
{

		List<MethodResolver> mResolvers = eContext.getMethodResolvers();
		if (mResolvers != null) {
			for (MethodResolver methodResolver : mResolvers) {
				try {
					MethodExecutor cEx = methodResolver.resolve(
							eContext, contextObject, name, argumentTypes);
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
			for (Map.Entry<?,?> entry : mapData.entrySet()) {
				try {
					state.pushActiveContextObject(new TypedValue(entry));
					result.add(this.children[0].getValueInternal(state).getValue());
				}
				finally {
					state.popActiveContextObject();
				}
			}
			return new ValueRef.TypedValueHolderValueRef(new TypedValue(result),this); // TODO unable to build correct type descriptor
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
				return new ValueRef.TypedValueHolderValueRef(new TypedValue(resultArray),this);
			}
			return new ValueRef.TypedValueHolderValueRef(new TypedValue(result),this);
		}
		else {
			if (operand==null) {
				if (this.nullSafe) {
					return ValueRef.NullValueRef.instance;
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

		TypedValue result = readProperty(contextObject, eContext, this.name);

		// Dynamically create the objects if the user has requested that optional behavior
		if (result.getValue() == null && isAutoGrowNullReferences &&
				nextChildIs(Indexer.class, PropertyOrFieldReference.class)) {
			TypeDescriptor resultDescriptor = result.getTypeDescriptor();
			// Creating lists and maps
			if ((resultDescriptor.getType().equals(List.class) || resultDescriptor.getType().equals(Map.class))) {
				// Create a new collection or map ready for the indexer
				if (resultDescriptor.getType().equals(List.class)) {
					try { 
						if (isWritableProperty(this.name,contextObject,eContext)) {
							List<?> newList = ArrayList.class.newInstance();
							writeProperty(contextObject, eContext, this.name, newList);
							result = readProperty(contextObject, eContext, this.name);
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
						if (isWritableProperty(this.name,contextObject,eContext)) {
							Map<?,?> newMap = HashMap.class.newInstance();
							writeProperty(contextObject, eContext, name, newMap);
							result = readProperty(contextObject, eContext, this.name);
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
					if (isWritableProperty(this.name,contextObject,eContext)) {
						Object newObject  = result.getTypeDescriptor().getType().newInstance();
						writeProperty(contextObject, eContext, name, newObject);
						result = readProperty(contextObject, eContext, this.name);
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
			for (Map.Entry<?,?> entry : mapdata.entrySet()) {
				try {
					TypedValue kvpair = new TypedValue(entry);
					state.pushActiveContextObject(kvpair);
					Object o = selectionCriteria.getValueInternal(state).getValue();
					if (o instanceof Boolean) {
						if (((Boolean) o).booleanValue() == true) {
							if (variant == FIRST) {
								result.put(entry.getKey(),entry.getValue());
								return new ValueRef.TypedValueHolderValueRef(new TypedValue(result),this);
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
				return new ValueRef.TypedValueHolderValueRef(new TypedValue(null),this);
			}
			if (variant == LAST) {
				Map<Object,Object> resultMap = new HashMap<Object,Object>();
				Object lastValue = result.get(lastKey);
				resultMap.put(lastKey,lastValue);
				return new ValueRef.TypedValueHolderValueRef(new TypedValue(resultMap),this);
			}
			return new ValueRef.TypedValueHolderValueRef(new TypedValue(result),this);
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
								return new ValueRef.TypedValueHolderValueRef(new TypedValue(element),this);
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
				return ValueRef.NullValueRef.instance;
			}
			if (variant == LAST) {
				return new ValueRef.TypedValueHolderValueRef(new TypedValue(result.get(result.size() - 1)),this);
			}
			if (operand instanceof Collection) {
				return new ValueRef.TypedValueHolderValueRef(new TypedValue(result),this);
			}
			else {
				Class<?> elementType = ClassUtils.resolvePrimitiveIfNecessary(op.getTypeDescriptor().getElementTypeDescriptor().getType());
				Object resultArray = Array.newInstance(elementType, result.size());
				System.arraycopy(result.toArray(), 0, resultArray, 0, result.size());
				return new ValueRef.TypedValueHolderValueRef(new TypedValue(resultArray),this);
			}
		} else {
			if (operand==null) {
				if (nullSafe) { 
					return ValueRef.NullValueRef.instance;
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
		SpelNodeImpl expr = eatUnaryExpression();
		if (peekToken(TokenKind.POWER)) {
			Token t = nextToken();//consume POWER
			SpelNodeImpl rhExpr = eatUnaryExpression();
			checkRightOperand(t,rhExpr);
			return new OperatorPower(toPos(t),expr, rhExpr);
		} else if (expr!=null && peekToken(TokenKind.INC,TokenKind.DEC)) {
			Token t = nextToken();//consume INC/DEC
			if (t.getKind()==TokenKind.INC) {
				return new OpInc(toPos(t),true,expr);
			} else {
				return new OpDec(toPos(t),true,expr);
			}
		}
		return expr;
	}
{

		final CountDownLatch asyncResultLatch = new CountDownLatch(1);

		WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);

		asyncManager.registerCallableInterceptor("mockmvc", new CallableProcessingInterceptor() {
			public void preProcess(NativeWebRequest request, Callable<?> task) throws Exception { }
			public void postProcess(NativeWebRequest request, Callable<?> task, Object value) throws Exception {
				asyncResultLatch.countDown();
			}
		});

		asyncManager.registerDeferredResultInterceptor("mockmvc", new DeferredResultProcessingInterceptor() {
			public void preProcess(NativeWebRequest request, DeferredResult<?> result) throws Exception { }
			public void postProcess(NativeWebRequest request, DeferredResult<?> result, Object value) throws Exception {
				asyncResultLatch.countDown();
			}
			public void afterExpiration(NativeWebRequest request, DeferredResult<?> result) throws Exception { }
		});

		return asyncResultLatch;
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
				if (startsWithDelimiter(script, i, delim)) {
					if (sb.length() > 0) {
						statements.add(sb.toString());
						sb = new StringBuilder();
					}
					i += delim.length() - 1;
					continue;
				}
				else if (c == '\n' || c == '\t') {
					c = ' ';
				}
			}
			sb.append(c);
		}
		if (StringUtils.hasText(sb)) {
			statements.add(sb.toString());
		}
	}
{
		Method method = findGetterForProperty(propertyName, clazz, target instanceof Class);
		if(method == null && target instanceof Class) {
			method = findGetterForProperty(propertyName, target.getClass(), false);
		}
		return method;
	}
{
		Method method = findSetterForProperty(propertyName, clazz, target instanceof Class);
		if(method == null && target instanceof Class) {
			method = findSetterForProperty(propertyName, target.getClass(), false);
		}
		return method;
	}
{
		Field field = findField(name, clazz, target instanceof Class);
		if(field == null && target instanceof Class) {
			field = findField(name, target.getClass(), false);
		}
		return field;
	}
{
		switch (httpMethod) {
			case GET:
				return new HttpGet(uri);
			case DELETE:
				return new HttpDelete(uri);
			case HEAD:
				return new HttpHead(uri);
			case OPTIONS:
				return new HttpOptions(uri);
			case POST:
				return new HttpPost(uri);
			case PUT:
				return new HttpPut(uri);
			case TRACE:
				return new HttpTrace(uri);
			case PATCH:
				return new HttpPatch(uri);
			default:
				throw new IllegalArgumentException("Invalid HTTP method: " + httpMethod);
		}
	}
{
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Loading ApplicationContext for merged context configuration [%s].",
				mergedConfig));
		}

		GenericApplicationContext context = new GenericApplicationContext();
		prepareContext(context);
		prepareContext(context, mergedConfig);
		customizeBeanFactory(context.getDefaultListableBeanFactory());
		loadBeanDefinitions(context, mergedConfig);
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		customizeContext(context);
		context.refresh();
		context.registerShutdownHook();
		return context;
	}
{
		Assert.notNull(fqClassName, "Class name must not be null");
		int lastDotIndex = fqClassName.lastIndexOf(PACKAGE_SEPARATOR);
		return (lastDotIndex != -1 ? fqClassName.substring(0, lastDotIndex) : "");
	}
{

		request.setAttribute("javax.servlet.error.exception", ex);
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}
{
		Assert.notNull(factoryClass, "'factoryClass' must not be null");
		if (classLoader == null) {
			classLoader = SpringFactoriesLoader.class.getClassLoader();
		}
		List<String> factoryNames = loadFactoryNames(factoryClass, classLoader);
		if (logger.isTraceEnabled()) {
			logger.trace("Loaded [" + factoryClass.getName() + "] names: " + factoryNames);
		}
		List<T> result = new ArrayList<T>(factoryNames.size());
		for (String factoryName : factoryNames) {
			result.add(instantiateFactory(factoryName, factoryClass, classLoader));
		}
		OrderComparator.sort(result);
		return result;
	}
{
		ResponseStatus annot = getMethodAnnotation(ResponseStatus.class);
		if (annot != null) {
			this.responseStatus = annot.value();
			this.responseReason = annot.reason();
		}
	}
{
		Set<MediaType> result = new HashSet<MediaType>();
		for (RequestMappingInfo partialMatch : partialMatches) {
			if (partialMatch.getConsumesCondition().getMatchingCondition(request) == null) {
				result.addAll(partialMatch.getConsumesCondition().getConsumableMediaTypes());
			}
		}
		return result;
	}
{
		Set<MediaType> result = new HashSet<MediaType>();
		for (RequestMappingInfo partialMatch : partialMatches) {
			if (partialMatch.getProducesCondition().getMatchingCondition(request) == null) {
				result.addAll(partialMatch.getProducesCondition().getProducibleMediaTypes());
			}
		}
		return result;
	}
{
		JavaType javaType = getJavaType(type);
		return (this.objectMapper.canDeserialize(javaType) && canRead(mediaType));
	}
{
		try {
			return this.objectMapper.readValue(inputMessage.getBody(), javaType);
		}
		catch (IOException ex) {
			throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
		}
	}
{
		JavaType javaType = getJavaType(type);
		return (this.objectMapper.canDeserialize(javaType) && canRead(mediaType));
	}
{
		try {
			return this.objectMapper.readValue(inputMessage.getBody(), javaType);
		}
		catch (IOException ex) {
			throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
		}
	}
{
		MediaType contentType = response.getHeaders().getContentType();
		if (contentType == null) {
			if (logger.isTraceEnabled()) {
				logger.trace("No Content-Type header found, defaulting to application/octet-stream");
			}
			contentType = MediaType.APPLICATION_OCTET_STREAM;
		}
		return contentType;
	}
{
			List<MediaType> supportedMediaTypes = messageConverter.getSupportedMediaTypes();
			List<MediaType> result = new ArrayList<MediaType>(supportedMediaTypes.size());
			for (MediaType supportedMediaType : supportedMediaTypes) {
				if (supportedMediaType.getCharSet() != null) {
					supportedMediaType =
							new MediaType(supportedMediaType.getType(), supportedMediaType.getSubtype());
				}
				result.add(supportedMediaType);
			}
			return result;
		}
{

		Annotation[] annotations = parameter.getParameterAnnotations();
		for (Annotation annot : annotations) {
			if (annot.annotationType().getSimpleName().startsWith("Valid")) {
				Object hints = AnnotationUtils.getValue(annot);
				binder.validate(hints instanceof Object[] ? (Object[]) hints : new Object[] {hints});
				BindingResult bindingResult = binder.getBindingResult();
				if (bindingResult.hasErrors()) {
					if (isBindExceptionRequired(binder, parameter)) {
						throw new MethodArgumentNotValidException(parameter, bindingResult);
					}
				}
			}
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
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Delegating to %s to load context from %s.", name(loader), mergedConfig));
		}
		return loader.loadContext(mergedConfig);
	}
{
		String participateAttributeName = getParticipateAttributeName();
		Integer count = (Integer) request.getAttribute(participateAttributeName, WebRequest.SCOPE_REQUEST);
		if (count == null) {
			return false;
		}
		// Do not modify the Session: just clear the marker.
		if (count > 1) {
			request.setAttribute(participateAttributeName, count - 1, WebRequest.SCOPE_REQUEST);
		}
		else {
			request.removeAttribute(participateAttributeName, WebRequest.SCOPE_REQUEST);
		}
		return true;
	}
{
		for (Method method : HandlerMethodSelector.selectMethods(handlerType, EXCEPTION_HANDLER_METHODS)) {
			for (Class<? extends Throwable> exceptionType : detectExceptionMappings(method)) {
				addExceptionMapping(exceptionType, method);
			}
		}
	}
{
		if (handlerMethod != null) {
			Class<?> handlerType = handlerMethod.getBeanType();
			ExceptionHandlerMethodResolver resolver = this.exceptionHandlerCache.get(handlerType);
			if (resolver == null) {
				resolver = new ExceptionHandlerMethodResolver(handlerType);
				this.exceptionHandlerCache.put(handlerType, resolver);
			}
			Method method = resolver.resolveMethod(exception);
			if (method != null) {
				return new ServletInvocableHandlerMethod(handlerMethod.getBean(), method);
			}
		}
		for (Entry<ControllerAdviceBean, ExceptionHandlerMethodResolver> entry : this.exceptionHandlerAdviceCache.entrySet()) {
			Method method = entry.getValue().resolveMethod(exception);
			if (method != null) {
				return new ServletInvocableHandlerMethod(entry.getKey().resolveBean(), method);
			}
		}
		return null;
	}
{
		InvocableHandlerMethod attrMethod = new InvocableHandlerMethod(bean, method);
		attrMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
		attrMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
		attrMethod.setDataBinderFactory(factory);
		return attrMethod;
	}
{
		InvocableHandlerMethod binderMethod = new InvocableHandlerMethod(bean, method);
		binderMethod.setHandlerMethodArgumentResolvers(this.initBinderArgumentResolvers);
		binderMethod.setDataBinderFactory(new DefaultDataBinderFactory(this.webBindingInitializer));
		binderMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
		return binderMethod;
	}
{
        try {
            return getXStream().unmarshal(streamReader);
        }
        catch (Exception ex) {
            throw convertXStreamException(ex, false);
        }
    }
{
		// look up by type and qualifier from @Transactional
		if (StringUtils.hasText(qualifier)) {
			try {
				// Use autowire-capable factory in order to support extended qualifier
				// matching (only exposed on the internal BeanFactory, not on the
				// ApplicationContext).
				BeanFactory bf = testContext.getApplicationContext().getAutowireCapableBeanFactory();

				return BeanFactoryAnnotationUtils.qualifiedBeanOfType(bf, PlatformTransactionManager.class, qualifier);
			} catch (RuntimeException ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Caught exception while retrieving transaction manager for test context " + testContext
							+ " and qualifier [" + qualifier + "]", ex);
				}
				throw ex;
			}
		}

		// else
		return getTransactionManager(testContext);
	}
{
		List<Element> paths = DomUtils.getChildElementsByTagName(interceptor, elementName);
		String[] patterns = new String[paths.size()];
		for (int i = 0; i < paths.size(); i++) {
			patterns[i] = paths.get(i).getAttribute("path");
		}
		return patterns;
	}
{
		Assert.notNull(deferredResult, "DeferredResult is required");
		Assert.state(this.asyncWebRequest != null, "AsyncWebRequest was not set");
		this.asyncWebRequest.startAsync();

		deferredResult.init(new DeferredResultHandler() {
			public void handle(Object result) {
				if (asyncWebRequest.isAsyncCompleted()) {
					throw new StaleAsyncWebRequestException("Too late to set DeferredResult: " + result);
				}
				setLastCallable(new PassThroughCallable(result));
				taskExecutor.execute(new AsyncExecutionChainRunnable(asyncWebRequest, buildChain()));
			}
		});

		this.asyncWebRequest.setTimeoutHandler(deferredResult.getTimeoutHandler());
	}
{
		if (handlerMethod != null) {
			Class<?> handlerType = handlerMethod.getBeanType();
			ExceptionHandlerMethodResolver resolver = this.exceptionHandlersByType.get(handlerType);
			if (resolver == null) {
				resolver = new ExceptionHandlerMethodResolver(handlerType);
				this.exceptionHandlersByType.put(handlerType, resolver);
			}
			Method method = resolver.resolveMethod(exception);
			if (method != null) {
				return new ServletInvocableHandlerMethod(handlerMethod.getBean(), method);
			}
		}
		return getGlobalExceptionHandlerMethod(exception);
	}
{
		try {
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
			return getXStream().unmarshal(streamReader);
		}
		catch (Exception ex) {
			throw convertXStreamException(ex, false);
		}
	}
{
		try {
			HierarchicalStreamReader hierarchicalStreamReader =
					new StaxReader(new QNameMap(),streamReader);
			return getXStream().unmarshal(hierarchicalStreamReader);
		}
		catch (Exception ex) {
			throw convertXStreamException(ex, false);
		}
	}
{
		try {
			HierarchicalStreamReader streamReader;
			if (streamDriver != null) {
				streamReader = streamDriver.createReader(reader);
			}
			else {
				streamReader = new XppReader(reader);
			}
			return getXStream().unmarshal(streamReader);
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

		if (arg == null) {
			return;
		}
		Annotation[] annotations = parameter.getParameterAnnotations();
		for (Annotation annot : annotations) {
			if (!annot.annotationType().getSimpleName().startsWith("Valid")) {
				continue;
			}
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
{
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
{
		if (instances.size() > 1) {
			throw new IllegalStateException(
					"Only one [" + instanceType + "] was expected but multiple instances were provided: " + instances);
		}
		else if (instances.size() == 1) {
			return instances.get(0);
		}
		else {
			return null;
		}
	}
{
		this.asyncContext = null;
		this.asyncCompleted.set(true);
	}
{
		expect(this.request.isAsyncSupported()).andReturn(true);
		replay(this.request);
		this.asyncRequest.onComplete(new AsyncEvent(null));
		try {
			this.asyncRequest.startAsync();
			fail("expected exception");
		}
		catch (IllegalStateException ex) {
			assertEquals("Cannot use async request after completion", ex.getMessage());
		}
	}
{
		LocaleContextHolder.setLocale(request.getLocale(), this.threadContextInheritable);
		RequestContextHolder.setRequestAttributes(requestAttributes, this.threadContextInheritable);
		if (logger.isDebugEnabled()) {
			logger.debug("Bound request context to thread: " + request);
		}
	}
{
		LocaleContextHolder.resetLocaleContext();
		RequestContextHolder.resetRequestAttributes();
	}
{

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
		if (mv != null && !mv.hasView()) {
			mv.setViewName(getDefaultViewName(request));
		}
	}
{

		boolean errorView = false;

		if (exception != null) {
			if (exception instanceof ModelAndViewDefiningException) {
				logger.debug("ModelAndViewDefiningException encountered", exception);
				mv = ((ModelAndViewDefiningException) exception).getModelAndView();
			}
			else {
				Object handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
				mv = processHandlerException(request, response, handler, exception);
				errorView = (mv != null);
			}
		}

		// Did the handler return a view to render?
		if (mv != null && !mv.wasCleared()) {
			render(mv, request, response);
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

		if (mappedHandler != null) {
			mappedHandler.triggerAfterCompletion(request, response, null);
		}
	}
{

		if (mappedHandler != null) {
			mappedHandler.triggerAfterCompletion(request, response, ex);
		}
		throw ex;
	}
{

		ServletException ex = new NestedServletException("Handler processing failed", error);
		if (mappedHandler != null) {
			mappedHandler.triggerAfterCompletion(request, response, ex);
		}
		throw ex;
	}
{

		LocaleContextHolder.setLocaleContext(localeContext, this.threadContextInheritable);
		if (attributes != null) {
			RequestContextHolder.setRequestAttributes(attributes, this.threadContextInheritable);
		}
		if (logger.isTraceEnabled()) {
			logger.trace("Bound request context to thread: " + request);
		}
	}
{

		LocaleContextHolder.setLocaleContext(prevLocaleContext, this.threadContextInheritable);
		RequestContextHolder.setRequestAttributes(previousAttributes, this.threadContextInheritable);
		if (logger.isTraceEnabled()) {
			logger.trace("Cleared thread-bound request context: " + request);
		}
	}
{

		Throwable failureCause = null;
		try {
			if (t != null) {
				if (t instanceof ServletException) {
					failureCause = t;
					throw (ServletException) t;
				}
				else if (t instanceof IOException) {
					failureCause = t;
					throw (IOException) t;
				}
				else {
					NestedServletException ex = new NestedServletException("Request processing failed", t);
					failureCause = ex;
					throw ex;
				}
			}
		}
		finally {
			if (requestAttributes != null) {
				requestAttributes.requestCompleted();
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
		return true;
	}
{

		modelFactory.updateModel(webRequest, mavContainer);

		if (mavContainer.isRequestHandled()) {
			return null;
		}
		ModelMap model = mavContainer.getModel();
		ModelAndView mav = new ModelAndView(mavContainer.getViewName(), model);
		if (!mavContainer.isViewReference()) {
			mav.setView((View) mavContainer.getView());
		}
		if (model instanceof RedirectAttributes) {
			Map<String, ?> flashAttributes = ((RedirectAttributes) model).getFlashAttributes();
			HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
			RequestContextUtils.getOutputFlashMap(request).putAll(flashAttributes);
		}
		return mav;
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
						attrs.put(method.getName(), getAnnotationAttributes(
								(Annotation)value, classValuesAsString, nestedAnnotationsAsMap));
					}
					else if (nestedAnnotationsAsMap && value instanceof Annotation[]) {
						Annotation[] realAnnotations = (Annotation[])value;
						AnnotationAttributes[] mappedAnnotations = new AnnotationAttributes[realAnnotations.length];
						for (int i = 0; i < realAnnotations.length; i++) {
							mappedAnnotations[i] = getAnnotationAttributes(
									realAnnotations[i], classValuesAsString, nestedAnnotationsAsMap);
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
		String contentType = request.getContentType();
		if (contentType == null || !contentType.toLowerCase().startsWith("multipart/")) {
			throw new MultipartException("The current request is not a multipart request");
		}
	}
{
		String contentType = request.getContentType();
		if (contentType == null || !contentType.toLowerCase().startsWith("multipart/")) {
			throw new MultipartException("The current request is not a multipart request");
		}
	}
{
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE, new FlashMap());
		request.setAttribute(DispatcherServlet.FLASH_MAP_MANAGER_ATTRIBUTE, new SessionFlashMapManager());
		return request;
	}
{
		for (T mapping : mappings) {
			T match = getMatchingMapping(mapping, request);
			if (match != null) {
				matches.add(new Match(match, handlerMethods.get(mapping)));
			}
		}
	}
{
		retrieveFlashMaps(request, true).add(flashMap);
	}
{
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
		if (!ObjectUtils.isEmpty(validationHints) && validator instanceof SmartValidator) {
			((SmartValidator) validator).validate(obj, errors, validationHints);
		}
		else {
			validator.validate(obj, errors);
		}
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
		for (ConstraintViolation<Object> violation : violations) {
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

		Object arg = readWithMessageConverters(webRequest, parameter, parameter.getParameterType());
		Annotation[] annotations = parameter.getParameterAnnotations();
		for (Annotation annot : annotations) {
			if ("Valid".equals(annot.annotationType().getSimpleName())) {
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
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String headerName = entry.getKey();
            for (String headerValue : entry.getValue()) {
                this.connection.addRequestProperty(headerName, headerValue);
            }
        }
    }
{
		Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
		if (registry instanceof EnvironmentCapable) {
			return ((EnvironmentCapable) registry).getEnvironment();
		}
		return new StandardEnvironment();
	}
{
		super.setEnvironment(environment);
		this.reader.setEnvironment(environment);
		this.scanner.setEnvironment(environment);
	}
{
		Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
		if (registry instanceof EnvironmentCapable) {
			return ((EnvironmentCapable) registry).getEnvironment();
		}
		return new StandardEnvironment();
	}
{
		HeaderValueHolder header = HeaderValueHolder.getByName(this.headers, name);
		Assert.notNull(value, "Header value must not be null");
		if (header == null || replace) {
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

		if (!evictions.isEmpty()) {

			boolean log = logger.isTraceEnabled();

			for (CacheOperationContext context : evictions) {
				if (context.isConditionPassing()) {
					CacheEvictOperation evictOp = (CacheEvictOperation) context.operation;

					// for each cache
					// lazy key initialization
					Object key = null;

					for (Cache cache : context.getCaches()) {
						// cache-wide flush
						if (evictOp.isCacheWide()) {
							cache.clear();
							if (log) {
								logger.trace("Invalidating entire cache for definition " + evictOp + " on method " + context.method);
							}
						} else {
							// check key
							if (key == null) {
								key = context.generateKey();
							}
							if (log) {
								logger.trace("Invalidating cache key " + key + " for definition " + evictOp + " on method " + context.method);
							}
							cache.evict(key);
						}
					}
				}
				else {
					if (log) {
						logger.trace("Cache condition failed on method " + context.method + " for definition " + context.operation);
					}
				}
			}
		}
	}
{
		Map<CacheOperationContext, Object> cUpdates = new LinkedHashMap<CacheOperationContext, Object>(
				cacheables.size());

		boolean updateRequire = false;
		Object retVal = null;

		if (!cacheables.isEmpty()) {
			boolean log = logger.isTraceEnabled();
			boolean atLeastOnePassed = false;

			for (CacheOperationContext context : cacheables) {
				if (context.isConditionPassing()) {
					atLeastOnePassed = true;
					Object key = context.generateKey();

					if (log) {
						logger.trace("Computed cache key " + key + " for definition " + context.operation);
					}
					if (key == null) {
						throw new IllegalArgumentException(
								"Null key returned for cache definition (maybe you are using named params on classes without debug info?) "
										+ context.operation);
					}

					// add op/key (in case an update is discovered later on)
					cUpdates.put(context, key);

					boolean localCacheHit = false;

					// check whether the cache needs to be inspected or not (the method will be invoked anyway)
					if (!updateRequire) {
						for (Cache cache : context.getCaches()) {
							Cache.ValueWrapper wrapper = cache.get(key);
							if (wrapper != null) {
								retVal = wrapper.get();
								localCacheHit = true;
								break;
							}
						}
					}

					if (!localCacheHit) {
						updateRequire = true;
					}
				}
				else {
					if (log) {
						logger.trace("Cache condition failed on method " + context.method + " for definition " + context.operation);
					}
				}
			}
			
			// return a status only if at least on cacheable matched
			if (atLeastOnePassed) {
				return new CacheStatus(cUpdates, updateRequire, retVal);
			}
		}

		return null;
	}
{
		for (Map.Entry<CacheOperationContext, Object> entry : updates.entrySet()) {
			for (Cache cache : entry.getKey().getCaches()) {
				cache.put(entry.getValue(), retVal);
			}
		}
	}
{

		Map<String, ?> attributesInSession = this.sessionAttributesHandler.retrieveAttributes(request);
		mavContainer.mergeAttributes(attributesInSession);

		invokeModelAttributeMethods(request, mavContainer);

		for (String name : findSessionAttributeArguments(handlerMethod)) {
			if (!mavContainer.containsAttribute(name)) {
				Object value = this.sessionAttributesHandler.retrieveAttribute(request, name);
				if (value == null) {
					throw new HttpSessionRequiredException("Expected session attribute '" + name + "'");
				}
				mavContainer.addAttribute(name, value);
			}
		}
	}
{
		
		RequestContext requestContext = null;
		if (getWebApplicationContext() != null) {
			requestContext = createRequestContext(request, response, model);
		}
		else {
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			if (wac != null && wac.getServletContext() != null) {
				requestContext = new RequestContext(request, response, wac.getServletContext(), model);
			}
		}

		if (requestContext != null) {
			RequestDataValueProcessor processor = requestContext.getRequestDataValueProcessor();
			if (processor != null) {
				targetUrl = processor.processUrl(request, targetUrl);
			}
		}
		
		return targetUrl;
	}
{
		parsedSql.addNamedParameter(parameter, i - escapes, j - escapes);
		totalParameterCount++;
		return totalParameterCount;
	}
{
		if (!namedParameters.contains(parameter)) {
			namedParameters.add(parameter);
			namedParameterCount++;
		}
		return namedParameterCount;
	}
{
		DataBinder binder = binderFactory.createBinder(request, null, attributeName);
		ConversionService conversionService = binder.getConversionService();
		if (conversionService != null) {
			TypeDescriptor source = TypeDescriptor.valueOf(String.class);
			TypeDescriptor target = new TypeDescriptor(parameter);
			if (conversionService.canConvert(source, target)) {
				return binder.convertIfNecessary(sourceValue, parameter.getParameterType(), parameter);
			}
		}
		return null;
	}
{
		
		Map<String, String> uriTemplateVars = 
			(Map<String, String>) request.getAttribute(
					HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);

		return (uriTemplateVars != null) ? uriTemplateVars : Collections.<String, String>emptyMap();
	}
{
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
		String targetPath = flashMap.getTargetRequestPath();
		flashMap.setTargetRequestPath(decodeAndNormalizePath(targetPath, request));
		flashMap.startExpirationPeriod(this.flashTimeout);
	}
{

		HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
		if (!isMultipartRequest(servletRequest)) {
			throw new MultipartException("The current request is not a multipart request.");
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
				if (isValidationApplicable(arg, parameter)) {
					WebDataBinder binder = binderFactory.createBinder(request, arg, partName);
					binder.validate();
					BindingResult bindingResult = binder.getBindingResult();
					if (bindingResult.hasErrors()) {
						throw new MethodArgumentNotValidException(parameter, bindingResult);
					}
				}
			} 
			catch (MissingServletRequestPartException e) {
				// handled below
				arg = null;
			}
		}

		RequestPart annot = parameter.getParameterAnnotation(RequestPart.class);
		boolean isRequired = (annot != null) ? annot.required() : true;

		if (arg == null && isRequired) {
			throw new MissingServletRequestPartException(partName);
		}
		
		return arg;
	}
{
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		HttpEntity<TestData> jsonEntity = new HttpEntity<TestData>(new TestData("Jason"));
		parts.add("json-data", jsonEntity);
		parts.add("file-data", new ClassPathResource("logo.jpg", this.getClass()));

		URI location = restTemplate.postForLocation(url, parts);
		assertEquals("http://localhost:8080/test/Jason/logo.jpg", location.toString());
	}
{
		Assert.notNull(uri, "'uri' must not be null");
		Matcher m = URI_PATTERN.matcher(uri);
		if (m.matches()) {
			Map<UriComponent, String> result = new EnumMap<UriComponent, String>(UriComponent.class);

			result.put(UriComponent.SCHEME, m.group(2));
			result.put(UriComponent.AUTHORITY, m.group(3));
			result.put(UriComponent.USER_INFO, m.group(5));
			result.put(UriComponent.HOST, m.group(6));
			result.put(UriComponent.PORT, m.group(8));
			result.put(UriComponent.PATH, m.group(9));
			result.put(UriComponent.QUERY, m.group(11));
			result.put(UriComponent.FRAGMENT, m.group(13));

			return result;
		}
		else {
			throw new IllegalArgumentException("[" + uri + "] is not a valid URI");
		}
	}
{
		Assert.notNull(httpUrl, "'httpUrl' must not be null");
		Matcher m = HTTP_URL_PATTERN.matcher(httpUrl);
		if (m.matches()) {
			Map<UriComponent, String> result = new EnumMap<UriComponent, String>(UriComponent.class);

			result.put(UriComponent.SCHEME, m.group(1));
			result.put(UriComponent.AUTHORITY, m.group(2));
			result.put(UriComponent.USER_INFO, m.group(4));
			result.put(UriComponent.HOST, m.group(5));
			result.put(UriComponent.PORT, m.group(7));
			result.put(UriComponent.PATH, m.group(8));
			result.put(UriComponent.QUERY, m.group(10));

			return result;
		}
		else {
			throw new IllegalArgumentException("[" + httpUrl + "] is not a valid HTTP URL");
		}
	}
{
		StringBuilder uriBuilder = new StringBuilder();

		if (scheme != null) {
			uriBuilder.append(scheme);
			uriBuilder.append(':');
		}

		if (userinfo != null || host != null || port != null) {
			uriBuilder.append("//");
			if (userinfo != null) {
				uriBuilder.append(userinfo);
				uriBuilder.append('@');
			}
			if (host != null) {
				uriBuilder.append(host);
			}
			if (port != null) {
				uriBuilder.append(':');
				uriBuilder.append(port);
			}
		}
		else if (authority != null) {
			uriBuilder.append("//");
			uriBuilder.append(authority);
		}

		if (path != null) {
			uriBuilder.append(path);
		}

		if (query != null) {
			uriBuilder.append('?');
			uriBuilder.append(query);
		}

		if (fragment != null) {
			uriBuilder.append('#');
			uriBuilder.append(fragment);
		}

		return uriBuilder.toString();
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
		return expandAsString(encodeUriVariableValues, values);
	}
{
		Assert.notNull(uriVariableValues, "'uriVariableValues' must not be null");
		if (uriVariableValues.length < this.variableNames.size()) {
			throw new IllegalArgumentException(
					"Not enough of variables values in [" + this.uriTemplate + "]: expected at least " +
							this.variableNames.size() + "; got " + uriVariableValues.length);
		}
		Matcher matcher = NAMES_PATTERN.matcher(this.uriTemplate);
		StringBuffer uriBuffer = new StringBuffer();
		int i = 0;
		while (matcher.find()) {
			Object uriVariable = uriVariableValues[i++];
			String uriVariableString = uriVariable != null ? uriVariable.toString() : "";
			if (encodeVariableValues && uriComponent != null) {
				uriVariableString = UriUtils.encode(uriVariableString, uriComponent, false);
			}
			String replacement = Matcher.quoteReplacement(uriVariableString);
			matcher.appendReplacement(uriBuffer, replacement);
		}
		matcher.appendTail(uriBuffer);
		return uriBuffer.toString();
	}
{
		return null;
	}
{
		String acceptHeader = request.getHeader("Accept");
		if (StringUtils.hasLength(acceptHeader)) {
			return MediaType.parseMediaTypes(acceptHeader);
		}
		else {
			return Collections.singletonList(MediaType.ALL);
		}
	}
{
		List<FlashMap> allMaps = retrieveFlashMaps(request, false);
		if (CollectionUtils.isEmpty(allMaps)) {
			return null;
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Looking up previous FlashMap among available FlashMaps: " + allMaps);
		}
		
		List<FlashMap> matches = new ArrayList<FlashMap>();
		for (FlashMap flashMap : allMaps) {
			if (flashMap.matches(request)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Matched " + flashMap);
				}
				matches.add(flashMap);
			}
		}
		
		if (!matches.isEmpty()) {
			Collections.sort(matches);
			FlashMap match = matches.remove(0);
			allMaps.remove(match);
			return Collections.unmodifiableMap(match);
		}
		
		return null;
	}
{
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
	}
{
		List<FlashMap> allMaps = retrieveFlashMaps(request, false);
		if (CollectionUtils.isEmpty(allMaps)) {
			return null;
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Looking up previous FlashMap among available FlashMaps: " + allMaps);
		}
		
		List<FlashMap> matches = new ArrayList<FlashMap>();
		for (FlashMap flashMap : allMaps) {
			if (flashMap.matches(request)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Matched " + flashMap);
				}
				matches.add(flashMap);
			}
		}
		
		if (!matches.isEmpty()) {
			Collections.sort(matches);
			return matches.remove(0);
		}
		
		return null;
	}
{
			if (pattern.equals(lookupPath)) {
				return pattern;
			}
			boolean hasSuffix = pattern.indexOf('.') != -1;
			if (!hasSuffix && pathMatcher.match(pattern + ".*", lookupPath)) {
				return pattern + ".*";
			}
			if (pathMatcher.match(pattern, lookupPath)) {
				return pattern;
			}
			boolean endsWithSlash = pattern.endsWith("/");
			if (!endsWithSlash && pathMatcher.match(pattern + "/", lookupPath)) {
				return pattern + "/";
			}
			return null;
		}
{
		if (triggerKeyClass != null) {
			try {
				Method rescheduleJob = Scheduler.class.getMethod("rescheduleJob", triggerKeyClass, Trigger.class);
				Object key = ReflectionUtils.invokeMethod(Trigger.class.getMethod("getKey"), trigger);
				ReflectionUtils.invokeMethod(rescheduleJob, getScheduler(), key, trigger);
			}
			catch (NoSuchMethodException ex) {
				throw new IllegalStateException("Inconsistent Quartz 2.0 API: " + ex);
			}
		}
		else {
			getScheduler().rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
		}
	}
{
		BeanDefinitionReaderUtils.registerWithGeneratedName(new RootBeanDefinition(ImportAwareBeanPostProcessor.class), registry);
		int registryID = System.identityHashCode(registry);
		if (this.registriesPostProcessed.contains(registryID)) {
			throw new IllegalStateException(
					"postProcessBeanDefinitionRegistry already called for this post-processor against " + registry);
		}
		if (this.factoriesPostProcessed.contains(registryID)) {
			throw new IllegalStateException(
					"postProcessBeanFactory already called for this post-processor against " + registry);
		}
		this.registriesPostProcessed.add(registryID);
		processConfigBeanDefinitions(registry);
	}
{
		int factoryID = System.identityHashCode(beanFactory);
		if (this.factoriesPostProcessed.contains(factoryID)) {
			throw new IllegalStateException(
					"postProcessBeanFactory already called for this post-processor against " + beanFactory);
		}
		this.factoriesPostProcessed.add(factoryID);
		if (!this.registriesPostProcessed.contains(factoryID)) {
			// BeanDefinitionRegistryPostProcessor hook apparently not supported...
			// Simply call processConfigurationClasses lazily at this point then.
			processConfigBeanDefinitions((BeanDefinitionRegistry)beanFactory);
		}
		enhanceConfigurationClasses(beanFactory);
	}
{
		return (clazz != null && isCglibProxyClassName(clazz.getName()));
	}
{
		return (className != null && className.contains(CGLIB_CLASS_SEPARATOR));
	}
{
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
		
		return mergedModel;
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
		
		return targetUrl.toString();
	}
{
		ReflectionUtils.makeAccessible(this.getBridgedMethod());
		try {
			return getBridgedMethod().invoke(getBean(), args);
		}
		catch (IllegalArgumentException e) {
			String msg = getInvocationErrorMessage(e.getMessage(), args);
			throw new IllegalArgumentException(msg, e);
		}
		catch (InvocationTargetException e) { 
			// Unwrap for HandlerExceptionResolvers ...
			Throwable targetException = e.getTargetException();
			if (targetException instanceof RuntimeException) {
				throw (RuntimeException) targetException;
			}
			else if (targetException instanceof Error) {
				throw (Error) targetException;
			}
			else if (targetException instanceof Exception) {
				throw (Exception) targetException;
			}
			else {
				String msg = getInvocationErrorMessage("Failed to invoke controller method", args);
				throw new IllegalStateException(msg, targetException);
			}
		}
	}
{
		MediaType contentType = MediaType.TEXT_PLAIN;
		servletRequest.addHeader("Content-Type", contentType.toString());

		@SuppressWarnings("unchecked")
		HttpMessageConverter<SimpleBean> beanConverter = createMock(HttpMessageConverter.class);
		expect(beanConverter.getSupportedMediaTypes()).andReturn(Collections.singletonList(MediaType.TEXT_PLAIN));
		expect(beanConverter.canRead(SimpleBean.class, contentType)).andReturn(true);
		expect(beanConverter.read(eq(SimpleBean.class), isA(HttpInputMessage.class))).andReturn(simpleBean);
		replay(beanConverter);

		processor = new RequestResponseBodyMethodProcessor(Collections.<HttpMessageConverter<?>>singletonList(beanConverter));
		processor.resolveArgument(paramValidBean, mavContainer, webRequest, new ValidatingBinderFactory());
		
		verify(beanConverter);
	}
{
		PointcutParser parser = initializePointcutParser(classLoader);
		PointcutParameter[] pointcutParameters = new PointcutParameter[this.pointcutParameterNames.length];
		for (int i = 0; i < pointcutParameters.length; i++) {
			pointcutParameters[i] = parser.createPointcutParameter(
					this.pointcutParameterNames[i],
					this.pointcutParameterTypes[i]);
		}
		return parser.parsePointcutExpression(
				replaceBooleanOperators(getExpression()),
				this.pointcutDeclarationScope, pointcutParameters);
	}
{
		ModelMap model = mavContainer.getModel();
		if (model.size() > 0) {
			int lastIndex = model.size()-1;
			String lastKey = new ArrayList<String>(model.keySet()).get(lastIndex);
			if (lastKey.startsWith(BindingResult.MODEL_KEY_PREFIX)) {
				return model.get(lastKey);
			}
		}

		throw new IllegalStateException("Errors/BindingResult argument declared "
				+ "without preceding model attribute. Check your handler method signature!");
	}
{

		List<ContextConfigurationAttributes> configAttributesList = resolveContextConfigurationAttributes(testClass);

		ContextLoader contextLoader = resolveContextLoader(testClass, configAttributesList,
			defaultContextLoaderClassName);

		// Algorithm:
		// - iterate over config attributes
		// -- let loader process locations
		// -- let loader process classes, if it's a SmartContextLoader

		final List<String> locationsList = new ArrayList<String>();
		final List<Class<?>> classesList = new ArrayList<Class<?>>();

		for (ContextConfigurationAttributes configAttributes : configAttributesList) {
			if (logger.isTraceEnabled()) {
				logger.trace(String.format(
					"Processing locations and classes for context configuration attributes [%s]", configAttributes));
			}

			if (contextLoader instanceof SmartContextLoader) {
				SmartContextLoader smartContextLoader = (SmartContextLoader) contextLoader;
				// TODO Decide on mutability of locations and classes properties
				smartContextLoader.processContextConfigurationAttributes(configAttributes);
				locationsList.addAll(Arrays.asList(configAttributes.getLocations()));
				classesList.addAll(Arrays.asList(configAttributes.getClasses()));
			}
			else {
				String[] processedLocations = contextLoader.processLocations(configAttributes.getDeclaringClass(),
					configAttributes.getLocations());
				locationsList.addAll(Arrays.asList(processedLocations));
				// Legacy ContextLoaders don't know how to process classes
			}
		}

		String[] locations = StringUtils.toStringArray(locationsList);
		Class<?>[] classes = ClassUtils.toClassArray(classesList);
		String[] activeProfiles = resolveActiveProfiles(testClass);

		return new MergedContextConfiguration(testClass, locations, classes, activeProfiles, contextLoader);
	}
{
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Loading ApplicationContext for locations [%s].",
				StringUtils.arrayToCommaDelimitedString(locations)));
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
		return expressions.isEmpty();
	}
{
		return expressions.isEmpty();
	}
{
		if (handler == null) {
			return null;
		}
		
		Map<String, HttpRequestHandler> urlMap = new HashMap<String, HttpRequestHandler>();
		urlMap.put("/**", handler);

		SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
		handlerMapping.setOrder(Integer.MAX_VALUE);
		handlerMapping.setUrlMap(urlMap);
		return handlerMapping;
	}
{
		ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver = new ExceptionHandlerExceptionResolver();
		exceptionHandlerExceptionResolver.setMessageConverters(getMessageConverters());
		exceptionHandlerExceptionResolver.afterPropertiesSet();

		exceptionResolvers.add(exceptionHandlerExceptionResolver);
		exceptionResolvers.add(new ResponseStatusExceptionResolver());
		exceptionResolvers.add(new DefaultHandlerExceptionResolver());
	}
{
		if (targetType.getMapKeyTypeDescriptor() == null) {
			// yes
			return true;
		}
		if (sourceType.getMapKeyTypeDescriptor() == null) {
			// maybe
			return true;
		}
		boolean canConvert = conversionService.canConvert(sourceType.getMapKeyTypeDescriptor(), targetType.getMapKeyTypeDescriptor());
		if (canConvert) {
			// yes
			return true;
		} else {
			if (sourceType.getMapKeyTypeDescriptor().getType().isAssignableFrom(targetType.getMapKeyTypeDescriptor().getType())) {
				// maybe;
				return true;
			} else {
				// no;
				return false;
			}
		}
	}
{
		if (weaverToUse == null) {
			if (InstrumentationLoadTimeWeaver.isInstrumentationAvailable()) {
				weaverToUse = new InstrumentationLoadTimeWeaver(beanClassLoader);
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
			Assert.state(populator != null, "DatabasePopulator must be provided");
			try {
				Connection connection = this.dataSource.getConnection();
				try {
					populator.populate(connection);
				} finally {
					try {
						connection.close();
					} catch (SQLException ex) {
						// ignore
					}
				}
			} catch (Exception ex) {
				throw new DataAccessResourceFailureException("Failed to execute database script", ex);
			}
		}
	}
{
		if (!isCollection() && !isArray()) {
			throw new IllegalStateException("Not a java.util.Collection or Array");
		}		
	}
{
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
				assertEquals(count, t.queryForInt("select count(*) from T_TEST"));
			}
		} finally {
			context.close();
		}

	}
{
		assertEquals(name, jdbcTemplate.queryForObject("select NAME from T_TEST", String.class));
	}
{
		Class<?> contextClass = determineContextClass(sc);
		if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass)) {
			throw new ApplicationContextException("Custom context class [" + contextClass.getName() +
					"] is not of type [" + ConfigurableWebApplicationContext.class.getName() + "]");
		}
		ConfigurableWebApplicationContext wac =
				(ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);
		return wac;
	}
{
		if (ObjectUtils.identityToString(wac).equals(wac.getId())) {
			// The application context id is still set to its original default value
			// -> assign a more useful id based on available information
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
		}

		// Determine parent for root web application context, if any.
		ApplicationContext parent = loadParentContext(sc);

		wac.setParent(parent);
		wac.setServletContext(sc);
		wac.setConfigLocation(sc.getInitParameter(CONFIG_LOCATION_PARAM));
		customizeContext(sc, wac);
		wac.refresh();
	}
{

		if (ObjectUtils.identityToString(wac).equals(wac.getId())) {
			// The application context id is still set to its original default value
			// -> assign a more useful id based on available information
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
		}

		wac.setServletContext(getServletContext());
		wac.setServletConfig(getServletConfig());
		wac.setNamespace(getNamespace());
		wac.addApplicationListener(new SourceFilteringListener(wac, new ContextRefreshListener()));

		postProcessWebApplicationContext(wac);

		applyInitializers(wac);

		wac.refresh();
	}
{
		if (source == null) {
			assertNotPrimitiveTargetType(sourceType, targetType);
			return source;
		} else if (sourceType.isAssignableTo(targetType)) {
			logger.debug("No converter found - returning assignable source object as-is");
			return source;				
		}
		else {
			throw new ConverterNotFoundException(sourceType, targetType);
		}		
	}
{
		return metadata.isAnnotated(Configuration.class.getName());
	}
{
		return metadata.isAnnotated(Component.class.getName()) ||
				metadata.hasAnnotatedMethods(Bean.class.getName());
	}
{
		if (messageConverters == null) {
			messageConverters = new ArrayList<HttpMessageConverter<?>>();
			configurers.configureMessageConverters(messageConverters);
			if (messageConverters.isEmpty()) {
				addDefaultHttpMessageConverters(messageConverters);
			}
		}
		return messageConverters;
	}
{

		Set<MediaType> producibleMediaTypes = getProducibleMediaTypes(returnType.getMethod(), returnValue.getClass());
		Set<MediaType> acceptableMediaTypes = getAcceptableMediaTypes(inputMessage);

		List<MediaType> mediaTypes = new ArrayList<MediaType>();
		for (MediaType acceptableMediaType : acceptableMediaTypes) {
			for (MediaType producibleMediaType : producibleMediaTypes) {
				if (acceptableMediaType.isCompatibleWith(producibleMediaType)) {
					mediaTypes.add(getMostSpecificMediaType(acceptableMediaType, producibleMediaType));
				}
			}
		}
		if (mediaTypes.isEmpty()) {
			throw new HttpMediaTypeNotAcceptableException(allSupportedMediaTypes);
		}
		MediaType.sortBySpecificity(mediaTypes);
		MediaType selectedMediaType = null;
		for (MediaType mediaType : mediaTypes) {
			if (mediaType.isConcrete()) {
				selectedMediaType = mediaType;
				break;
			}
			else if (mediaType.equals(MediaType.ALL) || mediaType.equals(MEDIA_TYPE_APPLICATION)) {
				selectedMediaType = MediaType.APPLICATION_OCTET_STREAM;
			}
		}
		if (selectedMediaType != null) {
			for (HttpMessageConverter<?> messageConverter : messageConverters) {
				if (messageConverter.canWrite(returnValue.getClass(), selectedMediaType)) {
					((HttpMessageConverter<T>) messageConverter).write(returnValue, selectedMediaType, outputMessage);
					if (logger.isDebugEnabled()) {
						logger.debug("Written [" + returnValue + "] as \"" + selectedMediaType + "\" using [" +
								messageConverter + "]");
					}
					return;
				}
			}
		}
		else {
			throw new HttpMediaTypeNotAcceptableException(allSupportedMediaTypes);
		}
	}
{
		RequestMapping requestMappingAnn = handlerMethod.getAnnotation(RequestMapping.class);
		if (requestMappingAnn == null) {
			requestMappingAnn = handlerMethod.getClass().getAnnotation(RequestMapping.class);
		}
		Set<MediaType> result = new HashSet<MediaType>();
		if (requestMappingAnn != null) {
			for (String produce : requestMappingAnn.produces()) {
				result.add(MediaType.parseMediaType(produce));
			}
		}
		else {
			for (HttpMessageConverter<?> messageConverter : messageConverters) {
				if (messageConverter.canWrite(returnValueClass, null)) {
					result.addAll(messageConverter.getSupportedMediaTypes());
				}
			}
		}
		if (result.isEmpty()) {
			result.add(MediaType.ALL);
		}
		return result;
	}
{
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(appContext);
		ClassPathResource resource = new ClassPathResource(fileName, AnnotationDrivenBeanDefinitionParserTests.class);
		reader.loadBeanDefinitions(resource);
		assertEquals(expectedBeanCount, appContext.getBeanDefinitionCount());
		appContext.refresh();
	}
{
		String generatedName = generateBeanName(definition, registry, false);
		registry.registerBeanDefinition(generatedName, definition);
		return generatedName;
	}
{
		if (argumentResolvers == null) {
			argumentResolvers = new HandlerMethodArgumentResolverComposite();
		}
		
		// Annotation-based resolvers
		argumentResolvers.addResolver(new RequestParamMethodArgumentResolver(beanFactory, false));
		argumentResolvers.addResolver(new RequestParamMapMethodArgumentResolver());
		argumentResolvers.addResolver(new PathVariableMethodArgumentResolver());
		argumentResolvers.addResolver(new ServletModelAttributeMethodProcessor(false));
		argumentResolvers.addResolver(new RequestResponseBodyMethodProcessor(messageConverters));
		argumentResolvers.addResolver(new RequestHeaderMethodArgumentResolver(beanFactory));
		argumentResolvers.addResolver(new RequestHeaderMapMethodArgumentResolver());
		argumentResolvers.addResolver(new ServletCookieValueMethodArgumentResolver(beanFactory));
		argumentResolvers.addResolver(new ExpressionValueMethodArgumentResolver(beanFactory));
		
		// Custom resolvers
		argumentResolvers.addResolvers(customArgumentResolvers);

		// Type-based resolvers
		argumentResolvers.addResolver(new ServletRequestMethodArgumentResolver());
		argumentResolvers.addResolver(new ServletResponseMethodArgumentResolver());
		argumentResolvers.addResolver(new HttpEntityMethodProcessor(messageConverters));
		argumentResolvers.addResolver(new ModelMethodProcessor());
		argumentResolvers.addResolver(new ErrorsMethodArgumentResolver());
		
		// Default-mode resolution
		argumentResolvers.addResolver(new RequestParamMethodArgumentResolver(beanFactory, true));
		argumentResolvers.addResolver(new ServletModelAttributeMethodProcessor(true));
	}
{
		if (initBinderArgumentResolvers == null) {
			initBinderArgumentResolvers = new HandlerMethodArgumentResolverComposite();
		}

		// Annotation-based resolvers
		initBinderArgumentResolvers.addResolver(new RequestParamMethodArgumentResolver(beanFactory, false));
		initBinderArgumentResolvers.addResolver(new RequestParamMapMethodArgumentResolver());
		initBinderArgumentResolvers.addResolver(new PathVariableMethodArgumentResolver());
		initBinderArgumentResolvers.addResolver(new ExpressionValueMethodArgumentResolver(beanFactory));

		// Custom resolvers
		argumentResolvers.addResolvers(customArgumentResolvers);

		// Type-based resolvers
		initBinderArgumentResolvers.addResolver(new ServletRequestMethodArgumentResolver());
		initBinderArgumentResolvers.addResolver(new ServletResponseMethodArgumentResolver());
		
		// Default-mode resolution
		initBinderArgumentResolvers.addResolver(new RequestParamMethodArgumentResolver(beanFactory, true));
	}
{
		if (returnValueHandlers == null) {
			returnValueHandlers = new HandlerMethodReturnValueHandlerComposite();
		}
		
		// Annotation-based handlers
		returnValueHandlers.addHandler(new RequestResponseBodyMethodProcessor(messageConverters));
		returnValueHandlers.addHandler(new ModelAttributeMethodProcessor(false));
		
		// Custom return value handlers
		returnValueHandlers.addHandlers(customReturnValueHandlers);
		
		// Type-based handlers
		returnValueHandlers.addHandler(new ModelAndViewMethodReturnValueHandler());
		returnValueHandlers.addHandler(new ModelMethodProcessor());
		returnValueHandlers.addHandler(new ViewMethodReturnValueHandler());
		returnValueHandlers.addHandler(new HttpEntityMethodProcessor(messageConverters));
		
		// Default handler
		returnValueHandlers.addHandler(new DefaultMethodReturnValueHandler(modelAndViewResolvers));
	}
{
		String name = info.name;
		if (info.name.length() == 0) {
			name = parameter.getParameterName();
			Assert.notNull(name, "Name for argument type [" + parameter.getParameterType().getName()
						+ "] not available, and parameter name information not found in class file either.");
		}
		String defaultValue = (ValueConstants.DEFAULT_NONE.equals(info.defaultValue) ? null : info.defaultValue);
		return new NamedValueInfo(name, info.required, defaultValue);
	}
{
		if (argumentResolvers == null) {
			argumentResolvers = new HandlerMethodArgumentResolverComposite();
			registerArgumentResolvers(customArgumentResolvers);
			registerArgumentResolvers(getDefaultArgumentResolvers(messageConverters, beanFactory));
		}
		if (returnValueHandlers == null) {
			returnValueHandlers = new HandlerMethodReturnValueHandlerComposite();
			registerReturnValueHandlers(customReturnValueHandlers);
			registerReturnValueHandlers(getDefaultReturnValueHandlers(messageConverters, modelAndViewResolvers));
		}
		if (initBinderArgumentResolvers == null) {
			initBinderArgumentResolvers = new HandlerMethodArgumentResolverComposite();
			registerInitBinderArgumentResolvers(customArgumentResolvers);
			registerInitBinderArgumentResolvers(getDefaultInitBinderArgumentResolvers(beanFactory));
		}
	}
{
		if (argumentResolvers == null) {
			argumentResolvers = new HandlerMethodArgumentResolverComposite();
			registerArgumentResolvers(customArgumentResolvers);
			registerArgumentResolvers(getDefaultArgumentResolvers());
		}
		if (returnValueHandlers == null) {
			returnValueHandlers = new HandlerMethodReturnValueHandlerComposite();
			registerReturnValueHandlers(customReturnValueHandlers);
			registerReturnValueHandlers(getDefaultReturnValueHandlers(messageConverters));
		}
	}
{
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		return (requestAttributes instanceof NativeWebRequest) ? (NativeWebRequest) requestAttributes : null; 
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
				argumentResolvers.registerArgumentResolver(new WebArgumentResolverAdapter(customResolver));
			}
		}	
	}
{
		if (returnValueHandlers != null) {
			return;
		}
		returnValueHandlers = new HandlerMethodReturnValueHandlerComposite();

		// Annotation-based handlers
		returnValueHandlers.registerReturnValueHandler(new RequestResponseBodyMethodProcessor(messageConverters));
		returnValueHandlers.registerReturnValueHandler(new ModelAttributeMethodProcessor(false));
		
		// Type-based handlers
		returnValueHandlers.registerReturnValueHandler(new ModelAndViewMethodReturnValueHandler());
		returnValueHandlers.registerReturnValueHandler(new ModelMethodProcessor());
		returnValueHandlers.registerReturnValueHandler(new ViewMethodReturnValueHandler());
		returnValueHandlers.registerReturnValueHandler(new HttpEntityMethodProcessor(messageConverters));
		
		// Default handler
		returnValueHandlers.registerReturnValueHandler(new DefaultMethodReturnValueHandler(customModelAndViewResolvers));
	}
{
		Assert.notNull(contextLoader, "ContextLoader must not be null");
		Assert.notNull(clazz, "Class must not be null");

		Class<ContextConfiguration> annotationType = ContextConfiguration.class;
		Class<?> declaringClass = AnnotationUtils.findAnnotationDeclaringClass(annotationType, clazz);
		Assert.notNull(declaringClass, String.format(
			"Could not find an 'annotation declaring class' for annotation type [%s] and class [%s]", annotationType,
			clazz));

		boolean processConfigurationClasses = (contextLoader instanceof ResourceTypeAwareContextLoader)
				&& ((ResourceTypeAwareContextLoader) contextLoader).supportsClassResources();
		LocationsResolver locationsResolver = processConfigurationClasses ? classNameLocationsResolver
				: resourcePathLocationsResolver;

		List<String> locationsList = new ArrayList<String>();

		while (declaringClass != null) {
			ContextConfiguration contextConfiguration = declaringClass.getAnnotation(annotationType);

			if (logger.isTraceEnabled()) {
				logger.trace(String.format("Retrieved @ContextConfiguration [%s] for declaring class [%s].",
					contextConfiguration, declaringClass));
			}

			String[] resolvedLocations = locationsResolver.resolveLocations(contextConfiguration, declaringClass);
			String[] processedLocations = contextLoader.processLocations(declaringClass, resolvedLocations);
			locationsList.addAll(0, Arrays.<String> asList(processedLocations));

			declaringClass = contextConfiguration.inheritLocations() ? AnnotationUtils.findAnnotationDeclaringClass(
				annotationType, declaringClass.getSuperclass()) : null;
		}

		return locationsList.toArray(new String[locationsList.size()]);
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

		// TODO [SPR-6184] Implement recursive search for configuration classes.

		ContextConfiguration contextConfiguration = declaringClass.getAnnotation(annotationType);
		if (logger.isTraceEnabled()) {
			logger.trace(String.format("Retrieved @ContextConfiguration [%s] for declaring class [%s].",
				contextConfiguration, declaringClass));
		}

		String[] classNames = null;

		Class<?>[] configClasses = contextConfiguration.classes();
		if (!ObjectUtils.isEmpty(configClasses)) {
			classNames = new String[configClasses.length];

			for (int i = 0; i < configClasses.length; i++) {
				classNames[i] = configClasses[i].getName();
			}
		}

		return contextLoader.processLocations(declaringClass, classNames);
	}
{
		Assert.notNull(testClass, "Test class must not be null");
		Assert.notNull(contextCache, "ContextCache must not be null");

		ContextConfiguration contextConfiguration = testClass.getAnnotation(ContextConfiguration.class);
		ContextLoader contextLoader = null;
		String[] locations = null;

		if (contextConfiguration == null) {
			if (logger.isInfoEnabled()) {
				logger.info(String.format("@ContextConfiguration not found for class [%s]", testClass));
			}
		}
		else {
			if (logger.isTraceEnabled()) {
				logger.trace(String.format("Retrieved @ContextConfiguration [%s] for class [%s]", contextConfiguration,
					testClass));
			}
			contextLoader = ContextLoaderUtils.resolveContextLoader(testClass, defaultContextLoaderClassName);
			locations = ContextLoaderUtils.resolveContextLocations(contextLoader, testClass);
		}

		this.testClass = testClass;
		this.contextCache = contextCache;
		this.contextLoader = contextLoader;
		this.locations = locations;
	}
{
		createBeanDefinitionReader(context).loadBeanDefinitions(locations);
	}
{
		String generatedName = generateBeanName(definition, registry, false);
		registry.registerBeanDefinition(generatedName, definition);
		return generatedName;
	}
{
		XmlReaderContext readerContext = parserContext.getReaderContext();
		ClassLoader classLoader = readerContext.getResourceLoader().getClassLoader();

		ComponentScanSpec spec =
			ComponentScanSpec.forDelimitedPackages(element.getAttribute("base-package"))
			.includeAnnotationConfig(element.getAttribute("annotation-config"))
			.useDefaultFilters(element.getAttribute("use-default-filters"))
			.resourcePattern(element.getAttribute("resource-pattern"))
			.beanNameGenerator(element.getAttribute("name-generator"), classLoader)
			.scopeMetadataResolver(element.getAttribute("scope-resolver"), classLoader)
			.scopedProxyMode(element.getAttribute("scoped-proxy"));

		// Parse exclude and include filter elements.
		NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String localName = parserContext.getDelegate().getLocalName(node);
				String filterType = ((Element)node).getAttribute("type");
				String expression = ((Element)node).getAttribute("expression");
				if ("include-filter".equals(localName)) {
					spec.addIncludeFilter(filterType, expression, classLoader);
				}
				else if ("exclude-filter".equals(localName)) {
					spec.addExcludeFilter(filterType, expression, classLoader);
				}
			}
		}

		spec.beanDefinitionDefaults(parserContext.getDelegate().getBeanDefinitionDefaults())
			.autowireCandidatePatterns(parserContext.getDelegate().getAutowireCandidatePatterns())
			.source(readerContext.extractSource(element))
			.sourceName(element.getTagName())
			.execute(createExecutorContext(parserContext));
		return null;
	}
{
		Set<Class<?>> rawFieldTypes = new HashSet<Class<?>>(7);
		rawFieldTypes.add(ReadableInstant.class);
		rawFieldTypes.add(LocalDate.class);
		rawFieldTypes.add(LocalTime.class);
		rawFieldTypes.add(LocalDateTime.class);
		rawFieldTypes.add(Date.class);
		rawFieldTypes.add(Calendar.class);
		rawFieldTypes.add(Long.class);
		return Collections.unmodifiableSet(rawFieldTypes);		
	}
{
		if (this.formatters != null) {
			for (Object formatter : this.formatters) {
				if (formatter instanceof Formatter<?>) {
					this.conversionService.addFormatter((Formatter<?>) formatter);
				} else if (formatter instanceof AnnotationFormatterFactory<?>) {
					this.conversionService.addFormatterForFieldAnnotation((AnnotationFormatterFactory<?>) formatter);
				} else {
					throw new IllegalArgumentException(
							"Custom formatters must be implementations of Formatter or AnnotationFormatterFactory");
				}
			}
		}
		if (this.formatterRegistrars != null) {
			for (FormatterRegistrar registrar : this.formatterRegistrars) {
				registrar.registerFormatters(this.conversionService);
			}
		}
		installFormatters(this.conversionService);
	}
{
		MethodParameter methodParameter = new MethodParameter(parentMethodParameter);
		methodParameter.increaseNestingLevel();
		return methodParameter;
	}
{
		for (PropertySource<?> propertySource : propertySources) {
			this.addLast(propertySource);
		}
	}
{
		return getFirst(ETAG);
	}
{
		this.reader.setEnvironment(environment);
		this.scanner.setEnvironment(environment);
	}
{
		if (allowNullValues && val == NULL_HOLDER) {
			return null;
		}
		return val;
	}
{
		if (this.activeProfiles.isEmpty()) {
			String profiles = getProperty(ACTIVE_PROFILES_PROPERTY_NAME);
			if (StringUtils.hasText(profiles)) {
				this.activeProfiles = commaDelimitedListToSet(trimAllWhitespace(profiles));
			}
		}
		return Collections.unmodifiableSet(activeProfiles);
	}
{
		String profileSpec = root.getAttribute(PROFILE_ATTRIBUTE);
		boolean isCandidate = false;
		if (profileSpec == null || profileSpec.equals("")) {
			isCandidate = true;
		} else {
			String[] profiles = commaDelimitedListToStringArray(trimAllWhitespace(profileSpec));
			for (String profile : profiles) {
				if (this.environment.getActiveProfiles().contains(profile)) {
					isCandidate = true;
					break;
				}
			}
		}

		if (!isCandidate) {
			// TODO SPR-7508 logging
			// logger.debug(format("XML is targeted for environment [%s], but current environment is [%s]. Skipping", targetEnvironment, environment == null ? null : environment.getName()));
			return;
		}

		// any nested <beans> elements will cause recursion in this method. in
		// order to propagate and preserve <beans> default-* attributes correctly,
		// keep track of the current (parent) delegate, which may be null. Create
		// the new (child) delegate with a reference to the parent for fallback purposes,
		// then ultimately reset this.delegate back to its original (parent) reference.
		// this behavior emulates a stack of delegates without actually necessitating one.
		BeanDefinitionParserDelegate parent = this.delegate;
		this.delegate = createHelper(readerContext, root, parent);

		preProcessXml(root);
		parseBeanDefinitions(root, this.delegate);
		postProcessXml(root);

		this.delegate = parent;
	}
{
		return this.environment;
	}
{
		Map<String, Class<?>> result = new LinkedHashMap<String, Class<?>>(map.size());

		for (Map.Entry<String, ?> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			Class type;
			if (value instanceof Class) {
				type = (Class) value;
			}
			else if (value instanceof String) {
				String s = (String) value;
				type = ClassUtils.forName(s, classLoader);
			}
			else {
				throw new IllegalArgumentException("Unknown value [" + value + "], expected String or Class");
			}
			result.put(key, type);
		}
		return result;
	}
{
		try {
			new EmbeddedDriver().connect(
					String.format(URL_TEMPLATE, databaseName, SHUTDOWN_COMMAND), new Properties());
		}
		catch (SQLException ex) {
			if (!SHUTDOWN_CODE.equals(ex.getSQLState())) {
				logger.warn("Could not shutdown in-memory Derby database", ex);
				return;
			}
			if (!IS_AT_LEAST_DOT_SIX) {
				// Explicitly purge the in-memory database, to prevent it
				// from hanging around after being shut down.
				try {
					VFMemoryStorageFactory.purgeDatabase(new File(databaseName).getCanonicalPath());
				}
				catch (IOException ex2) {
					logger.warn("Could not purge in-memory Derby database", ex2);
				}
			}
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
					if (nativeJdbcExtractor != null) {
						provider.setNativeJdbcExtractor(nativeJdbcExtractor);
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
		Class[] classes = (Class[]) getValue();
		if (ObjectUtils.isEmpty(classes)) {
			return "";
		}
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
	}
{
		return new StaxSource(streamReader);
	}
{
		return new StaxResult(streamWriter);
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
		return null;
	}
{
			RequestMappingInfo mappingInfo = new RequestMappingInfo();
			RequestMapping mapping = AnnotationUtils.findAnnotation(handlerMethod, RequestMapping.class);
			mappingInfo.patterns = mapping.value();
			if (!hasTypeLevelMapping() || !Arrays.equals(mapping.method(), getTypeLevelMapping().method())) {
				mappingInfo.methods = mapping.method();
			}
			if (!hasTypeLevelMapping() || !Arrays.equals(mapping.params(), getTypeLevelMapping().params())) {
				mappingInfo.params = mapping.params();
			}
			if (!hasTypeLevelMapping() || !Arrays.equals(mapping.headers(), getTypeLevelMapping().headers())) {
				mappingInfo.headers = mapping.headers();
			}
			return mappingInfo;
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
			} else if (value.getClass().getComponentType().isArray()) {
				List<Object> l = Arrays.asList((Object[]) value);
				if (!isNested) {
					sb.append(value.getClass().getComponentType().getName());
				}
				sb.append("[").append(l.size()).append("]{");
				int i = 0;
				for (Object object : l) {
					if (i > 0) {
						sb.append(",");
					}
					i++;
					sb.append(stringValueOf(object, true));
				}
				sb.append("}");
			} else {
				List<Object> l = Arrays.asList((Object[]) value);
				if (!isNested) {
					sb.append(value.getClass().getComponentType().getName());
				}
				sb.append("[").append(l.size()).append("]{");
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
			List<PropertyAccessor> defaultAccessors = new ArrayList<PropertyAccessor>();
			defaultAccessors.add(new ReflectivePropertyAccessor());
			this.propertyAccessors = defaultAccessors;
		}
	}
{
		if (this.methodResolvers == null) {
			List<MethodResolver> defaultResolvers = new ArrayList<MethodResolver>();
			defaultResolvers.add(reflectiveMethodResolver = new ReflectiveMethodResolver());
			this.methodResolvers = defaultResolvers;
		}
	}
{
		if (this.constructorResolvers == null) {
			List<ConstructorResolver> defaultResolvers = new ArrayList<ConstructorResolver>();
			defaultResolvers.add(new ReflectiveConstructorResolver());
			this.constructorResolvers = defaultResolvers;
		}
	}
{
		Set<Class<?>> rawFieldTypes = new HashSet<Class<?>>(7);
		rawFieldTypes.add(Short.class);
		rawFieldTypes.add(Integer.class);
		rawFieldTypes.add(Long.class);
		rawFieldTypes.add(Float.class);
		rawFieldTypes.add(Double.class);
		rawFieldTypes.add(BigDecimal.class);
		rawFieldTypes.add(BigInteger.class);
		this.fieldTypes = Collections.unmodifiableSet(rawFieldTypes);
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
				.toDate(), new TypeDescriptor(modelClass.getField("date")), TypeDescriptor.valueOf(String.class));
		assertEquals("10/31/09", formatted);
		LocalDate date = new LocalDate(formattingService.convert("10/31/09", TypeDescriptor.valueOf(String.class),
				new TypeDescriptor(modelClass.getField("date"))));
		assertEquals(new LocalDate(2009, 10, 31), date);

		List<Date> dates = new ArrayList<Date>();
		dates.add(new LocalDate(2009, 10, 31).toDateTimeAtCurrentTime().toDate());
		dates.add(new LocalDate(2009, 11, 1).toDateTimeAtCurrentTime().toDate());
		dates.add(new LocalDate(2009, 11, 2).toDateTimeAtCurrentTime().toDate());
		formatted = (String) formattingService.convert(dates,
				new TypeDescriptor(modelClass.getField("dates")), TypeDescriptor.valueOf(String.class));
		assertEquals("10/31/09,11/1/09,11/2/09", formatted);
		dates = (List<Date>) formattingService.convert("10/31/09,11/1/09,11/2/09",
				TypeDescriptor.valueOf(String.class), new TypeDescriptor(modelClass.getField("dates")));
		assertEquals(new LocalDate(2009, 10, 31), new LocalDate(dates.get(0)));
		assertEquals(new LocalDate(2009, 11, 1), new LocalDate(dates.get(1)));
		assertEquals(new LocalDate(2009, 11, 2), new LocalDate(dates.get(2)));
	}
{
		if (registry instanceof PropertyEditorRegistrySupport) {
			((PropertyEditorRegistrySupport) registry).overrideDefaultEditor(requiredType, editor);
		}
		else {
			registry.registerCustomEditor(requiredType, editor);
		}
	}
{
		ApplicationContext ctx = new ClassPathXmlApplicationContext(fileName, getClass());
		ResourceTestBean tb = ctx.getBean("resourceTestBean", ResourceTestBean.class);
		assertTrue(resourceClass.isInstance(tb.getResource()));
		assertTrue(tb.getResourceArray().length > 0);
		assertTrue(resourceClass.isInstance(tb.getResourceArray()[0]));
		assertTrue(tb.getResourceMap().size() == 1);
		assertTrue(resourceClass.isInstance(tb.getResourceMap().get("key1")));
		assertTrue(tb.getResourceArrayMap().size() == 1);
		assertTrue(tb.getResourceArrayMap().get("key1").length > 0);
		assertTrue(resourceClass.isInstance(tb.getResourceArrayMap().get("key1")[0]));
	}
{
		if (bf.containsBeanDefinition(beanName)) {
			BeanDefinition bd = bf.getMergedBeanDefinition(beanName);
			if (bd instanceof AbstractBeanDefinition) {
				AbstractBeanDefinition abd = (AbstractBeanDefinition) bd;
				AutowireCandidateQualifier candidate = abd.getQualifier(Qualifier.class.getName());
				if ((candidate != null && qualifier.equals(candidate.getAttribute(AutowireCandidateQualifier.VALUE_KEY))) ||
						qualifier.equals(beanName) || ObjectUtils.containsElement(bf.getAliases(beanName), qualifier)) {
					return true;
				}
			}
			if (bd instanceof RootBeanDefinition) {
				Method factoryMethod = ((RootBeanDefinition) bd).getResolvedFactoryMethod();
				if (factoryMethod != null) {
					Qualifier targetAnnotation = factoryMethod.getAnnotation(Qualifier.class);
					if (targetAnnotation != null && qualifier.equals(targetAnnotation.value())) {
						return true;
					}
				}
			}
		}
		return false;
	}
{
		try {
			return this.typeConverterDelegate.convertIfNecessary(propertyName, oldValue, newValue, requiredType, td);
		}
		catch (ConverterNotFoundException ex) {
			PropertyChangeEvent pce =
					new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, newValue);
			throw new ConversionNotSupportedException(pce, td.getType(), ex);
		}
		catch (ConversionException ex) {
			PropertyChangeEvent pce =
					new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, newValue);
			throw new TypeMismatchException(pce, requiredType, ex);
		}
		catch (IllegalStateException ex) {
			PropertyChangeEvent pce =
					new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, newValue);
			throw new ConversionNotSupportedException(pce, requiredType, ex);
		}
		catch (IllegalArgumentException ex) {
			PropertyChangeEvent pce =
					new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, newValue);
			throw new TypeMismatchException(pce, requiredType, ex);
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
		if (features != null) {
			clientDef.getPropertyValues().add("webServiceFeatures", features);
		}
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
		List<View> candidateViews = new ArrayList<View>();

		for (ViewResolver viewResolver : this.viewResolvers) {
			View view = viewResolver.resolveViewName(viewName, locale);
			if (view != null) {
				candidateViews.add(view);
			}
			for (MediaType requestedMediaType : requestedMediaTypes) {
				List<String> extensions = getExtensionsForMediaType(requestedMediaType);
				for (String extension : extensions) {
					String viewNameWithExtension = viewName + "." + extension;
					view = viewResolver.resolveViewName(viewNameWithExtension, locale);
					if (view != null) {
						candidateViews.add(view);
					}
				}

			}
		}

		if (!CollectionUtils.isEmpty(this.defaultViews)) {
			candidateViews.addAll(this.defaultViews);
		}
		return candidateViews;
	}
{
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
				if (logger.isDebugEnabled()) {
					logger.debug(
							"Returning [" + bestView + "] based on requested media type '" + bestRequestedMediaType +
									"'");
				}
				break;
			}
		}
		return bestView;

	}
{
		if (this.field != null) {
			return this.field.getAnnotations();
		}
		else if (this.methodParameter != null) {
			if (this.methodParameter.getParameterIndex() < 0) {
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

		ByteArrayOutputStream bos = new ByteArrayOutputStream(source.length * 2);

		for (int i = 0; i < source.length; i++) {
			int b = source[i];
			if (b < 0) {
				b += 256;
			}
			if (notEncoded.get(b)) {
				bos.write(b);
			}
			else {
				bos.write('%');
				char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, 16));
				char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
				bos.write(hex1);
				bos.write(hex2);
			}
		}
		return bos.toByteArray();
	}
{
		PropertyEditor uriEditor = new URIEditor();
		uriEditor.setAsText(uriSpec);
		Object value = uriEditor.getValue();
		assertTrue(value instanceof URI);
		URI uri = (URI) value;
		assertEquals(uriSpec, uri.toString());
	}
{

		Method handlerMethodToInvoke = BridgeMethodResolver.findBridgedMethod(handlerMethod);
		try {
			boolean debug = logger.isDebugEnabled();
			for (String attrName : this.methodResolver.getActualSessionAttributeNames()) {
				Object attrValue = this.sessionAttributeStore.retrieveAttribute(webRequest, attrName);
				if (attrValue != null) {
					implicitModel.addAttribute(attrName, attrValue);
				}
			}
			for (Method attributeMethod : this.methodResolver.getModelAttributeMethods()) {
				Method attributeMethodToInvoke = BridgeMethodResolver.findBridgedMethod(attributeMethod);
				Object[] args = resolveHandlerArguments(attributeMethodToInvoke, handler, webRequest, implicitModel);
				if (debug) {
					logger.debug("Invoking model attribute method: " + attributeMethodToInvoke);
				}
				String attrName = AnnotationUtils.findAnnotation(attributeMethodToInvoke, ModelAttribute.class).value();
				if (!"".equals(attrName) && implicitModel.containsAttribute(attrName)) {
					continue;
				}
				ReflectionUtils.makeAccessible(attributeMethodToInvoke);
				Object attrValue = attributeMethodToInvoke.invoke(handler, args);
				if ("".equals(attrName)) {
					Class resolvedType = GenericTypeResolver.resolveReturnType(attributeMethodToInvoke, handler.getClass());
					attrName = Conventions.getVariableNameForReturnType(attributeMethodToInvoke, resolvedType, attrValue);
				}
				if (!implicitModel.containsAttribute(attrName)) {
					implicitModel.addAttribute(attrName, attrValue);
				}
			}
			Object[] args = resolveHandlerArguments(handlerMethodToInvoke, handler, webRequest, implicitModel);
			if (debug) {
				logger.debug("Invoking request handler method: " + handlerMethodToInvoke);
			}
			ReflectionUtils.makeAccessible(handlerMethodToInvoke);
			return handlerMethodToInvoke.invoke(handler, args);
		}
		catch (IllegalStateException ex) {
			// Internal assertion failed (e.g. invalid signature):
			// throw exception with full handler method context...
			throw new HandlerMethodInvocationException(handlerMethodToInvoke, ex);
		}
		catch (InvocationTargetException ex) {
			// User-defined @ModelAttribute/@InitBinder/@RequestMapping method threw an exception...
			ReflectionUtils.rethrowException(ex.getTargetException());
			return null;
		}
	}
{

		if (this.bindingInitializer != null) {
			this.bindingInitializer.initBinder(binder, webRequest);
		}
		if (handler != null) {
			Set<Method> initBinderMethods = this.methodResolver.getInitBinderMethods();
			if (!initBinderMethods.isEmpty()) {
				boolean debug = logger.isDebugEnabled();
				for (Method initBinderMethod : initBinderMethods) {
					Method methodToInvoke = BridgeMethodResolver.findBridgedMethod(initBinderMethod);
					String[] targetNames = AnnotationUtils.findAnnotation(methodToInvoke, InitBinder.class).value();
					if (targetNames.length == 0 || Arrays.asList(targetNames).contains(attrName)) {
						Object[] initBinderArgs =
								resolveInitBinderArguments(handler, methodToInvoke, binder, webRequest);
						if (debug) {
							logger.debug("Invoking init-binder method: " + methodToInvoke);
						}
						ReflectionUtils.makeAccessible(methodToInvoke);
						Object returnValue = methodToInvoke.invoke(handler, initBinderArgs);
						if (returnValue != null) {
							throw new IllegalStateException(
									"InitBinder methods must not have a return value: " + methodToInvoke);
						}
					}
				}
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
			List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
			if (acceptedMediaTypes.isEmpty()) {
				acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
			}
			MediaType.sortBySpecificity(acceptedMediaTypes);
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
		MediaType contentType = inputMessage.getHeaders().getContentType();
		if (contentType == null) {
			StringBuilder builder = new StringBuilder(ClassUtils.getShortName(methodParam.getParameterType()));
			String paramName = methodParam.getParameterName();
			if (paramName != null) {
				builder.append(' ');
				builder.append(paramName);
			}
			throw new HttpMediaTypeNotSupportedException(
					"Cannot extract parameter (" + builder.toString() + "): no Content-Type found");
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
		if (state.getConfiguration().isAutoGrowCollections()) {
			Object newCollectionElement = null;
			try {
				int newElements = index-collection.size();
				if (elementType == null) {
					throw new SpelEvaluationException(getStartPosition(), SpelMessage.UNABLE_TO_GROW_COLLECTION_UNKNOWN_ELEMENT_TYPE);	
				}
				while (newElements>0) {
					collection.add(elementType.newInstance());
					newElements--;
				}
				newCollectionElement = elementType.newInstance();
			}
			catch (Exception ex) {
				throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.UNABLE_TO_GROW_COLLECTION);
			}
			collection.add(newCollectionElement);
			return true;
		}
		return false;
	}
{
		if (this.configurationAttributes == null) {
			Class<?> clazz = testContext.getTestClass();
			Class<TransactionConfiguration> annotationType = TransactionConfiguration.class;
			TransactionConfiguration config = clazz.getAnnotation(annotationType);
			if (logger.isDebugEnabled()) {
				logger.debug("Retrieved @TransactionConfiguration [" + config + "] for test class [" + clazz + "]");
			}

			String transactionManagerName;
			boolean defaultRollback;
			if (config != null) {
				transactionManagerName = config.transactionManager();
				defaultRollback = config.defaultRollback();
			}
			else {
				transactionManagerName = (String) AnnotationUtils.getDefaultValue(annotationType, "transactionManager");
				defaultRollback = (Boolean) AnnotationUtils.getDefaultValue(annotationType, "defaultRollback");
			}

			TransactionConfigurationAttributes configAttributes =
					new TransactionConfigurationAttributes(transactionManagerName, defaultRollback);
			if (logger.isDebugEnabled()) {
				logger.debug("Retrieved TransactionConfigurationAttributes [" + configAttributes + "] for class [" + clazz + "]");
			}
			this.configurationAttributes = configAttributes;
		}
		return this.configurationAttributes;
	}
{
		if (arg instanceof TypeVariable) {
			TypeVariable tv = (TypeVariable) arg;
			arg = getTypeVariableMap(ownerClass).get(tv);
			if (arg == null) {
				arg = extractBoundForTypeVariable(tv);
			}
			else {
				arg = extractClass(ownerClass, arg);
			}
		}
		else if (arg instanceof GenericArrayType) {
			GenericArrayType gat = (GenericArrayType) arg;
			Type gt = gat.getGenericComponentType();
			Class<?> componentClass = extractClass(ownerClass, gt);
			arg = Array.newInstance(componentClass, 0).getClass();
		}
		return (arg instanceof Class ? (Class) arg : Object.class);
	}
{
		Map<String, String> parameters = Collections.singletonMap("boundary", new String(boundary, "US-ASCII"));
		MediaType contentType = new MediaType(MediaType.MULTIPART_FORM_DATA, parameters);
		headers.setContentType(contentType);
	}
{
		for (Map.Entry<String,List<Object>> entry : map.entrySet()) {
			String name = entry.getKey();
			for (Object part : entry.getValue()) {
				writeBoundary(boundary, os);
				writePart(name, part, os);
				writeNewLine(os);
			}
		}
	}
{
		os.write('-');
		os.write('-');
		os.write(boundary);
		os.write('-');
		os.write('-');
		writeNewLine(os);
	}
{
		this.declaredRowMappers.put(parameterName, rowMapper);
		if (logger.isDebugEnabled()) {
			logger.debug("Added row mapper for [" + getProcedureName() + "]: " + parameterName);
		}
	}
{
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
	}
{
		
		Assert.hasLength(encoding, "'encoding' must not be empty");
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
	}
{
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			marshalOutputStream(graph, os);
			ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
			Transformer transformer = this.transformerFactory.newTransformer();
			transformer.transform(new StreamSource(is), result);
		}
		catch (TransformerException ex) {
			throw new MarshallingFailureException(
					"Could not transform to [" + ClassUtils.getShortName(result.getClass()) + "]");
		}

	}
{
		try {
			Transformer transformer = transformerFactory.newTransformer();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			transformer.transform(source, new StreamResult(os));
			ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
			return unmarshalInputStream(is);
		}
		catch (TransformerException ex) {
			throw new MarshallingFailureException(
					"Could not transform from [" + ClassUtils.getShortName(source.getClass()) + "]");
		}
	}
{
		runTestClassAndAssertStats(CleanTestCase.class, 1);
		assertCacheStats("after clean test class", 1, cacheHits.get(), cacheMisses.incrementAndGet());
	}
{
		Object value = typedValue.getValue();
		
		if (targetType == null || ClassUtils.isAssignableValue(targetType, value)) {
			return (T) value;
		}
		if (context != null) {
			return (T) context.getTypeConverter().convertValue(value, typedValue.getTypeDescriptor(), TypeDescriptor.valueOf(targetType));
		}
		throw new EvaluationException("Cannot convert value '" + value + "' to type '" + targetType.getName() + "'");
	}
{
		for (Map.Entry<String, String> entry : map.entrySet()) {
			builder.append(';');
			builder.append(entry.getKey());
			builder.append('=');
			builder.append(entry.getValue());
		}
	}
{
			if (this.targetFactory instanceof EntityManagerFactoryInfo) {
				this.proxyClassLoader = ((EntityManagerFactoryInfo) this.targetFactory).getBeanClassLoader();
			}
			else {
				this.proxyClassLoader = EntityManagerFactory.class.getClassLoader();
			}
		}
{
		if (this.importStack.contains(configClass)) {
			this.problemReporter.error(new CircularImportProblem(configClass, this.importStack, configClass.getMetadata()));
		}
		else {
			this.importStack.push(configClass);
			for (String classToImport : classesToImport) {
				MetadataReader reader = this.metadataReaderFactory.getMetadataReader(classToImport);
				processConfigurationClass(new ConfigurationClass(reader, null));
			}
			this.importStack.pop();
		}
	}
{
		response.setContentLength(body.length);
		if (body.length > 0) {
			FileCopyUtils.copy(body, response.getOutputStream());
		}
	}
{
		if (checkForXmlRootElement && AnnotationUtils.findAnnotation(clazz, XmlRootElement.class) == null) {
			return false;
		}
		if (AnnotationUtils.findAnnotation(clazz, XmlType.class) == null) {
			return false;
		}
		if (StringUtils.hasLength(getContextPath())) {
			String packageName = ClassUtils.getPackageName(clazz);
			String[] contextPaths = StringUtils.tokenizeToStringArray(getContextPath(), ":");
			for (String contextPath : contextPaths) {
				if (contextPath.equals(packageName)) {
					return true;
				}
			}
			return false;
		}
		else if (!ObjectUtils.isEmpty(getClassesToBeBound())) {
			return Arrays.asList(getClassesToBeBound()).contains(clazz);
		}
		return false;
	}
{
		try {
			Class<?> mbeanClass = JBossWorkManagerUtils.class.getClassLoader().loadClass(JBOSS_WORK_MANAGER_MBEAN_CLASS_NAME);
			InitialContext jndiContext = new InitialContext();
			MBeanServerConnection mconn = (MBeanServerConnection) jndiContext.lookup(MBEAN_SERVER_CONNECTION_JNDI_NAME);
			ObjectName objectName = ObjectName.getInstance(mbeanName);
			Object workManagerMBean = MBeanServerInvocationHandler.newProxyInstance(mconn, objectName, mbeanClass, false);
			Method getInstanceMethod = workManagerMBean.getClass().getMethod("getInstance");
			return (WorkManager) getInstanceMethod.invoke(workManagerMBean);
		}
		catch (Exception ex) {
			throw new IllegalStateException(
					"Could not initialize JBossWorkManagerTaskExecutor because JBoss API is not available: " + ex);
		}
	}
{
		if (!parserContext.getRegistry().containsBeanDefinition(HANDLER_ADAPTER_BEAN_NAME)) {
			RootBeanDefinition handlerAdapterDef = new RootBeanDefinition(SimpleControllerHandlerAdapter.class);
			handlerAdapterDef.setSource(source);
			handlerAdapterDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			parserContext.getRegistry().registerBeanDefinition(HANDLER_ADAPTER_BEAN_NAME, handlerAdapterDef);
			parserContext.registerComponent(new BeanComponentDefinition(handlerAdapterDef, HANDLER_ADAPTER_BEAN_NAME));
		}
	}
{
		if (!parserContext.getRegistry().containsBeanDefinition(HANDLER_MAPPING_BEAN_NAME)) {
			RootBeanDefinition handlerMappingDef = new RootBeanDefinition(SimpleUrlHandlerMapping.class);
			handlerMappingDef.setSource(source);
			handlerMappingDef.getPropertyValues().add("order", "1");
			handlerMappingDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			parserContext.getRegistry().registerBeanDefinition(HANDLER_MAPPING_BEAN_NAME, handlerMappingDef);
			parserContext.registerComponent(new BeanComponentDefinition(handlerMappingDef, HANDLER_MAPPING_BEAN_NAME));
			return handlerMappingDef;
		}
		else {
			return parserContext.getRegistry().getBeanDefinition(HANDLER_MAPPING_BEAN_NAME);
		}
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
	}
{
		Set<String> beanNames = new LinkedHashSet<String>(getBeanDefinitionCount());
		beanNames.addAll(Arrays.asList(getBeanDefinitionNames()));
		beanNames.addAll(Arrays.asList(getSingletonNames()));
		Map<String, Object> results = new LinkedHashMap<String, Object>();
		for (String beanName : beanNames) {
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
		classQueue.addFirst(ifc);
		for (Class<?> inheritedIfc : ifc.getInterfaces()) {
			addInterfaceHierarchy(inheritedIfc, classQueue);
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
		return new DefaultTransactionStatus(
				transaction, newTransaction, actualNewSynchronization,
				definition.isReadOnly(), debug, suspendedResources);
	}
{
		if (status.isNewSynchronization()) {
			TransactionSynchronizationManager.setActualTransactionActive(status.hasTransaction());
			TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(
					(definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT) ?
							definition.getIsolationLevel() : null);
			TransactionSynchronizationManager.setCurrentTransactionReadOnly(definition.isReadOnly());
			TransactionSynchronizationManager.setCurrentTransactionName(definition.getName());
			TransactionSynchronizationManager.initSynchronization();
		}
	}
{
		if (newValue.getName() != null) {
			for (Iterator<ValueHolder> it = this.genericArgumentValues.iterator(); it.hasNext();) {
				ValueHolder currentValue = it.next();
				if (newValue.getName().equals(currentValue.getName())) {
					if (newValue.getValue() instanceof Mergeable) {
						Mergeable mergeable = (Mergeable) newValue.getValue();
						if (mergeable.isMergeEnabled()) {
							newValue.setValue(mergeable.merge(currentValue.getValue()));
						}
					}
					it.remove();
				}
			}
		}
		this.genericArgumentValues.add(newValue);
	}
{
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
		conversionService.addConverter(new ObjectToStringConverter());
		conversionService.addConverter(new StringToBooleanConverter());
		conversionService.addConverter(new StringToCharacterConverter());
		conversionService.addConverter(new StringToLocaleConverter());
		conversionService.addConverter(new NumberToCharacterConverter());
		conversionService.addConverterFactory(new StringToNumberConverterFactory());
		conversionService.addConverterFactory(new StringToEnumConverterFactory());
		conversionService.addConverterFactory(new NumberToNumberConverterFactory());
		conversionService.addConverterFactory(new CharacterToNumberFactory());
		conversionService.addGenericConverter(new ObjectToObjectGenericConverter());
		conversionService.addGenericConverter(new IdToEntityConverter(conversionService));
	}
{
		synchronized (this) {
			if (defaultConversionService == null) {
				defaultConversionService = ConversionServiceFactory.createDefaultConversionService();
			}
		}
		this.conversionService = defaultConversionService;
	}
{
		ParameterizedBeanPropertyRowMapper<T> newInstance = new ParameterizedBeanPropertyRowMapper<T>();
		newInstance.setMappedClass(mappedClass);
		return newInstance;
	}
{
		Map<String, Lifecycle> lifecycleBeans = getLifecycleBeans();
		Map<Integer, LifecycleGroup> phases = new HashMap<Integer, LifecycleGroup>();
		for (Map.Entry<String, ? extends Lifecycle> entry : lifecycleBeans.entrySet()) {
			Lifecycle lifecycle = entry.getValue();
			if (!autoStartupOnly || (lifecycle instanceof SmartLifecycle && ((SmartLifecycle) lifecycle).isAutoStartup())) {
				int phase = getPhase(lifecycle);
				LifecycleGroup group = phases.get(phase);
				if (group == null) {
					group = new LifecycleGroup(phase, this.timeoutPerShutdownPhase, lifecycleBeans);
					phases.put(phase, group);
				}
				group.add(entry.getKey(), lifecycle);
			}
		}
		if (phases.size() > 0) {
			List<Integer> keys = new ArrayList<Integer>(phases.keySet());
			Collections.sort(keys);
			for (Integer key : keys) {
				phases.get(key).start();
			}
		}
		this.running = true;
	}
{
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

		return new InjectionMetadata(clazz, elements);
	}
{

		String encoding = pageContext.getResponse().getCharacterEncoding();

		StringBuilder qs = new StringBuilder();
		for (Param param : params) {
			if (!usedParams.contains(param.getName()) && StringUtils.hasLength(param.getName())) {
				if (includeQueryStringDelimiter && qs.length() == 0) {
					qs.append("?");
				}
				else {
					qs.append("&");
				}
				try {
					qs.append(UriUtils.encodeQueryParam(param.getName(), encoding));
					if (param.getValue() != null) {
						qs.append("=");
						qs.append(UriUtils.encodeQueryParam(param.getValue(), encoding));
					}
				}
				catch (UnsupportedEncodingException ex) {
					throw new JspException(ex);
				}
			}
		}
		return qs.toString();
	}
{
		String encoding = pageContext.getResponse().getCharacterEncoding();

		for (Param param : params) {
			String template = URL_TEMPLATE_DELIMITER_PREFIX + param.getName() + URL_TEMPLATE_DELIMITER_SUFFIX;
			if (uri.contains(template)) {
				usedParams.add(param.getName());
				try {
					uri = uri.replace(template, UriUtils.encodePath(param.getValue(), encoding));
				}
				catch (UnsupportedEncodingException ex) {
					throw new JspException(ex);
				}
			}
		}
		return uri;
	}
{
		if (argValue instanceof SqlParameterValue) {
			SqlParameterValue paramValue = (SqlParameterValue) argValue;
			StatementCreatorUtils.setParameterValue(ps, parameterPosition + 1, paramValue, paramValue.getValue());
		}
		else {
			StatementCreatorUtils.setParameterValue(ps, parameterPosition + 1, SqlTypeValue.TYPE_UNKNOWN, argValue);
		}
	}
{
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
	}
{
		Annotation[] anns = getIntrospectedClass().getAnnotations();
		for (Annotation ann : anns) {
			if (ann.annotationType().getName().equals(annotationType)) {
				return AnnotationUtils.getAnnotationAttributes(ann, classValuesAsString);
			}
			for (Annotation metaAnn : ann.annotationType().getAnnotations()) {
				if (metaAnn.annotationType().getName().equals(annotationType)) {
					return AnnotationUtils.getAnnotationAttributes(metaAnn, classValuesAsString);
				}
			}
		}
		return null;
	}
{
		if (context.getRegistry().containsBeanDefinition("conversionService")) {
			builder.addPropertyReference("conversionService", "conversionService");
		} else {
			builder.addPropertyValue("conversionService", createConversionService(element, source, context));
		}
	}
{
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
		builder.getRawBeanDefinition().setSource(source);
		return builder;
	}
{
		if (logger.isWarnEnabled()) {
			try {
				logger.warn(method.name() + " request for \"" + url + "\" resulted in " + response.getStatusCode() +
						" (" + response.getStatusText() + "); invoking error handler");
			}
			catch (IOException e) {
				// ignore
			}
		}
		getErrorHandler().handleError(response);
	}
{
		List<Integer> resets = new ArrayList<Integer>();

		int second = calendar.get(Calendar.SECOND);
		List<Integer> emptyList = Collections.<Integer> emptyList();
		int updateSecond = findNext(this.seconds, second, 60, calendar, Calendar.SECOND, emptyList);
		if (second == updateSecond) {
			resets.add(Calendar.SECOND);
		}

		int minute = calendar.get(Calendar.MINUTE);
		int updateMinute = findNext(this.minutes, minute, 60, calendar, Calendar.MINUTE, resets);
		if (minute == updateMinute) {
			resets.add(Calendar.MINUTE);
		} else {
			doNext(calendar);
		}

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int updateHour = findNext(this.hours, hour, 24, calendar, Calendar.HOUR_OF_DAY, resets);
		if (hour == updateHour) {
			resets.add(Calendar.HOUR_OF_DAY);
		} else {
			doNext(calendar);
		}

		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int updateDayOfMonth = findNextDay(calendar, this.daysOfMonth, dayOfMonth, daysOfWeek, dayOfWeek, 366, resets);
		if (dayOfMonth == updateDayOfMonth) {
			resets.add(Calendar.DAY_OF_MONTH);
		} else {
			doNext(calendar);			
		}

		int month = calendar.get(Calendar.MONTH);
		int updateMonth = findNext(this.months, month, 12, calendar, Calendar.MONTH, resets);
		if (month != updateMonth) {
			doNext(calendar);			
		}

	}
{
		for (Iterator<InjectedElement> it = this.injectedElements.iterator(); it.hasNext();) {
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
		Assert.notNull(source, "The source to map from cannot be null");
		Assert.notNull(target, "The target to map to cannot be null");
		try {
			MappingContextHolder.push(source);
			EvaluationContext sourceContext = getEvaluationContext(source);
			EvaluationContext targetContext = getEvaluationContext(target);
			SpelMappingContext context = new SpelMappingContext(sourceContext, targetContext);
			for (SpelMapping mapping : this.mappings) {
				if (logger.isDebugEnabled()) {
					logger.debug(MappingContextHolder.getLevel() + mapping);
				}
				mapping.map(context);
			}
			Set<FieldToFieldMapping> autoMappings = getAutoMappings(sourceContext, targetContext);
			for (SpelMapping mapping : autoMappings) {
				if (logger.isDebugEnabled()) {
					logger.debug(MappingContextHolder.getLevel() + mapping + " (auto)");
				}
				mapping.map(context);
			}
			context.handleFailures();
			return target;
		} finally {
			MappingContextHolder.pop();
		}
	}
{
		Expression sourceExp;
		try {
			sourceExp = sourceExpressionParser.parseExpression(sourceFieldExpression);
		} catch (ParseException e) {
			throw new IllegalArgumentException("The mapping source '" + sourceFieldExpression
					+ "' is not a parseable value expression", e);
		}
		return sourceExp;
	}
{
		Expression targetExp;
		try {
			targetExp = targetExpressionParser.parseExpression(targetFieldExpression);
		} catch (ParseException e) {
			throw new IllegalArgumentException("The mapping target '" + targetFieldExpression
					+ "' is not a parseable property expression", e);
		}
		return targetExp;
	}
{
		try {
			if (System.getSecurityManager() != null) {
				return AccessController.doPrivileged(new PrivilegedAction<Method>() {
					public Method run() {
						return findDestroyMethod();
					}
				});
			}
			else {
				return findDestroyMethod();
			}
		}
		catch (IllegalArgumentException ex) {
			throw new BeanDefinitionValidationException("Couldn't find a unique destroy method on bean with name '" +
					this.beanName + ": " + ex.getMessage());
		}
	}
{
		if (this.mappingTargetFactory.supports(targetType)) {
			Object target = this.mappingTargetFactory.createTarget(targetType);
			return this.mapper.map(source, target);
		} else {
			IllegalStateException cause = new IllegalStateException("["
					+ this.mappingTargetFactory.getClass().getName() + "] does not support target type ["
					+ targetType.getName() + "]");
			throw new ConversionFailedException(sourceType, targetType, source, cause);
		}

	}
{
		if (logger.isDebugEnabled()) {
			logger.debug(SpelMappingContextHolder.getLevel() + mapping);
		}
		mapping.map(sourceContext, targetContext, failures);
	}
{
		if (sourceType.isAssignableTo(targetType)) {
			return NO_OP_CONVERTER;
		} else {
			return null;
		}
	}
{
		Assert.notNull(sourceType, "The sourceType to convert to is required");
		Assert.notNull(targetType, "The targetType to convert to is required");
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
		if (!ObjectUtils.isEmpty(headers)) {
			for (String header : headers) {
				int separator = header.indexOf('=');
				if (separator == -1) {
					if (header.startsWith("!")) {
						if (request.getHeader(header.substring(1)) != null) {
							return false;
						}
					}
					else if (request.getHeader(header) == null) {
						return false;
					}
				}
				else {
					String key = header.substring(0, separator);
					String value = header.substring(separator + 1);
					if (isMediaTypeHeader(key)) {
						List<MediaType> requestMediaTypes = MediaType.parseMediaTypes(request.getHeader(key));
						List<MediaType> valueMediaTypes = MediaType.parseMediaTypes(value);
						boolean found = false;
						for (Iterator<MediaType> valIter = valueMediaTypes.iterator(); valIter.hasNext() && !found;) {
							MediaType valueMediaType = valIter.next();
							for (Iterator<MediaType> reqIter = requestMediaTypes.iterator(); reqIter.hasNext() && !found;) {
								MediaType requestMediaType = reqIter.next();
								if (valueMediaType.includes(requestMediaType)) {
									found = true;
								}
							}

						}
						if (!found) {
							return false;
						}
					}
					else if (!value.equals(request.getHeader(key))) {
						return false;
					}
				}
			}
		}
		return true;
	}
{
		Class[] typeArgs = GenericTypeResolver.resolveTypeArguments(factory.getClass(), AnnotationFormatterFactory.class);
		if (typeArgs == null) {
			throw new IllegalArgumentException(
					"Unable to extract Annotation type A argument from AnnotationFormatterFactory [" +
							factory.getClass().getName() + "]; does the factory parameterize the <A> generic type?");
		}
		this.annotationFormatters.put(typeArgs[0], factory);
	}
{
		return null;
	}
{
		if (sourceType.isAssignableTo(targetType)) {
			return source;
		}
		TypeDescriptor sourceElementType = sourceType.getElementTypeDescriptor();
		TypeDescriptor targetElementType = targetType.getElementTypeDescriptor();
		Object target = Array.newInstance(targetElementType.getType(), Array.getLength(source));
		GenericConverter converter = conversionService.getConverter(sourceElementType, targetElementType);
		for (int i = 0; i < Array.getLength(target); i++) {
			Array.set(target, i, converter.convert(Array.get(source, i), sourceElementType, targetElementType));
		}
		return target;		
	}
{
		int length = Array.getLength(source);
		Collection collection = CollectionFactory.createCollection(targetType.getType(), length);
		TypeDescriptor sourceElementType = sourceType.getElementTypeDescriptor();
		TypeDescriptor targetElementType = targetType.getElementTypeDescriptor();
		if (targetElementType == TypeDescriptor.NULL || sourceElementType.isAssignableTo(targetElementType)) {
			for (int i = 0; i < length; i++) {
				collection.add(Array.get(source, i));
			}
		} else {
			GenericConverter converter = conversionService.getConverter(sourceElementType, targetElementType);
			for (int i = 0; i < length; i++) {
				collection.add(converter.convert(Array.get(source, i), sourceElementType, targetElementType));
			}
		}
		return collection;
	}
{
		Collection sourceCollection = (Collection) source;
		TypeDescriptor sourceElementType = sourceType.getElementTypeDescriptor();
		if (sourceElementType == TypeDescriptor.NULL) {
			sourceElementType = getElementType(sourceCollection);
		}
		TypeDescriptor targetElementType = targetType.getElementTypeDescriptor();
		Object array = Array.newInstance(targetElementType.getType(), sourceCollection.size());
		int i = 0;		
		if (sourceElementType == TypeDescriptor.NULL || sourceElementType.isAssignableTo(targetElementType)) {
			for (Iterator it = sourceCollection.iterator(); it.hasNext(); i++) {
				Array.set(array, i, it.next());
			}
		} else {
			GenericConverter converter = conversionService.getConverter(sourceElementType, targetElementType);
			for (Iterator it = sourceCollection.iterator(); it.hasNext(); i++) {
				Array.set(array, i, converter.convert(it.next(), sourceElementType, targetElementType));
			}
		}
		return array;
	}
{
		Collection sourceCollection = (Collection) source;
		TypeDescriptor sourceElementType = sourceType.getElementTypeDescriptor();
		if (sourceElementType == TypeDescriptor.NULL) {
			sourceElementType = getElementType(sourceCollection);
		}
		TypeDescriptor targetElementType = targetType.getElementTypeDescriptor();
		if (sourceElementType == TypeDescriptor.NULL || sourceElementType.isAssignableTo(targetElementType)) {
			if (sourceType.isAssignableTo(targetType)) {
				return source;
			} else {
				Collection targetCollection = CollectionFactory.createCollection(targetType.getType(), sourceCollection.size());
				targetCollection.addAll(sourceCollection);
				return targetCollection;
			}
		}
		Collection targetCollection = CollectionFactory.createCollection(targetType.getType(), sourceCollection.size());
		GenericConverter converter = conversionService.getConverter(sourceElementType, targetElementType);
		for (Object element : sourceCollection) {
			targetCollection.add(converter.convert(element, sourceElementType, targetElementType));
		}
		return targetCollection;
	}
{
		if (sourceType.isAssignableTo(targetType)) {
			return source;
		}
		TypeDescriptor sourceElementType = sourceType.getElementTypeDescriptor();
		TypeDescriptor targetElementType = targetType.getElementTypeDescriptor();
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
			return this.service.canConvert(sourceType, targetType);
		}
{
		Assert.notNull(targetType, "The targetType to convert to is required");
		if (source == null) {
			return null;
		}
		if (targetType == TypeDescriptor.NULL) {
			return null;
		}
		Class sourceType = ClassUtils.resolvePrimitiveIfNecessary(source.getClass());
		GenericConverter converter = getConverter(sourceType, targetType.getObjectType());
		if (converter != null) {
			try {
				return converter.convert(source, targetType);
			} catch (ConversionFailedException e) {
				throw e;
			} catch (Exception e) {
				throw new ConversionFailedException(sourceType, targetType, source, e);
			}
		} else {
			if (targetType.isAssignableValue(source)) {
				return source;
			} else {
				throw new ConverterNotFoundException(sourceType, targetType.getObjectType());
			}
		}
	}
{
		Object convertedValue = currentConvertedValue;

		if(Enum.class.equals(requiredType)) {
			// target type is declared as raw enum, treat the trimmed value as <enum.fqn>.FIELD_NAME
			int index = trimmedValue.lastIndexOf(".");
			if(index > - 1) {
				String enumType = trimmedValue.substring(0, index);
				String fieldName = trimmedValue.substring(index + 1);

				ClassLoader loader = this.targetObject.getClass().getClassLoader();

				try {
					Class<?> enumValueType = loader.loadClass(enumType);
					Field enumField = enumValueType.getField(fieldName);
					convertedValue = enumField.get(null);
				} catch(ClassNotFoundException ex) {
					if(logger.isTraceEnabled()) {
						logger.trace("Enum class [" + enumType + "] cannot be loaded from [" + loader + "]", ex);
					}
				}
				catch (Throwable ex) {
					if(logger.isTraceEnabled()) {
						logger.trace("Field [" + fieldName + "] isn't an enum value for type [" + enumType + "]", ex);
					}
				}
			}
		}
		if (convertedValue == currentConvertedValue) {
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

		return convertedValue;
	}
{

		Object value = getAutowireCandidateResolver().getSuggestedValue(descriptor);
		if (value != null) {
			if (value instanceof String) {
				String strVal = resolveEmbeddedValue((String) value);
				value = evaluateBeanDefinitionString(strVal, getMergedBeanDefinition(beanName));
			}
			TypeConverter converter = (typeConverter != null ? typeConverter : getTypeConverter());
			return converter.convertIfNecessary(value, type);
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
		StringBuilder buf = new StringBuilder(strVal);

		int startIndex = strVal.indexOf(PLACEHOLDER_PREFIX);
		while (startIndex != -1) {
			int endIndex = findPlaceholderEndIndex(buf, startIndex);
			if (endIndex != -1) {
				String placeholder = buf.substring(startIndex + PLACEHOLDER_PREFIX.length(), endIndex);
				if (!visitedPlaceholders.add(placeholder)) {
					throw new IllegalArgumentException(
							"Circular placeholder reference '" + placeholder + "' in property definitions");
				}
				// Recursive invocation, parsing placeholders contained in the placeholder key.
				placeholder = parseStringValue(placeholder, placeholderResolver, visitedPlaceholders);

				// Now obtain the value for the fully resolved key...
				String propVal = placeholderResolver.resolvePlaceholder(placeholder);
				if (propVal != null) {
					// Recursive invocation, parsing placeholders contained in the
					// previously resolved placeholder value.
					propVal = parseStringValue(propVal, placeholderResolver, visitedPlaceholders);
					buf.replace(startIndex, endIndex + PLACEHOLDER_SUFFIX.length(), propVal);

					//if (logger.isTraceEnabled()) {
					//	logger.trace("Resolved placeholder '" + placeholder + "'");
					//}

					startIndex = buf.indexOf(PLACEHOLDER_PREFIX, startIndex + propVal.length());
				}
				else  {
					// Proceed with unprocessed value.
					startIndex = buf.indexOf(PLACEHOLDER_PREFIX, endIndex + PLACEHOLDER_SUFFIX.length());
				}

				visitedPlaceholders.remove(placeholder);
			}
			else {
				startIndex = -1;
			}
		}

		return buf.toString();
	}
{
			return this.currencyFormatter;
		}
{
		List<TestExecutionListener> listenersReversed = new ArrayList<TestExecutionListener>(
			getTestExecutionListeners());
		Collections.reverse(listenersReversed);
		return listenersReversed;
	}
{
		Set<String> parameterNames = new HashSet<String>();
		Enumeration parameterEnum = request.getParameterNames();
		while (parameterEnum.hasMoreElements()) {
			parameterNames.add((String) parameterEnum.nextElement());
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

		List<String> parameterMapKeys = new ArrayList<String>();
		List<Object> parameterMapValues = new ArrayList<Object>();
		for (Object o : request.getParameterMap().keySet()) {
			String key = (String) o;
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
	}
{
		Set<String> fileNames = new HashSet<String>();
		Iterator fileIter = request.getFileNames();
		while (fileIter.hasNext()) {
			fileNames.add((String) fileIter.next());
		}
		assertEquals(3, fileNames.size());
		assertTrue(fileNames.contains("field1"));
		assertTrue(fileNames.contains("field2"));
		assertTrue(fileNames.contains("field2x"));
		CommonsMultipartFile file1 = (CommonsMultipartFile) request.getFile("field1");
		CommonsMultipartFile file2 = (CommonsMultipartFile) request.getFile("field2");
		CommonsMultipartFile file2x = (CommonsMultipartFile) request.getFile("field2x");

		Map<String, MultipartFile> fileMap = request.getFileMap();
		assertEquals(3, fileMap.size());
		assertTrue(fileMap.containsKey("field1"));
		assertTrue(fileMap.containsKey("field2"));
		assertTrue(fileMap.containsKey("field2x"));
		assertEquals(file1, fileMap.get("field1"));
		assertEquals(file2, fileMap.get("field2"));
		assertEquals(file2x, fileMap.get("field2x"));

		MultiValueMap<String, MultipartFile> multiFileMap = request.getMultiFileMap();
		assertEquals(3, multiFileMap.size());
		assertTrue(multiFileMap.containsKey("field1"));
		assertTrue(multiFileMap.containsKey("field2"));
		assertTrue(multiFileMap.containsKey("field2x"));
		List<MultipartFile> field1Files = multiFileMap.get("field1");
		assertEquals(2, field1Files.size());
		assertTrue(field1Files.contains(file1));
		assertEquals(file1, multiFileMap.getFirst("field1"));
		assertEquals(file2, multiFileMap.getFirst("field2"));
		assertEquals(file2x, multiFileMap.getFirst("field2x"));

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
		file1.transferTo(transfer1);
		File transfer2 = new File("C:/transfer2");
		file2.transferTo(transfer2);
		assertEquals(transfer1, ((MockFileItem) file1.getFileItem()).writtenFile);
		assertEquals(transfer2, ((MockFileItem) file2.getFileItem()).writtenFile);

	}
{
		MultipartTestBean1 mtb1 = new MultipartTestBean1();
		assertEquals(null, mtb1.getField1());
		assertEquals(null, mtb1.getField2());
		ServletRequestDataBinder binder = new ServletRequestDataBinder(mtb1, "mybean");
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		binder.bind(request);
		CommonsMultipartFile file1 = (CommonsMultipartFile) request.getFile("field1");
		CommonsMultipartFile file2 = (CommonsMultipartFile) request.getFile("field2");
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
		if (this.type != null) {
			return this.type;
		}
		else if (this.field != null) {
			return this.field.getType();
		}
		else if (this.methodParameter != null) {
			return this.methodParameter.getParameterType();
		}
		else {
			return null;
		}
	}
{
		return this.typeConverter.canConvert(sourceType, targetType);
	}
{

		this.targetCollectionType = targetCollectionType;
		Class<?> elementType = targetCollectionType.getElementType();
		if (elementType != null) {
			this.elementConverter = typeConverter.getConversionExecutor(sourceObjectType.getType(), TypeDescriptor.valueOf(elementType));
		}
		else {
			this.elementConverter = NoOpConversionExecutor.INSTANCE;
		}
	}
{
		String fullPath = calculateEndpointPath(endpoint, serviceName);
		HttpContext httpContext = this.server.createContext(fullPath);
		if (this.filters != null) {
			httpContext.getFilters().addAll(this.filters);
		}
		if (this.authenticator != null) {
			httpContext.setAuthenticator(this.authenticator);
		}
		return httpContext;
	}
{
		String fullAddress = this.baseAddress + serviceName;
		if (endpoint.getClass().getName().startsWith("weblogic.")) {
			// Workaround for WebLogic 10.3
			fullAddress = fullAddress + "/";
		}
		return fullAddress;
	}
{
		if (bean instanceof BeanNameAware) {
			((BeanNameAware) bean).setBeanName(beanName);
		}

		if (bean instanceof BeanClassLoaderAware) {
			((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
		}

		if (bean instanceof BeanFactoryAware) {
			((BeanFactoryAware) bean).setBeanFactory(AbstractAutowireCapableBeanFactory.this);
		}
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
		if (typesToMatch != null) {
			ClassLoader tempClassLoader = getTempClassLoader();
			if (tempClassLoader != null) {
				if (tempClassLoader instanceof DecoratingClassLoader) {
					DecoratingClassLoader dcl = (DecoratingClassLoader) tempClassLoader;
					for (Class<?> typeToMatch : typesToMatch) {
						dcl.excludeClass(typeToMatch.getName());
					}
				}
				String className = mbd.getBeanClassName();
				return (className != null ? ClassUtils.forName(className, tempClassLoader) : null);
			}
		}
		return mbd.resolveBeanClass(getBeanClassLoader());
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
	}
{
			HttpInputMessage inputMessage = new ServletServerHttpRequest(webRequest.getRequest());
			List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
			HttpOutputMessage outputMessage = new ServletServerHttpResponse(webRequest.getResponse());
			Class<?> returnValueType = returnValue.getClass();
			List<MediaType> allSupportedMediaTypes = new ArrayList<MediaType>();
			for (HttpMessageConverter messageConverter : messageConverters) {
				allSupportedMediaTypes.addAll(messageConverter.getSupportedMediaTypes());
				if (messageConverter.supports(returnValueType)) {
					for (Object o : messageConverter.getSupportedMediaTypes()) {
						MediaType supportedMediaType = (MediaType) o;
						for (MediaType acceptedMediaType : acceptedMediaTypes) {
							if (supportedMediaType.includes(acceptedMediaType)) {
								messageConverter.write(returnValue, outputMessage);
								responseArgumentUsed = true;
								return;
							}
						}
					}
				}
			}
			throw new HttpMediaTypeNotAcceptableException(allSupportedMediaTypes);
		}
{
		this.defaultMessageFactory = defaultMessageFactory;
		return this;
	}
{
			GenericBindingRule rule = nestedBindingRules.get(property);
			if (rule == null) {
				rule = new GenericBindingRule(property, modelClass);
				nestedBindingRules.put(property, rule);
			}
			return rule;
		}
{
		Class<?> formattedType = getFormattedObjectType(formatter.getClass());
		value = bindingContext.getTypeConverter().convert(value, formattedType);
		return formatter.format(value, getLocale());
	}
{
		Formatter formatter = typeFormatters.get(type);
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
				return DefaultFormatter.INSTANCE;
			}
		}		
	}
{
		Assert.notNull(clazz, "Class must not be null");
		Assert.hasText(defaultContextLoaderClassName, "Default ContextLoader class name must not be null or empty");

		Class<ContextConfiguration> annotationType = ContextConfiguration.class;
		Class<?> declaringClass = AnnotationUtils.findAnnotationDeclaringClass(annotationType, clazz);
		Assert.notNull(declaringClass, "Could not find an 'annotation declaring class' for annotation type ["
				+ annotationType + "] and class [" + clazz + "]");

		while (declaringClass != null) {
			ContextConfiguration contextConfiguration = declaringClass.getAnnotation(annotationType);
			if (logger.isTraceEnabled()) {
				logger.trace("Processing ContextLoader for @ContextConfiguration [" + contextConfiguration
						+ "] and declaring class [" + declaringClass + "]");
			}

			Class<? extends ContextLoader> contextLoaderClass = contextConfiguration.loader();
			if (!ContextLoader.class.equals(contextLoaderClass)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Found explicit ContextLoader [" + contextLoaderClass
							+ "] for @ContextConfiguration [" + contextConfiguration + "] and declaring class ["
							+ declaringClass + "]");
				}
				return contextLoaderClass;
			}

			declaringClass = AnnotationUtils.findAnnotationDeclaringClass(annotationType,
				declaringClass.getSuperclass());
		}

		try {
			ContextConfiguration contextConfiguration = clazz.getAnnotation(ContextConfiguration.class);
			if (logger.isTraceEnabled()) {
				logger.trace("Using default ContextLoader class [" + defaultContextLoaderClassName
						+ "] for @ContextConfiguration [" + contextConfiguration + "] and class [" + clazz + "]");
			}
			return (Class<? extends ContextLoader>) getClass().getClassLoader().loadClass(defaultContextLoaderClassName);
		}
		catch (ClassNotFoundException ex) {
			throw new IllegalStateException("Could not load default ContextLoader class ["
					+ defaultContextLoaderClassName + "]. Specify @ContextConfiguration's 'loader' "
					+ "attribute or make the default loader class available.");
		}
	}
{
		this.propertyDescriptor = findPropertyDescriptor(property, model);
		this.property = property;
	}
{
		String property = sourceValue.getKey();
		Object value = sourceValue.getValue();
		if (binding.isReadOnly()) {
			return new PropertyNotWriteableResult(property, value, messageSource);
		} else {
			binding.applySourceValue(value);
			if (binding.isValid()) {
				binding.commit();
			}
			return new BindingStatusResult(property, value, binding.getStatusAlert());
		}
	}
{
		Class classToIntrospect = converterClass;
		while (classToIntrospect != null) {
			Type[] ifcs = classToIntrospect.getGenericInterfaces();
			for (Type ifc : ifcs) {
				if (ifc instanceof ParameterizedType) {
					ParameterizedType paramIfc = (ParameterizedType) ifc;
					Type rawType = paramIfc.getRawType();
					if (Converter.class.equals(rawType) || ConverterFactory.class.equals(rawType)) {
						List typeInfo = new ArrayList(2);						
						Type arg1 = paramIfc.getActualTypeArguments()[0];
						if (arg1 instanceof TypeVariable) {
							arg1 = GenericTypeResolver.resolveTypeVariable((TypeVariable) arg1, converterClass);
						}
						if (arg1 instanceof Class) {
							typeInfo.add((Class) arg1);
						}						
						Type arg2 = paramIfc.getActualTypeArguments()[1];
						if (arg2 instanceof TypeVariable) {
							arg2 = GenericTypeResolver.resolveTypeVariable((TypeVariable) arg2, converterClass);
						}
						if (arg2 instanceof Class) {
							typeInfo.add((Class) arg2);
						}
						if (typeInfo.size() == 2) {
							return typeInfo;
						}						
					}
					else if (Converter.class.isAssignableFrom((Class) rawType)) {
						return getConverterTypeInfo((Class) rawType);
					}
				}
				else if (Converter.class.isAssignableFrom((Class) ifc)) {
					return getConverterTypeInfo((Class) ifc);
				}
			}
			classToIntrospect = classToIntrospect.getSuperclass();
		}
		return null;
	}
{
			// TODO consider caching this info
			Class classToIntrospect = formatterClass;
			while (classToIntrospect != null) {
				Type[] ifcs = classToIntrospect.getGenericInterfaces();
				for (Type ifc : ifcs) {
					if (ifc instanceof ParameterizedType) {
						ParameterizedType paramIfc = (ParameterizedType) ifc;
						Type rawType = paramIfc.getRawType();
						if (Formatter.class.equals(rawType)) {
							Type arg = paramIfc.getActualTypeArguments()[0];
							if (arg instanceof TypeVariable) {
								arg = GenericTypeResolver.resolveTypeVariable((TypeVariable) arg, formatterClass);
							}
							if (arg instanceof Class) {
								return (Class) arg;
							}
						}
						else if (ApplicationListener.class.isAssignableFrom((Class) rawType)) {
							return getFormattedObjectType((Class) rawType);
						}
					}
					else if (ApplicationListener.class.isAssignableFrom((Class) ifc)) {
						return getFormattedObjectType((Class) ifc);
					}
				}
				classToIntrospect = classToIntrospect.getSuperclass();
			}
			return null;
		}
{
		ServletContextAdapter adaptedContext = new ServletContextAdapter(new DelegatingServletConfig());
		createTilesInitializer().initialize(new ServletTilesApplicationContext(adaptedContext));
	}
{
		testContext.markApplicationContextDirty();
		testContext.setAttribute(DependencyInjectionTestExecutionListener.REINJECT_DEPENDENCIES_ATTRIBUTE, Boolean.TRUE);
	}
{
		return sourceValues;
	}
{
		if (StringUtils.hasLength(this.contextPath) && !ObjectUtils.isEmpty(this.classesToBeBound)) {
			throw new IllegalArgumentException("Specify either 'contextPath' or 'classesToBeBound property'; not both");
		}
		else if (!StringUtils.hasLength(this.contextPath) && ObjectUtils.isEmpty(this.classesToBeBound)) {
			throw new IllegalArgumentException("Setting either 'contextPath' or 'classesToBeBound' is required");
		}
		if (!lazyInit) {
			getJaxbContext();
		}
		if (!ObjectUtils.isEmpty(this.schemaResources)) {
			this.schema = loadSchema(this.schemaResources, this.schemaLanguage);
		}
	}
{
			Class type;
			try { 
				type = property.getValueType(createEvaluationContext());
			} catch (EvaluationException e) {
				throw new IllegalArgumentException("Failed to get property expression value type - this should not happen", e);
			}
			return type;
		}
{
		Assert.isTrue(index >= 0, "Index must not be negative");
		ValueHolder valueHolder = this.indexedArgumentValues.get(index);
		if (valueHolder != null &&
				(valueHolder.getType() == null ||
						(requiredType != null && requiredType.getName().equals(valueHolder.getType()))) &&
				(valueHolder.getName() == null ||
						(requiredName != null && requiredName.equals(valueHolder.getName())))) {
			return valueHolder;
		}
		return null;
	}
{
		if (!moreTokens()) {
			return false;
		}
		Token t = peekToken();
		if (t.kind==desiredTokenKind) {
			if (consumeIfMatched) {
				tokenStreamPointer++;
			}
			return true;
		} else {
			return false;
		}
	}
{
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
		Connection connection = null;
		try {
			SimpleDriverDataSource shutdownDataSource = new SimpleDriverDataSource();
			shutdownDataSource.setDriverClass(EmbeddedDriver.class);
			shutdownDataSource.setUrl(String.format(URL_TEMPLATE, databaseName, "shutdown=true"));
			connection = shutdownDataSource.getConnection();
		} catch (SQLException e) {
			if (SHUTDOWN_CODE.equals(e.getSQLState())) {
				purgeDatabase(databaseName);
			} else {
				logger.warn("Could not shutdown in-memory Derby database", e);
			}
		} finally {
			JdbcUtils.closeConnection(connection);
		}
	}
{
		JdbcTemplate t = new JdbcTemplate(dataSource);
		assertEquals(1, t.queryForInt("select count(*) from T_TEST"));
	}
{

		JdbcTemplate template = new JdbcTemplate(db);
		assertEquals("Keith", template.queryForObject("select NAME from T_TEST", String.class));
		db.shutdown();
	}
{
		beanFactory.registerScope(WebApplicationContext.SCOPE_REQUEST, new RequestScope());
		beanFactory.registerScope(WebApplicationContext.SCOPE_SESSION, new SessionScope(false));
		beanFactory.registerScope(WebApplicationContext.SCOPE_GLOBAL_SESSION, new SessionScope(true));
		if (sc != null) {
			ServletContextScope appScope = new ServletContextScope(sc);
			beanFactory.registerScope(WebApplicationContext.SCOPE_APPLICATION, appScope);
			// Register as ServletContext attribute, for ContextCleanupListener to detect it.
			sc.setAttribute(ServletContextScope.class.getName(), appScope);
		}

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

		if (jsfPresent) {
			FacesDependencyRegistrar.registerFacesDependencies(beanFactory);
		}
	}
{
			if (this.cachedMethodArguments == null) {
				return null;
			}
			Object[] arguments = new Object[this.cachedMethodArguments.length];
			for (int i = 0; i < arguments.length; i++) {
				arguments[i] = resolvedCachedArgument(beanName, this.cachedMethodArguments[i]);
			}
			return arguments;
		}
{
		Map<String, Object> attrs = new HashMap<String, Object>();
		Method[] methods = annotation.annotationType().getDeclaredMethods();
		for (Method method : methods) {
			if (method.getParameterTypes().length == 0 && method.getReturnType() != void.class) {
				try {
					Object value = method.invoke(annotation);
					if (filterClasses) {
						if (value instanceof Class) {
							value = ((Class) value).getName();
						}
						else if (value instanceof Class[]) {
							Class[] clazzArray = (Class[]) value;
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
		this.databaseConfigurer = configurer;
	}
{
		for (Resource script : scripts) {
			executeSqlScript(template, new EncodedResource(script, sqlScriptEncoding), false);
		}
	}
{		// Chew on the expression text - relying on the rules:		// brackets must be in pairs: () [] {}		// string literals are "..." or '...' and these may contain unmatched brackets		int pos = afterPrefixIndex;		int maxlen = expressionString.length();		int nextSuffix = expressionString.indexOf(suffix,afterPrefixIndex);		if (nextSuffix ==-1 ) {			return -1; // the suffix is missing		}		Stack<Bracket> stack = new Stack<Bracket>();		while (pos<maxlen) {			if (isSuffixHere(expressionString,pos,suffix) && stack.isEmpty()) {				break;			}			char ch = expressionString.charAt(pos);			switch (ch) {			case '{': case '[': case '(':				stack.push(new Bracket(ch,pos));				break;			case '}':case ']':case ')':				if (stack.isEmpty()) {					throw new ParseException(expressionString,"Found closing '"+ch+"' at position "+pos+" without an opening '"+Bracket.theOpenBracketFor(ch)+"'");				}				Bracket p = stack.pop();				if (!p.compatibleWithCloseBracket(ch)) {					throw new ParseException(expressionString,"Found closing '"+ch+"' at position "+pos+" but most recent opening is '"+p.bracket+"' at position "+p.pos);				}				break;			case '\'':			case '"':				// jump to the end of the literal				int endLiteral = expressionString.indexOf(ch,pos+1);				if (endLiteral==-1) {					throw new ParseException(expressionString,"Found non terminating string literal starting at position "+pos);				}				pos=endLiteral;				break;							}			pos++;		}		if (!stack.isEmpty()) {			Bracket p = stack.pop();			throw new ParseException(expressionString,"Missing closing '"+Bracket.theCloseBracketFor(p.bracket)+"' for '"+p.bracket+"' at position "+p.pos);		}		if (!isSuffixHere(expressionString, pos, suffix)) {			return -1;		}		return pos;			}
{
		SpelAntlrExpressionParser parser = new SpelAntlrExpressionParser();
		Expression expr = parser.parseExpression(expression,context);
		assertEquals(expectedValue,expr.getValue(TestScenarioCreator.getTestEvaluationContext()));
	}
{
		AllHttpScopesHashModel fmModel = new AllHttpScopesHashModel(getObjectWrapper(), getServletContext(), request);
		fmModel.put(FreemarkerServlet.KEY_JSP_TAGLIBS, this.taglibFactory);
		fmModel.put(FreemarkerServlet.KEY_APPLICATION, this.servletContextHashModel);
		fmModel.put(FreemarkerServlet.KEY_SESSION, buildSessionModel(request, response));
		fmModel.put(FreemarkerServlet.KEY_REQUEST, new HttpRequestHashModel(request, response, getObjectWrapper()));
		fmModel.put(FreemarkerServlet.KEY_REQUEST_PARAMETERS, new HttpRequestParametersHashModel(request));
		fmModel.putAll(model);
		return fmModel;
	}
{

		for (int i = 0; i < elementNodes.getLength(); i++) {
			Node node = elementNodes.item(i);
			if (node instanceof Element && !DomUtils.nodeNameEquals(node, DESCRIPTION_ELEMENT)) {
				target.add(parsePropertySubElement((Element) node, bd, defaultElementType));
			}
		}
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
		EntryConverter entryConverter = this.entryConverter;
		if (entryConverter == EntryConverter.NO_OP_INSTANCE) {
			Class<?> targetKeyType = targetType.getMapKeyType();
			Class<?> targetValueType = targetType.getMapValueType();
			if (targetKeyType != null && targetValueType != null) {
				ConversionExecutor keyConverter = null;
				ConversionExecutor valueConverter = null;
				Iterator<?> it = map.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>) it.next();
					Object key = entry.getKey();
					Object value = entry.getValue();
					if (keyConverter == null && key != null) {
						keyConverter = conversionService.getConversionExecutor(key.getClass(), TypeDescriptor
								.valueOf(targetKeyType));
					}
					if (valueConverter == null && value != null) {
						valueConverter = conversionService.getConversionExecutor(value.getClass(), TypeDescriptor
								.valueOf(targetValueType));
					}
					if (keyConverter != null && valueConverter != null) {
						break;
					}
				}
				entryConverter = new EntryConverter(keyConverter, valueConverter);
			}
		}
		return entryConverter;
	}
{
		ConversionExecutor elementConverter = getElementConverter();
		if (elementConverter == NoOpConversionExecutor.INSTANCE && !source.isEmpty() && getTargetElementType() != null) {
			Iterator<?> it = source.iterator();
			while (it.hasNext()) {
				Object value = it.next();
				if (value != null) {
					elementConverter = getConversionService().getConversionExecutor(value.getClass(), TypeDescriptor.valueOf(getTargetElementType()));
					break;
				}
			}
		}
		return elementConverter;
	}
{
		this.conversionService = conversionService;
		this.sourceCollectionType = sourceCollectionType;
		this.targetCollectionType = targetCollectionType;
		Class<?> sourceElementType = sourceCollectionType.getElementType();
		Class<?> targetElementType = targetCollectionType.getElementType();
		if (sourceElementType != null && targetElementType != null) {
			elementConverter = conversionService.getConversionExecutor(sourceElementType, TypeDescriptor.valueOf(targetElementType));
		} else {
			elementConverter = NoOpConversionExecutor.INSTANCE;
		}
	}
{
		servlet = new DispatcherServlet() {
			@Override
			protected WebApplicationContext createWebApplicationContext(WebApplicationContext parent)
					throws BeansException {
				GenericWebApplicationContext wac = new GenericWebApplicationContext();
				wac.registerBeanDefinition("controller", new RootBeanDefinition(controllerclass));
				wac.refresh();
				return wac;
			}
		};
		servlet.init(new MockServletConfig());
	}
{
		ParameterizedBeanPropertyRowMapper<T> newInstance = new ParameterizedBeanPropertyRowMapper<T>();
		newInstance.setMappedClass(mappedClass);
		newInstance.setPrimitivesDefaultedForNullValue(primitivesDefaultedForNullValue);
		return newInstance;
	}
{
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			if (uriTemplateVariables != null) {
				for (int i = 1; i <= matcher.groupCount(); i++) {
					String name = this.variableNames.get(i - 1);
					String value = matcher.group(i);
					uriTemplateVariables.put(name, value);
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
{
		if (ObjectUtils.isEmpty(annotationsToSearch)) {
			return true;
		}
		SimpleTypeConverter typeConverter = new SimpleTypeConverter();
		for (Annotation annotation : annotationsToSearch) {
			Class<? extends Annotation> type = annotation.annotationType();
			if (isQualifier(type)) {
				if (!checkQualifier(bdHolder, annotation, typeConverter)) {
					return false;
				}
			}
		}
		return true;
	}
{
		for (Annotation annotation : annotationsToSearch) {
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

		Class[] paramTypes = (methodOrCtor instanceof Method ?
				((Method) methodOrCtor).getParameterTypes() : ((Constructor) methodOrCtor).getParameterTypes());
		Object[] argsToResolve = mbd.preparedConstructorArguments;
		TypeConverter converter = (this.typeConverter != null ? this.typeConverter : bw);
		BeanDefinitionValueResolver valueResolver =
				new BeanDefinitionValueResolver(this.beanFactory, beanName, mbd, converter);
		Object[] resolvedArgs = new Object[argsToResolve.length];
		for (int argIndex = 0; argIndex < argsToResolve.length; argIndex++) {
			Object argValue = argsToResolve[argIndex];
			MethodParameter methodParam = MethodParameter.forMethodOrConstructor(methodOrCtor, argIndex);
			GenericTypeResolver.resolveParameterType(methodParam, methodOrCtor.getDeclaringClass());
			if (argValue instanceof AutowiredArgumentMarker) {
				argValue = resolveAutowiredArgument(methodParam, beanName, null, converter);
			}
			else if (argValue instanceof BeanMetadataElement) {
				argValue = valueResolver.resolveValueIfNecessary("constructor argument", argValue);
			}
			else if (argValue instanceof String) {
				argValue = this.beanFactory.evaluateBeanDefinitionString((String) argValue, mbd);
			}
			Class paramType = paramTypes[argIndex];
			try {
				resolvedArgs[argIndex] = converter.convertIfNecessary(argValue, paramType, methodParam);
			}
			catch (TypeMismatchException ex) {
				String methodType = (methodOrCtor instanceof Constructor ? "constructor" : "factory method");
				throw new UnsatisfiedDependencyException(
						mbd.getResourceDescription(), beanName, argIndex, paramType,
						"Could not convert " + methodType + " argument value of type [" +
						ObjectUtils.nullSafeClassName(argValue) +
						"] to required type [" + paramType.getName() + "]: " + ex.getMessage());
			}
		}
		return resolvedArgs;
	}
{
		BeanDefinitionRegistry configBeanDefs = getConfigurationBeanDefinitions(false);
		
		// return an empty registry immediately if no @Configuration classes were found
		if(configBeanDefs.getBeanDefinitionCount() == 0)
			return configBeanDefs;
		
		// populate a new ConfigurationModel by parsing each @Configuration classes
		ConfigurationParser parser = createConfigurationParser();
        
        for(String beanName : configBeanDefs.getBeanDefinitionNames()) {
        	BeanDefinition beanDef = configBeanDefs.getBeanDefinition(beanName);
        	String className = beanDef.getBeanClassName();
        	
        	parser.parse(className, beanName);
        }
		
		ConfigurationModel configModel = parser.getConfigurationModel();
		
		// validate the ConfigurationModel
		validateModel(configModel);
		
		// read the model and create bean definitions based on its content
	    return new ConfigurationModelBeanDefinitionReader().loadBeanDefinitions(configModel);
    }
{
		marshaller.setValidation(this.validating);
		if (this.namespaceMappings != null) {
			for (Map.Entry<String, String> entry : namespaceMappings.entrySet()) {
				marshaller.setNamespaceMapping(entry.getKey(), entry.getValue());
			}
		}
	}
{
		if (this.jdbcExceptionTranslator != null && ex instanceof JDBCException) {
			JDBCException jdbcEx = (JDBCException) ex;
			return this.jdbcExceptionTranslator.translate(
					"Hibernate flushing: " + jdbcEx.getMessage(), jdbcEx.getSQL(), jdbcEx.getSQLException());
		}
		return SessionFactoryUtils.convertHibernateAccessException(ex);
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
		if (flush) {
			session.flush();
			sessionControl.setVoidCallable(1);
		}
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
					if (flush) {
						status.flush();
					}
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
		for (Map.Entry<String, String> entry : this.aliasMap.entrySet()) {
			String registeredName = entry.getValue();
			if (registeredName.equals(name)) {
				String alias = entry.getKey();
				result.add(alias);
				retrieveAliases(alias, result);
			}
		}
	}
{
		if (isFrozen()) {
			throw new AopConfigException("Cannot add advisor: Configuration is frozen.");
		}
		if (!CollectionUtils.isEmpty(advisors)) {
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
		return urlPath.substring(begin, end);
	}
{
		Assert.notNull(request, "Request must not be null");
		Assert.notNull(response, "Response must not be null");
		if (!(response instanceof MockMimeResponse)) {
			throw new IllegalArgumentException("MockPortletRequestDispatcher requires MockMimeResponse");
		}
		((MockMimeResponse) response).setIncludedUrl(this.url);
		if (logger.isDebugEnabled()) {
			logger.debug("MockPortletRequestDispatcher: including URL [" + this.url + "]");
		}
	}
{
		if (JAXBElement.class.isAssignableFrom(clazz)) {
			return true;
		}
		else if (clazz.getAnnotation(XmlRootElement.class) != null) {
			return true;
		}
		if (StringUtils.hasLength(contextPath)) {
			String packageName = ClassUtils.getPackageName(clazz);
			String[] contextPaths = StringUtils.tokenizeToStringArray(contextPath, ":");
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
		if (this.singleton) {
			return this.singletonInstance;
		}
		else {
			return mergeProperties();
		}
	}
{
		InputStream isToUse = inputStream;
		OutputStream osToUse = outputStream;

		if (this.debugLogger != null && this.debugLogger.isDebugEnabled()) {
			PrintWriter debugWriter = new PrintWriter(new CommonsLogWriter(this.debugLogger));
			isToUse = new HessianDebugInputStream(inputStream, debugWriter);
			if (debugOutputStreamAvailable) {
				osToUse = DebugStreamFactory.createDebugOutputStream(outputStream, debugWriter);
			}
		}

		Hessian2Input in = new Hessian2Input(isToUse);
		if (this.serializerFactory != null) {
			in.setSerializerFactory(this.serializerFactory);
		}

		int code = in.read();
		if (code != 'c') {
			throw new IOException("expected 'c' in hessian input at " + code);
		}

		AbstractHessianOutput out = null;
		int major = in.read();
		int minor = in.read();
		if (major >= 2) {
			out = new Hessian2Output(osToUse);
		}
		else {
			out = new HessianOutput(osToUse);
		}
		if (this.serializerFactory != null) {
			out.setSerializerFactory(this.serializerFactory);
		}

		try {
			this.skeleton.invoke(in, out);
		}
		finally {
			try {
				in.close();
				isToUse.close();
			}
			catch (IOException ex) {
				// ignore
			}
			try {
				out.close();
				osToUse.close();
			}
			catch (IOException ex) {
				// ignore
			}
		}
	}
{
		if (patIdxTmp == patIdxStart + 1 && patArr[patIdxStart] == '*' && patArr[patIdxTmp] == '*') {
			// Two stars next to each other, skip the first one.
			patIdxStart++;
			return true;
		}
		return false;
	}
{
        nonDottedNode_return retval = new nonDottedNode_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        indexer_return indexer69 = null;



        try {
            // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:136:2: ( indexer )
            // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:136:4: indexer
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_indexer_in_nonDottedNode737);
            indexer69=indexer();
            _fsp--;
            if (failed) return retval;
            if ( backtracking==0 ) adaptor.addChild(root_0, indexer69.getTree());

            }

            retval.stop = input.LT(-1);

            if ( backtracking==0 ) {
                retval.tree = (Object)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

                catch(RecognitionException e) {
                        reportError(e);
                        throw e;
                }
        finally {
        }
        return retval;
    }
{
        dottedNode_return retval = new dottedNode_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        methodOrProperty_return methodOrProperty70 = null;

        functionOrVar_return functionOrVar71 = null;

        projection_return projection72 = null;

        selection_return selection73 = null;

        firstSelection_return firstSelection74 = null;

        lastSelection_return lastSelection75 = null;

        exprList_return exprList76 = null;



        try {
            // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:139:2: ( ( ( methodOrProperty | functionOrVar | projection | selection | firstSelection | lastSelection | exprList ) ) )
            // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:140:2: ( ( methodOrProperty | functionOrVar | projection | selection | firstSelection | lastSelection | exprList ) )
            {
            root_0 = (Object)adaptor.nil();

            // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:140:2: ( ( methodOrProperty | functionOrVar | projection | selection | firstSelection | lastSelection | exprList ) )
            // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:140:3: ( methodOrProperty | functionOrVar | projection | selection | firstSelection | lastSelection | exprList )
            {
            // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:140:3: ( methodOrProperty | functionOrVar | projection | selection | firstSelection | lastSelection | exprList )
            int alt17=7;
            switch ( input.LA(1) ) {
            case ID:
                {
                alt17=1;
                }
                break;
            case POUND:
                {
                alt17=2;
                }
                break;
            case PROJECT:
                {
                alt17=3;
                }
                break;
            case SELECT:
                {
                alt17=4;
                }
                break;
            case SELECT_FIRST:
                {
                alt17=5;
                }
                break;
            case SELECT_LAST:
                {
                alt17=6;
                }
                break;
            case LPAREN:
                {
                alt17=7;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("140:3: ( methodOrProperty | functionOrVar | projection | selection | firstSelection | lastSelection | exprList )", 17, 0, input);

                throw nvae;
            }

            switch (alt17) {
                case 1 :
                    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:140:4: methodOrProperty
                    {
                    pushFollow(FOLLOW_methodOrProperty_in_dottedNode750);
                    methodOrProperty70=methodOrProperty();
                    _fsp--;
                    if (failed) return retval;
                    if ( backtracking==0 ) adaptor.addChild(root_0, methodOrProperty70.getTree());

                    }
                    break;
                case 2 :
                    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:141:4: functionOrVar
                    {
                    pushFollow(FOLLOW_functionOrVar_in_dottedNode756);
                    functionOrVar71=functionOrVar();
                    _fsp--;
                    if (failed) return retval;
                    if ( backtracking==0 ) adaptor.addChild(root_0, functionOrVar71.getTree());

                    }
                    break;
                case 3 :
                    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:142:7: projection
                    {
                    pushFollow(FOLLOW_projection_in_dottedNode764);
                    projection72=projection();
                    _fsp--;
                    if (failed) return retval;
                    if ( backtracking==0 ) adaptor.addChild(root_0, projection72.getTree());

                    }
                    break;
                case 4 :
                    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:143:7: selection
                    {
                    pushFollow(FOLLOW_selection_in_dottedNode773);
                    selection73=selection();
                    _fsp--;
                    if (failed) return retval;
                    if ( backtracking==0 ) adaptor.addChild(root_0, selection73.getTree());

                    }
                    break;
                case 5 :
                    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:144:7: firstSelection
                    {
                    pushFollow(FOLLOW_firstSelection_in_dottedNode782);
                    firstSelection74=firstSelection();
                    _fsp--;
                    if (failed) return retval;
                    if ( backtracking==0 ) adaptor.addChild(root_0, firstSelection74.getTree());

                    }
                    break;
                case 6 :
                    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:145:7: lastSelection
                    {
                    pushFollow(FOLLOW_lastSelection_in_dottedNode791);
                    lastSelection75=lastSelection();
                    _fsp--;
                    if (failed) return retval;
                    if ( backtracking==0 ) adaptor.addChild(root_0, lastSelection75.getTree());

                    }
                    break;
                case 7 :
                    // /Users/aclement/el2/spring-framework/trunk/org.springframework.expression/src/main/java/org/springframework/expression/spel/generated/SpringExpressions.g:146:7: exprList
                    {
                    pushFollow(FOLLOW_exprList_in_dottedNode800);
                    exprList76=exprList();
                    _fsp--;
                    if (failed) return retval;
                    if ( backtracking==0 ) adaptor.addChild(root_0, exprList76.getTree());

                    }
                    break;

            }


            }


            }

            retval.stop = input.LT(-1);

            if ( backtracking==0 ) {
                retval.tree = (Object)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

                catch(RecognitionException e) {
                        reportError(e);
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
			if (expectedReturnType != null) {
				@SuppressWarnings("unused")
				Object value = expr.getValue(eContext, expectedReturnType);
			} else {
				@SuppressWarnings("unused")
				Object value = expr.getValue(eContext);
			}
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

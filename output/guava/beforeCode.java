{
    switch (size()) {
      case 0:
        return ImmutableList.of();
      case 1:
        return ImmutableList.of(iterator().next());
      default:
        return new RegularImmutableAsList<E>(this, toArray());
    }
  }
{
    // We special-case for 0 or 1 elements, but going further is madness.
    if (!elements.hasNext()) {
      return of();
    }
    E first = elements.next();
    if (!elements.hasNext()) {
      return of(first);
    } else {
      return new ImmutableList.Builder<E>().add(first).addAll(elements).build();
    }
  }
{
    checkPositionIndexes(fromIndex, toIndex, size());
    int length = toIndex - fromIndex;
    if (length == size()) {
      return this;
    }
    switch (length) {
      case 0:
        return of();
      case 1:
        return of(get(fromIndex));
      default:
        return subListUnchecked(fromIndex, toIndex);
    }
  }
{
    @SuppressWarnings("unchecked") // we'll only be using getKey and getValue, which are covariant
    Entry<K, V>[] entryArray = (Entry<K, V>[]) Iterables.toArray(entries, EMPTY_ENTRY_ARRAY);
    switch (entryArray.length) {
      case 0:
        return of();
      case 1:
        Entry<K, V> onlyEntry = entryArray[0];
        return of(onlyEntry.getKey(), onlyEntry.getValue());
      default:
        /*
         * The current implementation will end up using entryArray directly, though it will write
         * over the (arbitrary, potentially mutable) Entry objects actually stored in entryArray.
         */
        return RegularImmutableMap.fromEntries(entryArray);
    }
  }
{
    checkNotNull(comparator);
    boolean hasSameComparator = SortedIterables.hasSameComparator(comparator, elements);

    if (hasSameComparator && (elements instanceof ImmutableSortedSet)) {
      @SuppressWarnings("unchecked")
      ImmutableSortedSet<E> original = (ImmutableSortedSet<E>) elements;
      if (!original.isPartialView()) {
        return original;
      }
    }
    @SuppressWarnings("unchecked") // elements only contains E's; it's safe.
    E[] array = (E[]) Iterables.toArray(elements);
    return construct(comparator, array.length, array);
  }
{
    Comparator<? super E> comparator = SortedIterables.comparator(sortedSet);
    ImmutableList<E> list = ImmutableList.copyOf(sortedSet);
    if (list.isEmpty()) {
      return emptySet(comparator);
    } else {
      return new RegularImmutableSortedSet<E>(list, comparator);
    }
  }
{
      if (Collections2.safeContains(backingSet(), key)) {
        @SuppressWarnings("unchecked") // unsafe, but Javadoc warns about it
        K k = (K) key;
        return function.apply(k);
      } else {
        return null;
      }
    }
{
      V1 value = fromMap.get(key);
      return (value != null || fromMap.containsKey(key))
          ? transformer.transformEntry((K) key, value)
          : null;
    }
{
    // We special-case for 0 or 1 elements, but going further is madness.
    if (!elements.hasNext()) {
      return of();
    }
    E first = elements.next();
    if (!elements.hasNext()) {
      return of(first);
    } else {
      return new ImmutableList.Builder<E>().add(first).addAll(elements).build();
    }
  }
{
    checkPositionIndexes(fromIndex, toIndex, size());
    int length = toIndex - fromIndex;
    if (length == size()) {
      return this;
    }
    switch (length) {
      case 0:
        return of();
      case 1:
        return of(get(fromIndex));
      default:
        return subListUnchecked(fromIndex, toIndex);
    }
  }
{
    CountingSupplier countingSupplier = new CountingSupplier();
    Supplier<Integer> memoizedSupplier = Suppliers.memoize(countingSupplier);
    checkMemoize(countingSupplier, memoizedSupplier);
  }
{
    CountingSupplier countingSupplier = new CountingSupplier();
    Supplier<Integer> memoizedSupplier = Suppliers.memoize(countingSupplier);
    assertSame(memoizedSupplier, Suppliers.memoize(memoizedSupplier));
  }
{
    Supplier<Integer> exceptingSupplier = new Supplier<Integer>() {
      @Override
      public Integer get() {
        throw new NullPointerException();
      }
    };

    Supplier<Integer> memoizedSupplier = Suppliers.memoize(exceptingSupplier);

    // call get() twice to make sure that memoization doesn't interfere
    // with throwing the exception
    for (int i = 0; i < 2; i++) {
      try {
        memoizedSupplier.get();
        fail("failed to throw NullPointerException");
      } catch (NullPointerException e) {
        // this is what should happen
      }
    }
  }
{
    for (Range<C> range : other.asRanges()) {
      if (!encloses(range)) {
        return false;
      }
    }
    return true;
  }
{
    for (Range<C> range : other.asRanges()) {
      add(range);
    }
  }
{
    for (Range<C> range : other.asRanges()) {
      remove(range);
    }
  }
{
      for (Range<C> range : ranges.asRanges()) {
        add(range);
      }
      return this;
    }
{
    Set<K> keySet = map.keySet();
    Collection<V> valueCollection = map.values();
    Set<Entry<K, V>> entrySet = map.entrySet();

    assertEquals(map.size() == 0, map.isEmpty());
    assertEquals(map.size(), keySet.size());
    assertEquals(keySet.size() == 0, keySet.isEmpty());
    assertEquals(!keySet.isEmpty(), keySet.iterator().hasNext());

    int expectedKeySetHash = 0;
    for (K key : keySet) {
      V value = map.get(key);
      expectedKeySetHash += key != null ? key.hashCode() : 0;
      assertTrue(map.containsKey(key));
      assertTrue(map.containsValue(value));
      assertTrue(valueCollection.contains(value));
      assertTrue(valueCollection.containsAll(Collections.singleton(value)));
      assertTrue(entrySet.contains(mapEntry(key, value)));
      assertTrue(allowsNullKeys || (key != null));
    }
    assertEquals(expectedKeySetHash, keySet.hashCode());

    assertEquals(map.size(), valueCollection.size());
    assertEquals(valueCollection.size() == 0, valueCollection.isEmpty());
    assertEquals(!valueCollection.isEmpty(), valueCollection.iterator().hasNext());
    for (V value : valueCollection) {
      assertTrue(map.containsValue(value));
      assertTrue(allowsNullValues || (value != null));
    }

    assertEquals(map.size(), entrySet.size());
    assertEquals(entrySet.size() == 0, entrySet.isEmpty());
    assertEquals(!entrySet.isEmpty(), entrySet.iterator().hasNext());
    assertFalse(entrySet.contains("foo"));

    boolean supportsValuesHashCode = supportsValuesHashCode(map);
    if (supportsValuesHashCode) {
      int expectedEntrySetHash = 0;
      for (Entry<K, V> entry : entrySet) {
        assertTrue(map.containsKey(entry.getKey()));
        assertTrue(map.containsValue(entry.getValue()));
        int expectedHash =
            (entry.getKey() == null ? 0 : entry.getKey().hashCode())
                ^ (entry.getValue() == null ? 0 : entry.getValue().hashCode());
        assertEquals(expectedHash, entry.hashCode());
        expectedEntrySetHash += expectedHash;
      }
      assertEquals(expectedEntrySetHash, entrySet.hashCode());
      assertTrue(entrySet.containsAll(new HashSet<Entry<K, V>>(entrySet)));
      assertTrue(entrySet.equals(new HashSet<Entry<K, V>>(entrySet)));
    }

    Object[] entrySetToArray1 = entrySet.toArray();
    assertEquals(map.size(), entrySetToArray1.length);
    assertTrue(Arrays.asList(entrySetToArray1).containsAll(entrySet));

    Entry<?, ?>[] entrySetToArray2 = new Entry<?, ?>[map.size() + 2];
    entrySetToArray2[map.size()] = mapEntry("foo", 1);
    assertSame(entrySetToArray2, entrySet.toArray(entrySetToArray2));
    assertNull(entrySetToArray2[map.size()]);
    assertTrue(Arrays.asList(entrySetToArray2).containsAll(entrySet));

    Object[] valuesToArray1 = valueCollection.toArray();
    assertEquals(map.size(), valuesToArray1.length);
    assertTrue(Arrays.asList(valuesToArray1).containsAll(valueCollection));

    Object[] valuesToArray2 = new Object[map.size() + 2];
    valuesToArray2[map.size()] = "foo";
    assertSame(valuesToArray2, valueCollection.toArray(valuesToArray2));
    assertNull(valuesToArray2[map.size()]);
    assertTrue(Arrays.asList(valuesToArray2).containsAll(valueCollection));

    if (supportsValuesHashCode) {
      int expectedHash = 0;
      for (Entry<K, V> entry : entrySet) {
        expectedHash += entry.hashCode();
      }
      assertEquals(expectedHash, map.hashCode());
    }

    assertMoreInvariants(map);
  }
{
    switch (size()) {
      case 0:
        return ImmutableList.of();
      case 1:
        return ImmutableList.of(iterator().next());
      default:
        return new RegularImmutableAsList<E>(this, toArray());
    }
  }
{
    // We special-case for 0 or 1 elements, but going further is madness.
    if (!elements.hasNext()) {
      return of();
    }
    E first = elements.next();
    if (!elements.hasNext()) {
      return of(first);
    } else {
      return new ImmutableList.Builder<E>().add(first).addAll(elements).build();
    }
  }
{
    checkPositionIndexes(fromIndex, toIndex, size());
    int length = toIndex - fromIndex;
    if (length == size()) {
      return this;
    }
    switch (length) {
      case 0:
        return of();
      case 1:
        return of(get(fromIndex));
      default:
        return subListUnchecked(fromIndex, toIndex);
    }
  }
{
    @SuppressWarnings("unchecked") // we'll only be using getKey and getValue, which are covariant
    Entry<K, V>[] entryArray = (Entry<K, V>[]) Iterables.toArray(entries, EMPTY_ENTRY_ARRAY);
    switch (entryArray.length) {
      case 0:
        return of();
      case 1:
        Entry<K, V> onlyEntry = entryArray[0];
        return of(onlyEntry.getKey(), onlyEntry.getValue());
      default:
        /*
         * The current implementation will end up using entryArray directly, though it will write
         * over the (arbitrary, potentially mutable) Entry objects actually stored in entryArray.
         */
        return RegularImmutableMap.fromEntries(entryArray);
    }
  }
{
    checkNotNull(comparator);
    boolean hasSameComparator = SortedIterables.hasSameComparator(comparator, elements);

    if (hasSameComparator && (elements instanceof ImmutableSortedSet)) {
      @SuppressWarnings("unchecked")
      ImmutableSortedSet<E> original = (ImmutableSortedSet<E>) elements;
      if (!original.isPartialView()) {
        return original;
      }
    }
    @SuppressWarnings("unchecked") // elements only contains E's; it's safe.
    E[] array = (E[]) Iterables.toArray(elements);
    return construct(comparator, array.length, array);
  }
{
    Comparator<? super E> comparator = SortedIterables.comparator(sortedSet);
    ImmutableList<E> list = ImmutableList.copyOf(sortedSet);
    if (list.isEmpty()) {
      return emptySet(comparator);
    } else {
      return new RegularImmutableSortedSet<E>(list, comparator);
    }
  }
{
    V value = checkedConnections(nodeU).value(nodeV);
    if (value == null) {
      checkArgument(containsNode(nodeV), NODE_NOT_IN_GRAPH, nodeV);
      return defaultValue;
    }
    return value;
  }
{
    V value = backingGraph.checkedConnections(nodeU).value(nodeV);
    if (value == null) {
      checkArgument(backingGraph.containsNode(nodeV), NODE_NOT_IN_GRAPH, nodeV);
      return defaultValue;
    }
    return value;
  }
{
    new EqualsTester().addEqualityGroup(
        network,
        Graphs.copyOf(network),
        ImmutableNetwork.copyOf(network)).testEquals();

    String networkString = network.toString();
    assertThat(networkString).contains("isDirected: " + network.isDirected());
    assertThat(networkString).contains("allowsParallelEdges: " + network.allowsParallelEdges());
    assertThat(networkString).contains("allowsSelfLoops: " + network.allowsSelfLoops());

    int nodeStart = networkString.indexOf("nodes:");
    int edgeStart = networkString.indexOf("edges:");
    String nodeString = networkString.substring(nodeStart, edgeStart);
    String edgeString = networkString.substring(edgeStart);

    Graph<Integer, Set<String>> asGraph = network.asGraph();
    AbstractGraphTest.validateGraph(asGraph);
    assertThat(network.nodes()).isEqualTo(asGraph.nodes());
    assertThat(network.edges().size()).isAtLeast(asGraph.edges().size());
    assertThat(network.nodeOrder()).isEqualTo(asGraph.nodeOrder());
    assertThat(network.isDirected()).isEqualTo(asGraph.isDirected());
    assertThat(network.allowsSelfLoops()).isEqualTo(asGraph.allowsSelfLoops());

    sanityCheckCollection(network.nodes());
    sanityCheckCollection(network.edges());
    sanityCheckCollection(asGraph.edges());

    for (String edge : network.edges()) {
      // TODO(b/27817069): Consider verifying the edge's incident nodes in the string.
      assertThat(edgeString).contains(edge);

      Endpoints<Integer> endpoints = network.incidentNodes(edge);
      Integer nodeA = endpoints.nodeA();
      Integer nodeB = endpoints.nodeB();
      assertThat(asGraph.edges()).contains(Endpoints.of(network, nodeA, nodeB));
      assertThat(network.edgesConnecting(nodeA, nodeB)).contains(edge);
      assertThat(network.successors(nodeA)).contains(nodeB);
      assertThat(network.adjacentNodes(nodeA)).contains(nodeB);
      assertThat(network.outEdges(nodeA)).contains(edge);
      assertThat(network.incidentEdges(nodeA)).contains(edge);
      assertThat(network.predecessors(nodeB)).contains(nodeA);
      assertThat(network.adjacentNodes(nodeB)).contains(nodeA);
      assertThat(network.inEdges(nodeB)).contains(edge);
      assertThat(network.incidentEdges(nodeB)).contains(edge);

      for (Integer incidentNode : ImmutableSet.of(
          network.incidentNodes(edge).nodeA(), network.incidentNodes(edge).nodeB())) {
        assertThat(network.nodes()).contains(incidentNode);
        for (String adjacentEdge : network.incidentEdges(incidentNode)) {
          assertTrue(edge.equals(adjacentEdge)
              || Graphs.adjacentEdges(network, edge).contains(adjacentEdge));
        }
      }
    }

    for (Integer node : network.nodes()) {
      assertThat(nodeString).contains(node.toString());

      assertThat(network.adjacentNodes(node)).isEqualTo(asGraph.adjacentNodes(node));
      assertThat(network.predecessors(node)).isEqualTo(asGraph.predecessors(node));
      assertThat(network.successors(node)).isEqualTo(asGraph.successors(node));

      sanityCheckCollection(network.adjacentNodes(node));
      sanityCheckCollection(network.predecessors(node));
      sanityCheckCollection(network.successors(node));
      sanityCheckCollection(network.incidentEdges(node));
      sanityCheckCollection(network.inEdges(node));
      sanityCheckCollection(network.outEdges(node));

      for (Integer otherNode : network.nodes()) {
        Set<String> edgesConnecting = network.edgesConnecting(node, otherNode);
        boolean isSelfLoop = node.equals(otherNode);
        if (network.isDirected() || !isSelfLoop) {
          assertThat(edgesConnecting).isEqualTo(asGraph.edgeValue(node, otherNode));
          assertThat(edgesConnecting).isEqualTo(
              Sets.intersection(network.outEdges(node), network.inEdges(otherNode)));
        }
        if (!network.allowsParallelEdges()) {
          assertThat(edgesConnecting.size()).isAtMost(1);
        }
        if (!network.allowsSelfLoops() && isSelfLoop) {
          assertThat(edgesConnecting).isEmpty();
        }
        for (String edge : edgesConnecting) {
          assertThat(network.incidentNodes(edge)).isEqualTo(Endpoints.of(network, node, otherNode));
        }
      }

      for (String incidentEdge : network.incidentEdges(node)) {
        assertTrue(network.inEdges(node).contains(incidentEdge)
            || network.outEdges(node).contains(incidentEdge));
        assertThat(network.edges()).contains(incidentEdge);
        assertTrue(network.incidentNodes(incidentEdge).nodeA().equals(node)
            || network.incidentNodes(incidentEdge).nodeB().equals(node));
      }

      for (String inEdge : network.inEdges(node)) {
        assertThat(network.incidentEdges(node)).contains(inEdge);
        assertThat(network.outEdges(network.incidentNodes(inEdge).adjacentNode(node)))
            .contains(inEdge);
      }

      for (String outEdge : network.outEdges(node)) {
        assertThat(network.incidentEdges(node)).contains(outEdge);
        assertThat(network.inEdges(network.incidentNodes(outEdge).adjacentNode(node)))
            .contains(outEdge);
      }

      for (Integer adjacentNode : network.adjacentNodes(node)) {
        assertTrue(network.predecessors(node).contains(adjacentNode)
            || network.successors(node).contains(adjacentNode));
        assertTrue(!network.edgesConnecting(node, adjacentNode).isEmpty()
            || !network.edgesConnecting(adjacentNode, node).isEmpty());
      }

      for (Integer predecessor : network.predecessors(node)) {
        assertThat(network.successors(predecessor)).contains(node);
        assertThat(network.edgesConnecting(predecessor, node)).isNotEmpty();
      }

      for (Integer successor : network.successors(node)) {
        assertThat(network.predecessors(successor)).contains(node);
        assertThat(network.edgesConnecting(node, successor)).isNotEmpty();
      }
    }
  }
{
    String propertiesString = String.format(
        "isDirected: %s, allowsParallelEdges: %s, allowsSelfLoops: %s",
        isDirected(), allowsParallelEdges(), allowsSelfLoops());
    Function<E, Endpoints<N>> edgeToEndpointsFn = new Function<E, Endpoints<N>>() {
      @Override
      public Endpoints<N> apply(E edge) {
        return incidentNodes(edge);
      }
    };
    return String.format(GRAPH_STRING_FORMAT,
        propertiesString,
        nodes(),
        Maps.asMap(edges(), edgeToEndpointsFn));
  }
{
    new EqualsTester().addEqualityGroup(
        graph,
        Graphs.copyOf(graph),
        ImmutableGraph.copyOf(graph)).testEquals();

    String graphString = graph.toString();
    assertThat(graphString).contains("isDirected: " + graph.isDirected());
    assertThat(graphString).contains("allowsSelfLoops: " + graph.allowsSelfLoops());

    int nodeStart = graphString.indexOf("nodes:");
    int edgeStart = graphString.indexOf("edges:");
    String nodeString = graphString.substring(nodeStart, edgeStart);

    Set<Endpoints<Integer>> allEndpoints = new HashSet<Endpoints<Integer>>();

    for (Integer node : graph.nodes()) {
      assertThat(nodeString).contains(node.toString());

      for (Integer adjacentNode : graph.adjacentNodes(node)) {
        assertThat(graph.predecessors(node).contains(adjacentNode)
            || graph.successors(node).contains(adjacentNode)).isTrue();
      }

      for (Integer predecessor : graph.predecessors(node)) {
        assertThat(graph.successors(predecessor)).contains(node);
      }

      for (Integer successor : graph.successors(node)) {
        Endpoints<Integer> endpoints = Endpoints.of(graph, node, successor);
        allEndpoints.add(endpoints);
        assertThat(graph.edges()).contains(endpoints);
        assertThat(graph.predecessors(successor)).contains(node);
      }
    }

    assertThat(graph.edges()).isEqualTo(allEndpoints);
  }
{
    Set<R> rowSpaceBuilder = new LinkedHashSet<R>();
    Set<C> columnSpaceBuilder = new LinkedHashSet<C>();
    ImmutableList<Cell<R, C, V>> cellList = ImmutableList.copyOf(cells);
    for (Cell<R, C, V> cell : cells) {
      rowSpaceBuilder.add(cell.getRowKey());
      columnSpaceBuilder.add(cell.getColumnKey());
    }

    ImmutableSet<R> rowSpace =
        (rowComparator == null)
            ? ImmutableSet.copyOf(rowSpaceBuilder)
            : ImmutableSet.copyOf(
                Ordering.from(rowComparator).immutableSortedCopy(rowSpaceBuilder));
    ImmutableSet<C> columnSpace =
        (columnComparator == null)
            ? ImmutableSet.copyOf(columnSpaceBuilder)
            : ImmutableSet.copyOf(
                Ordering.from(columnComparator).immutableSortedCopy(columnSpaceBuilder));

    // use a dense table if more than half of the cells have values
    // TODO(gak): tune this condition based on empirical evidence
    return (cellList.size() > (((long) rowSpace.size() * columnSpace.size()) / 2))
        ? new DenseImmutableTable<R, C, V>(cellList, rowSpace, columnSpace)
        : new SparseImmutableTable<R, C, V>(cellList, rowSpace, columnSpace);
  }
{
    Multiset<N> predecessors = getReference(predecessorsReference);
    if (predecessors == null) {
      predecessors = HashMultiset.create(inEdgeMap.values());
      predecessorsReference = new SoftReference<Multiset<N>>(predecessors);
    }
    return Collections.unmodifiableSet(predecessors.elementSet());
  }
{
    Multiset<N> successors = getReference(successorsReference);
    if (successors == null) {
      successors = HashMultiset.create(outEdgeMap.values());
      successorsReference = new SoftReference<Multiset<N>>(successors);
    }
    return Collections.unmodifiableSet(successors.elementSet());
  }
{
    Multiset<N> adjacentNodes = getReference(adjacentNodesReference);
    if (adjacentNodes == null) {
      adjacentNodes = HashMultiset.create(incidentEdgeMap.values());
      adjacentNodesReference = new SoftReference<Multiset<N>>(adjacentNodes);
    }
    return Collections.unmodifiableSet(adjacentNodes.elementSet());
  }
{
    return new ConcurrentHashMultiset<E>(mapMaker.<E, AtomicInteger>makeMap());
  }
{
    checkNotNull(graph, "graph");
    checkNotNull(nodePredicate, "nodePredicate");
    // TODO(b/28087289): we can remove this restriction when Graph supports parallel edges
    checkArgument(!((graph instanceof Network) && ((Network<N, ?>) graph).allowsParallelEdges()),
        NETWORK_WITH_PARALLEL_EDGE);
    MutableGraph<N> copy = copyBuilder.build();

    for (N node : Sets.filter(graph.nodes(), nodePredicate)) {
      copy.addNode(node);
      for (N successor : Sets.filter(graph.successors(node), nodePredicate)) {
        // TODO(b/28087289): Ensure that multiplicity is preserved if parallel edges are supported.
        copy.addEdge(node, successor);
      }
    }

    return copy;
  }
{
    checkNotNull(graph, "graph");
    checkNotNull(nodePredicate, "nodePredicate");
    checkNotNull(edgePredicate, "edgePredicate");
    MutableNetwork<N, E> copy = copyBuilder.build();

    copyNodesInternal(graph, copy, nodePredicate);
    copyEdgesInternal(graph, copy, edgePredicate);

    return copy;
  }
{
    switch (size()) {
      case 0:
        return ImmutableList.of();
      case 1:
        return ImmutableList.of(iterator().next());
      default:
        return new RegularImmutableAsList<E>(this, toArray());
    }
  }
{
    Class<?> matchedClass = of(supertype).getRawType();
    if (!this.someRawTypeIsSubclassOf(matchedClass)) {
      return false;
    }
    Type[] typeParams = matchedClass.getTypeParameters();
    Type[] toTypeArgs = supertype.getActualTypeArguments();
    for (int i = 0; i < typeParams.length; i++) {
      // If 'supertype' is "List<? extends CharSequence>"
      // and 'this' is StringArrayList,
      // First step is to figure out StringArrayList "is-a" List<E> and <E> is
      // String.
      // typeParams[0] is E and fromTypeToken.get(typeParams[0]) will resolve to
      // String.
      // String is then matched against <? extends CharSequence>.
      if (!resolveType(typeParams[i]).is(toTypeArgs[i])) {
        return false;
      }
    }
    return true;
  }
{
    Preconditions.checkNotNull(hostAddr);

    // Decide if this should be an IPv6 or IPv4 address.
    String ipString;
    int expectBytes;
    if (hostAddr.startsWith("[") && hostAddr.endsWith("]")) {
      ipString = hostAddr.substring(1, hostAddr.length() - 1);
      expectBytes = 16;
    } else {
      ipString = hostAddr;
      expectBytes = 4;
    }

    // Parse the address, and make sure the length/version is correct.
    byte[] addr = ipStringToBytes(ipString);
    if (addr == null || addr.length != expectBytes) {
      throw formatIllegalArgumentException("Not a valid URI IP literal: '%s'", hostAddr);
    }

    return bytesToInetAddress(addr);
  }
{
    IncidentNodes<N> incidentNodes = checkedIncidentNodes(edge);
    N node1 = incidentNodes.node1();
    N node2 = incidentNodes.node2();
    NodeConnections<N, E> connectionsN1 = nodeConnections.get(node1);
    NodeConnections<N, E> connectionsN2 = nodeConnections.get(node2);
    if (disconnectIncidentNodes) {
      connectionsN1.removeSuccessor(node2);
      connectionsN2.removePredecessor(node1);
    }
    connectionsN1.removeOutEdge(edge);
    connectionsN2.removeInEdge(edge);
    edgeToIncidentNodes.remove(edge);
  }
{
    IncidentNodes<N> incidentNodes = checkedIncidentNodes(edge);
    N node1 = incidentNodes.node1();
    N node2 = incidentNodes.node2();
    NodeConnections<N, E> connectionsN1 = nodeConnections.get(node1);
    NodeConnections<N, E> connectionsN2 = nodeConnections.get(node2);
    if (disconnectIncidentNodes) {
      connectionsN1.removeSuccessor(node2);
      connectionsN2.removePredecessor(node1);
    }
    connectionsN1.removeOutEdge(edge);
    connectionsN2.removeInEdge(edge);
    edgeToIncidentNodes.remove(edge);
  }
{
    // Check for null is needed to avoid frequent JNI calls to isInstance().
    if (throwable != null && declaredType.isInstance(throwable)) {
      throw declaredType.cast(throwable);
    }
  }
{
    return expireAfterWriteNanos > 0;
  }
{
    graph.addEdge(E12, N1, N2);

    Graph<Integer, String> g2;
    switch (graphType) {
      case UNDIRECTED:
        g2 = Graphs.createDirected();
        break;
      case DIRECTED:
        g2 = Graphs.createUndirected();
        break;
      default:
        throw new IllegalStateException("Unexpected graph type: " + graphType);
    }

    g2.addEdge(E12, N1, N2);

    new EqualsTester().addEqualityGroup(graph).addEqualityGroup(g2).testEquals();
  }
{
      directedGraph.addNode(node);
      return this;
    }
{
      directedGraph.addEdge(edge, node1, node2);
      return this;
    }
{
    checkNotNull(node, "node");
    // Return false if the node doesn't exist in the graph
    NodeConnections<N, E> connections = nodeConnections.get(node);
    if (connections == null) {
      return false;
    }
    // Since views are returned, we need to copy the edges that will be removed.
    // Thus we avoid modifying the underlying view while iterating over it.
    for (E inEdge : ImmutableList.copyOf(inEdges(node))) {
      N predecessor = Graphs.oppositeNode(this, inEdge, node);
      NodeConnections<N, E> predecessorConnections = nodeConnections.get(predecessor);
      predecessorConnections.removeSuccessor(node);
      predecessorConnections.removeOutEdge(inEdge);
      edgeToIncidentNodes.remove(inEdge);
    }
    for (E outEdge : ImmutableList.copyOf(outEdges(node))) {
      N successor = Graphs.oppositeNode(this, outEdge, node);
      NodeConnections<N, E> successorConnections = nodeConnections.get(successor);
      successorConnections.removePredecessor(node);
      successorConnections.removeInEdge(outEdge);
      edgeToIncidentNodes.remove(outEdge);
    }
    nodeConnections.remove(node);
    return true;
  }
{
    checkNotNull(edge, "edge");
    UndirectedIncidentNodes<N> incidentNodes = edgeToIncidentNodes.get(edge);
    checkArgument(incidentNodes != null, EDGE_NOT_IN_GRAPH, edge);
    return incidentNodes;
  }
{
    checkNotNull(edge, "edge");
    UndirectedIncidentNodes<N> incidentNodes = edgeToIncidentNodes.get(edge);
    checkArgument(incidentNodes != null, EDGE_NOT_IN_GRAPH, edge);
    return incidentNodes;
  }
{
    checkNotNull(node, "node");
    DirectedIncidentEdges<E> incidentEdges = nodeToIncidentEdges.get(node);
    checkArgument(incidentEdges != null, NODE_NOT_IN_GRAPH, node);
    return incidentEdges.inEdges();
  }
{
    checkNotNull(edge, "edge");
    DirectedIncidentNodes<N> endpoints = edgeToIncidentNodes.get(edge);
    checkArgument(endpoints != null, EDGE_NOT_IN_GRAPH, edge);
    return endpoints.asImmutableSet();
  }
{
    checkNotNull(node, "node");
    ImmutableSet<E> incidentEdges = nodeToIncidentEdges.get(node);
    checkArgument(incidentEdges != null, NODE_NOT_IN_GRAPH, node);
    return incidentEdges;
  }
{
    checkNotNull(node, "node");
    DirectedIncidentEdges<E> incidentEdges = nodeToIncidentEdges.get(node);
    checkArgument(incidentEdges != null, NODE_NOT_IN_GRAPH, node);
    return Collections.unmodifiableSet(incidentEdges.inEdges());
  }
{
    checkNotNull(edge, "edge");
    // Returning an immutable set here as the edge's endpoints will not change anyway.
    DirectedIncidentNodes<N> endpoints = edgeToIncidentNodes.get(edge);
    checkArgument(endpoints != null, EDGE_NOT_IN_GRAPH, edge);
    return endpoints.asImmutableSet();
  }
{
    checkNotNull(node, "node");
    Set<E> incidentEdges = nodeToIncidentEdges.get(node);
    checkArgument(incidentEdges != null, NODE_NOT_IN_GRAPH, node);
    return Collections.unmodifiableSet(incidentEdges);
  }
{
    checkNotNull(node, "node");
    NodeConnections<N, E> connections = nodeConnections.get(node);
    checkArgument(connections != null, NODE_NOT_IN_GRAPH, node);
    return connections;
  }
{
    checkNotNull(node, "node");
    NodeConnections<N, E> connections = nodeConnections.get(node);
    checkArgument(connections != null, NODE_NOT_IN_GRAPH, node);
    return connections;
  }
{
    checkNotNull(edge, "edge");
    DirectedIncidentNodes<N> endpoints = edgeToIncidentNodes.get(edge);
    checkArgument(endpoints != null, EDGE_NOT_IN_GRAPH, edge);
    return endpoints;
  }
{
    checkNotNull(edge, "edge");
    DirectedIncidentNodes<N> endpoints = edgeToIncidentNodes.get(edge);
    checkArgument(endpoints != null, EDGE_NOT_IN_GRAPH, edge);
    return endpoints;
  }
{
    checkNotNull(edge, "edge");
    DirectedIncidentNodes<N> endpoints = edgeToIncidentNodes.get(edge);
    checkArgument(endpoints != null, EDGE_NOT_IN_GRAPH, edge);
    return endpoints;
  }
{
    checkNotNull(node, "node");
    NodeConnections<N, E> connections = nodeConnections.get(node);
    checkArgument(connections != null, NODE_NOT_IN_GRAPH, node);
    return connections;
  }
{
    checkNotNull(node, "node");
    NodeConnections<N, E> connections = nodeConnections.get(node);
    checkArgument(connections != null, NODE_NOT_IN_GRAPH, node);
    return connections;
  }
{
    checkNotNull(node, "node");
    NodeConnections<N, E> connections = nodeConnections.get(node);
    checkArgument(connections != null, NODE_NOT_IN_GRAPH, node);
    return connections;
  }
{
    checkNotNull(node, "node");
    NodeConnections<N, E> connections = nodeConnections.get(node);
    checkArgument(connections != null, NODE_NOT_IN_GRAPH, node);
    return connections;
  }
{
    checkNotNull(node, "node");
    NodeConnections<N, E> connections = nodeConnections.get(node);
    checkArgument(connections != null, NODE_NOT_IN_GRAPH, node);
    return connections;
  }
{
    checkNotNull(edge, "edge");
    DirectedIncidentNodes<N> endpoints = edgeToIncidentNodes.get(edge);
    checkArgument(endpoints != null, EDGE_NOT_IN_GRAPH, edge);
    return endpoints;
  }
{
    checkNotNull(edge, "edge");
    DirectedIncidentNodes<N> endpoints = edgeToIncidentNodes.get(edge);
    checkArgument(endpoints != null, EDGE_NOT_IN_GRAPH, edge);
    return endpoints;
  }
{
    checkNotNull(edge, "edge");
    DirectedIncidentNodes<N> endpoints = edgeToIncidentNodes.get(edge);
    checkArgument(endpoints != null, EDGE_NOT_IN_GRAPH, edge);
    return endpoints;
  }
{
    checkNotNull(node, "node");
    NodeConnections<N, E> connections = nodeConnections.get(node);
    checkArgument(connections != null, NODE_NOT_IN_GRAPH, node);
    return connections;
  }
{
    checkNotNull(node, "node");
    NodeConnections<N, E> connections = nodeConnections.get(node);
    checkArgument(connections != null, NODE_NOT_IN_GRAPH, node);
    return connections;
  }
{
    checkNotNull(node, "node");
    DirectedIncidentEdges<E> incidentEdges = nodeToIncidentEdges.get(node);
    checkArgument(incidentEdges != null, NODE_NOT_IN_GRAPH, node);
    return incidentEdges.inEdges();
  }
{
    checkNotNull(edge, "edge");
    DirectedIncidentNodes<N> endpoints = edgeToIncidentNodes.get(edge);
    checkArgument(endpoints != null, EDGE_NOT_IN_GRAPH, edge);
    return endpoints.asImmutableSet();
  }
{
    checkNotNull(node, "node");
    ImmutableSet<E> incidentEdges = nodeToIncidentEdges.get(node);
    checkArgument(incidentEdges != null, NODE_NOT_IN_GRAPH, node);
    return incidentEdges;
  }
{
    checkNotNull(node, "node");
    DirectedIncidentEdges<E> incidentEdges = nodeToIncidentEdges.get(node);
    checkArgument(incidentEdges != null, NODE_NOT_IN_GRAPH, node);
    return Collections.unmodifiableSet(incidentEdges.inEdges());
  }
{
    checkNotNull(edge, "edge");
    // Returning an immutable set here as the edge's endpoints will not change anyway.
    DirectedIncidentNodes<N> endpoints = edgeToIncidentNodes.get(edge);
    checkArgument(endpoints != null, EDGE_NOT_IN_GRAPH, edge);
    return endpoints.asImmutableSet();
  }
{
    checkNotNull(node, "node");
    Set<E> incidentEdges = nodeToIncidentEdges.get(node);
    checkArgument(incidentEdges != null, NODE_NOT_IN_GRAPH, node);
    return Collections.unmodifiableSet(incidentEdges);
  }
{
    return concat(ImmutableList.of(a, b));
  }
{
    return concat(ImmutableList.of(a, b, c));
  }
{
    return concat(ImmutableList.of(a, b, c, d));
  }
{
    checkNotNull(inputs);
    return new FluentIterable<T>() {
      @Override
      public Iterator<T> iterator() {
        return Iterators.concat(Iterables.transform(inputs, Iterables.<T>toIterator()).iterator());
      }
    };
  }
{
    if (checkNotNull(string).isEmpty()) {
      return null;
    }
    boolean negative = string.charAt(0) == '-';
    int index = negative ? 1 : 0;
    if (index == string.length()) {
      return null;
    }
    int digit = string.charAt(index++) - '0';
    if (digit < 0 || digit > 9) {
      return null;
    }
    long accum = -digit;
    while (index < string.length()) {
      digit = string.charAt(index++) - '0';
      if (digit < 0 || digit > 9 || accum < Long.MIN_VALUE / 10) {
        return null;
      }
      accum *= 10;
      if (accum < Long.MIN_VALUE + digit) {
        return null;
      }
      accum -= digit;
    }

    if (negative) {
      return accum;
    } else if (accum == Long.MIN_VALUE) {
      return null;
    } else {
      return -accum;
    }
  }
{
      if (modCount != expectedModCount) {
        throw new ConcurrentModificationException();
      }
    }
{
      if (modCount != expectedModCount) {
        throw new ConcurrentModificationException();
      }
    }
{
    @SuppressWarnings("unchecked") // we'll only be using getKey and getValue, which are covariant
    Entry<K, V>[] entryArray = (Entry<K, V>[]) Iterables.toArray(entries, EMPTY_ENTRY_ARRAY);
    switch (entryArray.length) {
      case 0:
        return of();
      case 1:
        Entry<K, V> onlyEntry = entryArray[0];
        return of(onlyEntry.getKey(), onlyEntry.getValue());
      default:
        /*
         * The current implementation will end up using entryArray directly, though it will write
         * over the (arbitrary, potentially mutable) Entry objects actually stored in entryArray.
         */
        return RegularImmutableMap.fromEntries(entryArray);
    }
  }
{
    return using(new NavigableMapSubmapTestMapGenerator<K, V>(delegate, to, from));
  }
{
    StringBuilder builder = new StringBuilder().append(type).append('/').append(subtype);
    if (!parameters.isEmpty()) {
      builder.append("; ");
      Multimap<String, String> quotedParameters = Multimaps.transformValues(parameters,
          new Function<String, String>() {
            @Override public String apply(String value) {
              return TOKEN_MATCHER.matchesAllOf(value) ? value : escapeAndQuote(value);
            }
          });
      PARAMETER_JOINER.appendTo(builder, quotedParameters.entries());
    }
    return builder.toString();
  }
{
    // before jdk7u40: creates one-bucket table
    // after  jdk7u40: creates empty table
    assertTrue(bucketsOf(Maps.newHashMapWithExpectedSize(0)) <= 1);

    for (int size = 1; size < 200; size++) {
      HashMap<Integer, Void> map1 = Maps.newHashMapWithExpectedSize(size);

      // Only start measuring table size after the first element inserted, to
      // deal with empty-map optimization.
      map1.put(0, null);

      int initialBuckets = bucketsOf(map1);

      for (int i = 1; i < size; i++) {
        map1.put(i, null);
      }
      assertEquals("table size after adding " + size + " elements",
          initialBuckets, bucketsOf(map1));

      /*
       * Something slightly different happens when the entries are added all at
       * once; make sure that passes too.
       */
      HashMap<Integer, Void> map2 = Maps.newHashMapWithExpectedSize(size);
      map2.putAll(map1);
      assertEquals("table size after adding " + size + " elements: ",
          initialBuckets, bucketsOf(map2));
    }
  }
{
    return (ImmutableSortedSet<E>) NATURAL_EMPTY_SET;
  }
{
    checkNotNull(comparator);
    boolean hasSameComparator =
        SortedIterables.hasSameComparator(comparator, elements);

    if (hasSameComparator && (elements instanceof ImmutableSortedSet)) {
      @SuppressWarnings("unchecked")
      ImmutableSortedSet<E> original = (ImmutableSortedSet<E>) elements;
      if (!original.isPartialView()) {
        return original;
      }
    }
    @SuppressWarnings("unchecked") // elements only contains E's; it's safe.
    E[] array = (E[]) Iterables.toArray(elements);
    return construct(comparator, array.length, array);
  }
{
    Comparator<? super E> comparator = SortedIterables.comparator(sortedSet);
    ImmutableList<E> list = ImmutableList.copyOf(sortedSet);
    if (list.isEmpty()) {
      return emptySet(comparator);
    } else {
      return new RegularImmutableSortedSet<E>(list, comparator);
    }
  }
{
    checkNotNull(future);
    checkNotNull(unit);
    checkArgument(!RuntimeException.class.isAssignableFrom(exceptionClass),
        "Futures.getChecked exception type (%s) must not be a RuntimeException",
        exceptionClass);
    try {
      return future.get(timeout, unit);
    } catch (InterruptedException e) {
      currentThread().interrupt();
      throw newWithCause(exceptionClass, e);
    } catch (TimeoutException e) {
      throw newWithCause(exceptionClass, e);
    } catch (ExecutionException e) {
      wrapAndThrowExceptionOrError(e.getCause(), exceptionClass);
      throw new AssertionError();
    }
  }
{
    return copyFromEntries(multiset.entrySet());
  }
{
    return copyFromEntries(multiset.entrySet());
  }
{
    checkNotNull(future);
    checkArgument(!RuntimeException.class.isAssignableFrom(exceptionClass),
        "Futures.get exception type (%s) must not be a RuntimeException",
        exceptionClass);
    try {
      return future.get();
    } catch (InterruptedException e) {
      currentThread().interrupt();
      throw newWithCause(exceptionClass, e);
    } catch (ExecutionException e) {
      wrapAndThrowExceptionOrError(e.getCause(), exceptionClass);
      throw new AssertionError();
    }
  }
{
    checkNotNull(future);
    checkNotNull(unit);
    checkArgument(!RuntimeException.class.isAssignableFrom(exceptionClass),
        "Futures.get exception type (%s) must not be a RuntimeException",
        exceptionClass);
    try {
      return future.get(timeout, unit);
    } catch (InterruptedException e) {
      currentThread().interrupt();
      throw newWithCause(exceptionClass, e);
    } catch (TimeoutException e) {
      throw newWithCause(exceptionClass, e);
    } catch (ExecutionException e) {
      wrapAndThrowExceptionOrError(e.getCause(), exceptionClass);
      throw new AssertionError();
    }
  }
{
      if (8 % alphabet.bitsPerChar == 0 ||
          (paddingChar != null && paddingChar.charValue() == padChar)) {
        return this;
      } else {
        return new StandardBaseEncoding(alphabet, padChar);
      }
    }
{
    byte[] bytes;
    try {
      // GWT does not support String.getBytes(Charset)
      bytes = decoded.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError();
    }
    assertEquals(encoded, encoding.encode(bytes));
  }
{
      return new StandardBaseEncoding(alphabet, paddingChar);
    }
{
      if (8 % alphabet.bitsPerChar == 0 ||
          (paddingChar != null && paddingChar.charValue() == padChar)) {
        return this;
      } else {
        return new StandardBaseEncoding(alphabet, padChar);
      }
    }
{
    ChainingListenableFuture<I, O> output =
        new ChainingListenableFuture<I, O>(function, input);
    input.addListener(output, directExecutor());
    return output;
  }
{
    checkNotNull(executor);
    ChainingListenableFuture<I, O> output =
        new ChainingListenableFuture<I, O>(function, input);
    input.addListener(rejectionPropagatingRunnable(output, output, executor), directExecutor());
    return output;
  }
{
    checkNotNull(fallback);
    return new FallbackFuture<V>(input, fallback, executor);
  }
{
    Scanner scanner = new Scanner();
    for (Map.Entry<File, ClassLoader> entry : getClassPathEntries(classloader).entrySet()) {
      scanner.scan(entry.getKey(), entry.getValue());
    }
    return scanner.getEntries();
  }
{
    Scanner scanner = new Scanner();
    for (Map.Entry<File, ClassLoader> entry : getClassPathEntries(classloader).entrySet()) {
      scanner.scan(entry.getKey(), entry.getValue());
    }
    return new ClassPath(scanner.getResources());
  }
{
    // TODO(user): generalize to removing an Iterable, perhaps
    checkNotNull(multisetToModify);
    checkNotNull(occurrencesToRemove);

    boolean changed = false;
    Iterator<Entry<E>> entryIterator = multisetToModify.entrySet().iterator();
    while (entryIterator.hasNext()) {
      Entry<E> entry = entryIterator.next();
      int removeCount = occurrencesToRemove.count(entry.getElement());
      if (removeCount >= entry.getCount()) {
        entryIterator.remove();
        changed = true;
      } else if (removeCount > 0) {
        multisetToModify.remove(entry.getElement(), removeCount);
        changed = true;
      }
    }
    return changed;
  }
{
    Scanner scanner = new Scanner();
    for (Map.Entry<File, ClassLoader> entry : getClassPathEntries(classloader).entrySet()) {
      scanner.scan(entry.getKey(), entry.getValue());
    }
    return scanner.getEntries();
  }
{
    Scanner scanner = new Scanner();
    for (Map.Entry<File, ClassLoader> entry : getClassPathEntries(classloader).entrySet()) {
      scanner.scan(entry.getKey(), entry.getValue());
    }
    return new ClassPath(scanner.getResources());
  }
{
    Preconditions.checkNotNull(r, "'r' must not be null.");
    boolean scheduleTaskRunner = false;
    synchronized (internalLock) {
      waitQueue.add(r);

      if (!isThreadScheduled) {
        isThreadScheduled = true;
        scheduleTaskRunner = true;
      }
    }
    if (scheduleTaskRunner) {
      boolean threw = true;
      try {
        executor.execute(taskRunner);
        threw = false;
      } finally {
        if (threw) {
          synchronized (internalLock) {
            // It is possible that at this point that there are still tasks in
            // the queue, it would be nice to keep trying but the error may not
            // be recoverable.  So we update our state and propogate so that if
            // our caller deems it recoverable we won't be stuck.
            isThreadScheduled = false;
          }
        }
      }
    }
  }
{
    assertThat(future.isDone()).isFalse();
    assertThat(future.isCancelled()).isFalse();

    verifyGetOnPendingFuture(future);
    verifyTimedGetOnPendingFuture(future);
  }
{
    future.cancel(true /* mayInterruptIfRunning */);

    assertThat(future.isDone()).isTrue();
    assertThat(future.isCancelled()).isTrue();

    try {
      future.get();
      fail();
    } catch (CancellationException expected) {
    }

    try {
      future.get(0, TimeUnit.SECONDS);
      fail();
    } catch (CancellationException expected) {
    }
  }
{
    synchronized (internalLock) {
      // We sometimes try to start a queue worker without knowing if there is any work to do.
      if (queue.peek() == null) {
        return;
      }
      if (suspensions > 0) {
        return;
      }
      if (isWorkerRunning) {
        return;
      }
      isWorkerRunning = true;
    }
    boolean executionRejected = true;
    try {
      executor.execute(new QueueWorker());
      executionRejected = false;
    } finally {
      if (executionRejected) {
        // The best we can do is to stop executing the queue, but reset the state so that
        // execution can be resumed later if the caller so wishes.
        synchronized (internalLock) {
          isWorkerRunning = false;
        }
      }
    }
  }
{
    Preconditions.checkNotNull(r, "'r' must not be null.");
    boolean scheduleTaskRunner = false;
    synchronized (internalLock) {
      waitQueue.add(r);

      if (!isThreadScheduled) {
        isThreadScheduled = true;
        scheduleTaskRunner = true;
      }
    }
    if (scheduleTaskRunner) {
      boolean threw = true;
      try {
        executor.execute(taskRunner);
        threw = false;
      } finally {
        if (threw) {
          synchronized (internalLock) {
            // It is possible that at this point that there are still tasks in
            // the queue, it would be nice to keep trying but the error may not
            // be recoverable.  So we update our state and propogate so that if
            // our caller deems it recoverable we won't be stuck.
            isThreadScheduled = false;
          }
        }
      }
    }
  }
{
    try {
      encoding.decode(cannotDecode);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException expected) {
      // success
    }
    try {
      encoding.decodeChecked(cannotDecode);
      fail("Expected DecodingException");
    } catch (DecodingException expected) {
      // success
    }
  }
{
    if (to.equals(from)) {
      return true;
    }
    if (to instanceof WildcardType) {
      return isAssignableToWildcardType(from, (WildcardType) to);
    }
    // if "from" is type variable, it's assignable if any of its "extends"
    // bounds is assignable to "to".
    if (from instanceof TypeVariable) {
      return isAssignableFromAny(((TypeVariable<?>) from).getBounds(), to);
    }
    // if "from" is wildcard, it'a assignable to "to" if any of its "extends"
    // bounds is assignable to "to".
    if (from instanceof WildcardType) {
      return isAssignableFromAny(((WildcardType) from).getUpperBounds(), to);
    }
    if (from instanceof GenericArrayType) {
      return isAssignableFromGenericArrayType((GenericArrayType) from, to);
    }
    // Proceed to regular Type assignability check
    if (to instanceof Class) {
      return isAssignableToClass(from, (Class<?>) to);
    } else if (to instanceof ParameterizedType) {
      return isAssignableToParameterizedType(from, (ParameterizedType) to);
    } else if (to instanceof GenericArrayType) {
      return isAssignableToGenericArrayType(from, (GenericArrayType) to);
    } else { // to instanceof TypeVariable
      return false;
    }
  }
{
      if (future != null) {
        future.cancel(mayInterruptIfRunning);
      }
    }
{
      for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
        put(entry.getKey(), entry.getValue());
      }
      return this;
    }
{
      for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
        put(entry.getKey(), entry.getValue());
      }
      return this;
    }
{
      switch (size) {
        case 0:
          return of();
        case 1:
          return of(entries[0].getKey(), entries[0].getValue());
        default:
          return new RegularImmutableMap<K, V>(size, entries);
      }
    }
{
      switch (size) {
        case 0:
          return of();
        case 1:
          return of(entries[0].getKey(), entries[0].getValue());
        default:
          return new RegularImmutableMap<K, V>(size, entries);
      }
    }
{
    // TODO(user): consider sharing this code with RegularImmutableBiMap
    if (key == null) {
      return null;
    }
    int index = Hashing.smear(key.hashCode()) & mask;
    for (ImmutableMapEntry<K, V> entry = table[index];
        entry != null;
        entry = entry.getNextInKeyBucket()) {
      K candidateKey = entry.getKey();

      /*
       * Assume that equals uses the == optimization when appropriate, and that
       * it would check hash codes as an optimization when appropriate. If we
       * did these things, it would just make things worse for the most
       * performance-conscious users.
       */
      if (key.equals(candidateKey)) {
        return entry.getValue();
      }
    }
    return null;
  }
{
    return description;
  }
{
    Class<?> rawType = type.getRawType();
    List<Object> samples = sampleInstances.get(rawType);
    Object sample = nextInstance(samples, null);
    if (sample != null) {
      return sample;
    }
    if (rawType.isEnum()) {
      return nextInstance(rawType.getEnumConstants(), null);
    }
    if (type.isArray()) {
      TypeToken<?> componentType = type.getComponentType();
      Object array = Array.newInstance(componentType.getRawType(), 1);
      Array.set(array, 0, generate(componentType));
      return array;
    }
    if (rawType == Optional.class && generatedOptionalTypes.add(type.getType())) {
      // For any Optional<T>, we'll first generate absent(). The next call generates a distinct
      // value of T to be wrapped in Optional.of().
      return Optional.absent();
    }
    Method generator = GENERATORS.get(rawType);
    if (generator != null) {
      ImmutableList<Parameter> params = Invokable.from(generator).getParameters();
      List<Object> args = Lists.newArrayListWithCapacity(params.size());
      TypeVariable<?>[] typeVars = rawType.getTypeParameters();
      for (int i = 0; i < params.size(); i++) {
        TypeToken<?> paramType = type.resolveType(typeVars[i]);
        // We require all @Generates methods to either be parameter-less or accept non-null
        // fresh values for their generic parameter types.
        Object argValue = generate(paramType);
        if (argValue == null) {
          // When a parameter of a @Generates method cannot be created,
          // The type most likely is a collection.
          // Our distinct proxy doesn't work for collections.
          // So just refuse to generate.
          return null;
        }
        args.add(argValue);
      }
      try {
        return generator.invoke(this, args.toArray());
      } catch (InvocationTargetException e) {
        Throwables.propagate(e.getCause());
      } catch (Exception e) {
        throw Throwables.propagate(e);
      }
    }
    return defaultGenerate(rawType);
  }
{
    FreshValueGenerator generator = new FreshValueGenerator();
    Object value1 = generator.generate(type);
    Object value2 = generator.generate(type);
    assertNotNull("Null returned for " + type, value1);
    assertFalse("Equal instance " + value1 + " returned for " + type, value1.equals(value2));
  }
{
    assertFreshInstance(new TypeToken<ConcurrentMap<String, ?>>() {});
    FreshValueGenerator generator = new FreshValueGenerator();
    ConcurrentMap<String, Integer> expected = Maps.newConcurrentMap();
    expected.put(generator.generate(String.class), generator.generate(int.class));
    assertValueAndTypeEquals(expected,
        new FreshValueGenerator().generate(new TypeToken<ConcurrentMap<String, Integer>>() {}));
    assertNotInstantiable(new TypeToken<ConcurrentMap<EmptyEnum, String>>() {});
  }
{
    for (Set<Locale> uselessLocales = Sets.newHashSet(); ; ) {
      Locale locale = freshLocale();
      if (uselessLocales.contains(locale)) { // exhausted all locales
        return Currency.getInstance(Locale.US);
      }
      try {
        return Currency.getInstance(locale);
      } catch (IllegalArgumentException e) {
        uselessLocales.add(locale);
      }
    }
  }
{
    long timeoutNanos = unit.toNanos(time);
    final ReentrantLock lock = this.lock;
    if (!fair && lock.tryLock()) {
      return true;
    }
    long deadline = System.nanoTime() + timeoutNanos;
    boolean interrupted = Thread.interrupted();
    try {
      while (true) {
        try {
          return lock.tryLock(timeoutNanos, TimeUnit.NANOSECONDS);
        } catch (InterruptedException interrupt) {
          interrupted = true;
          timeoutNanos = deadline - System.nanoTime();
        }
      }
    } finally {
      if (interrupted) {
        Thread.currentThread().interrupt();
      }
    }
  }
{
    return new ConverterComposition<A, B, C>(this, checkNotNull(secondConverter));
  }
{
    checkNotNull(funnel);
    checkArgument(expectedInsertions >= 0, "Expected insertions (%s) must be >= 0",
        expectedInsertions);
    checkArgument(fpp > 0.0, "False positive probability (%s) must be > 0.0", fpp);
    checkArgument(fpp < 1.0, "False positive probability (%s) must be < 1.0", fpp);
    if (expectedInsertions == 0) {
      expectedInsertions = 1;
    }
    /*
     * TODO(user): Put a warning in the javadoc about tiny fpp values,
     * since the resulting size is proportional to -log(p), but there is not
     * much of a point after all, e.g. optimalM(1000, 0.0000000000000001) = 76680
     * which is less than 10kb. Who cares!
     */
    long numBits = optimalNumOfBits(expectedInsertions, fpp);
    int numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
    try {
      return new BloomFilter<T>(new BitArray(numBits), numHashFunctions, funnel,
          BloomFilterStrategies.MURMUR128_MITZ_32);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Could not create BloomFilter of " + numBits + " bits", e);
    }
  }
{
    checkPermits(permits);
    long microsToWait;
    synchronized (mutex) {
      microsToWait = reserveNextTicket(permits, readSafeMicros());
    }
    ticker.sleepMicrosUninterruptibly(microsToWait);
    return 1.0 * microsToWait / TimeUnit.SECONDS.toMicros(1L);
  }
{
    int utf16Length = sequence.length();
    long utf8Length = utf16Length;  // Optimize for runs of ASCII
    for (int i = 0; i < utf16Length; i++) {
      char c = sequence.charAt(i);
      if (c < 0x80) {
        // already counted
      } else if (c < 0x800) {
        utf8Length += 1;
      } else {
        utf8Length += 2;
        // jdk7+: if (Character.isSurrogate(c)) {
        if (Character.MIN_SURROGATE <= c && c <= Character.MAX_SURROGATE) {
          // Expect a surrogate pair.
          int cp = Character.codePointAt(sequence, i);
          if (cp < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
            // The pair starts with a low surrogate, or no character follows the high surrogate.
            throw new IllegalArgumentException("Unpaired surrogate at index " + i);
          }
          i++;
        }
      }
    }
    // return Ints.checkedCast(utf8Length);
    int result = (int) utf8Length;
    if (result != utf8Length) {
      throw new IllegalArgumentException("UTF-8 length does not fit in int: " + utf8Length);
    }
    return result;
  }
{
      ServiceManagerState state = this.state.get();
      if (state != null) {
        state.transitionService(service, from, to);
        return true;
      }
      return false;
    }
{
      ServiceManagerState state = this.state.get();
      if (state != null) {
        state.transitionService(service, from, to);
        return true;
      }
      return false;
    }
{
      ServiceManagerState state = this.state.get();
      if (state != null) {
        state.transitionService(service, from, to);
        return true;
      }
      return false;
    }
{
      ServiceManagerState state = this.state.get();
      if (state != null) {
        state.transitionService(service, from, to);
        return true;
      }
      return false;
    }
{
      ServiceManagerState state = this.state.get();
      if (state != null) {
        state.transitionService(service, from, to);
        return true;
      }
      return false;
    }
{
      checkState(unstoppedServices > 0, 
          "All services should have already stopped but %s just stopped.", service);
      unstoppedServices--;
      if (unstoppedServices == 0) {
        checkState(unstartedServices == 0, 
            "All services are stopped but %d services haven't finished starting", 
            unstartedServices);
        for (final ListenerExecutorPair pair : listeners) {
          queuedListeners.add(new Runnable() {
            @Override public void run() {
              pair.listener.stopped();
            }
          }, pair.executor);
        }
        // no more listeners could possibly be called, so clear them out
        listeners.clear();
      }
    }
{
      synchronized (watch) {
        if (!watch.isRunning()) { // only start the watch once.
          watch.start();
          if (!(service instanceof NoOpService)) {
            logger.log(Level.FINE, "Starting {0}.", service);
          }
        }
      }
    }
{
    ImmutableMultimap.Builder<State, Service> builder = ImmutableMultimap.builder();
    for (Service service : services.keySet()) {
      if (!(service instanceof NoOpService)) {
        builder.put(service.state(), service);
      }
    }
    return builder.build();
  }
{ 
    List<Entry<Service, Long>> loadTimes = Lists.newArrayListWithCapacity(services.size());
    for (Map.Entry<Service, ServiceListener> entry : services.entrySet()) {
      Service service = entry.getKey();
      State state = service.state();
      if (state != State.NEW & state != State.STARTING & !(service instanceof NoOpService)) {
        loadTimes.add(Maps.immutableEntry(service, entry.getValue().startupTimeMillis()));
      }
    }
    Collections.sort(loadTimes, Ordering.<Long>natural()
        .onResultOf(new Function<Entry<Service, Long>, Long>() {
          @Override public Long apply(Map.Entry<Service, Long> input) {
            return input.getValue();
          }
        }));
    ImmutableMap.Builder<Service, Long> builder = ImmutableMap.builder();
    for (Entry<Service, Long> entry : loadTimes) {
      builder.put(entry);
    }
    return builder.build();
  }
{
    assertEquals("CharMatcher.NONE", CharMatcher.anyOf("").toString());
    assertEquals("CharMatcher.is('\\u0031')", CharMatcher.anyOf("1").toString());
    assertEquals("CharMatcher.isNot('\\u0031')", CharMatcher.isNot('1').toString());
    assertEquals("CharMatcher.anyOf(\"\\u0031\\u0032\")", CharMatcher.anyOf("12").toString());
    assertEquals("CharMatcher.anyOf(\"\\u0031\\u0032\\u0033\")",
        CharMatcher.anyOf("321").toString());
    assertEquals("CharMatcher.inRange('\\u0031', '\\u0033')",
        CharMatcher.inRange('1', '3').toString());
  }
{
      switch (size) {
        case 0:
          return of();
        case 1:
          return of(entries[0].getKey(), entries[0].getValue());
        default:
          return new RegularImmutableMap<K, V>(size, entries);
      }
    }
{
    return new IntHashCode(hash);
  }
{
    return new LongHashCode(hash);
  }
{
    checkArgument(bytes.length >= 1, "A HashCode must contain at least 1 byte.");
    return fromBytesNoCopy(bytes.clone());
  }
{
    return new MediaType(type, subtype, ImmutableListMultimap.<String, String>of());
  }
{
    return supplier;
  }
{
      InputStream in = ByteSource.this.openStream();
      if (offset > 0) {
        try {
          ByteStreams.skipFully(in, offset);
        } catch (Throwable e) {
          Closer closer = Closer.create();
          closer.register(in);
          try {
            throw closer.rethrow(e);
          } finally {
            closer.close();
          }
        }
      }
      return ByteStreams.limit(in, length);
    }
{
      switch (size) {
        case 0:
          return of();
        case 1:
          return of(entries[0].getKey(), entries[0].getValue());
        default:
          return new RegularImmutableMap<K, V>(size, entries);
      }
    }
{
      try {
        return future.setException(t);
      } catch (Error e) {
        // the error will already be propagated by the loading thread
        return false;
      }
    }
{
      Type[] types = constructor.getGenericParameterTypes();
      Class<?> declaringClass = constructor.getDeclaringClass();
      if (!Modifier.isStatic(declaringClass.getModifiers())
          && declaringClass.getEnclosingClass() != null) {
        if (types.length == constructor.getParameterTypes().length) {
          // first parameter is the hidden 'this'
          return Arrays.copyOfRange(types, 1, types.length);
        }
      }
      return types;
    }
{
    ImmutableSet.Builder<Class<?>> builder = ImmutableSet.builder();
    for (Type type : types) {
      builder.addAll(getRawTypes(type));
    }
    return builder.build();
  }
{
    for (int i = 0, len = charSequence.length(); i < len; i++) {
      putChar(charSequence.charAt(i));
    }
    return this;
  }
{
    int len = input.length();
    Hasher hasher = newHasher(len * 2);
    for (int i = 0; i < len; i++) {
      hasher.putChar(input.charAt(i));
    }
    return hasher.hash();
  }
{
    return newHasher().putString(input).hash();
  }
{
      for (int i = 0; i < charSequence.length(); i++) {
        putChar(charSequence.charAt(i));
      }
      return this;
    }
{
      switch (size) {
        case 0:
          return of();
        case 1:
          return of(entries[0].getKey(), entries[0].getValue());
        default:
          return new RegularImmutableMap<K, V>(size, entries);
      }
    }
{
      switch (size) {
        case 0:
          return of();
        case 1:
          return of(entries[0].getKey(), entries[0].getValue());
        default:
          return new RegularImmutableMap<K, V>(size, entries);
      }
    }
{
      switch (size) {
        case 0:
          return of();
        case 1:
          return of(entries[0].getKey(), entries[0].getValue());
        default:
          return new RegularImmutableMap<K, V>(size, entries);
      }
    }
{
      switch (size) {
        case 0:
          return of();
        case 1:
          return of(entries[0].getKey(), entries[0].getValue());
        default:
          return new RegularImmutableMap<K, V>(size, entries);
      }
    }
{
    switch (n) {
      case 0:
        return of();
      case 1: {
        @SuppressWarnings("unchecked") // all callers only have Ks here
        K key = (K) entries[0].getKey();
        @SuppressWarnings("unchecked") // all callers only have Vs here
        V value = (V) entries[0].getValue();
        return new SingletonImmutableBiMap<K, V>(key, value);
      }
      default:
        return new RegularImmutableBiMap<K, V>(n, entries);
    }
  }
{
      switch (size) {
        case 0:
          return of();
        case 1:
          return of(entries[0].getKey(), entries[0].getValue());
        default:
          return new RegularImmutableMap<K, V>(size, (TerminalMapEntry<?, ?>[]) entries);
      }
    }
{
      switch (size) {
        case 0:
          return of();
        case 1:
          return of(entries[0].getKey(), entries[0].getValue());
        default:
          return new RegularImmutableMap<K, V>(size, (TerminalMapEntry<?, ?>[]) entries);
      }
    }
{
      Function<Map<C, V1>, Map<C, V2>> rowFunction =
          new Function<Map<C, V1>, Map<C, V2>>() {
            @Override public Map<C, V2> apply(Map<C, V1> row) {
              return Maps.transformValues(row, function);
            }
          };
      return Maps.transformValues(fromTable.rowMap(), rowFunction);
    }
{
      Function<Map<R, V1>, Map<R, V2>> columnFunction =
          new Function<Map<R, V1>, Map<R, V2>>() {
            @Override public Map<R, V2> apply(Map<R, V1> column) {
              return Maps.transformValues(column, function);
            }
          };
      return Maps.transformValues(fromTable.columnMap(), columnFunction);
    }
{
    // We special-case for 0 or 1 elements, but going further is madness.
    if (!elements.hasNext()) {
      return of();
    }
    E first = elements.next();
    if (!elements.hasNext()) {
      return of(first);
    } else {
      return new ImmutableList.Builder<E>()
          .add(first)
          .addAll(elements)
          .build();
    }
  }
{
    checkPositionIndexes(fromIndex, toIndex, size());
    int length = toIndex - fromIndex;
    switch (length) {
      case 0:
        return of();
      case 1:
        return of(get(fromIndex));
      default:
        return subListUnchecked(fromIndex, toIndex);
    }
  }
{
    long nanos = elapsedNanos();

    TimeUnit unit = chooseUnit(nanos);
    double value = (double) nanos / NANOSECONDS.convert(1, unit);

    // Too bad this functionality is not exposed as a regular method call
    return String.format("%." + significantDigits + "g %s",
        value, abbreviate(unit));
  }
{
    chars = padding().trimTrailingFrom(chars);
    ByteInput decodedInput = decodingStream(asCharInput(chars));
    byte[] tmp = new byte[maxDecodedSize(chars.length())];
    int index = 0;
    try {
      for (int i = decodedInput.read(); i != -1; i = decodedInput.read()) {
        tmp[index++] = (byte) i;
      }
    } catch (IOException badInput) {
      throw new IllegalArgumentException(badInput);
    }
    return extract(tmp, index);
  }
{
    switch (size) {
      case 0:
        return of();
      case 1:
        @SuppressWarnings("unchecked") // all entries will be Entry<K, V>'s
        Entry<K, V> onlyEntry = (Entry<K, V>) entries[0];
        return ImmutableBiMap.of(onlyEntry.getKey(), onlyEntry.getValue());
      default:
        return new RegularImmutableMap<K, V>(size, entries);
    }
  }
{
    entries = createEntryArray(size);
    int tableSize = Hashing.closedTableSize(size, MAX_LOAD_FACTOR);
    table = createEntryArray(tableSize);
    mask = tableSize - 1;
    for (int entryIndex = 0; entryIndex < size; entryIndex++) {
      // each of our 6 callers carefully put only Entry<K, V>s into the array!
      @SuppressWarnings("unchecked")
      Entry<K, V> entry = (Entry<K, V>) theEntries[entryIndex];
      K key = entry.getKey();
      V value = entry.getValue();
      /*
       * TODO(user): figure out a way to avoid the redundant check for
       * ImmutableMap.of(), Builder constructors without introducing race
       * conditions or redundant copies for the copyOf constructor.
       */
      checkEntryNotNull(key, value);
      int keyHashCode = key.hashCode();
      int tableIndex = Hashing.smear(keyHashCode) & mask;
      @Nullable LinkedEntry<K, V> existing = table[tableIndex];
      // prepend, not append, so the entries can be immutable
      LinkedEntry<K, V> linkedEntry = newLinkedEntry(key, value, existing);
      table[tableIndex] = linkedEntry;
      entries[entryIndex] = linkedEntry;
      for (; existing != null; existing = existing.next()) {
        checkNoConflict(!key.equals(existing.getKey()), "key", linkedEntry, existing);
      }
    }
  }
{
      ensureCapacity(size + 1);
      Entry<K, V> entry = entryOf(key, value);
      // don't inline this: we want to fail atomically if key or value is null
      entries[size++] = entry;
      return this;
    }
{
      ensureCapacity(size + 1);
      Entry<K, V> entry = entryOf(key, value);
      // don't inline this: we want to fail atomically if key or value is null
      entries[size++] = entry;
      return this;
    }
{
    return new SetFromMap<E>(map);
  }
{
    if ((map instanceof ImmutableMap) && !(map instanceof ImmutableSortedMap)) {
      // TODO(user): Make ImmutableMap.copyOf(immutableBiMap) call copyOf()
      // on the ImmutableMap delegate(), rather than the bimap itself

      @SuppressWarnings("unchecked") // safe since map is not writable
      ImmutableMap<K, V> kvMap = (ImmutableMap<K, V>) map;
      if (!kvMap.isPartialView()) {
        return kvMap;
      }
    } else if (map instanceof EnumMap) {
      EnumMap<?, ?> enumMap = (EnumMap<?, ?>) map;
      for (Map.Entry<?, ?> entry : enumMap.entrySet()) {
        checkNotNull(entry.getKey());
        checkNotNull(entry.getValue());
      }
      @SuppressWarnings("unchecked")
      // immutable collections are safe for covariant casts
      ImmutableMap<K, V> result = ImmutableEnumMap.asImmutable(new EnumMap(enumMap));
      return result;
    }

    @SuppressWarnings("unchecked") // we won't write to this array
    Entry<K, V>[] entries = map.entrySet().toArray(new Entry[0]);
    switch (entries.length) {
      case 0:
        return of();
      case 1:
        return new SingletonImmutableBiMap<K, V>(entryOf(
            entries[0].getKey(), entries[0].getValue()));
      default:
        for (int i = 0; i < entries.length; i++) {
          K k = entries[i].getKey();
          V v = entries[i].getValue();
          entries[i] = entryOf(k, v);
        }
        return new RegularImmutableMap<K, V>(entries);
    }
  }
{
    boolean sameComparator = false;
    if (map instanceof SortedMap) {
      SortedMap<?, ?> sortedMap = (SortedMap<?, ?>) map;
      Comparator<?> comparator2 = sortedMap.comparator();
      sameComparator = (comparator2 == null)
          ? comparator == NATURAL_ORDER
          : comparator.equals(comparator2);
    }

    if (sameComparator && (map instanceof ImmutableSortedMap)) {
      // TODO(kevinb): Prove that this cast is safe, even though
      // Collections.unmodifiableSortedMap requires the same key type.
      @SuppressWarnings("unchecked")
      ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap<K, V>) map;
      if (!kvMap.isPartialView()) {
        return kvMap;
      }
    }

    // "adding" type params to an array of a raw type should be safe as
    // long as no one can ever cast that same array instance back to a
    // raw type.
    @SuppressWarnings("unchecked")
    Entry<K, V>[] entries = map.entrySet().toArray(new Entry[0]);

    for (int i = 0; i < entries.length; i++) {
      Entry<K, V> entry = entries[i];
      entries[i] = entryOf(entry.getKey(), entry.getValue());
    }

    List<Entry<K, V>> list = Arrays.asList(entries);

    if (!sameComparator) {
      sortEntries(list, comparator);
      validateEntries(list, comparator);
    }

    return fromSortedEntries(comparator, list);
  }
{
    Preconditions.checkNotNull(valueEquivalence);

    Map<K, V> onlyOnLeft = newHashMap();
    Map<K, V> onlyOnRight = new HashMap<K, V>(right); // will whittle it down
    Map<K, V> onBoth = newHashMap();
    Map<K, MapDifference.ValueDifference<V>> differences = newHashMap();
    boolean eq = true;

    for (Entry<? extends K, ? extends V> entry : left.entrySet()) {
      K leftKey = entry.getKey();
      V leftValue = entry.getValue();
      if (right.containsKey(leftKey)) {
        V rightValue = onlyOnRight.remove(leftKey);
        if (valueEquivalence.equivalent(leftValue, rightValue)) {
          onBoth.put(leftKey, leftValue);
        } else {
          eq = false;
          differences.put(
              leftKey, ValueDifferenceImpl.create(leftValue, rightValue));
        }
      } else {
        eq = false;
        onlyOnLeft.put(leftKey, leftValue);
      }
    }

    boolean areEqual = eq && onlyOnRight.isEmpty();
    return mapDifference(
        areEqual, onlyOnLeft, onlyOnRight, onBoth, differences);
  }
{
    switch (elements.length) {
      case 0:
        return of();
      case 1:
        @SuppressWarnings("unchecked") // collection had only Es in it
        ImmutableList<E> list = new SingletonImmutableList<E>((E) elements[0]);
        return list;
      default:
        return construct(elements);
    }
  }
{
    if (n == 0) {
      return 0;
    }
    for (int i = 0; i < n; i++) {
      ObjectArrays.checkElementNotNull(contents[i], i);
    }
    Arrays.sort(contents, 0, n, comparator);
    int uniques = 1;
    for (int i = 1; i < n; i++) {
      E cur = contents[i];
      E prev = contents[uniques - 1];
      if (comparator.compare(cur, prev) != 0) {
        contents[uniques++] = cur;
      }
    }
    Arrays.fill(contents, uniques, n, null);
    return uniques;
  }
{
    switch (elements.length) {
      case 0:
        return of();
      case 1:
        @SuppressWarnings("unchecked") // collection had only Es in it
        ImmutableList<E> list = new SingletonImmutableList<E>((E) elements[0]);
        return list;
      default:
        for (int i = 0; i < elements.length; i++) {
          ObjectArrays.checkElementNotNull(elements[i], i);
        }
        return new RegularImmutableList<E>(elements);
    }
  }
{
    for (int i = 0; i < elements.length; i++) {
      ObjectArrays.checkElementNotNull(elements[i], i);
    }
    return new RegularImmutableList<E>(elements);
  }
{
      return TreeRangeSet.this;
    }
{
    int size = chars.length;
    boolean containsZero = chars[0] == 0;
    boolean reprobe = false;

    // Compute the filter.
    long filter = 0;
    for (char c : chars) {
      filter |= 1L << c;
    }
    char[] table = null;
    for (int i = size; table == null && i < MAX_TABLE_SIZE; i++) {
      table = buildTable(i, chars, false);
    }
    // Compute the hash table.
    if (table == null) {
      table = buildTable(MAX_TABLE_SIZE, chars, true);
      reprobe = true;
    }
    return new SmallCharMatcher(table, filter, containsZero, reprobe, description);
  }
{
    checkNotNull(processor);

    byte[] buf = new byte[BUF_SIZE];
    Closer closer = Closer.create();
    try {
      InputStream in = closer.add(supplier.getInput());
      int read;
      do {
        read = in.read(buf);
      } while (read != -1 && processor.processBytes(buf, 0, read));
      return processor.getResult();
    } catch (Throwable e) {
      throw closer.rethrow(e, IOException.class);
    } finally {
      closer.close();
    }
  }
{
    int length = string.length();
    StringBuilder builder = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      builder.append(toLowerCase(string.charAt(i)));
    }
    return builder.toString();
  }
{
    int length = string.length();
    StringBuilder builder = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      builder.append(toUpperCase(string.charAt(i)));
    }
    return builder.toString();
  }
{
    int first = indexIn(sequence);
    if (first == -1) {
      return sequence.toString();
    }

    // TODO(kevinb): see if this implementation can be made faster
    StringBuilder builder = new StringBuilder(sequence.length())
        .append(sequence.subSequence(0, first))
        .append(replacement);
    boolean in = true;
    for (int i = first + 1; i < sequence.length(); i++) {
      char c = sequence.charAt(i);
      if (matches(c)) {
        if (!in) {
          builder.append(replacement);
          in = true;
        }
      } else {
        builder.append(c);
        in = false;
      }
    }
    return builder.toString();
  }
{
      checkNotNull(name);
      addHolder(value).builder.append(name).append('=').append(value);
      return this;
    }
{
    return desiredUnit.convert(elapsedNanos(), NANOSECONDS);
  }
{
    checkNotNull(b);
    return new InputSupplier<ByteArrayInputStream>() {
      @Override
      public ByteArrayInputStream getInput() {
        return new ByteArrayInputStream(b, off, len);
      }
    };
  }
{
    checkNotNull(from);
    checkNotNull(to);
    Closer closer = Closer.create();
    try {
      OutputStream out = closer.add(to.getOutput());
      out.write(from);
    } catch (Throwable e) {
      throw closer.rethrow(e, IOException.class);
    } finally {
      closer.close();
    }
  }
{
    checkNotNull(value);
    return new InputSupplier<StringReader>() {
      @Override
      public StringReader getInput() {
        return new StringReader(value);
      }
    };
  }
{
    checkNotNull(out);
    checkNotNull(charset);
    return new OutputSupplier<OutputStreamWriter>() {
      @Override
      public OutputStreamWriter getOutput() throws IOException {
        return new OutputStreamWriter(out.getOutput(), charset);
      }
    };
  }
{
    checkNotNull(domain);
    checkNotNull(range);
    if (isEmpty()) {
      return ImmutableSortedSet.of();
    }
    Range<C> span = span();
    if (!range.isConnected(span)) {
      return ImmutableSortedSet.of();
    }

    range = range.intersection(span).canonical(domain);

    if (!range.hasLowerBound()) {
      // according to the spec of canonical, neither this ImmutableRangeSet nor
      // the range have a lower bound
      throw new IllegalArgumentException(
          "Neither the DiscreteDomain nor this range set are bounded below");
    } else if (!range.hasUpperBound()) {
      try {
        domain.maxValue();
      } catch (NoSuchElementException e) {
        throw new IllegalArgumentException(
            "Neither the DiscreteDomain nor this range set are bounded above");
      }
    }

    ImmutableList<Range<C>> subRanges = intersectRanges(range);
    switch (subRanges.size()) {
      case 0:
        return ImmutableSortedSet.of();
      case 1:
        return subRanges.get(0).asSet(domain);
      default:
        return new AsSet(domain, range, subRanges);
    }
  }
{
    return totalSize;
  }
{
    return asInt();
  }
{
    return new UnsignedInteger(value);
  }
{
    Closer closer = Closer.create();
    try {
      R r = closer.add(supplier.getInput());
      LineReader lineReader = new LineReader(r);
      String line;
      while ((line = lineReader.readLine()) != null) {
        if (!callback.processLine(line)) {
          break;
        }
      }
      return callback.getResult();
    } catch (Throwable e) {
      throw closer.rethrow(e, IOException.class);
    } finally {
      closer.close();
    }
  }
{
    return new UnsignedLong(value);
  }
{
    /*
     * As long as the hash function that produced this isn't of horrible quality, this
     * won't be of horrible quality either.
     */
    return asInt();
  }
{
    Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntry =
        entriesByLowerBound.floorEntry(Cut.belowValue(key));
    if (mapEntry != null && mapEntry.getValue().contains(key)) {
      return mapEntry.getValue().getValue();
    } else {
      return null;
    }
  }
{
    checkNotNull(comparator);
    boolean hasSameComparator =
        SortedIterables.hasSameComparator(comparator, elements);

    if (hasSameComparator && (elements instanceof ImmutableSortedSet)) {
      @SuppressWarnings("unchecked")
      ImmutableSortedSet<E> original = (ImmutableSortedSet<E>) elements;
      if (!original.isPartialView()) {
        return original;
      }
    }
    @SuppressWarnings("unchecked") // elements only contains E's; it's safe.
    E[] array = (E[]) Iterables.toArray(elements);
    return construct(comparator, array.length, array);
  }
{
    Comparator<? super E> comparator = SortedIterables.comparator(sortedSet);
    E[] elements = (E[]) sortedSet.toArray();
    if (elements.length == 0) {
      return emptySet(comparator);
    } else {
      return new RegularImmutableSortedSet<E>(
          ImmutableList.<E>asImmutableList(elements), comparator);
    }
  }
{
    List<Object> samples = sampleInstances.get(type);
    @SuppressWarnings("unchecked") // sampleInstances is always registered by type.
    T sample = (T) nextInstance(samples, null);
    if (sample != null) {
      return sample;
    }
    for (Method method : FreshValueGenerator.class.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Generates.class)) {
        if (Primitives.wrap(type).isAssignableFrom(Primitives.wrap(method.getReturnType()))) {
          try {
            @SuppressWarnings("unchecked") // protected by isAssignableFrom
            T result = (T) method.invoke(this);
            return result;
          } catch (InvocationTargetException e) {
            Throwables.propagate(e.getCause());
          } catch (Exception e) {
            throw Throwables.propagate(e);
          }
        }
      }
    }
    if (type.isInterface()) {
      // always create a new proxy
      return newProxy(type);
    }
    if (type.isEnum()) {
      return nextInstance(type.getEnumConstants(), null);
    }
    return ArbitraryInstances.get(type);
  }
{
    FreshValueGenerator generator = new FreshValueGenerator();
    T value1 = generator.generate(type);
    T value2 = generator.generate(type);
    assertNotNull("Null returned for " + type, value1);
    assertFalse("Equal instance " + value1 + " returned for " + type, value1.equals(value2));
  }
{
    checkArgument(k >= 0, "%d is negative", k);

    // values is not an E[], but we use it as such for readability. Hack.
    @SuppressWarnings("unchecked")
    E[] values = (E[]) Iterables.toArray(iterable);

    // TODO(nshupe): also sort whole list if k is *near* values.length?
    // TODO(kevinb): benchmark this impl against hand-coded heap
    E[] resultArray;
    if (values.length <= k) {
      Arrays.sort(values, this);
      resultArray = values;
    } else {
      quicksortLeastK(values, 0, values.length - 1, k);

      // this is not an E[], but we use it as such for readability. Hack.
      @SuppressWarnings("unchecked")
      E[] tmp = (E[]) new Object[k];
      resultArray = tmp;
      System.arraycopy(values, 0, resultArray, 0, k);
    }

    // We can't use ImmutableList since we want to support null elements.
    return Collections.unmodifiableList(Arrays.asList(resultArray));
  }
{
    Object instance = instantiate(cls, TestErrorReporter.FOR_SERIALIZABLE_TEST);
    if (instance != null) {
      if (isEqualsDefined(cls)) {
        SerializableTester.reserializeAndAssert(instance);
      } else {
        SerializableTester.reserialize(instance);
      }
    }
  }
{
    // TODO: when we use @BeforeClass, we can pay the cost of class path scanning only once.
    for (Class<?> classToTest
        : findClassesToTest(loadPublicClassesInPackage(), SERIALIZABLE_TEST_METHOD_NAMES)) {
      if (Serializable.class.isAssignableFrom(classToTest)) {
        testSerializable(classToTest);
      }
    }
  }
{
    final FakeTicker ticker = new FakeTicker();

    int numberOfThreads = 64;
    ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
    final CountDownLatch startLatch = new CountDownLatch(numberOfThreads);
    final CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);
    for (int i = numberOfThreads; i > 0; i--) {
      executorService.submit(new Callable<Void>() {
        @Override
        public Void call() throws Exception {
          // adds two nanoseconds to the ticker
          startLatch.countDown();
          startLatch.await();
          ticker.advance(1L);
          Thread.sleep(10);
          ticker.advance(1L);
          doneLatch.countDown();
          return null;
        }
      });
    }
    doneLatch.await();
    assertEquals(numberOfThreads * 2, ticker.read());
  }
{
    checkArgument(interfaceType.isInterface(), "%s isn't an interface", interfaceType);
    Method[] methods = interfaceType.getMethods();
    // The interface could be package-private or private.
    AccessibleObject.setAccessible(methods, true);
    for (Method method : methods) {
      testSuccessfulForwarding(interfaceType, method, wrapperFunction);
      testExceptionPropagation(interfaceType, method, wrapperFunction);
    }
    if (testsEquals) {
      testEquals(interfaceType, wrapperFunction);
    }
    testToString(interfaceType, wrapperFunction);
  }
{
    return ImmutableList.copyOf(iterable);
  }
{
    return Ordering.from(comparator).immutableSortedCopy(iterable);
  }
{
    return ImmutableSet.copyOf(iterable);
  }
{
    return ImmutableSortedSet.copyOf(comparator, iterable);
  }
{
    checkNotNull(valueFunction);
    // Using LHM instead of a builder so as not to fail on duplicate keys
    Map<K, V> builder = newLinkedHashMap();
    for (K key : keys) {
      builder.put(key, valueFunction.apply(key));
    }
    return ImmutableMap.copyOf(builder);
  }
{
    return Math.pow((double) bits.bitCount() / bits.size(), numHashFunctions);
  }
{
    checkState(keyStrength == null, "Key strength was already set to %s", keyStrength);
    keyStrength = checkNotNull(strength);
    if (strength != Strength.STRONG) {
      // STRONG could be used during deserialization.
      useCustomMap = true;
    }
    return this;
  }
{
    checkState(valueStrength == null, "Value strength was already set to %s", valueStrength);
    valueStrength = checkNotNull(strength);
    if (strength != Strength.STRONG) {
      // STRONG could be used during deserialization.
      useCustomMap = true;
    }
    return this;
  }
{
      k1 *= C1;
      k1 = Long.rotateLeft(k1, 31);
      k1 *= C2;
      h1 ^= k1;

      h1 = Long.rotateLeft(h1, 27);
      h1 += h2;
      h1 = h1 * 5 + 0x52dce729;

      k2 *= C2;
      k2 = Long.rotateLeft(k2, 33);
      k2 *= C1;
      h2 ^= k2;

      h2 = Long.rotateLeft(h2, 31);
      h2 += h1;
      h2 = h2 * 5 + 0x38495ab5;
    }
{
      k1 *= C1;
      k1 = Long.rotateLeft(k1, 31);
      k1 *= C2;
      h1 ^= k1;

      h1 = Long.rotateLeft(h1, 27);
      h1 += h2;
      h1 = h1 * 5 + 0x52dce729;

      k2 *= C2;
      k2 = Long.rotateLeft(k2, 33);
      k2 *= C1;
      h2 ^= k2;

      h2 = Long.rotateLeft(h2, 31);
      h2 += h1;
      h2 = h2 * 5 + 0x38495ab5;
    }
{
    checkNotNull(function);
    EntryTransformer<K, V1, V2> transformer =
        new EntryTransformer<K, V1, V2>() {
          @Override
          public V2 transformEntry(K key, V1 value) {
            return function.apply(value);
          }
        };
    return transformEntries(fromMap, transformer);
  }
{
    lock.lock();
    try {
      if (state == State.NEW) {
        state = State.STARTING;
        doStart();
      }
    } catch (Throwable startupFailure) {
      // put the exception in the future, the user can get it via Future.get()
      notifyFailed(startupFailure);
    } finally {
      lock.unlock();
    }

    return startup;
  }
{
    lock.lock();
    try {
      if (state != State.STARTING) {
        IllegalStateException failure = new IllegalStateException(
            "Cannot notifyStarted() when the service is " + state);
        notifyFailed(failure);
        throw failure;
      }

      state = State.RUNNING;
      if (shutdownWhenStartupFinishes) {
        stop();
      } else {
        startup.set(State.RUNNING);
      }
    } finally {
      lock.unlock();
    }
  }
{
    lock.lock();
    try {
      if (state != State.STOPPING && state != State.RUNNING) {
        IllegalStateException failure = new IllegalStateException(
            "Cannot notifyStopped() when the service is " + state);
        notifyFailed(failure);
        throw failure;
      }

      state = State.TERMINATED;
      shutdown.set(State.TERMINATED);
    } finally {
      lock.unlock();
    }
  }
{
    checkNotNull(cause);

    lock.lock();
    try {
      if (state == State.STARTING) {
        startup.setException(cause);
        shutdown.setException(new Exception(
            "Service failed to start.", cause));
      } else if (state == State.STOPPING) {
        shutdown.setException(cause);
      } else if (state == State.RUNNING) {
        shutdown.setException(
            new Exception("Service failed while running", cause));
      } else if (state == State.NEW || state == State.TERMINATED) {
        throw new IllegalStateException(
            "Failed while in state:" + state, cause);
      }
      state = State.FAILED;
    } finally {
      lock.unlock();
    }
  }
{
    checkNotNull(iterator);
    checkArgument(numberToSkip >= 0, "number to skip cannot be negative");

    int i;
    for (i = 0; i < numberToSkip && iterator.hasNext(); i++) {
      iterator.next();
    }
    return i;
  }
{
    assertEquals(expected, murmur3_32().hashInt(input).asInt());
    assertEquals(expected, murmur3_32().newHasher().putInt(input).hash().asInt());
  }
{
    assertEquals(expected, murmur3_32().hashString(input).asInt());
    assertEquals(expected, murmur3_32().newHasher().putString(input).hash().asInt());
  }
{
    if (type instanceof Class) {
      return (Class<?>) type;
    } else if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      // JDK implementation declares getRawType() to return Class<?>
      return (Class<?>) parameterizedType.getRawType();
    } else if (type instanceof GenericArrayType) {
      GenericArrayType genericArrayType = (GenericArrayType) type;
      return Types.getArrayClass(getRawType(genericArrayType.getGenericComponentType()));
    } else if (type instanceof TypeVariable) {
      // First bound is always the "primary" bound that determines the runtime signature.
      return getRawType(((TypeVariable<?>) type).getBounds()[0]);
    } else if (type instanceof WildcardType) {
      // Wildcard can have one and only one upper bound.
      return getRawType(((WildcardType) type).getUpperBounds()[0]);
    } else {
      throw new AssertionError(type + " unsupported");
    }
  }
{
    boolean hasSameComparator =
        SortedIterables.hasSameComparator(comparator, elements);

    if (hasSameComparator && (elements instanceof ImmutableSortedSet)) {
      @SuppressWarnings("unchecked")
      ImmutableSortedSet<E> original = (ImmutableSortedSet<E>) elements;
      if (!original.isPartialView()) {
        return original;
      }
    }
    ImmutableList<E> list = ImmutableList.copyOf(
        SortedIterables.sortedUnique(comparator, elements));
    return list.isEmpty()
        ? ImmutableSortedSet.<E>emptySet(comparator)
        : new RegularImmutableSortedSet<E>(list, comparator);
  }
{
    return comparator;
  }
{
    ASSERT.that(set).isEmpty();
  }
{
    ASSERT.that(set).isEmpty();
  }
{
    ASSERT.that(set).isEmpty();
  }
{
    ASSERT.that(set).isEmpty();
  }
{
    Object[] elements = collection.toArray();
    switch (elements.length) {
      case 0:
        return of();
      case 1:
        @SuppressWarnings("unchecked") // collection had only Es in it
        E onlyElement = (E) elements[0];
        return of(onlyElement);
      default:
        // safe to use the array without copying it
        // as specified by Collection.toArray().
        return construct(elements);
    }
  }
{
    if (newFromIndex < newToIndex) {
      return new ImmutableSortedMap<K, V>(
          entries.subList(newFromIndex, newToIndex), comparator);
    } else {
      return emptyMap(comparator);
    }
  }
{
    return (rowIndex == null || columnIndex == null)
        ? null : array[rowIndex][columnIndex];
  }
{
    this.rowList = ImmutableList.copyOf(rowKeys);
    this.columnList = ImmutableList.copyOf(columnKeys);
    checkArgument(!rowList.isEmpty());
    checkArgument(!columnList.isEmpty());

    /*
     * TODO(jlevy): Support empty rowKeys or columnKeys? If we do, when
     * columnKeys is empty but rowKeys isn't, the table is empty but
     * containsRow() can return true and rowKeySet() isn't empty.
     */
    ImmutableMap.Builder<R, Integer> rowBuilder = ImmutableMap.builder();
    for (int i = 0; i < rowList.size(); i++) {
      rowBuilder.put(rowList.get(i), i);
    }
    rowKeyToIndex = rowBuilder.build();

    ImmutableMap.Builder<C, Integer> columnBuilder = ImmutableMap.builder();
    for (int i = 0; i < columnList.size(); i++) {
      columnBuilder.put(columnList.get(i), i);
    }
    columnKeyToIndex = columnBuilder.build();

    @SuppressWarnings("unchecked")
    V[][] tmpArray
        = (V[][]) new Object[rowList.size()][columnList.size()];
    array = tmpArray;
  }
{
    @SuppressWarnings("unchecked")
    E cast = (E) o;
    // Make sure the object is accepted by the comparator (e.g., the right type, possibly non-null).
    comparator.compare(cast, cast);
    return cast;
  }
{
    checkPositionIndexes(fromIndex, toIndex, size());
    int length = toIndex - fromIndex;
    switch (length) {
      case 0:
        return of();
      case 1:
        return of(get(fromIndex));
      default:
        return subListUnchecked(fromIndex, toIndex);
    }
  }
{
      checkState(hasMore());
      int startPosition = position;
      position = matcher.negate().indexIn(input, startPosition);
      checkState(position != startPosition);
      return hasMore() ? input.substring(startPosition, position) : input.substring(startPosition);
    }
{
    checkNotNull(fromElement);
    checkNotNull(toElement);
    checkArgument(comparator().compare(fromElement, toElement) <= 0);
    return subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
  }
{
      final Iterator<? extends Entry<?, V>> entryIterator
          = multimap.entries().iterator();
      return new UnmodifiableIterator<V>() {
        @Override
        public boolean hasNext() {
          return entryIterator.hasNext();
        }
        @Override
        public V next() {
          return entryIterator.next().getValue();
        }
      };
    }
{
    checkArgument(length >= 0);
    int end = offset + length;

    // Technically we should give a slightly more descriptive error on overflow
    Preconditions.checkPositionIndexes(offset, end, array.length);

    /*
     * We can't use call the two-arg constructor with arguments (offset, end)
     * because the returned Iterator is a ListIterator that may be moved back
     * past the beginning of the iteration.
     */
    return new AbstractIndexedListIterator<T>(length) {
      @Override protected T get(int index) {
        return array[offset + index];
      }
    };
  }
{
    for (Constructor<?> constructor : c.getDeclaredConstructors()) {
      if (isPublic(constructor) && !isIgnored(constructor)) {
        testConstructor(constructor);
      }
    }
  }
{
    for (Method method : c.getDeclaredMethods()) {
      if (isPublic(method) && isStatic(method) && !isIgnored(method)) {
        testMethod(null, method);
      }
    }
  }
{
    Class<?> c = instance.getClass();
    for (Method method : c.getDeclaredMethods()) {
      if (isPublic(method) && !isStatic(method) && !isIgnored(method)) {
        testMethod(instance, method);
      }
    }
  }
{
    byte[] addr = ipStringToBytes(ipString);

    // The argument was malformed, i.e. not an IP string literal.
    if (addr == null) {
      throw new IllegalArgumentException(
          String.format("'%s' is not an IP string literal.", ipString));
    }

    try {
      return InetAddress.getByAddress(addr);
    } catch (UnknownHostException e) {

      /*
       * This really shouldn't happen in practice since all our byte
       * sequences should be valid IP addresses.
       *
       * However {@link InetAddress#getByAddress} is documented as
       * potentially throwing this "if IP address is of illegal length".
       *
       * This is mapped to IllegalArgumentException since, presumably,
       * the argument triggered some processing bug in either
       * {@link IPAddressUtil#textToNumericFormatV4} or
       * {@link IPAddressUtil#textToNumericFormatV6}.
       */
      throw new IllegalArgumentException(
          String.format("'%s' is extremely broken.", ipString), e);
    }
  }
{
    checkNotNull(appendable);
    Iterator<?> iterator = parts.iterator();
    if (iterator.hasNext()) {
      appendable.append(toString(iterator.next()));
      while (iterator.hasNext()) {
        appendable.append(separator);
        appendable.append(toString(iterator.next()));
      }
    }
    return appendable;
  }
{
    try {
      appendTo((Appendable) builder, parts);
    } catch (IOException impossible) {
      throw new AssertionError(impossible);
    }
    return builder;
  }
{
    return appendTo(new StringBuilder(), parts).toString();
  }
{
      checkNotNull(appendable);
      Iterator<? extends Map.Entry<?, ?>> iterator = entries.iterator();
      if (iterator.hasNext()) {
        Entry<?, ?> entry = iterator.next();
        appendable.append(joiner.toString(entry.getKey()));
        appendable.append(keyValueSeparator);
        appendable.append(joiner.toString(entry.getValue()));
        while (iterator.hasNext()) {
          appendable.append(joiner.separator);
          Entry<?, ?> e = iterator.next();
          appendable.append(joiner.toString(e.getKey()));
          appendable.append(keyValueSeparator);
          appendable.append(joiner.toString(e.getValue()));
        }
      }
      return appendable;
    }
{
      try {
        appendTo((Appendable) builder, entries);
      } catch (IOException impossible) {
        throw new AssertionError(impossible);
      }
      return builder;
    }
{
      return appendTo(new StringBuilder(), entries).toString();
    }
{
    Random random = new Random(42);
    Ordering<Integer> ordering = Ordering.natural();

    for (int i = 0; i < 1000; i++) {
      List<Integer> list = Lists.newArrayList();
      for (int j = 0; j < 300; j++) {
        list.add(random.nextInt(10 * i + j + 1));
      }

      for (int seed = 1; seed < 20; seed++) {
        int k = random.nextInt(10 * seed);
        assertEquals(ordering.sortedCopy(list).subList(0, k),
            ordering.leastOf(list, k));
      }
    }
  }
{
    Iterator<E> iterator = iterable.iterator();

    // let this throw NoSuchElementException as necessary
    E maxSoFar = iterator.next();

    while (iterator.hasNext()) {
      maxSoFar = max(maxSoFar, iterator.next());
    }

    return maxSoFar;
  }
{
    Iterator<E> iterator = iterable.iterator();

    // let this throw NoSuchElementException as necessary
    E minSoFar = iterator.next();

    while (iterator.hasNext()) {
      minSoFar = min(minSoFar, iterator.next());
    }

    return minSoFar;
  }
{
      try {
        if (count != 0) { // read-volatile
          // don't call getLiveEntry, which would ignore loading values
          ReferenceEntry<K, V> e = getEntry(key, hash);
          if (e != null) {
            long now = map.ticker.read();
            V value = getLiveValue(e, now);
            if (value != null) {
              recordRead(e, now);
              statsCounter.recordHits(1);
              return value;
            }
            ValueReference<K, V> valueReference = e.getValueReference();
            if (valueReference.isLoading()) {
              return waitForLoadingValue(e, valueReference);
            }
          }
        }

        // at this point e is either null or expired;
        return lockedGetOrLoad(key, hash, loader);
      } finally {
        postReadCleanup();
      }
    }
{
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            AssertionFailedError afe =
                new AssertionFailedError("Unexpected InterruptedException");
            afe.initCause(ie);
            throw afe;
        }
    }
{
    if (strictParsing) {
      if (weigher == null) {
        checkState(maximumWeight == UNSET_INT, "maximumWeight requires weigher");
      } else {
        checkState(maximumWeight != UNSET_INT, "weigher requires maximumWeight");
      }
    } else {
      if (weigher == null) {
        if (maximumWeight != UNSET_INT) {
          logger.log(Level.WARNING,
              "ignoring CacheBuilder.maximumWeight specified without CacheBuilder.weigher");
        }
      } else {
        if (maximumWeight == UNSET_INT) {
          logger.log(Level.WARNING,
              "ignoring CacheBuilder.weigher specified without CacheBuilder.maximumWeight");
        }
      }
    }

    return new LocalCache.AutoLocalCache<K1, V1>(this, loader);
  }
{
    checkState(expireAfterWriteNanos == UNSET_INT, "expireAfterWrite was already set to %s ns",
        expireAfterWriteNanos);
    checkState(expireAfterAccessNanos == UNSET_INT, "expireAfterAccess was already set to %s ns",
        expireAfterAccessNanos);
    checkArgument(duration >= 0, "duration cannot be negative: %s %s", duration, unit);
  }
{
    checkState(expireAfterWriteNanos == UNSET_INT, "expireAfterWrite was already set to %s ns",
        expireAfterWriteNanos);
    checkState(expireAfterAccessNanos == UNSET_INT, "expireAfterAccess was already set to %s ns",
        expireAfterAccessNanos);
    checkArgument(duration >= 0, "duration cannot be negative: %s %s", duration, unit);
  }
{
    CustomConcurrentHashMap<?, ?> cchm = toCustomConcurrentHashMap(cache);

    int size = 0;
    for (Segment<?, ?> segment : cchm.segments) {
      size += expirationQueueSize(segment);
    }
    return size;
  }
{
    int hash = hash(checkNotNull(key));
    return segmentFor(hash).getOrLoad(key, hash, loader);
  }
{
      try {
        outer: while (true) {
          // don't call getLiveEntry, which would ignore loading values
          ReferenceEntry<K, V> e = null;
          if (count != 0) { // read-volatile
            e = getEntry(key, hash);
            if (e != null) {
              V value = getLiveValue(e);
              if (value != null) {
                recordRead(e);
                statsCounter.recordHit();
                return value;
              }
            }
          }

          // at this point e is either null, loading, or expired;
          // avoid locking if it's already loading
          if (e == null || !e.getValueReference().isLoading()) {
            boolean createNewEntry = true;
            LoadingValueReference<K, V> loadingValueReference = null;
            lock();
            try {
              preWriteCleanup();

              int newCount = this.count - 1;
              AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
              int index = hash & (table.length() - 1);
              ReferenceEntry<K, V> first = table.get(index);

              for (e = first; e != null; e = e.getNext()) {
                K entryKey = e.getKey();
                if (e.getHash() == hash && entryKey != null
                    && map.keyEquivalence.equivalent(key, entryKey)) {
                  ValueReference<K, V> valueReference = e.getValueReference();
                  if (valueReference.isLoading()) {
                    createNewEntry = false;
                  } else {
                    V value = valueReference.get();
                    if (value == null) {
                      enqueueNotification(entryKey, hash, valueReference, RemovalCause.COLLECTED);
                    } else if (map.expires() && map.isExpired(e)) {
                      // This is a duplicate check, as preWriteCleanup already purged expired
                      // entries, but let's accomodate an incorrect expiration queue.
                      enqueueNotification(entryKey, hash, valueReference, RemovalCause.EXPIRED);
                    } else {
                      recordLockedRead(e);
                      statsCounter.recordHit();
                      return value;
                    }

                    // immediately reuse invalid entries
                    evictionQueue.remove(e);
                    expirationQueue.remove(e);
                    this.count = newCount; // write-volatile
                  }
                  break;
                }
              }

              if (createNewEntry) {
                loadingValueReference = new LoadingValueReference<K, V>();

                if (e == null) {
                  e = newEntry(key, hash, first);
                  e.setValueReference(loadingValueReference);
                  table.set(index, e);
                } else {
                  e.setValueReference(loadingValueReference);
                }
              }
            } finally {
              unlock();
              postWriteCleanup();
            }

            if (createNewEntry) {
              try {
                return load(key, hash, e, loadingValueReference, loader);
              } finally {
                statsCounter.recordMiss();
              }
            }
          }

          // The entry already exists. Wait for loading.
          checkState(!Thread.holdsLock(e), "Recursive load");
          // don't consider expiration as we're concurrent with loading
          try {
            V value = e.getValueReference().waitForValue();
            recordRead(e);
            return value;
          } finally {
            statsCounter.recordMiss();
          }
        }
      } finally {
        postReadCleanup();
      }
    }
{
      try {
        outer: while (true) {
          // don't call getLiveEntry, which would ignore loading values
          ReferenceEntry<K, V> e = null;
          if (count != 0) { // read-volatile
            e = getEntry(key, hash);
            if (e != null) {
              V value = getLiveValue(e);
              if (value != null) {
                recordRead(e);
                statsCounter.recordHit();
                return value;
              }
            }
          }

          // at this point e is either null, loading, or expired;
          // avoid locking if it's already loading
          if (e == null || !e.getValueReference().isLoading()) {
            boolean createNewEntry = true;
            LoadingValueReference<K, V> loadingValueReference = null;
            lock();
            try {
              preWriteCleanup();

              int newCount = this.count - 1;
              AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
              int index = hash & (table.length() - 1);
              ReferenceEntry<K, V> first = table.get(index);

              for (e = first; e != null; e = e.getNext()) {
                K entryKey = e.getKey();
                if (e.getHash() == hash && entryKey != null
                    && map.keyEquivalence.equivalent(key, entryKey)) {
                  ValueReference<K, V> valueReference = e.getValueReference();
                  if (valueReference.isLoading()) {
                    createNewEntry = false;
                  } else {
                    V value = valueReference.get();
                    if (value == null) {
                      enqueueNotification(entryKey, hash, valueReference, RemovalCause.COLLECTED);
                    } else if (map.expires() && map.isExpired(e)) {
                      // This is a duplicate check, as preWriteCleanup already purged expired
                      // entries, but let's accomodate an incorrect expiration queue.
                      enqueueNotification(entryKey, hash, valueReference, RemovalCause.EXPIRED);
                    } else {
                      recordLockedRead(e);
                      statsCounter.recordHit();
                      return value;
                    }

                    // immediately reuse invalid entries
                    evictionQueue.remove(e);
                    expirationQueue.remove(e);
                    this.count = newCount; // write-volatile
                  }
                  break;
                }
              }

              if (createNewEntry) {
                loadingValueReference = new LoadingValueReference<K, V>();

                if (e == null) {
                  e = newEntry(key, hash, first);
                  e.setValueReference(loadingValueReference);
                  table.set(index, e);
                } else {
                  e.setValueReference(loadingValueReference);
                }
              }
            } finally {
              unlock();
              postWriteCleanup();
            }

            if (createNewEntry) {
              try {
                return load(key, hash, e, loadingValueReference, loader);
              } finally {
                statsCounter.recordMiss();
              }
            }
          }

          // The entry already exists. Wait for loading.
          checkState(!Thread.holdsLock(e), "Recursive load");
          // don't consider expiration as we're concurrent with loading
          try {
            V value = e.getValueReference().waitForValue();
            recordRead(e);
            return value;
          } finally {
            statsCounter.recordMiss();
          }
        }
      } finally {
        postReadCleanup();
      }
    }
{
      checkNotNull(name);
      maybeAppendSeparator().append(name).append('=').append(value);
      return this;
    }
{
      V value = null;
      long start = System.nanoTime();
      try {
        // Synchronizes on the entry to allow failing fast when a recursive computation is
        // detected. This is not fool-proof since the entry may be copied when the segment
        // is written to.
        synchronized (e) {
          value = loadingValueReference.load(key, hash);
        }
        long end = System.nanoTime();
        statsCounter.recordLoadSuccess(end - start);

        // putIfAbsent
        V oldValue = put(key, hash, value, true);
        if (oldValue != null) {
          // the loaded value was already clobbered
          // create 0-weight value reference for removal notification
          ValueReference<K, V> valueReference =
              map.valueStrength.referenceValue(this, e, value, 0);
          enqueueNotification(key, hash, valueReference, RemovalCause.REPLACED);
        }
        return value;
      } finally {
        if (value == null) {
          long end = System.nanoTime();
          statsCounter.recordLoadException(end - start);
          clearValue(key, hash, loadingValueReference);
        }
      }
    }
{
      lock();
      try {
        preWriteCleanup();

        AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
        int index = hash & (table.length() - 1);
        ReferenceEntry<K, V> first = table.get(index);

        for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
          K entryKey = e.getKey();
          if (e.getHash() == hash && entryKey != null
              && map.keyEquivalence.equivalent(key, entryKey)) {
            // If the value disappeared, this entry is partially collected,
            // and we should pretend like it doesn't exist.
            ValueReference<K, V> valueReference = e.getValueReference();
            V entryValue = valueReference.get();
            if (entryValue == null) {
              if (isCollected(valueReference)) {
                int newCount = this.count - 1;
                ++modCount;
                enqueueNotification(entryKey, hash, valueReference, RemovalCause.COLLECTED);
                ReferenceEntry<K, V> newFirst = removeFromChain(first, e);
                newCount = this.count - 1;
                table.set(index, newFirst);
                this.count = newCount; // write-volatile
              }
              return false;
            }

            if (map.valueEquivalence.equivalent(oldValue, entryValue)) {
              ++modCount;
              enqueueNotification(key, hash, valueReference, RemovalCause.REPLACED);
              setValue(e, key, newValue);
              evictEntries();
              return true;
            } else {
              // Mimic
              // "if (map.containsKey(key) && map.get(key).equals(oldValue))..."
              recordLockedRead(e);
              return false;
            }
          }
        }

        return false;
      } finally {
        unlock();
        postWriteCleanup();
      }
    }
{
    checkState(keyStrength == null, "Key strength was already set to %s", keyStrength);
    keyStrength = checkNotNull(strength);
    if (strength != Strength.STRONG) {
      // STRONG could be used during deserialization.
      useCustomMap = true;
    }
    return this;
  }
{
    checkState(valueStrength == null, "Value strength was already set to %s", valueStrength);
    valueStrength = checkNotNull(strength);
    if (strength != Strength.STRONG) {
      // STRONG could be used during deserialization.
      useCustomMap = true;
    }
    return this;
  }
{
    executionList.execute();
  }
{
    executionList.execute();
  }
{
    executionList.execute();
  }
{
    Segment<K, V>[] segments = this.segments;
    long sum = 0;
    for (int i = 0; i < segments.length; ++i) {
      sum += segments[i].count;
    }
    return Ints.saturatedCast(sum);
  }
{
    Comparator<? super K> comparator = orNaturalOrder(left.comparator());
    SortedMap<K, V> onlyOnLeft = Maps.newTreeMap(comparator);
    SortedMap<K, V> onlyOnRight = Maps.newTreeMap(comparator);
    onlyOnRight.putAll(right); // will whittle it down
    SortedMap<K, V> onBoth = Maps.newTreeMap(comparator);
    SortedMap<K, MapDifference.ValueDifference<V>> differences =
        Maps.newTreeMap(comparator);
    boolean eq = true;

    for (Entry<? extends K, ? extends V> entry : left.entrySet()) {
      K leftKey = entry.getKey();
      V leftValue = entry.getValue();
      if (right.containsKey(leftKey)) {
        V rightValue = onlyOnRight.remove(leftKey);
        if (Objects.equal(leftValue, rightValue)) {
          onBoth.put(leftKey, leftValue);
        } else {
          eq = false;
          differences.put(
              leftKey, ValueDifferenceImpl.create(leftValue, rightValue));
        }
      } else {
        eq = false;
        onlyOnLeft.put(leftKey, leftValue);
      }
    }

    boolean areEqual = eq && onlyOnRight.isEmpty();
    return sortedMapDifference(
        areEqual, onlyOnLeft, onlyOnRight, onBoth, differences);
  }
{
    long size = 0;
    ImmutableMap.Builder<E, Integer> builder = ImmutableMap.builder();

    for (Entry<? extends E> entry : multiset.entrySet()) {
      int count = entry.getCount();
      if (count > 0) {
        // Since ImmutableMap.Builder throws an NPE if an element is null, no
        // other null checks are needed.
        builder.put(entry.getElement(), count);
        size += count;
      }
    }

    if (size == 0) {
      return of();
    }
    return new RegularImmutableMultiset<E>(builder.build(),
        Ints.saturatedCast(size));
  }
{
    checkNotNull(function);
    EntryTransformer<K, V1, V2> transformer =
        new EntryTransformer<K, V1, V2>() {
          @Override
          public V2 transformEntry(K key, V1 value) {
            return function.apply(value);
          }
        };
    return transformEntries(fromMap, transformer);
  }
{
    return new TransformedEntriesSortedMap<K, V1, V2>(fromMap, transformer);
  }
{
    checkNotNull(keyFunction);
    ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
    for (V value : values) {
      builder.put(keyFunction.apply(value), value);
    }
    return builder.build();
  }
{
    checkNotNull(keyFunction);
    ImmutableListMultimap.Builder<K, V> builder
        = ImmutableListMultimap.builder();
    for (V value : values) {
      checkNotNull(value, values);
      builder.put(keyFunction.apply(value), value);
    }
    return builder.build();
  }
{
    try {
      return unbox(countMap.remove(element));
    } catch (NullPointerException e) {
      return 0;
    } catch (ClassCastException e) {
      return 0;
    }
  }
{
    return comparator;
  }
{
    return originalTarget == changedTarget;
  }
{
    checkNotNull(comparator);
    checkNotNull(mutationRule);
    checkNotNull(key);
    BstBalancePolicy<N> rebalancePolicy = mutationRule.getBalancePolicy();
    BstNodeFactory<N> nodeFactory = mutationRule.getNodeFactory();
    BstModifier<K, N> modifier = mutationRule.getModifier();

    if (tree != null) {
      int cmp = comparator.compare(key, tree.getKey());
      if (cmp != 0) {
        BstSide side = (cmp < 0) ? LEFT : RIGHT;
        BstMutationResult<K, N> mutation =
            mutate(comparator, mutationRule, tree.childOrNull(side), key);
        return mutation.lift(tree, side, nodeFactory, rebalancePolicy);
      }
    }
    // We're modifying this node
    N newTree = modifier.modify(key, tree);
    if (newTree == tree) {
      return BstMutationResult.identity(key, tree, tree);
    } else if (newTree == null) {
      newTree =
          rebalancePolicy.combine(nodeFactory, tree.childOrNull(LEFT), tree.childOrNull(RIGHT));
    } else {
      N left = null;
      N right = null;
      if (tree != null) {
        left = tree.childOrNull(LEFT);
        right = tree.childOrNull(RIGHT);
      }
      newTree = rebalancePolicy.balance(nodeFactory, newTree, left, right);
    }
    return BstMutationResult.mutationResult(key, tree, newTree, tree, newTree);
  }
{
    checkNotNull(future);
    checkArgument(!RuntimeException.class.isAssignableFrom(exceptionClass),
        "Futures.get exception type (%s) must not be a RuntimeException",
        exceptionClass);
    try {
      return future.get();
    } catch (InterruptedException e) {
      currentThread().interrupt();
      throw newWithCause(exceptionClass, e);
    } catch (CancellationException e) {
      throw e;
    } catch (ExecutionException e) {
      throw newWithCause(exceptionClass, e.getCause());
    } catch (RuntimeException e) {
      throw newWithCause(exceptionClass, e);
    }
  }
{
    Map<K, V> onlyOnLeft = newHashMap();
    Map<K, V> onlyOnRight = new HashMap<K, V>(right); // will whittle it down
    Map<K, V> onBoth = newHashMap();
    Map<K, MapDifference.ValueDifference<V>> differences = newHashMap();
    boolean eq = true;

    for (Entry<? extends K, ? extends V> entry : left.entrySet()) {
      K leftKey = entry.getKey();
      V leftValue = entry.getValue();
      if (right.containsKey(leftKey)) {
        V rightValue = onlyOnRight.remove(leftKey);
        if (Objects.equal(leftValue, rightValue)) {
          onBoth.put(leftKey, leftValue);
        } else {
          eq = false;
          differences.put(
              leftKey, new ValueDifferenceImpl<V>(leftValue, rightValue));
        }
      } else {
        eq = false;
        onlyOnLeft.put(leftKey, leftValue);
      }
    }

    boolean areEqual = eq && onlyOnRight.isEmpty();
    return mapDifference(
        areEqual, onlyOnLeft, onlyOnRight, onBoth, differences);
  }
{
      checkNotNull(appendable);
      Iterator<? extends Map.Entry<?, ?>> iterator = map.entrySet().iterator();
      if (iterator.hasNext()) {
        Entry<?, ?> entry = iterator.next();
        appendable.append(joiner.toString(entry.getKey()));
        appendable.append(keyValueSeparator);
        appendable.append(joiner.toString(entry.getValue()));
        while (iterator.hasNext()) {
          appendable.append(joiner.separator);
          Entry<?, ?> e = iterator.next();
          appendable.append(joiner.toString(e.getKey()));
          appendable.append(keyValueSeparator);
          appendable.append(joiner.toString(e.getValue()));
        }
      }
      return appendable;
    }
{
      try {
        appendTo((Appendable) builder, map);
      } catch (IOException impossible) {
        throw new AssertionError(impossible);
      }
      return builder;
    }
{
    if (ip instanceof Inet6Address) {
      return "[" + ip.getHostAddress() + "]";
    }
    return ip.getHostAddress();
  }
{
    return new InternetDomainName(checkNotNull(domain));
  }
{
    try {
      fromLenient(name);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
{
    // Lock while we update our state so the add method above will finish adding
    // any listeners before we start to run them.
    synchronized (runnables) {
      if (executed) {
        return;
      }
      executed = true;
    }

    // At this point the runnables will never be modified by another
    // thread, so we are safe using it outside of the synchronized block.
    while (!runnables.isEmpty()) {
      runnables.poll().execute();
    }
  }
{
    checkNotNull(fromElement);
    checkNotNull(toElement);
    return unsafeDelegateSortedSet(
        sortedDelegate.subSet(fromElement, toElement), true);
  }
{
    checkNotNull(fromElement);
    checkNotNull(toElement);
    checkArgument(comparator().compare(fromElement, toElement) <= 0);
    return subSetImpl(fromElement, toElement);
  }
{
      try {
        ReferenceEntry<K, V> e = getEntry(key, hash);
        if (e != null) {
          V value = getLiveValue(e);
          if (value != null) {
            recordRead(e);
          }
          return value;
        }
        return null;
      } finally {
        postReadCleanup();
      }
    }
{
      try {
        outer: while (true) {
          ReferenceEntry<K, V> e = getEntry(key, hash);
          if (e != null) {
            V value = getLiveValue(e);
            if (value != null) {
              recordRead(e);
              statsCounter.recordHit();
              return value;
            }
          }

          // at this point e is either null, computing, or expired;
          // avoid locking if it's already computing
          if (e == null || !e.getValueReference().isComputingReference()) {
            ComputingValueReference<K, V> computingValueReference = null;
            lock();
            try {
              preWriteCleanup();

              int newCount = this.count - 1;
              AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
              int index = hash & (table.length() - 1);
              ReferenceEntry<K, V> first = table.get(index);

              boolean createNewEntry = true;
              for (e = first; e != null; e = e.getNext()) {
                K entryKey = e.getKey();
                if (e.getHash() == hash && entryKey != null
                    && map.keyEquivalence.equivalent(key, entryKey)) {
                  ValueReference<K, V> valueReference = e.getValueReference();
                  if (valueReference.isComputingReference()) {
                    createNewEntry = false;
                  } else {
                    // never return expired entries
                    V value = getLiveValue(e);
                    if (value != null) {
                      recordLockedRead(e);
                      statsCounter.recordHit();
                      return value;
                    }
                    // immediately reuse partially collected entries
                    enqueueNotification(entryKey, hash, value, RemovalCause.COLLECTED);
                    evictionQueue.remove(e);
                    expirationQueue.remove(e);
                    this.count = newCount; // write-volatile
                  }
                  break;
                }
              }

              if (createNewEntry) {
                computingValueReference = new ComputingValueReference<K, V>(loader);

                if (e == null) {
                  e = newEntry(key, hash, first);
                  table.set(index, e);
                }
                e.setValueReference(computingValueReference);
              }
            } finally {
              unlock();
              postWriteCleanup();
            }

            if (computingValueReference != null) {
              // This thread solely created the entry.

              V value = null;
              long start = System.nanoTime();
              long end = 0;
              try {
                // Synchronizes on the entry to allow failing fast when a recursive computation is
                // detected. This is not fool-proof since the entry may be copied when the segment
                // is written to.
                synchronized (e) {
                  value = computingValueReference.compute(key, hash);
                  end = System.nanoTime();
                  statsCounter.recordCreateSuccess(end - start);
                }
                if (value != null) {
                  // putIfAbsent
                  V oldValue = put(key, hash, value, true);
                  if (oldValue != null) {
                    // the computed value was already clobbered
                    enqueueNotification(key, hash, value, RemovalCause.REPLACED);
                  }
                }
                return value;
              } finally {
                if (end == 0) {
                  end = System.nanoTime();
                  statsCounter.recordCreateException(end - start);
                }
                if (value == null) {
                  clearValue(key, hash, computingValueReference);
                }
              }
            }
          }

          // The entry already exists. Wait for the computation.
          checkState(!Thread.holdsLock(e), "Recursive computation");
          V value = e.getValueReference().waitForValue();
          // don't consider expiration as we're concurrent with computation
          if (value != null) {
            recordRead(e);
            statsCounter.recordConcurrentMiss();
            return value;
          }
          // else computing thread will clearValue
          continue outer;
        }
      } finally {
        postReadCleanup();
      }
    }
{
      checkNotNull(value);
      lock();
      try {
        preWriteCleanup();

        int newCount = this.count - 1;
        AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
        int index = hash & (table.length() - 1);
        ReferenceEntry<K, V> first = table.get(index);

        for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
          K entryKey = e.getKey();
          if (e.getHash() == hash && entryKey != null
              && map.keyEquivalence.equivalent(key, entryKey)) {
            V entryValue = e.getValueReference().get();
            if (map.valueEquivalence.equivalent(value, entryValue)) {
              ++modCount;
              enqueueNotification(entryKey, hash, entryValue, RemovalCause.EXPLICIT);
              ReferenceEntry<K, V> newFirst = removeFromChain(first, e);
              table.set(index, newFirst);
              this.count = newCount; // write-volatile
              return true;
            }
            return false;
          }
        }

        return false;
      } finally {
        unlock();
        postWriteCleanup();
      }
    }
{
    checkState(keyStrength == null, "Key strength was already set to %s", keyStrength);
    keyStrength = checkNotNull(strength);
    if (strength != Strength.STRONG) {
      // STRONG could be used during deserialization.
      useCustomMap = true;
    }
    return this;
  }
{
    checkState(valueStrength == null, "Value strength was already set to %s", valueStrength);
    valueStrength = checkNotNull(strength);
    if (strength != Strength.STRONG) {
      // STRONG could be used during deserialization.
      useCustomMap = true;
    }
    return this;
  }
{
      if (isUnset(entry)) {
        // keep count consistent
        return false;
      }

      int newCount = this.count - 1;
      ++modCount;
      ValueReference<K, V> valueReference = entry.getValueReference();
      if (valueReference.isComputingReference()) {
        return false;
      }

      K key = entry.getKey();
      enqueueNotification(key, hash, valueReference.get(), cause);
      enqueueCleanup(entry);
      this.count = newCount; // write-volatile
      return true;
    }
{
      if (count != 0) {
        lock();
        try {
          AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
          if (map.removalNotificationQueue != DISCARDING_QUEUE) {
            for (int i = 0; i < table.length(); ++i) {
              for (ReferenceEntry<K, V> e = table.get(i); e != null; e = e.getNext()) {
                enqueueNotification(
                    e.getKey(), e.getHash(), e.getValueReference().get(), RemovalCause.EXPLICIT);
              }
            }
          }
          for (int i = 0; i < table.length(); ++i) {
            table.set(i, null);
          }
          evictionQueue.clear();
          expirationQueue.clear();
          readCount.set(0);

          ++modCount;
          count = 0; // write-volatile
        } finally {
          unlock();
          postWriteCleanup();
        }
      }
    }
{
    return useNullCache()
        ? new ComputingMapAdapter<K, V>(this, CACHE_STATS_COUNTER, computingFunction)
        : new NullComputingConcurrentMap<K, V>(this, computingFunction);
  }
{
      if (isUnset(entry)) {
        // keep count consistent
        return false;
      }

      int newCount = this.count - 1;
      ++modCount;
      ValueReference<K, V> valueReference = entry.getValueReference();
      if (valueReference.isComputingReference()) {
        return false;
      }

      K key = entry.getKey();
      map.enqueueNotification(key, hash, valueReference);
      enqueueCleanup(entry);
      this.count = newCount; // write-volatile
      return true;
    }
{
      if (isUnset(entry)) {
        // keep count consistent
        return false;
      }

      int newCount = this.count - 1;
      ++modCount;
      ValueReference<K, V> valueReference = entry.getValueReference();
      if (valueReference.isComputingReference()) {
        return false;
      }

      K key = entry.getKey();
      map.enqueueNotification(key, hash, valueReference);
      enqueueCleanup(entry);
      this.count = newCount; // write-volatile
      return true;
    }
{
      if (isUnset(entry)) {
        // keep count consistent
        return false;
      }

      int newCount = this.count - 1;
      ++modCount;
      ValueReference<K, V> valueReference = entry.getValueReference();
      if (valueReference.isComputingReference()) {
        return false;
      }

      K key = entry.getKey();
      map.enqueueNotification(key, hash, valueReference);
      enqueueCleanup(entry);
      this.count = newCount; // write-volatile
      return true;
    }
{
      if (isUnset(entry)) {
        // keep count consistent
        return false;
      }

      int newCount = this.count - 1;
      ++modCount;
      ValueReference<K, V> valueReference = entry.getValueReference();
      if (valueReference.isComputingReference()) {
        return false;
      }

      K key = entry.getKey();
      map.enqueueNotification(key, hash, valueReference);
      enqueueCleanup(entry);
      this.count = newCount; // write-volatile
      return true;
    }
{
      if (isUnset(entry)) {
        // keep count consistent
        return false;
      }

      int newCount = this.count - 1;
      ++modCount;
      ValueReference<K, V> valueReference = entry.getValueReference();
      if (valueReference.isComputingReference()) {
        return false;
      }

      K key = entry.getKey();
      map.enqueueNotification(key, hash, valueReference);
      enqueueCleanup(entry);
      this.count = newCount; // write-volatile
      return true;
    }
{
      // The entry already exists. Wait for the computation.
      boolean interrupted = false;
      try {
        while (true) {
          try {
            checkState(!Thread.holdsLock(entry), "Recursive computation");
            return entry.getValueReference().waitForValue();
          } catch (InterruptedException ie) {
            interrupted = true;
          }
        }
      } finally {
        if (interrupted) {
          Thread.currentThread().interrupt();
        }
      }
    }
{
      try {
        outer: while (true) {
          ReferenceEntry<K, V> e = getEntry(key, hash);
          if (e != null) {
            V value = getLiveValue(e);
            if (value != null) {
              recordRead(e);
              return value;
            }
          }

          // at this point e is either null, computing, or expired;
          // avoid locking if it's already computing
          if (e == null || !e.getValueReference().isComputingReference()) {
            ComputingValueReference<K, V> computingValueReference = null;
            lock();
            try {
              preWriteCleanup();

              // getFirst, but remember the index
              AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
              int index = hash & (table.length() - 1);
              ReferenceEntry<K, V> first = table.get(index);

              for (e = first; e != null; e = e.getNext()) {
                K entryKey = e.getKey();
                if (e.getHash() == hash && entryKey != null
                    && map.keyEquivalence.equivalent(key, entryKey)) {
                  if (!e.getValueReference().isComputingReference()) {
                    // never return expired entries
                    V value = getLiveValue(e);
                    if (value != null) {
                      recordLockedRead(e);
                      return value;
                    }
                    // clobber invalid entries
                    unsetLiveEntry(e, hash);
                  }
                  break;
                }
              }

              if (e == null || isUnset(e)) {
                // Create a new entry.
                ComputingConcurrentHashMap<K, V> computingMap =
                    (ComputingConcurrentHashMap<K, V>) map;
                computingValueReference = new ComputingValueReference<K, V>(computingMap);

                if (e == null) {
                  e = computingMap.newEntry(key, hash, first);
                  table.set(index, e);
                }
                e.setValueReference(computingValueReference);
              }
            } finally {
              unlock();
              postWriteCleanup();
            }

            if (computingValueReference != null) {
              // This thread solely created the entry.
              V value = null;
              try {
                // Synchronizes on the entry to allow failing fast when a
                // recursive computation is detected. This is not fool-proof
                // since the entry may be copied when the segment is written to.
                synchronized (e) {
                  value = computingValueReference.compute(key, hash);
                }
                checkNotNull(value, "compute() returned null unexpectedly");
                return value;
              } finally {
                if (value == null) {
                  clearValue(key, hash, computingValueReference);
                }
              }
            }
          }

          // The entry already exists. Wait for the computation.
          boolean interrupted = false;
          try {
            while (true) {
              try {
                checkState(!Thread.holdsLock(e), "Recursive computation");
                V value = e.getValueReference().waitForValue();
                // don't consider expiration as we're concurrent with computation
                if (value != null) {
                  recordRead(e);
                  return value;
                }
                // else computing thread will clearValue
                continue outer;
              } catch (InterruptedException ie) {
                interrupted = true;
              }
            }
          } finally {
            if (interrupted) {
              Thread.currentThread().interrupt();
            }
          }
        }
      } finally {
        postReadCleanup();
      }
    }
{
    Object[] elements = collection.toArray();
    switch (elements.length) {
      case 0:
        return of();
      case 1:
        @SuppressWarnings("unchecked") // collection had only Es in it
        E onlyElement = (E) elements[0];
        return of(onlyElement);
      default:
        // safe to use the array without copying it
        // as specified by Collection.toArray().
        return construct(elements);
    }
  }
{
    Object[] elements = collection.toArray();
    switch (elements.length) {
      case 0:
        return of();
      case 1:
        @SuppressWarnings("unchecked") // collection had only Es in it
        E onlyElement = (E) elements[0];
        return of(onlyElement);
      default:
        // safe to use the array without copying it
        // as specified by Collection.toArray().
        return construct(elements);
    }
  }
{
    checkPositionIndex(index, size);
    modCount++;
    size--;
    if (size == index) {
      queue[size] = null;
      return null;
    }
    E toTrickle = elementData(size);
    queue[size] = null;
    Heap heap = heapForIndex(index);
    // We consider elementData(index) a "hole", and we want to fill it
    // with the last element of the heap, toTrickle.
    // Since the last element of the heap is from the bottom level, we
    // optimistically fill index position with elements from lower levels,
    // moving the hole down. In most cases this reduces the number of
    // comparisons with toTrickle, but in some cases we will need to bubble it
    // all the way up again.
    int vacated = heap.fillHoleAt(index);
    // Try to see if toTrickle can be bubbled up min levels.
    int bubbledTo = heap.bubbleUpAlternatingLevels(vacated, toTrickle);
    if (bubbledTo == vacated) {
      // Could not bubble toTrickle up min levels, try moving
      // it from min level to max level (or max to min level) and bubble up
      // there.
      return heap.tryCrossOverAndBubbleUp(index, vacated, toTrickle);
    } else {
      return (bubbledTo < index)
          ? new MoveDesc<E>(toTrickle, elementData(index))
          : null;
    }
  }
{
      if (entry.getKey() == null) {
        return null;
      }
      V value = entry.getValueReference().get();
      if (value == null) {
        return null;
      }
      if (expires() && isExpired(entry)) {
        // cleanup expired entries when the lock is available
        if (tryLock()) {
          try {
            expireEntries();
          } finally {
            unlock();
          }
        }
        return null;
      }
      return value;
    }
{
      drainRecencyQueue();

      ReferenceEntry<K, V> evictable = evictionHead.getNextEvictable();
      checkState(evictable != evictionHead);

      // then remove a single entry
      if (!unsetEntry(evictable, evictable.getHash())) {
        throw new AssertionError();
      }
    }
{
    checkNotNull(function);
    Function<I, ListenableFuture<O>> wrapperFunction
        = new Function<I, ListenableFuture<O>>() {
            @Override public ListenableFuture<O> apply(I input) {
              O output = function.apply(input);
              return immediateFuture(output);
            }
        };
    return chain(future, wrapperFunction, exec);
  }
{
    if (future instanceof ListenableFuture) {
      return compose((ListenableFuture<I>) future, function);
    }
    checkNotNull(future);
    checkNotNull(function);
    return new Future<O>() {

      /*
       * Concurrency detail:
       *
       * <p>To preserve the idempotency of calls to this.get(*) calls to the
       * function are only applied once. A lock is required to prevent multiple
       * applications of the function. The calls to future.get(*) are performed
       * outside the lock, as is required to prevent calls to
       * get(long, TimeUnit) to persist beyond their timeout.
       *
       * <p>Calls to future.get(*) on every call to this.get(*) also provide
       * the cancellation behavior for this.
       *
       * <p>(Consider: in thread A, call get(), in thread B call get(long,
       * TimeUnit). Thread B may have to wait for Thread A to finish, which
       * would be unacceptable.)
       *
       * <p>Note that each call to Future<O>.get(*) results in a call to
       * Future<I>.get(*), but the function is only applied once, so
       * Future<I>.get(*) is assumed to be idempotent.
       */

      private final Object lock = new Object();
      private boolean set = false;
      private O value = null;
      private ExecutionException exception = null;

      @Override
      public O get() throws InterruptedException, ExecutionException {
        return apply(future.get());
      }

      @Override
      public O get(long timeout, TimeUnit unit) throws InterruptedException,
          ExecutionException, TimeoutException {
        return apply(future.get(timeout, unit));
      }

      private O apply(I raw) throws ExecutionException {
        synchronized (lock) {
          if (!set) {
            try {
              value = function.apply(raw);
            } catch (RuntimeException e) {
              exception = new ExecutionException(e);
            } catch (Error e) {
              exception = new ExecutionException(e);
            }
            set = true;
          }

          if (exception != null) {
            throw exception;
          }
          return value;
        }
      }

      @Override
      public boolean cancel(boolean mayInterruptIfRunning) {
        return future.cancel(mayInterruptIfRunning);
      }

      @Override
      public boolean isCancelled() {
        return future.isCancelled();
      }

      @Override
      public boolean isDone() {
        return future.isDone();
      }
    };
  }
{
      checkNotNull(oldValue);
      checkNotNull(newValue);
      lock();
      try {
        expireEntries();

        for (ReferenceEntry<K, V> e = getFirst(hash); e != null;
            e = e.getNext()) {
          K entryKey = e.getKey();
          if (e.getHash() == hash && entryKey != null
              && keyEquivalence.equivalent(key, entryKey)) {
            // If the value disappeared, this entry is partially collected,
            // and we should pretend like it doesn't exist.
            V entryValue = e.getValueReference().get();
            if (entryValue == null) {
              return false;
            }

            if (valueEquivalence.equivalent(oldValue, entryValue)) {
              setValue(e, newValue);
              return true;
            } else {
              // Mimic
              // "if (map.containsKey(key) && map.get(key).equals(oldValue))..."
              recordWrite(e);
            }
          }
        }

        return false;
      } finally {
        unlock();
        scheduleCleanup();
      }
    }
{
      int newCount = this.count - 1;
      ++modCount;
      K key = entry.getKey();
      ValueReference<K, V> valueReference = entry.getValueReference();
      enqueueNotification(key, hash, valueReference);
      enqueueCleanup(entry);
      this.count = newCount; // write-volatile
    }
{
      ValueReference<K, V> valueReference = entry.getValueReference();
      if (isInvalid(valueReference)) {
        // short-circuit to ensure that notifications are only sent once
        return false;
      }

      int newCount = this.count - 1;
      for (ReferenceEntry<K, V> e = getFirst(hash); e != null;
          e = e.getNext()) {
        if (e == entry) {
          ++modCount;
          K key = entry.getKey();
          enqueueNotification(key, hash, valueReference);
          enqueueCleanup(entry);
          this.count = newCount; // write-volatile
          return true;
        }
      }

      return false;
    }
{
      int newCount = this.count - 1;
      ++modCount;
      K key = entry.getKey();
      ValueReference<K, V> valueReference = entry.getValueReference();
      enqueueNotification(key, hash, valueReference);
      enqueueCleanup(entry);
      this.count = newCount; // write-volatile
    }
{
      ValueReference<K, V> valueReference = entry.getValueReference();
      if (isInvalid(valueReference)) {
        // short-circuit to ensure that notifications are only sent once
        return false;
      }

      int newCount = this.count - 1;
      for (ReferenceEntry<K, V> e = getFirst(hash); e != null;
          e = e.getNext()) {
        if (e == entry) {
          ++modCount;
          K key = entry.getKey();
          enqueueNotification(key, hash, valueReference);
          enqueueCleanup(entry);
          this.count = newCount; // write-volatile
          return true;
        }
      }

      return false;
    }
{
      checkNotNull(value);
      lock();
      try {
        expireEntries();

        int newCount = this.count + 1;
        if (newCount > this.threshold) { // ensure capacity
          expand();
        }

        // getFirst, but remember the index
        AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
        int index = hash & (table.length() - 1);
        ReferenceEntry<K, V> first = table.get(index);

        // Look for an existing entry.
        for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
          K entryKey = e.getKey();
          if (e.getHash() == hash && entryKey != null
              && keyEquivalence.equivalent(key, entryKey)) {
            // We found an existing entry.

            // If the value disappeared, this entry is partially collected,
            // and we should pretend like it doesn't exist.
            ValueReference<K, V> valueReference = e.getValueReference();
            V entryValue = valueReference.get();
            boolean absent = (entryValue == null);
            if (onlyIfAbsent && !absent) {
              if (evictsBySize() || expires()) {
                recordWrite(e);
              }
              return entryValue;
            }

            setValue(e, value);
            if (valueReference == UNSET) {
              ++modCount;
              this.count = newCount; // write-volatile
            } else if (absent) {
              // value was garbage collected
              enqueueNotification(key, hash);
            }
            return entryValue;
          }
        }

        if (evictsBySize() && newCount > maxSegmentSize) {
          evictEntry();
          // this.count just changed; read it again
          newCount = this.count + 1;
          first = table.get(index);
        }

        // Create a new entry.
        ++modCount;
        ReferenceEntry<K, V> newEntry = entryFactory.newEntry(
            CustomConcurrentHashMap.this, key, hash, first);
        setValue(newEntry, value);
        table.set(index, newEntry);
        this.count = newCount; // write-volatile
        return null;
      } finally {
        unlock();
        processPendingNotifications();
      }
    }
{
      checkNotNull(value);
      lock();
      try {
        expireEntries();

        int newCount = this.count + 1;
        if (newCount > this.threshold) { // ensure capacity
          expand();
        }

        // getFirst, but remember the index
        AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
        int index = hash & (table.length() - 1);
        ReferenceEntry<K, V> first = table.get(index);

        // Look for an existing entry.
        for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
          K entryKey = e.getKey();
          if (e.getHash() == hash && entryKey != null
              && keyEquivalence.equivalent(key, entryKey)) {
            // We found an existing entry.

            // If the value disappeared, this entry is partially collected,
            // and we should pretend like it doesn't exist.
            ValueReference<K, V> valueReference = e.getValueReference();
            V entryValue = valueReference.get();
            boolean absent = (entryValue == null);
            if (onlyIfAbsent && !absent) {
              if (evictsBySize() || expires()) {
                recordWrite(e);
              }
              return entryValue;
            }

            setValue(e, value);
            if (valueReference == UNSET) {
              ++modCount;
              this.count = newCount; // write-volatile
            } else if (absent) {
              // value was garbage collected
              enqueueNotification(key, hash);
            }
            return entryValue;
          }
        }

        if (evictsBySize() && newCount > maxSegmentSize) {
          evictEntry();
          // this.count just changed; read it again
          newCount = this.count + 1;
          first = table.get(index);
        }

        // Create a new entry.
        ++modCount;
        ReferenceEntry<K, V> newEntry = entryFactory.newEntry(
            CustomConcurrentHashMap.this, key, hash, first);
        setValue(newEntry, value);
        table.set(index, newEntry);
        this.count = newCount; // write-volatile
        return null;
      } finally {
        unlock();
        processPendingNotifications();
      }
    }
{
    checkNotNull(multimap); // eager for GWT
    if (multimap.isEmpty()) {
      return of();
    }

    if (multimap instanceof ImmutableSetMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableSetMultimap<K, V> kvMultimap
          = (ImmutableSetMultimap<K, V>) multimap;
      if (!kvMultimap.isPartialView()) {
        return kvMultimap;
      }
    }

    ImmutableMap.Builder<K, ImmutableSet<V>> builder = ImmutableMap.builder();
    int size = 0;

    for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry
        : multimap.asMap().entrySet()) {
      K key = entry.getKey();
      Collection<? extends V> values = entry.getValue();
      ImmutableSet<V> set = ImmutableSet.copyOf(values);
      if (!set.isEmpty()) {
        builder.put(key, set);
        size += set.size();
      }
    }

    return new ImmutableSetMultimap<K, V>(builder.build(), size);
  }
{
    if (expirationMillis != 0) {
      throw new IllegalStateException("expiration time of "
          + expirationMillis + " ns was already set");
    }
    if (duration <= 0) {
      throw new IllegalArgumentException("invalid duration: " + duration);
    }
    this.expirationMillis = unit.toMillis(duration);
    useCustomMap = true;
    return this;
  }
{
    Object[] elements = collection.toArray();
    switch (elements.length) {
      case 0:
        return of();
      case 1:
        @SuppressWarnings("unchecked") // collection had only Es in it
        E onlyElement = (E) elements[0];
        return of(onlyElement);
      default:
        // safe to use the array without copying it
        // as specified by Collection.toArray().
        return construct(elements);
    }
  }
{
    int size = 1;
    for (int i = 1; i < array.length; i++) {
      Object element = array[i];
      if (unsafeCompare(comparator, array[size - 1], element) != 0) {
        array[size] = element;
        size++;
      }
    }

    // TODO(kevinb): Move to ObjectArrays?
    if (size == array.length) {
      return array;
    } else {
      Object[] copy = new Object[size];
      Platform.unsafeArrayCopy(array, 0, copy, 0, size);
      return copy;
    }
  }
{
    if (multimap instanceof ImmutableMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableMultimap<K, V> kvMultimap
          = (ImmutableMultimap<K, V>) multimap;
      return kvMultimap;
    } else {
      return ImmutableListMultimap.copyOf(multimap);
    }
  }
{
    if (multimap instanceof ImmutableMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableMultimap<K, V> kvMultimap
          = (ImmutableMultimap<K, V>) multimap;
      return kvMultimap;
    } else {
      return ImmutableListMultimap.copyOf(multimap);
    }
  }
{
    if (multimap instanceof ImmutableMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableMultimap<K, V> kvMultimap
          = (ImmutableMultimap<K, V>) multimap;
      return kvMultimap;
    } else {
      return ImmutableListMultimap.copyOf(multimap);
    }
  }
{
    if (multimap instanceof ImmutableMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableMultimap<K, V> kvMultimap
          = (ImmutableMultimap<K, V>) multimap;
      return kvMultimap;
    } else {
      return ImmutableListMultimap.copyOf(multimap);
    }
  }
{
    if (multimap instanceof ImmutableMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableMultimap<K, V> kvMultimap
          = (ImmutableMultimap<K, V>) multimap;
      return kvMultimap;
    } else {
      return ImmutableListMultimap.copyOf(multimap);
    }
  }
{
    if (multimap instanceof ImmutableMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableMultimap<K, V> kvMultimap
          = (ImmutableMultimap<K, V>) multimap;
      return kvMultimap;
    } else {
      return ImmutableListMultimap.copyOf(multimap);
    }
  }
{
    if (multimap instanceof ImmutableMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableMultimap<K, V> kvMultimap
          = (ImmutableMultimap<K, V>) multimap;
      return kvMultimap;
    } else {
      return ImmutableListMultimap.copyOf(multimap);
    }
  }
{
    if (multimap instanceof ImmutableMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableMultimap<K, V> kvMultimap
          = (ImmutableMultimap<K, V>) multimap;
      return kvMultimap;
    } else {
      return ImmutableListMultimap.copyOf(multimap);
    }
  }
{
    if (multimap instanceof ImmutableMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableMultimap<K, V> kvMultimap
          = (ImmutableMultimap<K, V>) multimap;
      return kvMultimap;
    } else {
      return ImmutableListMultimap.copyOf(multimap);
    }
  }
{
    if (multimap instanceof ImmutableMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableMultimap<K, V> kvMultimap
          = (ImmutableMultimap<K, V>) multimap;
      return kvMultimap;
    } else {
      return ImmutableListMultimap.copyOf(multimap);
    }
  }
{
    if (multimap instanceof ImmutableMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableMultimap<K, V> kvMultimap
          = (ImmutableMultimap<K, V>) multimap;
      return kvMultimap;
    } else {
      return ImmutableListMultimap.copyOf(multimap);
    }
  }
{
    if (multimap instanceof ImmutableMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableMultimap<K, V> kvMultimap
          = (ImmutableMultimap<K, V>) multimap;
      return kvMultimap;
    } else {
      return ImmutableListMultimap.copyOf(multimap);
    }
  }
{
    if (multimap instanceof ImmutableMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableMultimap<K, V> kvMultimap
          = (ImmutableMultimap<K, V>) multimap;
      return kvMultimap;
    } else {
      return ImmutableListMultimap.copyOf(multimap);
    }
  }
{
    if (multimap instanceof ImmutableMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableMultimap<K, V> kvMultimap
          = (ImmutableMultimap<K, V>) multimap;
      return kvMultimap;
    } else {
      return ImmutableListMultimap.copyOf(multimap);
    }
  }
{
    if (multimap instanceof ImmutableMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableMultimap<K, V> kvMultimap
          = (ImmutableMultimap<K, V>) multimap;
      return kvMultimap;
    } else {
      return ImmutableListMultimap.copyOf(multimap);
    }
  }
{
    if (multimap instanceof ImmutableMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableMultimap<K, V> kvMultimap
          = (ImmutableMultimap<K, V>) multimap;
      return kvMultimap;
    } else {
      return ImmutableListMultimap.copyOf(multimap);
    }
  }
{
    if (multimap instanceof ImmutableMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableMultimap<K, V> kvMultimap
          = (ImmutableMultimap<K, V>) multimap;
      return kvMultimap;
    } else {
      return ImmutableListMultimap.copyOf(multimap);
    }
  }
{
    if (multimap instanceof ImmutableMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableMultimap<K, V> kvMultimap
          = (ImmutableMultimap<K, V>) multimap;
      return kvMultimap;
    } else {
      return ImmutableListMultimap.copyOf(multimap);
    }
  }
{
    if (multimap instanceof ImmutableMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableMultimap<K, V> kvMultimap
          = (ImmutableMultimap<K, V>) multimap;
      return kvMultimap;
    } else {
      return ImmutableListMultimap.copyOf(multimap);
    }
  }
{
    if (multimap instanceof ImmutableMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableMultimap<K, V> kvMultimap
          = (ImmutableMultimap<K, V>) multimap;
      return kvMultimap;
    } else {
      return ImmutableListMultimap.copyOf(multimap);
    }
  }
{
    if (elements instanceof ImmutableSet
        && !(elements instanceof ImmutableSortedSet)) {
      @SuppressWarnings("unchecked") // all supported methods are covariant
      ImmutableSet<E> set = (ImmutableSet<E>) elements;
      return set;
    }
    return copyFromCollection(elements);
  }
{
    Object[] elements = collection.toArray();
    switch (elements.length) {
      case 0:
        return of();
      case 1:
        @SuppressWarnings("unchecked") // collection had only Es in it
        E onlyElement = (E) elements[0];
        return of(onlyElement);
      default:
        // safe to use the array without copying it
        // as specified by Collection.toArray().
        return construct(elements);
    }
  }
{
    checkNotNull(comparator);
    return copyOfInternal(comparator, elements, false);
  }
{
    // Hack around K not being a subtype of Comparable.
    // Unsafe, see ImmutableSortedSetFauxverideShim.
    @SuppressWarnings("unchecked")
    Ordering<E> naturalOrder = (Ordering) Ordering.<Comparable>natural();
    return copyOfInternal(naturalOrder, elements, false);
  }
{
    switch (collection.size()) {
      case 0:
        return of();
      case 1:
        // TODO: Remove "ImmutableSet.<E>" when eclipse bug is fixed.
        return ImmutableSet.<E>of(collection.iterator().next());
      default:
        Set<E> delegate = Sets.newLinkedHashSet();
        for (E element : collection) {
          checkNotNull(element);
          delegate.add(element);
        }
        return new RegularImmutableSet<E>(delegate);
    }
  }
{
    if (elements instanceof ImmutableSet
        && !(elements instanceof ImmutableSortedSet)) {
      @SuppressWarnings("unchecked") // all supported methods are covariant
      ImmutableSet<E> set = (ImmutableSet<E>) elements;
      return set;
    }
    return copyFromCollection(elements);
  }
{
    if (elements instanceof ImmutableSet
        && !(elements instanceof ImmutableSortedSet)) {
      @SuppressWarnings("unchecked") // all supported methods are covariant
      ImmutableSet<E> set = (ImmutableSet<E>) elements;
      return set;
    }
    return copyOfInternal(Collections2.toCollection(elements));
  }
{
    final int paramCount = 12;
    Object[] array = new Object[paramCount + others.length];
    copyIntoArray(array, 0, e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12);
    copyIntoArray(array, paramCount, others);
    return new RegularImmutableList<E>(array);
  }
{
    checkArgument(bytes.length >= BYTES,
        "array too small: %s < %s", bytes.length, BYTES);
    return (char) ((bytes[0] << 8) | (bytes[1] & 0xFF));
  }
{
    checkArgument(bytes.length >= BYTES,
        "array too small: %s < %s", bytes.length, BYTES);
    return bytes[0] << 24
        | (bytes[1] & 0xFF) << 16
        | (bytes[2] & 0xFF) << 8
        | (bytes[3] & 0xFF);
  }
{
    checkArgument(bytes.length >= BYTES,
        "array too small: %s < %s", bytes.length, BYTES);
    return (bytes[0] & 0xFFL) << 56
        | (bytes[1] & 0xFFL) << 48
        | (bytes[2] & 0xFFL) << 40
        | (bytes[3] & 0xFFL) << 32
        | (bytes[4] & 0xFFL) << 24
        | (bytes[5] & 0xFFL) << 16
        | (bytes[6] & 0xFFL) << 8
        | (bytes[7] & 0xFFL);
  }
{
    checkArgument(bytes.length >= BYTES,
        "array too small: %s < %s", bytes.length, BYTES);
    return (short) ((bytes[0] << 8) | (bytes[1] & 0xFF));
  }
{
    // count is always the (nonzero) number of elements in the iterable
    int tableSize = Hashing.chooseTableSize(count);
    Object[] table = new Object[tableSize];
    int mask = tableSize - 1;

    List<E> elements = new ArrayList<E>(count);
    int hashCode = 0;

    for (E element : iterable) {
      int hash = element.hashCode();
      for (int i = Hashing.smear(hash); true; i++) {
        int index = i & mask;
        Object value = table[index];
        if (value == null) {
          // Came to an empty bucket. Put the element here.
          table[index] = element;
          elements.add(element);
          hashCode += hash;
          break;
        } else if (value.equals(element)) {
          break; // Found a duplicate. Nothing to do.
        }
      }
    }

    if (elements.size() == 1) {
      // The iterable contained only duplicates of the same element.
      return new SingletonImmutableSet<E>(elements.get(0), hashCode);
    } else if (tableSize > Hashing.chooseTableSize(elements.size())) {
      // Resize the table when the iterable includes too many duplicates.
      return create(elements, elements.size());
    } else {
      return new RegularImmutableSet<E>(
          elements.toArray(), hashCode, table, mask);
    }
  }
{
    if (object == this) {
      return true;
    }
    if (object instanceof Map) {
      Map<?, ?> that = (Map<?, ?>) object;
      return this.entrySet().equals(that.entrySet());
    }
    return false;
  }
{
    return tldIndex == 0;
  }
{
    return tldIndex != NO_TLD_FOUND;
  }
{
    return tldIndex > 0;
  }
{
    return tldIndex == 1;
  }
{
    if (isImmediatelyUnderTld()) {
      return this;
    }

    if (!isUnderRecognizedTld()) {
      throw new IllegalStateException("Not under TLD: " + name);
    }

    return ancestor(tldIndex - 1);
  }
{
    Object[] array = new Object[source.length];
    int index = 0;
    for (Object element : source) {
      if (element == null) {
        throw new NullPointerException("at index " + index);
      }
      array[index++] = element;
    }
    return array;
  }
{
    checkNotNull(elements); // for GWT
    switch (elements.length) {
      case 0:
        return of();
      case 1:
        return of(elements[0]);
      default:
        return create(elements);
    }
  }
{
    checkNotNull(iterable);
    if (iterable instanceof List) {
      return ((List<T>) iterable).get(position);
    }

    if (iterable instanceof Collection) {
      // Can check both ends
      Collection<T> collection = (Collection<T>) iterable;
      Preconditions.checkElementIndex(position, collection.size());
    } else {
      // Can only check the lower end
      if (position < 0) {
        throw new IndexOutOfBoundsException(
            "position cannot be negative: " + position);
      }
    }
    return Iterators.get(iterable.iterator(), position);
  }
{
    if (iterable instanceof List) {
      List<T> list = (List<T>) iterable;
      // TODO: Support a concurrent list whose size changes while this method
      // is running.
      if (list.isEmpty()) {
        throw new NoSuchElementException();
      }
      return list.get(list.size() - 1);
    }

    if (iterable instanceof SortedSet) {
      SortedSet<T> sortedSet = (SortedSet<T>) iterable;
      return sortedSet.last();
    }

    return Iterators.getLast(iterable.iterator());
  }
{
    if (position < 0) {
      throw new IndexOutOfBoundsException("position (" + position
          + ") must not be negative");
    }

    int skipped = 0;
    while (iterator.hasNext()) {
      T t = iterator.next();
      if (skipped++ == position) {
        return t;
      }
    }

    throw new IndexOutOfBoundsException("position (" + position
        + ") must be less than the number of elements that remained ("
        + skipped + ")");
  }
{
    return getClass().getSimpleName();
  }
{
    return getClass().getSimpleName();
  }
{
    if (elements instanceof ImmutableList) {
      /*
       * TODO: If the given ImmutableList is a sublist, copy the referenced
       * portion of the array into a new array to save space?
       */
      @SuppressWarnings("unchecked") // all supported methods are covariant
      ImmutableList<E> list = (ImmutableList<E>) elements;
      return list;
    } else if (elements instanceof Collection) {
      @SuppressWarnings("unchecked")
      Collection<? extends E> coll = (Collection<? extends E>) elements;
      return copyOfInternal(coll);
    } else {
      return copyOfInternal(Lists.newArrayList(elements));
    }
  }
{
    Function<I, ListenableFuture<O>> wrapperFunction
        = new Function<I, ListenableFuture<O>>() {
            public ListenableFuture<O> apply(I input) {
              O output = function.apply(input);
              return immediateFuture(output);
            }
        };
    return chain(future, wrapperFunction);
  }

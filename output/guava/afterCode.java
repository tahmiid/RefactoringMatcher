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
    Object[] elements = collection.toArray();
    switch (elements.length) {
      case 0:
        return of();
      case 1:
        @SuppressWarnings("unchecked") // collection had only Es in it
        ImmutableList<E> list = new SingletonImmutableList<E>((E) elements[0]);
        return list;
      default:
        return new RegularImmutableList<E>(ImmutableList.<E>nullCheckedList(elements));
    }
  }
{
    switch (list.size()) {
      case 0:
        return of();
      case 1:
        return new SingletonImmutableList<E>(list.iterator().next());
      default:
        @SuppressWarnings("unchecked")
        List<E> castedList = (List<E>) list;
        return new RegularImmutableList<E>(castedList);
    }
  }
{
    int size = entries.size();
    switch (size) {
      case 0:
        return of();
      case 1:
        Entry<? extends K, ? extends V> entry = getOnlyElement(entries);
        return of((K) entry.getKey(), (V) entry.getValue());
      default:
        @SuppressWarnings("unchecked")
        Entry<K, V>[] entryArray
            = entries.toArray(new Entry[entries.size()]);
        return new RegularImmutableMap<K, V>(entryArray);
    }
  }
{
    checkNotNull(comparator);

    boolean hasSameComparator
        = fromSortedSet || hasSameComparator(elements, comparator);
    if (hasSameComparator && (elements instanceof ImmutableSortedSet)) {
      @SuppressWarnings("unchecked")
      ImmutableSortedSet<E> result = (ImmutableSortedSet<E>) elements;
      boolean isSubset = (result instanceof RegularImmutableSortedSet)
          && ((RegularImmutableSortedSet) result).isSubset;
      if (!isSubset) {
        // Only return the original copy if this immutable sorted set isn't
        // a subset of another, to avoid memory leak.
        return result;
      }
    }
    return copyOfInternal(comparator, elements.iterator());
  }
{
    checkNotNull(comparator);
    if (!elements.hasNext()) {
      return emptySet(comparator);
    }
    SortedSet<E> delegate = new TreeSet<E>(comparator);
    while (elements.hasNext()) {
      E element = elements.next();
      checkNotNull(element);
      delegate.add(element);
    }
    return new RegularImmutableSortedSet<E>(delegate, false);
  }
{
      if (Collections2.safeContains(backingSet(), key)) {
        @SuppressWarnings("unchecked") // unsafe, but Javadoc warns about it
        K k = (K) key;
        return function.apply(k);
      } else {
        return defaultValue;
      }
    }
{
      V1 value = fromMap.get(key);
      return (value != null || fromMap.containsKey(key))
          ? transformer.transformEntry((K) key, value)
          : defaultValue;
    }
{
    Object[] elements = collection.toArray();
    switch (elements.length) {
      case 0:
        return of();
      case 1:
        @SuppressWarnings("unchecked") // collection had only Es in it
        ImmutableList<E> list = new SingletonImmutableList<E>((E) elements[0]);
        return list;
      default:
        return new RegularImmutableList<E>(ImmutableList.<E>nullCheckedList(elements));
    }
  }
{
    switch (list.size()) {
      case 0:
        return of();
      case 1:
        return new SingletonImmutableList<E>(list.iterator().next());
      default:
        @SuppressWarnings("unchecked")
        List<E> castedList = (List<E>) list;
        return new RegularImmutableList<E>(castedList);
    }
  }
{
    Supplier<Integer> memoizedSupplier = Suppliers.memoize(countingSupplier);
    checkMemoize(countingSupplier, memoizedSupplier);
  }
{
    Supplier<Integer> memoizedSupplier = Suppliers.memoize(countingSupplier);
    assertSame(memoizedSupplier, Suppliers.memoize(memoizedSupplier));
  }
{
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
    for (Range<C> range : ranges) {
      if (!encloses(range)) {
        return false;
      }
    }
    return true;
  }
{
    for (Range<C> range : ranges) {
      add(range);
    }
  }
{
    for (Range<C> range : ranges) {
      remove(range);
    }
  }
{
      for (Range<C> range : ranges) {
        add(range);
      }
      return this;
    }
{
    // Very unlikely that a buggy collection would ever return true. It might accidentally throw.
    assertFalse(entrySet.contains("foo"));
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
    Object[] elements = collection.toArray();
    switch (elements.length) {
      case 0:
        return of();
      case 1:
        @SuppressWarnings("unchecked") // collection had only Es in it
        ImmutableList<E> list = new SingletonImmutableList<E>((E) elements[0]);
        return list;
      default:
        return new RegularImmutableList<E>(ImmutableList.<E>nullCheckedList(elements));
    }
  }
{
    switch (list.size()) {
      case 0:
        return of();
      case 1:
        return new SingletonImmutableList<E>(list.iterator().next());
      default:
        @SuppressWarnings("unchecked")
        List<E> castedList = (List<E>) list;
        return new RegularImmutableList<E>(castedList);
    }
  }
{
    int size = entries.size();
    switch (size) {
      case 0:
        return of();
      case 1:
        Entry<? extends K, ? extends V> entry = getOnlyElement(entries);
        return of((K) entry.getKey(), (V) entry.getValue());
      default:
        @SuppressWarnings("unchecked")
        Entry<K, V>[] entryArray
            = entries.toArray(new Entry[entries.size()]);
        return new RegularImmutableMap<K, V>(entryArray);
    }
  }
{
    checkNotNull(comparator);

    boolean hasSameComparator
        = fromSortedSet || hasSameComparator(elements, comparator);
    if (hasSameComparator && (elements instanceof ImmutableSortedSet)) {
      @SuppressWarnings("unchecked")
      ImmutableSortedSet<E> result = (ImmutableSortedSet<E>) elements;
      boolean isSubset = (result instanceof RegularImmutableSortedSet)
          && ((RegularImmutableSortedSet) result).isSubset;
      if (!isSubset) {
        // Only return the original copy if this immutable sorted set isn't
        // a subset of another, to avoid memory leak.
        return result;
      }
    }
    return copyOfInternal(comparator, elements.iterator());
  }
{
    checkNotNull(comparator);
    if (!elements.hasNext()) {
      return emptySet(comparator);
    }
    SortedSet<E> delegate = new TreeSet<E>(comparator);
    while (elements.hasNext()) {
      E element = elements.next();
      checkNotNull(element);
      delegate.add(element);
    }
    return new RegularImmutableSortedSet<E>(delegate, false);
  }
{
    V value = checkedConnections(nodeU).value(nodeV);
    if (value == null) {
      checkArgument(containsNode(nodeV), NODE_NOT_IN_GRAPH, nodeV);
      throw new IllegalArgumentException(String.format(EDGE_CONNECTING_NOT_IN_GRAPH, nodeU, nodeV));
    }
    return value;
  }
{
    V value = backingGraph.checkedConnections(nodeU).value(nodeV);
    if (value == null) {
      checkArgument(backingGraph.containsNode(nodeV), NODE_NOT_IN_GRAPH, nodeV);
      throw new IllegalArgumentException(String.format(EDGE_CONNECTING_NOT_IN_GRAPH, nodeU, nodeV));
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

    Graph<N, Set<E>> asGraph = network.asGraph();
    AbstractGraphTest.validateGraph(asGraph);
    assertThat(network.nodes()).isEqualTo(asGraph.nodes());
    assertThat(network.edges().size()).isAtLeast(asGraph.edges().size());
    assertThat(network.nodeOrder()).isEqualTo(asGraph.nodeOrder());
    assertThat(network.isDirected()).isEqualTo(asGraph.isDirected());
    assertThat(network.allowsSelfLoops()).isEqualTo(asGraph.allowsSelfLoops());

    sanityCheckCollection(network.nodes());
    sanityCheckCollection(network.edges());
    sanityCheckCollection(asGraph.edges());

    for (E edge : network.edges()) {
      // TODO(b/27817069): Consider verifying the edge's incident nodes in the string.
      assertThat(edgeString).contains(edge.toString());

      Endpoints<N> endpoints = network.incidentNodes(edge);
      N nodeA = endpoints.nodeA();
      N nodeB = endpoints.nodeB();
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

      for (N incidentNode : ImmutableSet.of(
          network.incidentNodes(edge).nodeA(), network.incidentNodes(edge).nodeB())) {
        assertThat(network.nodes()).contains(incidentNode);
        for (E adjacentEdge : network.incidentEdges(incidentNode)) {
          assertTrue(edge.equals(adjacentEdge)
              || Graphs.adjacentEdges(network, edge).contains(adjacentEdge));
        }
      }
    }

    for (N node : network.nodes()) {
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

      for (N otherNode : network.nodes()) {
        Set<E> edgesConnecting = network.edgesConnecting(node, otherNode);
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
        for (E edge : edgesConnecting) {
          assertThat(network.incidentNodes(edge)).isEqualTo(Endpoints.of(network, node, otherNode));
        }
      }

      for (E incidentEdge : network.incidentEdges(node)) {
        assertTrue(network.inEdges(node).contains(incidentEdge)
            || network.outEdges(node).contains(incidentEdge));
        assertThat(network.edges()).contains(incidentEdge);
        assertTrue(network.incidentNodes(incidentEdge).nodeA().equals(node)
            || network.incidentNodes(incidentEdge).nodeB().equals(node));
      }

      for (E inEdge : network.inEdges(node)) {
        assertThat(network.incidentEdges(node)).contains(inEdge);
        assertThat(network.outEdges(network.incidentNodes(inEdge).adjacentNode(node)))
            .contains(inEdge);
      }

      for (E outEdge : network.outEdges(node)) {
        assertThat(network.incidentEdges(node)).contains(outEdge);
        assertThat(network.inEdges(network.incidentNodes(outEdge).adjacentNode(node)))
            .contains(outEdge);
      }

      for (N adjacentNode : network.adjacentNodes(node)) {
        assertTrue(network.predecessors(node).contains(adjacentNode)
            || network.successors(node).contains(adjacentNode));
        assertTrue(!network.edgesConnecting(node, adjacentNode).isEmpty()
            || !network.edgesConnecting(adjacentNode, node).isEmpty());
      }

      for (N predecessor : network.predecessors(node)) {
        assertThat(network.successors(predecessor)).contains(node);
        assertThat(network.edgesConnecting(predecessor, node)).isNotEmpty();
      }

      for (N successor : network.successors(node)) {
        assertThat(network.predecessors(successor)).contains(node);
        assertThat(network.edgesConnecting(node, successor)).isNotEmpty();
      }
    }
  }
{
    Function<E, Endpoints<N>> edgeToEndpointsFn = new Function<E, Endpoints<N>>() {
      @Override
      public Endpoints<N> apply(E edge) {
        return incidentNodes(edge);
      }
    };
    return Maps.asMap(edges(), edgeToEndpointsFn);
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

    sanityCheckCollection(graph.nodes());
    sanityCheckCollection(graph.edges());

    Set<Endpoints<N>> allEndpoints = new HashSet<Endpoints<N>>();

    for (N node : graph.nodes()) {
      assertThat(nodeString).contains(node.toString());

      sanityCheckCollection(graph.adjacentNodes(node));
      sanityCheckCollection(graph.predecessors(node));
      sanityCheckCollection(graph.successors(node));

      for (N adjacentNode : graph.adjacentNodes(node)) {
        assertThat(graph.predecessors(node).contains(adjacentNode)
            || graph.successors(node).contains(adjacentNode)).isTrue();
      }

      for (N predecessor : graph.predecessors(node)) {
        assertThat(graph.successors(predecessor)).contains(node);
      }

      for (N successor : graph.successors(node)) {
        allEndpoints.add(Endpoints.of(graph, node, successor));
        assertThat(graph.predecessors(successor)).contains(node);
      }
    }

    assertThat(graph.edges()).isEqualTo(allEndpoints);
  }
{
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
    return predecessors;
  }
{
    Multiset<N> successors = getReference(successorsReference);
    if (successors == null) {
      successors = HashMultiset.create(outEdgeMap.values());
      successorsReference = new SoftReference<Multiset<N>>(successors);
    }
    return successors;
  }
{
    Multiset<N> adjacentNodes = getReference(adjacentNodesReference);
    if (adjacentNodes == null) {
      adjacentNodes = HashMultiset.create(incidentEdgeMap.values());
      adjacentNodesReference = new SoftReference<Multiset<N>>(adjacentNodes);
    }
    return adjacentNodes;
  }
{
    return new ConcurrentHashMultiset<E>(countMap);
  }
{
    checkNotNull(graph, "graph");
    // TODO(b/28087289): we can remove this restriction when Graph supports parallel edges
    checkArgument(!((graph instanceof Network) && ((Network<N, ?>) graph).allowsParallelEdges()),
        NETWORK_WITH_PARALLEL_EDGE);
    MutableGraph<N> copy = GraphBuilder.from(graph)
        .expectedNodeCount(graph.nodes().size())
        .build();

    for (N node : graph.nodes()) {
      copy.addNode(node);
      for (N successor : graph.successors(node)) {
        // TODO(b/28087289): Ensure that multiplicity is preserved if parallel edges are supported.
        copy.addEdge(node, successor);
      }
    }

    return copy;
  }
{
    checkNotNull(graph, "graph");
    MutableNetwork<N, E> copy = NetworkBuilder.from(graph)
        .expectedNodeCount(graph.nodes().size())
        .expectedEdgeCount(graph.edges().size())
        .build();

    for (N node : graph.nodes()) {
      copy.addNode(node);
    }
    for (E edge : graph.edges()) {
      addEdge(copy, edge, graph.incidentNodes(edge));
    }

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
    for (TypeToken<?> type : getTypes()) {
      Type ownerType = type.getOwnerTypeIfPresent();
      if (ownerType != null && of(ownerType).isSubtypeOf(supertype)) {
        return true;
      }
    }
    return false;
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
      return null;
    }

    return bytesToInetAddress(addr);
  }
{
    checkNotNull(node, "node");
    if (!nodes().contains(node)) {
      return false;
    }
    for (N successor : nodeConnections.get(node).successors()) {
      if (!node.equals(successor)) {
        // don't remove the successor if it's the input node (=> CME); will be removed below
        nodeConnections.get(successor).removePredecessor(node);
      }
    }
    for (N predecessor : nodeConnections.get(node).predecessors()) {
      nodeConnections.get(predecessor).removeSuccessor(node);
    }
    nodeConnections.remove(node);
    return true;
  }
{
    checkNotNull(node1, "node1");
    checkNotNull(node2, "node2");
    NodeAdjacencies<N> connectionsN1 = nodeConnections.get(node1);
    NodeAdjacencies<N> connectionsN2 = nodeConnections.get(node2);
    if (connectionsN1 == null || connectionsN2 == null) {
      return false;
    }
    boolean result = connectionsN1.removeSuccessor(node2);
    connectionsN2.removePredecessor(node1);
    return result;
  }
{
    checkNotNull(throwable);
    if (declaredType.isInstance(throwable)) {
      throw declaredType.cast(throwable);
    }
  }
{
    return expireAfterWriteNanos > 0;
  }
{
    switch (graphType) {
      case UNDIRECTED:
        return GraphType.DIRECTED;
      case DIRECTED:
        return GraphType.UNDIRECTED;
      default:
        throw new IllegalStateException("Unexpected graph type: " + graphType);
    }
  }
{
      graph.addNode(node);
      return this;
    }
{
      graph.addEdge(edge, node1, node2);
      return this;
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
    checkNotNull(edge, "edge");
    IncidentNodes<N> incidentNodes = edgeToIncidentNodes.get(edge);
    checkArgument(incidentNodes != null, EDGE_NOT_IN_GRAPH, edge);
    return incidentNodes;
  }
{
    checkNotNull(edge, "edge");
    IncidentNodes<N> incidentNodes = edgeToIncidentNodes.get(edge);
    checkArgument(incidentNodes != null, EDGE_NOT_IN_GRAPH, edge);
    return incidentNodes;
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
    checkNotNull(node, "node");
    DirectedIncidentEdges<E> incidentEdges = nodeToIncidentEdges.get(node);
    checkArgument(incidentEdges != null, NODE_NOT_IN_GRAPH, node);
    return incidentEdges.outEdges();
  }
{
    checkNotNull(edge, "edge");
    DirectedIncidentNodes<N> endpoints = edgeToIncidentNodes.get(edge);
    checkArgument(endpoints != null, EDGE_NOT_IN_GRAPH, edge);
    return endpoints.asImmutableSet();
  }
{
    checkNotNull(edge, "edge");
    DirectedIncidentNodes<N> endpoints = edgeToIncidentNodes.get(edge);
    checkArgument(endpoints != null, EDGE_NOT_IN_GRAPH, edge);
    return endpoints.source();
  }
{
    checkNotNull(edge, "edge");
    DirectedIncidentNodes<N> endpoints = edgeToIncidentNodes.get(edge);
    checkArgument(endpoints != null, EDGE_NOT_IN_GRAPH, edge);
    return endpoints.target();
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
    checkNotNull(node, "node");
    DirectedIncidentEdges<E> incidentEdges = nodeToIncidentEdges.get(node);
    checkArgument(incidentEdges != null, NODE_NOT_IN_GRAPH, node);
    return Collections.unmodifiableSet(incidentEdges.outEdges());
  }
{
    checkNotNull(node, "node");
    DirectedIncidentEdges<E> incidentEdges = nodeToIncidentEdges.get(node);
    checkArgument(incidentEdges != null, NODE_NOT_IN_GRAPH, node);
    final Set<E> inEdges = incidentEdges.inEdges();
    return new SetView<N>() {
      @Override
      public boolean isEmpty() {
        return inEdges.isEmpty();
      }

      @Override
      Set<N> elements() {
        Set<N> nodes = Sets.newLinkedHashSet();
        for (E edge : inEdges) {
          nodes.add(source(edge));
        }
        return nodes;
      }
    };
  }
{
    checkNotNull(node, "node");
    DirectedIncidentEdges<E> incidentEdges = nodeToIncidentEdges.get(node);
    checkArgument(incidentEdges != null, NODE_NOT_IN_GRAPH, node);
    final Set<E> outEdges = incidentEdges.outEdges();
    return new SetView<N>() {
      @Override
      public boolean isEmpty() {
        return outEdges.isEmpty();
      }

      @Override
      Set<N> elements() {
        Set<N> nodes = Sets.newLinkedHashSet();
        for (E edge : outEdges) {
          nodes.add(target(edge));
        }
        return nodes;
      }
    };
  }
{
    checkNotNull(edge, "edge");
    // Returning an immutable set here as the edge's endpoints will not change anyway.
    DirectedIncidentNodes<N> endpoints = edgeToIncidentNodes.get(edge);
    checkArgument(endpoints != null, EDGE_NOT_IN_GRAPH, edge);
    return endpoints.asImmutableSet();
  }
{
    checkNotNull(edge, "edge");
    DirectedIncidentNodes<N> endpoints = edgeToIncidentNodes.get(edge);
    checkArgument(endpoints != null, EDGE_NOT_IN_GRAPH, edge);
    return endpoints.source();
  }
{
    checkNotNull(edge, "edge");
    DirectedIncidentNodes<N> endpoints = edgeToIncidentNodes.get(edge);
    checkArgument(endpoints != null, EDGE_NOT_IN_GRAPH, edge);
    return endpoints.target();
  }
{
    checkNotNull(node, "node");
    Set<E> incidentEdges = nodeToIncidentEdges.get(node);
    checkArgument(incidentEdges != null, NODE_NOT_IN_GRAPH, node);
    return Collections.unmodifiableSet(incidentEdges);
  }
{
    checkNotNull(node, "node");
    final Set<E> incidentEdges = nodeToIncidentEdges.get(node);
    checkArgument(incidentEdges != null, NODE_NOT_IN_GRAPH, node);
    return new SetView<N>() {
      @Override
      public boolean isEmpty() {
        return incidentEdges.isEmpty();
      }

      @Override
      Set<N> elements() {
        Set<N> nodes = Sets.newLinkedHashSetWithExpectedSize(incidentEdges.size());
        for (E edge : incidentEdges) {
          nodes.add(oppositeNode(IncidenceSetUndirectedGraph.this, edge, node));
        }
        return nodes;
      }
    };
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
    checkNotNull(node, "node");
    NodeConnections<N, E> connections = nodeConnections.get(node);
    checkArgument(connections != null, NODE_NOT_IN_GRAPH, node);
    return connections;
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
    if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX) {
      throw new IllegalArgumentException(
          "radix must be between MIN_RADIX and MAX_RADIX but was " + radix);
    }
    boolean negative = string.charAt(0) == '-';
    int index = negative ? 1 : 0;
    if (index == string.length()) {
      return null;
    }
    int digit = digit(string.charAt(index++));
    if (digit < 0 || digit >= radix) {
      return null;
    }
    long accum = -digit;

    long cap = Long.MIN_VALUE / radix;

    while (index < string.length()) {
      digit = digit(string.charAt(index++));
      if (digit < 0 || digit >= radix || accum < cap) {
        return null;
      }
      accum *= radix;
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
      return next != null;
    }
{
      if (modCount != expectedModCount) {
        throw new ConcurrentModificationException();
      }
      checkRemove(toRemove != null);
      delete(toRemove);
      expectedModCount = modCount;
      toRemove = null;
    }
{
    int size = entries.size();
    switch (size) {
      case 0:
        return of();
      case 1:
        Entry<? extends K, ? extends V> entry = getOnlyElement(entries);
        return of((K) entry.getKey(), (V) entry.getValue());
      default:
        @SuppressWarnings("unchecked")
        Entry<K, V>[] entryArray
            = entries.toArray(new Entry[entries.size()]);
        return new RegularImmutableMap<K, V>(entryArray);
    }
  }
{
    return using(generator);
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
    // Only start measuring table size after the first element inserted, to
    // deal with empty-map optimization.
    map1.put(0, null);

    int initialBuckets = bucketsOf(map1);

    for (int i = 1; i < size; i++) {
      map1.put(i, null);
    }
    assertThat(bucketsOf(map1))
        .named("table size after adding " + size + " elements")
        .isEqualTo(initialBuckets);

    /*
     * Something slightly different happens when the entries are added all at
     * once; make sure that passes too.
     */
    map2.putAll(map1);
    assertThat(bucketsOf(map1))
        .named("table size after adding " + size + " elements")
        .isEqualTo(initialBuckets);
  }
{
    return (ImmutableSortedSet<E>) NATURAL_EMPTY_SET;
  }
{
    checkNotNull(comparator);

    boolean hasSameComparator
        = fromSortedSet || hasSameComparator(elements, comparator);
    if (hasSameComparator && (elements instanceof ImmutableSortedSet)) {
      @SuppressWarnings("unchecked")
      ImmutableSortedSet<E> result = (ImmutableSortedSet<E>) elements;
      boolean isSubset = (result instanceof RegularImmutableSortedSet)
          && ((RegularImmutableSortedSet) result).isSubset;
      if (!isSubset) {
        // Only return the original copy if this immutable sorted set isn't
        // a subset of another, to avoid memory leak.
        return result;
      }
    }
    return copyOfInternal(comparator, elements.iterator());
  }
{
    checkNotNull(comparator);
    if (!elements.hasNext()) {
      return emptySet(comparator);
    }
    SortedSet<E> delegate = new TreeSet<E>(comparator);
    while (elements.hasNext()) {
      E element = elements.next();
      checkNotNull(element);
      delegate.add(element);
    }
    return new RegularImmutableSortedSet<E>(delegate, false);
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
    if (elements instanceof ImmutableMultiset) {
      @SuppressWarnings("unchecked") // all supported methods are covariant
      ImmutableMultiset<E> result = (ImmutableMultiset<E>) elements;
      if (!result.isPartialView()) {
        return result;
      }
    }

    Multiset<? extends E> multiset = (elements instanceof Multiset)
        ? Multisets.cast(elements)
        : LinkedHashMultiset.create(elements);

    return copyFromEntries(multiset.entrySet());
  }
{
    Multiset<E> multiset = LinkedHashMultiset.create();
    Iterators.addAll(multiset, elements);
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
      return new StandardBaseEncoding(alphabet, paddingChar);
    }
{
    try {
      // GWT does not support String.getBytes(Charset)
      return decoded.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
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
      return new StandardBaseEncoding(alphabet, paddingChar);
    }
{
    ChainingListenableFuture<I, O> output = new ChainingListenableFuture<I, O>(function, input);
    input.addListener(output, directExecutor());
    return output;
  }
{
    checkNotNull(executor);
    ChainingListenableFuture<I, O> output = new ChainingListenableFuture<I, O>(function, input);
    input.addListener(rejectionPropagatingRunnable(output, output, executor), directExecutor());
    return output;
  }
{
    checkNotNull(fallback);
    return new AsyncFunction<Throwable, V>() {
      @Override
      public ListenableFuture<V> apply(Throwable t) throws Exception {
        return checkNotNull(fallback.create(t), "FutureFallback.create returned null instead of a "
            + "Future. Did you mean to return immediateFuture(null)?");
      }
    };
  }
{
    Scanner scanner = new Scanner();
    for (Map.Entry<File, ClassLoader> entry : getClassPathEntries(classloader).entrySet()) {
      scanner.scan(entry.getKey(), entry.getValue());
    }
    return new ClassPath(scanner.getResources());
  }
{
    Scanner scanner = new Scanner();
    for (Map.Entry<File, ClassLoader> entry : getClassPathEntries(classloader).entrySet()) {
      scanner.scan(entry.getKey(), entry.getValue());
    }
    return scanner.getEntries();
  }
{
    if (occurrencesToRemove instanceof Multiset) {
      return removeOccurrences(
          multisetToModify, (Multiset<?>) occurrencesToRemove);
    } else {
      checkNotNull(multisetToModify);
      checkNotNull(occurrencesToRemove);
      boolean changed = false;
      for (Object o : occurrencesToRemove) {
        changed |= multisetToModify.remove(o);
      }
      return changed;
    }
  }
{
    Scanner scanner = new Scanner();
    for (Map.Entry<File, ClassLoader> entry : getClassPathEntries(classloader).entrySet()) {
      scanner.scan(entry.getKey(), entry.getValue());
    }
    return new ClassPath(scanner.getResources());
  }
{
    Scanner scanner = new Scanner();
    for (Map.Entry<File, ClassLoader> entry : getClassPathEntries(classloader).entrySet()) {
      scanner.scan(entry.getKey(), entry.getValue());
    }
    return scanner.getEntries();
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
    assertThat(future.isDone()).isFalse();
    assertThat(future.isCancelled()).isFalse();

    CountingRunnable listener = new CountingRunnable();
    future.addListener(listener, directExecutor());
    listener.assertNotRun();

    verifyGetOnPendingFuture(future);
    verifyTimedGetOnPendingFuture(future);
  }
{
    assertDone(future);
    assertThat(future.isCancelled()).isTrue();
    assertThat(future.wasInterrupted()).isEqualTo(expectWasInterrupted);

    try {
      future.get();
      fail();
    } catch (CancellationException expected) {
    }

    try {
      future.get(0, SECONDS);
      fail();
    } catch (CancellationException expected) {
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
    try {
      encoding.decode(cannotDecode);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException expected) {
      if (expectedMessage != null) {
        assertThat(expected.getCause()).hasMessage(expectedMessage);
      }
    }
    try {
      encoding.decodeChecked(cannotDecode);
      fail("Expected DecodingException");
    } catch (DecodingException expected) {
      if (expectedMessage != null) {
        assertThat(expected).hasMessage(expectedMessage);
      }
    }
  }
{
    for (Type to : toTypes) {
      if (isAssignable(from, to)) {
        return true;
      }
    }
    return false;
  }
{
      /*
       * Our additional cancellation work needs to occur even if
       * !mayInterruptIfRunning, so we can't move it into interruptTask().
       */
      if (super.cancel(mayInterruptIfRunning)) {
        ListenableFuture<? extends I> localInputFuture = inputFuture;
        if (localInputFuture != null) {
          inputFuture.cancel(mayInterruptIfRunning);
        }
        return true;
      }
      return false;
    }
{
      for (Entry<? extends K, ? extends V> entry : entries) {
        put(entry);
      }
      return this;
    }
{
      for (Entry<? extends K, ? extends V> entry : entries) {
        put(entry);
      }
      return this;
    }
{
      int size = entries.size();
      switch (size) {
        case 0:
          return of();
        case 1:
          Entry<K, V> entry = getOnlyElement(entries);
          return of(entry.getKey(), entry.getValue());
        default:
          @SuppressWarnings("unchecked")
          Entry<K, V>[] entryArray
              = entries.toArray(new Entry[entries.size()]);
          return new RegularImmutableMap<K, V>(entryArray);
      }
    }
{
      int size = entries.size();
      switch (size) {
        case 0:
          return of();
        case 1:
          Entry<K, V> entry = getOnlyElement(entries);
          return of(entry.getKey(), entry.getValue());
        default:
          @SuppressWarnings("unchecked")
          Entry<K, V>[] entryArray
              = entries.toArray(new Entry[entries.size()]);
          return new RegularImmutableMap<K, V>(entryArray);
      }
    }
{
    if (key == null) {
      return null;
    }
    int index = Hashing.smear(key.hashCode()) & mask;
    for (ImmutableMapEntry<?, V> entry = keyTable[index];
        entry != null;
        entry = entry.getNextInKeyBucket()) {
      Object candidateKey = entry.getKey();

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
    try {
      return generator.invoke(this, args);
    } catch (InvocationTargetException e) {
      throw Throwables.propagate(e.getCause());
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }
{
    FreshValueGenerator generator = new FreshValueGenerator();
    EqualsTester tester = new EqualsTester();
    for (int i = 0; i < instances; i++) {
      tester.addEqualityGroup(generator.generateFresh(type));
    }
    tester.testEquals();
  }
{
    FreshValueGenerator generator = new FreshValueGenerator();
    assertValueAndTypeEquals(expected, generator.generateFresh(type));
    assertNull(generator.generateFresh(type));
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
    return (timeoutNanos <= 0L) ? 0L
        : (timeoutNanos > (Long.MAX_VALUE / 4) * 3) ? (Long.MAX_VALUE / 4) * 3
        : timeoutNanos;
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
    checkNotNull(strategy);

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
      return new BloomFilter<T>(new BitArray(numBits), numHashFunctions, funnel, strategy);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Could not create BloomFilter of " + numBits + " bits", e);
    }
  }
{
    checkPermits(permits);
    synchronized (mutex) {
      return reserveNextTicket(permits, readSafeMicros());
    }
  }
{
    int utf16Length = sequence.length();
    int utf8Length = 0;
    for (int i = start; i < utf16Length; i++) {
      char c = sequence.charAt(i);
      if (c < 0x800) {
        utf8Length += (0x7f - c) >>> 31; // branch free!
      } else {
        utf8Length += 2;
        // jdk7+: if (Character.isSurrogate(c)) {
        if (Character.MIN_SURROGATE <= c && c <= Character.MAX_SURROGATE) {
          // Check that we have a well-formed surrogate pair.
          int cp = Character.codePointAt(sequence, i);
          if (cp < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
            throw new IllegalArgumentException("Unpaired surrogate at index " + i);
          }
          i++;
        }
      }
    }
    return utf8Length;
  }
{
      ServiceManagerState state = this.state.get();
      if (state != null) {
        state.transitionService(service, NEW, STARTING);
        if (!(service instanceof NoOpService)) {
          logger.log(Level.FINE, "Starting {0}.", service);
        }
      }
    }
{
      ServiceManagerState state = this.state.get();
      if (state != null) {
        state.transitionService(service, STARTING, RUNNING);
      }
    }
{
      ServiceManagerState state = this.state.get();
      if (state != null) {
        state.transitionService(service, from, STOPPING);
      }
    }
{
      ServiceManagerState state = this.state.get();
      if (state != null) {
        if (!(service instanceof NoOpService)) {
          logger.log(Level.FINE, "Service {0} has terminated. Previous state was: {1}", 
              new Object[] {service, from});
        }
        state.transitionService(service, from, TERMINATED);
      }
    }
{
      ServiceManagerState state = this.state.get();
      if (state != null) {
        // Log before the transition, so that if the process exits in response to server failure,
        // there is a higher likelihood that the cause will be in the logs.
        if (!(service instanceof NoOpService)) {
          logger.log(Level.SEVERE, "Service " + service + " has failed in the " + from + " state.",
              failure);
        }
        state.transitionService(service, from, FAILED);
      }
    }
{
      for (final ListenerExecutorPair pair : listeners) {
        queuedListeners.add(new Runnable() {
          @Override public void run() {
            pair.listener.stopped();
          }
        }, pair.executor);
      }
    }
{
      if (maybeTransition(NEW, STARTING)) {
        if (!(service instanceof NoOpService)) {
          logger.log(Level.FINE, "Starting {0}.", service);
        }
      }
    }
{
      ImmutableSetMultimap.Builder<State, Service> builder = ImmutableSetMultimap.builder();
      monitor.enter();
      try {
        for (Entry<State, Service> entry : servicesByState.entries()) {
          if (!(entry.getValue() instanceof NoOpService)) {
            builder.put(entry.getKey(), entry.getValue());
          }
        }
      } finally {
        monitor.leave();
      }
      return builder.build();
    }
{
      List<Entry<Service, Long>> loadTimes;
      monitor.enter();
      try {
        loadTimes = Lists.newArrayListWithCapacity(
            states.size() - states.count(NEW) + states.count(STARTING));
        for (Entry<Service, Stopwatch> entry : startupTimers.entrySet()) {
          Service service = entry.getKey();
          Stopwatch stopWatch = entry.getValue();
          // N.B. we check the service state in the multimap rather than via Service.state() because
          // the multimap is guaranteed to be in sync with our timers while the Service.state() is
          // not.  Due to happens-before ness of the monitor this 'weirdness' will not be observable
          // by our caller.
          if (!stopWatch.isRunning() && !servicesByState.containsEntry(NEW, service) 
              && !(service instanceof NoOpService)) {
            loadTimes.add(Maps.immutableEntry(service, stopWatch.elapsed(MILLISECONDS)));
          }
        }
      } finally {
        monitor.leave();
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
    assertEquals(expected, matcher.toString());
    assertEquals(expected, matcher.precomputed().toString());
    assertEquals(expected, matcher.negate().negate().toString());
    assertEquals(expected, matcher.negate().precomputed().negate().toString());
    assertEquals(expected, matcher.negate().precomputed().negate().precomputed().toString());
   }
{
      int size = entries.size();
      switch (size) {
        case 0:
          return of();
        case 1:
          Entry<K, V> entry = getOnlyElement(entries);
          return of(entry.getKey(), entry.getValue());
        default:
          @SuppressWarnings("unchecked")
          Entry<K, V>[] entryArray
              = entries.toArray(new Entry[entries.size()]);
          return new RegularImmutableMap<K, V>(entryArray);
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
    KNOWN_TYPES.put(mediaType, mediaType);
    return mediaType;
  }
{
    return source;
  }
{
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
      int size = entries.size();
      switch (size) {
        case 0:
          return of();
        case 1:
          Entry<K, V> entry = getOnlyElement(entries);
          return of(entry.getKey(), entry.getValue());
        default:
          @SuppressWarnings("unchecked")
          Entry<K, V>[] entryArray
              = entries.toArray(new Entry[entries.size()]);
          return new RegularImmutableMap<K, V>(entryArray);
      }
    }
{
      return futureValue.setException(t);
    }
{
      Class<?> declaringClass = constructor.getDeclaringClass();
      if (declaringClass.getEnclosingConstructor() != null) {
        // Enclosed in a constructor, needs hidden this
        return true;
      }
      Method enclosingMethod = declaringClass.getEnclosingMethod();
      if (enclosingMethod != null) {
        // Enclosed in a method, if it's not static, must need hidden this.
        return !Modifier.isStatic(enclosingMethod.getModifiers());
      } else {
        // Strictly, this doesn't necessarily indicate a hidden 'this' in the case of
        // static initializer. But there seems no way to tell in that case. :(
        // This may cause issues when an anonymous class is created inside a static initializer,
        // and the class's constructor's first parameter happens to be the enclosing class.
        // In such case, we may mistakenly think that the class is within a non-static context
        // and the first parameter is the hidden 'this'.
        return declaringClass.getEnclosingClass() != null
          && !Modifier.isStatic(declaringClass.getModifiers());
      }
    }
{
    checkNotNull(type);
    final ImmutableSet.Builder<Class<?>> builder = ImmutableSet.builder();
    new TypeVisitor() {
      @Override void visitTypeVariable(TypeVariable<?> t) {
        visit(t.getBounds());
      }
      @Override void visitWildcardType(WildcardType t) {
        visit(t.getUpperBounds());
      }
      @Override void visitParameterizedType(ParameterizedType t) {
        builder.add((Class<?>) t.getRawType());
      }
      @Override void visitClass(Class<?> t) {
        builder.add(t);
      }
      @Override void visitGenericArrayType(GenericArrayType t) {
        builder.add(Types.getArrayClass(getRawType(t.getGenericComponentType())));
      }

    }.visit(type);
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
    return newHasher().putUnencodedChars(input).hash();
  }
{
      for (int i = 0; i < charSequence.length(); i++) {
        putChar(charSequence.charAt(i));
      }
      return this;
    }
{
      int size = entries.size();
      switch (size) {
        case 0:
          return of();
        case 1:
          Entry<K, V> entry = getOnlyElement(entries);
          return of(entry.getKey(), entry.getValue());
        default:
          @SuppressWarnings("unchecked")
          Entry<K, V>[] entryArray
              = entries.toArray(new Entry[entries.size()]);
          return new RegularImmutableMap<K, V>(entryArray);
      }
    }
{
      int size = entries.size();
      switch (size) {
        case 0:
          return of();
        case 1:
          Entry<K, V> entry = getOnlyElement(entries);
          return of(entry.getKey(), entry.getValue());
        default:
          @SuppressWarnings("unchecked")
          Entry<K, V>[] entryArray
              = entries.toArray(new Entry[entries.size()]);
          return new RegularImmutableMap<K, V>(entryArray);
      }
    }
{
      int size = entries.size();
      switch (size) {
        case 0:
          return of();
        case 1:
          Entry<K, V> entry = getOnlyElement(entries);
          return of(entry.getKey(), entry.getValue());
        default:
          @SuppressWarnings("unchecked")
          Entry<K, V>[] entryArray
              = entries.toArray(new Entry[entries.size()]);
          return new RegularImmutableMap<K, V>(entryArray);
      }
    }
{
      int size = entries.size();
      switch (size) {
        case 0:
          return of();
        case 1:
          Entry<K, V> entry = getOnlyElement(entries);
          return of(entry.getKey(), entry.getValue());
        default:
          @SuppressWarnings("unchecked")
          Entry<K, V>[] entryArray
              = entries.toArray(new Entry[entries.size()]);
          return new RegularImmutableMap<K, V>(entryArray);
      }
    }
{
    if (map instanceof ImmutableBiMap) {
      @SuppressWarnings("unchecked") // safe since map is not writable
      ImmutableBiMap<K, V> bimap = (ImmutableBiMap<K, V>) map;
      // TODO(user): if we need to make a copy of a BiMap because the
      // forward map is a view, don't make a copy of the non-view delegate map
      if (!bimap.isPartialView()) {
        return bimap;
      }
    }
    Entry<?, ?>[] entries = map.entrySet().toArray(EMPTY_ENTRY_ARRAY);
    switch (entries.length) {
      case 0:
        return of();
      case 1:
        @SuppressWarnings("unchecked") // safe covariant cast in this context
        Entry<K, V> entry = (Entry<K, V>) entries[0];
        return of(entry.getKey(), entry.getValue());
      default:
        return new RegularImmutableBiMap<K, V>(entries);
    }
  }
{
      int size = entries.size();
      switch (size) {
        case 0:
          return of();
        case 1:
          Entry<K, V> entry = getOnlyElement(entries);
          return of(entry.getKey(), entry.getValue());
        default:
          @SuppressWarnings("unchecked")
          Entry<K, V>[] entryArray
              = entries.toArray(new Entry[entries.size()]);
          return new RegularImmutableMap<K, V>(entryArray);
      }
    }
{
      int size = entries.size();
      switch (size) {
        case 0:
          return of();
        case 1:
          Entry<K, V> entry = getOnlyElement(entries);
          return of(entry.getKey(), entry.getValue());
        default:
          @SuppressWarnings("unchecked")
          Entry<K, V>[] entryArray
              = entries.toArray(new Entry[entries.size()]);
          return new RegularImmutableMap<K, V>(entryArray);
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
    Object[] elements = collection.toArray();
    switch (elements.length) {
      case 0:
        return of();
      case 1:
        @SuppressWarnings("unchecked") // collection had only Es in it
        ImmutableList<E> list = new SingletonImmutableList<E>((E) elements[0]);
        return list;
      default:
        return new RegularImmutableList<E>(ImmutableList.<E>nullCheckedList(elements));
    }
  }
{
    switch (list.size()) {
      case 0:
        return of();
      case 1:
        return new SingletonImmutableList<E>(list.iterator().next());
      default:
        @SuppressWarnings("unchecked")
        List<E> castedList = (List<E>) list;
        return new RegularImmutableList<E>(castedList);
    }
  }
{
    long nanos = elapsedNanos();

    TimeUnit unit = chooseUnit(nanos);
    double value = (double) nanos / NANOSECONDS.convert(1, unit);

    // Too bad this functionality is not exposed as a regular method call
    return String.format("%.4g %s", value, abbreviate(unit));
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
    } catch (DecodingException badInput) {
      throw badInput;
    } catch (IOException impossible) {
      throw new AssertionError(impossible);
    }
    return extract(tmp, index);
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
        checkEntryNotNull(entry.getKey(), entry.getValue());
      }
      @SuppressWarnings("unchecked")
      // immutable collections are safe for covariant casts
      ImmutableMap<K, V> result = ImmutableEnumMap.asImmutable(new EnumMap(enumMap));
      return result;
    }
    Entry<?, ?>[] entries = map.entrySet().toArray(EMPTY_ENTRY_ARRAY);
    switch (entries.length) {
      case 0:
        return of();
      case 1:
        @SuppressWarnings("unchecked") // all entries will be Entry<K, V>'s
        Entry<K, V> onlyEntry = (Entry<K, V>) entries[0];
        return of(onlyEntry.getKey(), onlyEntry.getValue());
      default:
        return new RegularImmutableMap<K, V>(entries);
    }
  }
{
    for (; bucketHead != null; bucketHead = bucketHead.getNextInBucket()) {
      checkNoConflict(!key.equals(bucketHead.getKey()), "key", entry, bucketHead);
    }
  }
{
    return new TerminalMapEntry<K, V>(key, value);
  }
{
      return ImmutableSortedMap.entryOf(key, value);
    }
{
    return new SetFromMap<E>(map);
  }
{
    switch (size) {
      case 0:
        return of();
      case 1:
        @SuppressWarnings("unchecked") // all entries will be Entry<K, V>'s
        Entry<K, V> onlyEntry = (Entry<K, V>) entries[0];
        return new SingletonImmutableBiMap<K, V>(onlyEntry);
      default:
        return new RegularImmutableMap<K, V>(size, entries);
    }
  }
{
    for (int i = 0; i < size; i++) {
      Entry<K, V> entry = entries[i];
      entries[i] = entryOf(entry.getKey(), entry.getValue());
    }
    if (!sameComparator) {
      sortEntries(comparator, size, entries);
      validateEntries(size, entries, comparator);
    }

    return fromSortedEntries(comparator, size, entries);
  }
{
    for (Entry<? extends K, ? extends V> entry : left.entrySet()) {
      K leftKey = entry.getKey();
      V leftValue = entry.getValue();
      if (right.containsKey(leftKey)) {
        V rightValue = onlyOnRight.remove(leftKey);
        if (valueEquivalence.equivalent(leftValue, rightValue)) {
          onBoth.put(leftKey, leftValue);
        } else {
          differences.put(
              leftKey, ValueDifferenceImpl.create(leftValue, rightValue));
        }
      } else {
        onlyOnLeft.put(leftKey, leftValue);
      }
    }
  }
{
    switch (length) {
      case 0:
        return of();
      case 1:
        @SuppressWarnings("unchecked") // collection had only Es in it
        ImmutableList<E> list = new SingletonImmutableList<E>((E) elements[0]);
        return list;
      default:
        if (length < elements.length) {
          elements = arraysCopyOf(elements, length);
        }
        return new RegularImmutableList<E>(elements);
    }
  }
{
    if (n == 0) {
      return emptySet(comparator);
    }
    checkElementsNotNull(contents, n);
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
    return new RegularImmutableSortedSet<E>(
        ImmutableList.<E>asImmutableList(contents, uniques), comparator);
  }
{
    for (int i = 0; i < elements.length; i++) {
      ObjectArrays.checkElementNotNull(elements[i], i);
    }
    return new RegularImmutableList<E>(elements);
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
      return TreeRangeSet.this;
    }
{
    // Compute the filter.
    long filter = 0;
    int size = chars.cardinality();
    boolean containsZero = chars.get(0);
    // Compute the hash table.
    char[] table = new char[chooseTableSize(size)];
    int mask = table.length - 1;
    for (int c = chars.nextSetBit(0); c != -1; c = chars.nextSetBit(c + 1)) {
      // Compute the filter at the same time.
      filter |= 1L << c;
      int index = smear(c) & mask;
      while (true) {
        // Check for empty.
        if (table[index] == 0) {
          table[index] = (char) c;
          break;
        }
        // Linear probing.
        index = (index + 1) & mask;
      }
    }
    return new SmallCharMatcher(table, filter, containsZero, description);
  }
{
    checkNotNull(input);
    checkNotNull(processor);

    byte[] buf = new byte[BUF_SIZE];
    int read;
    do {
      read = input.read(buf);
    } while (read != -1 && processor.processBytes(buf, 0, read));
    return processor.getResult();
  }
{
    int length = chars.length();
    StringBuilder builder = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      builder.append(toLowerCase(chars.charAt(i)));
    }
    return builder.toString();
  }
{
    int length = chars.length();
    StringBuilder builder = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      builder.append(toUpperCase(chars.charAt(i)));
    }
    return builder.toString();
  }
{
    for (int i = start; i < end; i++) {
      char c = sequence.charAt(i);
      if (matches(c)) {
        if (!inMatchingGroup) {
          builder.append(replacement);
          inMatchingGroup = true;
        }
      } else {
        builder.append(c);
        inMatchingGroup = false;
      }
    }
    return builder.toString();
  }
{
      ValueHolder valueHolder = addHolder();
      valueHolder.value = value;
      valueHolder.name = checkNotNull(name);
      return this;
    }
{
    return desiredUnit.convert(elapsedNanos(), NANOSECONDS);
  }
{
    checkNotNull(supplier);
    return new ByteSource() {
      @Override
      public InputStream openStream() throws IOException {
        return supplier.getInput();
      }
    };
  }
{
    checkNotNull(supplier);
    return new ByteSink() {
      @Override
      public OutputStream openStream() throws IOException {
        return supplier.getOutput();
      }
    };
  }
{
    checkNotNull(supplier);
    return new CharSource() {
      @Override
      public Reader openStream() throws IOException {
        return asReader(supplier.getInput());
      }
    };
  }
{
    checkNotNull(supplier);
    return new CharSink() {
      @Override
      public Writer openStream() throws IOException {
        return asWriter(supplier.getOutput());
      }
    };
  }
{
    checkNotNull(domain);
    if (isEmpty()) {
      return ImmutableSortedSet.of();
    }
    Range<C> span = span().canonical(domain);
    if (!span.hasLowerBound()) {
      // according to the spec of canonical, neither this ImmutableRangeSet nor
      // the range have a lower bound
      throw new IllegalArgumentException(
          "Neither the DiscreteDomain nor this range set are bounded below");
    } else if (!span.hasUpperBound()) {
      try {
        domain.maxValue();
      } catch (NoSuchElementException e) {
        throw new IllegalArgumentException(
            "Neither the DiscreteDomain nor this range set are bounded above");
      }
    }

    return new AsSet(domain);
  }
{
    checkNotNull(values);
    Collection<V> result = removeAll(key);
    putAll(key, values);
    return result;
  }
{
    /*
     * As long as the hash function that produced this isn't of horrible quality, this
     * won't be of horrible quality either.
     */
    return asInt();
  }
{
    return new UnsignedInteger(bits);
  }
{
    checkNotNull(readable);
    checkNotNull(processor);

    LineReader lineReader = new LineReader(readable);
    String line;
    while ((line = lineReader.readLine()) != null) {
      if (!processor.processLine(line)) {
        break;
      }
    }
    return processor.getResult();
  }
{
    // TODO(user): consider caching small values, like Long.valueOf
    return new UnsignedLong(bits);
  }
{
    return asInt();
  }
{
    Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntry =
        entriesByLowerBound.floorEntry(Cut.belowValue(key));
    if (mapEntry != null && mapEntry.getValue().contains(key)) {
      return mapEntry.getValue();
    } else {
      return null;
    }
  }
{
    checkNotNull(comparator);

    boolean hasSameComparator
        = fromSortedSet || hasSameComparator(elements, comparator);
    if (hasSameComparator && (elements instanceof ImmutableSortedSet)) {
      @SuppressWarnings("unchecked")
      ImmutableSortedSet<E> result = (ImmutableSortedSet<E>) elements;
      boolean isSubset = (result instanceof RegularImmutableSortedSet)
          && ((RegularImmutableSortedSet) result).isSubset;
      if (!isSubset) {
        // Only return the original copy if this immutable sorted set isn't
        // a subset of another, to avoid memory leak.
        return result;
      }
    }
    return copyOfInternal(comparator, elements.iterator());
  }
{
    checkNotNull(comparator);
    if (!elements.hasNext()) {
      return emptySet(comparator);
    }
    SortedSet<E> delegate = new TreeSet<E>(comparator);
    while (elements.hasNext()) {
      E element = elements.next();
      checkNotNull(element);
      delegate.add(element);
    }
    return new RegularImmutableSortedSet<E>(delegate, false);
  }
{
    // Not completely safe since sample instances are registered by raw types.
    // But we assume the generic type parameters are mostly unimportant for these dummy values,
    // because what really matters are equals/hashCode.
    @SuppressWarnings("unchecked")
    T result = (T) generateIfPossible(type);
    return result;
  }
{
    FreshValueGenerator generator = new FreshValueGenerator();
    T value1 = generator.generate(type);
    T value2 = generator.generate(type);
    assertNotNull("Null returned for " + type, value1);
    assertFalse("Equal instance " + value1 + " returned for " + type, value1.equals(value2));
  }
{
    checkNotNull(elements);
    checkArgument(k >= 0, "k (%d) must be nonnegative", k);

    if (k == 0 || !elements.hasNext()) {
      return ImmutableList.of();
    }

    /*
     * Our goal is an O(n) algorithm using only one pass and O(k) additional
     * memory.
     *
     * We use the following algorithm: maintain a buffer of size 2*k. Every time
     * the buffer gets full, find the median and partition around it, keeping
     * only the lowest k elements.  This requires n/k find-median-and-partition
     * steps, each of which take O(k) time with a traditional quickselect.
     *
     * After sorting the output, the whole algorithm is O(n + k log k). It
     * degrades gracefully for worst-case input (descending order), performs
     * competitively or wins outright for randomly ordered input, and doesn't
     * require the whole collection to fit into memory.
     */
    int bufferCap = k * 2;
    @SuppressWarnings("unchecked") // we'll only put E's in
    E[] buffer = (E[]) new Object[bufferCap];
    E threshold = elements.next();
    buffer[0] = threshold;
    int bufferSize = 1;
    // threshold is the kth smallest element seen so far.  Once bufferSize >= k,
    // anything larger than threshold can be ignored immediately.

    while (bufferSize < k && elements.hasNext()) {
      E e = elements.next();
      buffer[bufferSize++] = e;
      threshold = max(threshold, e);
    }

    while (elements.hasNext()) {
      E e = elements.next();
      if (compare(e, threshold) >= 0) {
        continue;
      }

      buffer[bufferSize++] = e;
      if (bufferSize == bufferCap) {
        // We apply the quickselect algorithm to partition about the median,
        // and then ignore the last k elements.
        int left = 0;
        int right = bufferCap - 1;

        while (left < right) {
          int pivotIndex = (left + right + 1) >>> 1;
          int pivotNewIndex = partition(buffer, left, right, pivotIndex);
          if (pivotNewIndex > k) {
            right = pivotNewIndex - 1;
          } else if (pivotNewIndex < k) {
            left = Math.max(pivotNewIndex, left + 1);
          } else {
            break;
          }
        }
        bufferSize = k;

        threshold = buffer[0];
        for (int i = 1; i < bufferSize; i++) {
          threshold = max(threshold, buffer[i]);
        }
      }
    }

    Arrays.sort(buffer, 0, bufferSize, this);

    bufferSize = Math.min(bufferSize, k);
    return Collections.unmodifiableList(
        Arrays.asList(ObjectArrays.arraysCopyOf(buffer, bufferSize)));
    // We can't use ImmutableList; we have to be null-friendly!
  }
{
    // TODO: when we use @BeforeClass, we can pay the cost of class path scanning only once.
    for (Class<?> classToTest
        : findClassesToTest(loadClassesInPackage(), SERIALIZABLE_TEST_METHOD_NAMES)) {
      if (Serializable.class.isAssignableFrom(classToTest)) {
        try {
          Object instance = tester.instantiate(classToTest);
          if (instance != null) {
            if (isEqualsDefined(classToTest)) {
              SerializableTester.reserializeAndAssert(instance);
            } else {
              SerializableTester.reserialize(instance);
            }
          }
        } catch (Throwable e) {
          throw sanityError(classToTest, SERIALIZABLE_TEST_METHOD_NAMES, "serializable test", e);
        }
      }
    }
  }
{
    List<Class<?>> classes = Lists.newArrayList();
    String packageName = getClass().getPackage().getName();
    for (ClassPath.ClassInfo classInfo 
        : ClassPath.from(getClass().getClassLoader()).getTopLevelClasses(packageName)) {
      Class<?> cls;
      try {
        cls = classInfo.load();
      } catch (NoClassDefFoundError e) {
        // In case there were linking problems, this is probably not a class we care to test anyway.
        logger.log(Level.SEVERE, "Cannot load class " + classInfo + ", skipping...", e);
        continue;
      }
      if (!cls.isInterface()) {
        classes.add(cls);
      }
    }
    return classes;
  }
{
    ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
    final CountDownLatch startLatch = new CountDownLatch(numberOfThreads);
    final CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);
    for (int i = numberOfThreads; i > 0; i--) {
      executorService.submit(new Callable<Void>() {
        @Override
        public Void call() throws Exception {
          startLatch.countDown();
          startLatch.await();
          callable.call();
          doneLatch.countDown();
          return null;
        }
      });
    }
    doneLatch.await();
  }
{
    Method[] methods = type.getMethods();
    for (int i = 0; i < methods.length; i++) {
      try {
        methods[i] = type.getMethod(methods[i].getName(), methods[i].getParameterTypes());
      } catch (Exception e) {
        throw Throwables.propagate(e);
      }
    }
    return methods;
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
    while (keys.hasNext()) {
      K key = keys.next();
      builder.put(key, valueFunction.apply(key));
    }
    return ImmutableMap.copyOf(builder);
  }
{
    // You down with FPP? (Yeah you know me!) Who's down with FPP? (Every last homie!)
    return Math.pow((double) bits.bitCount() / bits.size(), numHashFunctions);
  }
{
    return this;
  }
{
    return this;
  }
{
      k1 *= C1;
      k1 = Long.rotateLeft(k1, 31);
      k1 *= C2;
      return k1;
    }
{
      k2 *= C2;
      k2 = Long.rotateLeft(k2, 33);
      k2 *= C1;
      return k2;
    }
{
    checkNotNull(function);
    return new EntryTransformer<K, V1, V2>() {
      @Override
      public V2 transformEntry(K key, V1 value) {
        return function.apply(value);
      }
    };
  }
{
    state = State.STARTING;
    for (Listener listener : listeners) {
      listener.starting();
    }
  }
{
    state = State.RUNNING;
    for (Listener listener : listeners) {
      listener.running();
    }
  }
{
    state = State.TERMINATED;
    for (Listener listener : listeners) {
      listener.terminated(from);
    }
    // There are no more state transitions so we can clear this out.
    listeners.clear();
  }
{
    failure = cause;
    state = State.FAILED;
    for (Listener listener : listeners) {
      listener.failed(from, cause);
    }
    // There are no more state transitions so we can clear this out.
    listeners.clear();
  }
{
    checkNotNull(iterator);
    checkArgument(numberToAdvance >= 0, "number to advance cannot be negative");

    int i;
    for (i = 0; i < numberToAdvance && iterator.hasNext(); i++) {
      iterator.next();
    }
    return i;
  }
{
    assertEquals(593689054, murmur3_32().hashInt(0).asInt());
    assertEquals(-189366624, murmur3_32().hashInt(-42).asInt());
    assertEquals(-1134849565, murmur3_32().hashInt(42).asInt());
    assertEquals(-1718298732, murmur3_32().hashInt(Integer.MIN_VALUE).asInt());
    assertEquals(-1653689534, murmur3_32().hashInt(Integer.MAX_VALUE).asInt());
  }
{
    assertEquals(0, murmur3_32().hashString("").asInt());
    assertEquals(679745764, murmur3_32().hashString("k").asInt());
    assertEquals(-675079799, murmur3_32().hashString("hello").asInt());
    assertEquals(1935035788, murmur3_32().hashString("http://www.google.com/").asInt());
    assertEquals(-528633700,
        murmur3_32().hashString("The quick brown fox jumps over the lazy dog").asInt());
  }
{
    if (type instanceof Class) {
      return ImmutableSet.<Class<?>>of((Class<?>) type);
    } else if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      // JDK implementation declares getRawType() to return Class<?>
      return ImmutableSet.<Class<?>>of((Class<?>) parameterizedType.getRawType());
    } else if (type instanceof GenericArrayType) {
      GenericArrayType genericArrayType = (GenericArrayType) type;
      return ImmutableSet.<Class<?>>of(Types.getArrayClass(
          getRawType(genericArrayType.getGenericComponentType())));
    } else if (type instanceof TypeVariable) {
      return getRawTypes(((TypeVariable<?>) type).getBounds());
    } else if (type instanceof WildcardType) {
      return getRawTypes(((WildcardType) type).getUpperBounds());
    } else {
      throw new AssertionError(type + " unsupported");
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
    return elementSet;
  }
{
    ASSERT.that(ContiguousSet.create(Ranges.closed(1, 3), integers()).headSet(0)).isEmpty();
  }
{
    ASSERT.that(ContiguousSet.create(Ranges.closed(1, 3), integers()).tailSet(4)).isEmpty();
  }
{
    ASSERT.that(ContiguousSet.create(Ranges.closed(1, 3), integers()).subSet(4, 6)).isEmpty();
  }
{
    ASSERT.that(ContiguousSet.create(Ranges.closed(1, 3), integers()).subSet(-1, 0)).isEmpty();
  }
{
    if (!elements.hasNext()) {
      return of();
    }
    E first = elements.next();
    if (!elements.hasNext()) {
      // TODO: Remove "ImmutableSet.<E>" when eclipse bug is fixed.
      return ImmutableSet.<E>of(first);
    }

    Set<E> delegate = Sets.newLinkedHashSet();
    delegate.add(checkNotNull(first));
    do {
      delegate.add(checkNotNull(elements.next()));
    } while (elements.hasNext());

    return unsafeDelegate(delegate);
  }
{
    checkNotNull(fromKey);
    if (!inclusive) {
      fromKey = higher(fromKey);
      if (fromKey == null) {
        return emptyMap(comparator());
      }
    }
    return tailMap(fromKey);
  }
{
    Integer rowIndex = rowKeyToIndex.get(rowKey);
    Integer columnIndex = columnKeyToIndex.get(columnKey);
    return (rowIndex == null || columnIndex == null)
        ? null : array[rowIndex][columnIndex];
  }
{
    ImmutableMap.Builder<E, Integer> columnBuilder = ImmutableMap.builder();
    for (int i = 0; i < list.size(); i++) {
      columnBuilder.put(list.get(i), i);
    }
    return columnBuilder.build();
  }
{
    try {
      @SuppressWarnings("unchecked")
      E e = (E) element;
      AvlNode<E> root = rootReference.get();
      if (!range.contains(e) || root == null) {
        return 0;
      }
      return root.count(comparator(), e);
    } catch (ClassCastException e) {
      return 0;
    } catch (NullPointerException e) {
      return 0;
    }
  }
{
    switch (list.size()) {
      case 0:
        return of();
      case 1:
        return new SingletonImmutableList<E>(list.iterator().next());
      default:
        @SuppressWarnings("unchecked")
        List<E> castedList = (List<E>) list;
        return new RegularImmutableList<E>(castedList);
    }
  }
{
      checkState(hasMore());
      int startPosition = position;
      position = matcher.negate().indexIn(input, startPosition);
      return hasMore() ? input.substring(startPosition, position) : input.substring(startPosition);
    }
{
    checkNotNull(fromElement);
    checkNotNull(toElement);
    checkArgument(comparator().compare(fromElement, toElement) <= 0);
    return subSetImpl(fromElement, true, toElement, false);
  }
{
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
    return new AbstractIndexedListIterator<T>(length, index) {
      @Override protected T get(int index) {
        return array[offset + index];
      }
    };
  }
{
    for (Constructor<?> constructor : c.getDeclaredConstructors()) {
      if (minimalVisibility.isVisible(constructor) && !isIgnored(constructor)) {
        testConstructor(constructor);
      }
    }
  }
{
    for (Method method : c.getDeclaredMethods()) {
      if (minimalVisibility.isVisible(method)
          && isStatic(method)
          && !isIgnored(method)) {
        testMethod(null, method);
      }
    }
  }
{
    Class<?> c = instance.getClass();
    for (Method method : c.getDeclaredMethods()) {
      if (minimalVisibility.isVisible(method)
          && !isStatic(method)
          && !isIgnored(method)) {
        testMethod(instance, method);
      }
    }
  }
{
    try {
      return InetAddress.getByAddress(addr);
    } catch (UnknownHostException e) {
      throw new AssertionError(e);
    }
  }
{
    checkNotNull(appendable);
    if (parts.hasNext()) {
      appendable.append(toString(parts.next()));
      while (parts.hasNext()) {
        appendable.append(separator);
        appendable.append(toString(parts.next()));
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
      if (parts.hasNext()) {
        Entry<?, ?> entry = parts.next();
        appendable.append(joiner.toString(entry.getKey()));
        appendable.append(keyValueSeparator);
        appendable.append(joiner.toString(entry.getValue()));
        while (parts.hasNext()) {
          appendable.append(joiner.separator);
          Entry<?, ?> e = parts.next();
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

    for (int i = 0; i < iterations; i++) {
      List<Integer> list = Lists.newArrayList();
      for (int j = 0; j < elements; j++) {
        list.add(random.nextInt(10 * i + j + 1));
      }

      for (int seed = 1; seed < seeds; seed++) {
        int k = random.nextInt(10 * seed);
        assertEquals(ordering.sortedCopy(list).subList(0, k),
            ordering.leastOf(list, k));
      }
    }
  }
{
    // let this throw NoSuchElementException as necessary
    E maxSoFar = iterator.next();

    while (iterator.hasNext()) {
      maxSoFar = max(maxSoFar, iterator.next());
    }

    return maxSoFar;
  }
{
    // let this throw NoSuchElementException as necessary
    E minSoFar = iterator.next();

    while (iterator.hasNext()) {
      minSoFar = min(minSoFar, iterator.next());
    }

    return minSoFar;
  }
{
      if (map.refreshes() && (now - entry.getWriteTime() > map.refreshNanos)) {
        V newValue = refresh(key, hash, loader);
        if (newValue != null) {
          return newValue;
        }
      }
      return oldValue;
    }
{
        long startTime = System.nanoTime();
        long ns = millis * 1000 * 1000;
        for (;;) {
            if (millis > 0L)
                Thread.sleep(millis);
            else // too short to sleep
                Thread.yield();
            long d = ns - (System.nanoTime() - startTime);
            if (d > 0L)
                millis = d / (1000 * 1000);
            else
                break;
        }
    }
{
    if (weigher == null) {
      checkState(maximumWeight == UNSET_INT, "maximumWeight requires weigher");
    } else {
      if (strictParsing) {
        checkState(maximumWeight != UNSET_INT, "weigher requires maximumWeight");
      } else {
        if (maximumWeight == UNSET_INT) {
          logger.log(Level.WARNING, "ignoring weigher specified without maximumWeight");
        }
      }
    }
  }
{
    checkState(expireAfterWriteNanos == UNSET_INT, "expireAfterWrite was already set to %s ns",
        expireAfterWriteNanos);
    checkArgument(duration >= 0, "duration cannot be negative: %s %s", duration, unit);
    this.expireAfterWriteNanos = unit.toNanos(duration);
    return this;
  }
{
    checkState(expireAfterAccessNanos == UNSET_INT, "expireAfterAccess was already set to %s ns",
        expireAfterAccessNanos);
    checkArgument(duration >= 0, "duration cannot be negative: %s %s", duration, unit);
    this.expireAfterAccessNanos = unit.toNanos(duration);
    return this;
  }
{
    CustomConcurrentHashMap<?, ?> cchm = toCustomConcurrentHashMap(cache);
    int size = 0;
    for (Segment<?, ?> segment : cchm.segments) {
      size += accessQueueSize(segment);
    }
    return size;
  }
{
    int hash = hash(checkNotNull(key));
    return segmentFor(hash).getOrLoad(key, hash, loader);
  }
{
      ReferenceEntry<K, V> e;
      ValueReference<K, V> valueReference = null;
      LoadingValueReference<K, V> loadingValueReference = null;
      boolean createNewEntry = true;

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
            valueReference = e.getValueReference();
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
      } else {
        // The entry already exists. Wait for loading.
        return waitForLoadingValue(e, valueReference);
      }
    }
{
      if (!valueReference.isLoading()) {
        throw new AssertionError();
      }

      checkState(!Thread.holdsLock(e), "Recursive load");
      // don't consider expiration as we're concurrent with loading
      try {
        V value = valueReference.waitForValue();
        recordRead(e);
        return value;
      } finally {
        statsCounter.recordMiss();
      }
    }
{
      checkNotNull(name);
      return maybeAppendSeparator().append(name).append('=');
    }
{
      lock();
      try {
        preWriteCleanup();

        int newCount = this.count + 1;
        AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
        int index = hash & (table.length() - 1);
        ReferenceEntry<K, V> first = table.get(index);

        for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
          K entryKey = e.getKey();
          if (e.getHash() == hash && entryKey != null
              && map.keyEquivalence.equivalent(key, entryKey)) {
            ValueReference<K, V> valueReference = e.getValueReference();
            V entryValue = valueReference.get();
            if (entryValue == null || oldValueReference == valueReference) {
              ++modCount;
              if (oldValueReference.isActive()) {
                RemovalCause cause =
                    (entryValue == null) ? RemovalCause.COLLECTED : RemovalCause.REPLACED;
                enqueueNotification(key, hash, oldValueReference, cause);
                newCount--;
              }
              setValue(e, key, newValue);
              this.count = newCount; // write-volatile
              evictEntries();
              return true;
            }

            // the loaded value was already clobbered
            valueReference = new WeightedStrongValueReference<K, V>(newValue, 0);
            enqueueNotification(key, hash, valueReference, RemovalCause.REPLACED);
            return false;
          }
        }

        ++modCount;
        ReferenceEntry<K, V> newEntry = newEntry(key, hash, first);
        setValue(newEntry, key, newValue);
        table.set(index, newEntry);
        this.count = newCount; // write-volatile
        evictEntries();
        return true;
      } finally {
        unlock();
        postWriteCleanup();
      }
    }
{
      enqueueNotification(key, hash, valueReference, cause);
      evictionQueue.remove(entry);
      expirationQueue.remove(entry);

      if (valueReference.isLoading()) {
        valueReference.notifyNewValue(null);
        return first;
      } else {
        return removeEntryFromChain(first, entry);
      }
    }
{
    return this;
  }
{
    return this;
  }
{
    if (!sync.cancel()) {
      return false;
    }
    executionList.execute();
    if (mayInterruptIfRunning) {
      interruptTask();
    }
    return true;
  }
{
    boolean result = sync.set(value);
    if (result) {
      executionList.execute();
    }
    return result;
  }
{
    boolean result = sync.setException(checkNotNull(throwable));
    if (result) {
      executionList.execute();
    }

    // If it's an Error, we want to make sure it reaches the top of the
    // call stack, so we rethrow it.
    if (throwable instanceof Error) {
      throw (Error) throwable;
    }
    return result;
  }
{
    Segment<K, V>[] segments = this.segments;
    long sum = 0;
    for (int i = 0; i < segments.length; ++i) {
      sum += segments[i].count;
    }
    return sum;
  }
{
    checkNotNull(left);
    checkNotNull(right);
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
    for (Entry<E2> entry : entries) {
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
    return new RegularImmutableMultiset<E>(builder.build(), Ints.saturatedCast(size));
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
    while (values.hasNext()) {
      V value = values.next();
      builder.put(keyFunction.apply(value), value);
    }
    return builder.build();
  }
{
    checkNotNull(keyFunction);
    ImmutableListMultimap.Builder<K, V> builder
        = ImmutableListMultimap.builder();
    while (values.hasNext()) {
      V value = values.next();
      checkNotNull(value, values);
      builder.put(keyFunction.apply(value), value);
    }
    return builder.build();
  }
{
    checkNonnegative(count, "count");
    while (true) {
      AtomicInteger existingCounter = safeGet(element);
      if (existingCounter == null) {
        if (count == 0) {
          return 0;
        } else {
          existingCounter = countMap.putIfAbsent(element, new AtomicInteger(count));
          if (existingCounter == null) {
            return 0;
          }
          // existingCounter != null: fall through
        }
      }

      while (true) {
        int oldValue = existingCounter.get();
        if (oldValue == 0) {
          if (count == 0) {
            return 0;
          } else {
            AtomicInteger newCounter = new AtomicInteger(count);
            if ((countMap.putIfAbsent(element, newCounter) == null)
                || countMap.replace(element, existingCounter, newCounter)) {
              return 0;
            }
          }
          break;
        } else {
          if (existingCounter.compareAndSet(oldValue, count)) {
            if (count == 0) {
              // Just CASed to 0; remove the entry to clean up the map. If the removal fails,
              // another thread has already replaced it with a new counter, which is fine.
              countMap.remove(element, existingCounter);
            }
            return oldValue;
          }
        }
      }
    }
  }
{
    return comparator;
  }
{
    assert liftOriginalRoot != null & side != null & nodeFactory != null & balancePolicy != null;
    switch (modificationType()) {
      case IDENTITY:
        this.originalRoot = this.changedRoot = liftOriginalRoot;
        return this;
      case REBUILDING_CHANGE:
      case REBALANCING_CHANGE:
        this.originalRoot = liftOriginalRoot;
        N resultLeft = liftOriginalRoot.childOrNull(LEFT);
        N resultRight = liftOriginalRoot.childOrNull(RIGHT);
        switch (side) {
          case LEFT:
            resultLeft = changedRoot;
            break;
          case RIGHT:
            resultRight = changedRoot;
            break;
          default:
            throw new AssertionError();
        }
        if (modificationType() == REBUILDING_CHANGE) {
          this.changedRoot = nodeFactory.createNode(liftOriginalRoot, resultLeft, resultRight);
        } else {
          this.changedRoot =
              balancePolicy.balance(nodeFactory, liftOriginalRoot, resultLeft, resultRight);
        }
        return this;
      default:
        throw new AssertionError();
    }
  }
{
    BstBalancePolicy<N> rebalancePolicy = mutationRule.getBalancePolicy();
    BstNodeFactory<N> nodeFactory = mutationRule.getNodeFactory();
    BstModifier<K, N> modifier = mutationRule.getModifier();

    N originalRoot = tree;
    N changedRoot;
    N originalTarget = (tree == null) ? null : nodeFactory.createLeaf(tree);
    BstModificationResult<N> modResult = modifier.modify(key, originalTarget);
    N originalLeft = null;
    N originalRight = null;
    if (tree != null) {
      originalLeft = tree.childOrNull(LEFT);
      originalRight = tree.childOrNull(RIGHT);
    }
    switch (modResult.getType()) {
      case IDENTITY:
        changedRoot = tree;
        break;
      case REBUILDING_CHANGE:
        if (modResult.getChangedTarget() != null) {
          changedRoot =
              nodeFactory.createNode(modResult.getChangedTarget(), originalLeft, originalRight);
        } else if (tree == null) {
          changedRoot = null;
        } else {
          throw new AssertionError(
              "Modification result is a REBUILDING_CHANGE, but rebalancing required");
        }
        break;
      case REBALANCING_CHANGE:
        if (modResult.getChangedTarget() != null) {
          changedRoot = rebalancePolicy.balance(
              nodeFactory, modResult.getChangedTarget(), originalLeft, originalRight);
        } else if (tree != null) {
          changedRoot = rebalancePolicy.combine(nodeFactory, originalLeft, originalRight);
        } else {
          changedRoot = null;
        }
        break;
      default:
        throw new AssertionError();
    }
    return BstMutationResult.mutationResult(key, originalRoot, changedRoot, modResult);
  }
{
    if (cause instanceof Error) {
      throw new ExecutionError((Error) cause);
    }
    if (cause instanceof RuntimeException) {
      throw new UncheckedExecutionException((RuntimeException) cause);
    }
    throw newWithCause(exceptionClass, cause);
  }
{
    Preconditions.checkNotNull(equivalence);

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
        if (equivalence.equivalent(leftValue, rightValue)) {
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
    Preconditions.checkNotNull(ip);
    if (ip instanceof Inet4Address) {
      // For IPv4, Java's formatting is good enough.
      return ip.getHostAddress();
    }
    Preconditions.checkArgument(ip instanceof Inet6Address);
    byte[] bytes = ip.getAddress();
    int[] hextets = new int[IPV6_PART_COUNT];
    for (int i = 0; i < hextets.length; i++) {
      hextets[i] = Ints.fromBytes(
          (byte) 0, (byte) 0, bytes[2 * i], bytes[2 * i + 1]);
    }
    compressLongestRunOfZeroes(hextets);
    return hextetsToIPv6String(hextets);
  }
{
    return new InternetDomainName(checkNotNull(domain));
  }
{
    try {
      from(name);
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
    int cmp = comparator().compare(fromElement, toElement);
    checkArgument(cmp <= 0, "fromElement (%s) is less than toElement (%s)", fromElement, toElement);
    if (cmp == 0 && !(fromInclusive && toInclusive)) {
      return emptySet(comparator());
    }
    return tailSet(fromElement, fromInclusive).headSet(toElement, toInclusive);
  }
{
    checkNotNull(fromElement);
    checkNotNull(toElement);
    checkArgument(comparator().compare(fromElement, toElement) <= 0);
    return subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
  }
{
      ReferenceEntry<K, V> e = getEntry(key, hash);
      if (e == null) {
        return null;
      } else if (map.expires() && map.isExpired(e)) {
        tryExpireEntries();
        return null;
      }
      return e;
    }
{
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
{
      if (entry.getKey() == null) {
        return true;
      }
      return isCollected(entry.getValueReference());
    }
{
    return this;
  }
{
    return this;
  }
{
      int newCount = this.count - 1;
      AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
      int index = hash & (table.length() - 1);
      ReferenceEntry<K, V> first = table.get(index);

      for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
        if (e == entry) {
          ++modCount;
          enqueueNotification(e.getKey(), hash, e.getValueReference().get(), cause);
          ReferenceEntry<K, V> newFirst = removeFromChain(first, e);
          table.set(index, newFirst);
          this.count = newCount; // write-volatile
          return true;
        }
      }

      return false;
    }
{
      enqueueNotification(entry.getKey(), entry.getHash(), entry.getValueReference().get(), cause);
    }
{
    return useNullCache()
        ? new ComputingMapAdapter<K, V>(this, CACHE_STATS_COUNTER, loader)
        : new NullComputingConcurrentMap<K, V>(this, loader);
  }
{
      checkNotNull(oldValue);
      checkNotNull(newValue);
      lock();
      try {
        preWriteCleanup();

        // getFirst, but remember the index
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
              removeLiveEntry(table, index, first, e, hash, valueReference);
              return false;
            }

            if (map.valueEquivalence.equivalent(oldValue, entryValue)) {
              ++modCount;
              setValue(e, newValue);
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
      checkNotNull(newValue);
      lock();
      try {
        preWriteCleanup();

        // getFirst, but remember the index
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
              removeLiveEntry(table, index, first, e, hash, valueReference);
              return null;
            }

            ++modCount;
            setValue(e, newValue);
            return entryValue;
          }
        }

        return null;
      } finally {
        unlock();
        postWriteCleanup();
      }
    }
{
      lock();
      try {
        preWriteCleanup();

        int newCount = this.count - 1;
        // getFirst, but remember the index
        AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
        int index = hash & (table.length() - 1);
        ReferenceEntry<K, V> first = table.get(index);

        for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
          K entryKey = e.getKey();
          if (e.getHash() == hash && entryKey != null
              && map.keyEquivalence.equivalent(key, entryKey)) {
            ValueReference<K, V> valueReference = e.getValueReference();
            V entryValue = valueReference.get();
            if (entryValue == null) {
              removeLiveEntry(table, index, first, e, hash, valueReference);
            } else {
              ++modCount;
              ReferenceEntry<K, V> newFirst = removeFromChain(first, e); // decrements count
              newCount = this.count - 1;
              table.set(index, newFirst);
              this.count = newCount; // write-volatile
            }
            return entryValue;
          }
        }

        return null;
      } finally {
        unlock();
        postWriteCleanup();
      }
    }
{
      checkNotNull(value);
      lock();
      try {
        preWriteCleanup();

        int newCount = this.count - 1;
        // getFirst, but remember the index
        AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
        int index = hash & (table.length() - 1);
        ReferenceEntry<K, V> first = table.get(index);

        for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
          K entryKey = e.getKey();
          if (e.getHash() == hash && entryKey != null
              && map.keyEquivalence.equivalent(key, entryKey)) {
            ValueReference<K, V> valueReference = e.getValueReference();
            V entryValue = valueReference.get();
            if (entryValue == null) {
              removeLiveEntry(table, index, first, e, hash, valueReference);
            } else if (map.valueEquivalence.equivalent(value, entryValue)) {
              ++modCount;
              ReferenceEntry<K, V> newFirst = removeFromChain(first, e); // decrements count
              newCount = this.count - 1;
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
      if (valueReference.isComputingReference()) {
        return false;
      }

      ReferenceEntry<K, V> newFirst = removeFromChain(first, entry);
      table.set(index, newFirst);
      clearLiveEntry(entry, hash, valueReference);
      return true;
    }
{
      try {
        outer: while (true) {
          ReferenceEntry<K, V> e = getEntry(key, hash);
          if (e != null) {
            V value = getLiveValue(e);
            if (value != null) {
              // TODO(user): recordHit
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
                      // TODO(user): recordHit
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
                return value;
              } finally {
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
            // TODO(user): recordMiss
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
    if (!elements.hasNext()) {
      return of();
    }
    E first = elements.next();
    if (!elements.hasNext()) {
      // TODO: Remove "ImmutableSet.<E>" when eclipse bug is fixed.
      return ImmutableSet.<E>of(first);
    }

    Set<E> delegate = Sets.newLinkedHashSet();
    delegate.add(checkNotNull(first));
    do {
      delegate.add(checkNotNull(elements.next()));
    } while (elements.hasNext());

    return unsafeDelegate(delegate);
  }
{
    if (!elements.hasNext()) {
      return of();
    }
    E first = elements.next();
    if (!elements.hasNext()) {
      // TODO: Remove "ImmutableSet.<E>" when eclipse bug is fixed.
      return ImmutableSet.<E>of(first);
    }

    Set<E> delegate = Sets.newLinkedHashSet();
    delegate.add(checkNotNull(first));
    do {
      delegate.add(checkNotNull(elements.next()));
    } while (elements.hasNext());

    return unsafeDelegate(delegate);
  }
{
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
      if (tryLock()) {
        try {
          expireEntries();
        } finally {
          unlock();
        }
      }
    }
{
      if (evictsBySize() && count >= maxSegmentSize) {
        drainRecencyQueue();

        ReferenceEntry<K, V> e = evictionQueue.remove();
        if (!unsetEntry(e, e.getHash())) {
          throw new AssertionError();
        }
        return true;
      }
      return false;
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
      return transform((ListenableFuture<I>) future, function);
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
      expireEntries();
      // perform inline cleanup while under lock
      scheduleCleanup();
    }
{
      if (isUnset(entry)) {
        // keep count consistent
        return false;
      }

      int newCount = this.count - 1;
      ++modCount;
      K key = entry.getKey();
      ValueReference<K, V> valueReference = entry.getValueReference();
      enqueueNotification(key, hash, valueReference);
      enqueueCleanup(entry);
      this.count = newCount; // write-volatile
      return true;
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
      if (evictsBySize() && count >= maxSegmentSize) {
        evictEntry();
        return true;
      }
      return false;
    }
{
      return valueReference == UNSET;
    }
{
    checkNotNull(multimap); // eager for GWT
    if (multimap.isEmpty() && valueComparator == null) {
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
      ImmutableSet<V> set = (valueComparator == null)
          ? ImmutableSet.copyOf(values) 
          : ImmutableSortedSet.copyOf(valueComparator, values);
      if (!set.isEmpty()) {
        builder.put(key, set);
        size += set.size();
      }
    }

    return new ImmutableSetMultimap<K, V>(
        builder.build(), size, valueComparator);
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
    if (!elements.hasNext()) {
      return of();
    }
    E first = elements.next();
    if (!elements.hasNext()) {
      // TODO: Remove "ImmutableSet.<E>" when eclipse bug is fixed.
      return ImmutableSet.<E>of(first);
    }
    Set<E> delegate = Sets.newLinkedHashSet();
    delegate.add(checkNotNull(first));
    do {
      delegate.add(checkNotNull(elements.next()));
    } while (elements.hasNext());
    return new RegularImmutableSet<E>(delegate);
  }
{
    boolean hasSameComparator
        = fromSortedSet || hasSameComparator(elements, comparator);

    if (hasSameComparator && (elements instanceof ImmutableSortedSet)) {
      @SuppressWarnings("unchecked")
      ImmutableSortedSet<E> original = (ImmutableSortedSet<E>) elements;
      if (original.isEmpty()) {
        return original;
      }
      ImmutableList<E> elementsList = original.asList();
      ImmutableList<E> copiedElementsList = ImmutableList.copyOf(elements);
      if (elementsList == copiedElementsList) {
        return original;
      }
      return new RegularImmutableSortedSet<E>(copiedElementsList, comparator);
    }

    ImmutableList<E> list =
        immutableSortedUniqueCopy(comparator, Lists.newArrayList(elements));
    if (list.isEmpty()) {
      return emptySet(comparator);
    }
    return new RegularImmutableSortedSet<E>(list, comparator);
  }
{
    return false;
  }
{
    return false;
  }
{
      return multimap.isPartialView();
    }
{
    return map.isPartialView();
  }
{
    return false;
  }
{
    return false;
  }
{
    return false;
  }
{
    return fromIndex != 0 || toIndex != elements.length;
  }
{
      return false;
    }
{
      return false;
    }
{
    return false;
  }
{
      return false;
    }
{
    return fromIndex != 0 || toIndex != entries.length;   
  }
{
    return delegate.isPartialView() || inverse.delegate().isPartialView();
  }
{
    return offset != 0 || size != array.length;
  }
{
    return false;
  }
{
    return fromIndex != 0 || toIndex != elements.length;
  }
{
    return false;
  }
{
    return false;
  }
{
    return false;
  }
{
    if (elements instanceof ImmutableSet
        && !(elements instanceof ImmutableSortedSet)) {
      @SuppressWarnings("unchecked") // all supported methods are covariant
      ImmutableSet<E> set = (ImmutableSet<E>) elements;
      return set;
    }
    return copyOf(elements.iterator());
  }
{
    if (!elements.hasNext()) {
      return of();
    }
    E first = elements.next();
    if (!elements.hasNext()) {
      // TODO: Remove "ImmutableSet.<E>" when eclipse bug is fixed.
      return ImmutableSet.<E>of(first);
    }
    Set<E> delegate = Sets.newLinkedHashSet();
    delegate.add(checkNotNull(first));
    do {
      delegate.add(checkNotNull(elements.next()));
    } while (elements.hasNext());
    return new RegularImmutableSet<E>(delegate);
  }
{
    // Hack around K not being a subtype of Comparable.
    // Unsafe, see ImmutableSortedSetFauxverideShim.
    @SuppressWarnings("unchecked")
    Ordering<E> naturalOrder = (Ordering) Ordering.<Comparable>natural();
    return copyOfInternal(naturalOrder, elements, false);
  }
{
    checkNotNull(comparator);
    return copyOfInternal(comparator, elements, false);
  }
{
    if (!elements.hasNext()) {
      return of();
    }
    E first = elements.next();
    if (!elements.hasNext()) {
      // TODO: Remove "ImmutableSet.<E>" when eclipse bug is fixed.
      return ImmutableSet.<E>of(first);
    }
    Set<E> delegate = Sets.newLinkedHashSet();
    delegate.add(checkNotNull(first));
    do {
      delegate.add(checkNotNull(elements.next()));
    } while (elements.hasNext());
    return new RegularImmutableSet<E>(delegate);
  }
{
    if (elements instanceof ImmutableSet
        && !(elements instanceof ImmutableSortedSet)) {
      @SuppressWarnings("unchecked") // all supported methods are covariant
      ImmutableSet<E> set = (ImmutableSet<E>) elements;
      return set;
    }
    return copyOf(elements.iterator());
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
    for (int i = 0; i < elements.length; i++) {
      checkElementNotNull(elements[i], i);
    }
    return new RegularImmutableList<E>(elements);
  }
{
    return (char) ((b1 << 8) | (b2 & 0xFF));
  }
{
    return b1 << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8 | (b4 & 0xFF);
  }
{
    return (b1 & 0xFFL) << 56
        | (b2 & 0xFFL) << 48
        | (b3 & 0xFFL) << 40
        | (b4 & 0xFFL) << 32
        | (b5 & 0xFFL) << 24
        | (b6 & 0xFFL) << 16
        | (b7 & 0xFFL) << 8
        | (b8 & 0xFFL);
  }
{
    return (short) ((b1 << 8) | (b2 & 0xFF));
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
    return delegate.equals(obj);
  }
{
    return publicSuffixIndex == 0;
  }
{
    return publicSuffixIndex != NO_PUBLIC_SUFFIX_FOUND;
  }
{
    return publicSuffixIndex > 0;
  }
{
    return publicSuffixIndex == 1;
  }
{
    if (isTopPrivateDomain()) {
      return this;
    }
    checkState(isUnderPublicSuffix(), "Not under a public suffix: %s", name);
    return ancestor(publicSuffixIndex - 1);
  }
{
    int index = pos;
    for (Object element : source) {
      if (element == null) {
        throw new NullPointerException("at index " + index);
      }
      dest[index++] = element;
    }
    return dest;
  }
{
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
    if (position < 0) {
      throw new IndexOutOfBoundsException(
          "position cannot be negative: " + position);
    }
  }
{
    return list.get(list.size() - 1);
  }
{
    if (position < 0) {
      throw new IndexOutOfBoundsException("position (" + position
          + ") must not be negative");
    }
  }
{
    return getClass().getSimpleName();
  }
{
    return getClass().getSimpleName();
  }
{
    checkNotNull(elements);
    // TODO: Once the ImmutableAsList and ImmutableSortedAsList are
    // GWT-compatible, return elements.asList() when elements is an
    // ImmutableCollection.
    if (elements instanceof ImmutableList) {
      /*
       * TODO: When given an ImmutableList that's a sublist, copy the referenced
       * portion of the array into a new array to save space?
       */
      @SuppressWarnings("unchecked") // all supported methods are covariant
      ImmutableList<E> list = (ImmutableList<E>) elements;
      return list;
    }
    return copyFromCollection(elements);
  }
{
    Function<I, ListenableFuture<O>> wrapperFunction
        = new Function<I, ListenableFuture<O>>() {
            /*@Override*/ public ListenableFuture<O> apply(I input) {
              O output = function.apply(input);
              return immediateFuture(output);
            }
        };
    return chain(future, wrapperFunction, exec);
  }

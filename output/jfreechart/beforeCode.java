add(double x, double y, double deltaX, double deltaY) {
        super.add(new VectorDataItem(x, y, deltaX, deltaY), true);
    }
add(double x, double xLow, double xHigh, double y) {
        super.add(new XIntervalDataItem(x, xLow, xHigh, y), true);
    }
add(double x, double xLow, double xHigh, double y, double yLow,
            double yHigh) {
        super.add(new XYIntervalDataItem(x, xLow, xHigh, y, yLow, yHigh), true);
    }
add(double x, double y, double yLow, double yHigh) {
        super.add(new YIntervalDataItem(x, y, yLow, yHigh), true);
    }
darker(TexturePaint paint,
            boolean ignoreThisDummyArgument) {
        try {
            return darker(paint);
        }
        catch (Exception e) {
            /*
             * Lots can go wrong while fiddling with Images, Colour Models
             * & such!  If anything at all goes awry, just return the original
             * TexturePaint.  (TexturePaint's are immutable anyway, so no harm
             * done)
             */
            return paint;
        }
    }
fetchLegendItems() {
        this.items.clear();
        RectangleEdge p = getPosition();
        if (RectangleEdge.isTopOrBottom(p)) {
            this.items.setArrangement(this.hLayout);
        }
        else {
            this.items.setArrangement(this.vLayout);
        }
        for (int s = 0; s < this.sources.length; s++) {
            LegendItemCollection legendItems = this.sources[s].getLegendItems();
            if (legendItems != null) {
                for (int i = 0; i < legendItems.getItemCount(); i++) {
                    LegendItem item = legendItems.get(i);
                    Block block = createLegendItemBlock(item);
                    this.items.add(block);
                }
            }
        }
    }
DefaultPlotEditor(Plot plot) {
        this.plotInsets = plot.getInsets();
        this.backgroundPaintSample = new PaintSample(plot.getBackgroundPaint());
        this.outlineStrokeSample = new StrokeSample(plot.getOutlineStroke());
        this.outlinePaintSample = new PaintSample(plot.getOutlinePaint());
        if (plot instanceof CategoryPlot) {
            this.plotOrientation = ((CategoryPlot) plot).getOrientation();
        }
        else if (plot instanceof XYPlot) {
            this.plotOrientation = ((XYPlot) plot).getOrientation();
        }
        if (plot instanceof CategoryPlot) {
            CategoryItemRenderer renderer = ((CategoryPlot) plot).getRenderer();
            if (renderer instanceof LineAndShapeRenderer) {
                LineAndShapeRenderer r = (LineAndShapeRenderer) renderer;
                this.drawLines = BooleanUtilities.valueOf(
                        r.getBaseLinesVisible());
                this.drawShapes = BooleanUtilities.valueOf(
                        r.getBaseShapesVisible());
            }
        }
        else if (plot instanceof XYPlot) {
            XYItemRenderer renderer = ((XYPlot) plot).getRenderer();
            if (renderer instanceof StandardXYItemRenderer) {
                StandardXYItemRenderer r = (StandardXYItemRenderer) renderer;
                this.drawLines = BooleanUtilities.valueOf(r.getPlotLines());
                this.drawShapes = BooleanUtilities.valueOf(
                        r.getBaseShapesVisible());
            }
        }

        setLayout(new BorderLayout());

        this.availableStrokeSamples = new StrokeSample[4];
        this.availableStrokeSamples[0] = new StrokeSample(null);
        this.availableStrokeSamples[1] = new StrokeSample(
                new BasicStroke(1.0f));
        this.availableStrokeSamples[2] = new StrokeSample(
                new BasicStroke(2.0f));
        this.availableStrokeSamples[3] = new StrokeSample(
                new BasicStroke(3.0f));

        // create a panel for the settings...
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), plot.getPlotType()
                + localizationResources.getString(":")));

        JPanel general = new JPanel(new BorderLayout());
        general.setBorder(BorderFactory.createTitledBorder(
                localizationResources.getString("General")));

        JPanel interior = new JPanel(new LCBLayout(7));
        interior.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

//        interior.add(new JLabel(localizationResources.getString("Insets")));
//        JButton button = new JButton(
//            localizationResources.getString("Edit...")
//        );
//        button.setActionCommand("Insets");
//        button.addActionListener(this);
//
//        this.insetsTextField = new InsetsTextField(this.plotInsets);
//        this.insetsTextField.setEnabled(false);
//        interior.add(this.insetsTextField);
//        interior.add(button);

        interior.add(new JLabel(localizationResources.getString(
                "Outline_stroke")));
        JButton button = new JButton(localizationResources.getString(
                "Select..."));
        button.setActionCommand("OutlineStroke");
        button.addActionListener(this);
        interior.add(this.outlineStrokeSample);
        interior.add(button);

        interior.add(new JLabel(localizationResources.getString(
                "Outline_Paint")));
        button = new JButton(localizationResources.getString("Select..."));
        button.setActionCommand("OutlinePaint");
        button.addActionListener(this);
        interior.add(this.outlinePaintSample);
        interior.add(button);

        interior.add(new JLabel(localizationResources.getString(
                "Background_paint")));
        button = new JButton(localizationResources.getString("Select..."));
        button.setActionCommand("BackgroundPaint");
        button.addActionListener(this);
        interior.add(this.backgroundPaintSample);
        interior.add(button);

        if (this.plotOrientation != null) {
            boolean isVertical = this.plotOrientation.equals(
                    PlotOrientation.VERTICAL);
            int index = isVertical ? ORIENTATION_VERTICAL
                    : ORIENTATION_HORIZONTAL;
            interior.add(new JLabel(localizationResources.getString(
                    "Orientation")));
            this.orientationCombo = new JComboBox(orientationNames);
            this.orientationCombo.setSelectedIndex(index);
            this.orientationCombo.setActionCommand("Orientation");
            this.orientationCombo.addActionListener(this);
            interior.add(new JPanel());
            interior.add(this.orientationCombo);
        }

        if (this.drawLines != null) {
            interior.add(new JLabel(localizationResources.getString(
                    "Draw_lines")));
            this.drawLinesCheckBox = new JCheckBox();
            this.drawLinesCheckBox.setSelected(this.drawLines.booleanValue());
            this.drawLinesCheckBox.setActionCommand("DrawLines");
            this.drawLinesCheckBox.addActionListener(this);
            interior.add(new JPanel());
            interior.add(this.drawLinesCheckBox);
        }

        if (this.drawShapes != null) {
            interior.add(new JLabel(localizationResources.getString(
                    "Draw_shapes")));
            this.drawShapesCheckBox = new JCheckBox();
            this.drawShapesCheckBox.setSelected(this.drawShapes.booleanValue());
            this.drawShapesCheckBox.setActionCommand("DrawShapes");
            this.drawShapesCheckBox.addActionListener(this);
            interior.add(new JPanel());
            interior.add(this.drawShapesCheckBox);
        }

        general.add(interior, BorderLayout.NORTH);

        JPanel appearance = new JPanel(new BorderLayout());
        appearance.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        appearance.add(general, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        Axis domainAxis = null;
        if (plot instanceof CategoryPlot) {
            domainAxis = ((CategoryPlot) plot).getDomainAxis();
        }
        else if (plot instanceof XYPlot) {
            domainAxis = ((XYPlot) plot).getDomainAxis();
        }
        this.domainAxisPropertyPanel = DefaultAxisEditor.getInstance(
                domainAxis);
        if (this.domainAxisPropertyPanel != null) {
            this.domainAxisPropertyPanel.setBorder(
                    BorderFactory.createEmptyBorder(2, 2, 2, 2));
            tabs.add(localizationResources.getString("Domain_Axis"),
                    this.domainAxisPropertyPanel);
        }

        Axis rangeAxis = null;
        if (plot instanceof CategoryPlot) {
            rangeAxis = ((CategoryPlot) plot).getRangeAxis();
        }
        else if (plot instanceof XYPlot) {
            rangeAxis = ((XYPlot) plot).getRangeAxis();
        }

        this.rangeAxisPropertyPanel = DefaultAxisEditor.getInstance(rangeAxis);
        if (this.rangeAxisPropertyPanel != null) {
            this.rangeAxisPropertyPanel.setBorder(
                    BorderFactory.createEmptyBorder(2, 2, 2, 2));
            tabs.add(localizationResources.getString("Range_Axis"),
                    this.rangeAxisPropertyPanel);
        }

//dmo: added this panel for colorbar control. (start dmo additions)
        ColorBar colorBar = null;
        if (plot instanceof ContourPlot) {
            colorBar = ((ContourPlot) plot).getColorBar();
        }

        this.colorBarAxisPropertyPanel = DefaultColorBarEditor.getInstance(
                colorBar);
        if (this.colorBarAxisPropertyPanel != null) {
            this.colorBarAxisPropertyPanel.setBorder(
                    BorderFactory.createEmptyBorder(2, 2, 2, 2));
            tabs.add(localizationResources.getString("Color_Bar"),
                    this.colorBarAxisPropertyPanel);
        }
//dmo: (end dmo additions)

        tabs.add(localizationResources.getString("Appearance"), appearance);
        panel.add(tabs);

        add(panel);
    }
refreshAngleTicks() {
        List ticks = new ArrayList();
        for (double currentTickVal = 0.0; currentTickVal < 360.0;
                currentTickVal += this.angleTickUnit.getSize()) {
            TextAnchor ta = TextAnchor.CENTER;
            if (currentTickVal == 0.0 || currentTickVal == 360.0) {
                ta = TextAnchor.BOTTOM_CENTER;
            }
            else if (currentTickVal > 0.0 && currentTickVal < 90.0) {
                ta = TextAnchor.BOTTOM_LEFT;
            }
            else if (currentTickVal == 90.0) {
                ta = TextAnchor.CENTER_LEFT;
            }
            else if (currentTickVal > 90.0 && currentTickVal < 180.0) {
                ta = TextAnchor.TOP_LEFT;
            }
            else if (currentTickVal == 180) {
                ta = TextAnchor.TOP_CENTER;
            }
            else if (currentTickVal > 180.0 && currentTickVal < 270.0) {
                ta = TextAnchor.TOP_RIGHT;
            }
            else if (currentTickVal == 270) {
                ta = TextAnchor.CENTER_RIGHT;
            }
            else if (currentTickVal > 270.0 && currentTickVal < 360.0) {
                ta = TextAnchor.BOTTOM_RIGHT;
            }

            NumberTick tick = new NumberTick(new Double(currentTickVal),
                this.angleTickUnit.valueToString(currentTickVal),
                ta, TextAnchor.CENTER, 0.0);
            ticks.add(tick);
        }
        return ticks;
    }
setDataset(XYDataset dataset) {
        // if there is an existing dataset, remove the plot from the list of
        // change listeners...
        XYDataset existing = this.dataset;
        if (existing != null) {
            existing.removeChangeListener(this);
        }

        // set the new m_Dataset, and register the chart as a change listener...
        this.dataset = dataset;
        if (this.dataset != null) {
            setDatasetGroup(this.dataset.getGroup());
            this.dataset.addChangeListener(this);
        }

        // send a m_Dataset change event to self...
        DatasetChangeEvent event = new DatasetChangeEvent(this, this.dataset);
        datasetChanged(event);
    }
addOrUpdate(Number x, Number y) {
        if (x == null) {
            throw new IllegalArgumentException("Null 'x' argument.");
        }
        if (this.allowDuplicateXValues) {
            add(x, y);
            return null;
        }

        // if we get to here, we know that duplicate X values are not permitted
        XYDataItem overwritten = null;
        int index = indexOf(x);
        if (index >= 0) {
            XYDataItem existing = (XYDataItem) this.data.get(index);
            try {
                overwritten = (XYDataItem) existing.clone();
            }
            catch (CloneNotSupportedException e) {
                throw new SeriesException("Couldn't clone XYDataItem!");
            }
            // figure out if we need to iterate through all the y-values
            boolean iterate = false;
            double oldY = existing.getYValue();
            if (!Double.isNaN(oldY)) {
                iterate = oldY <= this.minY || oldY >= this.maxY;
            }
            existing.setY(y);

            if (iterate) {
                findBoundsByIteration();
            }
            else if (y != null) {
                double yy = y.doubleValue();
                this.minY = minIgnoreNaN(this.minY, yy);
                this.maxY = minIgnoreNaN(this.maxY, yy);
            }
        }
        else {
            // if the series is sorted, the negative index is a result from
            // Collections.binarySearch() and tells us where to insert the
            // new item...otherwise it will be just -1 and we should just
            // append the value to the list...
            XYDataItem item = new XYDataItem(x, y);
            if (this.autoSort) {
                this.data.add(-index - 1, item);
            }
            else {
                this.data.add(item);
            }
            updateBoundsForAddedItem(item);

            // check if this addition will exceed the maximum item count...
            if (getItemCount() > this.maximumItemCount) {
                XYDataItem removed = (XYDataItem) this.data.remove(0);
                updateBoundsForRemovedItem(removed);
            }
        }
        fireSeriesChanged();
        return overwritten;
    }
addOrUpdate(RegularTimePeriod period,
                                          Number value) {

        if (period == null) {
            throw new IllegalArgumentException("Null 'period' argument.");
        }
        if (this.timePeriodClass == null) {
            this.timePeriodClass = period.getClass();
        }
        else if (!this.timePeriodClass.equals(period.getClass())) {
            String msg = "You are trying to add data where the time "
                    + "period class is " + period.getClass().getName()
                    + ", but the TimeSeries is expecting an instance of "
                    + this.timePeriodClass.getName() + ".";
            throw new SeriesException(msg);
        }
        TimeSeriesDataItem overwritten = null;

        TimeSeriesDataItem key = new TimeSeriesDataItem(period, value);
        int index = Collections.binarySearch(this.data, key);
        if (index >= 0) {
            TimeSeriesDataItem existing
                    = (TimeSeriesDataItem) this.data.get(index);
            // figure out if we need to iterate through all the y-values
            // to find the revised minY / maxY
            boolean iterate = false;
            Number oldYN = existing.getValue();
            double oldY = oldYN != null ? oldYN.doubleValue() : Double.NaN;
            if (!Double.isNaN(oldY)) {
                iterate = oldY <= this.minY || oldY >= this.maxY;
            }
            existing.setValue(value);
            if (iterate) {
                findBoundsByIteration();
            }
            else if (value != null) {
                double yy = value.doubleValue();
                this.minY = minIgnoreNaN(this.minY, yy);
                this.maxY = minIgnoreNaN(this.maxY, yy);
            }
            overwritten = (TimeSeriesDataItem) existing.clone();
        }
        else {
            TimeSeriesDataItem item = new TimeSeriesDataItem(period, value);
            this.data.add(-index - 1, item);
            updateBoundsForAddedItem(item);

            // check if this addition will exceed the maximum item count...
            if (getItemCount() > this.maximumItemCount) {
                TimeSeriesDataItem d = (TimeSeriesDataItem) this.data.remove(0);
                updateBoundsForRemovedItem(d);
            }
        }
        removeAgedItems(false);  // remove old items if necessary, but
                                 // don't notify anyone, because that
                                 // happens next anyway...
        fireSeriesChanged();
        return overwritten;

    }
delete(int start, int end) {
        if (end < start) {
            throw new IllegalArgumentException("Requires start <= end.");
        }
        for (int i = 0; i <= (end - start); i++) {
            this.data.remove(start);
        }
        if (this.data.isEmpty()) {
            this.timePeriodClass = null;
        }
        fireSeriesChanged();
    }
createPopupMenu(boolean properties,
                                         boolean save,
                                         boolean print,
                                         boolean zoom) {

        JPopupMenu result = new JPopupMenu("Chart:");
        boolean separator = false;

        if (properties) {
            JMenuItem propertiesItem = new JMenuItem(
                    localizationResources.getString("Properties..."));
            propertiesItem.setActionCommand(PROPERTIES_COMMAND);
            propertiesItem.addActionListener(this);
            result.add(propertiesItem);
            separator = true;
        }

        if (save) {
            if (separator) {
                result.addSeparator();
                separator = false;
            }
            JMenuItem saveItem = new JMenuItem(
                    localizationResources.getString("Save_as..."));
            saveItem.setActionCommand(SAVE_COMMAND);
            saveItem.addActionListener(this);
            result.add(saveItem);
            separator = true;
        }

        if (print) {
            if (separator) {
                result.addSeparator();
                separator = false;
            }
            JMenuItem printItem = new JMenuItem(
                    localizationResources.getString("Print..."));
            printItem.setActionCommand(PRINT_COMMAND);
            printItem.addActionListener(this);
            result.add(printItem);
            separator = true;
        }

        if (zoom) {
            if (separator) {
                result.addSeparator();
                separator = false;
            }

            JMenu zoomInMenu = new JMenu(
                    localizationResources.getString("Zoom_In"));

            this.zoomInBothMenuItem = new JMenuItem(
                    localizationResources.getString("All_Axes"));
            this.zoomInBothMenuItem.setActionCommand(ZOOM_IN_BOTH_COMMAND);
            this.zoomInBothMenuItem.addActionListener(this);
            zoomInMenu.add(this.zoomInBothMenuItem);

            zoomInMenu.addSeparator();

            this.zoomInDomainMenuItem = new JMenuItem(
                    localizationResources.getString("Domain_Axis"));
            this.zoomInDomainMenuItem.setActionCommand(ZOOM_IN_DOMAIN_COMMAND);
            this.zoomInDomainMenuItem.addActionListener(this);
            zoomInMenu.add(this.zoomInDomainMenuItem);

            this.zoomInRangeMenuItem = new JMenuItem(
                    localizationResources.getString("Range_Axis"));
            this.zoomInRangeMenuItem.setActionCommand(ZOOM_IN_RANGE_COMMAND);
            this.zoomInRangeMenuItem.addActionListener(this);
            zoomInMenu.add(this.zoomInRangeMenuItem);

            result.add(zoomInMenu);

            JMenu zoomOutMenu = new JMenu(
                    localizationResources.getString("Zoom_Out"));

            this.zoomOutBothMenuItem = new JMenuItem(
                    localizationResources.getString("All_Axes"));
            this.zoomOutBothMenuItem.setActionCommand(ZOOM_OUT_BOTH_COMMAND);
            this.zoomOutBothMenuItem.addActionListener(this);
            zoomOutMenu.add(this.zoomOutBothMenuItem);

            zoomOutMenu.addSeparator();

            this.zoomOutDomainMenuItem = new JMenuItem(
                    localizationResources.getString("Domain_Axis"));
            this.zoomOutDomainMenuItem.setActionCommand(
                    ZOOM_OUT_DOMAIN_COMMAND);
            this.zoomOutDomainMenuItem.addActionListener(this);
            zoomOutMenu.add(this.zoomOutDomainMenuItem);

            this.zoomOutRangeMenuItem = new JMenuItem(
                    localizationResources.getString("Range_Axis"));
            this.zoomOutRangeMenuItem.setActionCommand(ZOOM_OUT_RANGE_COMMAND);
            this.zoomOutRangeMenuItem.addActionListener(this);
            zoomOutMenu.add(this.zoomOutRangeMenuItem);

            result.add(zoomOutMenu);

            JMenu autoRangeMenu = new JMenu(
                    localizationResources.getString("Auto_Range"));

            this.zoomResetBothMenuItem = new JMenuItem(
                    localizationResources.getString("All_Axes"));
            this.zoomResetBothMenuItem.setActionCommand(
                    ZOOM_RESET_BOTH_COMMAND);
            this.zoomResetBothMenuItem.addActionListener(this);
            autoRangeMenu.add(this.zoomResetBothMenuItem);

            autoRangeMenu.addSeparator();
            this.zoomResetDomainMenuItem = new JMenuItem(
                    localizationResources.getString("Domain_Axis"));
            this.zoomResetDomainMenuItem.setActionCommand(
                    ZOOM_RESET_DOMAIN_COMMAND);
            this.zoomResetDomainMenuItem.addActionListener(this);
            autoRangeMenu.add(this.zoomResetDomainMenuItem);

            this.zoomResetRangeMenuItem = new JMenuItem(
                    localizationResources.getString("Range_Axis"));
            this.zoomResetRangeMenuItem.setActionCommand(
                    ZOOM_RESET_RANGE_COMMAND);
            this.zoomResetRangeMenuItem.addActionListener(this);
            autoRangeMenu.add(this.zoomResetRangeMenuItem);

            result.addSeparator();
            result.add(autoRangeMenu);

        }

        return result;

    }
findRangeBounds(CategoryDataset dataset) {
        if (dataset == null) {
            return null;
        }
        return DatasetUtilities.findRangeBounds(dataset);
    }
findDomainBounds(XYDataset dataset) {
        if (dataset != null) {
            return DatasetUtilities.findDomainBounds(dataset, false);
        }
        else {
            return null;
        }
    }
findRangeBounds(XYDataset dataset) {
        if (dataset != null) {
            return DatasetUtilities.findRangeBounds(dataset, false);
        }
        else {
            return null;
        }
    }
createInstance(Date millisecond, TimeZone zone) {
        RegularTimePeriod result = null;
        try {
            Constructor c = this.periodClass.getDeclaredConstructor(
                    new Class[] {Date.class, TimeZone.class});
            result = (RegularTimePeriod) c.newInstance(new Object[] {
                    millisecond, zone});
        }
        catch (Exception e) {
            // do nothing
        }
        return result;
    }
sampleFunction2D(Function2D f, double start,
            double end, int samples, Comparable seriesKey) {

        if (f == null) {
            throw new IllegalArgumentException("Null 'f' argument.");
        }
        if (seriesKey == null) {
            throw new IllegalArgumentException("Null 'seriesKey' argument.");
        }
        if (start >= end) {
            throw new IllegalArgumentException("Requires 'start' < 'end'.");
        }
        if (samples < 2) {
            throw new IllegalArgumentException("Requires 'samples' > 1");
        }

        XYSeries series = new XYSeries(seriesKey);
        double step = (end - start) / (samples - 1);
        for (int i = 0; i < samples; i++) {
            double x = start + (step * i);
            series.add(x, f.getValue(x));
        }
        XYSeriesCollection collection = new XYSeriesCollection(series);
        return collection;
    }
createStackedValueList(CategoryDataset dataset,
            Comparable category, double base, boolean asPercentages) {

        List result = new ArrayList();
        double posBase = base;
        double negBase = base;
        double total = 0.0;
        if (asPercentages) {
            total = DataUtilities.calculateColumnTotal(dataset,
                    dataset.getColumnIndex(category));
        }

        int baseIndex = -1;
        int seriesCount = dataset.getRowCount();
        for (int s = 0; s < seriesCount; s++) {
            Number n = dataset.getValue(dataset.getRowKey(s), category);
            if (n == null) {
                continue;
            }
            double v = n.doubleValue();
            if (asPercentages) {
                v = v / total;
            }
            if (v >= 0.0) {
                if (baseIndex < 0) {
                    result.add(new Object[] {null, new Double(base)});
                    baseIndex = 0;
                }
                posBase = posBase + v;
                result.add(new Object[] {new Integer(s), new Double(posBase)});
            }
            else if (v < 0.0) {
                if (baseIndex < 0) {
                    result.add(new Object[] {null, new Double(base)});
                    baseIndex = 0;
                }
                negBase = negBase + v; // '+' because v is negative
                result.add(0, new Object[] {new Integer(-s - 1),
                        new Double(negBase)});
                baseIndex++;
            }
        }
        return result;

    }
refreshTicksHorizontal(Graphics2D g2,
                Rectangle2D dataArea, RectangleEdge edge) {

        List result = new java.util.ArrayList();

        Font tickLabelFont = getTickLabelFont();
        g2.setFont(tickLabelFont);

        if (isAutoTickUnitSelection()) {
            selectAutoTickUnit(g2, dataArea, edge);
        }

        DateTickUnit unit = getTickUnit();
        Date tickDate = calculateLowestVisibleTickValue(unit);
        Date upperDate = getMaximumDate();

        while (tickDate.before(upperDate)) {
            long lowestTickTime = tickDate.getTime();
            long distance = unit.addToDate(tickDate, this.timeZone).getTime()
                    - lowestTickTime;
            for(int minorTick = 1; minorTick < getMinorTickCount();
                    minorTick++) {
                long minorTickTime = lowestTickTime - distance
                        * minorTick / getMinorTickCount();
                if (minorTickTime > 0 && getRange().contains(minorTickTime)
                        && (!isHiddenValue(minorTickTime))) {
                    result.add(new DateTick(TickType.MINOR,
                            new Date(minorTickTime), "", TextAnchor.TOP_CENTER,
                            TextAnchor.CENTER, 0.0));
                }
            }

            if (!isHiddenValue(tickDate.getTime())) {
                // work out the value, label and position
                String tickLabel;
                DateFormat formatter = getDateFormatOverride();
                if (formatter != null) {
                    tickLabel = formatter.format(tickDate);
                }
                else {
                    tickLabel = this.tickUnit.dateToString(tickDate);
                }
                TextAnchor anchor = null;
                TextAnchor rotationAnchor = null;
                double angle = 0.0;
                if (isVerticalTickLabels()) {
                    anchor = TextAnchor.CENTER_RIGHT;
                    rotationAnchor = TextAnchor.CENTER_RIGHT;
                    if (edge == RectangleEdge.TOP) {
                        angle = Math.PI / 2.0;
                    }
                    else {
                        angle = -Math.PI / 2.0;
                    }
                }
                else {
                    if (edge == RectangleEdge.TOP) {
                        anchor = TextAnchor.BOTTOM_CENTER;
                        rotationAnchor = TextAnchor.BOTTOM_CENTER;
                    }
                    else {
                        anchor = TextAnchor.TOP_CENTER;
                        rotationAnchor = TextAnchor.TOP_CENTER;
                    }
                }

                Tick tick = new DateTick(tickDate, tickLabel, anchor,
                        rotationAnchor, angle);
                result.add(tick);

                long currentTickTime = tickDate.getTime();
                tickDate = unit.addToDate(tickDate, this.timeZone);
                long nextTickTime = tickDate.getTime();
                for (int minorTick = 1; minorTick < getMinorTickCount();
                        minorTick++){
                    long minorTickTime = currentTickTime
                            + (nextTickTime - currentTickTime)
                            * minorTick / getMinorTickCount();
                    if (getRange().contains(minorTickTime)
                            && (!isHiddenValue(minorTickTime))) {
                        result.add(new DateTick(TickType.MINOR,
                                new Date(minorTickTime), "",
                                TextAnchor.TOP_CENTER, TextAnchor.CENTER,
                                0.0));
                    }
                }

            }
            else {
                tickDate = unit.rollDate(tickDate, this.timeZone);
                continue;
            }

            // could add a flag to make the following correction optional...
            switch (unit.getUnit()) {

                case (DateTickUnit.MILLISECOND) :
                case (DateTickUnit.SECOND) :
                case (DateTickUnit.MINUTE) :
                case (DateTickUnit.HOUR) :
                case (DateTickUnit.DAY) :
                    break;
                case (DateTickUnit.MONTH) :
                    // FIXME:  the following month needs a locale
                    tickDate = calculateDateForPosition(new Month(tickDate,
                            this.timeZone), this.tickMarkPosition);
                    break;
                case(DateTickUnit.YEAR) :
                    // FIXME:  the following year needs a locale
                    tickDate = calculateDateForPosition(new Year(tickDate,
                            this.timeZone), this.tickMarkPosition);
                    break;

                default: break;

            }

        }
        return result;

    }
mapDatasetToDomainAxis(int index, int axisIndex) {
        this.datasetToDomainAxisMap.set(index, new Integer(axisIndex));
        // fake a dataset change event to update axes...
        datasetChanged(new DatasetChangeEvent(this, getDataset(index)));
    }
mapDatasetToRangeAxis(int index, int axisIndex) {
        this.datasetToRangeAxisMap.set(index, new Integer(axisIndex));
        // fake a dataset change event to update axes...
        datasetChanged(new DatasetChangeEvent(this, getDataset(index)));
    }
mapDatasetToDomainAxis(int index, int axisIndex) {
        this.datasetToDomainAxisMap.put(new Integer(index),
                new Integer(axisIndex));
        // fake a dataset change event to update axes...
        datasetChanged(new DatasetChangeEvent(this, getDataset(index)));
    }
mapDatasetToRangeAxis(int index, int axisIndex) {
        this.datasetToRangeAxisMap.put(new Integer(index),
                new Integer(axisIndex));
        // fake a dataset change event to update axes...
        datasetChanged(new DatasetChangeEvent(this, getDataset(index)));
    }
setVisible(boolean flag) {
        if (flag != this.visible) {
            this.visible = flag;
            notifyListeners(new AxisChangeEvent(this));
        }
    }
createStandardDateTickUnits(TimeZone zone) {

        if (zone == null) {
            throw new IllegalArgumentException("Null 'zone' argument.");
        }
        TickUnits units = new TickUnits();

        // date formatters
        DateFormat f1 = new SimpleDateFormat("HH:mm:ss.SSS");
        DateFormat f2 = new SimpleDateFormat("HH:mm:ss");
        DateFormat f3 = new SimpleDateFormat("HH:mm");
        DateFormat f4 = new SimpleDateFormat("d-MMM, HH:mm");
        DateFormat f5 = new SimpleDateFormat("d-MMM");
        DateFormat f6 = new SimpleDateFormat("MMM-yyyy");
        DateFormat f7 = new SimpleDateFormat("yyyy");

        f1.setTimeZone(zone);
        f2.setTimeZone(zone);
        f3.setTimeZone(zone);
        f4.setTimeZone(zone);
        f5.setTimeZone(zone);
        f6.setTimeZone(zone);
        f7.setTimeZone(zone);

        // milliseconds
        units.add(new DateTickUnit(DateTickUnit.MILLISECOND, 1, f1));
        units.add(new DateTickUnit(DateTickUnit.MILLISECOND, 5,
                DateTickUnit.MILLISECOND, 1, f1));
        units.add(new DateTickUnit(DateTickUnit.MILLISECOND, 10,
                DateTickUnit.MILLISECOND, 1, f1));
        units.add(new DateTickUnit(DateTickUnit.MILLISECOND, 25,
                DateTickUnit.MILLISECOND, 5, f1));
        units.add(new DateTickUnit(DateTickUnit.MILLISECOND, 50,
                DateTickUnit.MILLISECOND, 10, f1));
        units.add(new DateTickUnit(DateTickUnit.MILLISECOND, 100,
                DateTickUnit.MILLISECOND, 10, f1));
        units.add(new DateTickUnit(DateTickUnit.MILLISECOND, 250,
                DateTickUnit.MILLISECOND, 10, f1));
        units.add(new DateTickUnit(DateTickUnit.MILLISECOND, 500,
                DateTickUnit.MILLISECOND, 50, f1));

        // seconds
        units.add(new DateTickUnit(DateTickUnit.SECOND, 1,
                DateTickUnit.MILLISECOND, 50, f2));
        units.add(new DateTickUnit(DateTickUnit.SECOND, 5,
                DateTickUnit.SECOND, 1, f2));
        units.add(new DateTickUnit(DateTickUnit.SECOND, 10,
                DateTickUnit.SECOND, 1, f2));
        units.add(new DateTickUnit(DateTickUnit.SECOND, 30,
                DateTickUnit.SECOND, 5, f2));

        // minutes
        units.add(new DateTickUnit(DateTickUnit.MINUTE, 1,
                DateTickUnit.SECOND, 5, f3));
        units.add(new DateTickUnit(DateTickUnit.MINUTE, 2,
                DateTickUnit.SECOND, 10, f3));
        units.add(new DateTickUnit(DateTickUnit.MINUTE, 5,
                DateTickUnit.MINUTE, 1, f3));
        units.add(new DateTickUnit(DateTickUnit.MINUTE, 10,
                DateTickUnit.MINUTE, 1, f3));
        units.add(new DateTickUnit(DateTickUnit.MINUTE, 15,
                DateTickUnit.MINUTE, 5, f3));
        units.add(new DateTickUnit(DateTickUnit.MINUTE, 20,
                DateTickUnit.MINUTE, 5, f3));
        units.add(new DateTickUnit(DateTickUnit.MINUTE, 30,
                DateTickUnit.MINUTE, 5, f3));

        // hours
        units.add(new DateTickUnit(DateTickUnit.HOUR, 1,
                DateTickUnit.MINUTE, 5, f3));
        units.add(new DateTickUnit(DateTickUnit.HOUR, 2,
                DateTickUnit.MINUTE, 10, f3));
        units.add(new DateTickUnit(DateTickUnit.HOUR, 4,
                DateTickUnit.MINUTE, 30, f3));
        units.add(new DateTickUnit(DateTickUnit.HOUR, 6,
                DateTickUnit.HOUR, 1, f3));
        units.add(new DateTickUnit(DateTickUnit.HOUR, 12,
                DateTickUnit.HOUR, 1, f4));

        // days
        units.add(new DateTickUnit(DateTickUnit.DAY, 1,
                DateTickUnit.HOUR, 1, f5));
        units.add(new DateTickUnit(DateTickUnit.DAY, 2,
                DateTickUnit.HOUR, 1, f5));
        units.add(new DateTickUnit(DateTickUnit.DAY, 7,
                DateTickUnit.DAY, 1, f5));
        units.add(new DateTickUnit(DateTickUnit.DAY, 15,
                DateTickUnit.DAY, 1, f5));

        // months
        units.add(new DateTickUnit(DateTickUnit.MONTH, 1,
                DateTickUnit.DAY, 1, f6));
        units.add(new DateTickUnit(DateTickUnit.MONTH, 2,
                DateTickUnit.DAY, 1, f6));
        units.add(new DateTickUnit(DateTickUnit.MONTH, 3,
                DateTickUnit.MONTH, 1, f6));
        units.add(new DateTickUnit(DateTickUnit.MONTH, 4,
                DateTickUnit.MONTH, 1, f6));
        units.add(new DateTickUnit(DateTickUnit.MONTH, 6,
                DateTickUnit.MONTH, 1, f6));

        // years
        units.add(new DateTickUnit(DateTickUnit.YEAR, 1,
                DateTickUnit.MONTH, 1, f7));
        units.add(new DateTickUnit(DateTickUnit.YEAR, 2,
                DateTickUnit.MONTH, 3, f7));
        units.add(new DateTickUnit(DateTickUnit.YEAR, 5,
                DateTickUnit.YEAR, 1, f7));
        units.add(new DateTickUnit(DateTickUnit.YEAR, 10,
                DateTickUnit.YEAR, 1, f7));
        units.add(new DateTickUnit(DateTickUnit.YEAR, 25,
                DateTickUnit.YEAR, 5, f7));
        units.add(new DateTickUnit(DateTickUnit.YEAR, 50,
                DateTickUnit.YEAR, 10, f7));
        units.add(new DateTickUnit(DateTickUnit.YEAR, 100,
                DateTickUnit.YEAR, 20, f7));

        return units;

    }
setLabelInsets(RectangleInsets insets) {
        if (insets == null) {
            throw new IllegalArgumentException("Null 'insets' argument.");
        }
        if (!insets.equals(this.labelInsets)) {
            this.labelInsets = insets;
            notifyListeners(new AxisChangeEvent(this));
        }
    }
zoomRangeAxes(double factor, PlotRenderingInfo info, 
                              Point2D source) {
        // delegate 'info' and 'source' argument checks...
        CategoryPlot subplot = findSubplot(info, source);
        if (subplot != null) {
            subplot.zoomRangeAxes(factor, info, source);
        }
        else {
            // if the source point doesn't fall within a subplot, we do the
            // zoom on all subplots...
            Iterator iterator = getSubplots().iterator();
            while (iterator.hasNext()) {
                subplot = (CategoryPlot) iterator.next();
                subplot.zoomRangeAxes(factor, info, source);
            }
        }
    }
zoomRangeAxes(double factor, PlotRenderingInfo info, 
                              Point2D source) {
        // delegate 'info' and 'source' argument checks...
        XYPlot subplot = findSubplot(info, source);
        if (subplot != null) {
            subplot.zoomRangeAxes(factor, info, source);
        }
        else {
            // if the source point doesn't fall within a subplot, we do the
            // zoom on all subplots...
            Iterator iterator = getSubplots().iterator();
            while (iterator.hasNext()) {
                subplot = (XYPlot) iterator.next();
                subplot.zoomRangeAxes(factor, info, source);
            }
        }
    }
zoomDomainAxes(double factor, PlotRenderingInfo info, 
                               Point2D source) {
        // delegate 'info' and 'source' argument checks...
        XYPlot subplot = findSubplot(info, source);
        if (subplot != null) {
            subplot.zoomDomainAxes(factor, info, source);
        }
        else {
            // if the source point doesn't fall within a subplot, we do the
            // zoom on all subplots...
            Iterator iterator = getSubplots().iterator();
            while (iterator.hasNext()) {
                subplot = (XYPlot) iterator.next();
                subplot.zoomDomainAxes(factor, info, source);
            }
        }
    }
iterateCategoryRangeBounds(CategoryDataset dataset, 
            boolean includeInterval) {
        double minimum = Double.POSITIVE_INFINITY;
        double maximum = Double.NEGATIVE_INFINITY;
        boolean interval = includeInterval 
                           && dataset instanceof IntervalCategoryDataset;
        int rowCount = dataset.getRowCount();
        int columnCount = dataset.getColumnCount();
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                Number lvalue;
                Number uvalue;
                if (interval) {
                    IntervalCategoryDataset icd 
                        = (IntervalCategoryDataset) dataset;
                    lvalue = icd.getStartValue(row, column);
                    uvalue = icd.getEndValue(row, column);
                }
                else {
                    lvalue = dataset.getValue(row, column);
                    uvalue = lvalue;
                }
                if (lvalue != null) {
                    minimum = Math.min(minimum, lvalue.doubleValue());
                }
                if (uvalue != null) {
                    maximum = Math.max(maximum, uvalue.doubleValue());
                }
            }
        }
        if (minimum == Double.POSITIVE_INFINITY) {
            return null;
        }
        else {
            return new Range(minimum, maximum);
        }
    }
findRangeBounds(XYDataset dataset, 
                                        boolean includeInterval) {
        if (dataset == null) {
            throw new IllegalArgumentException("Null 'dataset' argument.");
        }
        Range result = null;
        if (dataset instanceof RangeInfo) {
            RangeInfo info = (RangeInfo) dataset;
            result = info.getRangeBounds(includeInterval);
        }
        else {
            result = iterateXYRangeBounds(dataset);
        }
        return result;
    }
drawHorizontalItem(Graphics2D g2, 
                                   Rectangle2D dataArea,
                                   PlotRenderingInfo info,
                                   XYPlot plot, 
                                   ValueAxis domainAxis, 
                                   ValueAxis rangeAxis,
                                   XYDataset dataset, 
                                   int series, 
                                   int item,
                                   CrosshairState crosshairState,
                                   int pass) {

        // setup for collecting optional entity info...
        EntityCollection entities = null;
        if (info != null) {
            entities = info.getOwner().getEntityCollection();
        }

        BoxAndWhiskerXYDataset boxAndWhiskerData 
                = (BoxAndWhiskerXYDataset) dataset;

        Number x = boxAndWhiskerData.getX(series, item);
        Number yMax = boxAndWhiskerData.getMaxRegularValue(series, item);
        Number yMin = boxAndWhiskerData.getMinRegularValue(series, item);
        Number yMedian = boxAndWhiskerData.getMedianValue(series, item);
        Number yAverage = boxAndWhiskerData.getMeanValue(series, item);
        Number yQ1Median = boxAndWhiskerData.getQ1Value(series, item);
        Number yQ3Median = boxAndWhiskerData.getQ3Value(series, item);
        
        double xx = domainAxis.valueToJava2D(x.doubleValue(), dataArea, 
                plot.getDomainAxisEdge());

        RectangleEdge location = plot.getRangeAxisEdge();
        double yyMax = rangeAxis.valueToJava2D(yMax.doubleValue(), dataArea, 
                location);
        double yyMin = rangeAxis.valueToJava2D(yMin.doubleValue(), dataArea, 
                location);
        double yyMedian = rangeAxis.valueToJava2D(yMedian.doubleValue(), 
                dataArea, location);
        double yyAverage = 0.0;
        if (yAverage != null) {
            yyAverage = rangeAxis.valueToJava2D(yAverage.doubleValue(), 
                    dataArea, location);
        }
        double yyQ1Median = rangeAxis.valueToJava2D(yQ1Median.doubleValue(), 
                dataArea, location);
        double yyQ3Median = rangeAxis.valueToJava2D(yQ3Median.doubleValue(), 
                dataArea, location);
        
        double exactBoxWidth = getBoxWidth();
        double width = exactBoxWidth;
        double dataAreaX = dataArea.getHeight();
        double maxBoxPercent = 0.1;
        double maxBoxWidth = dataAreaX * maxBoxPercent;
        if (exactBoxWidth <= 0.0) {
            int itemCount = boxAndWhiskerData.getItemCount(series);
            exactBoxWidth = dataAreaX / itemCount * 4.5 / 7;
            if (exactBoxWidth < 3) {
                width = 3;
            }
            else if (exactBoxWidth > maxBoxWidth) {
                width = maxBoxWidth;
            }
            else {
                width = exactBoxWidth;
            }
        }

        Paint p = getBoxPaint();
        if (p != null) {
            g2.setPaint(p);
        }
        Stroke s = getItemStroke(series, item);
        g2.setStroke(s);

        // draw the upper shadow
        g2.draw(new Line2D.Double(yyMax, xx, yyQ3Median, xx));
        g2.draw(new Line2D.Double(yyMax, xx - width / 2, yyMax, 
                xx + width / 2));

        // draw the lower shadow
        g2.draw(new Line2D.Double(yyMin, xx, yyQ1Median, xx));
        g2.draw(new Line2D.Double(yyMin, xx - width / 2, yyMin, 
                xx + width / 2));

        // draw the body
        Shape box = null;
        if (yyQ1Median < yyQ3Median) {
            box = new Rectangle2D.Double(yyQ1Median, xx - width / 2, 
                    yyQ3Median - yyQ1Median, width);
        }
        else {
            box = new Rectangle2D.Double(yyQ3Median, xx - width / 2, 
                    yyQ1Median - yyQ3Median, width);
        }
        if (getBoxPaint() != null) {
            g2.setPaint(getBoxPaint());
        }
        if (this.fillBox) {
            g2.fill(box);   
        }
        g2.draw(box);

        // draw median
        g2.setPaint(getArtifactPaint());
        g2.draw(new Line2D.Double(yyMedian, 
                xx - width / 2, yyMedian, xx + width / 2));
        
        // draw average - SPECIAL AIMS REQUIREMENT
        if (yAverage != null) {
            double aRadius = width / 4;
            // here we check that the average marker will in fact be visible
            // before drawing it...
            if ((yyAverage > (dataArea.getMinX() - aRadius)) 
                    && (yyAverage < (dataArea.getMaxX() + aRadius))) {
                Ellipse2D.Double avgEllipse = new Ellipse2D.Double(
                        yyAverage - aRadius, xx - aRadius, aRadius * 2, 
                        aRadius * 2);
                g2.fill(avgEllipse);
                g2.draw(avgEllipse);
            }
        }
        
        // FIXME: draw outliers
        
        // add an entity for the item...
        if (entities != null && box.intersects(dataArea)) {
            addEntity(entities, box, dataset, series, item, yyAverage, xx);
        }

    }
setNoDataMessage(String message) {
        this.noDataMessage = message;
        notifyListeners(new PlotChangeEvent(this));
    }
addDomainMarker(int index, CategoryMarker marker, Layer layer) {
        if (marker == null) {
            throw new IllegalArgumentException("Null 'marker' not permitted.");
        }
        if (layer == null) {
            throw new IllegalArgumentException("Null 'layer' not permitted.");
        }
        Collection markers;
        if (layer == Layer.FOREGROUND) {
            markers = (Collection) this.foregroundDomainMarkers.get(
                    new Integer(index));
            if (markers == null) {
                markers = new java.util.ArrayList();
                this.foregroundDomainMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        }
        else if (layer == Layer.BACKGROUND) {
            markers = (Collection) this.backgroundDomainMarkers.get(
                    new Integer(index));
            if (markers == null) {
                markers = new java.util.ArrayList();
                this.backgroundDomainMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);            
        }
        marker.addChangeListener(this);
        notifyListeners(new PlotChangeEvent(this));
    }
removeDomainMarker(int index, Marker marker, Layer layer) {
        ArrayList markers;
        if (layer == Layer.FOREGROUND) {
            markers = (ArrayList) this.foregroundDomainMarkers.get(new Integer(
                    index));
        }
        else {
            markers = (ArrayList) this.backgroundDomainMarkers.get(new Integer(
                    index));
        }
        boolean removed = markers.remove(marker);
        if (removed) {
            notifyListeners(new PlotChangeEvent(this));
        }
        return removed;
    }
addRangeMarker(int index, Marker marker, Layer layer) {
        Collection markers;
        if (layer == Layer.FOREGROUND) {
            markers = (Collection) this.foregroundRangeMarkers.get(
                    new Integer(index));
            if (markers == null) {
                markers = new java.util.ArrayList();
                this.foregroundRangeMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        }
        else if (layer == Layer.BACKGROUND) {
            markers = (Collection) this.backgroundRangeMarkers.get(
                    new Integer(index));
            if (markers == null) {
                markers = new java.util.ArrayList();
                this.backgroundRangeMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);            
        }
        marker.addChangeListener(this);
        notifyListeners(new PlotChangeEvent(this));
    }
removeRangeMarker(int index, Marker marker, Layer layer) {
        if (marker == null) {
            throw new IllegalArgumentException("Null 'marker' argument.");
        }
        ArrayList markers;
        if (layer == Layer.FOREGROUND) {
            markers = (ArrayList) this.foregroundRangeMarkers.get(new Integer(
                    index));
        }
        else {
            markers = (ArrayList) this.backgroundRangeMarkers.get(new Integer(
                    index));
        }

        boolean removed = markers.remove(marker);
        if (removed) {
            notifyListeners(new PlotChangeEvent(this));
        }
        return removed;
    }
addAnnotation(CategoryAnnotation annotation) {
        if (annotation == null) {
            throw new IllegalArgumentException("Null 'annotation' argument.");
        }
        this.annotations.add(annotation);
        notifyListeners(new PlotChangeEvent(this));
    }
removeAnnotation(CategoryAnnotation annotation) {
        if (annotation == null) {
            throw new IllegalArgumentException("Null 'annotation' argument.");
        }
        boolean removed = this.annotations.remove(annotation);
        if (removed) {
            notifyListeners(new PlotChangeEvent(this));
        }
        return removed;
    }
addDomainMarker(int index, Marker marker, Layer layer) {
        if (marker == null) {
            throw new IllegalArgumentException("Null 'marker' not permitted.");
        }
        if (layer == null) {
            throw new IllegalArgumentException("Null 'layer' not permitted.");
        }
        Collection markers;
        if (layer == Layer.FOREGROUND) {
            markers = (Collection) this.foregroundDomainMarkers.get(
                    new Integer(index));
            if (markers == null) {
                markers = new java.util.ArrayList();
                this.foregroundDomainMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        }
        else if (layer == Layer.BACKGROUND) {
            markers = (Collection) this.backgroundDomainMarkers.get(
                    new Integer(index));
            if (markers == null) {
                markers = new java.util.ArrayList();
                this.backgroundDomainMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        }
        marker.addChangeListener(this);
        notifyListeners(new PlotChangeEvent(this));
    }
removeDomainMarker(int index, Marker marker, Layer layer) {
        ArrayList markers;
        if (layer == Layer.FOREGROUND) {
            markers = (ArrayList) this.foregroundDomainMarkers.get(new Integer(
                    index));
        }
        else {
            markers = (ArrayList) this.backgroundDomainMarkers.get(new Integer(
                    index));
        }
        boolean removed = markers.remove(marker);
        if (removed) {
            notifyListeners(new PlotChangeEvent(this));
        }
        return removed;
    }
addRangeMarker(int index, Marker marker, Layer layer) {
        Collection markers;
        if (layer == Layer.FOREGROUND) {
            markers = (Collection) this.foregroundRangeMarkers.get(
                    new Integer(index));
            if (markers == null) {
                markers = new java.util.ArrayList();
                this.foregroundRangeMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        }
        else if (layer == Layer.BACKGROUND) {
            markers = (Collection) this.backgroundRangeMarkers.get(
                    new Integer(index));
            if (markers == null) {
                markers = new java.util.ArrayList();
                this.backgroundRangeMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        }
        marker.addChangeListener(this);
        notifyListeners(new PlotChangeEvent(this));
    }
removeRangeMarker(int index, Marker marker, Layer layer) {
        if (marker == null) {
            throw new IllegalArgumentException("Null 'marker' argument.");
        }
        ArrayList markers;
        if (layer == Layer.FOREGROUND) {
            markers = (ArrayList) this.foregroundRangeMarkers.get(new Integer(
                    index));
        }
        else {
            markers = (ArrayList) this.backgroundRangeMarkers.get(new Integer(
                    index));
        }

        boolean removed = markers.remove(marker);
        if (removed) {
            notifyListeners(new PlotChangeEvent(this));
        }
        return removed;
    }
addAnnotation(XYAnnotation annotation) {
        if (annotation == null) {
            throw new IllegalArgumentException("Null 'annotation' argument.");
        }
        this.annotations.add(annotation);
        notifyListeners(new PlotChangeEvent(this));
    }
removeAnnotation(XYAnnotation annotation) {
        if (annotation == null) {
            throw new IllegalArgumentException("Null 'annotation' argument.");
        }
        boolean removed = this.annotations.remove(annotation);
        if (removed) {
            notifyListeners(new PlotChangeEvent(this));
        }
        return removed;
    }
drawItem(Graphics2D g2, 
                         XYItemRendererState state,
                         Rectangle2D dataArea, 
                         PlotRenderingInfo info,
                         XYPlot plot, 
                         ValueAxis domainAxis, 
                         ValueAxis rangeAxis,
                         XYDataset dataset, 
                         int series, 
                         int item,
                         CrosshairState crosshairState, 
                         int pass) {

        // do nothing if item is not visible
        if (!getItemVisible(series, item)) {
            return;   
        }

        PlotOrientation orientation = plot.getOrientation();
        
        Paint seriesPaint = getItemPaint(series, item);
        Stroke seriesStroke = getItemStroke(series, item);
        g2.setPaint(seriesPaint);
        g2.setStroke(seriesStroke);

        // get the data point...
        double x1 = dataset.getXValue(series, item);
        double y1 = dataset.getYValue(series, item);
        if (Double.isNaN(y1)) {
            return;
        }

        RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
        RectangleEdge yAxisLocation = plot.getRangeAxisEdge();
        double transX1 = domainAxis.valueToJava2D(x1, dataArea, xAxisLocation);
        double transY1 = rangeAxis.valueToJava2D(y1, dataArea, yAxisLocation);

        if (item > 0) {
            // get the previous data point...
            double x0 = dataset.getXValue(series, item - 1);
            double y0 = dataset.getYValue(series, item - 1);
            if (!Double.isNaN(y0)) {
                double transX0 = domainAxis.valueToJava2D(x0, dataArea, 
                        xAxisLocation);
                double transY0 = rangeAxis.valueToJava2D(y0, dataArea, 
                        yAxisLocation);

                Line2D line = state.workingLine;
                if (orientation == PlotOrientation.HORIZONTAL) {
                    if (transY0 == transY1) { //this represents the situation 
                                              // for drawing a horizontal bar.
                        line.setLine(transY0, transX0, transY1, transX1);
                        g2.draw(line);
                    }
                    else {  //this handles the need to perform a 'step'.
                        line.setLine(transY0, transX0, transY0, transX1);
                        g2.draw(line);
                        line.setLine(transY0, transX1, transY1, transX1);
                        g2.draw(line);
                    }
                }
                else if (orientation == PlotOrientation.VERTICAL) {
                    if (transY0 == transY1) { // this represents the situation 
                                              // for drawing a horizontal bar.
                        line.setLine(transX0, transY0, transX1, transY1);
                        g2.draw(line);
                    }
                    else {  //this handles the need to perform a 'step'.
                        line.setLine(transX0, transY0, transX1, transY0);
                        g2.draw(line);
                        line.setLine(transX1, transY0, transX1, transY1);
                        g2.draw(line);
                    }
                }

            }
        }

        // draw the item label if there is one...
        if (isItemLabelVisible(series, item)) {
            double xx = transX1;
            double yy = transY1;
            if (orientation == PlotOrientation.HORIZONTAL) {
                xx = transY1;
                yy = transX1;
            }          
            drawItemLabel(g2, orientation, dataset, series, item, xx, yy, 
                    (y1 < 0.0));
        }

        int domainAxisIndex = plot.getDomainAxisIndex(domainAxis);
        int rangeAxisIndex = plot.getRangeAxisIndex(rangeAxis);
        updateCrosshairValues(crosshairState, x1, y1, domainAxisIndex, 
                rangeAxisIndex, transX1, transY1, orientation);
        
        // collect entity and tool tip information...
        if (state.getInfo() != null) {
            EntityCollection entities = state.getEntityCollection();
            if (entities != null) {
                int r = getDefaultEntityRadius();
                Shape shape = orientation == PlotOrientation.VERTICAL
                    ? new Rectangle2D.Double(transX1 - r, transY1 - r, 2 * r, 
                            2 * r)
                    : new Rectangle2D.Double(transY1 - r, transX1 - r, 2 * r, 
                            2 * r);           
                if (shape != null) {
                    String tip = null;
                    XYToolTipGenerator generator 
                        = getToolTipGenerator(series, item);
                    if (generator != null) {
                        tip = generator.generateToolTip(dataset, series, item);
                    }
                    String url = null;
                    if (getURLGenerator() != null) {
                        url = getURLGenerator().generateURL(dataset, series, 
                                item);
                    }
                    XYItemEntity entity = new XYItemEntity(shape, dataset, 
                            series, item, tip, url);
                    entities.add(entity);
                }
            }
        }
    }
setFixedDomainAxisSpace(AxisSpace space) {
        this.fixedDomainAxisSpace = space;
        notifyListeners(new PlotChangeEvent(this));
    }
setFixedRangeAxisSpace(AxisSpace space) {
        this.fixedRangeAxisSpace = space;
        notifyListeners(new PlotChangeEvent(this));
    }
setFixedDomainAxisSpace(AxisSpace space) {
        this.fixedDomainAxisSpace = space;
        // TODO: notify?
    }
setFixedRangeAxisSpace(AxisSpace space) {
        this.fixedRangeAxisSpace = space;
        // TODO: fire event?
    }
add(BoxAndWhiskerItem item, 
                    Comparable rowKey, 
                    Comparable columnKey) {

        this.data.addObject(item, rowKey, columnKey);
        
        // update cached min and max values
        int r = this.data.getRowIndex(rowKey);
        int c = this.data.getColumnIndex(columnKey);
        if (this.maximumRangeValueRow == r 
                && this.maximumRangeValueColumn == c) {
            this.maximumRangeValue = Double.NaN;
        }
        if (this.minimumRangeValueRow == r 
                && this.minimumRangeValueColumn == c) {
            this.minimumRangeValue = Double.NaN;
        }
        
        
        double minval = Double.NaN;
        if (item.getMinOutlier() != null) {
            minval = item.getMinOutlier().doubleValue();
        }
        double maxval = Double.NaN;
        if (item.getMaxOutlier() != null) {
            maxval = item.getMaxOutlier().doubleValue();
        }
        
        if (Double.isNaN(this.maximumRangeValue)) {
            this.maximumRangeValue = maxval;
            this.maximumRangeValueRow = r;
            this.maximumRangeValueColumn = c;
        }
        else if (maxval > this.maximumRangeValue) {
            this.maximumRangeValue = maxval;
            this.maximumRangeValueRow = r;
            this.maximumRangeValueColumn = c;
        }
        
        if (Double.isNaN(this.minimumRangeValue)) {
            this.minimumRangeValue = minval;
            this.minimumRangeValueRow = r;
            this.minimumRangeValueColumn = c;
        }
        else if (minval < this.minimumRangeValue) {
            this.minimumRangeValue = minval;
            this.minimumRangeValueRow = r;
            this.minimumRangeValueColumn = c;
        }
        
        this.rangeBounds = new Range(this.minimumRangeValue,
              this.maximumRangeValue);

        fireDatasetChanged();

    }
zoomRangeAxes(double factor, PlotRenderingInfo state, 
                              Point2D source) {
        for (int i = 0; i < this.rangeAxes.size(); i++) {
            ValueAxis rangeAxis = (ValueAxis) this.rangeAxes.get(i);
            if (rangeAxis != null) {
                rangeAxis.resizeRange(factor);
            }
        }
    }
zoomDomainAxes(double factor, PlotRenderingInfo info,
                               Point2D source) {
        for (int i = 0; i < this.domainAxes.size(); i++) {
            ValueAxis domainAxis = (ValueAxis) this.domainAxes.get(i);
            if (domainAxis != null) {
                domainAxis.resizeRange(factor);
            }
        }
    }
zoomRangeAxes(double factor, PlotRenderingInfo info,
                              Point2D source) {
        for (int i = 0; i < this.rangeAxes.size(); i++) {
            ValueAxis rangeAxis = (ValueAxis) this.rangeAxes.get(i);
            if (rangeAxis != null) {
                rangeAxis.resizeRange(factor);
            }
        }
    }

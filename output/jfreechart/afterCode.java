{
        super.add(item, notify);
    }
{
        super.add(item, notify);
    }
{
        super.add(item, notify);
    }
{
        super.add(item, notify);
    }
{

        if (paint instanceof Color) {
            return darker((Color) paint);
        }
        if (legacyAlpha == true) {
            /*
             * Legacy? Just return the original Paint.
             * (this corresponds EXACTLY to how Paints used to be darkened)
             */
            return paint;
        }
        if (paint instanceof GradientPaint) {
            return darker((GradientPaint) paint);
        }
        if (paint instanceof LinearGradientPaint) {
            return darkerLinearGradientPaint((LinearGradientPaint) paint);
        }
        if (paint instanceof RadialGradientPaint) {
            return darkerRadialGradientPaint((RadialGradientPaint) paint);
        }
        if (paint instanceof TexturePaint) {
            try {
                return darkerTexturePaint((TexturePaint) paint);
            }
            catch (Exception e) {
                /*
                 * Lots can go wrong while fiddling with Images, Color Models
                 * & such!  If anything at all goes awry, just return the original
                 * TexturePaint.  (TexturePaint's are immutable anyway, so no harm
                 * done)
                 */
                return paint;
            }
        }
        return paint;
    }
{
        Block block = createLegendItemBlock(item);
        this.items.add(block);
    }
{
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

        JTabbedPane tabs = createPlotTabs(plot);
        tabs.add(localizationResources.getString("Appearance"), appearance);
        panel.add(tabs);
        
        return panel;
    }
{
        TextAnchor ta = TextAnchor.CENTER;

        // normalize angle
        double offset = angleOffset;
        while (offset < 0.0)
            offset += 360.0;
        double normalizedAngle = (((counterClockwise ? -1 : 1) * angleDegrees)
                + offset) % 360;
        while (counterClockwise && (normalizedAngle < 0.0))
            normalizedAngle += 360.0;
        
        if (normalizedAngle == 0.0) {
            ta = TextAnchor.CENTER_LEFT;
        }
        else if (normalizedAngle > 0.0 && normalizedAngle < 90.0) {
            ta = TextAnchor.TOP_LEFT;
        }
        else if (normalizedAngle == 90.0) {
            ta = TextAnchor.TOP_CENTER;
        }
        else if (normalizedAngle > 90.0 && normalizedAngle < 180.0) {
            ta = TextAnchor.TOP_RIGHT;
        }
        else if (normalizedAngle == 180) {
            ta = TextAnchor.CENTER_RIGHT;
        }
        else if (normalizedAngle > 180.0 && normalizedAngle < 270.0) {
            ta = TextAnchor.BOTTOM_RIGHT;
        }
        else if (normalizedAngle == 270) {
            ta = TextAnchor.BOTTOM_CENTER;
        }
        else if (normalizedAngle > 270.0 && normalizedAngle < 360.0) {
            ta = TextAnchor.BOTTOM_LEFT;
        }
        return ta;
    }
{
        XYDataset existing = getDataset(index);
        if (existing != null) {
            existing.removeChangeListener(this);
        }
        this.datasets.set(index, dataset);
        if (dataset != null) {
            dataset.addChangeListener(this);
        }

        // send a dataset change event to self...
        DatasetChangeEvent event = new DatasetChangeEvent(this, dataset);
        datasetChanged(event);
    }
{
        if (item == null) {
            throw new IllegalArgumentException("Null 'item' argument.");
        }
        if (this.allowDuplicateXValues) {
            add(item);
            return null;
        }

        // if we get to here, we know that duplicate X values are not permitted
        XYDataItem overwritten = null;
        int index = indexOf(item.getX());
        if (index >= 0) {
            XYDataItem existing = (XYDataItem) this.data.get(index);
            overwritten = (XYDataItem) existing.clone();
            // figure out if we need to iterate through all the y-values
            boolean iterate = false;
            double oldY = existing.getYValue();
            if (!Double.isNaN(oldY)) {
                iterate = oldY <= this.minY || oldY >= this.maxY;
            }
            existing.setY(item.getY());

            if (iterate) {
                findBoundsByIteration();
            }
            else if (item.getY() != null) {
                double yy = item.getY().doubleValue();
                this.minY = minIgnoreNaN(this.minY, yy);
                this.maxY = minIgnoreNaN(this.maxY, yy);
            }
        }
        else {
            // if the series is sorted, the negative index is a result from
            // Collections.binarySearch() and tells us where to insert the
            // new item...otherwise it will be just -1 and we should just
            // append the value to the list...
            item = (XYDataItem) item.clone();
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
{

        if (item == null) {
            throw new IllegalArgumentException("Null 'period' argument.");
        }
        Class periodClass = item.getPeriod().getClass();
        if (this.timePeriodClass == null) {
            this.timePeriodClass = periodClass;
        }
        else if (!this.timePeriodClass.equals(periodClass)) {
            String msg = "You are trying to add data where the time "
                    + "period class is " + periodClass.getName()
                    + ", but the TimeSeries is expecting an instance of "
                    + this.timePeriodClass.getName() + ".";
            throw new SeriesException(msg);
        }
        TimeSeriesDataItem overwritten = null;
        int index = Collections.binarySearch(this.data, item);
        if (index >= 0) {
            TimeSeriesDataItem existing
                    = (TimeSeriesDataItem) this.data.get(index);
            overwritten = (TimeSeriesDataItem) existing.clone();
            // figure out if we need to iterate through all the y-values
            // to find the revised minY / maxY
            boolean iterate = false;
            Number oldYN = existing.getValue();
            double oldY = oldYN != null ? oldYN.doubleValue() : Double.NaN;
            if (!Double.isNaN(oldY)) {
                iterate = oldY <= this.minY || oldY >= this.maxY;
            }
            existing.setValue(item.getValue());
            if (iterate) {
                findBoundsByIteration();
            }
            else if (item.getValue() != null) {
                double yy = item.getValue().doubleValue();
                this.minY = minIgnoreNaN(this.minY, yy);
                this.maxY = minIgnoreNaN(this.maxY, yy);
            }
        }
        else {
            item = (TimeSeriesDataItem) item.clone();
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
{
        if (end < start) {
            throw new IllegalArgumentException("Requires start <= end.");
        }
        for (int i = 0; i <= (end - start); i++) {
            this.data.remove(start);
        }
        findBoundsByIteration();
        if (this.data.isEmpty()) {
            this.timePeriodClass = null;
        }
        if (notify) {
            fireSeriesChanged();
        }
    }
{

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

        if (copy) {
            if (separator) {
                result.addSeparator();
                separator = false;
            }
            JMenuItem copyItem = new JMenuItem(
                    localizationResources.getString("Copy"));
            copyItem.setActionCommand(COPY_COMMAND);
            copyItem.addActionListener(this);
            result.add(copyItem);
            separator = !save;
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
{
        if (dataset == null) {
            return null;
        }
        if (getDataBoundsIncludesVisibleSeriesOnly()) {
            List visibleSeriesKeys = new ArrayList();
            int seriesCount = dataset.getRowCount();
            for (int s = 0; s < seriesCount; s++) {
                if (isSeriesVisible(s)) {
                    visibleSeriesKeys.add(dataset.getRowKey(s));
                }
            }
            return DatasetUtilities.findRangeBounds(dataset,
                    visibleSeriesKeys, includeInterval);
        }
        else {
            return DatasetUtilities.findRangeBounds(dataset, includeInterval);
        }
    }
{
        if (dataset == null) {
            return null;
        }
        if (getDataBoundsIncludesVisibleSeriesOnly()) {
            List visibleSeriesKeys = new ArrayList();
            int seriesCount = dataset.getSeriesCount();
            for (int s = 0; s < seriesCount; s++) {
                if (isSeriesVisible(s)) {
                    visibleSeriesKeys.add(dataset.getSeriesKey(s));
                }
            }
            return DatasetUtilities.findDomainBounds(dataset,
                    visibleSeriesKeys, includeInterval);
        }
        else {
            return DatasetUtilities.findDomainBounds(dataset, includeInterval);
        }
    }
{
        if (dataset == null) {
            return null;
        }
        if (getDataBoundsIncludesVisibleSeriesOnly()) {
            List visibleSeriesKeys = new ArrayList();
            int seriesCount = dataset.getSeriesCount();
            for (int s = 0; s < seriesCount; s++) {
                if (isSeriesVisible(s)) {
                    visibleSeriesKeys.add(dataset.getSeriesKey(s));
                }
            }
            ValueAxis xAxis = null;
            int index = plot.getIndexOf(this);
            if (index >= 0) {
                xAxis = plot.getDomainAxisForDataset(index);
            }
            Range xRange = null;
            if (xAxis != null) {
                xRange = xAxis.getRange();
            }
            else {
                xRange = new Range(Double.NEGATIVE_INFINITY,
                        Double.POSITIVE_INFINITY);
            }
            return DatasetUtilities.findRangeBounds(dataset,
                    visibleSeriesKeys, xRange, includeInterval);
        }
        else {
            return DatasetUtilities.findRangeBounds(dataset, includeInterval);
        }
    }
{
        RegularTimePeriod result = null;
        try {
            Constructor c = this.periodClass.getDeclaredConstructor(
                    new Class[] {Date.class, TimeZone.class, Locale.class});
            result = (RegularTimePeriod) c.newInstance(new Object[] {
                    millisecond, zone, locale});
        }
        catch (Exception e) {
            // do nothing
        }
        return result;
    }
{

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
        return series;
    }
{

        List result = new ArrayList();
        double posBase = base;
        double negBase = base;
        double total = 0.0;
        if (asPercentages) {
            total = DataUtilities.calculateColumnTotal(dataset,
                    dataset.getColumnIndex(category), includedRows);
        }

        int baseIndex = -1;
        int rowCount = includedRows.length;
        for (int i = 0; i < rowCount; i++) {
            int r = includedRows[i];
            Number n = dataset.getValue(dataset.getRowKey(r), category);
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
                result.add(new Object[] {new Integer(r), new Double(posBase)});
            }
            else if (v < 0.0) {
                if (baseIndex < 0) {
                    result.add(new Object[] {null, new Double(base)});
                    baseIndex = 0;
                }
                negBase = negBase + v; // '+' because v is negative
                result.add(0, new Object[] {new Integer(-r - 1),
                        new Double(negBase)});
                baseIndex++;
            }
        }
        return result;

    }
{
        Date result = time;
        switch (unit.getUnit()) {
            case (DateTickUnit.MILLISECOND) :
            case (DateTickUnit.SECOND) :
            case (DateTickUnit.MINUTE) :
            case (DateTickUnit.HOUR) :
            case (DateTickUnit.DAY) :
                break;
            case (DateTickUnit.MONTH) :
                result = calculateDateForPosition(new Month(time,
                        this.timeZone, this.locale), position);
                break;
            case(DateTickUnit.YEAR) :
                result = calculateDateForPosition(new Year(time,
                        this.timeZone, this.locale), position);
                break;

            default: break;
        }
        return result;
    }
{
        if (index < 0) {
            throw new IllegalArgumentException("Requires 'index' >= 0.");
        }
        checkAxisIndices(axisIndices);
        Integer key = new Integer(index);
        this.datasetToDomainAxesMap.put(key, new ArrayList(axisIndices));
        // fake a dataset change event to update axes...
        datasetChanged(new DatasetChangeEvent(this, getDataset(index)));
    }
{
        if (index < 0) {
            throw new IllegalArgumentException("Requires 'index' >= 0.");
        }
        checkAxisIndices(axisIndices);
        Integer key = new Integer(index);
        this.datasetToRangeAxesMap.put(key, new ArrayList(axisIndices));
        // fake a dataset change event to update axes...
        datasetChanged(new DatasetChangeEvent(this, getDataset(index)));
    }
{
        if (index < 0) {
            throw new IllegalArgumentException("Requires 'index' >= 0.");
        }
        checkAxisIndices(axisIndices);
        Integer key = new Integer(index);
        this.datasetToDomainAxesMap.put(key, new ArrayList(axisIndices));
        // fake a dataset change event to update axes...
        datasetChanged(new DatasetChangeEvent(this, getDataset(index)));
    }
{
        if (index < 0) {
            throw new IllegalArgumentException("Requires 'index' >= 0.");
        }
        checkAxisIndices(axisIndices);
        Integer key = new Integer(index);
        this.datasetToRangeAxesMap.put(key, new ArrayList(axisIndices));
        // fake a dataset change event to update axes...
        datasetChanged(new DatasetChangeEvent(this, getDataset(index)));
    }
{
        notifyListeners(new AxisChangeEvent(this));
    }
{

        if (zone == null) {
            throw new IllegalArgumentException("Null 'zone' argument.");
        }
        if (locale == null) {
        	throw new IllegalArgumentException("Null 'locale' argument.");
        }
        TickUnits units = new TickUnits();

        // date formatters
        DateFormat f1 = new SimpleDateFormat("HH:mm:ss.SSS", locale);
        DateFormat f2 = new SimpleDateFormat("HH:mm:ss", locale);
        DateFormat f3 = new SimpleDateFormat("HH:mm", locale);
        DateFormat f4 = new SimpleDateFormat("d-MMM, HH:mm", locale);
        DateFormat f5 = new SimpleDateFormat("d-MMM", locale);
        DateFormat f6 = new SimpleDateFormat("MMM-yyyy", locale);
        DateFormat f7 = new SimpleDateFormat("yyyy", locale);

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
{
        if (insets == null) {
            throw new IllegalArgumentException("Null 'insets' argument.");
        }
        if (!insets.equals(this.labelInsets)) {
            this.labelInsets = insets;
            if (notify) {
                notifyListeners(new AxisChangeEvent(this));
            }
        }
    }
{
        // delegate 'info' and 'source' argument checks...
        CategoryPlot subplot = findSubplot(info, source);
        if (subplot != null) {
            subplot.zoomRangeAxes(factor, info, source, useAnchor);
        }
        else {
            // if the source point doesn't fall within a subplot, we do the
            // zoom on all subplots...
            Iterator iterator = getSubplots().iterator();
            while (iterator.hasNext()) {
                subplot = (CategoryPlot) iterator.next();
                subplot.zoomRangeAxes(factor, info, source, useAnchor);
            }
        }
    }
{
        // delegate 'state' and 'source' argument checks...
        XYPlot subplot = findSubplot(state, source);
        if (subplot != null) {
            subplot.zoomRangeAxes(factor, state, source, useAnchor);
        }
        else {
            // if the source point doesn't fall within a subplot, we do the
            // zoom on all subplots...
            Iterator iterator = getSubplots().iterator();
            while (iterator.hasNext()) {
                subplot = (XYPlot) iterator.next();
                subplot.zoomRangeAxes(factor, state, source, useAnchor);
            }
        }
    }
{
        // delegate 'info' and 'source' argument checks...
        XYPlot subplot = findSubplot(info, source);
        if (subplot != null) {
            subplot.zoomDomainAxes(factor, info, source, useAnchor);
        }
        else {
            // if the source point doesn't fall within a subplot, we do the
            // zoom on all subplots...
            Iterator iterator = getSubplots().iterator();
            while (iterator.hasNext()) {
                subplot = (XYPlot) iterator.next();
                subplot.zoomDomainAxes(factor, info, source, useAnchor);
            }
        }
    }
{
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
{
        double minimum = Double.POSITIVE_INFINITY;
        double maximum = Double.NEGATIVE_INFINITY;
        int seriesCount = dataset.getSeriesCount();
        for (int series = 0; series < seriesCount; series++) {
            int itemCount = dataset.getItemCount(series);
            for (int item = 0; item < itemCount; item++) {
                double lvalue;
                double uvalue;
                if (includeInterval && dataset instanceof IntervalXYDataset) {
                    IntervalXYDataset intervalXYData 
                        = (IntervalXYDataset) dataset;
                    lvalue = intervalXYData.getStartYValue(series, item);
                    uvalue = intervalXYData.getEndYValue(series, item);
                }
                else if (includeInterval && dataset instanceof OHLCDataset) {
                    OHLCDataset highLowData = (OHLCDataset) dataset;
                    lvalue = highLowData.getLowValue(series, item);
                    uvalue = highLowData.getHighValue(series, item);
                }
                else {
                    lvalue = dataset.getYValue(series, item);
                    uvalue = lvalue;
                }
                if (!Double.isNaN(lvalue)) {
                    minimum = Math.min(minimum, lvalue);
                }
                if (!Double.isNaN(uvalue)) {     
                    maximum = Math.max(maximum, uvalue);
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
{
    	Paint p = getBoxPaint();
    	if (p != null) {
    		return p;
    	}
    	else {
    		// TODO: could change this to itemFillPaint().  For backwards
    		// compatibility, it might require a useFillPaint flag.
    		return getItemPaint(series, item);
    	}
    }
{
        notifyListeners(new PlotChangeEvent(this));
    }
{
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
        if (notify) {
            notifyListeners(new PlotChangeEvent(this));
        }
    }
{
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
        if (removed && notify) {
            notifyListeners(new PlotChangeEvent(this));
        }
        return removed;
    }
{
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
        if (notify) {
            notifyListeners(new PlotChangeEvent(this));
        }
    }
{
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
        if (removed && notify) {
            notifyListeners(new PlotChangeEvent(this));
        }
        return removed;
    }
{
        if (annotation == null) {
            throw new IllegalArgumentException("Null 'annotation' argument.");
        }
        this.annotations.add(annotation);
        if (notify) {
            notifyListeners(new PlotChangeEvent(this));
        }
    }
{
        if (annotation == null) {
            throw new IllegalArgumentException("Null 'annotation' argument.");
        }
        boolean removed = this.annotations.remove(annotation);
        if (removed && notify) {
            notifyListeners(new PlotChangeEvent(this));
        }
        return removed;
    }
{
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
        if (notify) {
            notifyListeners(new PlotChangeEvent(this));
        }
    }
{
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
        if (removed && notify) {
            notifyListeners(new PlotChangeEvent(this));
        }
        return removed;
    }
{
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
        if (notify) {
            notifyListeners(new PlotChangeEvent(this));
        }
    }
{
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
        if (removed && notify) {
            notifyListeners(new PlotChangeEvent(this));
        }
        return removed;
    }
{
        if (annotation == null) {
            throw new IllegalArgumentException("Null 'annotation' argument.");
        }
        this.annotations.add(annotation);
        if (notify) {
            notifyListeners(new PlotChangeEvent(this));
        }
    }
{
        if (annotation == null) {
            throw new IllegalArgumentException("Null 'annotation' argument.");
        }
        boolean removed = this.annotations.remove(annotation);
        if (removed && notify) {
            notifyListeners(new PlotChangeEvent(this));
        }
        return removed;
    }
{
        if (Double.isNaN(x0) || Double.isNaN(x1) || Double.isNaN(y0) 
        		|| Double.isNaN(y1)) {
            return;
        }
        line.setLine(x0, y0, x1, y1);
        g2.draw(line);
    }
{
        this.fixedDomainAxisSpace = space;
        if (notify) {
            notifyListeners(new PlotChangeEvent(this));
        }
    }
{
        this.fixedRangeAxisSpace = space;
        if (notify) {
            notifyListeners(new PlotChangeEvent(this));
        }
    }
{
        this.fixedDomainAxisSpace = space;
        if (notify) {
            notifyListeners(new PlotChangeEvent(this));
        }
    }
{
        this.fixedRangeAxisSpace = space;
        if (notify) {
            notifyListeners(new PlotChangeEvent(this));
        }
    }
{
        this.minimumRangeValue = Double.NaN;
        this.minimumRangeValueRow = -1;
        this.minimumRangeValueColumn = -1;
        this.maximumRangeValue = Double.NaN;
        this.maximumRangeValueRow = -1;
        this.maximumRangeValueColumn = -1;
        int rowCount = getRowCount();
        int columnCount = getColumnCount();
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                BoxAndWhiskerItem item = getItem(r, c);
                if (item != null) {
                    Number min = item.getMinOutlier();
                    if (min != null) {
                        double minv = min.doubleValue();
                        if (!Double.isNaN(minv)) {
                            if (minv < this.minimumRangeValue || Double.isNaN(
                                    this.minimumRangeValue)) {
                                this.minimumRangeValue = minv;
                                this.minimumRangeValueRow = r;
                                this.minimumRangeValueColumn = c;
                            }
                        }
                    }
                    Number max = item.getMaxOutlier();
                    if (max != null) {
                        double maxv = max.doubleValue();
                        if (!Double.isNaN(maxv)) {
                            if (maxv > this.maximumRangeValue || Double.isNaN(
                                    this.maximumRangeValue)) {
                                this.maximumRangeValue = maxv;
                                this.maximumRangeValueRow = r;
                                this.maximumRangeValueColumn = c;
                            }
                        }
                    }
                }
            }
        }
    }
{
                
        // perform the zoom on each range axis
        for (int i = 0; i < this.rangeAxes.size(); i++) {
            ValueAxis rangeAxis = (ValueAxis) this.rangeAxes.get(i);
            if (rangeAxis != null) {
                if (useAnchor) {
                    // get the relevant source coordinate given the plot 
                    // orientation
                    double sourceY = source.getY();
                    if (this.orientation == PlotOrientation.HORIZONTAL) {
                        sourceY = source.getX();
                    }
                    double anchorY = rangeAxis.java2DToValue(sourceY, 
                            info.getDataArea(), getRangeAxisEdge());
                    rangeAxis.resizeRange(factor, anchorY);
                }
                else {
                    rangeAxis.resizeRange(factor);
                }
            }
        }
    }
{
                
        // perform the zoom on each domain axis
        for (int i = 0; i < this.domainAxes.size(); i++) {
            ValueAxis domainAxis = (ValueAxis) this.domainAxes.get(i);
            if (domainAxis != null) {
                if (useAnchor) {
                    // get the relevant source coordinate given the plot 
                    // orientation
                    double sourceX = source.getX();
                    if (this.orientation == PlotOrientation.HORIZONTAL) {
                        sourceX = source.getY();
                    }
                    double anchorX = domainAxis.java2DToValue(sourceX, 
                            info.getDataArea(), getDomainAxisEdge());
                    domainAxis.resizeRange(factor, anchorX);
                }
                else {
                    domainAxis.resizeRange(factor);
                }
            }
        }
    }
{
                
        // perform the zoom on each range axis
        for (int i = 0; i < this.rangeAxes.size(); i++) {
            ValueAxis rangeAxis = (ValueAxis) this.rangeAxes.get(i);
            if (rangeAxis != null) {
                if (useAnchor) {
                    // get the relevant source coordinate given the plot 
                    // orientation
                    double sourceY = source.getY();
                    if (this.orientation == PlotOrientation.HORIZONTAL) {
                        sourceY = source.getX();
                    }
                    double anchorY = rangeAxis.java2DToValue(sourceY, 
                            info.getDataArea(), getRangeAxisEdge());
                    rangeAxis.resizeRange(factor, anchorY);
                }
                else {
                    rangeAxis.resizeRange(factor);
                }
            }
        }
    }

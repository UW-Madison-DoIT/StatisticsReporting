/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.web.chart;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;
import de.laures.cewolf.tooltips.XYToolTipGenerator;
import edu.wisc.my.stats.domain.Fact;
import edu.wisc.my.stats.util.Table;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class StatsTableDatasetProducer implements DatasetProducer, XYToolTipGenerator, Serializable {
    private final Table<Long, Fact, Double> statsData;
    
    public StatsTableDatasetProducer(final Table<Long, Fact, Double> statsData) {
        this.statsData = statsData;
    }

    /**
     * @see de.laures.cewolf.DatasetProducer#getProducerId()
     */
    public String getProducerId() {
        return "StatsTableDatasetProducer";
    }

    /**
     * @see de.laures.cewolf.DatasetProducer#hasExpired(java.util.Map, java.util.Date)
     */
    public boolean hasExpired(Map params, Date since) {
        return true;
    }

    /**
     * @see de.laures.cewolf.DatasetProducer#produceDataset(java.util.Map)
     */
    public Object produceDataset(Map params) throws DatasetProduceException {
        final XYSeriesCollection seriesCollection = new XYSeriesCollection();
        
        for (final Fact fact : this.statsData.getColumnKeys()) {
            final XYSeries series = new XYSeries(fact.getName());

            for (final Table.Entry<Long, Fact, Double> entry : this.statsData.getColumnEntries(fact)) {
                series.add(entry.getRowKey(), entry.getValue());
            }
            
            seriesCollection.addSeries(series);
        }
        
        return seriesCollection;
    }

    /**
     * @see de.laures.cewolf.tooltips.XYToolTipGenerator#generateToolTip(org.jfree.data.xy.XYDataset, int, int)
     */
    public String generateToolTip(XYDataset data, int series, int item) {
        final Number x = data.getX(series, item);
        final Number y = data.getY(series, item);
        
        final String seriesName = (String)data.getSeriesKey(series);
        
        //TODO format Date based on Resolution
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return seriesName + "\\n" + y.doubleValue() + ", " + dateFormat.format(new Date(x.longValue()));
    }
}

/*
 *  GeoBatch - Open Source geospatial batch processing system
 *  http://geobatch.codehaus.org/
 *  Copyright (C) 2007-2008-2009 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 *  GPLv3 + Classpath exception
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */



package it.geosolutions.geobatch.configuration.event.consumer.file;

import it.geosolutions.geobatch.catalog.impl.BaseConfiguration;
import it.geosolutions.geobatch.configuration.event.action.ActionConfiguration;
import it.geosolutions.geobatch.configuration.event.consumer.EventConsumerConfiguration;
import it.geosolutions.geobatch.configuration.event.listener.ProgressListenerConfiguration;
import it.geosolutions.geobatch.flow.event.consumer.file.FileEventRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration for the event consumers based on xml marshalled files.
 *
 * <P> TODO: we may need another hierarchy level for a <B>BaseEventConsumerConfiguration</B> class
 *
 * @author Simone Giannecchini, GeoSolutions
 */
public class FileBasedEventConsumerConfiguration 
        extends BaseConfiguration
        implements EventConsumerConfiguration {

    /**
     * List of configurable actions that will be sequentially performed at the end of event
     * consumption.
     */
    private List<? extends ActionConfiguration> actions;

    /**
     * List of rules defining the consumer behavior.
     */
    private List<FileEventRule> rules;

    /**
     * The configuring directory. This is the directory where the consumer will store the input
     * data.
     */
    private String workingDirectory;

    /**
     * Do we remove input files and put them on a backup directory?
     */
    private boolean performBackup;

    /**
     * The id of the Listener Configuration.
     * <BR>They are needed for a post-load binding; loader logic will put the proper
     * listener configurations into {@link #listenerConfigurations}.
     */
    private List<String> listenerIds = new ArrayList<String>();

    /**
     * These configs are filled by the loader, dereferencing the listenersId.
     */
    protected List<ProgressListenerConfiguration> listenerConfigurations = new ArrayList<ProgressListenerConfiguration>();

	protected FileBasedEventConsumerConfiguration(String id, String name,
			String description, boolean dirty) {
		super(id, name, description, dirty);
	}

	protected FileBasedEventConsumerConfiguration(String id, String name,
			String description) {
		super(id, name, description);
	}

	/**
     * Default Constructor.
     */
    public FileBasedEventConsumerConfiguration() {
        super();
    }


    /**
     * Getter for the consumer actions.
     * 
     * @return actions
     */
    public List<? extends ActionConfiguration> getActions() {
        return this.actions;
    }

    /**
     * Setter for the consumer actions.
     * 
     * @param actions
     */
    public void setActions(List<? extends ActionConfiguration> actions) {
        this.actions = new ArrayList<ActionConfiguration>(actions);
    }

    /**
     * Getter for the consumer rules.
     * 
     * @return rules
     */
    public List<FileEventRule> getRules() {
        return rules;
    }

    /**
     * Setter for the consumer rules.
     * 
     * @param rules
     */
    public void setRules(List<FileEventRule> rules) {
        this.rules = new ArrayList<FileEventRule>(rules);
    }

    /**
     * Getter for the configuring directory attribute.
     * 
     * @return workingDirectory
     */
    public String getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * Setter for the configuring directory attribute.
     * 
     * @param workingDirectory
     */
    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    /**
     * Is the backup of the input data enabled?
     * 
     * @return performBackup
     */
    public boolean isPerformBackup() {
        return performBackup;
    }

    /**
     * Setter for the perform backup option.
     * 
     * @param performBackup
     */
    public void setPerformBackup(boolean performBackup) {
        this.performBackup = performBackup;
    }

    public List<String> getListenerIds() {
//        synchronized(this) {
//            if(listenerIds == null) { // this may happen when loading via XStream
//                listenerIds = new ArrayList<String>();
//            }
//        }
        return listenerIds;
    }

    public void setListenerId(List<String> ids) {
//        synchronized(this) {
//            if(listenerIds == null) { // this may happen when loading via XStream
//                listenerIds = new ArrayList<String>();
//            }
//        }
        this.listenerIds = ids;
    }

    public void addListenerConfiguration(ProgressListenerConfiguration plc) {
        synchronized(this) {
            if(listenerConfigurations == null) { // this may happen when loading via XStream
                listenerConfigurations = new ArrayList<ProgressListenerConfiguration>();
            }
        }
        listenerConfigurations.add(plc);
    }
    public List<ProgressListenerConfiguration> getListenerConfigurations() {
        synchronized(this) {
            if(listenerConfigurations == null) { // this may happen when loading via XStream
                listenerConfigurations = new ArrayList<ProgressListenerConfiguration>();
            }
        }
        return listenerConfigurations;
    }
    
    @Override
	public FileBasedEventConsumerConfiguration clone() { // throws CloneNotSupportedException {
		
    	// clone object
		final FileBasedEventConsumerConfiguration object = new FileBasedEventConsumerConfiguration(super.getId(),super.getName(),super.getDescription(),super.isDirty());
		object.setPerformBackup(performBackup);
		object.setWorkingDirectory(workingDirectory);
		
		//clone its elements
		final List<FileEventRule> clonedRules = new ArrayList<FileEventRule>(rules.size());
		for(FileEventRule rule :rules)
			clonedRules.add(rule.clone());
		object.setRules(clonedRules);
		
		final List<ActionConfiguration> clonedActions = new ArrayList<ActionConfiguration>(actions.size());
		for(ActionConfiguration action :actions)
			clonedActions.add(action.clone());
		object.setActions(clonedActions);

        // clone listeners
        if(listenerConfigurations != null) { // tricks from xstream
            for (ProgressListenerConfiguration progressListenerConfiguration : listenerConfigurations) {
                object.addListenerConfiguration(progressListenerConfiguration);
            }
        }

		return object;

	}

}

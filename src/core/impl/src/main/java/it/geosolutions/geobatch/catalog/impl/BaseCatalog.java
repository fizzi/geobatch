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

package it.geosolutions.geobatch.catalog.impl;

import it.geosolutions.geobatch.catalog.Catalog;
import it.geosolutions.geobatch.catalog.Resource;
import it.geosolutions.geobatch.catalog.event.CatalogAddEvent;
import it.geosolutions.geobatch.catalog.event.CatalogEvent;
import it.geosolutions.geobatch.catalog.event.CatalogListener;
import it.geosolutions.geobatch.catalog.event.CatalogModifyEvent;
import it.geosolutions.geobatch.catalog.event.CatalogRemoveEvent;
import it.geosolutions.geobatch.catalog.impl.event.CatalogAddEventImpl;
import it.geosolutions.geobatch.catalog.impl.event.CatalogModifyEventImpl;
import it.geosolutions.geobatch.catalog.impl.event.CatalogRemoveEventImpl;
import it.geosolutions.geobatch.configuration.CatalogConfiguration;
import it.geosolutions.geobatch.configuration.flow.FlowConfiguration;
import it.geosolutions.geobatch.flow.FlowManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import org.apache.commons.collections.MultiHashMap;

/**
 * @author Alessio Fabiani, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 * 
 */
public class BaseCatalog extends BasePersistentResource<CatalogConfiguration> implements Catalog {
    private final static Logger LOGGER = Logger.getLogger(BaseCatalog.class.getName());

    /**
     * flow manager types
     * @uml.property  name="flowManagers"
     * @uml.associationEnd  multiplicity="(1 1)"
     */
    private final MultiHashMap /* <Class> */flowManagers = new MultiHashMap();

    /**
     * resources
     * @uml.property  name="resources"
     * @uml.associationEnd  qualifier="getClass:java.lang.Class it.geosolutions.geobatch.flow.FlowManager"
     */
    private final MultiHashMap /* <Class> */resources = new MultiHashMap();

    /**
     * listeners
     * @uml.property  name="listeners"
     * @uml.associationEnd  multiplicity="(0 -1)" elementType="it.geosolutions.geobatch.catalog.event.CatalogListener"
     */
    private final List<CatalogListener> listeners = new CopyOnWriteArrayList<CatalogListener>();

    /**
     * Default Constructor.
     */
    public BaseCatalog() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.geosolutions.geobatch.catalog.Catalog#add(it.geosolutions.geobatch
     * .catalog.FlowManager)
     */
    public <EO extends EventObject, FC extends FlowConfiguration> void add(
            final FlowManager<EO, FC> resource) {
        // sanity checks
        if (resource.getId() == null) {
            throw new IllegalArgumentException(
                    "No ID has been specified for this Flow BaseEventConsumer.");
        }

        if (resource.getWorkingDirectory() == null) {
            throw new IllegalArgumentException(
                    "No Output Dir has been specified for this Flow BaseEventConsumer.");
        }

        // if (resource.getConfiguration() == null) {
        // throw new IllegalArgumentException(
        // "No Flow BaseEventConsumer Descriptor has been specified for this Flow BaseEventConsumer.");
        // }

        synchronized (resources) {
            resources.put(resource.getClass(), resource);
        }

        added(resource);
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.geosolutions.geobatch.catalog.Catalog#dispose()
     */
    public void dispose() {
        try {
            persist();
        } catch (Throwable e) {
            // TODO
            e.printStackTrace();
        }
        flowManagers.clear();
        resources.clear();
        listeners.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.geosolutions.geobatch.catalog.Catalog#getFlowManagerType(java.lang .String,
     * java.lang.Class)
     */
    public <EO extends EventObject, FC extends FlowConfiguration, FM extends FlowManager<EO, FC>> FM getFlowManager(
            final String id, final Class<FM> clazz) {

        final List<FM> l = lookup(clazz, flowManagers);

        for (final FM fm : l) {
            if (id.equals(fm.getId())) {
                return fm;
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.geosolutions.geobatch.catalog.Catalog#getFlowManagerTypeByName(java .lang.String,
     * java.lang.Class)
     */
    public <EO extends EventObject, FC extends FlowConfiguration, FM extends FlowManager<EO, FC>> FM getFlowManagerByName(
            final String name, final Class<FM> clazz) {

        final List<FM> l = lookup(clazz, flowManagers);

        for (final FM fm : l) {

            if (name.equals(fm.getName())) {
                return fm;
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.geosolutions.geobatch.catalog.Catalog#getFlowManagers(java.lang.Class)
     */
    public <EO extends EventObject, FC extends FlowConfiguration, FM extends FlowManager<EO, FC>> List<FM> getFlowManagers(
            final Class<FM> clazz) {
        return getResources(clazz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.geosolutions.geobatch.catalog.Catalog#getResource(java.lang.String, java.lang.Class)
     */
    public <R extends Resource> R getResource(final String id, final Class<R> clazz) {
        final List<R> l = lookup(clazz, resources);

        for (R resource : l) {
            if (id.equals(resource.getId())) {
                return resource;
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.geosolutions.geobatch.catalog.Catalog#getResourceByName(java.lang. String,
     * java.lang.Class)
     */
    public <R extends Resource> R getResourceByName(final String name, final Class<R> clazz) {
        final List<R> l = lookup(clazz, resources);

        for (R resource : l) {
            if (name.equals(resource.getName())) {
                return resource;
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.geosolutions.geobatch.catalog.Catalog#getResources(java.lang.Class)
     */
    public <R extends Resource> List<R> getResources(final Class<R> clazz) {
        return Collections.unmodifiableList(lookup(clazz, resources));
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.geosolutions.geobatch.catalog.Catalog#remove(it.geosolutions.geobatch
     * .catalog.FlowManager)
     */
    public <EO extends EventObject, FC extends FlowConfiguration> void remove(
            final FlowManager<EO, FC> resource) {
        synchronized (resources) {
            resources.remove(resource.getClass(), resource);
        }

        removed(resource);
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.geosolutions.geobatch.catalog.Catalog#save(it.geosolutions.geobatch
     * .catalog.FlowManager)
     */
    public <EO extends EventObject, FC extends FlowConfiguration> void save(
            final FlowManager<EO, FC> resource) {
        // TODO
        LOGGER.severe("To be implemented");
    }

    // //
    // Helper Methods
    // //
    /**
     *
     */
    @SuppressWarnings("unchecked")
    <R extends Resource> List<R> lookup(final Class<R> clazz, final MultiHashMap map) {
        final List<R> result = new ArrayList<R>();

        for (final Class<R> key : (Iterable<Class<R>>) map.keySet()) {
            // for (final Iterator<Class<T>> k = map.keySet().iterator();
            // k.hasNext();) {
            // final Class<T> key = k.next();
            if (clazz.isAssignableFrom(key)) {
                result.addAll(map.getCollection(key));
            }
        }

        return result;
    }

    // //
    // Event Methods
    // //
    public Collection<CatalogListener> getListeners() {
        return Collections.unmodifiableCollection(listeners);
    }

    public void addListener(final CatalogListener listener) {
        listeners.add(listener);
    }

    public void removeListener(final CatalogListener listener) {
        listeners.remove(listener);

    }

    protected <T> void added(final T object) {
        fireAdded(object);
    }

    protected <T> void fireAdded(final T object) {
        final CatalogAddEventImpl<T> event = new CatalogAddEventImpl<T>(object);
        event(event);
    }

    protected <T> void fireModified(final T object, final List<String> propertyNames,
            final List<T> oldValues, final List<T> newValues) {
        final CatalogModifyEventImpl<T> event = new CatalogModifyEventImpl<T>(object);
        event.setPropertyNames(propertyNames);
        event.setOldValues(oldValues);
        event.setNewValues(newValues);

        event(event);
    }

    protected <T> void removed(final T object) {
        final CatalogRemoveEventImpl<T> event = new CatalogRemoveEventImpl<T>(object);

        event(event);
    }

    @SuppressWarnings("unchecked")
    protected <T> void event(final CatalogEvent<T> event) {
        synchronized (listeners) {
            for (final CatalogListener listener : listeners) {
                if (event instanceof CatalogAddEvent) {
                    listener.handleAddEvent((CatalogAddEvent) event);
                } else if (event instanceof CatalogRemoveEvent) {
                    listener.handleRemoveEvent((CatalogRemoveEvent) event);
                } else if (event instanceof CatalogModifyEvent) {
                    listener.handleModifyEvent((CatalogModifyEvent) event);
                }
            }
        }
    }

    public <T extends Resource> void add(T resource) {
        // sanity checks
        if (resource.getId() == null) {
            throw new IllegalArgumentException(
                    "No ID has been specified for this Flow BaseEventConsumer.");
        }

        if (resource.getName() == null) {
            throw new IllegalArgumentException(
                    "No Output Dir has been specified for this Flow BaseEventConsumer.");
        }

        if (resource.getDescription() == null) {
            throw new IllegalArgumentException(
                    "No Flow BaseEventConsumer Descriptor has been specified for this Flow BaseEventConsumer.");
        }

        synchronized (resources) {
            resources.put(resource.getClass(), resource);
        }

        added(resource);

    }

}

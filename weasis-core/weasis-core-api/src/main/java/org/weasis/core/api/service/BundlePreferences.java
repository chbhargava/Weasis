/*******************************************************************************
 * Copyright (c) 2010 Nicolas Roduit.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nicolas Roduit - initial API and implementation
 ******************************************************************************/
package org.weasis.core.api.service;

import java.io.File;
import java.util.Arrays;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.prefs.Preferences;
import org.osgi.service.prefs.PreferencesService;
import org.weasis.core.api.gui.util.AppProperties;

public class BundlePreferences {

    private BundlePreferences() {
    }

    public static File getDataFolder(BundleContext context) {
        if (context != null) {
            File dataFolder =
                new File(AppProperties.WEASIS_PATH + File.separator + "data", context.getBundle().getSymbolicName()); //$NON-NLS-1$
            dataFolder.mkdirs();
            return dataFolder;
        }
        return new File(AppProperties.WEASIS_PATH, "data"); //$NON-NLS-1$
    }

    public static Preferences getDefaultPreferences(BundleContext context) {
        return getUserPreferences(context, AppProperties.WEASIS_USER);
    }

    public static Preferences getUserPreferences(BundleContext context, String name) {
        if (context != null) {
            String user = name == null ? AppProperties.WEASIS_USER : name;
            PreferencesService prefService = BundlePreferences.getService(context, PreferencesService.class);
            if (prefService != null) {
                return prefService.getUserPreferences(user);
            }
        }
        return null;
    }

    public static <S> S getService(BundleContext context, Class<S> clazz) {
        if (clazz != null) {
            try {
                ServiceReference<S> serviceRef = context.getServiceReference(clazz);
                if (serviceRef != null) {
                    return context.getService(serviceRef);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void putStringPreferences(Preferences pref, String key, String value) {
        // Does not support null key or value
        if (pref != null && key != null && value != null) {
            String val2 = pref.get(key, null);
            // Update only if the value is different to avoid setting the changeSet to true
            if (val2 == null || !value.equals(val2)) {
                pref.put(key, value);
            }
        }
    }

    public static void putBooleanPreferences(Preferences pref, String key, boolean value) {
        // Does not support null key
        if (pref != null && key != null) {
            Boolean result = null;
            final String s = pref.get(key, null);
            if (s != null) {
                result = Boolean.valueOf(s);
            }
            // Update only if the value is different to avoid setting the changeSet to true
            if (result == null || result.booleanValue() != value) {
                pref.putBoolean(key, value);
            }
        }
    }

    public static void putByteArrayPreferences(Preferences pref, String key, byte[] value) {
        // Does not support null key or value
        if (pref != null && key != null && value != null) {
            byte[] val2 = pref.getByteArray(key, null);
            // Update only if the value is different to avoid setting the changeSet to true
            if (val2 == null || !Arrays.equals(value, val2)) {
                pref.putByteArray(key, value);
            }
        }
    }

    public static void putDoublePreferences(Preferences pref, String key, double value) {
        // Does not support null key
        if (pref != null && key != null) {
            Double result = null;
            final String s = pref.get(key, null);
            if (s != null) {
                try {
                    result = Double.parseDouble(s);
                } catch (NumberFormatException ignore) {
                }
            }
            // Update only if the value is different to avoid setting the changeSet to true
            if (result == null || result.doubleValue() != value) {
                pref.putDouble(key, value);
            }
        }
    }

    public static void putFloatPreferences(Preferences pref, String key, float value) {
        // Does not support null key
        if (pref != null && key != null) {
            Float result = null;
            final String s = pref.get(key, null);
            if (s != null) {
                try {
                    result = Float.parseFloat(s);
                } catch (NumberFormatException ignore) {
                }
            }
            // Update only if the value is different to avoid setting the changeSet to true
            if (result == null || result.floatValue() != value) {
                pref.putFloat(key, value);
            }
        }
    }

    public static void putIntPreferences(Preferences pref, String key, int value) {
        // Does not support null key
        if (pref != null && key != null) {
            Integer result = null;
            final String s = pref.get(key, null);
            if (s != null) {
                try {
                    result = Integer.parseInt(s);
                } catch (NumberFormatException ignore) {
                }
            }
            // Update only if the value is different to avoid setting the changeSet to true
            if (result == null || result.intValue() != value) {
                pref.putInt(key, value);
            }
        }
    }

    public static void putLongPreferences(Preferences pref, String key, long value) {
        // Does not support null key
        if (pref != null && key != null) {
            Long result = null;
            final String s = pref.get(key, null);
            if (s != null) {
                try {
                    result = Long.parseLong(s);
                } catch (NumberFormatException ignore) {
                }
            }
            // Update only if the value is different to avoid setting the changeSet to true
            if (result == null || result.longValue() != value) {
                pref.putLong(key, value);
            }
        }
    }
}

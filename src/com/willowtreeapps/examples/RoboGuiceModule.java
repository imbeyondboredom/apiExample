package com.willowtreeapps.examples;

import com.google.inject.AbstractModule;

import com.willowtreeapps.examples.utilities.EncryptedPreferencesProvider;
import com.willowtreeapps.examples.utilities.EncryptedSharedPreferences;

/**
 * Generated from archetype
 */
public class RoboGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EncryptedSharedPreferences.class).toProvider(EncryptedPreferencesProvider.class);
    }
}

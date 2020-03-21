package com.github.gregwhitaker.envopts;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;

import static org.junit.Assert.*;

public class EnvOptsTest {

    @Rule
    public final RestoreSystemProperties restoreSystemProperties = new RestoreSystemProperties();

    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Test
    public void shouldParseSingleFlag() {
        environmentVariables.set("ENV_OPTS", "test");

        EnvOpts.parse();

        assertTrue(System.getProperties().containsKey("test"));
    }

    @Test
    public void shouldParseMultipleFlags() {
        environmentVariables.set("ENV_OPTS", "test,test1,test2");

        EnvOpts.parse();

        assertTrue(System.getProperties().containsKey("test"));
        assertTrue(System.getProperties().containsKey("test1"));
        assertTrue(System.getProperties().containsKey("test2"));
    }

    @Test
    public void shouldIgnoreEmbeddedPeriods() {
        environmentVariables.set("ENV_OPTS", "test,test1.test2");

        EnvOpts.parse();

        assertTrue(System.getProperties().containsKey("test"));
        assertTrue(System.getProperties().containsKey("test1.test2"));
        assertFalse(System.getProperties().containsKey("test1"));
        assertFalse(System.getProperties().containsKey("test2"));
    }

    @Test
    public void shouldParseJavaParameterWithoutQuotes() {
        environmentVariables.set("ENV_OPTS", "-Dspring.profiles.active=local");

        EnvOpts.parse();

        assertEquals("local", System.getProperty("spring.profiles.active"));
    }

    @Test
    public void shouldParseJavaParameterWithQuotes() {
        environmentVariables.set("ENV_OPTS", "-Dspring.profiles.active='local'");

        EnvOpts.parse();

        assertEquals("local", System.getProperty("spring.profiles.active"));
    }

    @Test
    public void shouldParseJavaParameterWithMultipleValues() {
        environmentVariables.set("ENV_OPTS", "-Dspring.profiles.active='local,dev'");

        EnvOpts.parse();

        assertEquals("local,dev", System.getProperty("spring.profiles.active"));
    }

    @Test
    public void shouldParseMultipleJavaParameters() {
        environmentVariables.set("ENV_OPTS", "-Dspring.profiles.active='local',-Dlog4j.configurationFile=log4j2.xml");

        EnvOpts.parse();

        assertEquals("local", System.getProperty("spring.profiles.active"));
        assertEquals("log4j2.xml", System.getProperty("log4j.configurationFile"));
    }

    @Test
    public void shouldIgnoreSpacesInMultiValueJavaParameters() {
        environmentVariables.set("ENV_OPTS", "-Dspring.profiles.active='local',-Dfoo.bar='foo, bar, baz'");

        EnvOpts.parse();

        assertEquals("local", System.getProperty("spring.profiles.active"));
        assertEquals("foo, bar, baz", System.getProperty("foo.bar"));
    }
}

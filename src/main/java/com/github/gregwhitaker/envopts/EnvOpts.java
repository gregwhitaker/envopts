package com.github.gregwhitaker.envopts;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Parses an environment variable containing a list of system parameters and sets the parameters for use
 * in your application.
 */
public final class EnvOpts {
    private static final Logger LOG = LoggerFactory.getLogger(EnvOpts.class);

    public static final String DEFAULT_ENV_NAME = "ENV_OPTS";

    private EnvOpts() {
        // Prevent direct instantiation
    }

    /**
     * Parses the `ENV_OPTS` environment variable for system parameters.
     */
    public static void parse() {
        parse(DEFAULT_ENV_NAME);
    }

    /**
     * Parses the specified environment variable for system parameters.
     *
     * @param envName environment variable name
     */
    public static void parse(final String envName) {
        LOG.debug("Parsing environment variable '{}' for system parameters...", envName);

        final String envValue = System.getenv(envName);

        if (envValue != null && !envValue.isEmpty()) {
            Parser parser = new Parser();
            parser.parse(envValue).forEach(parameter -> {
                LOG.debug("Setting system parameter '{}': {}", parameter.key, parameter.value);
                System.setProperty(parameter.key, parameter.value);
            });
        }
    }

    /**
     * Parses the environment variable containing the system parameters.
     */
    private static class Parser {
        private static final List<Character> QUOTATIONS = Arrays.asList('"', '\'', '`');

        /**
         * Parses the specified environment variable for system parameters.
         *
         * @param envValue environment variable name
         * @return a list of parsed {@link Parameter} objects
         */
        List<Parameter> parse(String envValue) {
            if (envValue != null && !envValue.isEmpty()) {
                Splitter splitter = Splitter.onPattern(",(?=(?:[^\'0]*\'[^\']*\')*[^\']*$)");

                return splitter.splitToList(envValue).stream()
                        .map(s -> new Parameter(parseKey(s), parseValue(s)))
                        .collect(Collectors.toList());
            } else {
                return Lists.newArrayList();
            }
        }

        private String parseKey(String envValue) {
            if (envValue.startsWith("-D")) {
                envValue = envValue.substring(2);
            }

            if (envValue.contains("=")) {
                envValue = envValue.substring(0, envValue.indexOf("="));
            }

            return envValue.trim();
        }

        private String parseValue(String envValue) {
            if (envValue.contains("=")) {
                envValue = envValue.trim();
                envValue = envValue.substring(envValue.indexOf("=") + 1);

                if (QUOTATIONS.contains(envValue.charAt(0))) {
                    envValue = envValue.substring(1);
                }

                if (QUOTATIONS.contains(envValue.charAt(envValue.length() - 1))) {
                    envValue = envValue.substring(0, envValue.length() - 1);
                }

                return envValue;
            }

            return "";
        }
    }

    /**
     * Parsed parameter from the environment variable.
     */
    private static class Parameter {
        final String key;
        final String value;

        public Parameter(String key) {
            this(key, null);
        }

        public Parameter(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}

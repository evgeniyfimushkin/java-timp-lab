package edu.evgen.habitat;

import lombok.RequiredArgsConstructor;

import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
public enum HabitatProperty {
    processDelay(HabitatConfiguration::getProcessDelay, (h, s) -> h.setProcessDelay(Long.parseLong(s)), "10"),
    developerLivingTime(HabitatConfiguration::getDeveloperLivingTime, (h, s) -> h.setDeveloperLivingTime(Long.parseLong(s)), "4"),
    managerLivingTime(HabitatConfiguration::getManagerLivingTime, (h, s) -> h.setManagerLivingTime(Long.parseLong(s)), "5"),
    developerDelay(HabitatConfiguration::getDeveloperDelay, (h, s) -> h.setDeveloperDelay(Long.parseLong(s)), "1"),
    managerDelay(HabitatConfiguration::getManagerDelay, (h, s) -> h.setManagerDelay(Long.parseLong(s)), "1"),
    developerProbability(HabitatConfiguration::getDeveloperProbability, (h, s) -> h.setDeveloperProbability(Double.parseDouble(s)) , "0.5"),
    managerRatio(HabitatConfiguration::getManagerRatio, (h, s) -> h.setManagerRatio(Double.parseDouble(s)) , "0.5"),
    paneSize(HabitatConfiguration::getPaneSize, (h, s) -> h.setPaneSize(Long.parseLong(s)), "400"),
    moveDelay(HabitatConfiguration::getMoveDelay, (h, s) -> h.setMoveDelay(Long.parseLong(s)), "1");

    final Function<HabitatConfiguration, Number> getter;

    final BiConsumer<HabitatConfiguration, String> setter;

    final String defaultValue;
    public static void merge(HabitatConfiguration configuration, Properties properties) {
        Stream.of(HabitatProperty.values())
                .forEach(property -> properties.setProperty(property.name(), property.getter.apply(configuration).toString()));
    }

    public static void readProperties(HabitatConfiguration configuration, Properties properties){
        Stream.of(HabitatProperty.values())
                .forEach(property -> property.setter.accept(configuration, properties.getProperty(property.name(), property.defaultValue)));
    }

}

/**
 * Copyright (c) 2016, All partners of the iTesla project (http://www.itesla-project.eu/consortium)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package eu.itesla_project.iidm.network.impl;

import eu.itesla_project.iidm.network.*;
import com.google.common.collect.Iterables;
import eu.itesla_project.iidm.network.VoltageLevel.NodeBreakerView;
import eu.itesla_project.iidm.network.test.EurostagTutorialExample1Factory;
import eu.itesla_project.iidm.network.test.NetworkTest1Factory;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Geoffroy Jamgotchian <geoffroy.jamgotchian at rte-france.com>
 */
public class NetworkTest {

    public NetworkTest() {
    }

    @Test
    public void testNetwork1() {
        Network network = NetworkTest1Factory.create();
        assertTrue(Iterables.size(network.getCountries()) == 1);
        assertTrue(network.getCountryCount() == 1);
        Country country1 = network.getCountries().iterator().next();

        assertTrue(Iterables.size(network.getSubstations()) == 1);
        assertTrue(network.getSubstationCount() == 1);

        Substation substation1 = network.getSubstation("substation1");
        assertTrue(substation1 != null);
        assertTrue(substation1.getId().equals("substation1"));
        assertTrue(substation1.getCountry() == country1);
        assertTrue(substation1.getGeographicalTags().size() == 1);
        assertTrue(substation1.getGeographicalTags().contains("region1"));
        assertTrue(Iterables.size(network.getVoltageLevels()) == 1);
        assertTrue(network.getVoltageLevelCount() == 1);

        VoltageLevel voltageLevel1 = network.getVoltageLevel("voltageLevel1");
        assertTrue(voltageLevel1 != null);
        assertTrue(voltageLevel1.getId().equals("voltageLevel1"));
        assertTrue(voltageLevel1.getNominalV() == 400);
        assertTrue(voltageLevel1.getSubstation() == substation1);
        assertTrue(voltageLevel1.getTopologyKind() == TopologyKind.NODE_BREAKER);

        NodeBreakerView topology1 = voltageLevel1.getNodeBreakerView();
        assertTrue(topology1.getNodeCount() == 10);
        assertTrue(Iterables.size(topology1.getBusbarSections()) == 2);
        assertTrue(topology1.getBusbarSectionCount() == 2);

        BusbarSection voltageLevel1BusbarSection1 = topology1.getBusbarSection("voltageLevel1BusbarSection1");
        assertTrue(voltageLevel1BusbarSection1 != null);
        assertTrue(voltageLevel1BusbarSection1.getId().equals("voltageLevel1BusbarSection1"));

        BusbarSection voltageLevel1BusbarSection2 = topology1.getBusbarSection("voltageLevel1BusbarSection2");
        assertTrue(voltageLevel1BusbarSection2 != null);
        assertTrue(voltageLevel1BusbarSection2.getId().equals("voltageLevel1BusbarSection2"));
        assertTrue(Iterables.size(topology1.getSwitches()) == 5);
        assertTrue(topology1.getSwitchCount() == 5);

        Switch voltageLevel1Breaker1 = topology1.getSwitch("voltageLevel1Breaker1");
        assertTrue(voltageLevel1Breaker1 != null);
        assertTrue(voltageLevel1Breaker1.getId().equals("voltageLevel1Breaker1"));
        assertTrue(!voltageLevel1Breaker1.isOpen());
        assertTrue(voltageLevel1Breaker1.isRetained());
        assertTrue(voltageLevel1Breaker1.getKind() == SwitchKind.BREAKER);
        assertTrue(topology1.getNode1(voltageLevel1Breaker1.getId()) == voltageLevel1BusbarSection1.getTerminal().getNodeBreakerView().getNode());
        assertTrue(topology1.getNode2(voltageLevel1Breaker1.getId()) == voltageLevel1BusbarSection2.getTerminal().getNodeBreakerView().getNode());
        assertTrue(Iterables.size(voltageLevel1.getLoads()) == 1);
        assertTrue(voltageLevel1.getLoadCount() == 1);

        Load load1 = network.getLoad("load1");
        assertTrue(load1 != null);
        assertTrue(load1.getId().equals("load1"));
        assertTrue(load1.getTerminal().getNodeBreakerView().getNode() == 2);
        assertTrue(load1.getP0() == 10);
        assertTrue(load1.getQ0() == 3);

        Generator generator1 = network.getGenerator("generator1");
        assertTrue(generator1 != null);
        assertTrue(generator1.getId().equals("generator1"));
        assertTrue(generator1.getTerminal().getNodeBreakerView().getNode() == 5);
        assertTrue(generator1.getMinP() == 200);
        assertTrue(generator1.getMaxP() == 900);
        assertTrue(generator1.getEnergySource() == EnergySource.NUCLEAR);
        assertTrue(generator1.isVoltageRegulatorOn());
        assertTrue(generator1.getTargetP() == 900);
        assertTrue(generator1.getTargetV() == 380);
        ReactiveCapabilityCurve rcc1 = generator1.getReactiveLimits(ReactiveCapabilityCurve.class);
        assertTrue(rcc1.getPointCount() == 2);
        assertTrue(rcc1.getMaxQ(500) == 500);
        assertTrue(rcc1.getMinQ(500) == 300);

        assertTrue(Iterables.size(voltageLevel1.getBusBreakerView().getBuses()) == 2);
        Bus busCalc1 = voltageLevel1BusbarSection1.getTerminal().getBusBreakerView().getBus();
        Bus busCalc2 = voltageLevel1BusbarSection2.getTerminal().getBusBreakerView().getBus();
        assertTrue(load1.getTerminal().getBusBreakerView().getBus() == busCalc1);
        assertTrue(generator1.getTerminal().getBusBreakerView().getBus() == busCalc2);
        assertTrue(busCalc1.getConnectedComponent().getNum() == 0);
        assertTrue(busCalc2.getConnectedComponent().getNum() == 0);

        assertTrue(Iterables.size(voltageLevel1.getBusView().getBuses()) == 1);
        Bus busCalc = voltageLevel1BusbarSection1.getTerminal().getBusView().getBus();
        assertTrue(busCalc == voltageLevel1BusbarSection2.getTerminal().getBusView().getBus());
        assertTrue(load1.getTerminal().getBusView().getBus() == busCalc);
        assertTrue(generator1.getTerminal().getBusView().getBus() == busCalc);
        assertTrue(busCalc.getConnectedComponent().getNum() == 0);
    }

    @Test
    public void testVoltageLevelGetConnectable() {
        Network n = EurostagTutorialExample1Factory.create();
        assertTrue(n.getVoltageLevel("VLLOAD").getConnectable("LOAD", Load.class) != null);
        assertTrue(n.getVoltageLevel("VLLOAD").getConnectable("NHV2_NLOAD", TwoTerminalsConnectable.class) != null);
        assertTrue(n.getVoltageLevel("VLGEN").getConnectable("LOAD", Load.class) == null);
        assertTrue(n.getVoltageLevel("VLGEN").getConnectable("NHV2_NLOAD", TwoTerminalsConnectable.class) == null);
    }

}

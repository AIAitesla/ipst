/**
 * Copyright (c) 2016, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package eu.itesla_project.iidm.network;

/**
 * @author Geoffroy Jamgotchian <geoffroy.jamgotchian at rte-france.com>
 */
public interface StaticVarCompensatorAdder extends SingleTerminalConnectableAdder<StaticVarCompensatorAdder> {

    StaticVarCompensatorAdder setBmin(float bMin);

    StaticVarCompensatorAdder setBmax(float bMax);

    StaticVarCompensatorAdder setVoltageSetPoint(float voltageSetPoint);

    StaticVarCompensatorAdder setReactivePowerSetPoint(float reactivePowerSetPoint);

    StaticVarCompensatorAdder setRegulationMode(StaticVarCompensator.RegulationMode regulationMode);

    StaticVarCompensator add();
}

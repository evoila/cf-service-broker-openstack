package de.evoila.cf.broker.util.ServiceInstanceUtilsTest;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import de.evoila.cf.broker.model.catalog.ServerAddress;
import de.evoila.cf.broker.util.ServiceInstanceUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HostListTest extends BaseTest {

    @Test
    void withNullList() {
        String result = ServiceInstanceUtils.hostList(null);
        assertEquals("", result);
    }

    @Test
    void withEmptyList() {
        String result = ServiceInstanceUtils.hostList(Collections.emptyList());
        assertEquals("", result);
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    @Nested
    class withNotFullySetServerAddresses {

        private List<ServerAddress> inputList = List.of(new ServerAddress("OnlyName1"),
                                                        new ServerAddress("OnlyName2"),
                                                        new ServerAddress("OnlyName3"));

        @Test
        void withOne() {
            List<ServerAddress> oneItemList = inputList.subList(0, 1);
            String result = ServiceInstanceUtils.hostList(oneItemList);
            assertEquals("", result);
        }

        @Test
        void withThree() {
            String result = ServiceInstanceUtils.hostList(inputList);
            assertEquals("", result);
        }

    }

    @SuppressWarnings("InnerClassMayBeStatic")
    @Nested
    class withFullySetServerAddresses {

        private List<ServerAddress> inputList = List.of(new ServerAddress("Name1", "ip1", 1),
                                                        new ServerAddress("Name2", "ip2", 2),
                                                        new ServerAddress("Name3", "ip3", 3));

        @Test
        void withOne() {
            List<ServerAddress> oneItemList = inputList.subList(0, 1);
            String result = ServiceInstanceUtils.hostList(oneItemList);
            assertEquals("ip1", result);
        }

        @Test
        void withThree() {
            String result = ServiceInstanceUtils.hostList(inputList);
            assertEquals("ip1,ip2,ip3", result);
        }

    }

}

package org.atlas.service.catalog.port.inbound.master;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.catalog.port.inbound.master.ListBrandUseCase.Output;

import java.util.List;

public interface ListBrandUseCase extends UseCase<Void, Output> {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Output {

        private List<Output.Brand> brands;

        @Data
        public static class Brand {

            private Integer id;
            private String name;
        }
    }
}

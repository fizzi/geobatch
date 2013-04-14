package it.geosolutions.geobatch.beam;

import java.io.File;
import java.io.IOException;

public class BeamGeorectifyTest {

    static class ConfigSamplePair {
        public ConfigSamplePair(String file, BeamGeorectifierConfiguration config) {
            super();
            this.file = file;
            this.config = config;
        }

        String file;

        BeamGeorectifierConfiguration config;
    }

    public static void main(String[] args) throws IOException {
        BeamGeorectifierConfiguration configuration = new BeamGeorectifierConfiguration();
        configuration.setFilterInclude(false);
        configuration.setGeophysics(false);
        configuration.setOutputFolder("c:\\data\\output\\NEWCODE_DIM");
        configuration.setOutputFormat("NETCDF");

        final ConfigSamplePair[] cases = new ConfigSamplePair[] {
//                new ConfigSamplePair(
//                        "c:/data/dlr/W_XX-EUMETSAT-Darmstadt,VIS+IR+IMAGERY,MSG2+SEVIRI_C_EUMG_20120830211510.nc", // Meteosat MSG2
//                        configuration.clone()),
//                new ConfigSamplePair(
//                        "c:/data/dlr/W_XX-EUMETSAT-Darmstadt,HYPERSPECT+SOUNDING,METOPA+IASI_C_EUMP_20121120113254_31593_eps_o_l1c.nc", // METOPA IASI
//                                                                                                                                        // L1C
//                        configuration.clone()),
                new ConfigSamplePair(
                        "c:/data/dlr/W_XX-EUMETSAT-Darmstadt,IASI,METOPA+IASI_C_EUMP_20121120062959_31590_eps_o_l2.nc", // METOPA IASI L2
                        configuration.clone()),
//                new ConfigSamplePair(
//                        "c:/data/dlr/W_XX-EUMETSAT-Darmstadt,SURFACE+SATELLITE,METOPA+ASCAT_C_EUMP_20110620020000_24214_eps_o_125_l1.nc", // METOPA
//                                                                                                                                          // ASCAT L1
//                        configuration.clone()),
//                new ConfigSamplePair(
//                        "c:/data/dlr/W_XX-EUMETSAT-Darmstadt,SURFACE+SATELLITE,METOPA+ASCAT_C_EUMP_20110620020000_24214_eps_o_125_ssm_l2.nc", // METOPA
//                                                                                                                                              // ASCAT
                                                                                                                                              // L2
//                        configuration.clone()),

        };

        for (ConfigSamplePair singlecase : cases) {
            if (singlecase.file.endsWith("C_EUMP_20121120062959_31590_eps_o_l2.nc")) {
            singlecase.config.setDimensions("nlt, nlq, new, nlo, surf_temp, cloud_formations");
            }
            BeamGeorectifier georectifier = new BeamGeorectifier(singlecase.config);

            // configuration.setGeophysics(false);
            georectifier.georectify(new File(singlecase.file));
        }
    }
}

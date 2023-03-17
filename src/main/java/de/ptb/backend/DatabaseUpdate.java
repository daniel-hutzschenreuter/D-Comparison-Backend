package de.ptb.backend;

import de.ptb.backend.model.FundamentalConstant;
import de.ptb.backend.repository.FundamentalConstantRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@AllArgsConstructor
public class DatabaseUpdate implements CommandLineRunner {
    private FundamentalConstantRepository fundamentalConstantRepository;

    @Override
    public void run(String... args) throws Exception {
        FundamentalConstant PlanckConstant = new FundamentalConstant(null, "si:caesium_transition_frequency:2019",
                "https://siunits.stuchalk.domains.unf.edu/si/si:definingconstant:caesium_transition_frequency:2019",
                true, "hyperfine transition frequency of Cs-133",
                "frequence", 9192631770.0, "\\hertz", new Date(), 0,
                "normal");
        FundamentalConstant hyperfineTransitionFrequency = new FundamentalConstant(null, "si:definingconstant:planck_constant:2019",
                "https://siunits.stuchalk.domains.unf.edu/si/si:definingconstant:planck_constant:2019",
                true, "Planck constant",
                "action", 6.62607015e-34, "\\joule\\hertz\\tothe{-1}", new Date(), 0,
                "normal");
        FundamentalConstant speedOfLight = new FundamentalConstant(null, "si:speed_of_light_vacuum:2019",
                "https://siunits.stuchalk.domains.unf.edu/si/si:definingconstant:speed_of_light_vacuum:2019",
                true, "speed of light in vacuum",
                "speed", 299792458.0, "\\metre\\second\\tothe{-1}", new Date(), 0,
                "normal");
        FundamentalConstant elementaryCharge = new FundamentalConstant(null, "si:elementary_charge:2019",
                "https://siunits.stuchalk.domains.unf.edu/si/si:definingconstant:elementary_charge:2019",
                true, "elementary charge",
                "electric charge", 1.602176634e-19, "\\coulomb", new Date(), 0,
                "normal");
        FundamentalConstant boltzmannConstant = new FundamentalConstant(null, "si:boltzmann_constant:2019",
                "https://siunits.stuchalk.domains.unf.edu/si/si:definingconstant:boltzmann_constant:2019",
                true, "Boltzmann constant",
                "heat capacity", 1.380649e-23, "\\joule\\kelvin\\tothe{-1}", new Date(), 0,
                "normal");
        FundamentalConstant avogadroConstant = new FundamentalConstant(null, "si:avogadro_constant:2019",
                "https://siunits.stuchalk.domains.unf.edu/si/si:definingconstant:boltzmann_constant:2019",
                true, "Avogadro constant",
                "inverse amount of substance", 6.02214076e23, "\\mole\\tothe{-1}", new Date(), 0,
                "normal");
        FundamentalConstant luminousEfficacy = new FundamentalConstant(null, "si:luminous_efficacy:2019",
                "https://siunits.stuchalk.domains.unf.edu/si/si:definingconstant:luminous_efficacy:2019",
                true, "luminous efficacy",
                "luminous efficacy", 683.0, "\\lumen\\watt\\tothe{-1}", new Date(), 0,
                "normal");
        fundamentalConstantRepository.save(PlanckConstant);
        fundamentalConstantRepository.save(hyperfineTransitionFrequency);
        fundamentalConstantRepository.save(speedOfLight);
        fundamentalConstantRepository.save(elementaryCharge);
        fundamentalConstantRepository.save(boltzmannConstant);
        fundamentalConstantRepository.save(avogadroConstant);
        fundamentalConstantRepository.save(luminousEfficacy);
    }
}

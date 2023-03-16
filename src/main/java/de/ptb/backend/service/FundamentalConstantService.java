package de.ptb.backend.service;

import de.ptb.backend.dtos.ConstantDto;
import de.ptb.backend.dtos.FundamentalConstantDto;

import java.util.List;

public interface FundamentalConstantService {
    List<FundamentalConstantDto> getFundamentalConstantData();
    List<ConstantDto> getConstantData();
    List<FundamentalConstantDto> getFundamentalConstantsByPid(String pid);
}

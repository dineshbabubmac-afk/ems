package com.annztech.rewardsystem.common.repsoitory;

public interface SequenceGeneratorRepository {
    Long getNextSequence(String sequenceName);
}

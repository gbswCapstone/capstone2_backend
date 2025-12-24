package Capstone.capstoneProject.service;

import Capstone.capstoneProject.enums.HistoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsageCreatedEvent {
    private final Long userId;
    private final HistoryType historyType;
}

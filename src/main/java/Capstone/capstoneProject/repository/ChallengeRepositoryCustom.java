package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.challenges.Challenges;
import Capstone.capstoneProject.enums.SortType;
import Capstone.capstoneProject.enums.UserJobs;

import java.util.List;

public interface ChallengeRepositoryCustom {
    List<Challenges> searchDynamic(String hashtag, String keyword, SortType sortType, UserJobs userJobs);
}

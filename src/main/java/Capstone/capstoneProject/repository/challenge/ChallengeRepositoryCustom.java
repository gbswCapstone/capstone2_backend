package Capstone.capstoneProject.repository.challenge;

import Capstone.capstoneProject.entity.challenge.Challenges;
import Capstone.capstoneProject.enums.SortType;
import Capstone.capstoneProject.enums.UserJobs;

import java.util.List;

public interface ChallengeRepositoryCustom {
    List<Challenges> searchDynamic(String hashtag, String keyword, SortType sortType, UserJobs userJobs);
}

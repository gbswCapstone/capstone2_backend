package Capstone.capstoneProject.repository.mission;

import Capstone.capstoneProject.entity.mission.UserMissions;
import Capstone.capstoneProject.entity.user.Users;
import Capstone.capstoneProject.enums.DateSortType;
import Capstone.capstoneProject.enums.MissionCategory;

import java.util.List;

public interface UserMissionRepositoryCustom {
    List<UserMissions> findAllByUsersWithMissions(Users user, MissionCategory category, DateSortType sortType);

}

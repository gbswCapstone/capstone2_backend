package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Missions.UserMissions;
import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.enums.DateSortType;
import Capstone.capstoneProject.enums.MissionCategory;

import java.util.List;

public interface UserMissionRepositoryCustom {
    List<UserMissions> findAllByUsersWithMissions(Users user, MissionCategory category, DateSortType sortType);

}

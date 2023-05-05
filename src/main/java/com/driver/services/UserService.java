package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository

        User user = userRepository.findById(userId).get();

        int userAge = user.getAge();

        SubscriptionType userSubscriptionType = user.getSubscription().getSubscriptionType();

        List<WebSeries> webSeriesList = webSeriesRepository.findAll();

        Integer webSeriesCount = 0;

        for (WebSeries webSeries : webSeriesList) {
            if (userAge < webSeries.getAgeLimit()) continue;

            SubscriptionType webSeriesSubscritionType = webSeries.getSubscriptionType();

            switch (webSeriesSubscritionType){

                case BASIC:{
                    if(userSubscriptionType == SubscriptionType.BASIC || userSubscriptionType == SubscriptionType.PRO || userSubscriptionType == SubscriptionType.ELITE){
                        webSeriesCount++;
                    }
                }

                case PRO:{
                    if(userSubscriptionType == SubscriptionType.PRO || userSubscriptionType == SubscriptionType.ELITE){
                        webSeriesCount++;
                    }
                }

                case ELITE:{
                    if(userSubscriptionType == SubscriptionType.ELITE){
                        webSeriesCount++;
                    }
                }
            }
        }

        return webSeriesCount;
    }


}

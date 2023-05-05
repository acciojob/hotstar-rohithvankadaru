package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        User user = userRepository.findById(subscriptionEntryDto.getUserId()).get();

        int totalAmount = 0;

        if(subscriptionEntryDto.getSubscriptionType() == SubscriptionType.BASIC){
            totalAmount = (200 * subscriptionEntryDto.getNoOfScreensRequired()) + 500;
        }
        else if (subscriptionEntryDto.getSubscriptionType() == SubscriptionType.PRO){
            totalAmount = (250 * subscriptionEntryDto.getNoOfScreensRequired()) + 800;
        }
        else {
            totalAmount = (350 * subscriptionEntryDto.getNoOfScreensRequired()) + 1000;
        }

        Subscription subscription = new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        subscription.setStartSubscriptionDate(new Date());
        subscription.setTotalAmountPaid(totalAmount);
        subscription.setUser(user);
        user.setSubscription(subscription);

        userRepository.save(user);

        return totalAmount;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository

        User user = userRepository.findById(userId).get();

        Subscription subscription = user.getSubscription();

        if (subscription.getSubscriptionType() == SubscriptionType.ELITE){
            throw new Exception("Already the best Subscription");
        }

        int upgradeAmount = 0;

        if (subscription.getSubscriptionType() == SubscriptionType.PRO){
            subscription.setSubscriptionType(SubscriptionType.ELITE);
            upgradeAmount += 200;
            upgradeAmount += (100 * subscription.getNoOfScreensSubscribed());
            subscription.setTotalAmountPaid(upgradeAmount + subscription.getTotalAmountPaid());
            userRepository.save(user);
            return upgradeAmount;
        }

            subscription.setSubscriptionType(SubscriptionType.PRO);
            upgradeAmount += 300;
            upgradeAmount += (50 * subscription.getNoOfScreensSubscribed());
            subscription.setTotalAmountPaid(upgradeAmount + subscription.getTotalAmountPaid());
            userRepository.save(user);
            return upgradeAmount;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb

        List<Subscription> subscriptions = subscriptionRepository.findAll();

        Integer totalRevenue = 0;

        for (Subscription subscription : subscriptions) {
            totalRevenue += subscription.getTotalAmountPaid();
        }

        return totalRevenue;
    }

}

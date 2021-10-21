package es.gobcan.istac.edatos.publisher.web.config;

import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;
import es.gobcan.istac.edatos.publisher.web.job.SendNoticeOfSubscriptionsJob;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class QuartzSendNoticeOfSubscriptionsJobs {

    private final MetadataConfigurationService metadataService;

    public QuartzSendNoticeOfSubscriptionsJobs(MetadataConfigurationService metadataService) {
        this.metadataService = metadataService;
    }

    @Bean(name = "sendNoticeOfSubscriptions")
    public JobDetailFactoryBean jobCreateNoticeSubscription() {
        return QuartzConfiguration.createJobDetail(SendNoticeOfSubscriptionsJob.class, QuartzConstants.DATASETS_SUBSCRIPTIONS_JOB, QuartzConstants.DATASETS_SUBSCRIPTIONS_JOB_DESCRIPTION);
    }

    @Bean(name = "sendNoticeOfSubscriptionsTrigger")
    public CronTriggerFactoryBean triggerMemberClassStats(@Qualifier("sendNoticeOfSubscriptions") JobDetail jobDetail) {
        return QuartzConfiguration.createCronTrigger(jobDetail, metadataService.retrieveCronExpressionSendNoticeJob(), QuartzConstants.DATASETS_SUBSCRIPTIONS_TRIGGER);
    }
}

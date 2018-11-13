package de.evoila.cf.broker.service.impl;

import de.evoila.cf.broker.model.JobProgress;
import de.evoila.cf.broker.model.Plan;
import de.evoila.cf.broker.model.ServiceInstance;
import de.evoila.cf.broker.service.AsyncDeploymentService;
import de.evoila.cf.broker.service.JobProgressService;
import de.evoila.cf.broker.service.PlatformService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AsyncDeploymentServiceImpl implements AsyncDeploymentService {

	Logger log = LoggerFactory.getLogger(AsyncDeploymentServiceImpl.class);

	private JobProgressService progressService;

	public AsyncDeploymentServiceImpl(JobProgressService progressService) {
		this.progressService = progressService;
	}

	@Async
	@Override
	public void asyncCreateInstance(DeploymentServiceImpl deploymentService, ServiceInstance serviceInstance,
                                    Map<String, Object> parameters, Plan plan, PlatformService platformService) {
		progressService.startJob(serviceInstance, "Start creating service..", JobProgress.PROVISION);

		try {
            deploymentService.syncCreateInstance(serviceInstance, parameters, plan, platformService);
		} catch (Exception e) {
			progressService.failJob(serviceInstance,
					"Internal error during Instance creation, please contact our support.");

			log.error("Exception during Instance creation", e);
			return;
		}
		progressService.succeedProgress(serviceInstance, "Instance successfully created");
	}

    @Async
    @Override
    public void asyncUpdateInstance(DeploymentServiceImpl deploymentService, ServiceInstance serviceInstance,
                                    Map<String, Object> parameters, Plan plan, PlatformService platformService) {
        progressService.startJob(serviceInstance, "Start updating service..", JobProgress.UPDATE);

        try {
            deploymentService.syncUpdateInstance(serviceInstance, parameters, plan, platformService);
        } catch (Exception e) {
            progressService.failJob(serviceInstance,
                    "Internal error during Instance creation, please contact our support.");

            log.error("Exception during Instance creation", e);
            return;
        }
        progressService.succeedProgress(serviceInstance, "Instance successfully updated");
    }

	@Async
	@Override
	public void asyncDeleteInstance(DeploymentServiceImpl deploymentService,
			ServiceInstance serviceInstance, Plan plan, PlatformService platformService) {
		progressService.startJob(serviceInstance, "Start updating service..", JobProgress.DELETE);

		try {
            deploymentService.syncDeleteInstance(serviceInstance, plan, platformService);

		} catch (Exception e) {
			progressService.failJob(serviceInstance,
					"Internal error during Instance deletion, please contact our support.");

			log.error("Exception during Instance deletion", e);
			return;
		}
		progressService.succeedProgress(serviceInstance, "Instance successfully deleted");
	}

	@Override
	public JobProgress getProgress(String serviceInstanceId) {
		try {
			return progressService.getProgress(serviceInstanceId);
		} catch (Exception e) {
			log.error("Error during job progress retrieval", e);
			return new JobProgress(JobProgress.UNKNOWN, JobProgress.UNKNOWN, "Error during job progress retrieval");
		}
	}

}

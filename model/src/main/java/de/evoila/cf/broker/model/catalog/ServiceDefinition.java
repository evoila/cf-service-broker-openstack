package de.evoila.cf.broker.model.catalog;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.evoila.cf.broker.exception.ServiceDefinitionPlanDoesNotExistException;
import de.evoila.cf.broker.model.DashboardClient;
import de.evoila.cf.broker.model.catalog.plan.Plan;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A service offered by this broker.
 *
 * @author sgreenberg@gopivotal.com
 * @author Johannes Hiemer.
 * @author Marco Di Martino.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceDefinition {

    private String id;

    private String name;

    private String description;

    private boolean bindable;

    private List<Plan> plans = new ArrayList<>();

    private List<String> tags = new ArrayList<>();

    private ServiceMetadata metadata;

    private List<String> requires = new ArrayList<>();

    private Dashboard dashboard;

    @JsonProperty("instances_retrievable")
    private boolean instancesRetrievable;

    @JsonProperty("bindings_retrievable")
    private boolean bindingsRetrievable;

    @JsonProperty("dashboard_client")
    private DashboardClient dashboardClient;

    @JsonProperty("plan_updateable") // misspelling of attribute kept, do not change it
    private boolean updateable;

    @JsonSerialize
    @JsonProperty("allow_context_updates")
    private boolean allowContextUpdates;

    public ServiceDefinition() {
    }

    public ServiceDefinition(String id, String name, String description, boolean bindable, List<Plan> plans, boolean updatable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.bindable = bindable;
        this.setPlans(plans);
        this.updateable = updatable;
    }

    public ServiceDefinition(String id, String name, String description, boolean bindable, List<Plan> plans, boolean updatable, List<String> requires) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.bindable = bindable;
        this.updateable = updatable;
        this.setPlans(plans);
        this.setRequires(requires);
    }

    public ServiceDefinition(String id, String name, String description, boolean bindable, List<Plan> plans, boolean updatable,
                             List<String> tags, ServiceMetadata metadata, List<String> requires, boolean instancesRetrievable, boolean bindingsRetrievable) {
        this(id, name, description, bindable, plans, updatable);
        setTags(tags);
        setMetadata(metadata);
        setRequires(requires);
        setInstancesRetrievable(instancesRetrievable);
        setBindingsRetrievable(bindingsRetrievable);
    }

    public ServiceDefinition(String id, String name, String description, boolean bindable, List<Plan> plans) {
        this(id, name, description, bindable, plans, false);
    }

    public boolean isBindingsRetrievable() {
        return bindingsRetrievable;
    }

    public boolean specificPlanIsUpdatable(String planId) throws ServiceDefinitionPlanDoesNotExistException {
        Plan plan = plans.stream().filter(plan1 -> plan1.getId().equals(planId))
                .findFirst().orElseThrow(() -> new ServiceDefinitionPlanDoesNotExistException(this.id, planId));

        if (plan.isPlanUpdateable() == null) {
            return this.isUpdateable();
        } else {
            return plan.isPlanUpdateable();
        }
    }

    public void setBindingsRetrievable(boolean bindingsRetrievable) {
        this.bindingsRetrievable = bindingsRetrievable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isBindable() {
        return bindable;
    }

    public void setBindable(boolean bindable) {
        this.bindable = bindable;
    }

    public boolean isUpdateable() {
        return updateable;
    }

    public void setPlanUpdateable(boolean updatable) {
        this.updateable = updatable;
    }

    public List<Plan> getPlans() {
        return plans;
    }

    public void setPlans(List<Plan> plans) {
        this.plans = plans;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getRequires() {
        return requires;
    }

    public void setRequires(List<String> requires) {
        this.requires = requires;
    }

    public ServiceMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ServiceMetadata metadata) {
        this.metadata = metadata;
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    public DashboardClient getDashboardClient() {
        return dashboardClient;
    }

    public void setDashboardClient(DashboardClient dashboardClient) {
        this.dashboardClient = dashboardClient;
    }

    public boolean isInstancesRetrievable() {
        return instancesRetrievable;
    }

    public void setInstancesRetrievable(boolean instancesRetrievable) {
        this.instancesRetrievable = instancesRetrievable;
    }

    public boolean isAllowContextUpdates() {
        return allowContextUpdates;
    }

    public void setAllowContextUpdates(boolean allowContextUpdates) {
        this.allowContextUpdates = allowContextUpdates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        ServiceDefinition that = (ServiceDefinition) o;
        return bindable == that.bindable &&
               instancesRetrievable == that.instancesRetrievable &&
               bindingsRetrievable == that.bindingsRetrievable &&
               updateable == that.updateable &&
               allowContextUpdates == that.allowContextUpdates &&
               id.equals(that.id) &&
               name.equals(that.name) &&
               description.equals(that.description) &&
               plans.equals(that.plans) &&
               Objects.equals(tags, that.tags) &&
               Objects.equals(metadata, that.metadata) &&
               Objects.equals(requires, that.requires) &&
               Objects.equals(dashboard, that.dashboard) &&
               Objects.equals(dashboardClient, that.dashboardClient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, bindable, plans, tags, metadata, requires, dashboard, instancesRetrievable, bindingsRetrievable, dashboardClient, updateable, allowContextUpdates);
    }

}

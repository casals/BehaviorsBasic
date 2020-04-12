/*
 * Copyright 2019 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.behaviorsBasic.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.assets.management.AssetManager;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.behavior.BehaviorComponent;
import org.terasology.logic.behavior.GroupTagComponent;
import org.terasology.logic.behavior.Interpreter;
import org.terasology.logic.behavior.asset.BehaviorTree;
import org.terasology.logic.behavior.core.Actor;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.registry.In;
import org.terasology.wildAnimals.component.WildAnimalComponent;

import java.util.Optional;

@RegisterSystem(RegisterMode.AUTHORITY)
public class InitialBehaviorSystem extends BaseComponentSystem {

    private static final Logger logger = LoggerFactory.getLogger(InitialBehaviorSystem.class);
    @In
    private EntityManager entityManager;
    @In
    private AssetManager assetManager;

    /**     *
     * Objective: assign the same behavior to entities of the same type (wild animals).
     * Please modify the following code according to the instructions provided in each
     * GCI task.
     *
     * If you are on tasks Behaviors: 4 and Behavior: 5, please check the links provided
     * to know more about identifying and working with groups. Also, be sure to check
     * the WildAnimalsMadness module.
     *
     * @return success message
     */
    @Command(shortDescription = "Assigns the same behavior to all wild animals.")
    public String assignBehavior() {
        String behavior = "BehaviorsBasic:territorialCritter";
        //String behavior = "BehaviorsBasic:hannahsDeer";
        for (EntityRef entityRef : entityManager.getEntitiesWith(WildAnimalComponent.class, BehaviorComponent.class)) {
            logger.info("Assigning behavior to a wild animal based on the following prefab: " + entityRef.getParentPrefab().getName());
            assignBehaviorToEntity(entityRef, behavior);
            logger.info("Behavior assigned: " + behavior);
        }
        return "All wild animals should have the same behavior now.";
    }

    @Command(shortDescription = "Assigns wild animals in the \"territorial\" group the behavior \"territorialCritter\".")
    public String assignGroupBehavior() {
        String group = "territorial";
        String behavior = "Behaviors:critter";
        for (EntityRef entityRef : entityManager.getEntitiesWith(WildAnimalComponent.class, GroupTagComponent.class)) {
            GroupTagComponent groupTag = entityRef.getComponent(GroupTagComponent.class);
            if (!groupTag.groups.contains(group)) {
                continue;
            }

            logger.info("Assigning behavior to a wild animal based on the following prefab: " + entityRef.getParentPrefab().getName());

            if (entityRef.hasComponent(BehaviorComponent.class)) {
                BehaviorComponent behaviorComponent = entityRef.getComponent(BehaviorComponent.class);
                groupTag.backupBT = behaviorComponent.tree;
                groupTag.backupRunningState = new Interpreter(behaviorComponent.interpreter);
                entityRef.saveComponent(groupTag);
            }

            assignBehaviorToEntity(entityRef, behavior);

            logger.info("Behavior assigned: " + behavior);
        }
        return "All the animals in the group should have the same behavior now.";
    }

    private void assignBehaviorToEntity(EntityRef entityRef, String behavior) {
        Optional<BehaviorTree> potentialBehaviorTree = assetManager.getAsset(behavior, BehaviorTree.class);

        if (potentialBehaviorTree.isPresent()) {
            BehaviorComponent behaviorComponent = new BehaviorComponent();

            BehaviorTree newBehaviorTree = potentialBehaviorTree.get();
            behaviorComponent.tree = newBehaviorTree;
            behaviorComponent.interpreter = new Interpreter(new Actor(entityRef));
            behaviorComponent.interpreter.setTree(newBehaviorTree);

            entityRef.addOrSaveComponent(behaviorComponent);

        }
    }

    @Command(shortDescription = "Assigns the robot behavior to all members that are a part of the robo group.")
    public String assignGroupBehavior() {
        String behavior = "BehaviorsBasic:robo";
        for (EntityRef entityRef : entityManager.getEntitiesWith(GroupTagComponent.class))
            if (entityRef.getComponent(GroupTagComponent.class).groups.contains("robot"))
                assignBehaviorToEntity(entityRef, behavior);
        return "All members in the robo group now have the same behavior!";
    }
}

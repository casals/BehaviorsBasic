/*
 * Copyright 2017 MovingBlocks
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
package org.terasology.behaviorsBasic.actions;

import org.terasology.behaviors.components.FindNearbyPlayersComponent;
import org.terasology.behaviors.components.FollowComponent;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.behavior.BehaviorAction;
import org.terasology.logic.behavior.core.Actor;
import org.terasology.logic.behavior.core.BaseAction;
import org.terasology.logic.behavior.core.BehaviorState;
import org.terasology.registry.In;
import org.terasology.wildAnimals.component.WildAnimalComponent;

import java.util.List;


@BehaviorAction(name = "followDeer")
public class FollowDeer extends BaseAction {

    @In
    private EntityManager entityManager;

    @Override
    public void construct(Actor actor) {
        FollowComponent followComponent = new FollowComponent();
        EntityRef temp = null;
        for (EntityRef entityRef : entityManager.getEntitiesWith(WildAnimalComponent.class)) {
            if(entityRef.getParentPrefab().getName().equals("WildAnimals:deer")) {
                temp = entityRef;
                break;
            }
        }
        followComponent.entityToFollow = temp;
        actor.save(followComponent);
    }

    @Override
    public BehaviorState modify(Actor actor, BehaviorState result) {
        return BehaviorState.SUCCESS;
    }
}


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
package org.terasology.behaviorsBasic.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.behaviorsBasic.components.IsStillFriendlyComponent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.behavior.BehaviorAction;
import org.terasology.logic.behavior.core.Actor;
import org.terasology.logic.behavior.core.BaseAction;
import org.terasology.logic.behavior.core.BehaviorState;

@BehaviorAction(name = "is_still_friendly")
public class IsStillFriendlyAction extends BaseAction {
    private boolean isFriendly;

    @Override
    public void construct(Actor actor) {
        if(actor.hasComponent(IsStillFriendlyComponent.class)) {
            EntityRef entityRef = actor.getEntity();
            IsStillFriendlyComponent isStillFriendlyComponent = entityRef.getComponent(IsStillFriendlyComponent.class);
            isStillFriendlyComponent.isFriendly = isFriendly;
            entityRef.saveComponent(isStillFriendlyComponent);
        }
    }

    @Override
    public BehaviorState modify(Actor actor, BehaviorState result) {
        return BehaviorState.SUCCESS;
    }
}
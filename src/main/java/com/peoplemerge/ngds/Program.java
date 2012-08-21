/************************************************************************
** 
** Copyright (C) 2011 Dave Thomas, PeopleMerge.
** All rights reserved.
** Contact: opensource@peoplemerge.com.
**
** This file is part of the NGDS language.
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
**
**    http://www.apache.org/licenses/LICENSE-2.0
**
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
**  
** Other Uses
** Alternatively, this file may be used in accordance with the terms and
** conditions contained in a signed written agreement between you and the 
** copyright owner.
************************************************************************/


package com.peoplemerge.ngds;

import java.util.LinkedList;
import java.util.List;

public class Program {

	private List<Step> steps = new LinkedList<Step>();

	public void addStep(Executable command, AcceptsCommands acceptsCommands) {
		Step step = new Step(command, acceptsCommands);
		steps.add(step);
	}

	public List<Step> getSteps() {
		return steps;
	}
	
	public String display(){
		return toString();
	}
	
	public String toString(){
		String toRun = "";
		for(Step step : steps){
			// Loop through nodes here too?
			// Really need to use the runner!
			for (Node node : step.getNodes()){
				toRun += "on " + node + " run " + step.getCommand();
			}
		}
		return toRun;
	}
	
	public String execute(){
		String log = "";
		// actually do something
		return log;
	}
	
}

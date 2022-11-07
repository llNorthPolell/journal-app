import SimpleInput from '../../util/components/simple-input';
import useSimpleForm from '../../util/hooks/useSimpleForm';
import ObjectiveForm from './objective-form/objective-form';
import {DefaultObjective} from './objective-form/objective-dto';
import FormToolbar from '../../util/components/form-toolbar';
import useError from '../../util/hooks/useError';

import { DefaultGoal } from './goal-dto';
import { v4 as uuidv4 } from 'uuid';

function GoalForm(props) {
    const [formFields,updateForm,submit,resetForm] = useSimpleForm(DefaultGoal,props.submit);
    const [errors, throwError,clearErrors] = useError();

    const handleChange = e=>{
        updateForm({[e.target.name]: e.target.value});
    }

    const handleReset = e => {
        e.preventDefault();
        resetForm();
    }

    const handleSubmit = e => {
        e.preventDefault();
        submit();
    }

    const saveObjective = objective => {
        updateForm({objectives:[...formFields.objectives,{...objective,id: uuidv4()}]});
    }

    const updateObjective = payload =>{
        updateForm({
            objectives: formFields.objectives.map(objective =>
                (objective.id === payload.id) ?
                    payload : objective
            )
        });
    }

    const removeObjective = id =>{
        updateForm({objectives:formFields.objectives.filter(objective=> objective.id!==id)});
    }

    return (
        <div className="pageDiv container">
            <form>
                <SimpleInput
                    id="topicField"
                    value={formFields.topic}
                    fieldName="topic"
                    displayName="Topic"
                    placeholder="Subject of this goal."
                    type="text"
                    handleUpdate={handleChange}/> 
                <SimpleInput
                    id="descriptionField"
                    value={formFields.description}
                    fieldName="description"
                    displayName="Brief Description"
                    placeholder="Describe this goal."
                    type="text"
                    handleUpdate={handleChange}/> 
                <SimpleInput
                    id="assumptionsField"
                    value={formFields.assumptions}
                    fieldName="assumptions"
                    displayName="Assumptions"
                    placeholder="Any assumptions to be made to make this goal achievable?"
                    type="textarea"
                    handleUpdate={handleChange}/> 
                <SimpleInput
                    id="gainsField"
                    value={formFields.gains}
                    fieldName="gains"
                    displayName="Gains"
                    placeholder="What do you gain from this goal?"
                    type="textarea"
                    handleUpdate={handleChange}/> 
                <SimpleInput
                    id="deadlineField"
                    value={formFields.deadline}
                    fieldName="deadline"
                    displayName="Deadline"
                    type="date"
                    handleUpdate={handleChange}/> 

                <div id="objectivesDiv">
                    <ObjectiveForm 
                        data={DefaultObjective} 
                        save={saveObjective} 
                        mode="NEW" />

                    {
                        formFields.objectives.map(objective=>
                            <ObjectiveForm 
                                key={objective.id} 
                                data={objective} 
                                save={updateObjective} 
                                mode="VIEW"
                                remove={removeObjective}/>
                        )
                    }
                </div>
            </form>
            <br/><br/><br/>
            <FormToolbar handleSubmit={handleSubmit} handleReset={handleReset} mode={props.mode}/>
        </div>
    )
}
export default GoalForm
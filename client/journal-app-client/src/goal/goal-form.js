import SimpleInput from '../util/components/simple-input';
import useSimpleForm from '../util/hooks/useSimpleForm';

import { DefaultGoal } from './goal-dto';

function GoalFormPage(props) {
    const [formFields,updateForm,submitNew,submitUpdate,resetForm] = useSimpleForm(DefaultGoal,null,null);
  
    const handleChange = e=>{
        updateForm({[e.target.name]: e.target.value});
    }

    const handleReset = e => {
        e.preventDefault();
        resetForm();
    }

    const handleSubmit = e => {
        submitNew();
    }

    const handleAddObjective = e => {
        e.preventDefault();
        updateForm({objectives:[...formFields.objectives,{record:"Objective", condition:" greater than ", target: "1"}]});
    }

    return (
        <div className="pageDiv container">
            <form>
            <SimpleInput
                id="topicField"
                value={formFields.topic}
                fieldName="topic"
                displayName="Topic"
                type="text"
                handleUpdate={handleChange}></SimpleInput>
            <SimpleInput
                id="descriptionField"
                value={formFields.description}
                fieldName="description"
                displayName="Brief Description"
                type="text"
                handleUpdate={handleChange}></SimpleInput>
            <SimpleInput
                id="assumptionsField"
                value={formFields.assumptions}
                fieldName="assumptions"
                displayName="Assumptions"
                type="textarea"
                handleUpdate={handleChange}></SimpleInput>
            <SimpleInput
                id="deadlineField"
                value={formFields.deadline}
                fieldName="deadline"
                displayName="Deadline"
                type="date"
                handleUpdate={handleChange}></SimpleInput>
                    
            <button onClick={handleAddObjective}>Add Objective</button>
            <button onClick={handleReset}>Reset</button>
            </form>
        </div>
    )
}
export default GoalFormPage
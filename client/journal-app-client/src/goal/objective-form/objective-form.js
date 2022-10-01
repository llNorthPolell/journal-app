import React, { useState } from 'react';
import useSimpleChildForm from '../../util/hooks/useSimpleChildForm';
import SimpleInput from '../../util/components/simple-input';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPencil, faPlus } from '@fortawesome/free-solid-svg-icons'
import TargetForm from './targets/target-form';
import { DefaultTarget } from './targets/target-dto';
import { v4 as uuidv4 } from 'uuid';
import RemoveButton from '../../util/components/remove-button';

function ObjectiveForm(props) {
    const [formFields, updateForm, submit, resetForm] = useSimpleChildForm(props.data, props.save);
    const [mode, setMode] = useState(props.mode);

    const handleChange = e => {
        updateForm({ [e.target.name]: e.target.value });
    }

    const handleClear = e => {
        e.preventDefault();
        console.log("Clear");
    }

    const handleSubmit = e => {
        e.preventDefault();

        const error = submit(e);
        
        if (error)return;

        if (mode==="EDIT")
            setMode("VIEW");
        else if (mode==="NEW")
            resetForm();
    }

    const handleEdit = e => {
        e.preventDefault();
        setMode("EDIT");
    }

    const handleCancelEdit = e => {
        e.preventDefault();
        resetForm();
        setMode("VIEW");
    }

    const handleInsertNewTarget = e => {
        e.preventDefault();
        updateForm({ targetList: [...formFields.targetList, { ...DefaultTarget, id: uuidv4() }] })
    }

    const updateTarget = (e, id) => {
        e.preventDefault();
        let newTargetList = formFields.targetList.map(target =>
            target.id === id ? ({ ...target, [e.target.name]: e.target.value.trim() }) : target);

        updateForm({ targetList: newTargetList });

    }


    const removeTarget = id => {
        updateForm({ targetList: formFields.targetList.filter(target => target.id !== id) });
    }



    return (
        <div className="card">
            <div className="card-header">
                {
                    (mode === "VIEW") ?
                        <>
                            <div className="col">
                                <p className="card-title">{formFields.name}</p>
                            </div>
                            <div className="col">
                                <RemoveButton id={props.data.id} remove={props.remove} className="btn btn-secondary float-end" />
                                <button id={"edit_" + props.data.id} className="btn btn-secondary float-end" onClick={handleEdit}><FontAwesomeIcon icon={faPencil} /></button>
                            </div>
                        </>
                        :
                        <SimpleInput
                            id="nameField"
                            fieldName="name"
                            type="text"
                            value={formFields.name}
                            handleUpdate={handleChange}
                            placeholder="Objective Name"></SimpleInput>


                }

            </div>
            <div className="card-body">
                {
                    (mode === "VIEW") ?
                        <p className="card-text">{formFields.description}</p>
                        :
                        <SimpleInput
                            id="descriptionField"
                            fieldName="description"
                            displayName="Description"
                            placeholder="Describe this objective."
                            type="textarea"
                            value={formFields.description}
                            handleUpdate={handleChange}></SimpleInput>
                }

                <div className="row">
                    <div className="col">
                        <label id="targetGridLabel" htmlFor="targetsDiv" className="form-label underline">Measurable Targets</label>
                    </div>
                    {
                        (mode === "VIEW") ?
                            <></>
                            :
                            <div className="col col-md-4">
                                <button id="newTargetBtn" className="btn link-primary" onClick={handleInsertNewTarget}><FontAwesomeIcon icon={faPlus} /></button>
                            </div>

                    }
                </div>
                
                <div id="targetsDiv">
                    {
                        (formFields.targetList.map(target =>
                            <TargetForm
                                data={target}
                                update={updateTarget}
                                remove={removeTarget}
                                mode={mode} />

                        ))
                    }
                </div>

                <div className="mb-3 row">
                    {
                        (mode === "NEW") ?
                            <>
                                <div className="col">
                                    <button id="clearBodyFormBtn" className="btn btn-outline-secondary" onClick={handleClear}>Reset</button>
                                </div>
                                <div className="col">
                                    <button id="addToBodyBtn" className="btn btn-primary float-end" onClick={handleSubmit}>Add Objective</button>
                                </div>
                            </>

                            :
                            null
                    }

                    {
                        (mode === "EDIT") ?
                            <>
                                <div className="col">
                                    <button id="cancelEditBodyFormBtn" className="btn btn-outline-secondary" onClick={handleCancelEdit}>Cancel</button>
                                </div>
                                <div className="col">
                                    <button id="addToBodyBtn" className="btn btn-primary float-end" onClick={handleSubmit}>Save</button>
                                </div>
                            </>
                            :
                            null
                    }

                </div>
            </div>
        </div>
    )
}
export default ObjectiveForm;
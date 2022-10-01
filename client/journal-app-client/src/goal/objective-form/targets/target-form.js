import RemoveButton from '../../../util/components/remove-button';

const conditionsList = [
    "equals",
    "greater than", 
    "greater than or equals", 
    "less than",
    "less than or equals",
    "not equals"
]

function TargetForm(props) {
    const handleUpdate = e => {
        props.update(e,props.data.id);
    }

    return (
        <div id={'target_' + props.data.id} className="row mb-3">
            <div className="col">
                <input
                    id="targetKeyField"
                    name="targetKey"
                    type="text"
                    className="form-control"
                    value={props.data.targetKey}
                    onChange={handleUpdate}
                    placeholder="Target Key (i.e. Something to Measure Success)"
                    disabled={props.mode === "VIEW"}/>
            </div>
            <div className="col">
                <select
                    id="targetConditionField"
                    name="targetCondition"
                    type="text"
                    className="form-select"
                    value={props.data.targetCondition}
                    onChange={handleUpdate}
                    placeholder="Condition (i.e. =, >, <)"
                    disabled={props.mode === "VIEW"}>
                
                <option value="">Please Select a Condition</option>
                    {
                        conditionsList.map(
                            condition=>
                                <option value={condition}>{condition}</option>
                        )
                    }
                </select>
            </div>
            <div className="col">
                <input
                    id="targetValueField"
                    name="targetValue"
                    type="text"
                    className="form-control"
                    value={props.data.targetValue}
                    onChange={handleUpdate}
                    placeholder="Value (What number wins?)"
                    disabled={props.mode === "VIEW"}/>
            </div>
            {
                (props.mode === "VIEW") ?
                    <></>
                    :
                    <RemoveButton id={props.data.id} className="btn btn-secondary" remove={props.remove}></RemoveButton>
            }

        </div>
    )

}
export default TargetForm
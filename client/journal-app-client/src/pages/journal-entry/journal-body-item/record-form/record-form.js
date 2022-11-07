import RemoveButton from '../../../../util/components/remove-button';

function RecordForm(props) {
    const handleUpdate = e => {
        props.update(e,props.data.id);
    }

    return (
        <div id={'record_' + props.data.id} className="row mb-3">

            <div className="col">
                <input
                    id="recordKeyField"
                    name="recKey"
                    type="text"
                    className="form-control"
                    value={props.data.recKey}
                    onChange={handleUpdate}
                    placeholder="Key"
                    disabled={props.mode === "VIEW"}/>
            </div>
            <div className="col">
                <input
                    id="recordValueField"
                    name="recValue"
                    type="text"
                    className="form-control"
                    value={props.data.recValue}
                    onChange={handleUpdate}
                    placeholder="Value"
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
export default RecordForm
function JournalEntryToolbar(props) {
    return (
        <div id="formControlButtonsDiv" className="container-fluid fixed-bottom bg-dark">
            <br />
            <div className="row">
                <div className="col">
                    <button id="resetEntryFormButton" className="btn btn-danger" onClick={props.handleReset}>Reset</button>
                </div>
                <div className="col">
                    {
                        (props.mode === "NEW") ?
                            <button id="postEntryBtn" className="btn btn-primary float-end" onClick={props.handleSubmit}>Post</button>
                            :
                            (props.mode === "EDIT") ?
                                <button id="updateEntryBtn" className="btn btn-primary float-end" onClick={props.handleSubmit}>Save Changes</button>
                                :
                                <></>
                    }
                </div>
            </div>
        </div>
    );
}
export default JournalEntryToolbar;
import React, {useState} from 'react';
import useSimpleState from '../util/hooks/useSimpleState';
import SimpleInput from '../util/components/simple-input';
import Modal from 'react-bootstrap/Modal'


function NewJournalForm(props) {
    const [newJournal, setNewJournal, handleChangeNewJournal] = useSimpleState("");
    const [permissions, setPermissions, handleChangePermissions] = useSimpleState("private");
    const [show, setShow] = useState(false);

    const handleClose = ()=>{
        setShow(false);
    }

    const handleShow = (e)=>{
        e.preventDefault();
        setShow(true);
    };

    return (
        <div>
            <button className="btn btn-primary" onClick={handleShow}>Create New Journal</button>
            <Modal id="newJournalModal" show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>New Journal</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <SimpleInput
                        id="newJournalField"
                        value={newJournal}
                        fieldName="newJournal"
                        type="text"
                        handleUpdate={handleChangeNewJournal}></SimpleInput>

                    <SimpleInput
                        id="permissionsDropdown"
                        value={permissions}
                        fieldName="permissions"
                        type="select"
                        optionList={["private", "public"]}
                        handleUpdate={handleChangePermissions}></SimpleInput>
                </Modal.Body>
                <Modal.Footer>
                    <button className="btn btn-primary">Create</button>
                </Modal.Footer>

            </Modal>
        </div>

    );
}

export default NewJournalForm;
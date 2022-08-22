import React, {useState} from 'react';
import useSimpleState from '../../util/hooks/useSimpleState';
import SimpleInput from '../../util/components/simple-input';
import Modal from 'react-bootstrap/Modal';
import {useData} from '../../contexts/dataContext';

function NewJournalForm(props) {
    const [newJournal, setNewJournal, handleChangeNewJournal] = useSimpleState("");
    const [permissions, setPermissions, handleChangePermissions] = useSimpleState("private");
    const [journalPic, setJournalPic] = useState();

    const [show, setShow] = useState(false);

    const { createJournal, createWidgetConfig, userId } = useData();

    function reset(){
        setNewJournal("");
        setPermissions("private");
        setJournalPic();
    }


    const handleClose = ()=>{
        setShow(false);
        reset();
    }

    const handleShow = (e)=>{
        e.preventDefault();
        setShow(true);
    };

    const handleChangeJournalPic = (e)=>{
        e.preventDefault();
        setJournalPic(e.target.files[0]);
    }

    const handleSubmit = async (e) =>{
        e.preventDefault();
        const returnedJournal = await createJournal({
            name: newJournal.trim(),
            permissions: permissions,
            img: (journalPic)?journalPic:null,
            creation_timestamp: new Date().toISOString(),
            last_updated: new Date().toISOString(),
            author: userId,
            schemas: []
        });
        await createWidgetConfig({
            position: 0,
            type: "last-entry",
            journal: returnedJournal.key
        });
        reset();
        setShow(false);
    }


    return (
        <div>
            <button className="btn btn-primary" onClick={handleShow}>Create New Journal</button>
            <Modal id="newJournalModal" show={show} onHide={handleClose} backdrop="static">
                <Modal.Header closeButton>
                    <Modal.Title>New Journal</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <form>
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

                        <div className="mb-3">
                            <input id="journalPicField" type="file" name="journalPic" onChange={handleChangeJournalPic} /> 
                        </div>
                    </form>
                </Modal.Body>
                <Modal.Footer>
                    <button className="btn btn-primary" onClick={handleSubmit}>Create</button>
                </Modal.Footer>

            </Modal>
        </div>

    );
}

export default NewJournalForm;
import React, {useState} from 'react';
import Modal from 'react-bootstrap/Modal';

function WidgetMenuModal(props){
    const [show, setShow] = useState(false);
    const [widgetSearchInput, setWidgetSearchInput] = useState("");

    function reset(){

    }


    const handleClose = ()=>{
        setShow(false);
        reset();
    }

    const handleShow = (e)=>{
        e.preventDefault();
        setShow(true);
    };


    const handleChangeWidgetSearchInput = e => {
        setWidgetSearchInput(e.target.value);
    }

    return (
        <div>
            <button className="btn btn-outline-primary" onClick={handleShow}>Add Widget</button>
            <Modal id="widgetMenuModal" show={show} onHide={handleClose} backdrop="static">
                <Modal.Header closeButton>
                    <Modal.Title>New Widget</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <div className="container">
                        <input id="widgetSearchBar" className="form-control" type="text" placeholder="search widgets" value={widgetSearchInput} onChange={handleChangeWidgetSearchInput}/>
                    </div>
                </Modal.Body>
            </Modal>
        </div>
    );
}
export default WidgetMenuModal;
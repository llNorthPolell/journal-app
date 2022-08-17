import React, {useState} from 'react';
import Modal from 'react-bootstrap/Modal';
import WidgetMenuItems from './widget-menu-items.json';
import WidgetMenuForms from './widget-menu-forms';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons'

function WidgetMenuModal(props){
    const [show, setShow] = useState(false);
    const [widgetSearchInput, setWidgetSearchInput] = useState("");
    const [menu, setMenu] = useState("");

    function reset(){
        setWidgetSearchInput("");
        setMenu("");
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

    const handleClickMenuItem = e => {
        e.preventDefault();
        setMenu(e.target.value);
    }

    const handleClickBackButton = e =>{
        e.preventDefault();
        setMenu("");
    }


    return (
        <div>
            <button className="btn btn-outline-primary" onClick={handleShow}>Add Widget</button>
            <Modal id="widgetMenuModal" show={show} onHide={handleClose} backdrop="static">
                <Modal.Header closeButton>
                    <Modal.Title>New Widget</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    {
                        (menu==="")?
                        <div className="container">
                            <input id="widgetSearchBar" className="form-control" type="text" placeholder="search widgets" value={widgetSearchInput} onChange={handleChangeWidgetSearchInput}/>

                            <div id="widgetOptionsDiv" className="container-fluid">
                                <div className="col">
                                    {
                                        WidgetMenuItems.map(widgetMenuItem =>
                                            <div className="row">
                                                <h5 className="widget-category-name">{widgetMenuItem.categoryName}</h5>
                                                
                                                {
                                                    widgetMenuItem.widgetOptions.map(widgetOption => 
                                                        <div className="card widget-card text-center">
                                                            {
                                                                <img
                                                                    className="card-img-top"
                                                                    src={widgetOption.img}
                                                                    alt={widgetOption.displayName}
                                                                />
                                                            }
                                                            <div className="card-body">
                                                                <h2>
                                                                    <button 
                                                                        className="card-title widget-card-link stretched-link btn btn-link" 
                                                                        onClick={handleClickMenuItem} 
                                                                        value={widgetOption.key}
                                                                    >{widgetOption.displayName}</button>
                                                                </h2>
                                                            </div>
                                                        </div>
                                                    )
                                                }
                                            </div>
                                        )
                                    }
                                </div>
                            </div>
                        </div>
                        :
                        <div className="container">
                            <button className="btn btn-link"><FontAwesomeIcon icon={faArrowLeft} onClick={handleClickBackButton}/></button>
                            <WidgetMenuForms menu={menu}></WidgetMenuForms>
                        </div>
                    }
                </Modal.Body>
            </Modal>
        </div>
    );
}
export default WidgetMenuModal;
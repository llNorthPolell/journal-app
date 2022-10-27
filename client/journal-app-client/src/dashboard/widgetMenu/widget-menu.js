import React, {useState} from 'react';
import Modal from 'react-bootstrap/Modal';
import WidgetMenuItems from './widget-menu-items.json';
import WidgetMenuForms from './widget-menu-forms';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons'
import {useParams} from 'react-router-dom';

import {useDashboard} from '../../contexts/dashboardContext';
import useSession from '../../facades/hooks/useSession';

function WidgetMenuModal(props){
    const {journalId} = useParams();

    const [show, setShow] = useState(false);
    const [widgetSearchInput, setWidgetSearchInput] = useState("");
    const [menu, setMenu] = useState(props.menu? props.menu : "");

    const {getOpenDashboardPosition, addNewWidgetConfig} = useDashboard();
    const [currentJournal]=useSession(["currentJournal"])

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

    const clickMenuItem = value => {
        setMenu(value);
    }

    const handleClickBackButton = e =>{
        e.preventDefault();
        setMenu("");
    }

    const close = () => {
        setShow(false);
        reset();
    }

    const submit = output => {
        const processedOutput = {
            ...output,
            journal: journalId,
            position: getOpenDashboardPosition()
        }

        console.log("Final save to database: " + JSON.stringify(processedOutput));
        addNewWidgetConfig(processedOutput);
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

                            <div id="widgetOptionsDiv">
                                    {
                                        WidgetMenuItems.map(widgetMenuItem =>
                                            <div className="row">
                                                <h5 className="widget-category-name">{widgetMenuItem.categoryName}</h5>
                                                
                                                {
                                                    widgetMenuItem.widgetOptions.map(widgetOption => 
                                                        <div className="card widget-card text-center">
                                                            <img
                                                                className="card-img-top"
                                                                src={widgetOption.img}
                                                                alt={widgetOption.displayName}
                                                            />
                                                            <div className="card-body">
                                                                <h5>
                                                                    <a 
                                                                        className="card-title widget-card-link stretched-link" 
                                                                        onClick={()=>clickMenuItem(widgetOption.key)} 

                                                                    >{widgetOption.displayName}</a>
                                                                </h5>
                                                            </div>
                                                        </div>
                                                    )
                                                }
                                            </div>
                                        )
                                    }
                                </div>
                            </div>
                        :
                        <div className="container">
                            <button className="btn btn-link"><span><FontAwesomeIcon icon={faArrowLeft} onClick={handleClickBackButton}/></span></button>
                            <WidgetMenuForms menu={menu} close={close} schemas={currentJournal.schemas} submit={submit}></WidgetMenuForms>
                        </div>
                    }
                </Modal.Body>
            </Modal>
        </div>
    );
}
export default WidgetMenuModal;
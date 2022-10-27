export function processDashboardWidgets(config, journalEntriesList) {
    if (config==null){
        console.log("Error: config is undefined.");
        return;
    } 

    function generateLastEntry(){
        let payload = {
            overview: "You have no journal entries at the moment. Please create one."
        };
        if (journalEntriesList==null || journalEntriesList.length===0) return payload;

        const lastEntry = journalEntriesList[0];
        payload = {
            overview: lastEntry.overview,
            key: lastEntry.key
        }
        return payload;
    }

    function generateLineGraph(widget){
        let payload = {
            title : widget.title,
            labels : widget.labels,
            data : {
                x: [],
                y: []
            }
        };

        widget.data.yValues.forEach(
            widgetYValue => payload.data.y.push({
                ...widgetYValue, data: []
            })
        )

        console.log("line-graph payload:" + JSON.stringify(payload));

        const yValues = widget.data.yValues.map(y =>
        ({
            topic: y.yTopic,
            record: y.yRecord
        })
        );

        const data = journalEntriesList.map(journalEntry =>
        ({
            x: journalEntry[widget.data.xValue],
            y: journalEntry.journalBodyItems.map(journalBodyItem =>
                journalBodyItem.recordList.filter(record =>
                    yValues.some(y => y.topic === journalBodyItem.topic && y.record === record.recKey)
                ).map(record =>
                    ({ topic: journalBodyItem.topic, record: record.recKey, value: record.recValue })
                )
            ).flat()
        })
        );
        console.log("yValues : "+ JSON.stringify(yValues));
        console.log("Data : "+ JSON.stringify(data));
        console.log("PayloadY: " + JSON.stringify(payload.data.y));
        
        data.forEach(data=>{
            payload.data.x.push(data.x);
        });
        

        data.forEach(data=>{
            data.y.forEach(dataY=>{
                const payloadY = payload.data.y.find(y=> y.yTopic===dataY.topic && y.yRecord===dataY.record);
            
                if (payloadY) payloadY.data.push(dataY.value);
                else payload.data.y.find(y=> y.yTopic===dataY.topic && y.yRecord===dataY.record).push(0);
            });
        });


        return payload;
    }

    let output=[];
    config.forEach(
        widget=> {
            const processedWidget={...widget};
            switch(widget.type){
                case "last-entry":
                    processedWidget.payload = generateLastEntry();
                    break;
                case "line-graph":
                    processedWidget.payload = generateLineGraph(processedWidget);
                    break;
                default: 
                    break;
            }

            if (processedWidget.payload)
                output.push(processedWidget)
        }
    )

    return output;
}



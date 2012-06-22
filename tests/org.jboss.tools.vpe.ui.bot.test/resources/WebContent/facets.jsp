<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>

<html>
<head>
<title></title>
<link href="/css/main.css" rel="stylesheet" type="text/css" />
<style type="text/css">
h1 {
	background-color: white;
}

.label {
	font-weight: bold;
}

.whiteLine {
	background-color: white;
	border: 1px solid black;
}

.panel {
	background-color: SteelBlue;
	border: 1px solid black;
	vertical-align: top;
}

.goldPanel {
	background-color: gold;
	border: 1px solid black;
}

.greenPanel {
	background-color: forestGreen;
	border: 1px solid black;
}

.indianRedPanel {
	background-color: IndianRed;
	border: 1px solid black;
}

.thistlePanel {
	background-color: thistle;
	border: 1px solid black;
}

.orangePanel {
	background-color: orange;
	border: 1px solid black;
}

.darkBluePanel {
	background-color: DarkSlateBlue;
	border: 1px solid black;
}

.darkVioletPanel {
	background-color: DarkViolet;
	border: 1px solid black;
}
</style>
</head>
<body>
<f:view>
	<h1>1) h:panelGrid</h1>

	<h:panelGrid columns="2" styleClass="panel">
		<h:panelGrid columns="2" border="5" width="150" rules="all"
			frame="above" cellpadding="4" cellspacing="6" bgcolor="silver"
			style="COLOR: #ff0080; BACKGROUND-COLOR: #ffff00; TEXT-DECORATION: underline; FONT-STYLE: italic; FONT-WEIGHT: bold; FONT-SIZE: medium; FONT-FAMILY: 'Arial Black';"
			styleClass="goldPanel" captionClass="myStyle0" columnClasses=""
			rowClasses="" headerClass="" footerClass="">
			<f:facet name="footer">
				<h:commandButton value="CB">CB</h:commandButton>
				<p>part 111</p>
				<p>part 222</p>
				<input type="button" value="Input Button">Input Button</input>
			Just Text
		</f:facet>
			<h:outputText value="begin" />

		</h:panelGrid>
		<h:panelGrid columns="2" border="5" width="150" rules="all"
			frame="above" cellpadding="4" cellspacing="6" bgcolor="silver"
			style="COLOR: #ff0080; BACKGROUND-COLOR: green; TEXT-DECORATION: underline; FONT-STYLE: italic; FONT-WEIGHT: bold; FONT-SIZE: small; FONT-FAMILY: 'Arial Black';"
			styleClass="greenPanel" captionClass="myStyle0" columnClasses=""
			rowClasses="" headerClass="" footerClass="">
			<f:facet name="footer">
				<p>part 111</p>
				<p>part 222</p>
				<input type="button" value="Input Button">Input Button</input>
			Just Text
		</f:facet>
			<h:outputText value="begin" />
		</h:panelGrid>
	</h:panelGrid>

	<h1>2) h:dataTable</h1>

<h:dataTable border="1">	
	<f:facet name="header">
<h:outputText value="AAAAAAA" />
HHHHHHH1111Text111111
<h:outputText value="BBBBBBBBBBBBBBB" />
HHHHHHH222Text2222222
</f:facet>
</h:dataTable>
	
	<h:panelGrid columns="4" border="1" styleClass="panel">
		<h:panelGroup>
			<h:outputText styleClass="whiteLine"> Correct table </h:outputText>
			<h:dataTable value="#{user.list}" var="item" styleClass="goldPanel">
				<f:facet name="header">
					<h:outputText>Header</h:outputText>
				</f:facet>
				<f:facet name="footer">
					<h:outputText>Footer</h:outputText>
				</f:facet>
				<f:facet name="caption">
					<h:outputText>Caption</h:outputText>
				</f:facet>
				<h:column>
					<h:outputText>Column 1</h:outputText>
				</h:column>
			</h:dataTable>
		</h:panelGroup>

		<h:panelGroup>
			<h:outputText styleClass="whiteLine">  
			Plain HTML in facets with JSF tags
			<p></p>
			And plus the second jsf components in facets
			</h:outputText>
			<h:dataTable value="#{user.list}" var="item" border="1"
				styleClass="greenPanel">
				<f:facet name="header">
					<h:outputText>Header</h:outputText>
					<h:outputText>Header2</h:outputText>
					<p>Part 111</p>
					Some HTML text
				</f:facet>
				<f:facet name="footer">
					<h:outputText>Footer</h:outputText>
					<h:outputText>Footer2</h:outputText>
					<p>Part 111</p>
					Some HTML text
				</f:facet>
				<f:facet name="caption">
					<h:outputText>Caption</h:outputText>
					<p>Part 111</p>
					Some HTML text
				</f:facet>
			</h:dataTable>
		</h:panelGroup>

		<h:panelGroup>
			<h:outputText styleClass="whiteLine"> Only plain HTML in Caption </h:outputText>
			<h:dataTable value="#{user.list}" var="item" border="1"
				styleClass="indianRedPanel">
				<f:facet name="header">
					<h:outputText>Header1</h:outputText>
					<h:outputText>Header2</h:outputText>
					<h:outputText>Header3</h:outputText>
				</f:facet>
				<f:facet name="footer">
					<h:outputText>Footer1</h:outputText>
					<h:outputText>Footer2</h:outputText>
					<h:outputText>Footer3</h:outputText>
				</f:facet>
				<f:facet name="caption">
					<p>Part 111</p>
					Some HTML text
				</f:facet>
				<h:column>
					<h:outputText>Column 1</h:outputText>
				</h:column>
				<h:column>
					<h:outputText>Column 2</h:outputText>
				</h:column>
				<h:column>
					<h:outputText>Column 3</h:outputText>
				</h:column>
			</h:dataTable>
		</h:panelGroup>

		<h:panelGroup>
			<h:outputText styleClass="whiteLine"> Only plain HTML in facets and columns </h:outputText>
			<h:dataTable value="#{user.list}" var="item"
				styleClass="thistlePanel">
				<f:facet name="header">
					<p>Part 111</p>
					Some HTML text
				</f:facet>
				<f:facet name="footer">
					<p>Part 111</p>
					Some HTML text
				</f:facet>
				<f:facet name="caption">
					<p>Part 111</p>
					Some HTML text
				</f:facet>
				<h:column>
					<h:outputText>Column 1</h:outputText>
				</h:column>
				<h:column>
					<h:outputText>Column 2</h:outputText>
				</h:column>
				<h:column>
					<h:outputText>Column 3</h:outputText>
				</h:column>
			</h:dataTable>
		</h:panelGroup>

	</h:panelGrid>

	<h1>3) h:column</h1>
	<h:outputText> Correct table </h:outputText>
	<h:dataTable value="#{user.list}" var="item" styleClass="panel"
		columnClasses="goldPanel">
		<f:facet name="header">
			<h:outputText>Table Header</h:outputText>
		</f:facet>
		<f:facet name="footer">
			<h:outputText>Table Footer</h:outputText>
		</f:facet>
		<f:facet name="caption">
			<h:outputText styleClass="whiteLine">Table Caption</h:outputText>
		</f:facet>
		<h:column footerClass="greenPanel" headerClass="greenPanel">
			<f:facet name="header">
				<h:outputText>Col 1 header1</h:outputText>
				<h:outputText>Col 1 header2</h:outputText>
				<h:outputText>Col 1 header3</h:outputText>
				<span>COL1HEAD</span> C1HText
			</f:facet>
			<f:facet name="footer">
				<h:outputText>Col 1 footer1</h:outputText>
				<h:outputText>Col 1 footer2</h:outputText>
				<h:outputText>Col 1 footer3</h:outputText>
				<span>COL1FOOT</span> C1FText
			</f:facet>
			<h:outputText>[Column 1] </h:outputText>
			<h:outputText value="#{item}" />
		</h:column>
		<h:column footerClass="indianRedPanel" headerClass="indianRedPanel">
			<f:facet name="header">
				<span>COL2HEAD</span> C2FHead
			</f:facet>
			<f:facet name="footer">
				<span>COL2FOOT</span> C2FText
			</f:facet>
			<h:outputText>[Column 2] </h:outputText>
			<h:outputText value="#{item}" />
		</h:column>
		<h:column footerClass="thistlePanel" headerClass="thistlePanel">
			<f:facet name="header">
				<f:facet name="header">
					<span>COL3HEAD</span> C3FHead
			</f:facet>
			</f:facet>
			<f:facet name="footer">
				<h:outputText>Col 3 footer</h:outputText>
			</f:facet>
			<h:outputText>[Column 3] </h:outputText>
			<h:outputText value="#{item}" />
		</h:column>
	</h:dataTable>

	<h1>4) rich:panel</h1>
	<rich:panel>
		<f:facet name="header">
        	PHead111
        	<h:outputText>PanelHeader1</h:outputText>
			<h:outputText>PanelHeader2</h:outputText>
			<h:outputText>PanelHeader3</h:outputText>
			<span>P1Head</span> PHead
        </f:facet>
		<f:facet name="footer">
			<h:outputText>Col 1 footer3</h:outputText>
			<span>COL1FOOT</span> C1FText
        	FooterFacet
        </f:facet>
        	Panel Content
    </rich:panel>

	<h1>5) rich:simpleTogglePanel</h1>
	<rich:simpleTogglePanel id="simpleTogglePanel" switchType="client"
		label="STP LABEL">
		<f:facet name="header">
			STPHead111
        	<h:outputText>STPHeader1</h:outputText>
			<h:outputText>STPHeader2</h:outputText>
			<h:outputText>STPHeader3</h:outputText>
			<span>STP1Head</span> STPHead
		</f:facet>
		<f:facet name="footer">
			STPFoot111
        	<h:outputText>STPFooter1</h:outputText>
			<h:outputText>STPFooter2</h:outputText>
			<h:outputText>STPFooter3</h:outputText>
			<span>STP1Foot</span> STPFoot
		</f:facet>
		[rich:simpleTogglePanel CONTENT]
		</rich:simpleTogglePanel>

	<h1>6) rich:togglePanel</h1>
	<rich:togglePanel switchType="client" stateOrder="closed,tip1,tip2">
		<f:facet name="closed">
			<h:outputText value="Closed" />
			<rich:toggleControl>
				<rich:toggleControl switchToState="tip1" value="#{user.next}" />
			</rich:toggleControl>
		</f:facet>
		<f:facet name="tip1">
			<h:outputText value="TIP 1" />
			<h:panelGrid columns="2">
				<rich:toggleControl switchToState="closed" value="#{user.close}" />
				<rich:toggleControl switchToState="tip2" value="#{user.next}" />
			</h:panelGrid>
		</f:facet>
		<f:facet name="tip2">
			<h:outputText value="TIP 2" />
			<h:panelGrid columns="2">
				<rich:toggleControl switchToState="closed" value="#{user.close}" />
				<rich:toggleControl switchToState="tip1" value="#{user.previous}" />
			</h:panelGrid>
		</f:facet>
	</rich:togglePanel>
	<rich:togglePanel switchType="client" stateOrder="tip2,tip1,closed">
		<f:facet name="closed">
    		STPFoot111
        	<h:outputText>STPFooter1</h:outputText>
			<h:outputText>STPFooter2</h:outputText>
			<h:outputText>STPFooter3</h:outputText>
			<span>STP1Foot</span> STPFoot
		    <h:outputText value="CLOSE" />
		</f:facet>
		<f:facet name="tip1">
			<h:panelGroup>
				<h:outputText value="TIPPPP111" />
				<rich:toggleControl switchToState="tip2" value="#{user.next}" />
				<h:outputText value="TIPPPP111" />
			</h:panelGroup>
			<h:outputText value="TIPPP222222" />
		</f:facet>
		<f:facet name="tip2">
			<h:outputText value="TIP 2" />
		</f:facet>
	</rich:togglePanel>

	<h1>7) tabPanel</h1>
	<h:panelGrid columns="3">

		<rich:tabPanel switchType="ajax">
			<rich:tab label="First">
            Here is tab #1
        </rich:tab>
			<rich:tab label="Second" disabled="true">
            Here is tab #2
        </rich:tab>
			<rich:tab label="Third">
            Here is tab #3
        </rich:tab>
		</rich:tabPanel>

		<rich:tabPanel switchType="ajax">
			<rich:tab label="First">
				<f:facet name="label">
            Tab Label 1
        	<h:outputText>Tab Label 111</h:outputText>
					<h:outputText>Tab Label 222</h:outputText>
					<h:outputText>Tab Label 333</h:outputText>
					<span>TabLabelSpan1 </span> TabLabelSpan2
        </f:facet>
        Here is tab #1
        </rich:tab>
			<rich:tab disabled="true">
				<f:facet name="label">
            Tab Label 2
        	<h:outputText>Tab Label 2-111</h:outputText>
					<h:outputText>Tab Label 2-222</h:outputText>
					<h:outputText>Tab Label 2-333</h:outputText>
					<span>TabLabelSpan2-1 </span> TabLabelSpan2-2
        </f:facet>
        Here is tab #2
        </rich:tab>
			<rich:tab label="Third">
            Here is tab #3
        </rich:tab>
		</rich:tabPanel>
	</h:panelGrid>

	<br></br>
	<h1>8) rich:dataTable</h1>
	<br></br>

	<h:panelGrid styleClass="panel" columns="3">

		<rich:dataTable var="row" value="#{user.list}" rowKeyVar="rowKey"
			rowClasses="goldPanel panel" columns="2">
			<f:facet name="header">
				<rich:columnGroup>
					<rich:column rowspan="2">
						<rich:spacer />
					</rich:column>
					<rich:column colspan="3">
						<h:outputText value="FIELDS" />
					</rich:column>
					<rich:column breakBefore="true">
						<h:outputText value="field1" />
					</rich:column>
					<rich:column>
						<h:outputText value="field2" />
					</rich:column>
					<rich:column>
						<h:outputText value="field3" />
					</rich:column>
				</rich:columnGroup>
			</f:facet>


			<rich:columnGroup>
				<rich:column>
					<h:outputText value="#{rowKey}" />
				</rich:column>
				<rich:column>
					<h:outputText value="#{row}">
					</h:outputText>
				</rich:column>
				<rich:column>
					<h:outputText value="#{row}">
					</h:outputText>
				</rich:column>
				<rich:column>
					<h:outputText value="#{row}">
					</h:outputText>
				</rich:column>
			</rich:columnGroup>

			<f:facet name="footer">

				<rich:columnGroup>
					<rich:column></rich:column>
					<rich:column>
						<h:outputText value="footer1" />
					</rich:column>
					<rich:column>
						<h:outputText value="footer2" />
					</rich:column>
					<rich:column>
						<h:outputText value="footer3" />
					</rich:column>

				</rich:columnGroup>
			</f:facet>

		</rich:dataTable>

		<rich:dataTable var="row" value="#{user.list}" rowKeyVar="rowKey"
			rowClasses="goldPanel panel">
			<f:facet name="caption">
			TableCaptionText
			<h:outputText>Caption 1</h:outputText>
				<h:outputText>Caption 2</h:outputText>
				<h:outputText>Caption 3</h:outputText>
				<span>TableCaption1</span> TableCaption2
		</f:facet>
			<f:facet name="header">
			Table1HText2
			<h:outputText>Table header1</h:outputText>
				<h:outputText>Table header2</h:outputText>
				<h:outputText>Table header3</h:outputText>
				<span>Table1HEAD</span> Table1HText2
		</f:facet>
			<f:facet name="footer">
			TableC1FText2
			<h:outputText>Table FOOTER1</h:outputText>
				<h:outputText>Table FOOTER2</h:outputText>
				<h:outputText>Table FOOTER3</h:outputText>
				<span>Table1FOOT</span> Table1FText2
		</f:facet>

			<rich:column>
				<h:outputText value="#{rowKey}" />
			</rich:column>
			<rich:column>
				<h:outputText value="#{row}">
				</h:outputText>
			</rich:column>
			<rich:column>
				<h:outputText value="#{row}">
				</h:outputText>
			</rich:column>
			<rich:column>
				<h:outputText value="#{row}">
				</h:outputText>
			</rich:column>

		</rich:dataTable>
	</h:panelGrid>

	<br></br>
	<h1>9) rich:column</h1>

	<h:panelGrid styleClass="panel" columns="3">

		<rich:dataTable var="row" value="#{user.list}" rowKeyVar="rowKey"
			rowClasses="greenPanel">
			<f:facet name="header">
			Table1HText2
			<h:outputText>Table header1</h:outputText>
				<h:outputText>Table header2</h:outputText>
				<h:outputText>Table header3</h:outputText>
				<span>Table1HEAD</span> Table1HText2
		</f:facet>
			<f:facet name="footer">
			TableC1FText2
			<h:outputText>Table FOOTER1</h:outputText>
				<h:outputText>Table FOOTER2</h:outputText>
				<h:outputText>Table FOOTER3</h:outputText>
				<span>Table1FOOT</span> Table1FText2
		</f:facet>

			<rich:column>
				<h:outputText value="#{rowKey}" />
			</rich:column>
			<rich:column>
				<f:facet name="header">
					<h:outputText>Col 1 header1</h:outputText>
					<h:outputText>Col 1 header2</h:outputText>
					<h:outputText>Col 1 header3</h:outputText>
					<span>COL1HEAD</span> C1HText
			</f:facet>
				<f:facet name="footer">
					<h:outputText>Col 1 footer1</h:outputText>
					<h:outputText>Col 1 footer2</h:outputText>
					<h:outputText>Col 1 footer3</h:outputText>
					<span>COL1FOOT</span> C1FText
			</f:facet>
				<h:outputText value="[column 1] #{row}">
				</h:outputText>
			</rich:column>
			<rich:column>
				<f:facet name="header">
					<span>COL2HEAD</span> C2FHead
			</f:facet>
				<f:facet name="footer">
					<span>COL2FOOT</span> C2FText
			</f:facet>
				<h:outputText value="[column 2] #{row}">
				</h:outputText>
			</rich:column>
			<rich:column>
				<f:facet name="header">
					<span>COL3HEAD</span> C3Head
				<h:outputText value="[header 3]" />
				</f:facet>
				<f:facet name="footer">
					<span>COL3FOOT</span> C3Foot
				<h:outputText value="[footer 3]" />
				</f:facet>
				<h:outputText value="[column 3] #{row}">
				</h:outputText>
			</rich:column>

		</rich:dataTable>
	</h:panelGrid>

	<h1>10) rich:subTable</h1>
	<h:panelGrid columns="3" styleClass="panel">
		<rich:dataTable value="#{user.users}" var="users" border="1">
			<rich:column colspan="3" styleClass="orangePanel">
				<h:outputText value="#{users.name} #{users.lastName}" />
			</rich:column>

			<rich:subTable value="#{users.list}" var="book1"
				rowClasses="goldPanel, greenPanel">
				<rich:column>
					<h:outputText value="#{book1}" />
				</rich:column>
				<rich:column colspan="2" breakBefore="true">
					<h:outputText value="#{book1}" />
				</rich:column>
				<rich:column>
					<h:outputText value="#{book1}" />
				</rich:column>
			</rich:subTable>

			<rich:subTable value="#{user.list}" var="book2"
				columnClasses="indianRedPanel, thistlePanel">
				<rich:column rowspan="2">
					<h:outputText value="#{book2}" />
				</rich:column>
				<rich:column>
					<h:outputText value="#{book2}" />
				</rich:column>
				<rich:column>
					<h:outputText value="#{book2}" />
				</rich:column>
				<rich:column breakBefore="true">
					<h:outputText value="#{book2}" />
				</rich:column>
				<rich:column rowspan="2">
					<h:outputText value="#{book2}" />
				</rich:column>
				<rich:column breakBefore="true">
					<h:outputText value="#{book2}" />
				</rich:column>
				<rich:column>
					<h:outputText value="#{book2}" />
				</rich:column>
				<rich:column colspan="2" breakBefore="true">
					<h:outputText value="#{book2}" />
				</rich:column>
				<rich:column>
					<h:outputText value="#{book2}" />
				</rich:column>
			</rich:subTable>
		</rich:dataTable>

		<rich:dataTable value="#{user.users}" var="users" border="1">
			<rich:column colspan="3" styleClass="orangePanel">
				<h:outputText value="#{users.name} #{users.lastName}" />
			</rich:column>

			<rich:subTable value="#{users.list}" var="book3"
				rowClasses="goldPanel, greenPanel" headerClass="darkVioletPanel"
				footerClass="darkBluePanel">

				<f:facet name="caption">
			TableCaptionText
			<h:outputText>Caption 1</h:outputText>
					<h:outputText>Caption 2</h:outputText>
					<h:outputText>Caption 3</h:outputText>
					<span>TableCaption1</span> TableCaption2
		</f:facet>
				<f:facet name="header">
			Table1HText2
			<h:outputText>Table header1</h:outputText>
					<h:outputText>Table header2</h:outputText>
					<h:outputText>Table header3</h:outputText>
					<span>Table1HEAD</span> Table1HText2
		</f:facet>
				<f:facet name="footer">
			TableC1FText2
			<h:outputText>Table FOOTER1</h:outputText>
					<h:outputText>Table FOOTER2</h:outputText>
					<h:outputText>Table FOOTER3</h:outputText>
					<span>Table1FOOT</span> Table1FText2
		</f:facet>

				<rich:column>
					<h:outputText value="#{book3}" />
				</rich:column>
				<rich:column colspan="2" breakBefore="true">
					<h:outputText value="#{book3}" />
				</rich:column>
				<rich:column>
					<h:outputText value="#{book3}" />
				</rich:column>
			</rich:subTable>

			<rich:subTable value="#{user.list}" var="book4"
				columnClasses="indianRedPanel, thistlePanel"
				headerClass="darkVioletPanel" footerClass="darkBluePanel">

				<f:facet name="caption">
			TableCaptionText
			<h:outputText>Caption 1</h:outputText>
					<h:outputText>Caption 2</h:outputText>
					<h:outputText>Caption 3</h:outputText>
					<span>TableCaption1</span> TableCaption2
		</f:facet>
				<f:facet name="header">
			Table1HText2
			<h:outputText>Table header1</h:outputText>
					<h:outputText>Table header2</h:outputText>
					<h:outputText>Table header3</h:outputText>
					<span>Table1HEAD</span> Table1HText2
		</f:facet>
				<f:facet name="footer">
			TableC1FText2
			<h:outputText>Table FOOTER1</h:outputText>
					<h:outputText>Table FOOTER2</h:outputText>
					<h:outputText>Table FOOTER3</h:outputText>
					<span>Table1FOOT</span> Table1FText2
		</f:facet>

				<rich:column rowspan="2">
					<h:outputText value="#{book4}" />
				</rich:column>
				<rich:column>
					<h:outputText value="#{book4}" />
				</rich:column>
				<rich:column>
					<h:outputText value="#{book4}" />
				</rich:column>
				<rich:column breakBefore="true">
					<h:outputText value="#{book4}" />
				</rich:column>
				<rich:column rowspan="2">
					<h:outputText value="#{book4}" />
				</rich:column>
				<rich:column breakBefore="true">
					<h:outputText value="#{book4}" />
				</rich:column>
				<rich:column>
					<h:outputText value="#{book4}" />
				</rich:column>
				<rich:column colspan="2" breakBefore="true">
					<h:outputText value="#{book4}" />
				</rich:column>
				<rich:column>
					<h:outputText value="#{book4}" />
				</rich:column>
			</rich:subTable>
		</rich:dataTable>

		<rich:dataTable value="#{user.users}" var="users" border="1">
			<rich:column colspan="4" styleClass="orangePanel">
				<h:outputText value="#{users.name} #{users.lastName}" />
			</rich:column>

			<rich:subTable value="#{users.list}" var="book5"
				rowClasses="goldPanel, greenPanel">
				<rich:column headerClass="darkVioletPanel"
					footerClass="darkBluePanel">
					<f:facet name="header">
						<h:outputText>Col 111111111 header1</h:outputText>
						<h:outputText>Col 111111111 header2</h:outputText>
						<h:outputText>Col 111111111 header3</h:outputText>
						<span>COL1111111111HEAD</span> C1111111111HText
			</f:facet>
					<f:facet name="footer">
						<h:outputText>Col 1111111111 footer1</h:outputText>
						<h:outputText>Col 1111111111 footer2</h:outputText>
						<h:outputText>Col 1111111111 footer3</h:outputText>
						<span>COL1111111111FOOT</span> C1111111111FText
			</f:facet>
					<h:outputText value="#{book5}" />
				</rich:column>
				<rich:column colspan="2" breakBefore="true"
					headerClass="darkVioletPanel" footerClass="darkBluePanel">
					<f:facet name="header">
						<span>COL2HEAD</span> C2FHead
			</f:facet>
					<f:facet name="footer">
						<span>COL2FOOT</span> C2FText
			</f:facet>

					<h:outputText value="#{book5}" />
				</rich:column>
				<rich:column headerClass="darkVioletPanel"
					footerClass="darkBluePanel">
					<f:facet name="header">
						<span>COL3HEAD</span> C3Head
				<h:outputText value="[header 3]" />
					</f:facet>
					<f:facet name="footer">
						<span>COL3FOOT</span> C3Foot
				<h:outputText value="[footer 3]" />
					</f:facet>
					<h:outputText value="#{book5}" />
				</rich:column>
			</rich:subTable>

			<rich:subTable value="#{user.list}" var="book6"
				columnClasses="indianRedPanel, thistlePanel" headerClass="goldPanel"
				footerClass="thistlePanel">
				<rich:column rowspan="2" headerClass="darkVioletPanel"
					footerClass="darkBluePanel">
					<f:facet name="header">
						<h:outputText>Col 12222222 header1</h:outputText>
						<h:outputText>Col 12222222 header2</h:outputText>
						<h:outputText>Col 12222222 header3</h:outputText>
						<span>COL12222222HEAD</span> C12222222HText
			</f:facet>
					<f:facet name="footer">
						<h:outputText>Col 12222222 footer1</h:outputText>
						<h:outputText>Col 12222222 footer2</h:outputText>
						<h:outputText>Col 12222222 footer3</h:outputText>
						<span>COL12222222FOOT</span> C12222222FText
			</f:facet>
					<h:outputText value="#{book6}" />
				</rich:column>
				<rich:column>
					<f:facet name="header">
						<span>COL22222222HEAD</span> C22222222FHead
			</f:facet>
					<f:facet name="footer">
						<span>COL22222222FOOT</span> C22222222FText
			</f:facet>
					<h:outputText value="#{book6}" />
				</rich:column>
				<rich:column>
					<f:facet name="header">
						<span>COL32222222HEAD</span> C32222222Head
				<h:outputText value="[header 3]2222222" />
					</f:facet>
					<f:facet name="footer">
						<span>COL32222222FOOT</span> C32222222Foot
				<h:outputText value="[footer 3]" />
					</f:facet>
					<h:outputText value="#{book6}" />
				</rich:column>
				<rich:column breakBefore="true">
					<h:outputText value="#{book6}" />
				</rich:column>
				<rich:column rowspan="2">
					<h:outputText value="#{book6}" />
				</rich:column>
				<rich:column breakBefore="true">
					<h:outputText value="#{book6}" />
				</rich:column>
				<rich:column>
					<h:outputText value="#{book6}" />
				</rich:column>
				<rich:column colspan="2" breakBefore="true">
					<h:outputText value="#{book6}" />
				</rich:column>
				<rich:column>
					<h:outputText value="#{book6}" />
				</rich:column>
			</rich:subTable>
		</rich:dataTable>
	</h:panelGrid>

	<h1>11) rich:extendedDataTable</h1>
	<h:panelGrid columns="3" styleClass="panel">
		<rich:extendedDataTable value="#{user.list}" var="item1" width="200px"
			height="200px">
			<rich:column>
				<f:facet name="header">
					<h:outputText>Header</h:outputText>
				</f:facet>
				<h:outputText value="#{item1}" />
			</rich:column>
		</rich:extendedDataTable>

		<rich:extendedDataTable value="#{user.list}" var="item" width="500px"
			height="200px">
			<rich:column sortable="false">
				<f:facet name="header">
					<h:outputText value="Flag" />
				</f:facet>
				<f:facet name="footer">
					<h:outputText value="Footer1" />
				</f:facet>
				<h:outputText value="#{item}" />
			</rich:column>
			<rich:column sortable="true">
				<f:facet name="header">
					<h:outputText value="State Name" />
				</f:facet>
				<f:facet name="footer">
					<h:outputText value="Footer2" />
				</f:facet>
				<h:outputText value="#{item}" />
			</rich:column>
			<rich:column>
				<f:facet name="header">
					<h:outputText value="State Capital" />
				</f:facet>
				<f:facet name="footer">
					<h:outputText value="Footer3" />
				</f:facet>
				<h:outputText value="#{item}" />
			</rich:column>
			<rich:column sortable="false">
				<f:facet name="header">
					<h:outputText value="Time Zone" />
				</f:facet>
				<f:facet name="footer">
					<h:outputText value="Footer4" />
				</f:facet>
				<h:outputText value="#{item}" />
			</rich:column>
		</rich:extendedDataTable>

		<rich:extendedDataTable value="#{user.list}" var="item" width="600px"
			height="200px">
			<rich:column sortable="false">
				<f:facet name="header">
					<h:outputText value="F1" />
					<h:outputText value="F2" />
					<h:outputText value="F3" />
					<span>S1</span> T1
			</f:facet>
				<f:facet name="footer">
					<h:outputText value="FF1" />
					<h:outputText value="FF2" />
					<h:outputText value="FF3" />
					<span>SS1</span> TT1
			</f:facet>

				<h:outputText value="#{item}" />
			</rich:column>
			<rich:column sortable="true">
				<f:facet name="header">
					<span>COL2HEAD</span> C2FHead
			</f:facet>
				<f:facet name="footer">
					<span>COL2FOOT</span> C2FText
			</f:facet>
				<h:outputText value="#{item}" />
			</rich:column>
			<rich:column>
				<f:facet name="header">
					<h:outputText value="State Capital" />
				</f:facet>
				<h:outputText value="#{item}" />
			</rich:column>
			<rich:column sortable="false" width="300px">
				<f:facet name="header">
					<h:outputText value="Time Zone" />
					<span>COL3HEAD</span> C3Head
					<h:outputText value="[header 3]" />
				</f:facet>
				<f:facet name="footer">
					<span>COL3FOOT</span> C3Foot
				<h:outputText value="[footer 3]" />
				</f:facet>
				<h:outputText value="#{item}" />
			</rich:column>
		</rich:extendedDataTable>
	</h:panelGrid>

	<h1>12) rich:dataGrid</h1>

	<h:panelGrid columns="2" styleClass="panel">
		<rich:dataGrid value="#{user.list}" var="item" columns="2"
			elements="4">
			<f:facet name="caption">
				<h:outputText value="Caption #{item}"></h:outputText>
			</f:facet>
			<f:facet name="header">
				<h:outputText value="Header #{item}"></h:outputText>
			</f:facet>
			<h:outputText value="#{item}" />
		</rich:dataGrid>

		<rich:dataGrid value="#{user.list}" var="item" columns="3"
			elements="3">
			<f:facet name="caption">
				<p>Part 111</p>
					Some HTML text
				</f:facet>
			<f:facet name="header">
				<span>HEAD</span>
				<h:outputText value="#{item}"></h:outputText>
			</f:facet>
			<h:outputText value="#{item}" />
		</rich:dataGrid>

		<rich:dataGrid value="#{user.list}" var="item" columns="2"
			elements="6">
			<f:facet name="header">
				HEAD
				<h:outputText value="#{item}"></h:outputText>
			</f:facet>
			<h:outputText value="#{item}" />
		</rich:dataGrid>

		<rich:dataGrid value="#{user.list}" var="item" columns="2"
			elements="3">
			<f:facet name="caption">
					CText1
					<h:outputText value="C1" />
				<h:outputText value="C2" />
				<h:outputText value="C3" />
				<span>CapS1</span> CapT1
			</f:facet>
			<f:facet name="header">
					W1
					<h:outputText value="F1" />
				<h:outputText value="F2" />
				<h:outputText value="F3" />
				<span>S1</span> T1
			</f:facet>
			<f:facet name="footer">
					WW1
					<h:outputText value="FF1" />
				<h:outputText value="FF2" />
				<h:outputText value="FF3" />
				<span>SS1</span> TT1
			</f:facet>
			<h:outputText value="#{item}" />
		</rich:dataGrid>
		<rich:dataGrid value="#{user.list}" var="item" columns="2"
			elements="4">
			<h:outputText value="#{item}" />
		</rich:dataGrid>
	</h:panelGrid>

	<h1>13) rich:dataDefinitionList</h1>

	<h:panelGrid columns="3" styleClass="panel">

		<rich:dataDefinitionList var="car" value="#{user.list}" rows="3"
			first="2" title="Cars" styleClass="greenPanel">
			<f:facet name="term">
				<h:outputText value="#{car}"></h:outputText>
			</f:facet>
			<h:outputText value="Price:" styleClass="label"></h:outputText>
			<h:outputText value="#{car}" />
			<br />
			<h:outputText value="Mileage:" styleClass="label"></h:outputText>
			<h:outputText value="#{car}" />
			<br />
		</rich:dataDefinitionList>

		<rich:dataDefinitionList var="car" value="#{user.list}" rows="3"
			first="2" title="Cars" styleClass="greenPanel">
			<f:facet name="term">
				<h:outputText value="[term]"></h:outputText>
			</f:facet>
			<f:facet name="header">
				<h:outputText value="[header]"></h:outputText>
			</f:facet>
			<f:facet name="footer">
				<h:outputText value="[footer]"></h:outputText>
			</f:facet>
			<h:outputText value="Price:" styleClass="label"></h:outputText>
			<h:outputText value="#{car}" />
			<br />
		</rich:dataDefinitionList>

		<rich:dataDefinitionList var="car" value="#{user.list}" rows="3"
			first="2" title="Cars" styleClass="greenPanel">
			<f:facet name="caption">
					CText1
					<h:outputText value="C1" />
				<h:outputText value="C2" />
				<h:outputText value="C3" />
				<span>CapS1</span> CapT1
			</f:facet>
			<f:facet name="header">
					W1
					<h:outputText value="F1" />
				<h:outputText value="F2" />
				<h:outputText value="F3" />
				<span>S1</span> T1
			</f:facet>
			<f:facet name="term">
                    Term1
					<h:outputText value="T1" />
				<h:outputText value="T2" />
				<h:outputText value="T3" />
				<span>TS1</span> TT1
                </f:facet>
			<f:facet name="myfacet">
					MYF1
					<h:outputText value="MYF-OUT-1" />
				<h:outputText value="MYF-OUT-2" />
				<h:outputText value="MYF-OUT-3" />
				<span>MYF-S1</span> MYF-T1
			</f:facet>
			<f:facet name="footer">
					WW1
					<h:outputText value="FF1" />
				<h:outputText value="FF2" />
				<h:outputText value="FF3" />
				<span>SS1</span> TT1
			</f:facet>
			<h:outputText value="Mileage:" styleClass="label"></h:outputText>
			<h:outputText value="#{car}" />
				myt1
				<h:outputText value="myt1" />
			<h:outputText value="myt2" />
			<h:outputText value="myt3" />
			<span>myt-S1</span> myt-T1
			<br />
		</rich:dataDefinitionList>
	</h:panelGrid>

	<h1>14) rich:inplaceInput</h1>
	<h:panelGrid columns="3" styleClass="panel">
		<rich:inplaceInput value="#{user.name}" showControls="true" />
		<rich:inplaceInput value="#{user.name}" showControls="true">
			<f:facet name="controls">
				<h:panelGroup>
					<h:commandButton value="Save" type="button" />
					<h:commandButton value="Close" type="button" />
				</h:panelGroup>
			</f:facet>
		</rich:inplaceInput>

		<rich:inplaceInput value="#{user.name}" showControls="true">
			<f:facet name="controls">
				<h:commandButton value="Save" type="button" />
				<h:commandButton value="Close" type="button" />
			</f:facet>
		</rich:inplaceInput>

		<rich:inplaceInput value="#{user.name}" showControls="true">
			<f:facet name="controls">
				<h:commandButton value="Save" type="button" />
				<h:commandButton value="Close" type="button" />
			</f:facet>
			myt1
			<h:outputText value="myt1" />
			<h:outputText value="myt2" />
			<h:outputText value="myt3" />
			<span>myt-S1</span> myt-T1
			<br />
		</rich:inplaceInput>

		<rich:inplaceInput value="#{user.name}" showControls="true">
			<f:facet name="controls">
				<h:commandButton value="Save" type="button" />
				<h:commandButton value="Close" type="button" />
			</f:facet>
			<f:facet name="header">
					W1
					<h:outputText value="F1" />
				<h:outputText value="F2" />
				<h:outputText value="F3" />
				<span>S1</span> T1
			</f:facet>
			<f:facet name="term">
                    Term1
					<h:outputText value="T1" />
				<h:outputText value="T2" />
				<h:outputText value="T3" />
				<span>TS1</span> TT1
                </f:facet>
			myt1
			<h:outputText value="myt1" />
			<h:outputText value="myt2" />
			<h:outputText value="myt3" />
			<span>myt-S1</span> myt-T1
			<br />
		</rich:inplaceInput>

		<rich:inplaceInput value="#{user.name}" showControls="true">
			<f:facet name="controls">
					WW1
				<h:outputText value="FF1" />
				<h:outputText value="FF2" />
				<h:outputText value="FF3" />
				<span>SS1</span> TT1
			</f:facet>
		</rich:inplaceInput>

	</h:panelGrid>

<h1> 15) rich:orderingList </h1>

<h:panelGrid columns="3" styleClass="panel">

<rich:orderingList value="#{user.list}" var="item" listHeight="300" listWidth="350">
        	<f:facet name="caption">
	            <h:outputText value="List Caption" />
    	    </f:facet> 
        <rich:column  width="180">
        	<f:facet name="header">
	            <h:outputText value="Song Name" />
    	    </f:facet> 
            <h:outputText value="[You must be evil] #{item}"></h:outputText>
        </rich:column>
        <rich:column>
            <f:facet name="header">
				<h:outputText value="Artist Name" />
            </f:facet>
            <h:outputText value="[Chris Rea] #{item}"></h:outputText>
        </rich:column>
    </rich:orderingList>
    
    <rich:orderingList value="#{user.list}" var="item" controlsHorizontalAlign="left">
        	<f:facet name="heder">
        		CCCW1
				<h:outputText value="CCCF1" />
				<h:outputText value="CCCF2" />
				<h:outputText value="CCCF3" />
				<span>CCCS1</span> CCCT1
    	    </f:facet> 
        	<f:facet name="caption">
        		CCCW1
				<h:outputText value="CCCF1" />
				<h:outputText value="CCCF2" />
				<h:outputText value="CCCF3" />
				<span>CCCS1</span> CCCT1
    	    </f:facet> 
        <rich:column>
        	<f:facet name="header">
        		W1
				<h:outputText value="F1" />
				<h:outputText value="F2" />
				<h:outputText value="F3" />
				<span>S1</span> T1
	            <h:outputText value="Song Name" />
    	    </f:facet> 
            <h:outputText value="[You must be evil] #{item}"></h:outputText>
        </rich:column>
        <rich:column>
            <f:facet name="footer">
            WW1
				<h:outputText value="FF1" />
				<h:outputText value="FF2" />
				<h:outputText value="FF3" />
				<span>SS1</span> TT1
				<h:outputText value="Artist Name" />
            </f:facet>
            <h:outputText value="[Chris Rea] #{item}"></h:outputText>
        </rich:column>
    </rich:orderingList>
    
    <rich:orderingList value="#{user.list}" var="item" >
    <f:facet name="topControl">
    U1
				<h:outputText value="UF1" />
				<h:outputText value="UF2" />
				<h:outputText value="UF3" />
				<span>S1</span> T1
    </f:facet>
    <f:facet name="upControl">
            UU1
				<h:outputText value="UUFF1" />
				<h:outputText value="UUFF2" />
				<h:outputText value="UUFF3" />
				<span>UUSS1</span> UUTT1
            </f:facet>
    <f:facet name="downControl">
    	D1
				<h:outputText value="DF1" />
				<h:outputText value="DF2" />
				<h:outputText value="DF3" />
				<span>DS1</span> DT1
    </f:facet>
    <f:facet name="bottomControl">
    DDW1
				<h:outputText value="DDF1" />
				<h:outputText value="DDF2" />
				<h:outputText value="DDF3" />
				<span>DDS1</span> DDT1
    </f:facet>
        <rich:column>
        	<f:facet name="header">
	            <h:outputText value="Song Name" />
    	    </f:facet> 
            <h:outputText value="[You must be evil] #{item}"></h:outputText>
        </rich:column>
        <rich:column>
            <f:facet name="footer">
            WW1
				<h:outputText value="FF1" />
				<h:outputText value="FF2" />
				<h:outputText value="FF3" />
				<span>SS1</span> TT1
				<h:outputText value="Artist Name" />
            </f:facet>
            <h:outputText value="[Chris Rea] #{item}"></h:outputText>
        </rich:column>
    </rich:orderingList>
    
    
</h:panelGrid>
    
<h1> 16) rich:progressBar </h1>

<h:panelGrid columns="3" styleClass="panel">

    <rich:progressBar value="67" style="color:red" styleClass="btn" mode="client"
			id="progressBar">
			<f:facet name="initial">
				<h:outputText value="Process doesn't started yet" />
			</f:facet>
			<f:facet name="complete">
				<h:outputText value="Process Done" />
			</f:facet>
		</rich:progressBar>
		
    <rich:progressBar value="45">
			<f:facet name="initial">
				W1
				<h:outputText value="F1" />
				<h:outputText value="F2" />
				<h:outputText value="F3" />
				<span>S1</span> T1
			</f:facet>
			<f:facet name="complete">
				WW1
				<h:outputText value="FF1" />
				<h:outputText value="FF2" />
				<h:outputText value="FF3" />
				<span>SS1</span> TT1
			</f:facet>
		</rich:progressBar>

    <rich:progressBar value="30" label="Progres Bar"  style="color:red" styleClass="btn" mode="client">
			<f:facet name="complete">
				WW1
				<h:outputText value="FF1" />
				<h:outputText value="FF2" />
				<h:outputText value="FF3" />
				<span>SS1</span> TT1
			</f:facet>
			<h:outputText> first text </h:outputText>
			<h:outputText> Download speed 100 Mbit/s </h:outputText>
			<h:outputText> last text </h:outputText>
		</rich:progressBar>
		<rich:progressBar value="30" >
		</rich:progressBar>
		<rich:progressBar value="45">
		</rich:progressBar>
</h:panelGrid> </f:view> </body> </html>
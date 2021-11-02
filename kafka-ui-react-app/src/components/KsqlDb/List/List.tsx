import Indicator from 'components/common/Dashboard/Indicator';
import MetricsWrapper from 'components/common/Dashboard/MetricsWrapper';
import PageLoader from 'components/common/PageLoader/PageLoader';
import ListItem from 'components/KsqlDb/List/ListItem';
import React, { FC, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router';
import { fetchKsqlDbTables } from 'redux/actions/thunks/ksqlDb';
import { getKsqlDbTables } from 'redux/reducers/ksqlDb/selectors';
import { clusterKsqlDbQueryPath } from 'lib/paths';
import PageHeading from 'components/common/PageHeading/PageHeading';
import { MetricsContainerStyled } from 'components/common/Dashboard/MetricsContainer.styled';
import TableStyled from 'components/common/table/Table/Table.styled';
import TableHeaderCell from 'components/common/table/TableHeaderCell/TableHeaderCell';
import { Button } from 'components/common/Button/Button';

const headers = [
  { Header: 'Type', accessor: 'type' },
  { Header: 'Name', accessor: 'name' },
  { Header: 'Topic', accessor: 'topic' },
  { Header: 'Key Format', accessor: 'keyFormat' },
  { Header: 'Value Format', accessor: 'valueFormat' },
];

const accessors = headers.map((header) => header.accessor);

const List: FC = () => {
  const dispatch = useDispatch();

  const { clusterName } = useParams<{ clusterName: string }>();

  const { rows, fetching, tablesCount, streamsCount } =
    useSelector(getKsqlDbTables);

  useEffect(() => {
    dispatch(fetchKsqlDbTables(clusterName));
  }, []);

  return (
    <>
      <PageHeading text="KSQL DB">
        <Button
          isLink
          to={clusterKsqlDbQueryPath(clusterName)}
          buttonType="primary"
          buttonSize="M"
        >
          Execute KSQL request
        </Button>
      </PageHeading>
      <MetricsContainerStyled>
        <MetricsWrapper>
          <Indicator label="Tables" title="Tables" fetching={fetching}>
            {tablesCount}
          </Indicator>
          <Indicator label="Streams" title="Streams" fetching={fetching}>
            {streamsCount}
          </Indicator>
        </MetricsWrapper>
      </MetricsContainerStyled>
      <div>
        {fetching ? (
          <PageLoader />
        ) : (
          <TableStyled isFullwidth>
            <thead>
              <tr>
                <th> </th>
                {headers.map(({ Header, accessor }) => (
                  <TableHeaderCell title={Header} key={accessor} />
                ))}
              </tr>
            </thead>
            <tbody>
              {rows.map((row) => (
                <ListItem key={row.name} accessors={accessors} data={row} />
              ))}
              {rows.length === 0 && (
                <tr>
                  <td colSpan={headers.length + 1}>
                    No tables or streams found
                  </td>
                </tr>
              )}
            </tbody>
          </TableStyled>
        )}
      </div>
    </>
  );
};

export default List;

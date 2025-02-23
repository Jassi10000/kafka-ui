import React from 'react';
import { shallow } from 'enzyme';
import SchemaVersion from 'components/Schemas/Details/SchemaVersion';

import { versions } from './fixtures';

describe('SchemaVersion', () => {
  it('renders versions', () => {
    const wrapper = shallow(<SchemaVersion version={versions[0]} />);

    expect(wrapper.find('td').length).toEqual(4);
    expect(wrapper.exists('JSONEditor')).toBeFalsy();
    wrapper.find('.is-clickable').simulate('click');
    expect(wrapper.exists('JSONEditor')).toBeTruthy();
  });

  it('matches snapshot', () => {
    expect(shallow(<SchemaVersion version={versions[0]} />)).toMatchSnapshot();
  });
});

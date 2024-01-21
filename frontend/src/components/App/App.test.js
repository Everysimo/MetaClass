import { render, screen } from '@testing-library/react';
import UnloggedHome from '../../pages/UnloggedHome';

test('renders learn react link', () => {
  render(<UnloggedHome />);
  const linkElement = screen.getByText(/learn react/i);
  expect(linkElement).toBeInTheDocument();
});
